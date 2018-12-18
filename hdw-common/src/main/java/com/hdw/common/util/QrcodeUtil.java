package com.hdw.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.UUID;


/**
 * @author TuMinglong
 * @description 二维码工具类
 * @date 2018年1月25日 上午11:21:16
 */
public class QrcodeUtil {

    private static Logger logger = LoggerFactory.getLogger(QrcodeUtil.class);

    public static String createQrcode(String dir, String content) {
        return createQrcode(dir, content, 300, 300);
    }

    public static String createQrcode(String dir, String content, int width, int height) {
        try {

            File dirFile = null;
            if (StringUtils.isNotBlank(dir)) {
                dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
            }

            String qrcodeFormat = "png";
            HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);


            File file = new File(dir, UUID.randomUUID().toString() + "." + qrcodeFormat);
            MatrixToImageWriter.writeToPath(bitMatrix, qrcodeFormat, file.toPath());
            return file.getAbsolutePath();
        } catch (Exception e) {
            logger.error("", e);
        }
        return "";
    }

    public static String decodeQr(String filePath) {
        String retStr = "";
        if ("".equalsIgnoreCase(filePath) && filePath.length() == 0) {
            return "图片路径为空!";
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            HashMap<DecodeHintType, Object> hintTypeObjectHashMap = new HashMap<>();
            hintTypeObjectHashMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(bitmap, hintTypeObjectHashMap);
            retStr = result.getText();
        } catch (Exception e) {
            logger.error("", e);
        }
        return retStr;
    }
}
