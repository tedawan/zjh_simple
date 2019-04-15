package com.addplus.server.oss.service.impl;

import com.addplus.server.api.utils.date.DateUtils;
import com.addplus.server.oss.OssClientUtils;
import com.addplus.server.oss.config.OssConfig;
import com.addplus.server.oss.model.Policy;
import com.addplus.server.oss.model.ReturnPolicy;
import com.addplus.server.oss.service.OssService;
import com.addplus.server.oss.uploader.FileUploader;
import com.addplus.server.oss.utils.FileUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fuyq
 * @date 2018/8/30
 */
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OssConfig ossConfig;

    @Override
    public ReturnPolicy createPolicy(String fileName, Boolean randomName) {

        // 获取文件名
        String key = FileUtils.getUploadPath(fileName, randomName);
        String dir = FileUtils.getFileType().get(FileUtils.getSuffix(fileName));

        OSS oss = OssClientUtils.createOssClient();
        try {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            LocalDateTime localDateTimeExpireTime = localDateTime.plusSeconds(60);
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            Date date = DateUtils.localDateToDateTime(localDateTimeExpireTime.plusHours(8));
            String postPolicy = oss.generatePostPolicy(date, policyConditions);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = oss.calculatePostSignature(postPolicy);

            Policy policy = new Policy();
            policy.setOSSAccessKeyId(OssClientUtils.getAccessKeyId());
            policy.setPolicy(encodedPolicy);
            policy.setSignature(postSignature);
            policy.setExpire(String.valueOf(localDateTimeExpireTime.toEpochSecond(ZoneOffset.of("+8"))));
            policy.setKey(key);
            policy.setDir(dir);
            String host = "https://" + OssClientUtils.getBucket() + "." + OssClientUtils.getEndpoint();
            policy.setHost(host);
            ReturnPolicy returnPolicy = new ReturnPolicy();
            returnPolicy.setDataSet(policy);
            returnPolicy.setErrorInfo("success");
            returnPolicy.setReturnCode("000000");
            return returnPolicy;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> uploadFile(List<MultipartFile> files, Boolean randomName, Boolean fullPath) {
        List<String> fileList = null;
        if (files != null && files.size() > 0) {
            fileList = new ArrayList<>(files.size());
            String filePath = "";
            FileUploader fileUploader = new FileUploader(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret(), ossConfig.getEndpoint(), ossConfig.getBucket());
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    filePath = fileUploader.multipartFileUpload(file, randomName);
                    if (fullPath != null && fullPath) {
                        filePath = OssClientUtils.getServerUrl(filePath, ossConfig.getEndpoint(), ossConfig.getBucket());
                    }
                    fileList.add(filePath);
                }
            }
            fileUploader.close();
        }
        return fileList;
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> files, Integer width, Integer height, Boolean fullPath) {
        List<String> fileList = null;
        if (files != null && files.size() > 0) {
            fileList = new ArrayList<>(files.size());
            String filePath = "";
            FileUploader fileUploader = new FileUploader(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret(), ossConfig.getEndpoint(), ossConfig.getBucket());
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    filePath = fileUploader.multipartFileUpload(file);
                    if (width != null && height != null) {
                        filePath = OssClientUtils.imageThumbnail(filePath, width, height);
                    }
                    if (fullPath != null && fullPath) {
                        filePath = OssClientUtils.getServerUrl(filePath, ossConfig.getEndpoint(), ossConfig.getBucket());
                    }
                    fileList.add(filePath);
                }
            }
            fileUploader.close();
        }
        return fileList;
    }

    @Override
    public List<String> upload(List<MultipartFile> files, Integer width, Integer height, Boolean fullPath){
        List<String> fileList = null;
        if (files != null && files.size() > 0) {
            OSS ossClient = null;
            try {
                ossClient = OssClientUtils.createOssClient();
                ObjectMetadata objectMetadata = null;
                fileList = new ArrayList<>(files.size());
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        objectMetadata = new ObjectMetadata();
                        String contentType = FileUtils.getFileContentType(file.getOriginalFilename());
                        objectMetadata.setContentType(contentType);
                        String key = FileUtils.getUploadPath(file.getOriginalFilename(), true);
                        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucket(), key, file.getInputStream(), objectMetadata);
                        ossClient.putObject(putObjectRequest);
                        if (width != null && height != null) {
                            key = OssClientUtils.imageThumbnail(key, width, height);
                        }
                        if (fullPath != null && fullPath) {
                            key = OssClientUtils.getServerUrl(key, ossConfig.getEndpoint(), ossConfig.getBucket());
                        }
                        fileList.add(key);
                    }
                }
                return fileList;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
        return null;
    }
}
