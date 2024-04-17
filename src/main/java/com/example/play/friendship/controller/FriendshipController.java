package com.example.play.friendship.controller;

import com.example.play.friendship.constant.FriendshipDeleteStatus;
import com.example.play.friendship.dto.*;
import com.example.play.friendship.service.FriendshipService;
import com.example.play.jwt.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friendship")
@Slf4j
public class FriendshipController {
    private final FriendshipService friendshipService;
    @PostMapping("/create")
    public ResponseEntity<ResponseFriendship> sendFriendShipRequest(@Valid @RequestBody RequestFriendship friendship,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseFriendship responseFriendship =  friendshipService.create(friendship, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseFriendship);
    }
    @GetMapping("/received")
    public ResponseEntity<List<ResponseFriendListDto>> getWaitingFriendship(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ResponseFriendListDto> result = friendshipService.getWaitingFriendList(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    @PostMapping("/approve/{friendshipId}")
    public ResponseEntity<List<ResponseFriendshipDto>> approveFriendship(@Valid @PathVariable("friendshipId") Long friendshipId){
        List<ResponseFriendshipDto> responseApprove = friendshipService.approveFriendship(friendshipId);
        return ResponseEntity.status(HttpStatus.OK).body(responseApprove);
    }
    
    @GetMapping("/friendList")
    public ResponseEntity<List<ResponseFriendListDto>> readFriendList(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ResponseFriendListDto> friendList = friendshipService.findFriendList(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(friendList);
    }
    // 친구 요청 삭제
    /*
    *
    * 1. 친구 리스트에 있는 놈 삭제
    * 2-> friendship id로 엔티티 가져오기 counterpartId로 반대편 frinedship도 삭제
    *
    * 2. 친구 요청이 왔는데 거절
    *
    * . 친구 요청이 왔는데 거절
    * 1. friendship id로 엔티티 가져오기 counterpartId로 반대편 frinedship도 삭제
    * */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseFriendshipDelete> deleteFriend(@Valid @RequestBody RequestDeleteFriendship requestDeleteFriendship,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseFriendshipDelete responseDelete = friendshipService.deleteFriendship(requestDeleteFriendship, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(responseDelete);
    }
}
