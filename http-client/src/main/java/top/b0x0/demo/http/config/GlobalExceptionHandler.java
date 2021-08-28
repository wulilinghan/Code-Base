package top.b0x0.demo.http.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.b0x0.demo.http.common.R;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TANG
 * @date 2020-11-23
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常 预期以外异常
     *
     * @param ex /
     * @return /
     */
    @ExceptionHandler(Exception.class)
    public R handleUnexpectedServer(Exception ex) {
        log.error("系统异常：", ex);
        return R.fail(ex.getMessage());
    }

    /**
     * 缺少请求参数异常
     *
     * @param ex HttpMessageNotReadableException
     * @return /
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R handleHttpMessageNotReadableException(MissingServletRequestParameterException ex) {
        log.error("缺少请求参数，{}", ex.getMessage());
        return R.fail("缺少必要的请求参数");
    }

    //  Validated 请求参数校验异常处理

    /**
     * <1> 处理 form data方式调用接口校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public R bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return R.fail("collect", collect);
    }

    /**
     * <2> 处理 json 请求体调用接口校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return R.fail("collect", collect);
    }

    /**
     * <3> 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return R.fail("collect", collect);
    }
}
