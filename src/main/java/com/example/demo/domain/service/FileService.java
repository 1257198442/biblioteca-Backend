package com.example.demo.domain.service;

import com.example.demo.domain.exceptions.MaxUploadSizeExceededException;
import com.example.demo.domain.exceptions.UnprocessableEntityException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileService {

    private static final Set<String> COMMON_IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpeg", "jpg", "png", "gif", "bmp", "tiff", "webp", "svg", "ico",
            "JPEG", "JPG", "PNG", "GIF", "BMP", "TIFF", "WEBP", "SVG", "ICO"
    ));

    public void fileSizeTooLarge(MultipartFile file, int size){
        long maxFileSize = size * 1024;
        long fileSize = file.getSize();
        if(fileSize > maxFileSize){
            throw new MaxUploadSizeExceededException("Avatar files size cannot be larger than"+size+"KB");
        };
    }

    public void isImageType(MultipartFile file) throws IOException {
        String fileContentType = new Tika().detect(file.getInputStream(), file.getOriginalFilename());
        String[] imageTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif",
                "image/bmp", "image/tiff", "image/webp", "image/svg+xml",
                "image/x-icon"};
        if(!Arrays.asList(imageTypes).contains(fileContentType)){
            throw new UnprocessableEntityException("File is not an image.");
        }
    }

    public String fileWrite(String filePackage,String field,String id,MultipartFile file) throws IOException {
            String fileName = this.generateFileName(field+id,file);
            Path path = Paths.get(filePackage + fileName);
            Files.write(path, file.getBytes());
            return fileName;
    }

    public void fileDelete(String filePackage,String fileName) throws IOException {
        Path path = Paths.get(filePackage + fileName);
        Files.delete(path);
    }

    public String generateFileName(String field,MultipartFile file) {
        return field+"."+this.getExtension(file);
    }

    public String getExtension(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new UnprocessableEntityException("File is empty or null");
        }
        String fileName = file.getOriginalFilename();
        return isImageExtension(fileName);
    }

    public String isImageExtension(String fileName) {
        int lastDotIndex;
        if (fileName != null) {
            lastDotIndex = fileName.lastIndexOf('.');
        }else {
            throw new UnprocessableEntityException("File has no extension.");
        }
        String extension = lastDotIndex != -1 ? fileName.substring(lastDotIndex + 1) : "";
        if(COMMON_IMAGE_EXTENSIONS.contains(extension)){
          return extension;
        } else {
          throw new UnprocessableEntityException("File is not an image.");
        }
    }

}