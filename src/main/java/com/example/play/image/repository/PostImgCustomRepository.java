package com.example.play.image.repository;

import com.example.play.image.entity.PostImage;
import com.example.play.post.entity.Post;

import java.util.List;

public interface PostImgCustomRepository {
    List<PostImage> readByPost(Post post);
    List<PostImage> findListForDelete(Post post, List<Long> deleteIds);
}
