package com.example.play.image.mapper;

import com.example.play.image.dto.ResponseImg;
import com.example.play.image.entity.PostImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class PostImgMapper {
    public List<ResponseImg> entityToDto(List<PostImage> postImages) {
        List<ResponseImg> responseImgList = new ArrayList<>();
        for (PostImage postImage : postImages){
            ResponseImg responseImg = postImage.entityToDto();
            responseImgList.add(responseImg);
        }
        return responseImgList;
    }
}
