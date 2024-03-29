package com.example.play.image.repository;

import com.example.play.image.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImgRepository extends JpaRepository<PostImage, Long> {
}
