package indi.zx.downpan.support.util;

import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.security.model.JwtUser;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-18 16:36
 */
public class SecurityUtil {

    private static final ThreadLocal<JwtUser> USER = new NamedInheritableThreadLocal<>("user");

    public static JwtUser getCurrentUser(){
        return USER.get();
    }

    public static String getCurrentUsername(){
        return USER.get().getUsername();
    }

    public static String getCurrentUsernameFromContext(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public static void setConcurrentUser(JwtUser jwtUser){
        USER.remove();
        USER.set(jwtUser);
    }

}
