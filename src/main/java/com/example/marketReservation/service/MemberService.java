package com.example.marketReservation.service;

import com.example.marketReservation.domain.MemberEntity;
import com.example.marketReservation.model.Auth;
import com.example.marketReservation.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    }

    /* 회원 가입 */
    public MemberEntity signUp(Auth.SignUp member) {
        boolean existsUserYn = this.memberRepository.existsByUsername(member.getUsername());
        if(existsUserYn) {
            throw new RuntimeException("이미 사용중인 아이디입니다. ");
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));

        var result = this.memberRepository.save(member.toEntity());
        return result;
    }


    /* 로그인 */
    public MemberEntity signIn(Auth.SignIn member) {
        var user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 아이디입니다."));
        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
}
