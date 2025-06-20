package com.guzloo.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnails;
import java.io.*;


@Component
public class ImageUtils {


    public static BufferedImage cropToCircle(BufferedImage original, int x, int y, int width, int height) {

        // Clamp values to avoid RasterFormatException
        x = Math.max(0, x);
        y = Math.max(0, y);
        width = Math.min(width, original.getWidth() - x);
        height = Math.min(height, original.getHeight() - y);

        BufferedImage cropped = original.getSubimage(x, y, width, height);

        BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        // Antialiasing for smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Circle clipping
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, width, height));
        g2.drawImage(cropped, 0, 0, null);
        g2.dispose();

        return circleBuffer;
    }


    public File resizeProfileImage(MultipartFile file, String uploadDir, String baseName) throws IOException {

        // Validate image file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file type");
        }

        // Read the original profile_image
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("Could not read image content.");
        }

        // convert to PNG and prepare for resizing
        ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "png", pngStream);
        InputStream pngInputStream = new ByteArrayInputStream(pngStream.toByteArray());


        // ensure upload directory exists
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // output file (PNG format, 256x256) it is suitable for profile image
        String fileName = baseName + "_profile.png";
        File outputFile = new File(dir, fileName);

        // resize using Thumbnailator
        Thumbnails.of(originalImage)
                .size(1024, 1024)
                .outputFormat("png")
                .toFile(outputFile);

        return outputFile;
    }


   /* public void handleImageToResize(MultipartFile file, String uploadDir, String baseName) throws IOException {

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file type");
        }

        // Read original image
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("Could not read image content.");
        }

        // Convert to PNG and prepare for resizing
        ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "png", pngStream);
        InputStream pngInputStream = new ByteArrayInputStream(pngStream.toByteArray());

        // Sizes and labels
        int[] sizes = {64, 128, 256};
        String[] labels = {"small", "medium", "large"};

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int i = 0; i < sizes.length; i++) {
            String fileName = baseName + "_" + labels[i] + ".png";
            File outputFile = new File(dir, fileName);

            // Reset stream for each resize
            InputStream freshStream = new ByteArrayInputStream(pngStream.toByteArray());

            Thumbnails.of(freshStream)
                    .size(sizes[i], sizes[i])
                    .outputFormat("png")
                    .toFile(outputFile);

            freshStream.close();
        }

        pngInputStream.close();
    }*/


    // if user not upload image then automatic generate image
    public File generateInitialAvatar(String username, String uploadDir) throws IOException {
        int width = 300;
        int height = 300;
        String letter = username.substring(0, 1).toUpperCase();

        // Create blank image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // Enable anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //  background color
        Color backgroundColor = getColorForAlphabet(letter);
        g2.setColor(backgroundColor);
        g2.fillOval(0, 0, width, height);


        // Text settings
        g2.setFont(new Font("Arial", Font.BOLD, 120));
        g2.setColor(Color.WHITE);

        // Center text
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(letter)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();

        g2.drawString(letter, x, y);
        g2.dispose();

        // Save image
        String fileName = "avatar_" + UUID.randomUUID() + ".png";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Create parent directories
        }

        File outputFile = new File(uploadDir + File.separator + fileName);
        ImageIO.write(image, "png", outputFile);
        return outputFile;
    }


    private Color getColorForAlphabet(String letter) {

        if (letter == null || letter.isEmpty()) {
            return new Color(158, 158, 158); // Default grey
        }

        char ch = Character.toUpperCase(letter.charAt(0));

        Map<Character, Color> alphabetColors = Map.ofEntries(
                Map.entry('A', new Color(255, 87, 34)),   // Deep Orange
                Map.entry('B', new Color(76, 175, 80)),   // Green
                Map.entry('C', new Color(33, 150, 243)),  // Blue
                Map.entry('D', new Color(156, 39, 176)),  // Purple
                Map.entry('E', new Color(0, 150, 136)),   // Teal
                Map.entry('F', new Color(255, 193, 7)),   // Amber
                Map.entry('G', new Color(63, 81, 181)),   // Indigo
                Map.entry('H', new Color(139, 195, 74)),  // Light Green
                Map.entry('I', new Color(233, 30, 99)),   // Pink
                Map.entry('J', new Color(0, 188, 212)),   // Cyan
                Map.entry('K', new Color(121, 85, 72)),   // Brown
                Map.entry('L', new Color(255, 152, 0)),   // Orange
                Map.entry('M', new Color(96, 125, 139)),  // Blue Grey
                Map.entry('N', new Color(158, 158, 158)), // Grey
                Map.entry('O', new Color(205, 220, 57)),  // Lime
                Map.entry('P', new Color(103, 58, 183)),  // Deep Purple
                Map.entry('Q', new Color(3, 169, 244)),   // Light Blue
                Map.entry('R', new Color(244, 67, 54)),   // Red
                Map.entry('S', new Color(0, 150, 136)),   // Teal
                Map.entry('T', new Color(255, 87, 34)),   // Deep Orange
                Map.entry('U', new Color(76, 175, 80)),   // Green
                Map.entry('V', new Color(33, 150, 243)),  // Blue
                Map.entry('W', new Color(156, 39, 176)),  // Purple
                Map.entry('X', new Color(0, 188, 212)),   // Cyan
                Map.entry('Y', new Color(255, 152, 0)),   // Orange
                Map.entry('Z', new Color(96, 125, 139))   // Blue Grey
        );

        return alphabetColors.getOrDefault(ch, new Color(158, 158, 158)); // Default grey
    }


}
