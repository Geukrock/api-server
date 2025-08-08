package com.geukrock.geukrockapiserver.scheduler;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.crawler.service.CrawlerService;
import com.geukrock.geukrockapiserver.meeting.service.MeetingService;
import com.geukrock.geukrockapiserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyScheduler {
    private final CrawlerService crawlerService;
    private final MemberService memberService;
    private final MeetingService meetingService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void syncSomoimInfo() {
        List<CrawledMemberDto> crawledMembers = crawlerService.getMembers();
        memberService.syncMembers(crawledMembers);
        
        List<CrawledMeetingDto> meetings = crawlerService.getMeetings();
        meetingService.syncMeeting(meetings);
        
        log.info("syncSomoimInfo");
    }
}
