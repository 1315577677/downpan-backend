package indi.zx.downpan.controller;

import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.support.util.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-09 11:16
 */
@RestController
public class initUrlController {

    @GetMapping("/initUrl")
    public Response initUrl() {
        return ResponseUtil.success();
    }
}
