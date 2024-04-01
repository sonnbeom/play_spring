package com.example.play.image.service;

import com.example.play.global.common.constant.Bucket;
import com.example.play.global.common.provider.MinioServiceProvider;
import com.example.play.image.dto.ImageDto;
import com.example.play.image.dto.ResponseImg;
import com.example.play.image.entity.PostImage;
import com.example.play.image.exception.MinioUploadException;
import com.example.play.image.mapper.PostImgMapper;
import com.example.play.image.repository.PostImgCustomRepositoryImpl;
import com.example.play.image.repository.PostImgRepository;
import com.example.play.post.entity.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostImgService {
    private final PostImgRepository postImgRepository;
    private final MinioServiceProvider minioServiceProvider;
    private final PostImgCustomRepositoryImpl customRepository;
    private final PostImgMapper postImgMapper;

    public List<ResponseImg> savePostImage(List<MultipartFile> fileList, Post post){
        List<PostImage> postImages = new ArrayList<>();

        for (MultipartFile file : fileList){
            ImageDto imageDto = minioServiceProvider.uploadImage(Bucket.POST, file);
            if (imageDto.getStatus().equals(ImageDto.Status.UPLOADED)){
                PostImage postImage = PostImage.builder()
                            .isActive(1)
                            .post(post)
                            .url(imageDto.getPath())
                            .build();
                postImages.add(postImage);
            }else {
                throw new MinioUploadException("게시물 이미지 업로드에 실패하였습니다");
            }
        }

        List<PostImage> postImagesList = postImgRepository.saveAll(postImages);
        return postImgMapper.entityToDto(postImagesList);
    }
    public List<ResponseImg> readImages(Post post){
        List<PostImage> postImages = customRepository.readByPost(post);
        return postImgMapper.entityToDto(postImages);
    }

    public List<ResponseImg> update(Post post, List<Long> deleteImageList, List<MultipartFile> files) {
        if (deleteImageList != null && !deleteImageList.isEmpty()){
            List<PostImage> listImg = customRepository.findListForDelete(post, deleteImageList);
            for (PostImage img: listImg){
                img.changeStatus();
            }
        }
        saveUpdateImg(files, post);
        return readImages(post);
    }
    private void saveUpdateImg (List<MultipartFile> fileList, Post post){
        List<PostImage> postImages = new ArrayList<>();

        for (MultipartFile file : fileList){
            ImageDto imageDto = minioServiceProvider.uploadImage(Bucket.POST, file);
            if (imageDto.getStatus().equals(ImageDto.Status.UPLOADED)){
                PostImage postImage = PostImage.builder()
                        .isActive(1)
                        .post(post)
                        .url(imageDto.getPath())
                        .build();
                postImages.add(postImage);
            }else {
                throw new MinioUploadException("이미지가 업로드에 실패하였습니다");
            }
        }
        postImgRepository.saveAll(postImages);
    }

    public void deleteImg(Post post) {
        List<PostImage> postImages = customRepository.readByPost(post);
        for (PostImage img : postImages){
            img.changeStatus();
        }
    }
}
