package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.enumeration.FirmConnectionType;
import com.hisarresearch.wms.domain.enumeration.OrderStatus;
import com.hisarresearch.wms.repository.*;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.AurOrderDetailDTO;
import com.hisarresearch.wms.service.dto.AurOrderMasterDTO;
import com.hisarresearch.wms.service.dto.AurProcessHtmlRequestDto;
import com.hisarresearch.wms.service.dto.mail.ZReportDTO;
import com.hisarresearch.wms.service.dto.base.ResponseDto;
import com.hisarresearch.wms.service.dto.sms.ShippingSmsDTO;
import com.hisarresearch.wms.service.erp.MikroServices;
import com.hisarresearch.wms.utility.AurHelper;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.hisarresearch.wms.framework.config.JHipsterProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final String USER = "user";
    private static final String WMS = "wms";
    private static final String BASE_URL = "baseUrl";
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    /** Mail sablonlarinda gosterilecek logo adresi. Bos birakilirsa logo basilmaz. */
    @Value("${wms.branding.logo-url:}")
    private String logoUrl;

    /** Sevkiyat bilgilendirme formunun adresi. Bos birakilirsa ilgili blok basilmaz. */
    @Value("${wms.mail.shipment-form-url:}")
    private String shipmentFormUrl;

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private AurLogService aurLogService;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @Autowired
    private AurGroupMailAddressService aurGroupMailAddressService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private AurMailAddToService aurMailAddToService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private AurLookupService aurLookupService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private MikroServices mikroServices;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private Environment environment;

    @Autowired
    private ZReportService zReportService;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }


    @Async
    public void sendEmailBulk(List<String> to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            String[] mailTo = to.toArray(String[]::new);
            message.setTo(mailTo);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailBulkWithCcAndBcc(List<String> to, List<String> cc, List<String> bcc, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' cc '{}' with subject '{}' and content={}", isMultipart, isHtml, to, cc, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            String[] mailTo = to.toArray(String[]::new);
            String[] mailCc = cc.toArray(String[]::new);
            String[] mailBcc = bcc.toArray(String[]::new);
            message.setTo(mailTo);
            message.setCc(mailCc);
            message.setBcc(mailBcc);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        applyBrandingVariables(context);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplateClone(List<String> to, List<AurProcessHtmlRequestDto> aphr, String templateName) {
        Locale locale = Locale.forLanguageTag("tr");
        Context context = new Context(locale);
        applyBrandingVariables(context);
        context.setVariable(WMS, aphr);

        String orderType;
        if (aphr.get(0).getSipTip().equals("FMK")) {
            orderType = "ÜRÜN KABUL";
        } else {
            orderType = "SEVKİYAT";
        }

        String content = templateEngine.process(templateName, context);
        String subject = orderType + " " + aphr.get(0).getCariAdi() + "-" + aphr.get(0).getAltCariAdi();

        sendEmailBulk(to, subject, content, false, true);
    }

    @Async
    public void sendNotFoundEmail(String to, String orderNo, String cariName, String altCariName, String templateName) {
        Locale locale = Locale.forLanguageTag("tr");
        Context context = new Context(locale);
        applyBrandingVariables(context);
        HashMap<String, String> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("cariName", cariName);
        params.put("altCariName", altCariName);
        context.setVariable(WMS, params);

        String content = templateEngine.process(templateName, context);
        String subject = orderNo;

        sendEmail(to, subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplateCloneWithCcAndBcc(List<String> to, List<String> cc, List<String> bcc, List<AurProcessHtmlRequestDto> aphr, String templateName) {
        Locale locale = Locale.forLanguageTag("tr");
        Context context = new Context(locale);
        applyBrandingVariables(context);
        context.setVariable(WMS, aphr);

        String orderType;
        if (aphr.get(0).getSipTip().equals("FMK")) {
            orderType = "ÜRÜN KABUL";
        } else {
            orderType = "SEVKİYAT";
        }

        String content = templateEngine.process(templateName, context);
        String subject = orderType + " " + aphr.get(0).getCariAdi() + "-" + aphr.get(0).getAltCariAdi();

        sendEmailBulkWithCcAndBcc(to, cc, bcc, subject, content, false, true);
    }

    @Async
    public void sendZReport(List<String> to, List<AurVwZReport> zReportList) {
        zReportList.forEach(item -> {
            item.setAltCariAdi("");
            if (item.getBaglantiTipi().equals("4")) {
                String[] splitTex = item.getFirmName().split("-");
                if (splitTex[0].length() != item.getFirmName().length()) {
                    item.setFirmName(splitTex[0]);
                    item.setAltCariAdi(splitTex[1]);
                }
            }
            item.setFirmName(maskString(item.getFirmName(), 10));
            item.setAltCariAdi(maskString(item.getAltCariAdi(), 10));
        });

        Locale locale = Locale.forLanguageTag("tr");
        Context context = new Context(locale);
        applyBrandingVariables(context);
        ZReportDTO zReportDTO = new ZReportDTO();
        zReportDTO.setReceivingOrderList(zReportList.stream().filter(q -> q.getOpType().equals("FMK")).collect(Collectors.toList()));
        zReportDTO.setDispatchOrderList(zReportList.stream().filter(q -> q.getOpType().equals("MSK")).collect(Collectors.toList()));

        context.setVariable(WMS, zReportDTO);

        String content = templateEngine.process("mail/zreport", context);
        String subject = "Z Raporu";
        sendEmailBulk(to, subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    private void applyBrandingVariables(Context context) {
        context.setVariable("logoUrl", logoUrl);
        context.setVariable("shipmentFormUrl", shipmentFormUrl);
    }

    @Async
    public void sendProcessNotificationMail(List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto) {
        Long logId = aurLogService.logRequest("MailService", "sendProcessNotificationMail", aurProcessHtmlRequestDto.toString());
        List<AurProcessHtmlRequestDto> maskedTemplate = maskMailTemplate(aurProcessHtmlRequestDto);
        sendEmailFromTemplateClone(aurProcessHtmlRequestDto.get(0).getStokMailGrup(), maskedTemplate, "mail/processNotification");
        aurLogService.logResponse(logId, "Send Mail Process Is Done");
    }

    @Async
    public void sendCustomerMail(List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto, List<String> bcc) {
        List<String> cc = new ArrayList<>();
        Long logId = aurLogService.logRequest("MailService", "sendCustomerMail", aurProcessHtmlRequestDto.toString());
        List<AurProcessHtmlRequestDto> maskedTemplate = maskMailTemplate(aurProcessHtmlRequestDto);
        sendEmailFromTemplateCloneWithCcAndBcc(maskedTemplate.get(0).getStokMailGrup(), cc, bcc, maskedTemplate, "mail/invoiceinformation");
        aurLogService.logResponse(logId, "Send Mail To Customer Process Is Done");
    }

    @Async
    public void sendNotFoundMail(String orderNo, String cariName, String altCariName) {
        List<AurLookupTable> notFoundMail = aurLookupService.getByLookupName("MAIL_NOT_FOUND");

        if (notFoundMail.isEmpty()) {
            return;
        }

        Long logId = aurLogService.logRequest("MailService", "sendNotFoundMail", orderNo);
        sendNotFoundEmail(notFoundMail.get(0).getLookupCode(), orderNo, cariName, altCariName, "mail/notFound");
        aurLogService.logResponse(logId, "Send Not Found Mail Is Done");
    }

    @Async
    public void sendVendorNotFoundMail(String orderNo, String cariName, String altCariName) {
        List<AurLookupTable> notFoundMail = aurLookupService.getByLookupName("MAIL_NOT_FOUND");

        if (notFoundMail.isEmpty()) {
            return;
        }

        Long logId = aurLogService.logRequest("MailService", "sendVendorNotFoundMail", orderNo);
        sendNotFoundEmail(notFoundMail.get(0).getLookupCode(), orderNo, cariName, altCariName, "mail/vendorNotFound");
        aurLogService.logResponse(logId, "Send Not Found Mail Is Done");
    }


    @Async
    public void sendVendorMail(List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto, List<String> cc, List<String> bcc) {
        Long logId = aurLogService.logRequest("MailService", "sendVendorMail", aurProcessHtmlRequestDto.toString());
        List<AurProcessHtmlRequestDto> maskedTemplate = maskMailTemplate(aurProcessHtmlRequestDto);
        sendEmailFromTemplateCloneWithCcAndBcc(maskedTemplate.get(0).getStokMailGrup(), cc, bcc, maskedTemplate, "mail/invoiceinformation");
        aurLogService.logResponse(logId, "Send Mail To Vendor Process Is Done");
    }

    public List<String> discernStockCodes(List<AurOrderDetail> aod) {
        AurHelper aurHelper = new AurHelper();
        List<String> distinctStockCodes = aod.stream().filter(aurHelper.distinctByKey(AurOrderDetail::getStokKodu)).map(AurOrderDetail::getStokKodu).collect(Collectors.toList());
        List<String> response = new ArrayList<>();

        for (String item : distinctStockCodes) {
            String[] splintedText = item.split("\\.");
            response.add(splintedText[0].concat("."));
        }

        return response.stream().distinct().collect(Collectors.toList());
    }

    public List<String> discernStockCodesAppointed(List<AurOrderDetailDTO> aod) {
        AurHelper aurHelper = new AurHelper();
        List<String> distinctStockCodes = aod.stream().filter(aurHelper.distinctByKey(AurOrderDetailDTO::getStokKodu)).map(AurOrderDetailDTO::getStokKodu).collect(Collectors.toList());
        List<String> response = new ArrayList<>();

        for (String item : distinctStockCodes) {
            String[] splintedText = item.split("\\.");
            response.add(splintedText[0].concat("."));
        }

        return response.stream().distinct().collect(Collectors.toList());
    }

    @Transactional
    public void sendMailVersion2(Long orderId) {
        long logId = aurLogService.logRequest("sendMail", "sendMailVersion2", String.valueOf(orderId));
        List<AurLookupTable> mailStatus = aurLookupService.getByLookupName("MAIL_STATUS");

        if (!mailStatus.isEmpty() && mailStatus.get(0).getLookupCode().equals("false")) {
            return;
        }
        AurOrderMaster orderMaster = aurOrderMasterService.findWithDetailsById(orderId).orElseThrow(() -> {
            aurLogService.logResponse(logId, "Invalid order");
            return new InvalidOrderException();
        });

        List<AurOrderDetail> details = new ArrayList<>(orderMaster.getDetails());
        List<String> uniqueStockGroupCodes = discernStockCodes(details);
        List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto = mapperToMailRequest(orderMaster);
        sendMailToPublicGroup(orderMaster);
        sendMailByFirmConnectionType(orderMaster);
        List<AurLookupTable> smsStatus = aurLookupService.getByLookupName("SMS_SEND_IS_ACTIVE");
        if (smsStatus != null && !smsStatus.isEmpty() && smsStatus.get(0).getLookupCode().equals("1")) {
            sendSms(aurProcessHtmlRequestDto);
        }


        List<String> mailAddresses = uniqueStockGroupCodes.stream()
            .flatMap(stock -> getMailAddressByStockCode(stock).stream())
            .distinct()
            .collect(Collectors.toList());

        for (String address : mailAddresses) {
            if (!aurProcessHtmlRequestDto.isEmpty()) {
                aurProcessHtmlRequestDto.get(0).setStokMailGrup(Collections.singletonList(address));
                sendProcessNotificationMail(aurProcessHtmlRequestDto);
            }
        }

        if (!mailAddresses.isEmpty()) {
            aurLogService.logResponse(logId, orderMaster.toString());
        }

    }

    @Async
    @Transactional
    public void sendAppointedOrderMail(AurOrderMasterDTO dto) {
        long logId = aurLogService.logRequest("sendMail", "sendAppointedOrderMail", dto.getBelgeNo());
        List<AurLookupTable> mailStatus = aurLookupService.getByLookupName("MAIL_STATUS");

        if (!mailStatus.isEmpty() && mailStatus.get(0).getLookupCode().equals("false")) {
            return;
        }

        List<String> uniqueStockGroupCodes = discernStockCodesAppointed(dto.getAurTmpDetailList());

        List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto = mapperToMailRequestAppointed(dto);
        List<String> mailAddresses = uniqueStockGroupCodes.stream()
            .flatMap(stock -> getMailAddressByStockCode(stock).stream())
            .distinct()
            .collect(Collectors.toList());

        for (String address : mailAddresses) {
            if (!aurProcessHtmlRequestDto.isEmpty()) {
                aurProcessHtmlRequestDto.get(0).setStokMailGrup(Collections.singletonList(address));
                sendAppointedOrderMail(aurProcessHtmlRequestDto);
            }
        }

        if (!mailAddresses.isEmpty()) {
            aurLogService.logResponse(logId, dto.toString());
        }

    }

    @Transactional
    public void sendMailToPublicGroup(AurOrderMaster orderMaster) {
        long logId = aurLogService.logRequest("sendMail", "sendMailToPublicGroup", orderMaster.toString());

        List<AurProcessHtmlRequestDto> mailRequestDto = mapperToMailRequest(orderMaster);
        List<String> mailAddresses = getAllPublicMail();
        MailList globalMailLists = ruleService.executeRulesForMail(orderMaster, "receivingMail");
        if (globalMailLists != null && globalMailLists.getMailList() != null) {
            mailAddresses.addAll(globalMailLists.getMailList());
        }


        if (!mailRequestDto.isEmpty()) {
            mailRequestDto.get(0).setStokMailGrup(mailAddresses);
            List<AurProcessHtmlRequestDto> maskedTemplate = maskMailTemplate(mailRequestDto);
            sendProcessNotificationMail(maskedTemplate);
            aurLogService.logResponse(logId, mailRequestDto.toString());
        }

    }

    @Async
    public void sendAppointedOrderMail(List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto) {
        Long logId = aurLogService.logRequest("MailService", "sendProcessNotificationMail", aurProcessHtmlRequestDto.toString());
        List<AurProcessHtmlRequestDto> maskedTemplate = maskMailTemplateAppointed(aurProcessHtmlRequestDto);
        sendEmailFromTemplateCloneAppointed(aurProcessHtmlRequestDto.get(0).getStokMailGrup(), maskedTemplate, "mail/appointedOrder");
        aurLogService.logResponse(logId, "Send Mail Process Is Done");
    }

    @Async
    public void sendEmailFromTemplateCloneAppointed(List<String> to, List<AurProcessHtmlRequestDto> aphr, String templateName) {
        Locale locale = Locale.forLanguageTag("tr");
        Context context = new Context(locale);
        applyBrandingVariables(context);
        context.setVariable(WMS, aphr);

        String orderType = "ATANAN";
        String content = templateEngine.process(templateName, context);
        String subject = orderType + " " + aphr.get(0).getCariAdi() + "-" + aphr.get(0).getAltCariAdi();
        sendEmailBulk(to, subject, content, false, true);
    }


    public List<AurProcessHtmlRequestDto> maskMailTemplateAppointed(List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto) {
        aurProcessHtmlRequestDto.forEach(item -> {
            String[] splitText = item.getAtanan().split(" "); //String soforAdSoyad = item.getSoforAdSoyad() != null ? item.getSoforAdSoyad() : "";
            if (splitText[0].length() == item.getAtanan().length()) {
                //String atananAd = maskString(splitText[0], 100);
                item.setAtanan(splitText[0].concat(" BEY"));
            } else {
                item.setAtanan(splitText[0].concat(" BEY"));
            }
            item.setCariAdi(maskString(item.getCariAdi(), 10));
            item.setAltCariAdi(maskString(item.getAltCariAdi(), 10));

        });
        return aurProcessHtmlRequestDto;
    }


    public void sendMailByFirmConnectionType(AurOrderMaster order) {
        MailList globalMailLists = ruleService.executeRulesForMail(order, "dispatcherMail");
        if (order.getOpType().equals("MSK")) {
            if (order.getBaglantiTipi().equals(FirmConnectionType.VENDOR.getValue()) || environment.acceptsProfiles("dev")) {
                sendMailToVendor(order, globalMailLists);
                return;
            }
            sendMailToCustomer(order, globalMailLists);
        }
    }

    public void sendMailToVendor(AurOrderMaster order, MailList globalMailList) {
        long logId = aurLogService.logRequest("sendMailToVendor", "sendMailToVendor", order.toString());
        List<String> to = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        List<AurProcessHtmlRequestDto> mailRequestDto = mapperToMailRequest(order);
        List<String> cc = customerService.getByCustomerCode(order.getFirmCode())
            .stream()
            .map(Customer::getMail)
            .collect(Collectors.toList());

        customerService.getByDistrictCode(Integer.parseInt(order.getBolgeKodu())).forEach(vendorMailAddress -> {
            if (!cc.contains(vendorMailAddress.getMail())) {
                cc.add(vendorMailAddress.getMail());
            }
        });

        if (cc.isEmpty()) {
            aurLogService.logResponse(logId, "Bayi mail adresi bulunamadı" + order.getId());
            if (!mailRequestDto.isEmpty()) {
                AurProcessHtmlRequestDto firstItem = mailRequestDto.get(0);
                sendVendorNotFoundMail(firstItem.getSiparisNumarasi(), firstItem.getCariAdi(), firstItem.getAltCariAdi());
            }

            return;
        }


        if (!mailRequestDto.isEmpty()) {
            ResponseDto responseDto = findCustomerMailAddress(mailRequestDto);
            if (responseDto.getSuccess().equals("false")) {
                return;
            }
            if (responseDto.getData() != null) {
                LinkedHashMap<String, String> mail = (LinkedHashMap<String, String>) responseDto.getData();
                to.add(mail.get("orderMail"));
            }

            if (globalMailList != null && globalMailList.getMailList() != null) {
                bcc.addAll(globalMailList.getMailList());
            }

            mailRequestDto.get(0).setStokMailGrup(to);
            sendVendorMail(mailRequestDto, cc, bcc);
            aurLogService.logResponse(logId, mailRequestDto.toString());
        }
    }

    public void sendMailToCustomer(AurOrderMaster order, MailList globalMailList) {
        long logId = aurLogService.logRequest("sendMailToCustomer", "sendMailToCustomer", order.toString());

        List<AurProcessHtmlRequestDto> mailRequestDto = mapperToMailRequest(order);
        List<String> senderGroup = new ArrayList<>();
        List<String> bcc = new ArrayList<>();

        if (!mailRequestDto.isEmpty()) {
            ResponseDto responseDto = findCustomerMailAddress(mailRequestDto);
            if (responseDto.getSuccess().equals("false")) {
                aurLogService.logResponse(logId, "sendMailToCustomer servisinde problem meydana geldi.");
                return;
            }

            Object data = responseDto.getData();
            if (data != null) {
                LinkedHashMap<String, String> mail = (LinkedHashMap<String, String>) responseDto.getData();
                senderGroup.add(mail.get("orderMail"));
            }

            if (globalMailList != null && globalMailList.getMailList() != null) {
                bcc.addAll(globalMailList.getMailList());
            }

            mailRequestDto.get(0).setStokMailGrup(senderGroup);
            sendCustomerMail(mailRequestDto, bcc);

            aurLogService.logResponse(logId, mailRequestDto.toString());
        }
    }


    @Transactional
    public List<String> getAllPublicMail() {
        List<String> mailAddress = new ArrayList<>();
        List<AurMailAddTo> aurMailAddTo = aurMailAddToService.findAll();
        aurMailAddTo.forEach(aurMailAddTo1 -> mailAddress.add(aurMailAddTo1.getMailAdres()));

        return mailAddress;
    }

    @Transactional
    public List<String> getMailAddressByStockCode(String stockCode) {
        String[] splintedText = stockCode.split("\\.");
        Optional<AurGrupMailAdres> grupMailAdres = aurGroupMailAddressService.findByGrupKodu(Integer.valueOf(splintedText[0]));
        List<String> mailAddress = new ArrayList<>();
        grupMailAdres.ifPresent(aurGrupMailAdres -> mailAddress.add(aurGrupMailAdres.getMailAdres()));

        return mailAddress;

    }

    public List<AurProcessHtmlRequestDto> mapperToMailRequest(AurOrderMaster orderMaster) {
        List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto = new ArrayList<>();
        List<AurOrderDetail> mailDetails = orderMaster.getDetails()
            .stream()
            .filter(detail -> !detail.getPiece() && detail.getStatus().equals(OrderStatus.DONE.name()))
            .collect(Collectors.toList());
        char[] characters = {'K', 'k'};
        int counter = 1;
        AurHelper helper = new AurHelper();

        for (AurOrderDetail item : mailDetails) {
            if (helper.containsAnyCharacter(item.getSiparisNo(), characters)) {
                continue;
            }
            AurProcessHtmlRequestDto dto = new AurProcessHtmlRequestDto();
            dto.setId(counter);
            dto.setIrsaliyeTarihi(orderMaster.getLastModifiedDate());
            dto.setIrsaliyeNumarasi(orderMaster.getBelgeNo());
            dto.setKayitNo(orderMaster.getOrderInfo());
            Optional<Warehouse> depo = warehouseService.findByCode(String.valueOf(orderMaster.getDepoNo()));
            depo.ifPresentOrElse(depo1 -> dto.setDepo(orderMaster.getDepoNo().toString().concat("-").concat(depo1.getName())), () -> dto.setDepo(orderMaster.getDepoNo().toString().concat("")));
            dto.setSipTip(orderMaster.getOpType());
            dto.setSiparisNumarasi(item.getSiparisNo());
            dto.setCariKod(orderMaster.getFirmCode());
            if (orderMaster.getBaglantiTipi().equals("4") && orderMaster.getFirmName().contains("-")) {
                String[] splitText = orderMaster.getFirmName().split("-");
                if (splitText.length > 0) {
                    dto.setCariAdi(splitText[0]);
                }
                if (splitText.length > 1) {
                    dto.setAltCariAdi(splitText[1]);
                }
            } else {
                dto.setCariAdi(orderMaster.getFirmName());
                dto.setAltCariAdi("");
            }
            dto.setStokKodu(item.getStokKodu());
            dto.setStokAdi(item.getStokAdi());
            dto.setSiparisMiktari(item.getSiparisMiktar().toString());
            dto.setTeslimMiktari(item.getObserverAmount().toString());
            dto.setKalanMiktar(String.valueOf((item.getSiparisMiktar() - item.getObserverAmount())));
            dto.setSevkiyatAdresi("");
            dto.setIrtibatTel(orderMaster.getSoforTel());
            dto.setSoforAdSoyad(orderMaster.getSoforAdi());
            dto.setSoforPlaka(orderMaster.getSoforPlaka());
            dto.setSoforTc(orderMaster.getSoforTcNo());
            dto.setSevkiyatAdresi(customerAddressService.getSevkAddressFromOrder(orderMaster.getId()));
            dto.setMusteriTel(customerAddressService.getSevkMusteriTel(orderMaster.getId()));
            dto.setSipUid(item.getSipUid());


            aurProcessHtmlRequestDto.add(dto);

            counter++;
        }

        return aurProcessHtmlRequestDto;
    }

    public List<AurProcessHtmlRequestDto> mapperToMailRequestAppointed(AurOrderMasterDTO masterDTO) {
        List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto = new ArrayList<>();
        int counter = 1;

        for (AurOrderDetailDTO item : masterDTO.getAurTmpDetailList()) {

            AurProcessHtmlRequestDto dto = new AurProcessHtmlRequestDto();
            dto.setId(counter);
            dto.setIrsaliyeTarihi(masterDTO.getCreatedDate());
            dto.setIrsaliyeNumarasi(masterDTO.getBelgeNo());
            dto.setKayitNo(masterDTO.getOrderInfo());
            Optional<Warehouse> depo = warehouseService.findByCode(String.valueOf(masterDTO.getDepoNo()));
            depo.ifPresentOrElse(depo1 -> dto.setDepo(masterDTO.getDepoNo().toString().concat("-").concat(depo1.getName())), () -> dto.setDepo(masterDTO.getDepoNo().toString().concat("")));
            dto.setSipTip(masterDTO.getOpType());
            dto.setSiparisNumarasi(item.getSiparisNo());
            dto.setCariKod(masterDTO.getFirmCode());
            if (masterDTO.getCariBaglantiTipi().equals("4") && masterDTO.getFirmName().contains("-")) {
                String[] splitText = masterDTO.getFirmName().split("-");
                if (splitText.length > 0) {
                    dto.setCariAdi(splitText[0]);
                }
                if (splitText.length > 1) {
                    dto.setAltCariAdi(splitText[1]);
                }
            } else {
                dto.setCariAdi(masterDTO.getFirmName());
                dto.setAltCariAdi("");
            }
            dto.setStokKodu(item.getStokKodu());
            dto.setStokAdi(item.getStokAdi());
            dto.setSiparisMiktari(item.getSiparisMiktar().toString());
            dto.setTeslimMiktari(item.getTeslimMiktar().toString());
            dto.setKalanMiktar(String.valueOf((item.getSiparisMiktar() - item.getTeslimMiktar())));
            dto.setAtanan(userService.findById(masterDTO.getAurUserId()).getLogin());
            dto.setSevkiyatAdresi(customerAddressService.getSevkAddressFromOrder(masterDTO.getId()));
            dto.setMusteriTel(customerAddressService.getSevkMusteriTel(masterDTO.getId()));
            dto.setSipUid(item.getSipUid());


            aurProcessHtmlRequestDto.add(dto);

            counter++;
        }

        return aurProcessHtmlRequestDto;
    }

    public void getObjectFromS3AndSendEmail(Long orderId) {
        long logId = aurLogService.logRequest("mailService", "getObjectFromS3AndSendEmail", orderId.toString());
        AurOrderMaster orderMaster = aurOrderMasterService.findWithDetailsById(orderId).orElseThrow(() -> {
            aurLogService.logResponse(logId, "Invalid order");
            return new InvalidOrderException();
        });

        List<AurProcessHtmlRequestDto> mailRequestDto = mapperToMailRequest(orderMaster);
        minioService.sendMailWithObject("halil", mailRequestDto,
            "mail/invoiceinformation",
            "test@gmail.com",
            "06Aralık.pdf");

        aurLogService.logResponse(logId, orderMaster.toString());

    }

    public List<AurProcessHtmlRequestDto> maskMailTemplate(List<AurProcessHtmlRequestDto> aurProcessHtmlRequestDto) {
        aurProcessHtmlRequestDto.forEach(item -> {
            String[] splitText = item.getSoforAdSoyad().split(" "); //String soforAdSoyad = item.getSoforAdSoyad() != null ? item.getSoforAdSoyad() : "";
            if (splitText[0].length() == item.getSoforAdSoyad().length()) {
                String soforAdSoyAd = maskString(splitText[0], 6);
                item.setSoforAdSoyad(soforAdSoyAd.concat(" BEY"));
            } else {
                item.setSoforAdSoyad(splitText[0].concat(" BEY"));
            }
            item.setCariAdi(maskString(item.getCariAdi(), 10));
            item.setAltCariAdi(maskString(item.getAltCariAdi(), 10));

        });
        return aurProcessHtmlRequestDto;
    }

    public String maskString(String input, int maskedSize) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder maskedString = new StringBuilder();
        if (maskedSize > input.length()) {
            maskedSize = input.length() / 2;
        }
        maskedString.append(input.substring(0, maskedSize));
        for (int i = maskedSize; i < input.length(); i++) {
            maskedString.append('*');
        }
        return maskedString.toString();
    }

    public ResponseDto findCustomerMailAddress(List<AurProcessHtmlRequestDto> mailRequestDto) {
        String sipUid = mailRequestDto.get(0).getSipUid();
        String orderNo = mailRequestDto.get(0).getSiparisNumarasi();
        String cariName = mailRequestDto.get(0).getCariAdi();
        String altCariName = mailRequestDto.get(0).getAltCariAdi();
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = "";
        ResponseDto mailResponse = new ResponseDto();
        try {
            token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        } catch (Exception e) {
            mailResponse.setSuccess("false");
            return mailResponse;
        }
        ResponseDto responseDto = mikroServices.getCustomerMailInfo(token, aurCompanyDto.getApiEndPoint(), orderNo);
        if (responseDto.getSuccess().equals("true")) {
            mailResponse.setSuccess("true");
            List<String> data = (List<String>) responseDto.getData();
            if (data.size() == 0) {
                sendNotFoundMail(orderNo, cariName, altCariName);
            } else {
                mailResponse.setData(data.get(0));
            }
            return mailResponse;

        }
        mailResponse.setSuccess("false");
        return mailResponse;
    }

    public ResponseDto sendSms(List<AurProcessHtmlRequestDto> mailRequestDto) {

        ShippingSmsDTO sms = new ShippingSmsDTO();
        AurHelper aurHelper = new AurHelper();
        List<String> distinctOrderNo = mailRequestDto.stream().filter(aurHelper.distinctByKey(AurProcessHtmlRequestDto::getSiparisNumarasi)).map(AurProcessHtmlRequestDto::getSiparisNumarasi).collect(Collectors.toList());
        StringBuilder orderNoSb = new StringBuilder();
        int index = 0;
        for (String orderStr : distinctOrderNo) {
            if (index > 0) {
                orderNoSb.append(",");
            }
            String orderArray[] = orderStr.split("-");
            orderNoSb.append(orderArray[1]);
            index++;
        }
        Optional.ofNullable(mailRequestDto)
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("mailRequestDto listesi boş yada gelmedi!"));
        sms.setDriverName(mailRequestDto.get(0).getSoforAdSoyad());
        sms.setLicensePlate(mailRequestDto.get(0).getSoforPlaka());
        sms.setOrderNo(orderNoSb.toString());
        sms.setReceiverPhone(mailRequestDto.get(0).getMusteriTel());
        sms.setDriverPhone(mailRequestDto.get(0).getIrtibatTel());
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = "";
        ResponseDto mailResponse = new ResponseDto();
        try {
            token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
            mikroServices.getCustomerSmsInfo(token, aurCompanyDto.getApiEndPoint(), sms);
        } catch (Exception e) {
            mailResponse.setSuccess("false");
            return mailResponse;
        }

        return null;
    }
}
