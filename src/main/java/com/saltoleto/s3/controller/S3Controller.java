package com.saltoleto.s3.controller;

import com.saltoleto.s3.service.S3TemplateService;
import io.awspring.cloud.s3.S3Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3TemplateService s3TemplateService;

    @PostMapping("/bucket")
    public ResponseEntity<String> createBucket(
            @RequestParam("bucketName") String bucketName) {
        try {
            s3TemplateService.createBucketWithS3Template(bucketName);
            return ResponseEntity.ok("bucket created successfully .");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create bucket with main reason: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("bucketName") String bucketName,
                                             @RequestParam("key") String key,
                                             @RequestBody Map<String, String> body) {
        try {
            s3TemplateService.uploadObjectWithS3Template(bucketName, key, body);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("bucketName") String bucketName,
                                             @RequestParam("key") String key) {
        try {
            s3TemplateService.deleteObjectWithS3Template(bucketName, key);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }

    @GetMapping("/read")
    public ResponseEntity<S3Resource> readFileFromS3(@RequestParam("bucketName") String bucketName,
                                                     @RequestParam("key") String key) {
        return ResponseEntity.ok().body(s3TemplateService.readFileFromS3WithS3Template(bucketName, key));
    }

}
