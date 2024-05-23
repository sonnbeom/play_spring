package com.example.play.friendship.service;

import com.example.play.friendship.dto.*;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.exception.FriendshipApproveException;
import com.example.play.friendship.exception.FriendshipDeleteException;
import com.example.play.friendship.exception.FriendshipNotFoundException;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipCustomRepository;
import com.example.play.friendship.repository.FriendshipRepository;
import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.entity.MemberImage;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.entity.Member;


import com.example.play.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

import static com.example.play.friendship.constant.FriendshipPageSize.*;
import static com.example.play.image.dto.ResponseMemberImg.Status.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendshipServiceImpl implements FriendshipService{
    private final FriendshipCustomRepository friendshipCustomRepository;
    private final FriendshipRepository friendshipRepository;
    private final MemberService memberService;
    private final FriendshipMapper friendshipMapper;
    private final MemberImgService memberImgService;

    @Override
    public ResponseFriendshipDto create(RequestFriendship requestFriendship, String senderEmail) {
        // 친구요청 보내는 이와 받는 이를 조회 isFrom = true : 보내는 사람
        Member sender = memberService.findByEmail(senderEmail);
        Member receiver = memberService.findByEmail(requestFriendship.getReceiver());
        //친구 요청 보내는 사람의 엔티티 생성
        Friendship friendship = friendshipMapper.dtoToEntity(sender, receiver);
        //친구 요청 받는 사람의 엔티티 생성
        friendshipRepository.save(friendship);

        return friendship.toDto();
    }

    @Override
    public List<ResponseFriendshipWithImg> getWaitingFriendList(String email, int page) {
        //친구 요청 목록을 조회하고자 하는 멤버 조회
        Member member = memberService.findByEmail(email);
        Pageable pageable = PageRequest.of(page, size);

       // 해당 멤버의 친구 대기 요청 목록 조회
        List<Friendship> friendshipList = friendshipCustomRepository.findWaitinFrinedshipList(member, pageable);

        // key: 친구 멤버, value: 친구 이미지
        Map<Member, MemberImage> friendImg = findFriendImg(friendshipList);
        // 친구의 이미지가 있다면 이미지와 멤버를 매핑해서 dto로 변환 이미지가 없다면 멤버와 기본 프로필로 매핑
        List<ResponseFriendshipWithImg> dtoList = mappingFriendshipWithImg(friendshipList, friendImg);
        return dtoList;

    }

    @Override
    public ResponseFriendshipDto approveFriendship(Long friendshipId, String email) {
        Friendship friendship = findById(friendshipId);
        Member member = memberService.findByEmail(email);
        // 로그인한 유저가 친구신청을 받을 수 있느지 확인하는 메소드
        if (!friendship.isAuthorized(member)){
            throw new FriendshipApproveException("해당 유저의 권한으로 친구 신청을 승인할 수 없습니다. friendshipId: "+ friendship +"유저 이메일: " + email, HttpStatus.UNAUTHORIZED);
        }
        //친구 신청 상태 변경 WAITING -> ACCEPTED
        friendship.acceptFriendshipRequest();
        return friendship.toDto();
    }

    @Override
    public List<ResponseFriendshipWithImg> findFriendList(String email) {
        Member member = memberService.findByEmail(email);
        List<Friendship> friendshipList = friendshipCustomRepository.findFriendListByMember(member);
        Map<Member, MemberImage> friendImg = findFriendImg(friendshipList);
        List<ResponseFriendshipWithImg> dtoList = mappingFriendshipWithImg(friendshipList, friendImg);
        return dtoList;
    }
    private  Map<Member, MemberImage> findFriendImg(List<Friendship> friendshipList){
        List<Member> memberListBySend = new ArrayList<>();

        //친구 아이디를 가져와 리스트에 저장
        friendshipList.forEach(friendship -> {
            friendship.createSendMemberList(memberListBySend);
        });

        // 리스트에 저장된 멤버(친구 신청한 이들) id로 멤버 이미지 리스트 조회
        List<MemberImage> senderImgList = memberImgService.findImgListByIdList(memberListBySend);

        // key: 친구 멤버의 id, value: 친구 이미지
        Map<Member, MemberImage> map = new HashMap<>();
        for (MemberImage img : senderImgList){
            img.createMapWithMember(map);
        }
        return map;
    }
    private List<ResponseFriendshipWithImg> mappingFriendshipWithImg(List<Friendship> friendshipList, Map<Member, MemberImage> map){
        List<ResponseFriendshipWithImg> friendshipWithImgList = new ArrayList<>();
        //이미지가 존재하는 멤버라면 이미지를 포함해서 dto로 변환 이미지가 없다면 이미지 디폴트 값으로 dto로 변환
        for (Friendship friendship : friendshipList){

            Optional<ResponseMemberImg> img =  friendship.isExistSenderImg(map);
            ResponseFriendshipDto friendshipDto = friendship.toDto();
            ResponseFriendshipWithImg dto;
            // 친구 요청 보내는 사람에게 프로필 사진이 있는 경우
            if (img.isPresent()){
                dto = ResponseFriendshipWithImg.builder()
                        .friendshipDto(friendshipDto)
                        .img(img.get())
                        .build();
            }
            // 사진이 없는 경우
            else {
                dto = ResponseFriendshipWithImg.builder()
                        .friendshipDto(friendshipDto)
                        .img(new ResponseMemberImg(DEFAULT))
                        .build();
            }
            friendshipWithImgList.add(dto);
        }
        return friendshipWithImgList;
    }
    private Friendship findById(Long friendshipId){
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(() ->new FriendshipNotFoundException("해당 id를 가진 Friendship을 조회할 수 없습니다. id: "+friendshipId, HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteFriendship(RequestDeleteFriendship requestDeleteFriendship, String email) {
        Friendship friendship = findById(requestDeleteFriendship.getFriendshipId());
        Member member = memberService.findByEmail(email);
        // 삭제 권한이 있는지 확인
        if (!friendship.isAuthorized(member)){
            throw new FriendshipDeleteException("해당 유저의 권한으로 친구 신청을 삭제할 수 없습니다. :멤버 이메일: "+email, HttpStatus.UNAUTHORIZED);
        }
        friendshipRepository.delete(friendship);
    }
}
