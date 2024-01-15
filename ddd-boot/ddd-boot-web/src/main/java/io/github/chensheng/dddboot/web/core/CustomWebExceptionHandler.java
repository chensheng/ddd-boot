package io.github.chensheng.dddboot.web.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CustomWebExceptionHandler {
    private final Log logger = LogFactory.getLog(getClass());

    @ExceptionHandler
    public ResponseEntity<CommonResponse> handleException(Throwable except) {
        if (except instanceof BizException) {
           return this.doHandleBusinessException((BizException) except);
        } else if (except instanceof BindException) {
            return this.doHandleBindException((BindException) except);
        } else if (except instanceof MethodArgumentNotValidException) {
            return this.doMethodArgumentNotValidException((MethodArgumentNotValidException) except);
        } else {
            logger.error("System error occurs", except);
            return ResponseEntity.ok(CommonResponse.sysError(except.getMessage() != null ? except.getMessage() : except.toString()));
        }
    }

    private ResponseEntity<CommonResponse> doHandleBusinessException(BizException except) {
        String code = except.getCode();
        String msg = except.getMessage();
        if (isBlank(code)) {
            code = ResponseType.BIZ_ERROR.getCode();
        }
        if (isBlank(msg)) {
            msg = ResponseType.BIZ_ERROR.getMsg();
        }
        return ResponseEntity.ok(new CommonResponse(code, msg));
    }

    private ResponseEntity<CommonResponse> doHandleBindException(BindException except) {
        String msg = ResponseType.BIZ_ERROR.getMsg();

        List<ObjectError> allErrors = except.getAllErrors();
        if (!CollectionUtils.isEmpty(allErrors)) {
            for (ObjectError error : allErrors) {
                if (!isBlank(error.getDefaultMessage())) {
                    msg = error.getDefaultMessage();
                    break;
                }
            }
        }

        return ResponseEntity.ok(CommonResponse.bizError(msg));
    }

    private ResponseEntity<CommonResponse> doMethodArgumentNotValidException(MethodArgumentNotValidException except) {
        String msg = ResponseType.BIZ_ERROR.getMsg();

        if (except.getBindingResult() == null) {
            return ResponseEntity.ok(CommonResponse.bizError(msg));
        }

        List<ObjectError> allErrors = except.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(allErrors)) {
            for (ObjectError error : allErrors) {
                if (!isBlank(error.getDefaultMessage())) {
                    msg = error.getDefaultMessage();
                    break;
                }
            }
        }

        return ResponseEntity.ok(CommonResponse.bizError(msg));
    }

    private boolean isBlank(String text) {
        if(text == null || text.trim().equals("")) {
            return true;
        }

        return false;
    }
}
