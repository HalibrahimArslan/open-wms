package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Upload;
import com.hisarresearch.wms.service.dto.AurProcessHtmlRequestDto;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tech.jhipster.config.JHipsterProperties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class MinioService {

    @Value("${wms.branding.logo-url:}")
    private String logoUrl;

    @Value("${wms.mail.shipment-form-url:}")
    private String shipmentFormUrl;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.url}")
    private String url;

    private final Logger log = LoggerFactory.getLogger(MinioService.class);
    private final String SERVICE_NAME = "MinioService";
    private static final String WMS = "wms";
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final JHipsterProperties jHipsterProperties;
    private final AurLogService aurLogService;
    private final MinioClient minioClient;

    private final TranslationService translationService;

    private final AurLogService logService;

    private final UploadService uploadService;

    private final UserService userService;

    public MinioService(JavaMailSender javaMailSender, TemplateEngine templateEngine,
                        JHipsterProperties jHipsterProperties,AurLogService aurLogService,
                        TranslationService translationService,MinioClient minioClient,
                        AurLogService logService,UploadService uploadService,
                        UserService userService) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.jHipsterProperties = jHipsterProperties;
        this.aurLogService = aurLogService;
        this.translationService = translationService;
        this.minioClient = minioClient;
        this.logService = logService;
        this.uploadService = uploadService;
        this.userService = userService;
    }

    public Upload uploadObject(MultipartFile file) {
        long logId = 0;
        StringBuilder updatedFilename = new StringBuilder();
        try {
            logId = logService.logRequest("MinioService","uploadObject",file.toString());
            long epochTimeMillis = System.currentTimeMillis();
            updatedFilename.append(epochTimeMillis)
                .append("-")
                .append(file.getOriginalFilename());
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(updatedFilename.toString())
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build());

            Upload savedUpload = uploadService.saveMinioObject(updatedFilename.toString(),url,bucketName);
            logService.logResponse(logId,"Process is completed");
            return savedUpload;
        } catch (Exception e) {
            logService.logResponse(logId,e.getMessage());
            log.error(e.getMessage());
            throw new RuntimeException("Error occurred while uploading file");
        }
    }

    public void listBucketItems(){
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) throw new RuntimeException("Bucket " + bucketName + "doesnt found");

            Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());

            for (Result<Item> result : results) {
                Item item = result.get();
                System.out.println(item.lastModified() + "\t" + item.size() + "\t" + item.objectName());
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }

    }

    public InputStream downloadFile(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        long logId = logService.logRequest(SERVICE_NAME,"downloadFile",fileName);
        InputStream stream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build()
        );
        logService.logResponse(logId,stream.toString());
        return stream;
    }

    public void sendMailWithObject(String attachmentName, List<AurProcessHtmlRequestDto> aphr, String templateName,String to,String objectName) {
        long logId = 0;
        String orderType;
        String subject;
        try {
            logId = aurLogService.logRequest("MinioService","sendMailWithObject",aphr.toString() + templateName + attachmentName);

            InputStream stream =
                minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(objectName).build());

            Locale locale = Locale.forLanguageTag("tr");
            Context context = new Context(locale);
            context.setVariable("logoUrl", logoUrl);
            context.setVariable("shipmentFormUrl", shipmentFormUrl);
            context.setVariable(WMS, aphr);


            if (aphr.get(0).getSipTip().equals("FMK")) {
                orderType = "MAL KABUL";
            } else {
                orderType = "SEVKIYAT";
            }

            String content = templateEngine.process(templateName, context);

            if (templateName.equals("mail/invoiceinformation")) {
                subject = orderType + " " + aphr.get(0).getCariAdi() + "-" + aphr.get(0).getAltCariAdi();

            } else {
                subject = orderType + " " + aphr.get(0).getIrsaliyeNumarasi() + " " + aphr.get(0).getCariAdi();

            }

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setFileName(attachmentName);
            attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(stream, "application/octet-stream")));
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(content, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(attachmentPart);
            mimeMessage.setText(content);
            mimeMessage.setFrom(jHipsterProperties.getMail().getFrom());
            mimeMessage.setSubject(subject);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setContent(multipart);

            javaMailSender.send(mimeMessage);
            stream.close();

            aurLogService.logResponse(logId,"Mail gönderimi başarıyla tamamlandı");
        } catch (Exception e) {
            aurLogService.logResponse(logId,e.getMessage());
            throw new RuntimeException(translationService.getErrorMessage("minioService.sendMailWithObject") + e.getMessage());

        }
    }
}
