package indi.zx.downpan.controller;

import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.service.impl.FileServiceImpl;
import indi.zx.downpan.support.util.ResponseUtil;
import net.minidev.json.JSONObject;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 14:36
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private FileServiceImpl fileService;

    @Autowired
    public FileController(FileServiceImpl fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/{parent}")
    public void fileUpload(MultipartFile[] files, @PathVariable("parent") String parent) {
        fileService.upload(files, parent);
    }

    @GetMapping("/getData")
    public Response getData(String dir) {

        return ResponseUtil.success(
                fileService.getData(dir)
//                "\t[{\n" +
//                        "\"type\":\"image\",\"id\":\"444\",\"url\":\"http://127.0.0.1:8888/file/getFile/list.png\",\"ext\":\"\",\"isdir\":0,\"name\":\"123456\",\"createdTime\":\"2020:12:12 10:00:00\"\n" +
//                        "},{\"id\":\"123\",\"type\":\"video\",\"url\":\"http://127.0.0.1:8888/file/getFile/123.mp4\",\"ext\":\"\",\"name\":\"haha\",\"createdTime\":\"5123123\"}]  "
        );
    }

    @GetMapping("/getFile/{id}")
    public void getFile(@PathVariable("id") String id, HttpServletResponse response) {
        fileService.getFile(id, response);
    }

    @PostMapping("/delete")
    public Response deleteFiles(@RequestBody JSONObject json) {
        fileService.deleteFiles(json.getAsString("ids"));
        return ResponseUtil.success();
    }

    @PostMapping("/rename")
    public Response renameFile(@RequestBody JSONObject json) {
        String id = json.getAsString("id");
        String name = json.getAsString("name");
        fileService.update(id, name);
        return ResponseUtil.success();
    }
}
