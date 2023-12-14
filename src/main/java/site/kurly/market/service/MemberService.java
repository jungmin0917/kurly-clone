package site.kurly.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import site.kurly.market.domain.Member;
import site.kurly.market.dto.AddMemberRequestDTO;
import site.kurly.market.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입 처리
    public Long save(AddMemberRequestDTO dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return memberRepository.save(Member.builder()
                .auth(dto.getAuth())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .addressPostcode(dto.getAddressPostcode())
                .gender(dto.getGender())
                .birthday(dto.getBirthday())
                .build()).getNo();
    }

    // memberNo로 유저 반환
    public Member findById(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    // 이메일로 찾아 유저 반환
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
