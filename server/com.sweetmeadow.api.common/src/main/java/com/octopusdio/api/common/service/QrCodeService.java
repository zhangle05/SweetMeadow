/**
 * 
 */
package com.octopusdio.api.common.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author zhangle
 *
 */
@Service
public class QrCodeService extends AbstractBaseService {

    private static final int DEFAULT_WIDTH = 200;

    private static final int DEFAULT_HEIGHT = 200;

    /**
     * generate qr-code to output stream for the given text
     *
     * @param text
     * @param os
     * @throws WriterException
     * @throws IOException
     */
    public void generateQrCode(String text, OutputStream os)
            throws IOException {
        LOG.info("generating QR code for:" + text);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix bitMatrix;
        try {
            bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,
                    DEFAULT_WIDTH, DEFAULT_HEIGHT, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", os);
        } catch (WriterException e) {
            throw new IOException(e);
        }
    }
}
