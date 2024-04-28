package com.example.play.image.exceptionhandler;

import com.example.play.image.exception.FileExtensionException;
import com.example.play.image.exception.MemberImgException;
import com.example.play.image.exception.MinioUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ImageExceptionHandler {
    @ExceptionHandler(FileExtensionException.class)
    public ResponseEntity<String> handleFileExtensionException(FileExtensionException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(MemberImgException.class)
    public ResponseEntity<String> handleMemberImgException(MemberImgException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    @ExceptionHandler(MinioUploadException.class)
    public ResponseEntity<String> handleMinioUploadException(MinioUploadException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}