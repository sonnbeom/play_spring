package com.example.play.post.service;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.service.PostImgService;
import com.example.play.member.entity.Member;
import com.example.play.member.service.MemberService;
import com.example.play.post.constant.PageSize;
import com.example.play.post.dto.RequestPostDto;
import com.example.play.post.dto.ResponsePostOne;
import com.example.play.post.dto.ResponsePostDTo;
import com.example.play.post.dto.RequestUpdatePostDto;
import com.example.play.post.entity.Post;
import com.example.play.post.exception.PostDeleteException;
import com.example.play.post.exception.PostNotFoundException;
import com.example.play.post.exception.PostUpdateException;
import com.example.play.post.postMapper.PostMapper;
import com.example.play.post.repository.PostRepository;
import com.example.play.post.repository.CustomPostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CustomPostRepository customPostRepository;
    private final PostImgService postImgService;
    private final MemberService memberService;

    @Override
    public ResponsePostOne create(RequestPostDto postDto, List<MultipartFile> files, String email) {
        Member member = memberService.findByEmail(email);
        Post post = postMapper.dtoToEntity(postDto, member);
        Post saved = postRepository.save(post);
        if (!ObjectUtils.isEmpty(files)){
            List<ResponseImg> urls = postImgService.savePostImage(files, saved);
            return saved.ToDtoWithImage(urls);
        }
        else {
            return saved.entityToDto();
        }
    }
    @Override
    public ResponsePostOne readOne(Long postId) {
        Post post = findById(postId);
        post.upHit();
        List<ResponseImg> responseImgList = postImgService.readImages(post);
        return post.ToDtoWithImage(responseImgList);
    }
    @Override
    public Post findById(Long postId){
        return postRepository.findById(postId).
                orElseThrow(() -> new PostNotFoundException("ID와 일치하는 게시글을 조회할 수 없습니다. postId: "+ postId, HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponsePostDTo readBySort(int page, String sortType) {
        Pageable pageable = PageRequest.of(page ,PageSize.size);
        Page<Post> postPage = customPostRepository.findBySort(pageable, sortType);
        return postMapper.pageEntityToDto(postPage);
    }

    @Override
    public ResponsePostDTo readBySearch(int page, String type, String keyword) {
        Pageable pageable = PageRequest.of(page, PageSize.size);
        Page<Post> postPage = customPostRepository.findBySearch(pageable, type, keyword);
        return postMapper.pageEntityToDto(postPage);
    }

    @Override
    public ResponsePostOne update(Long postId ,
                                  RequestUpdatePostDto updateDto,
                                  List<MultipartFile> files,
                                  List<Long> deleteImageList,
                                  String email) {

        Post post = findById(postId);

        //업데이트 권한 체크
        checkUpdateAuthorization(post ,email, postId);

        if (!ObjectUtils.isEmpty(updateDto.getTitle())){
            post.updateTitle(updateDto.getTitle());
        }
        if (!ObjectUtils.isEmpty(updateDto.getContent())){
            post.updateContent(updateDto.getContent());
        }

        List<ResponseImg> imgList = postImgService.update(post, deleteImageList, files);
        return post.ToDtoWithImage(imgList);
    }
    private void checkUpdateAuthorization(Post post, String email, Long postId){
        Member member = memberService.findByEmail(email);

        if (!post.checkAuthorization(member)){
            log.info("게시글을 작성하지 않은 멤버가 해당 게시글을 업데이트하고자 했습니다 게시글 아이디, 멤버 이메일 : {}", postId, email);
            throw new PostUpdateException("유저가 해당 게시글을 업데이트할 수 있는 권한이 없습니다. " +
                    "postId: " + postId + " email: "+email, HttpStatus.FORBIDDEN);
        }
    }
    @Override
    public int delete(Long postId, String email) {
        Post post = findById(postId);

        checkDeleteAuthorization(post, email, postId);
        postImgService.deleteImg(post);
        return post.changeIsActive();
    }
    private void checkDeleteAuthorization(Post post, String email, Long postId){
        Member member = memberService.findByEmail(email);
        if (!post.checkAuthorization(member)){
            log.info("게시글을 작성하지 않은 멤버가 해당 게시글을 삭제하고자 했습니다 게시글 아이디, 이메일 : {}", postId, email);
            throw new PostDeleteException("유저가 해당 게시글을 삭제할 수 있는 권한이 없습니다." +
                    "postId: " + postId + " email: "+email, HttpStatus.FORBIDDEN);
        }
    }
    @Override
    public ResponsePostDTo getLikedPosts(String email, int page) {
        Member member = memberService.findByEmail(email);
        Pageable pageable = PageRequest.of(page ,PageSize.size);
        Page<Post> likedPostPage = customPostRepository.findLikedPosts(member, pageable);
        return postMapper.pageEntityToDto(likedPostPage);
    }
}
