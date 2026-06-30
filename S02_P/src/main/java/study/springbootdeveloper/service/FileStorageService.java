package study.springbootdeveloper.service;

import study.springbootdeveloper.dto.UploadResponse;

public interface FileStorageService {

    UploadResponse store(byte[] bytes, String filename);
}
