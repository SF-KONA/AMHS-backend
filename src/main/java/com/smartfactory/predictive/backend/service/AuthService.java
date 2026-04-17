package com.smartfactory.predictive.backend.service;

import com.smartfactory.predictive.backend.config.jwt.JwtTokenProvider;
import com.smartfactory.predictive.backend.domain.entity.Member;
import com.smartfactory.predictive.backend.dto.AuthResponseDto;
import com.smartfactory.predictive.backend.dto.LoginRequestDto;
import com.smartfactory.predictive.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto login(LoginRequestDto request) {
        Member member = memberRepository.findById(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(member.getLoginId(), member.getRole());

        return new AuthResponseDto(
                true,
                "로그인 성공",
                token,
                member.getRole().name()
        );
    }
}