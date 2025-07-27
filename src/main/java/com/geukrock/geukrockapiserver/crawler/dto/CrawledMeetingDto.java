package com.geukrock.geukrockapiserver.crawler.dto;

import java.time.LocalDate;
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
}
