package com.geukrock.geukrockapiserver.meeting.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.geukrock.geukrockapiserver.meeting.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting,Long>{

    @Query("select m from Meeting m left join fetch m.meetingMembers where m.title = :title")
    public Optional<Meeting> findByTitle(@Param("title") String title);
}
