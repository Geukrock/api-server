package com.geukrock.geukrockapiserver.member.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberReqDto {
    String somoimName;
    String profileUrl;
    LocalDate birthDate;
    LocalDate joinDate;
}
