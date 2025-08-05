package com.geukrock.geukrockapiserver.crawler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.crawler.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    private final CrawlerService crawlerService;

    @GetMapping("/members")
    public ResponseEntity<List<CrawledMemberDto>> getSomoimPage() {
        List<CrawledMemberDto> members = crawlerService.getMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/meetings")
    public ResponseEntity<List<CrawledMeetingDto>> getMeetings() {
        List<CrawledMeetingDto> meetings = crawlerService.getMeetings();
        return ResponseEntity.ok(meetings);
    }
}
