package com.example.play.post.service;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.service.PostImgService;
import com.example.play.post.constant.PageSize;
import com.example.play.post.dto.CreatePostDto;
import com.example.play.post.dto.PostResponseOne;
import com.example.play.post.dto.PostResponseDto;
import com.example.play.post.dto.PostUpdateDto;
import com.example.play.post.entity.Post;
import com.example.play.post.exception.PostNotFoundException;
import com.example.play.post.postMapper.PostMapper;
import com.example.play.post.repository.PostRepository;
import com.example.play.post.repository.PostRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostRepositoryCustom postRepositoryCustom;
    private final PostImgService postImgService;

    public PostResponseOne create(CreatePostDto postDto, List<MultipartFile> files) {
        Post post = postMapper.dtoToEntity(postDto);
        Post saved = postRepository.save(post);
        if (!files.isEmpty()){
            List<ResponseImg> urlS = postImgService.savePostImage(files, saved);
            return postMapper.entityToDtoWithImage(saved,urlS);
        }
        else {
            return postMapper.entityToDto(saved);
        }
    }
    public PostResponseOne readOne(Long postId) {
        Post post = findById(postId);
        post.upHit();
        List<ResponseImg> responseImgs = postImgService.readImages(post);
        return postMapper.entityToDtoWithImage(post, responseImgs);
    }
    private Post findById(Long postId){
        Optional<Post> optional = postRepository.findById(postId);
        return optional.orElseThrow(() -> new PostNotFoundException(postId+"로 ID를 가진 게시글을 조회할 수 없습니다."));
    }

    public PostResponseDto readBySort(int page, String sortType) {
        Pageable pageable = PageRequest.of(page ,PageSize.size);
        Page<Post> postPage = postRepositoryCustom.findBySort(pageable, sortType);
        return postMapper.pageEntityToDto(postPage);
    }

    public PostResponseDto readBySearch(int page, String type, String keyword) {
        Pageable pageable = PageRequest.of(page, PageSize.size);
        Page<Post> postPage = postRepositoryCustom.findBySearch(pageable, type, keyword);
        return postMapper.pageEntityToDto(postPage);
    }

    public PostResponseOne update(Long postId ,PostUpdateDto updateDto, List<MultipartFile> files, List<Long> deleteImageList) {
        Post post = findById(postId);
        if (updateDto.getTitle() != null && !updateDto.getTitle().isEmpty()){
            post.changeTitle(updateDto.getTitle());
        }
        if (updateDto.getContent() != null && !updateDto.getContent().isEmpty()){
            post.changeContent(updateDto.getContent());
        }
        List<ResponseImg> imgList = postImgService.update(post, deleteImageList, files);
        return postMapper.entityToDtoWithImage(post, imgList);
    }
    public int delete(Long postId) {
        Post post = findById(postId);
        post.changeIsActive();
        postImgService.deleteImg(post);
        return post.getIsActive();
    }
}
