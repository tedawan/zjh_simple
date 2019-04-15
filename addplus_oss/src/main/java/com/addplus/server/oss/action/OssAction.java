package com.addplus.server.oss.action;

import com.addplus.server.oss.model.PolicyParam;
import com.addplus.server.oss.model.ReturnPolicy;
import com.addplus.server.oss.service.OssService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fuyq
 * @date 2018/8/30
 */
@RestController
@RequestMapping(value = "oss")
public class OssAction {

    @Autowired
    private OssService ossService;

    private final String SYS_SUCCESS = "000000";

    @RequestMapping(value = "createPolicy", method = RequestMethod.GET)
    public ReturnPolicy createPolicy(String fileName, Boolean randomName) {
        return ossService.createPolicy(fileName, randomName);
    }

    @PostMapping(value = "createPolicyWeb", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ReturnPolicy createPolicyWeb(@RequestBody PolicyParam policyParam) {
        return ossService.createPolicy(policyParam.getFileName(), policyParam.getRandomName());
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ReturnPolicy uploadFile(MultipartHttpServletRequest multipartRequest) {
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            multipartFiles.addAll(entry.getValue());
        }
        String randomName = multipartRequest.getParameter("randomName");
        boolean rn = true;
        if (StringUtils.isNotBlank(randomName)) {
            rn = "true".equals(randomName);
        }
        return new ReturnPolicy(SYS_SUCCESS, ossService.uploadFile(multipartFiles, rn, false));
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public ReturnPolicy uploadImageFile(Integer width, Integer height, MultipartHttpServletRequest multipartRequest) {
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            multipartFiles.addAll(entry.getValue());
        }
        return new ReturnPolicy(SYS_SUCCESS, ossService.uploadImage(multipartFiles, width, height, false));
    }
}
