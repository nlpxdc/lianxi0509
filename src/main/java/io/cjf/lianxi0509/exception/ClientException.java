package io.cjf.lianxi0509.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException extends Exception {
    private Integer errcode;

    public ClientException(Integer errcode, String message){
        super(message);
        this.errcode = errcode;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }
}
