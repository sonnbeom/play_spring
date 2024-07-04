package com.example.play.friendship.controller;

import com.example.play.friendship.dto.*;
import com.example.play.friendship.service.FriendshipService;
import com.example.play.jwt.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "친구 요청 생성", description = "친구요청 하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "친구 요청 보내기에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "친구 요청 보내는 멤버를 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "친구 요청 받는 멤버를 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "친구 요청에 실패했습니다..", content = @Content(mediaType = "application/json"))
    @PostMapping()
    public ResponseEntity<ResponseFriendshipDto> sendFriendShipRequest(@Valid @RequestBody RequestFriendship friendship,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseFriendshipDto responseFriendship =  friendshipService.create(friendship, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseFriendship);
    }
    @Operation(summary = "대기 중인 친구 요청 조회", description = "대기 중인 친구요청을 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "대기 중인 친구요청을 조회하는데 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 API를 호출한 멤버를 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "친구 요청 조회에 실패했습니다..", content = @Content(mediaType = "application/json"))
    @GetMapping("/received")
    public ResponseEntity<List<ResponseFriendshipWithImg>> getWaitingFriendship(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                @RequestParam(defaultValue = "0") int page){
        List<ResponseFriendshipWithImg> result = friendshipService.getWaitingFriendList(userDetails.getUsername(), page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @Operation(summary = "친구 요청 수락", description = "친구 요청을 수락하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "친구 요청 수락이 성공했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "해당 멤버가 친구요청을 수락할 수 있는 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 API를 호출한 멤버를 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "친구 요청 수락에 실패했습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping("/approve/{friendshipId}")
    public ResponseEntity<ResponseFriendshipDto> approveFriendship(@Valid @PathVariable("friendshipId") Long friendshipId,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseFriendshipDto responseApprove = friendshipService.approveFriendship(friendshipId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(responseApprove);
    }
    @Operation(summary = "친구 리스트 조회", description = "친구 리스트를 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "친구 요청 조회를 성공했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 API를 호출한 멤버를 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "친구 요청 조회에 실패했습니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/friendList")
    public ResponseEntity<List<ResponseFriendshipWithImg>> readFriendList(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ResponseFriendshipWithImg> friendList = friendshipService.findFriendList(userDetails.getUsername());
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
    @Operation(summary = "친구요청 삭제", description = "친구 요청을 삭제하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "친구 요청 삭제를 성공했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 API를 호출한 멤버를 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "친구 요청을 조회하는데 실패했습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "해당 멤버의 권한으로 친구요청 삭제를 할 수 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "친구 요청 삭제를 실패했습니다.", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/delete")
    public ResponseEntity deleteFriend(@Valid @RequestBody RequestDeleteFriendship requestDeleteFriendship,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        friendshipService.deleteFriendship(requestDeleteFriendship, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
