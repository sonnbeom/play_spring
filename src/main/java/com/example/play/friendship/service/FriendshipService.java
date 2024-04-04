package com.example.play.friendship.service;

import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.dto.WaitingFriendListDto;
import com.example.play.friendship.entity.Friendship;
import com.example.play.friendship.mapper.FriendshipMapper;
import com.example.play.friendship.repository.FriendshipCustomRepository;
import com.example.play.friendship.repository.FriendshipRepository;
import com.example.play.image.constant.ImgConstant;
import com.example.play.image.entity.MemberImage;
import com.example.play.image.service.MemberImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ResponseFriendship create(RequestFriendship friendship) {
        // 친구요청 보내는 이와 받는 이를 조회 isFrom = true : 보내는 사람
        Member fromMember = memberService.findByEmail(friendship.getFromMemberEmail());
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


    public List<WaitingFriendListDto> getWaitingFriendList(String email) {
        //친구 요청 목록을 조회하고자 하는 멤버 조회
        Member member = memberService.findByEmail(email);

        //해당 멤버의 친구 대기 요청 목록 조회
        List<Friendship> friendshipList = friendshipCustomRepository.findWaitinFrinedshipList(member);

        //대기 요청 목록에서 신청한 이들의 아이디를 가져와 리스트에 저장
        List<Long> toMemberIdList = new ArrayList<>();
        friendshipList.forEach(friendship -> {
            toMemberIdList.add(friendship.getCounterpartId());
        });

        // 리스에 저장된 멤버(친구 신청한 이들) id로 멤버 이미지 리스트 조회
        List<MemberImage> toMemberImgList = memberImgService.findImgListByIdList(toMemberIdList);
        // key: 친구 요청 신청한 멤버의 id, value: 친구 요청
        Map<Long, MemberImage> map = new HashMap<>();
        for (MemberImage img : toMemberImgList){
            Long memberId = img.getMember().getId();
            map.put(memberId, img);
        }
        // 가져온 멤버 이미지(친구 신청한 멤버)의 멤버 id와 map의 key로 저장된 id가 일치한다면 클라이언트 전달한 dtoList 생성
        List<WaitingFriendListDto> dtoList = new ArrayList<>();

        for (Friendship friendship : friendshipList){
           Long toMemberId = friendship.getCounterpartId();
           // 사진이 있을 경우
            if (map.containsKey(toMemberId)){
                String url = map.get(toMemberId).getUrl();
            WaitingFriendListDto dto = WaitingFriendListDto.builder()
                    .friendNickname(friendship.getFriendNickname())
                    .imgUrl(url)
                    .friendshipId(friendship.getId())
                    .friendEmail(friendship.getFriendEmail())
                    .time(friendship.getCreatedAt())
                    .status(friendship.getStatus())
                    .build();
                dtoList.add(dto);
            }else {
                WaitingFriendListDto dto = WaitingFriendListDto.builder()
                        .friendNickname(friendship.getFriendNickname())
                        .imgUrl(String.valueOf(ImgConstant.DEFAULT))
                        .friendshipId(friendship.getId())
                        .friendEmail(friendship.getFriendEmail())
                        .time(friendship.getCreatedAt())
                        .status(friendship.getStatus())
                        .build();
                dtoList.add(dto);

            }
        }
        return dtoList;
    }
}
