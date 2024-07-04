package com.example.play.global.common.provider;


import com.example.play.image.dto.ImageDto;
import com.example.play.image.exception.FileExtensionException;
import io.minio.MinioClient;

import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

import static com.example.play.image.dto.ImageDto.Status.FAIL;
import static com.example.play.image.dto.ImageDto.Status.UPLOADED;

@Log4j2
@RequiredArgsConstructor
@Component
public class MinioServiceProvider {
    private final MinioClient minioClient;

    @Value("${minio.allowed.file_extension}")
    private List<String> allowedFileExtensions;


    public ImageDto uploadImage(String bucket, MultipartFile file){
        String path = "/" + UUID.randomUUID() + "/" + file.getOriginalFilename();
        extensionCheck(file);

        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();

            minioClient.putObject(args);

            return ImageDto.builder()
                    .status(UPLOADED)
                    .path(path)
                    .build();
        }
        catch (Exception e) {
            // 미니오 exception을 만들어서 그걸 던지자!
            log.info("fileUploadException{}", e.getMessage());
            e.printStackTrace();
            return ImageDto.builder()
                    .status(FAIL)
                    .build();
        }
    }
    private void extensionCheck(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!allowedFileExtensions.contains(extension)){
            throw new FileExtensionException("file extension exception", HttpStatus.BAD_REQUEST);
        }
    }
}
