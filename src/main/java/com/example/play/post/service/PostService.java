package com.example.play.post.service;

import com.example.play.post.dto.*;
import com.example.play.post.domain.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    // 게시글 생성
    ResponsePostOne create(RequestPostDto postDto, List<MultipartFile> files, String email);

    // 게시글 불러오기
    ResponsePostOne readOne(Long postId);

    // postId로 게시글 가져오기
    Post findById(Long postId);

    // 게시글을 분류타입에 따라 가져오기 (좋아요순, 조회순, 최신순 등등)
    ResponsePostDTo readBySort(int page, String sortType);

    //게시글을 검색하여 가져오기
    ResponsePostDTo readBySearch(int page, String type, String keyword);

    // 게시글 업데이트
    ResponsePostOne update(Long postId ,
                           RequestUpdatePostDto updateDto,
                           List<MultipartFile> files,
                           List<Long> deleteImageList,
                           String email);

    // 게시글 삭제하기
    ResponseDeletePostDTo delete(Long postId, String email);

    // 내가 좋아요한 게시글 가져오기
    ResponsePostDTo getLikedPosts(String email, int page);
}
