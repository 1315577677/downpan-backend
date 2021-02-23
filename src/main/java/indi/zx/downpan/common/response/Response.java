package indi.zx.downpan.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 13:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response <T>{

    private  int code;

    private T Data;

    private String message;

}
