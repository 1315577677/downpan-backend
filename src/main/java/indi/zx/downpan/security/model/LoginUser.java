package indi.zx.downpan.security.model;

import lombok.Data;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
@Data
public class LoginUser {

    private String username;
    private String password;
    private String rePassword;
    private Integer rememberMe;
}
