package com.example.play.image.service;

import com.example.play.global.common.constant.Bucket;
import com.example.play.global.common.provider.MinioServiceProvider;
import com.example.play.image.dto.ImageDto;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.entity.MemberImage;
import com.example.play.image.exception.MinioUploadException;
import com.example.play.image.mapper.MemberImgMapper;
import com.example.play.image.repository.MemberImgCustomRepository;
import com.example.play.image.repository.MemberImgRepository;
import com.example.play.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberImgService {
    private final MinioServiceProvider minioServiceProvider;
    private final MemberImgRepository memberImgRepository;
    private final MemberImgMapper memberImgMapper;
    private final MemberImgCustomRepository memberImgCustomRepository;

    public ResponseMemberImg findMemberImageByMember(Member member) {
        List<MemberImage> img = memberImgCustomRepository.findByMember(member);
        if (img.isEmpty() && img.size() != 1){
            return new ResponseMemberImg().builder()
                    .status(ResponseMemberImg.Status.DEFAULT)
                    .build();
        }
        else {
            return memberImgMapper.entityToDto(img);
        }
    }

    public void saveMemberImg(Member member, MultipartFile profile) {
        ImageDto imageDto = minioServiceProvider.uploadImage(Bucket.MEMBER, profile);
        if (imageDto.getStatus().equals(ImageDto.Status.UPLOADED)){
            MemberImage memberImage = MemberImage.builder()
                    .isActive(1)
                    .member(member)
                    .url(imageDto.getPath())
                    .build();
            memberImgRepository.save(memberImage);
        }else {
            throw new MinioUploadException("멤버 이미지 업로드에 실패하였습니다");
        }
    }
}
