// 3. FileStorageService에 삭제(delete) 기능을 추가합니다.
package com.novelplatform.novelsite.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new IllegalStateException("업로드 경로를 생성할 수 없습니다.", e);
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // 원본 파일명에서 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String filename = UUID.randomUUID() + "." + extension;

        Path destinationFile = this.rootLocation.resolve(filename);
        try {
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            // WebConfig에서 설정한 /uploads/ 경로를 반환
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("파일을 저장할 수 없습니다.", e);
        }
    }

    public void delete(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            // filePath (예: /uploads/filename.png) 에서 실제 파일명(filename.png) 추출
            String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
            Path file = this.rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            // 로그를 남기는 것이 좋지만, 일단 예외를 던지지 않고 넘어갑니다.
            System.err.println("파일 삭제에 실패했습니다: " + filePath);
        }
    }
}
