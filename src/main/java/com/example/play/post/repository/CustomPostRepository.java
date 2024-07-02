package com.example.play.post.repository;

import com.example.play.member.domain.Member;
import com.example.play.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface CustomPostRepository {
    Page<Post> findBySort(Pageable pageable, String type);
    Page<Post> findBySearch(Pageable pageable, String type, String keyword);

    Page<Post> findLikedPosts(Member member, Pageable pageable);
}
