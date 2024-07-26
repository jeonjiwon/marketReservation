package com.example.marketReservation.model;

import com.example.marketReservation.domain.MemberEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

public class Auth {

    @Data
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private String phoneNumber;
        private String userFgCd;
        private List<String> roles;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .phoneNumber(this.phoneNumber)
                    .userFgCd(this.userFgCd)
                    .roles(this.roles)
                    .build();
        }
    }
}
