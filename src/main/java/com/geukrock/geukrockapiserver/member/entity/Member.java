package com.geukrock.geukrockapiserver.member.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.meeting.entity.MeetingMember;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String somoimName;
    LocalDate birthDate;
    LocalDate joinDate;

    @Column(name = "profile_url")
    String profileUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<MeetingMember> meetingMembers = new ArrayList<>();

    public Member(CrawledMemberDto dto) {
        this.somoimName = dto.getSomoimName();
        this.birthDate = dto.getBirthDate();
        this.profileUrl = dto.getProfileUrl();
    }

    public boolean isSameContent(Member member) {
        return this.somoimName.equals(member.somoimName) &&
                this.birthDate.equals(member.birthDate) &&
                this.profileUrl.equals(member.profileUrl);
    }
}
