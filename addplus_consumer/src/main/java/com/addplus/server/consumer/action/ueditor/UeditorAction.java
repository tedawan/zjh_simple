package com.addplus.server.consumer.action.ueditor;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fuyq
 * @date 2018/8/8
 */
@RestController
@RequestMapping(value = "ueditor")
public class UeditorAction {

    @Autowired
    private OssService ossService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ReturnDataSet uploadFile(MultipartHttpServletRequest multipartRequest) {
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            multipartFiles.addAll(entry.getValue());
        }
        List<String> fieList = ossService.upload(multipartFiles, null, null, true);
        ReturnDataSet returnDataSet;
        if (fieList.size() > 0) {
            returnDataSet = new ReturnDataSet(ErrorCode.SYS_SUCCESS, fieList.get(0));
        } else {
            returnDataSet = new ReturnDataSet(ErrorCode.SYS_ERROR_NULLDATE, "");
        }
        return returnDataSet;
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public ReturnDataSet uploadImageFile(Integer width, Integer height, MultipartHttpServletRequest multipartRequest) {
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            multipartFiles.addAll(entry.getValue());
        }
        List<String> imageList = ossService.upload(multipartFiles, width, height, true);
        ReturnDataSet returnDataSet;
        if (imageList.size() > 0) {
            returnDataSet = new ReturnDataSet(ErrorCode.SYS_SUCCESS, imageList.get(0));
        } else {
            returnDataSet = new ReturnDataSet(ErrorCode.SYS_ERROR_NULLDATE, "");
        }
        return returnDataSet;
    }
}
