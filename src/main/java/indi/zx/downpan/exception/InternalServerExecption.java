package indi.zx.downpan.exception;

import afu.org.checkerframework.checker.oigj.qual.I;
import indi.zx.downpan.common.constants.GlobalConstants;
import lombok.Getter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:27
 */
public class InternalServerExecption  extends  RuntimeException{

    @Getter
    private final int code;

    public InternalServerExecption(){
        super(GlobalConstants.Res.UNKNOW_EXECPTION.getMsg());
        this.code = GlobalConstants.Res.UNKNOW_EXECPTION.getCode();
    }

    public InternalServerExecption(int code,String message){
        super(message);
        this.code = code;
    }

    public InternalServerExecption (GlobalConstants.Res res){
        super(res.getMsg());
        this.code = res.getCode();
    }
}
