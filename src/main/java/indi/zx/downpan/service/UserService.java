package indi.zx.downpan.service;

import indi.zx.downpan.entity.UserEntity;
import net.minidev.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:18
 */
public interface UserService {
    void updateUser(UserEntity user, MultipartFile file);

    UserEntity findUserByUsername(String Username);

    UserEntity save(Map<String,String> user);

    JSONObject getUserInfo(HttpServletResponse response);
}
