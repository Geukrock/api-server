package com.geukrock.geukrockapiserver.member.dto;

import java.time.LocalDate;

import com.geukrock.geukrockapiserver.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberResDto {
    String somoimName;
    LocalDate birthDate;
    String profileUrl;

    public MemberResDto(Member member){
        this.somoimName = member.getSomoimName();
        this.birthDate = member.getBirthDate();
        this.profileUrl = member.getProfileUrl();
    }
}
