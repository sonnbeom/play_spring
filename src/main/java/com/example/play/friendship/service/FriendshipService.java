package com.example.play.friendship.service;

import com.example.play.friendship.constant.FriendshipDeleteStatus;
import com.example.play.friendship.dto.*;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.exception.FriendshipDeleteAuthorityException;
import com.example.play.friendship.exception.FriendshipNotFoundException;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipCustomRepository;
import com.example.play.friendship.repository.FriendshipRepository;
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
    public ResponseFriendship create(RequestFriendship friendship, String fromUserEmail) {
        // 친구요청 보내는 이와 받는 이를 조회 isFrom = true : 보내는 사람
        Member fromMember = memberService.findByEmail(fromUserEmail);
        Member toMember = memberService.findByEmail(friendship.getToMemberEmail());

        //친구 요청 보내는 멤버, 받는 멤버를 친구 요청 엔티티로 매핑
        Friendship friendshipFrom = friendshipMapper.fromDtoToEntity(fromMember, toMember);
        Friendship friendshipTo = friendshipMapper.toDtoToEntity(fromMember, toMember);

        friendshipRepository.save(friendshipTo);
        friendshipRepository.save(friendshipFrom);

        // 레포지토리에 저장해야 아이디가 나오기에 저장 후 서로의 아이디 매핑
        friendshipFrom.setCounterpartId(friendshipTo.getId());
        friendshipTo.setCounterpartId(friendshipFrom.getId());

        //엔티티틀 클라이언트 요청에 맞게 매핑하여 리턴
        ResponseFriendshipDto responseFriendshipFrom = friendshipMapper.entityToDto(friendshipFrom);
        ResponseFriendshipDto responseFriendshipTo = friendshipMapper.entityToDto(friendshipTo);
        return ResponseFriendship.builder()
                .fromFriendship(responseFriendshipFrom)
                .toFriendship(responseFriendshipTo)
                .build();
    }

    public List<ResponseFriendListDto> getWaitingFriendList(String email) {
        //친구 요청 목록을 조회하고자 하는 멤버 조회
        Member member = memberService.findByEmail(email);

       // 해당 멤버의 친구 대기 요청 목록 조회
        List<Friendship> friendshipList = friendshipCustomRepository.findWaitinFrinedshipList(member);

        // key: 친구 멤버의 id, value: 친구 이미지
        Map<Long, MemberImage> friendImg = findFriendImg(friendshipList);
        // 친구의 이미지가 있다면 이미지와 멤버를 매핑해서 dto로 변환 이미지가 없다면 멤버와 기본 프로필로 매핑
        List<ResponseFriendListDto> dtoList = readFriendListWithImg(friendshipList, friendImg);
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

    public List<ResponseFriendListDto> findFriendList(String email) {
        Member member = memberService.findByEmail(email);
        List<Friendship> friendshipList = friendshipCustomRepository.findFriendListByMember(member);
        Map<Long, MemberImage> friendImg = findFriendImg(friendshipList);
        List<ResponseFriendListDto> dtoList = readFriendListWithImg(friendshipList, friendImg);
        return dtoList;
    }
    private  Map<Long, MemberImage> findFriendImg(List<Friendship> friendshipList){
        List<Long> toMemberIdList = new ArrayList<>();

        //친구 아이디를 가져와 리스트에 저장
        friendshipList.forEach(friendship -> {
            toMemberIdList.add(friendship.getCounterpartId());
        });

        // 리스트에 저장된 멤버(친구 신청한 이들) id로 멤버 이미지 리스트 조회
        List<MemberImage> toMemberImgList = memberImgService.findImgListByIdList(toMemberIdList);

        // key: 친구 멤버의 id, value: 친구 이미지
        Map<Long, MemberImage> map = new HashMap<>();
        for (MemberImage img : toMemberImgList){
            Long memberId = img.getMember().getId();
            map.put(memberId, img);
        }
        return map;
    }
    private List<ResponseFriendListDto> readFriendListWithImg(List<Friendship> friendshipList, Map<Long, MemberImage> friendImg){
        List<ResponseFriendListDto> dtoList = new ArrayList<>();
        //이미지가 존재하는 멤버라면 이미지를 포함해서 dto로 변환 이미지가 없다면 이미지 디폴트 값으로 dto로 변환
        for (Friendship friendship : friendshipList){
            Long toMemberId = friendship.getCounterpartId();

            // 사진이 있을 경우
            if (friendImg.containsKey(toMemberId)){
                String url = friendImg.get(toMemberId).getUrl();
                ResponseFriendListDto dto = friendshipMapper.EntityToDtoWithImg(friendship, url);
                dtoList.add(dto);
            }
            // 사진이 없는 경우
            else {
                ResponseFriendListDto dto = friendshipMapper.entityToDtoWithoutImg(friendship);
                dtoList.add(dto);
            }
        }
        return dtoList;
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
