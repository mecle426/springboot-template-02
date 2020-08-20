package com.mecle.shiro.handler;

import com.mecle.common_utils.Result;
import com.mecle.service_base.handler.MyExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Slf4j
@Component
public class ShiroExceptionHandler extends MyExceptionHandler {
    // 捕捉shiro的异常
    @ResponseBody
    @ExceptionHandler(ShiroException.class)
    public Result handle401(org.apache.shiro.ShiroException e) {
        return Result.error().code(401).message("你好"+e.getMessage());
    }
    /**
     * 处理Assert的异常
     */
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) throws IOException {
        return Result.error().message(e.getMessage());
    }
    /**
     * @Validated 校验错误异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) throws IOException {
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.error().message(objectError.getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) throws IOException {
        return Result.error().message(e.getMessage());
    }
}
