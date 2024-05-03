package com.example.play.friendship.service;

import com.example.play.friendship.dto.RequestDeleteFriendship;
import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendshipDto;
import com.example.play.friendship.dto.ResponseFriendshipWithImg;

import java.util.List;

public interface FriendshipService {

    // 친구 요청 생성
    ResponseFriendshipDto create(RequestFriendship requestFriendship, String senderEmail);

    // 대기중인 친구요청 가져오기
    List<ResponseFriendshipWithImg> getWaitingFriendList(String email, int page);

    // 친구 신청 승인
    ResponseFriendshipDto approveFriendship(Long friendshipId, String email);

    // 친구 리스트 가져오기
    List<ResponseFriendshipWithImg> findFriendList(String email);

    // 친구 삭제하기
    void deleteFriendship(RequestDeleteFriendship requestDeleteFriendship, String email);
}
