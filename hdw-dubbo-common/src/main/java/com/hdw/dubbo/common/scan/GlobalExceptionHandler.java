package com.hdw.dubbo.common.scan;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.hdw.dubbo.common.result.Result;
import com.hdw.dubbo.common.util.WebUtils;



/**
 * 
 * @description 全局异常拦截处理器
 * @date 2018年4月2日下午3:19:49
 * @author TuMinglong
 * @version 1.0.0
 */
@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
public class GlobalExceptionHandler {

	protected static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler: Code: %s Detail: %s";

	// 运行异常
	@ExceptionHandler(RuntimeException.class)
	public Result runtimeExecptionHandler(RuntimeException ex) {
		return resultFormat(1,ex);
	}

	// 空指针异常
	@ExceptionHandler(NullPointerException.class)
	public Result nullPointerExceptionHandler(NullPointerException ex) {
		return resultFormat(2,ex);
	}

	// 类型转换异常
	@ExceptionHandler(ClassCastException.class)
	public Result classCastExceptionHandler(ClassCastException ex) {
		return resultFormat(3,ex);
	}

	// IO异常
	@ExceptionHandler(IOException.class)
	public Result iOExceptionHandler(IOException ex) {
		return resultFormat(4,ex);
	}

	// 未知方法异常
	@ExceptionHandler(NoSuchMethodException.class)
	public Result noSuchMethodExceptionHandler(NoSuchMethodException ex) {
		return resultFormat(5,ex);
	}

	// 数组越界异常
	@ExceptionHandler(IndexOutOfBoundsException.class)
	public Result indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
		return resultFormat(6,ex);
	}

	// 400错误
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public Result requestNotReadable(HttpMessageNotReadableException ex) {
		System.out.println("400..requestNotReadable");
		return resultFormat(7,ex);
	}

	// 400错误
	@ExceptionHandler({ TypeMismatchException.class })
	public Result requestTypeMismatch(TypeMismatchException ex) {
		System.out.println("400..TypeMismatchException");
		return resultFormat(8,ex);
	}

	// 400错误
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public Result requestMissingServletRequest(MissingServletRequestParameterException ex) {
		System.out.println("400..MissingServletRequestParameterException");
		return resultFormat(9,ex);
	}

	// 405错误
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public Result request405(HttpRequestMethodNotSupportedException ex) {
		return resultFormat(10,ex);
	}

	// 406错误
	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
	public Result request406(HttpMediaTypeNotAcceptableException ex) {

		return resultFormat(11,ex);
	}

	// 500错误
	@ExceptionHandler({ ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
	public Result server500(RuntimeException ex) {
		return resultFormat(12,ex);
	}

	// 栈溢出
	@ExceptionHandler({ StackOverflowError.class })
	public Result requestStackOverflow(StackOverflowError ex) {
		return resultFormat(13,ex);
	}

	@ExceptionHandler({ Exception.class })
	public Result resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		// 非控制器请求异常
		if (!(handler instanceof HandlerMethod)) {
			return resultFormat(14,e);
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		if (WebUtils.isAjax(handlerMethod)) {
			return resultFormat(14,e);
		}

		// 上传文件大小超过限制异常
		if (e instanceof MaxUploadSizeExceededException) {
			return resultFormat(14,e);
		}

		return resultFormat(14,e);
	}

	// hibernate validator异常
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
		return resultFormat(15,ex);
	}

	@ExceptionHandler(BindException.class)
	public Result bindExceptionHandler(BindException ex) {
		return resultFormat(16,ex);
	}

	private <T extends Throwable> Result resultFormat(Integer code,T ex) {
		ex.printStackTrace();
		logger.error(String.format(logExceptionFormat, code, ex.getMessage()));
		return new Result(code, false, ex.getMessage());
	}

}
