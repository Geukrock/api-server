package com.geukrock.geukrockapiserver.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.geukrock.geukrockapiserver.meeting.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting,Long>{
    
}
