package com.example.play.image.mapper;

import com.example.play.image.dto.ResponseMemberImg;
import com.example.play.image.entity.MemberImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
@Slf4j
public class MemberImgMapper {
    public ResponseMemberImg entityToDto(MemberImage img) {
            return new ResponseMemberImg().builder()
                    .status(ResponseMemberImg.Status.NOT_DEFAULT)
                    .id(img.getId())
                    .url(img.getUrl())
                    .build();
    }
}
