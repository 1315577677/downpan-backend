package indi.zx.downpan.service.impl;

import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.repository.UserRepository;
import indi.zx.downpan.service.UserService;
import indi.zx.downpan.support.util.CheckUtil;
import indi.zx.downpan.support.util.MessageUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:19
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final Long CAPACITY = 104857600L;
    @Autowired
    public UserServiceImpl(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void updateUser(UserEntity user, MultipartFile file) {
        CheckUtil.ckeckEmpty(user.getUsername(), "账号不能为空");
        UserEntity userByUsername = findUserByUsername(user.getUsername());
        String oldPass = userByUsername.getPassword();
        String newPass = user.getPassword();
        if (!bCryptPasswordEncoder.matches(newPass, oldPass)) {
            user.setPassword(newPass);
        }
        userRepository.save(user);
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findUserEntityByUsername(username);
    }

    @Override
    public UserEntity save(Map<String, String> map) {
        UserEntity user = new UserEntity();
        String password = map.get("password");
        String repassword = map.get("rePassword");
        String username = map.get("username");

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setDiskCapacity(CAPACITY);
        user.setUsername(username);
        UserEntity save = null;
        if (!password.equals(repassword)) {
            MessageUtil.parameter("密码不一致");
        }
        if (username.length() < 6 || password.length() < 6) {
            MessageUtil.parameter("账号或密码至少6个字符");
        }
        try {
            save = userRepository.save(user);
        } catch (Exception e) {
            MessageUtil.parameter("该账号已被注册");
        }
        return save;
    }

    @Override
    public JSONObject getUserInfo(HttpServletResponse response) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        UserEntity user = userRepository.findUserEntityByUsername(currentUsername);
        JSONObject result = new JSONObject();
        result.put("username", user.getUsername());
        result.put("capacity", user.getDiskCapacity());
        result.put("used",user.getUsed());
        return result;
    }
}
