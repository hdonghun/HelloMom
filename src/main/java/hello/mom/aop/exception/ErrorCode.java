package hello.mom.aop.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(HttpStatus.OK.value(), "OK", "true"),

    //문자열 체크
    NOT_VALIDCONTENT(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", "유효하지 않는 내용입니다."),


    //회원가입
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST.value(), "DUPLICATE_EMAIL", "이미 사용중인 email 입니다."),
    DUPLICATE_NICK(HttpStatus.BAD_REQUEST.value(), "DUPLICATE_NICK", "이미 사용중인 닉네임 입니다."),
    WRONG_ADMIN_CODE(HttpStatus.BAD_REQUEST.value(), "WRONG_ADMINCODE", "관리자 코드가 일치하지 않습니다."),


    //TOKEN
    INVAILID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", "권한 정보가 없는 token 입니다."),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST.value(), "NOT_FOUND_TOKEN", "존재하지 않는 Token 입니다."),


    //로그인
    WRONG_EMAIL(HttpStatus.BAD_REQUEST.value(), "WRONG_EMAIL", "존재하지 않는 email 입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST.value(), "WRONG_PASSWORD", "비밀번호가 일치하지 않습니다."),


    //기타
    NOT_FOUND_USER_INFO(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "해당 유저가 존재하지 않습니다"),
    WRONG_INPUT_CONTENT(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "UNSUPPORTED_MEDIA_TYPE", "업로드 타입이 잘못되었습니다"),


    //게시글
    MAP_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "해당 게시물을 찾을 수 없습니다."),
    MAP_WRONG_ACCESS(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", "본인의 게시글이 아닙니다."),

    //friend
    FRIEND_IS_NOT_MEMBER(HttpStatus.BAD_REQUEST.value(),"BAD_REQUEST","본인은 참가할수 없습니다."),


    //댓글
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "해당 게시물을 찾을 수 없습니다."),

    //챗팅
    CANNOT_CHAT_WITH_ME(HttpStatus.BAD_REQUEST.value(), "CANNOT_CHAT_WITH_ME", "나 자신은 채팅의 대상이 될 수 없습니다."),
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_CHAT_ROOM", "해당 채팅방은 존재하지 않습니다."),
    NOT_FOUND_CHAT_ROOM_MEMBER(HttpStatus.NOT_FOUND.value(), "NOT_FOUND_CHAT_ROOM_MEMBER", "해당 참여자를 찾을 수 없습니다."),


    //이미지
    IMAGE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", "이미지 업로드에 실패했습니다"),
    WRONG_IMAGE_FORMAT(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", "지원하지 않는 파일 형식입니다."),

    // 반려견
    NOT_FOUND_DOG_INFO(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", "반려견 정보가 없습니다.");


    private final Integer status;
    private final String code;
    private final String message;

}