package indi.zx.downpan.controller;

import com.alibaba.fastjson.JSONObject;
import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.service.impl.UserServiceImpl;
import indi.zx.downpan.support.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserServiceImpl userService;


    @Autowired
    public UserController(UserServiceImpl userService) {
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

    @GetMapping("/logout")
    public Response logout() {
        return ResponseUtil.success();
    }

    @PostMapping("/addFriend/{username}")
    public Response addFriend(@PathVariable("username") String username) {
        userService.addFriend(username);
        return ResponseUtil.success();
    }

    @GetMapping("/getFriends")
    public Response getFriends() {
        return ResponseUtil.success(userService.getFriends());
    }

    @PostMapping("/delete")
    public Response deleteFriends(@RequestBody JSONObject json) {
        userService.deleteFriends(json.getString("ids"));
        return ResponseUtil.success();
    }

    @GetMapping("/getIcon/{username}")
    public void getIcon(HttpServletResponse response,@PathVariable("username") String username){
        userService.getUserIcon(response,username);
    }
}
