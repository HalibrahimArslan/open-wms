package com.hisarresearch.wms.service.pdf.impl;

import com.hisarresearch.wms.service.pdf.PdfGenerateService;
import com.lowagie.text.DocumentException;
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

    @Autowired
    private TemplateEngine templateEngine;

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
}
