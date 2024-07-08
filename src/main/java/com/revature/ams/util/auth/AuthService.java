package com.revature.ams.util.auth;

import com.revature.ams.Member.Member;
import com.revature.ams.Member.MemberService;

import javax.security.sasl.AuthenticationException;

// TODO: REVIEW ME
public class AuthService {
    private final MemberService memberService; // Assuming a MemberService instance

    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member login(String email, String password) throws AuthenticationException {
        Member member = memberService.findByEmailAndPassword(email, password);
        if(member == null) throw new AuthenticationException("Invalid member credentials, please try again");
        return member;
    }


}
