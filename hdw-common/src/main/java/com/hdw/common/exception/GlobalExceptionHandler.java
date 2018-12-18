package com.hdw.common.exception;

import com.hdw.common.result.ResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

/**
 * @Description 全局异常拦截处理器
 * @Author TuMinglong
 * @Date 2018/12/10 14:01
 */
@ControllerAdvice
//@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(GlobalException.class)
    public ResultMap handleGlobalException(GlobalException e){
        ResultMap r = new ResultMap();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        return r;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultMap handlerNoFoundException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResultMap handleDuplicateKeyException(DuplicateKeyException e){
        logger.error(e.getMessage(), e);
        return ResultMap.error("数据库中已存在该记录");
    }

    @ExceptionHandler(Exception.class)
    public ResultMap handleException(Exception e){
        logger.error(e.getMessage(), e);
        return ResultMap.error();
    }

    /**
     * 运行异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResultMap handleGlobalException(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "运行异常");
    }

    /**
     * 空指针异常
     * @param e
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResultMap handlerNullPointerException(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "空指针异常");
    }

    /**
     * 类型转换异常
     * @param e
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public ResultMap handlerClassCastException(ClassCastException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "类型转换异常");
    }

    /**
     * IO异常
     * @param e
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ResultMap handlerIOException(IOException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "IO异常");
    }

    /**
     * 未知方法异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public ResultMap handlerNoSuchMethodException(NoSuchMethodException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "未知方法异常");
    }

    /**
     * 数组越界异常
     * @param e
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResultMap handlerIndexOutOfBoundsException(IndexOutOfBoundsException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "数组越界异常");
    }

    /**
     * 400错误
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultMap handlerRequestNotReadable(HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(400, "http消息不可读异常");
    }

    /**
     * 类型匹配异常
     * @param e
     * @return
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResultMap handlerRequestTypeMismatch(TypeMismatchException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(400, "类型匹配异常");
    }

    /**
     * 错误服务请求参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultMap handlerRequestMissingServletRequest(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(400, "错误服务请求参数异常");
    }

    /**
     *不支持的request请求异常
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultMap handlerRequest405(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(405, "不支持的request方法请求异常");
    }

    /**
     * 不可接受的HTTP协议异常
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResultMap handlerRequest406(HttpMediaTypeNotAcceptableException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(406, "不可接受的HTTP协议异常");
    }

    /**
     * 转换不支持异常
     * @param e
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public ResultMap handlerServer500(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "转换不支持异常");
    }

    /**
     * 栈溢出异常
     * @param e
     * @return
     */
    @ExceptionHandler(StackOverflowError.class)
    public ResultMap handlerRequestStackOverflow(StackOverflowError e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "栈溢出异常");
    }

    /**
     * hibernate validator异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultMap handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "hibernate validator异常");
    }

    /**
     * 数据绑定异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResultMap bindExceptionHandler(BindException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "数据绑定异常");
    }

    /**
     * 超过最大上传数量异常
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResultMap maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        logger.error(e.getMessage(), e);
        return ResultMap.error(500, "超过最大上传数量异常");
    }

}
