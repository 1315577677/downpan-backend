package indi.zx.downpan.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 15:46
 */

public interface FileService {
    void upload(MultipartFile[] files);
}
