package com.geukrock.geukrockapiserver.meeting.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    LocalDate date;
    String location;
    
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<MeetingMember> meetingMembers = new ArrayList<>();
    
    // public Meeting(CrawledMeetingDto dto){
    //     this.title = dto.getTitle();
    //     this.date = dto.getDate();
    //     this.location = dto.getLocation();
    // }
}
