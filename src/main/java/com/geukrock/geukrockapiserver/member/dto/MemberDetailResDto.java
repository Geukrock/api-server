package com.geukrock.geukrockapiserver.member.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberDetailResDto {
    Long id;
    String somoimName;
    LocalDate birthDate;
    LocalDate joinDate;
    String profileUrl;

    Integer joinCount;
    LocalDate LastJoinDate;
}
