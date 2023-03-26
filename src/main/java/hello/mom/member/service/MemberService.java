package hello.mom.member.service;


import hello.mom.aop.dto.ResponseBodyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.mom.aop.exception.CustomException;
import hello.mom.aop.exception.ErrorCode;
import hello.mom.aop.jwt.TokenProvider;
import hello.mom.member.domain.Member;
import hello.mom.member.dto.request.MemberRequestDto;
import hello.mom.member.dto.request.TokenDto;
import hello.mom.member.dto.response.MemberResponseDto;
import hello.mom.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static hello.mom.aop.exception.ErrorCode.NOT_FOUND_USER_INFO;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ResponseBodyDto responseBodyDto;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    @Value("${KakaoRestApiKey}")
    private String kakaoRestApiKey;
    @Value("${adminCode}")
    private String adminCode;

    @Transactional
    public ResponseEntity<?> signup(MemberRequestDto.Signup signup) {
        if (signup.getRole().ordinal() == 2) {
            if (!signup.getAdminCode().equals(adminCode)) {
                throw new CustomException(ErrorCode.WRONG_ADMIN_CODE);
            }

            return saveMember(signup);
        }

        return saveMember(signup);
    }

    @Transactional
    public ResponseEntity<?> login(MemberRequestDto.Login login,
                                   HttpServletResponse response) {
        Member member = checkMemberByEmail(login.getEmail());

        if (!member.validatePassword(passwordEncoder, login.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        tokenToHeaders(tokenDto, response);

        return responseBodyDto.success(MemberResponseDto.builder()
                        .memberNo(member.getMemberNo())
                        .nick(member.getNick())
                        .email(member.getEmail())
                        .role(member.getRole())
                        .build(),
                member.getNick() + "님 반갑습니다 :)");
    }

//    //카카오 로그인
//    public ResponseEntity<?> kakaoLogin(String code,
//                                        HttpServletResponse response) throws JsonProcessingException {
//        // 1. "인가 코드"로 "액세스 토큰" 요청
//        String accessToken = getAccessToken(code);
//
//        // 2. 토큰으로 카카오 API 호출
//        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);
//
//        // 3. 가입 여부 확인 후 비회원일 경우 회원가입
//        Member kakaoUser = checkMemberByKakaoId(kakaoUserInfoDto);

//        // 4. 강제 kakao로그인 처리
//        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        TokenDto tokenDto = tokenProvider.generateToken(authentication);
//
//        tokenToHeaders(tokenDto, response);
//
//        return responseBodyDto.success(
//                KakaoUserResponseDto.builder()
//                        .memberNo(kakaoUser.getMemberNo())
//                        .email(kakaoUser.getEmail())
//                        .nick(kakaoUser.getNick())
//                        .build(),
//                kakaoUser.getNick() + "님 반갑습니다 :)"
//        );
//    }


    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoRestApiKey);
        body.add("redirect_uri", "https://daengtionary.site/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }



//    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//
//        RestTemplate rt = new RestTemplate();
//
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//        Long id = jsonNode.get("id").asLong();
//        String nick = jsonNode.get("properties").get("nick").asText();
//        String email = jsonNode.get("kakao_account").get("email").asText();
//
//        return KakaoUserInfoDto.builder()
//                .kakaoId(id)
//                .email(email)
//                .nick(nick)
//                .build();
//    }


    @Transactional(readOnly = true)
    public Member checkMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        return optionalMember.orElseThrow(
                () -> new CustomException(ErrorCode.WRONG_EMAIL)
        );
    }

//    @Transactional
//    public Member checkMemberByKakaoId(KakaoUserInfoDto kakaoUserInfoDto) {
//        Optional<Member> kakaoUser = memberRepository.findByKakaoId(kakaoUserInfoDto.getKakaoId());
//
//        return kakaoUser.orElseGet(
//                () -> memberRepository.save(
//                        Member.builder()
//                                .email(kakaoUserInfoDto.getEmail())
//                                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
//                                .nick(kakaoUserInfoDto.getNick())
//                                .kakaoId(kakaoUserInfoDto.getKakaoId())
//                                .role(Authority.USER)
//                                .build()
//                )
//        );
//    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> checkDuplicateByEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        return responseBodyDto.success("사용 가능한 email 입니다.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> checkDuplicateByNick(String nick) {
        if (memberRepository.existsByNick(nick)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICK);
        }

        return responseBodyDto.success("사용 가능한 닉네임 입니다.");
    }

    @Transactional(readOnly = true)
    public Member checkMemberByMemberNo(Long memberNo) {
        return memberRepository.findById(memberNo).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER_INFO)
        );
    }

    @Transactional(readOnly = true)
    public Member checkMemberByNick(String nick) {
        return memberRepository.findByNick(nick).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER_INFO)
        );
    }

    @Transactional
    public ResponseEntity<?> saveMember(MemberRequestDto.Signup signup) {
        Member member = Member.builder()
                .email(signup.getEmail())
                .password(passwordEncoder.encode(signup.getPassword()))
                .nick(signup.getNick())
                .role(signup.getRole())
                .build();

        memberRepository.save(member);

        return responseBodyDto.success(member.getNick() + "님 가입을 축하힙니다 :)");
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}