package com.example.play.image.service;

import com.example.play.image.dto.ResponseImg;
import com.example.play.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostImgService {

    // 게시글 이미지 저장
    List<ResponseImg> savePostImage(List<MultipartFile> fileList, Post post);

    // 게시글과 일치하는 이미지 불러오기
    List<ResponseImg> readImages(Post post);

    // 게시글 이미지 변경
    List<ResponseImg> update(Post post, List<Long> deleteImageList, List<MultipartFile> files);

    // 게시글 이미지 삭제
    void deleteImg(Post post);
}
