package com.example.play.like.post.service;

import com.example.play.like.post.dto.RequestLike;
import com.example.play.like.post.dto.ResponseLike;
import com.example.play.like.post.dto.ResponsePostLikeDto;
import com.example.play.like.post.entity.PostLike;
import com.example.play.like.post.exception.PostLikeException;
import com.example.play.like.post.mapper.PostLikeMapper;
import com.example.play.like.post.repository.PostLikeCustomRepository;
import com.example.play.like.post.repository.PostLikeRepository;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberServiceImpl;
import com.example.play.post.dto.ResponsePostOne;
import com.example.play.post.entity.Post;
import com.example.play.post.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.example.play.like.post.dto.ResponsePostLikeDto.LikeStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeCustomRepository postLikeCustomRepository;
    private final PostService postService;
    private final MemberServiceImpl memberServiceImpl;
    private final PostLikeMapper postLikeMapper;
    private final PostLikeRepository postLikeRepository;
    public ResponsePostLikeDto createLike(RequestLike likeRequest, String email) {

        Post post = postService.findById(likeRequest.getPostId());
        Member member = memberServiceImpl.findByEmail(email);
        List<PostLike> postLike = postLikeCustomRepository.findByPostAndMember(post, member);

        if (duplicateCheck(postLike)){
            PostLike like = postLikeMapper.createLike(post, member);
            PostLike savedLike = postLikeRepository.save(like);

            post.upLikeCount();

            ResponsePostOne responsePostOne = post.entityToDto();
            ResponseLike responseLike = savedLike.entityToDto();

            return ResponsePostLikeDto.builder()
                    .responsePostOne(responsePostOne)
                    .responseLike(responseLike)
                    .likeStatus(CREATE)
                    .build();

        }else if (postLike.size() == 1){

            PostLike deleteLike = postLike.get(0);
            deleteLike.deleteLike();

            post.downLikeCount();

            ResponsePostOne responsePostOne = post.entityToDto();
            ResponseLike responseLike = deleteLike.entityToDto();

            return ResponsePostLikeDto.builder()
                    .responsePostOne(responsePostOne)
                    .responseLike(responseLike)
                    .likeStatus(DELETE)
                    .build();
        }else {
            throw new PostLikeException("멤버가 해당 게시물에 좋아요를 누른 좋아요가 1개가 아닙니다. " +
                    "likeRequest: {}"+likeRequest.getPostId(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private boolean duplicateCheck(List<PostLike> postLike){
        if (ObjectUtils.isEmpty(postLike)){
            return true;
        }else {
            return false;
        }
    }
}
