package com.geukrock.geukrockapiserver.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.crawler.service.CrawlerService;
import com.geukrock.geukrockapiserver.meeting.service.MeetingService;
import com.geukrock.geukrockapiserver.member.dto.MemberDetailResDto;
import com.geukrock.geukrockapiserver.member.dto.MemberResDto;
import com.geukrock.geukrockapiserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final CrawlerService crawlerService;

    @GetMapping
    public ResponseEntity<List<MemberResDto>> getMembers() {
        List<MemberResDto> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }
    
    @PostMapping("/sync")
    public ResponseEntity<List<MemberDetailResDto>> syncMember() {
        List<CrawledMemberDto> crawledMembers = crawlerService.getMembers();
        List<MemberDetailResDto> syncMembers = memberService.syncMembers(crawledMembers);
        return ResponseEntity.ok(syncMembers);
    }

    @GetMapping("/detail")
    public ResponseEntity<List<MemberDetailResDto>> getMemberDetails() {
        List<MemberDetailResDto> memberDetails = memberService.getMemberDetails();
        return ResponseEntity.ok(memberDetails);
    }
}
