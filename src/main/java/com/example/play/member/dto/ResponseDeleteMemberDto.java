package com.example.play.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseDeleteMemberDto {
    private Enum memberStatus;
    private Enum memberImgStatus;
    public enum STATUS{
        DELETED, EXIST
    }
    public ResponseDeleteMemberDto(int memberActive, int memberImgActive) {
        this.memberStatus = setStatus(memberActive);
        this.memberImgStatus = setStatus(memberImgActive);
    }
    private Enum setStatus(int activeStatus) {
        return activeStatus == 1 ? STATUS.EXIST : STATUS.DELETED;
    }
}
