package com.hdw.common.scan;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.hdw.common.result.Result;
import com.hdw.common.util.BeanUtils;
import com.hdw.common.util.JacksonUtils;
import com.hdw.common.util.WebUtils;

/**
 * @author TuMinglong
 * @version 1.0.0
 * @description 全局异常拦截处理器
 * @date 2018年4月2日下午3:19:49
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    protected static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String logExceptionFormat = "全局异常捕获: Code: %s Detail: %s";

    // 运行异常
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result runtimeExecptionHandler(RuntimeException ex) {
        return resultFormat(1, ex);
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result nullPointerExceptionHandler(NullPointerException ex) {
        return resultFormat(2, ex);
    }

    // 类型转换异常
    @ExceptionHandler(ClassCastException.class)
    @ResponseBody
    public Result classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(3, ex);
    }

    // IO异常
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public Result iOExceptionHandler(IOException ex) {
        return resultFormat(4, ex);
    }

    // 未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseBody
    public Result noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return resultFormat(5, ex);
    }

    // 数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public Result indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return resultFormat(6, ex);
    }

    // 400错误
    @ExceptionHandler(HttpMessageNotReadableException.class)

    @ResponseBody
    public Result requestNotReadable(HttpMessageNotReadableException ex) {
        System.out.println("400..requestNotReadable");
        return resultFormat(7, ex);
    }

    // 400错误
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public Result requestTypeMismatch(TypeMismatchException ex) {
        System.out.println("400..TypeMismatchException");
        return resultFormat(8, ex);
    }

    // 400错误
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result requestMissingServletRequest(MissingServletRequestParameterException ex) {
        System.out.println("400..MissingServletRequestParameterException");
        return resultFormat(9, ex);
    }

    // 405错误
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result request405(HttpRequestMethodNotSupportedException ex) {
        return resultFormat(10, ex);
    }

    // 406错误
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public Result request406(HttpMediaTypeNotAcceptableException ex) {
        return resultFormat(11, ex);
    }

    // 500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public Result server500(RuntimeException ex) {
        return resultFormat(12, ex);
    }

    // 栈溢出
    @ExceptionHandler(StackOverflowError.class)
    @ResponseBody
    public Result requestStackOverflow(StackOverflowError ex) {
        return resultFormat(13, ex);
    }

    // hibernate validator异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return resultFormat(14, ex);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result bindExceptionHandler(BindException ex) {
        return resultFormat(15, ex);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public Result maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException ex) {
        return resultFormat(16, ex);
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception e) {
        // log记录异常
        logger.error(e.getMessage(), e);
        // 非控制器请求照成的异常
        if (!(handler instanceof HandlerMethod)) {
            return new ModelAndView("error/500");
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (WebUtils.isAjax(handlerMethod)) {
            Result result = new Result();
            result.setMsg(e.getMessage());
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setObjectMapper(JacksonUtils.getObjectmapper());
            view.setContentType("text/html;charset=UTF-8");
            return new ModelAndView(view, BeanUtils.toMap(result));
        }

        // 页面指定状态为500，便于上层的resion或者nginx的500页面跳转，由于error/500不适合对用户展示
        return new ModelAndView("error/500").addObject("error", e.getMessage());
    }

    private <T extends Throwable> Result resultFormat(Integer code, T ex) {
        ex.printStackTrace();
        logger.error(String.format(logExceptionFormat, code, ex.getMessage()));
        return new Result(code, false, ex.getMessage());
    }

}
