package hello.mom.member.controller;


import hello.mom.member.dto.request.MemberRequestDto;
import hello.mom.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController //@Controller 어노테이션과 @ResponseBody 어노테이션을 합친 것으로, 컨트롤러 클래스의 모든 메소드가 HTTP Response Body에 직접 데이터를 쓰게 됩니다.
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/checkemail")
    public ResponseEntity<?> checkDuplicateByEmail(@RequestParam String email) {
        return memberService.checkDuplicateByEmail(email);
    }

    @GetMapping("/checknick")
    public ResponseEntity<?> checkDuplicateByNick(@RequestParam String nick) {
        return memberService.checkDuplicateByNick(nick);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto.Signup signup) {
        return memberService.signup(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.Login login,
                                   HttpServletResponse response) {
        return memberService.login(login, response);
    }


//    @GetMapping("/kakao")
//    public ResponseEntity<?> kakaoLogin(@RequestParam String code,
//                                        HttpServletResponse response) throws JsonProcessingException {
//        return memberService.kakaoLogin(code, response);
//    }


}