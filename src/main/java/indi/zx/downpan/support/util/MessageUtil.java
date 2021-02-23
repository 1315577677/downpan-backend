package indi.zx.downpan.support.util;

import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.exception.InternalServerExecption;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 15:00
 */
public class MessageUtil {
    public static void parameter(String message){
        throw new InternalServerExecption(GlobalConstants.Res.PARAMETER_ERROR.getCode(),message);
    }
}
