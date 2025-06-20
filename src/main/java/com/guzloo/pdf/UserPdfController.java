package com.guzloo.pdf;

import com.guzloo.model.UserImage;
import com.guzloo.model.UserImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/users")
public class UserPdfController {

    @Autowired
    private UserImageRepository repository;


    @GetMapping("/profile-pdf/{username}")
    public ResponseEntity<byte[]> downloadProfilePdf(@PathVariable String username) {
        try {
            UserImage user = repository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            File imageFile = user.getImagePath() != null ? new File(user.getImagePath()) : null;

            byte[] pdfBytes = PdfProfileGenerator.generateUserProfilePdf(user, imageFile);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(username + "_profile.pdf").build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }
}
