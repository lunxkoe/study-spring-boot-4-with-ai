package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import study.springbootdeveloper.dto.UploadResponse;
import study.springbootdeveloper.service.FileStorageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/api/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileStorageService.store(file.getBytes(), file.getOriginalFilename()));
    }
}
