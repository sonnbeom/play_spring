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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostImgServiceImpl implements PostImgService{
    private final PostImgRepository postImgRepository;
    private final MinioServiceProvider minioServiceProvider;
    private final PostImgCustomRepositoryImpl customRepository;
    private final PostImgMapper postImgMapper;


    @Override
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
                throw new MinioUploadException("게시물 이미지 업로드에 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        List<PostImage> postImagesList = postImgRepository.saveAll(postImages);
        return postImgMapper.entityToDto(postImagesList);
    }

    @Override
    public List<ResponseImg> readImages(Post post){
        List<PostImage> postImages = customRepository.readByPost(post);
        return postImgMapper.entityToDto(postImages);
    }

    @Override
    public List<ResponseImg> update(Post post, List<Long> deleteImageList, List<MultipartFile> files) {
        // 이미지 삭제
        if (!ObjectUtils.isEmpty(deleteImageList)){
            List<PostImage> listImg = customRepository.findListForDelete(post, deleteImageList);
            for (PostImage img: listImg){
                img.changeStatus();
            }
        }
        // 새로 올라오는 이미지 저장
        savePostImage(files, post);
        // 기존 이미지, 추가된 이미지, 삭제된 이미지 반영해서 이미지 리턴
        return readImages(post);
    }

    @Override
    public void deleteImg(Post post) {
        List<PostImage> postImages = customRepository.readByPost(post);
        for (PostImage img : postImages){
            img.changeStatus();
        }
    }
}
