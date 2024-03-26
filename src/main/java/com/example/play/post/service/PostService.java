package com.example.play.post.service;

import com.example.play.post.dto.CreatePostDto;
import com.example.play.post.dto.ResponsePostDto;
import com.example.play.post.entity.Post;
import com.example.play.post.exception.PostNotFoundException;
import com.example.play.post.postMapper.PostMapper;
import com.example.play.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public ResponsePostDto create(CreatePostDto postDto) {
        Post post = postMapper.dtoToEntity(postDto);
        Post saved = postRepository.save(post);
        return postMapper.entityToDto(saved);
    }

    public ResponsePostDto readOne(Long postId) {
        Post post = findById(postId);
        return postMapper.entityToDto(post);
    }
    private Post findById(Long postId){
        Optional<Post> optional = postRepository.findById(postId);
        return optional.orElseThrow(() -> new PostNotFoundException(postId+"로 ID를 가진 게시글을 조회할 수 없습니다."));
    }
}
