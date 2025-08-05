package com.geukrock.geukrockapiserver.meetingmember.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.geukrock.geukrockapiserver.meeting.entity.MeetingMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long>{

    @Query("select count(mm) from MeetingMember mm where mm.member.id = :memberId")
    public Integer countByMemberId(@Param("memberId") Long memberId);

    @Query("select max(mm.meeting.date) from MeetingMember mm where mm.id = :memberId")
    public LocalDate findLastJoinDateByMemberId(@Param("memberId") Long memberId);
}
