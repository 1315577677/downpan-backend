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
    public Response fileUpload(MultipartFile files, @PathVariable("parent") String parent) {
        fileService.upload(files, parent.replace(".", "/"));
        return ResponseUtil.success();
    }

    @GetMapping("/getData/{dir}/{orderBy}")
    public Response getData(@PathVariable("dir") String dir, @PathVariable("orderBy") String orderBy) {
        return ResponseUtil.success(fileService.getData(dir, orderBy));
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

    @GetMapping("/createDir/{parent}/{name}")
    public Response createDir(@PathVariable("parent") String parent, @PathVariable("name") String name) {
        fileService.createDir(parent.replace(".", "/"), name);
        return ResponseUtil.success();
    }

    @GetMapping("/search/{name}")
    public Response searchFile(@PathVariable("name") String name) {
        return ResponseUtil.success(fileService.findByName(name));
    }

    @GetMapping("/getBackFile")
    public Response getBackFile() {
         fileService.getBackFile();
        return ResponseUtil.success();
    }
}
