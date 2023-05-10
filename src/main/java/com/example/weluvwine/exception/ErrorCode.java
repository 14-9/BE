package com.example.weluvwine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    INCORRECT_ADMIN_KEY(HttpStatus.BAD_REQUEST, "ADMIN 정보가 일치하지 않습니다."),
    DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임 입니다."),
    NOT_AUTHORIZED_USER(HttpStatus.BAD_REQUEST, "작성자만 수정,삭제할 수 있습니다."),
    INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST,"이메일 코드가 잘못되었습니다." ),
    //404 NOT_FOUND,
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
<<<<<<< Updated upstream
    WINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 와인을 찾을 수 없습니다.");
=======
    NON_CONTENT(HttpStatus.BAD_REQUEST, "글의 내용이 없습니다."),
    WINE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 와인을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 이메일을 찾을 수 없습니다." );
>>>>>>> Stashed changes
    private final HttpStatus httpStatus;
    private final String detail;

}
