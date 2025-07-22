package com.geukrock.geukrockapiserver.crawler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.geukrock.geukrockapiserver.crawler.service.CrawlerService;
import com.geukrock.geukrockapiserver.member.dto.CrawledMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    private final CrawlerService crawlerService;

    @GetMapping
    public ResponseEntity<List<CrawledMemberDto>> getSomoimPage() {
        List<CrawledMemberDto> members = crawlerService.getMembers();
        return ResponseEntity.ok(members);
    }
}
