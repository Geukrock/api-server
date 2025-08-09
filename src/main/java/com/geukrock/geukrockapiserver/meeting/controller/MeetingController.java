package com.geukrock.geukrockapiserver.meeting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.crawler.service.CrawlerService;
import com.geukrock.geukrockapiserver.meeting.entity.Meeting;
import com.geukrock.geukrockapiserver.meeting.service.MeetingService;
import com.geukrock.geukrockapiserver.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;
    private final CrawlerService crawlerService; 
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Meeting>> getMeetings() {
        List<Meeting> meetings = meetingService.getMeetings();
        return ResponseEntity.ok(meetings);
    }
    
    @PostMapping("/sync")
    public ResponseEntity<Void> syncMeetingEntity() {
        List<CrawledMemberDto> crawledMembers = crawlerService.getMembers();
        memberService.syncMembers(crawledMembers);
        
        List<CrawledMeetingDto> meetings = crawlerService.getMeetings();
        meetingService.syncMeeting(meetings);


        // List<CrawledMeetingDto> crawledMeetings = crawlerService.getMeetings();
        // meetingService.syncMeeting(crawledMeetings);
        return ResponseEntity.ok().build();
    }
}
