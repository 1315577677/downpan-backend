package indi.zx.downpan.support.util;

import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.exception.InternalServerExecption;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-19 16:05
 */
public class CheckUtil {
    public static void  ckeckEmpty(Object value, String message) {

        if (ckeckEmpty(value)) {
            throw new InternalServerExecption(GlobalConstants.Res.PARAMETER_ERROR.getCode(), message);
        }
    }

    public static boolean ckeckEmpty(Object value) {
        boolean empty = false;
        if (value == null || value == "") {
            empty = true;
        }

        if (value instanceof Collection) {
            empty = ((Collection) value).isEmpty();
        }

        if (value instanceof Object[]) {
            empty = ((Object[]) value).length == 0;
        }

        if (value instanceof Map) {
            empty = ((Map) value).isEmpty();
        }

        return empty;
    }

}
