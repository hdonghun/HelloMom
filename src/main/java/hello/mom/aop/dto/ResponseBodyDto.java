package hello.mom.aop.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseBodyDto {
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Body {
        private Integer state;
        private Object data;
        private String message;
    }

    public ResponseEntity<?> success(Object data, String message) {
        Body body = Body.builder()
                .state(HttpStatus.OK.value())
                .data(data)
                .message(message)
                .build();

        return ResponseEntity.ok(body);
    }
    public ResponseEntity<?> success(String message) {
        Body body = Body.builder()
                .state(HttpStatus.OK.value())
                .message(message)
                .build();

        return ResponseEntity.ok(body);
    }
}