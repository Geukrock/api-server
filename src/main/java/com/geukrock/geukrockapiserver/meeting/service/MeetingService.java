package com.geukrock.geukrockapiserver.meeting.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        for (CrawledMeetingDto crawledMeetingDto : crawledMeetingDtos) {
            Optional<Meeting> existingMeetingOpt = meetingRepository.findByTitle(crawledMeetingDto.getTitle());

            Meeting meeting;
            if (existingMeetingOpt.isPresent()) {
                meeting = existingMeetingOpt.get();
            } else {
                // 새로 만드는 경우
                meeting = Meeting.builder()
                        .title(crawledMeetingDto.getTitle())
                        .date(crawledMeetingDto.getDate())
                        .location(crawledMeetingDto.getLocation())
                        .build();
                meetingRepository.save(meeting); // 저장해서 영속 상태로 만들기
            }

            // 크롤링한 멤버 리스트로 MeetingMember 생성 (모두 동일한 영속 meeting 객체 참조)
            List<MeetingMember> meetingMembers = new ArrayList<>();
            for (String profileUrl : crawledMeetingDto.getJoinMemberProfileUrl()) {
                if (profileUrl.equals("/default_fac.png"))
                    continue;

                Member joinMember = memberRepository.findByProfileUrl(profileUrl);
                MeetingMember meetingMember = MeetingMember.builder()
                        .member(joinMember)
                        .meeting(meeting)
                        .build();
                meetingMembers.add(meetingMember);
            }

            if (existingMeetingOpt.isPresent()) {
                List<MeetingMember> dbMeetingMembers = meeting.getMeetingMembers();

                // 삭제할 멤버 찾기
                List<MeetingMember> removedMembers = dbMeetingMembers.stream()
                        .filter(dbMember -> meetingMembers.stream()
                                .noneMatch(newMember -> newMember.getMember().getId()
                                        .equals(dbMember.getMember().getId())))
                        .toList();
                meetingMemberRepository.deleteAll(removedMembers);

                // 추가할 멤버 찾기
                List<MeetingMember> addMembers = meetingMembers.stream()
                        .filter(newMember -> dbMeetingMembers.stream()
                                .noneMatch(
                                        dbMember -> dbMember.getMember().getId().equals(newMember.getMember().getId())))
                        .toList();
                meetingMemberRepository.saveAll(addMembers);
            } else {
                meetingMemberRepository.saveAll(meetingMembers);
            }
        }
    }
}
