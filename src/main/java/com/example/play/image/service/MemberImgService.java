package com.example.play.image.service;

import com.example.play.global.common.constant.Bucket;
import com.example.play.global.common.provider.MinioServiceProvider;
import com.example.play.image.dto.ImageDto;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.entity.MemberImage;
import com.example.play.image.exception.MemberImgException;
import com.example.play.image.exception.MinioUploadException;
import com.example.play.image.mapper.MemberImgMapper;
import com.example.play.image.repository.MemberImgCustomRepository;
import com.example.play.image.repository.MemberImgRepository;
import com.example.play.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberImgService {
    private final MinioServiceProvider minioServiceProvider;
    private final MemberImgRepository memberImgRepository;
    private final MemberImgMapper memberImgMapper;
    private final MemberImgCustomRepository memberImgCustomRepository;

    public ResponseMemberImg findByMember(Member member) {
        List<MemberImage> img = memberImgCustomRepository.findByMember(member);
        if (img.isEmpty() && img.size() != 1){
            return new ResponseMemberImg().builder()
                    .status(ResponseMemberImg.Status.DEFAULT)
                    .build();
        }
        else {
            MemberImage memberImage = img.get(0);
            return memberImgMapper.entityToDto(memberImage);
        }
    }

    public MemberImage saveMemberImg(Member member, MultipartFile profile) {

        ImageDto imageDto = minioServiceProvider.uploadImage(Bucket.MEMBER, profile);

        if (imageDto.getStatus().equals(ImageDto.Status.UPLOADED)){
            MemberImage memberImage = MemberImage.builder()
                    .isActive(1)
                    .member(member)
                    .url(imageDto.getPath())
                    .build();
            return memberImgRepository.save(memberImage);
        }else {
            throw new MinioUploadException("멤버 이미지 업로드에 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    프로필 사진이 디폴트인 경우도 있다...!
    1. 기본 프로필 -> 프로필 변경 x
    2. 기본 프포필 -> 프로필 변경 o
    3. 프로필 o -> 프로필 x
    4. 프로필 o -> 프로필 o

    * 1. deleteId = null 프로필을 바꾸지 않는다.
    * 2. profile 존재할 시에! deleteId는 필수입니다!
    * if profile 존재하고 deleteID가 존재하는 경우
    * else if deleteId
    * */
    public ResponseMemberImg updateStatus(MultipartFile profile, Long deleteFileId, Member updateMember) {
        // 2, 4
        if (!ObjectUtils.isEmpty(profile)){
            MemberImage memberImage = saveMemberImg(updateMember, profile);
            return memberImgMapper.entityToDto(memberImage);
        }
        if (deleteFileId != null){
            changeStatus(deleteFileId, updateMember);
        }
        return new ResponseMemberImg().builder()
                .status(ResponseMemberImg.Status.DEFAULT)
                .build();
    }
    private void changeStatus(Long deleteFileId, Member updateMember){
        List<MemberImage> deleteImgList = memberImgCustomRepository.findByIdAndMember(deleteFileId, updateMember);
        if (!deleteImgList.isEmpty() && deleteImgList.size() == 1){
            MemberImage deleteImg = deleteImgList.get(0);
            deleteImg.changeStatus();
        }
    }
    public int changeStatusByMember(Member member){
        List<MemberImage> deleteImgList = memberImgCustomRepository.findByMember(member);
        if (!deleteImgList.isEmpty() && deleteImgList.size()==1){
            MemberImage memberImage = deleteImgList.get(0);
            memberImage.changeStatus();
            return memberImage.getIsActive();
        }else {
            throw new MemberImgException("삭제하려는 멤버의 프로필이 1개 이상입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public List<MemberImage> findImgListByIdList(List<Member> memberList) {
        return memberImgCustomRepository.findImgsByMemberList(memberList);
    }
}
