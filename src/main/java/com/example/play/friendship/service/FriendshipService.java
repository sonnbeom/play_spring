package com.example.play.friendship.service;

import com.example.play.friendship.constant.FriendshipDeleteStatus;
import com.example.play.friendship.dto.*;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.exception.FriendshipDeleteAuthorityException;
import com.example.play.friendship.exception.FriendshipNotFoundException;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipCustomRepository;
import com.example.play.friendship.repository.FriendshipRepository;
import com.example.play.image.dto.ResponseImg;
import com.example.play.image.entity.MemberImage;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.play.friendship.constant.FriendshipDeleteStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendshipService {
    private final FriendshipCustomRepository friendshipCustomRepository;
    private final FriendshipRepository friendshipRepository;
    private final MemberService memberService;
    private final FriendshipMapper friendshipMapper;
    private final MemberImgService memberImgService;

    // 친구 요청 생성
    public ResponseFriendship create(RequestFriendship requestFriendship, String senderEmail) {
        // 친구요청 보내는 이와 받는 이를 조회 isFrom = true : 보내는 사람
        Member sender = memberService.findByEmail(senderEmail);
        Member receiver = memberService.findByEmail(requestFriendship.getReceiver());

        //친구 요청 보내는 사람의 엔티티 생성
        Friendship sendFriendship = friendshipMapper.dtoToEntityBySend(sender, receiver);
        //친구 요청 받는 사람의 엔티티 생성
        Friendship receivedFriendship = friendshipMapper.dtoToEntityByReceived(sender, receiver);

        friendshipRepository.save(sendFriendship);
        friendshipRepository.save(receivedFriendship);

        ResponseFriendshipDto friendshipDtoBySend = sendFriendship.entityToDto();
        ResponseFriendshipDto friendshipDtoByReceived = receivedFriendship.entityToDto();

        //엔티티틀 클라이언트 요청에 맞게 매핑하여 리턴
        return ResponseFriendship.builder()
                .friendshipDtoByReceived(friendshipDtoByReceived)
                .friendshipDtoBySend(friendshipDtoBySend)
                .build();
    }

    public List<ResponseFriendshipWithImg> getWaitingFriendList(String email) {
        //친구 요청 목록을 조회하고자 하는 멤버 조회
        Member member = memberService.findByEmail(email);

       // 해당 멤버의 친구 대기 요청 목록 조회
        List<Friendship> friendshipList = friendshipCustomRepository.findWaitinFrinedshipList(member);

        // key: 친구 멤버, value: 친구 이미지
        Map<Member, MemberImage> friendImg = findFriendImg(friendshipList);
        // 친구의 이미지가 있다면 이미지와 멤버를 매핑해서 dto로 변환 이미지가 없다면 멤버와 기본 프로필로 매핑
        List<ResponseFriendshipWithImg> dtoList = mappingFriendshipWithImg(friendshipList, friendImg);
        return dtoList;

    }

    // 친구 신청 승인
    public List<ResponseFriendshipDto> approveFriendship(Long friendshipId) {
        Friendship friendship = findById(friendshipId);
        Friendship counterFriendship = findById(friendship.getCounterpartId());

        friendship.acceptFriendshipRequest();
        counterFriendship.acceptFriendshipRequest();

        ResponseFriendshipDto response = friendshipMapper.entityToDto(friendship);
        ResponseFriendshipDto responseCounter = friendshipMapper.entityToDto(counterFriendship);

        List<ResponseFriendshipDto> list = new ArrayList<>();
        list.add(response);
        list.add(responseCounter);

        return list;
    }

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

            Optional<ResponseImg> img =  friendship.isExistSenderImg(map);
            ResponseFriendshipDto friendshipDto = friendship.entityToDto();
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
                        .img(new ResponseImg())
                        .build();
            }
            friendshipWithImgList.add(dto);
        }
        return friendshipWithImgList;
    }
    private Friendship findById(Long friendshipId){
        Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
        return friendship.orElseThrow(()->new FriendshipNotFoundException("해당 id를 가진 Friendship을 조회할 수 없습니다. :{}",friendshipId));
    }

    public ResponseFriendshipDelete deleteFriendship(RequestDeleteFriendship requestDeleteFriendship, String fromMemberEmail) {
        Friendship friendship = findById(requestDeleteFriendship.getFriendshipId());
        Member member = memberService.findByEmail(fromMemberEmail);

        // 삭제 권한이 있는지 확인
        checkDeleteRights(friendship, member);
        Long counterpartId = friendship.getCounterpartId();
        Friendship counterFriendship = findById(counterpartId);

        boolean deleteFriendship = friendshipCustomRepository.delete(friendship);
        boolean deleteCounterpart = friendshipCustomRepository.delete(counterFriendship);

        if(deleteCounterpart && deleteFriendship){
            return ResponseFriendshipDelete.builder()
                    .counterpartId(counterpartId)
                    .friendshipId(friendship.getId())
                    .status(SUCCESS)
                    .build();
        }else {
            return ResponseFriendshipDelete.builder()
                    .counterpartId(counterpartId)
                    .friendshipId(friendship.getId())
                    .status(FAILURE)
                    .build();
        }
    }
    private void checkDeleteRights(Friendship friendship, Member member){
        if (friendship.getMember().getId() != member.getId()){
            throw new FriendshipDeleteAuthorityException("해당 멤버의 id로 friendship를 삭제할 수 있는 권한이 없습니다.",member.getId(), friendship.getId());
        }
    }
}
