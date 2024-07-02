package com.example.play.member.dto;

import com.example.play.member.domain.Member;
import lombok.*;

@NoArgsConstructor
@Data
@Getter
@AllArgsConstructor
public class RequestUpdateMemberDto {
    private String nickname;
    private String email;
    private String password;
    public boolean isUpdateNicknamePresent(){
        return nickname != null && !nickname.trim().isEmpty();
    }
    public boolean isUpdatePwdPresent(){
        return password != null && !password.trim().isEmpty();
    }
    public boolean isUpdateEmailPresent(){
        return email != null && !email.trim().isEmpty();
    }

    public void sendNicknameToMember(Member member) {
        member.changeNickname(nickname);
    }
    public void sendUpdatePwdToMember(Member member) {
        member.changePassword(password);
    }
    public void sendUpdateEmailToMember(Member member) {
        member.changeEmail(email);
    }
}
