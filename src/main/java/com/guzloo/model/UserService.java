package com.guzloo.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ImageUtils imageUtils;
    @Autowired
    private UserImageRepository userprofilerepo;

    String uploadDir = "uploads/";

    public UserImage processUserProfileImage(String username, MultipartFile file) throws IOException {

        log.info("processUserProfileImage() called for user: {}", username);
        File imageFile;
        String baseName = username.toLowerCase().replaceAll("\\s+", "_");

        if (file != null && !file.isEmpty()) {
            imageFile = imageUtils.resizeProfileImage(file, uploadDir, baseName);
            log.info("profileImage Save With Image: {}", imageFile);
        } else {
            imageFile = imageUtils.generateInitialAvatar(username, uploadDir);
            log.info("profileImage Save Without Image: {}", imageFile);
        }

        // Save metadata to DB
        UserImage userImage = new UserImage();
        userImage.setUsername(username);
        userImage.setImagePath(imageFile.getPath());
        log.info("profileImage Save in DB: {}", imageFile);

        return userprofilerepo.save(userImage);
    }

    //delete user profile pick
    public String deleteUserProfileImage(String username) {
        log.info("deleteUserProfileImage() called for user: {}", username);

        Optional<UserImage> optional = userprofilerepo.findByUsername(username);

        if (optional.isPresent()) {
            UserImage userImage = optional.get();
            log.info("Username found in DB: {}", username);
            // Delete image file if exists
            if (userImage.getImagePath() != null) {
                File file = new File(userImage.getImagePath());
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        log.info("Don't Deleted image file and Failed: {}", file.getAbsolutePath());
                        return "Failed to delete image file.";
                    }
                }
                // Set imagePath to null and update record
                userImage.setImagePath(null);
                userprofilerepo.save(userImage);
                log.info("Image path removed from database for user: {}", username);
                return "Profile image deleted successfully.";
            } else {
                log.warn("No Image was set this user: {}",username);
                return "No image was set for this user.";
            }
        } else {
            log.warn("User not found: {}", username);
            return "User not found.";
        }
    }

    //update profile picture
    public UserImage updateProfilePicture(String username, MultipartFile file) throws IOException {

        log.info("updateProfilePicture() called for user: {}", username);
        if (file == null || file.isEmpty()) {
            log.warn("No profile image provided for update by user: {}", username);
            throw new IllegalArgumentException("Profile image is required for update.");
        }

        // Fetch user profile from db
        UserImage userImage = userprofilerepo.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found while updating profile picture: {}", username);
                    return new RuntimeException("User not found: " + username);
                });

        log.info("User found in DB: {}", username);

        // Delete old image
        if (userImage.getImagePath() != null) {
            File oldFile = new File(userImage.getImagePath());
            if (oldFile.exists()) {
                oldFile.delete();
                log.info("Deleted old profile image: {}", oldFile.getAbsolutePath());
            }
        }

        // Save new image
        String baseName = username.toLowerCase().replaceAll("\\s+", "_");
        File newImage = imageUtils.resizeProfileImage(file, uploadDir, baseName);
        log.info("New profile image saved at: {}", newImage.getAbsolutePath());

        // Update DB
        userImage.setImagePath(newImage.getAbsolutePath());
        log.info("Profile image updated successfully for user: {}", username);
        return userprofilerepo.save(userImage);
    }


    public UserImage cropAndSaveCircularImage(MultipartFile file, int x, int y, int width, int height, String outputDir, String username) throws IOException {
        BufferedImage original = ImageIO.read(file.getInputStream());
        BufferedImage circular = ImageUtils.cropToCircle(original, x, y, width, height);

        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = username.toLowerCase() + "_profile.png";
        File outputFile = new File(dir, fileName);
        ImageIO.write(circular, "png", outputFile);

        // Save or update user image info in DB
        Optional<UserImage> optional = userprofilerepo.findByUsername(username);
        UserImage userImage = optional.orElse(new UserImage());
        userImage.setUsername(username);
        userImage.setImagePath(outputFile.getAbsolutePath());

        return userprofilerepo.save(userImage);
    }

}



