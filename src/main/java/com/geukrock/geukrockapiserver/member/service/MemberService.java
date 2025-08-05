package com.geukrock.geukrockapiserver.member.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.meetingmember.repository.MeetingMemberRepository;
import com.geukrock.geukrockapiserver.member.dto.MemberDetailResDto;
import com.geukrock.geukrockapiserver.member.dto.MemberResDto;
import com.geukrock.geukrockapiserver.member.entity.Member;
import com.geukrock.geukrockapiserver.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = false)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    @Transactional(readOnly = true)
    public List<MemberResDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberResDto::new).toList();
    }

    public List<MemberDetailResDto> syncMembers(List<CrawledMemberDto> crawledMembers) {
        List<Member> syncMembers = crawledMembers.stream()
                .map(Member::new)
                .toList();

        List<Member> dbMembers = memberRepository.findAll();

        // DB 멤버 맵 (key = 이름_생일)
        Map<String, Member> dbMemberMap = dbMembers.stream()
                .collect(Collectors.toMap(
                        m -> m.getSomoimName() + "_" + m.getBirthDate(),
                        Function.identity()));

        Set<String> crawledKeys = syncMembers.stream()
                .map(m -> m.getSomoimName() + "_" + m.getBirthDate())
                .collect(Collectors.toSet());

        List<Member> toAdd = new ArrayList<>();
        List<Member> toUpdate = new ArrayList<>();
        List<Member> toDelete = new ArrayList<>();

        for (Member syncMember : syncMembers) {
            String key = syncMember.getSomoimName() + "_" + syncMember.getBirthDate();

            if (dbMemberMap.containsKey(key)) {
                Member dbMember = dbMemberMap.get(key);

                if (!syncMember.isSameContent(dbMember)) {
                    // 같은 사람이지만 내용이 다르면 업데이트
                    syncMember.setId(dbMember.getId()); // ID 유지
                    toUpdate.add(syncMember);
                }
            } else {
                // DB에 없으면 추가
                syncMember.setJoinDate(LocalDate.now());
                toAdd.add(syncMember);
            }
        }

        // 삭제 대상: DB에 있는데, 크롤링 결과에 없는 경우
        for (Member dbMember : dbMembers) {
            String key = dbMember.getSomoimName() + "_" + dbMember.getBirthDate();
            if (!crawledKeys.contains(key)) {
                toDelete.add(dbMember);
            }
        }

        memberRepository.saveAll(toAdd);
        memberRepository.saveAll(toUpdate);
        memberRepository.deleteAll(toDelete);

        return getMemberDetails();
    }

    @Transactional(readOnly = true)
    public List<MemberDetailResDto> getMemberDetails() {
        List<MemberDetailResDto> memberDetailResDtos = new ArrayList<>();

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            Integer joinCount = meetingMemberRepository.countByMemberId(member.getId());
            LocalDate lastJoinDate = meetingMemberRepository.findLastJoinDateByMemberId(member.getId());

            MemberDetailResDto memberDetailResDto = MemberDetailResDto.builder()
                    .id(member.getId())
                    .somoimName(member.getSomoimName())
                    .birthDate(member.getBirthDate())
                    .joinDate(member.getJoinDate())
                    .profileUrl(member.getProfileUrl())
                    .joinCount(joinCount)
                    .LastJoinDate(lastJoinDate).build();
            memberDetailResDtos.add(memberDetailResDto);
        }
        return memberDetailResDtos;
    }
}
