package com.geukrock.geukrockapiserver.meeting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.meeting.entity.Meeting;
import com.geukrock.geukrockapiserver.meeting.repository.MeetingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = false)
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public List<Meeting> getMeetings() {
        return meetingRepository.findAll();
    }

    public void syncMeeting(List<CrawledMeetingDto> crawledMeetingDtos) {
        List<Meeting> list = crawledMeetingDtos.stream().map(Meeting::new).toList();
        meetingRepository.saveAll(list);
    }
}
