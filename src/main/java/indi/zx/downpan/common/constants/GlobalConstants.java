package indi.zx.downpan.common.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:34
 */
@Getter
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
        DIR("dir/","dir"),

        NONE("none/","none"),

        IMAGE("image/","image"),

        VIDEO("video/","video"),

        TEXT("text/","text"),

        ZIP("/zip","zip"),

        AUDIO("audio/","audio");

        String type;
        String viewType;
    }

    public static String[] ADJ = new String[]{"飞翔的","奔跑的","可爱的","坚强的","疯狂的","勇敢的","动人的","果断地","团结的","聪明的","娇柔的","温柔的","体贴的","豁达的","忠心的","矜持的","婉丽的","娉婷的","婉顺的","回眸的"};
    public static String[] N = new String[]{"凤梨","兔子","羊驼","大黑牛","波罗斯","大黄","梅子","山羊","狮子","羚羊 ","斑马","海棠","丁香","酸奶","麦片","头发","鼻子","石头","赛车","宝马"};

}
