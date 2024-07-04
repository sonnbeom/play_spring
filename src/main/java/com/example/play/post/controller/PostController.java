package com.example.play.post.controller;

import com.example.play.jwt.dto.CustomUserDetails;
import com.example.play.post.dto.*;
import com.example.play.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "제목과 내용을 기반으로 게시글 생성하는 API입니다.")
    @ApiResponse(responseCode = "201", description = "게시글 생성에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "이미지 업로드에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "등록하려는 사진이 규정에 적합하지 않습니다..", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "게시글을 작성하려는 멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "게시글 생성에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @PostMapping()
    public ResponseEntity<ResponsePostOne> create(@Valid @RequestPart("postDto") RequestPostDto postDto,
                                    @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                    @AuthenticationPrincipal CustomUserDetails customUser){

        ResponsePostOne response = postService.create(postDto ,files, customUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글 조회", description = "id를 기반으로 게시글 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "게시글 조회에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "게시글 가져오기에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> getOne(@PathVariable("postId") Long postId){
        ResponsePostOne responsePostDto = postService.readOne(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }
    @Operation(summary = "좋아요한 게시글 리스트 조회", description = "좋아요한 게시글들을 불러오는 API입니다.")
    @ApiResponse(responseCode = "200", description = "좋아요한 게시글 가져오기를 성공하였습니다..", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당요청을 한 멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "좋아요한 게시글 가져오기에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/likeList")
    public ResponseEntity<ResponsePostDTo> readLikedPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestParam(defaultValue = "0") int page){
        ResponsePostDTo responsePostDTo = postService.getLikedPosts(userDetails.getUsername(), page);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDTo);
    }
    @Operation(summary = "타입에 따른 게시글 정렬", description = "좋아요순, 조회순, 최신순으로 게시글 정렬해서 가져오는 API입니다.")
    @ApiResponse(responseCode = "200", description = "정렬 조건에 맞는 게시글 가져오기를 성공하였습니다..", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당요청을 한 멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "정렬 조건에 맞는 게시글 가져오기에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    //최신순 페이징 정렬
    @GetMapping("/sort")
    public ResponseEntity<ResponsePostDTo> readBySort(@RequestParam(defaultValue = "0")int page,
                                                      @RequestParam String sortType){
        ResponsePostDTo postResponseDto = postService.readBySort(page ,sortType);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @Operation(summary = "게시글 검색", description = "제목, 작성자, 내용을 기반으로 게시글을 검색하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "검색 조건에 맞는 게시글 가져오기를 성공하였습니다..", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "해당요청을 한 멤버가 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "검색 조건에 맞는 게시글 가져오기에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @GetMapping("/search")
    public ResponseEntity<ResponsePostDTo> readBySearch(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam String type,
                                                        @RequestParam String keyword){
        ResponsePostDTo postResponseDto = postService.readBySearch(page, type, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }
    @Operation(summary = "게시글 수정", description = "id를 기반으로 게시글 제목,내용,사진을 업데이트하는  API입니다.")
    @ApiResponse(responseCode = "200", description = "게시글 정보 수정에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "멤버에게 게시글 업데이트 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "사진 등록에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "게시글 업데이트에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "등록하려는 사진이 규격에 적합하지 않습니다.", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{postId}")
    public ResponseEntity<ResponsePostOne> update(@PathVariable("postId") Long postId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestPart(value = "updatePostDto") RequestUpdatePostDto updateDto,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                  @RequestPart(value = "deleteFileList", required = false) List<Long> deleteImageList){
        ResponsePostOne responseOne = postService.update(postId ,updateDto, files, deleteImageList, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(responseOne);
    }
    @Operation(summary = "게시글 삭제", description = "id를 기반으로 게시글 삭제하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "게시글 삭제에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "멤버에게 삭제 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "게시글이 삭제되지 않았습니다.", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseDeletePostDTo> delete(@PathVariable("postId")Long postId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseDeletePostDTo result = postService.delete(postId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
