package com.hisarresearch.wms.service.pdf.impl;

import com.hisarresearch.wms.service.pdf.PdfGenerateService;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfGenerateServiceImpl implements PdfGenerateService {

    private final Logger logger = LoggerFactory.getLogger(PdfGenerateServiceImpl.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pdf.directory}")
    private String pdfDirectory;

    /** Cikti sablonlarinda gosterilecek logo adresi. Bos birakilirsa logo basilmaz. */
    @Value("${wms.branding.logo-url:}")
    private String logoUrl;

    @Override
    public InputStreamResource downloadPdf(String templateName, Map<String, Object> data) throws IOException, DocumentException {
        Locale locale = Locale.forLanguageTag("tr");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Context context = new Context(locale);
        context.setVariable("logoUrl", logoUrl);
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(baos);
        baos.close();

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
        return resource;

    }

    @Override
    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        Locale locale = Locale.forLanguageTag("tr");
        Context context = new Context(locale);
        context.setVariable("logoUrl", logoUrl);
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfDirectory + pdfFileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
