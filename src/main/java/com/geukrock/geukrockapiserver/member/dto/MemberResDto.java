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
    Long id;
    String somoimName;
    LocalDate birthDate;
    LocalDate joinDate;
    String profileUrl;

    public MemberResDto(Member member){
        this.id = member.getId();
        this.somoimName = member.getSomoimName();
        this.birthDate = member.getBirthDate();
        this.joinDate = member.getJoinDate();
        this.profileUrl = member.getProfileUrl();
    }
}
