package com.example.play.image.repository;

import com.example.play.image.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImgRepository extends JpaRepository<MemberImage, Long> {
}
