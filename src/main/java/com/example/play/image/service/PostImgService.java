package com.example.play.image.service;

import com.example.play.global.common.constant.Bucket;
import com.example.play.global.common.provider.MinioServiceProvider;
import com.example.play.image.dto.ImageDto;
import com.example.play.image.entity.PostImage;
import com.example.play.image.exception.MinioUploadException;
import com.example.play.image.repository.PostImgRepository;
import com.example.play.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImgService {
    private final PostImgRepository postImgRepository;
    private final MinioServiceProvider minioServiceProvider;

    public List<String> savePostImage(List<MultipartFile> fileList, Post post){
        List<PostImage> postImages = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : fileList){
            ImageDto imageDto = minioServiceProvider.uploadImage(Bucket.Post, file);
            if (imageDto.getStatus().equals(ImageDto.Status.UPLOADED)){
                PostImage postImage = PostImage.builder()
                            .isActive(1)
                            .post(post)
                            .url(imageDto.getPath())
                            .build();
                postImages.add(postImage);
                urls.add(imageDto.getPath());
            }else {
                throw new MinioUploadException("이미지가 업로드에 실패하였습니다");
            }
        }
        postImgRepository.saveAll(postImages);
        return urls;
    }
}
