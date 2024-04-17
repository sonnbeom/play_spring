//package com.example.play.auth.memberLoader;
//
//import com.example.play.jwt.dto.CustomUserDetails;
//import com.example.play.member.entity.Member;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ClientMemberLoader {
//    public Member getClientMember(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getPrincipal() instanceof CustomUserDetails){
//            return  ((CustomUserDetails) authentication.getPrincipal()).member();
//        }
//        return null;
//    }
//    public String getClientMemberEmail(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getPrincipal() instanceof CustomUserDetails){
//            return  ((CustomUserDetails) authentication.getPrincipal()).getUsername();
//        }
//        return null;
//    }
//}
