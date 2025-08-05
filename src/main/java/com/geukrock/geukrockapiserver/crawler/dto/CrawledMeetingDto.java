package com.geukrock.geukrockapiserver.crawler.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CrawledMeetingDto {
    String title;
    LocalDate date;
    String location;
    List<String> joinMemberProfileUrl = new ArrayList<>();
}
