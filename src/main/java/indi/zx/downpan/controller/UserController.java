package indi.zx.downpan.controller;

import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.service.UserService;
import indi.zx.downpan.support.util.ResponseUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 14:27
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Response register(@RequestBody Map<String, String> user) {
        userService.save(user);
        return ResponseUtil.success();
    }

    @PostMapping("/updateUser")
    public Response updateUser(@RequestBody UserEntity user, MultipartFile file) {
        userService.updateUser(user, file);
        return ResponseUtil.success();
    }

    @GetMapping("/getUserInfo")
    public Response getUserInfo(HttpServletResponse response) {
        JSONObject result = userService.getUserInfo(response);
        return ResponseUtil.success(result);
    }
}
