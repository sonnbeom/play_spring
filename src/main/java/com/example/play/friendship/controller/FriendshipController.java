package com.example.play.friendship.controller;

import com.example.play.friendship.dto.RequestFriendship;
import com.example.play.friendship.dto.ResponseFriendship;
import com.example.play.friendship.dto.WaitingFriendListDto;
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
    public ResponseEntity<List<WaitingFriendListDto>> getWaitingFriendship(@PathVariable String email){
        List<WaitingFriendListDto> result = friendshipService.getWaitingFriendList(email);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
