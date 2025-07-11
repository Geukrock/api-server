package com.geukrock.geukrockapiserver.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class ImageStore {
    @Value("${file.upload-path}")
    private String uploadDirPath;

    public String store(MultipartFile file){
        if (file == null || file.isEmpty() ) {
            return null;
        }
        
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDirPath, fileName);
        try{
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e){
            throw new RuntimeException("이미지 파일 저장 실패", e);
        }
        return fileName;
    }

    public void delete(String imageUrl){
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Path path = Paths.get(uploadDirPath, imageUrl);
            try{
                Files.deleteIfExists(path);
            } catch(IOException e){
                throw new RuntimeException("이미지 파일 삭제 실패");
            }
        }
    }
}
