package indi.zx.downpan.exception;

import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.support.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:29
 */

@Slf4j
@RestControllerAdvice

public class ExecptionHandler {

    @ExceptionHandler(InternalServerExecption.class)
    public Response handInternalServerExecption(InternalServerExecption execption) {
        String message = execption.getMessage();
        message = message == null ? execption.toString() : message;
        log.error(execption.toString());
        if (log.isDebugEnabled()) {
            log.debug("InternalServerExecption", execption);
        }
        return ResponseUtil.failure(execption.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public Response Exception(Exception execption) {
        String message = execption.getMessage();
        message = message == null ? execption.toString() : message;
        log.error(execption.toString());
        if (log.isDebugEnabled()) {
            log.debug("Execption", execption);
        }
        return ResponseUtil.failure(GlobalConstants.Res.UNKNOW_EXECPTION.getCode(), execption.getMessage());
    }
}
