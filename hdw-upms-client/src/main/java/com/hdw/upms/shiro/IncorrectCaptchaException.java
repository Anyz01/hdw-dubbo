package com.hdw.upms.shiro;

import org.apache.shiro.authc.AuthenticationException;


/**
 * 
 * @Descriptin 验证码错误异常
 * @author TuMinglong
 * @Date 2018年5月1日 下午2:41:28
 */
public class IncorrectCaptchaException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public IncorrectCaptchaException() {
        super();
    }

    public IncorrectCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectCaptchaException(String message) {
        super(message);
    }

    public IncorrectCaptchaException(Throwable cause) {
        super(cause);
    }

}
