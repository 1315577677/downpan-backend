package indi.zx.downpan.common.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:34
 */

public class GlobalConstants {
    @Getter
    @AllArgsConstructor
    public enum Res{

        SUCCESS(0,"成功"),

        UNKNOW_EXECPTION(-1,"未知异常"),

        SYS_BUSY(-1,"系统繁忙，亲稍后再试"),

        RESOURCE_NOT_FOUND(-4,"资源未找到"),

        UNAUTHORIZED(100001,"未认证"),

        ACCESS_DENIED(100002,"访问被拒绝"),

        TOKEN_INVALID_ERROR(100003,"Token不合法"),

        PARAMETER_ERROR(100004,"参数错误")
        ;

        int code;
        String msg;
    }
    @Getter
    @AllArgsConstructor
    public enum FileType{

        NONE("none"),

        IMAGE("image"),

        VIDEO("video"),

        TEXT("text");

        String type;
    }
}
