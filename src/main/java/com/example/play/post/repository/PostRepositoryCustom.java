package com.example.play.post.repository;

import com.example.play.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<Post> findBySort(Pageable pageable, String type);
    Page<Post> findBySearch(Pageable pageable, String type, String keyword);

}
