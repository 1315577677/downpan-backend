package indi.zx.downpan.controller;

import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.support.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-09 11:16
 */
@RestController
public class initUrlController {

    @PostMapping("/signin/check")
    public String initUrl(@RequestBody String param) {

        return "{\n" +
                "    \"status\":200,\n" +
                "    \"name\":\"hahah\",\n" +
                "    \"imgUrl\":\"\",\n" +
                "    \"id\":\"123456\",\n" +
                "    \"token\":\"77582512456\",\n" +
                "    \"identity\":{\n" +
                "    \"status\":200,\n" +
                "    \"name\":\"hahah\",\n" +
                "    \"imgUrl\":\"\",\n" +
                "    \"id\":\"123456\",\n" +
                "    \"token\":\"77582512456\"\n" +
                "    }\n" +
                "}";
    }

    @PostMapping("/lists/getlist")
    public String list(@RequestBody String param) {

        return "{\n" +
                "    \"status\":200,\n" +
                "    \"name\":\"hahah\",\n" +
                "    \"imgUrl\":\"\",\n" +
                "    \"id\":\"123456\",\n" +
                "    \"token\":\"77582512456\",\n" +
                "    \"nickName\":\"几句\",\n" +
                "    \"lastTime\":\"2020-10-10\",\n" +
                "    \"result\":[{\n" +
                "        \"status\":200,\n" +
                "    \"name\":\"hahah\",\n" +
                "    \"imgUrl\":\"\",\n" +
                "    \"id\":\"123456\",\n" +
                "    \"token\":\"77582512456\",\n" +
                "    \"nickName\":\"几句\",\n" +
                "    \"lastTime\":\"2020-10-10\"\n" +
                "    }]\n" +
                "}";
    }
}
