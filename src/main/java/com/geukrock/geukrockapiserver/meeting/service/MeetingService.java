package com.geukrock.geukrockapiserver.meeting.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.meeting.entity.Meeting;
import com.geukrock.geukrockapiserver.meeting.entity.MeetingMember;
import com.geukrock.geukrockapiserver.meeting.repository.MeetingRepository;
import com.geukrock.geukrockapiserver.meetingmember.repository.MeetingMemberRepository;
import com.geukrock.geukrockapiserver.member.entity.Member;
import com.geukrock.geukrockapiserver.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = false)
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    @Transactional(readOnly = true)
    public List<Meeting> getMeetings() {
        return meetingRepository.findAll();
    }

    public void syncMeeting(List<CrawledMeetingDto> crawledMeetingDtos) {
        // 나머지 값들은 그데로 넣으면 되는데 profileurl을 가지고 맴버를 뒤비에서 조회한 뒤에 meetingmember 리스트로 만들어서
        // 디비에 추가해줘야 함
        for (CrawledMeetingDto crawledMeetingDto : crawledMeetingDtos) {
            List<MeetingMember> meetingMembers = new ArrayList<>();
            Meeting meeting = Meeting.builder()
                    .title(crawledMeetingDto.getTitle())
                    .date(crawledMeetingDto.getDate())
                    .location(crawledMeetingDto.getLocation()).build();

            for (String profileUrl : crawledMeetingDto.getJoinMemberProfileUrl()) {
                if (profileUrl.equals("/default_fac.png")) {
                    continue;
                }

                Member joinMember = memberRepository.findByProfileUrl(profileUrl);
                MeetingMember meetingMember = MeetingMember.builder()
                        .member(joinMember)
                        .meeting(meeting).build();
                meetingMembers.add(meetingMember);
            }
            meetingRepository.save(meeting);
            meetingMemberRepository.saveAll(meetingMembers);    
        }
    }
}
