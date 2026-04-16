package com.smartfactory.predictive.backend.repository;

import com.smartfactory.predictive.backend.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}