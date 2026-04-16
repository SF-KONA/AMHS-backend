package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.config.jwt.JwtTokenProvider;
import com.smartfactory.predictive.backend.domain.entity.Member;
import com.smartfactory.predictive.backend.dto.AuthResponseDto;
import com.smartfactory.predictive.backend.dto.LoginRequestDto;
import com.smartfactory.predictive.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getLoginId(), member.getRole());

        return new AuthResponseDto(true, "로그인 성공", token);
    }
}