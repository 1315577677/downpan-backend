package indi.zx.downpan.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.repository.UserRepository;
import indi.zx.downpan.service.UserService;
import indi.zx.downpan.support.util.CheckUtil;
import indi.zx.downpan.support.util.MessageUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:19
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final Long CAPACITY = 107374182400L;

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
        user.setName(randomName());
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

    private String randomName() {
        String[] adj  = GlobalConstants.ADJ;
        String[] n = GlobalConstants.N;
        return  adj[(int) (Math.random()*20)] + n[(int) (Math.random()*20)];
    }

    @Override
    public JSONObject getUserInfo(HttpServletResponse response) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        UserEntity user = userRepository.findUserEntityByUsername(currentUsername);
        JSONObject result = new JSONObject();
        result.put("username", user.getUsername());
        result.put("capacity", user.getDiskCapacity());
        result.put("used", user.getUsed());
        result.put("name",user.getName());
        return result;
    }

    public void addFriend(String username) {
        UserEntity firend = userRepository.findUserEntityByUsername(username);

        List<UserEntity> userEntities = new ArrayList<>();
        if (firend == null){
            MessageUtil.parameter("未找到该用户！");
        }
        if (username.equals(SecurityUtil.getCurrentUsername())){
            MessageUtil.parameter("不能添加自己为好友");
        }

        UserEntity user = userRepository.findUserEntityByUsername(SecurityUtil.getCurrentUsername());
        JSONArray jsonArray = (JSONArray)JSONArray.parse(user.getFriends());
        jsonArray = jsonArray == null ? new JSONArray() : jsonArray;
        jsonArray.forEach(e ->{
            if (e.equals(firend.getId())){
                MessageUtil.parameter("该用户已在你的好友列表中");
            }
        });
        jsonArray.add(firend.getId());
        user.setFriends(jsonArray.toJSONString());
        JSONArray jsonArray1 = (JSONArray)JSONArray.parse(firend.getFriends());
        jsonArray1 = jsonArray1 == null ? new JSONArray() : jsonArray1;
        if(jsonArray.stream().noneMatch(e-> e.equals(user.getId()))) {
            jsonArray1.add(user.getId());
            firend.setFriends(jsonArray1.toJSONString());
            userEntities.add(firend);
        }
        userEntities.add(user);
        userRepository.saveAll(userEntities);
    }

    public List<UserEntity> getFriends() {
        UserEntity userEntity = userRepository.findUserEntityByUsername(SecurityUtil.getCurrentUsername());
        if (userEntity.getFriends() == null){
            return null;
        }
        List<UserEntity>  entities = new LinkedList<>();
        List<String> strings = JSONArray.parseArray(userEntity.getFriends(), String.class);
        for (String username: strings) {
            Optional<UserEntity> optional = userRepository.findById(username);
            optional.ifPresent(entities::add);
        }
         return entities;
    }

    public void deleteFriends(String ids) {
        String[] id = ids.split(",");
        Arrays.stream(id).forEach(e->{
            UserEntity user = userRepository.findUserEntityByUsername(SecurityUtil.getCurrentUsername());
            Optional<UserEntity> byId = userRepository.findById(e);
            JSONArray jsonArray = JSONArray.parseArray(user.getFriends());
            jsonArray.remove(e);
            user.setFriends(jsonArray.toJSONString());
            userRepository.save(user);
            UserEntity friend = byId.get();
            JSONArray jsonArray1 = JSONArray.parseArray(friend.getFriends());
            jsonArray1.remove(user.getId());
            friend.setFriends(jsonArray1.toJSONString());
            userRepository.save(friend);
        });

    }

    private void deleteFormFriend(String friendId, String id){

    }
}
