package com.example.marketReservation.controller;


import com.example.marketReservation.model.Auth;
import com.example.marketReservation.security.TokenProvider;
import com.example.marketReservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    /*
     * 회원가입 API
     * - 회원ID, PW, 구분(매장관리자/고객), 매장(매장관리자일때만 입력)
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request) {
        var result = this.memberService.signUp(request);
        return ResponseEntity.ok(request);
    }


    /*
     * 로그인 API
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request) {
        var member = this.memberService.signIn(request);
        var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());
        return ResponseEntity.ok(token);
    }
}
