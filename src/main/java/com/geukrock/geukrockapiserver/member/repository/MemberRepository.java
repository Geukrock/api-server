package com.geukrock.geukrockapiserver.member.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geukrock.geukrockapiserver.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{
    
    
}
