package com.geukrock.geukrockapiserver.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geukrock.geukrockapiserver.member.dto.MemberReqDto;
import com.geukrock.geukrockapiserver.member.entity.Member;
import com.geukrock.geukrockapiserver.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = false)
public class MemberService {
    private final MemberRepository memberRepository;

    public void addMembers(List<MemberReqDto> dtos) {
        List<Member> members = dtos.stream().map(Member::new).toList();
        memberRepository.saveAll(members);
    }

    public void addMember(MemberReqDto dto) {
        memberRepository.save(new Member(dto));
    }

    public void syncMembers(List<Member> crawledMembers) {
        
    }
}
