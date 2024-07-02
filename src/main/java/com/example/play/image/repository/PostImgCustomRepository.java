package com.example.play.image.repository;

import com.example.play.image.domain.PostImage;
import com.example.play.post.domain.Post;

import java.util.List;

public interface PostImgCustomRepository {
    List<PostImage> readByPost(Post post);
    List<PostImage> findListForDelete(Post post, List<Long> deleteIds);
}
