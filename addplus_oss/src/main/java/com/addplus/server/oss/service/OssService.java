package com.addplus.server.oss.service;

import com.addplus.server.oss.model.ReturnPolicy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author fuyq
 * @date 2018/8/30
 */
public interface OssService {

    ReturnPolicy createPolicy(String fileName, Boolean randomName);

    /**
     * 文件上传
     *
     * @param files 文件
     * @return 路径数组
     * @author fuyq
     * @date 2018/06/21
     */
    List<String> uploadFile(List<MultipartFile> files, Boolean randomName, Boolean fullPath);

    /**
     * 图片上传
     *
     * @param files  图片
     * @param width  宽
     * @param height 高
     * @return 路径数组
     * @author fuyq
     * @date 2018/06/21
     */
    List<String> uploadImage(List<MultipartFile> files, Integer width, Integer height, Boolean fullPath);

    /**
     * 简单上传
     *
     * @param files  图片
     * @param width  宽
     * @param height 高
     * @return 路径数组
     * @author fuyq
     * @date 2018/11/08
     */
    List<String> upload(List<MultipartFile> files, Integer width, Integer height, Boolean fullPath);
}
