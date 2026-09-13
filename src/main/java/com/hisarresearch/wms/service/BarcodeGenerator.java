package com.hisarresearch.wms.service;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
@Transactional
public class BarcodeGenerator{

    private static final Font BARCODE_TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    public static BufferedImage generateUPCABarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createUPCA(barcodeText); //checksum is automatically added
        barcode.setFont(BARCODE_TEXT_FONT);
        barcode.setResolution(400);

        return BarcodeImageHandler.getImage(barcode);
    }

    public  BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createEAN13(barcodeText); //checksum is automatically added
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

    public static BufferedImage generateCode128BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createCode128(barcodeText);
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

    public static BufferedImage generatePDF417BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createPDF417(barcodeText);
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

}
