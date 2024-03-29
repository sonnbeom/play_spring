package com.example.play.global.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("$(minio.secretKey)")
    private String secretKey;

    @Value("${minio.endpoint.url}")
    private String minioUrl;

    @Bean
    public MinioClient createMinioClient(){
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }


}
