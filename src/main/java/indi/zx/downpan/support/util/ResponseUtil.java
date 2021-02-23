package indi.zx.downpan.support.util;

import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.common.response.Response;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:34
 */
public class ResponseUtil {

    public static <T> Response<T> success(T data, String message) {
        return new Response<>(GlobalConstants.Res.SUCCESS.getCode(), data, message);
    }

    public static <T> Response<T> success(T data) {
        return success(data, GlobalConstants.Res.SUCCESS.getMsg());
    }

    public static Response<?> success() {
        return success(null, GlobalConstants.Res.SUCCESS.getMsg());
    }

    public static <T> Response<T> failure(int code, String message) {
        return new Response<>(code, null, message);
    }
    public static <T> Response<T> failure(GlobalConstants.Res res) {
        return new Response<>(res.getCode(), null, res.getMsg());
    }
}
