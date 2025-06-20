package com.guzloo.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserImageRepository userImageRepository;


    // save profile image
    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("username") String username,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        log.info("uploadProfileImage() called with username: {}", username);
        try {
            UserImage saved = userService.processUserProfileImage(username, file);
            log.info("Profile image processed successfully for user: {}", username);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("Failed to upload profile image for user: {} — {}", username, e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Delete profile image
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(@RequestParam("username") String username) {
        log.info("deleteProfile() called with username: {}", username);
        String msg = userService.deleteUserProfileImage(username);
        return ResponseEntity.ok(msg);
    }

    //update profile image
    @PutMapping("/update-profile-image")
    public ResponseEntity<?> updateProfileImage(
            @RequestParam("username") String username,
            @RequestParam("file") MultipartFile file
    ) {
        log.info("updateProfileImage() called with username: {}", username);
        try {
            UserImage updated = userService.updateProfilePicture(username, file);
            log.info("Updated profile image file for user: {}", username);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Failed to updateProfileImage profile image for user: {} — {}", username, e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Preview profile Image api
    @GetMapping("/image/{username}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String username) throws IOException {
        log.info("getProfileImage() called with username: {}", username);
        Optional<UserImage> optional = userImageRepository.findByUsername(username);

        if (optional.isPresent()) {
            UserImage userImage = optional.get();
            String imagePath = userImage.getImagePath();

            if (imagePath != null) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_PNG);
                    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
                }
            }
        }
        log.warn("username not found : {}", username);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/upload-crop")
    public ResponseEntity<?> uploadCroppedImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("x") int x,
            @RequestParam("y") int y,
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("username") String username
    ) throws IOException {
        UserImage saved = userService.cropAndSaveCircularImage(file, x, y, width, height, "uploads/", username);
        return ResponseEntity.ok("Profile image saved at: " + saved.getImagePath());
    }

    //Download profile image
    @GetMapping("/profile-image-download/{username}")
    public ResponseEntity<Resource> downloadProfileImage(@PathVariable String username) {
        //find user in db
        UserImage userImage = userImageRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imagePath = userImage.getImagePath();
        if (imagePath == null || imagePath.isEmpty()) {
            throw new RuntimeException("No profile image available for this user.");
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new RuntimeException("Image file not found on server.");
        }

        try {
            Path path = imageFile.toPath();
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + imageFile.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading image", e);
        }
    }


}
