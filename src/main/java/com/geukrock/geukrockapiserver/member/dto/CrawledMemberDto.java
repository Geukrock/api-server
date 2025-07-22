package com.geukrock.geukrockapiserver.member.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MemberReqDto와 동일 하지만 이후에 ReqDto가 달라질 수 있음
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrawledMemberDto {
    String somoimName;
    String profileUrl;
    LocalDate birthDate;
}
