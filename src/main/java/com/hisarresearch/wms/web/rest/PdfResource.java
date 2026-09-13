package com.hisarresearch.wms.web.rest;


import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.repository.AurOrderMasterRepository;
import com.hisarresearch.wms.repository.AurOrderDetailRepository;
import com.hisarresearch.wms.service.BarcodeGenerator;
import com.hisarresearch.wms.service.BarcodeService;
import com.hisarresearch.wms.service.pdf.PdfGenerateService;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class PdfResource {

    private final PdfGenerateService pdfGenerateService;
    private final AurOrderMasterRepository aurOrderMasterRepository;
    private final AurOrderDetailRepository aurOrderDetailRepository;
    private final BarcodeService barcodeService;
    private final BarcodeGenerator barcodeGenerator;

    public PdfResource(PdfGenerateService pdfGenerateService, AurOrderMasterRepository aurOrderMasterRepository, AurOrderDetailRepository aurOrderDetailRepository, BarcodeService barcodeService, BarcodeGenerator barcodeGenerator) {
        this.pdfGenerateService = pdfGenerateService;
        this.aurOrderMasterRepository = aurOrderMasterRepository;
        this.aurOrderDetailRepository = aurOrderDetailRepository;
        this.barcodeService = barcodeService;
        this.barcodeGenerator = barcodeGenerator;
    }

    @GetMapping("/generatepdf/{pdfName}/{orderId}")
    public void generatePdf(@PathVariable String pdfName,@PathVariable Long orderId){
        Map<String, Object> data = new HashMap<>();
        Optional<AurOrderMaster> aurOrderMaster = aurOrderMasterRepository.findByOrderInfo("AUR-".concat(orderId.toString()));
        if(aurOrderMaster.isPresent()){
            AurOrderMaster aom = aurOrderMaster.get();
            data.put("aurOrderMaster",aom);
            List<AurOrderDetail> aurOrderDetails = aurOrderDetailRepository.findByOrderId(aurOrderMaster.get().getId());
            data.put("aurTmpList", aurOrderDetails);
        }
        pdfGenerateService.generatePdfFile("mail/quotation", data, pdfName.concat(".pdf"));
    }

    @GetMapping("/generate-pdf/{orderId}")
    public ResponseEntity<InputStreamResource> generatePdfDownload(@PathVariable Long orderId) throws Exception{
        Map<String, Object> data = new HashMap<>();
        Optional<AurOrderMaster> aurOrderMaster = aurOrderMasterRepository.findById(orderId);
        if(aurOrderMaster.isPresent()){
            AurOrderMaster aom = aurOrderMaster.get();
            data.put("aurOrderMaster",aom);
            List<AurOrderDetail> aurOrderDetails = aurOrderDetailRepository.findByOrderId(aurOrderMaster.get().getId());
            data.put("aurTmpList", aurOrderDetails);
        }


        InputStreamResource inputStreamResource = pdfGenerateService.downloadPdf("mail/quotation",data);
        Date now = new Date();
        String fileName = "identity_cards_" + now;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Description", "File Transfer");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Content-Transfer-Encoding", "binary");
        headers.add("Connection", "Keep-Alive");
        headers.add("Content-Type", "application/pdf");
        return ResponseEntity.ok().headers(headers).body(inputStreamResource);



    }

    @GetMapping("/mikro-barcode/{stokKod}")
    public ResponseEntity<InputStreamResource> generateBarcodePdf(@PathVariable String stokKod) throws Exception{
        Map<String, Object> data = new HashMap<>();
        Object response = barcodeService.generateBarcodePdf(stokKod);
        BufferedImage bufferedImage = barcodeGenerator.generateEAN13BarcodeImage("978020137962");
        ImageIO.write(bufferedImage,"png",new File("tmpImage.png"));
        byte[] imageBytes = Files.readAllBytes(Paths.get("tmpImage.png"));
        Base64.Encoder encoder = Base64.getEncoder();

        String encoding = "data:image/png;base64," + encoder.encodeToString(imageBytes);
        ArrayList<Integer> size = new ArrayList<>();
        size.add(2);
        size.add(4);
        if(response != null){
            data.put("response",response);
            data.put("size",size);
            data.put("buffer",encoding);

        }
        else{
            throw new BadRequestAlertException("Response is null","PdfResource","Mikro Barkod Pdf Error");
        }

        InputStreamResource inputStreamResource = pdfGenerateService.downloadPdf("mail/mikrobarcode",data);
        Date now = new Date();
        String fileName = "identity_cards_" + now;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Description", "File Transfer");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Content-Transfer-Encoding", "binary");
        headers.add("Connection", "Keep-Alive");
        headers.add("Content-Type", "application/pdf");
        return ResponseEntity.ok().headers(headers).body(inputStreamResource);
    }

    @GetMapping(value = "/barbecue/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@PathVariable("barcode") String barcode) throws Exception {
        return okResponse(barcodeGenerator.generateEAN13BarcodeImage(barcode));
    }

    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

}
