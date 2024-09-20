package com.money.reaper;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class QRCodeDecoderFromURL {

    public static void main(String[] args) {
        String urlString = "https://merchant.upigateway.com/gateway/pay/1a10560548d2d2ecccfac1f9e646b4b5";  // URL of the HTML page

        try {
            // Fetch the HTML from the provided URL
            Document doc = fetchHTML(urlString);

            // Extract the base64 string from the img tag
            String base64Image = extractBase64FromHTML(doc);

            // Check if a base64 image was found
            if (base64Image != null) {
                // Decode the base64 string into byte array
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                // Convert byte array into BufferedImage
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                BufferedImage bufferedImage = ImageIO.read(bis);

                // Prepare the BinaryBitmap for decoding
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(
                                new BufferedImageLuminanceSource(bufferedImage)
                        )
                );

                // Decode the QR code
                Result result = new MultiFormatReader().decode(binaryBitmap);

                // Print the decoded text
                System.out.println("Decoded text: " + result.getText());
            } else {
                System.out.println("Base64 image not found in the HTML.");
            }

        } catch (NotFoundException e) {
            System.out.println("QR Code not found in the image.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch HTML content from the URL
    private static Document fetchHTML(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Parse the HTML content using Jsoup
        return Jsoup.parse(connection.getInputStream(), "UTF-8", urlString);
    }

    // Extract the base64 image from the <img> tag with class="qr_code_img"
    private static String extractBase64FromHTML(Document doc) {
        // Find the <img> tag with the base64 QR code
        Element imgTag = doc.selectFirst("img.qr_code_img");
        if (imgTag != null) {
            String imgSrc = imgTag.attr("src");
            if (imgSrc != null && imgSrc.startsWith("data:image/png;base64,")) {
                // Remove the "data:image/png;base64," prefix
                return imgSrc.substring("data:image/png;base64,".length());
            }
        }
        return null;
    }
}
