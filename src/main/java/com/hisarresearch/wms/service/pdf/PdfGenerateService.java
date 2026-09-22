package com.hisarresearch.wms.service.pdf;
import com.lowagie.text.DocumentException;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.util.Map;

public interface PdfGenerateService {
    InputStreamResource downloadPdf(String templateName, Map<String, Object> data) throws IOException, DocumentException;

}
