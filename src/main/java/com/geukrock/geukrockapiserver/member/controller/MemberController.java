package com.geukrock.geukrockapiserver.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.crawler.service.CrawlerService;
import com.geukrock.geukrockapiserver.member.dto.MemberResDto;
import com.geukrock.geukrockapiserver.member.entity.Member;
import com.geukrock.geukrockapiserver.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final CrawlerService crawlerService;

    @PostMapping("/sync")
    public ResponseEntity<Void> syncMember() {
        List<CrawledMemberDto> crawledMembers = crawlerService.getMembers();

        memberService.syncMembers(crawledMembers);
        return ResponseEntity.ok().build();
    }
}
