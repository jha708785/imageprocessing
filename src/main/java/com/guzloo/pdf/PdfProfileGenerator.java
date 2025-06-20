package com.guzloo.pdf;

import com.guzloo.model.UserImage;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PdfProfileGenerator {

    public static byte[] generateUserProfilePdf(UserImage user, File profileImageFile) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();


        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Username
        document.add(new Paragraph("User Profile").setBold().setFontSize(18));
        document.add(new Paragraph("Username: " + user.getUsername()).setFontSize(14));

        // Profile Image
        if (profileImageFile != null && profileImageFile.exists()) {
            ImageData imageData = ImageDataFactory.create(profileImageFile.getAbsolutePath());
            Image image = new Image(imageData);
            image.setWidth(256);
            image.setHeight(256);
            document.add(image);
        } else {
            document.add(new Paragraph("No profile image uploaded."));
        }

        document.close();
        return out.toByteArray();
    }
}
