package com.example.play.friendship.controller;

import com.example.play.friendship.dto.*;
import com.example.play.friendship.service.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friendship")
@Slf4j
public class FriendshipController {
    private final FriendshipService friendshipService;
    @PostMapping("/create")
    public ResponseEntity<ResponseFriendship> sendFriendShipRequest(@Valid @RequestBody RequestFriendship friendship){
        ResponseFriendship responseFriendship =  friendshipService.create(friendship);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseFriendship);
    }
    @GetMapping("/received/{email}")
    public ResponseEntity<List<ResponseFriendListDto>> getWaitingFriendship(@PathVariable String email){
        List<ResponseFriendListDto> result = friendshipService.getWaitingFriendList(email);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    @PostMapping("approve/{friendshipId}")
    public ResponseEntity<List<ResponseFriendshipDto>> approveFriendship(@Valid @PathVariable("friendshipId") Long friendshipId){
        List<ResponseFriendshipDto> responseApprove = friendshipService.approveFriendship(friendshipId);
        return ResponseEntity.status(HttpStatus.OK).body(responseApprove);
    }
    
    @GetMapping("/friendList/{memberId}")
    public ResponseEntity<List<ResponseFriendListDto>> readFriendList(@Valid @PathVariable("memberId") Long memberId){
        List<ResponseFriendListDto> friendList = friendshipService.findFriendList(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(friendList);
    }
    // 친구 요청 삭제
}
