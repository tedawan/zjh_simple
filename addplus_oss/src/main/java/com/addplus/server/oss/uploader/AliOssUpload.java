package com.addplus.server.oss.uploader;

import com.addplus.server.oss.utils.FileUtils;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 类描述:
 *
 * @author fyq
 * @version V1.0
 * @date 2017年05月25日 15:47
 */
public class AliOssUpload implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(AliOssUpload.class);

    private byte[] localFileByteArray;

    private long startPos;

    private long partSize;

    private int partNumber;

    private String uploadId;

    private static String key;

    private static String bucketName;

    /**
     * 新建一个List保存每个分块上传后的ETag和PartNumber
     */
    protected static List<PartETag> PART_E_TAGS = Collections.synchronizedList(new ArrayList<>());

    /**
     * 创建构造方法
     *
     * @param localFileByteArray 要上传的文件流
     * @param startPos        每个文件块的开始
     * @param partSize        文件块的大小
     * @param partNumber      文件块的个数
     * @param uploadId        作为块的标识
     * @param key             上传到OSS后的文件名
     */
    public AliOssUpload(byte[] localFileByteArray, long startPos, long partSize, int partNumber, String uploadId, String key, String bucketName) {
        this.localFileByteArray = localFileByteArray;
        this.startPos = startPos;
        this.partSize = partSize;
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        AliOssUpload.key = key;
        AliOssUpload.bucketName = bucketName;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            // 获取文件流
            inputStream = new ByteArrayInputStream(this.localFileByteArray);
            // 跳到每个分块的开头
            inputStream.skip(startPos);

            // 创建UploadPartRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(this.uploadId);
            uploadPartRequest.setInputStream(inputStream);
            uploadPartRequest.setPartSize(this.partSize);
            uploadPartRequest.setPartNumber(this.partNumber);
            UploadPartResult uploadPartResult = FileUploader.ossClient.uploadPart(uploadPartRequest);
            synchronized (PART_E_TAGS) {
                // 将返回的PartETag保存到List中
                PART_E_TAGS.add(uploadPartResult.getPartETag());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 方法描述: 初始化分块上传事件并生成uploadID，用来作为区分分块上传事件的唯一标识
     *
     * @param bucket
     * @param key
     * @author fyq
     * @date 2017年05月24日 09:52:31
     */
    protected static String claimUploadId(String bucket, String key) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(FileUtils.getFileContentType(key));
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, key, objectMetadata);
        InitiateMultipartUploadResult result = FileUploader.ossClient.initiateMultipartUpload(request);
        return result.getUploadId();
    }

    /**
     * 方法描述: 将文件分块进行升序排序并执行文件上传。
     *
     * @param uploadId
     * @author fyq
     * @date 2017年05月25日 16:04:22
     */
    protected static void completeMultipartUpload(String uploadId) {
        // 将文件分块按照升序排序
        Collections.sort(PART_E_TAGS, new Comparator<PartETag>() {
            @Override
            public int compare(PartETag o1, PartETag o2) {
                return o1.getPartNumber() - o2.getPartNumber();
            }
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, PART_E_TAGS);
        // 完成分块上传
        FileUploader.ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }

    /**
     * 方法描述: 列出文件所有分块的清单
     *
     * @param uploadId
     * @author fyq
     * @date 2017年05月25日 16:07:53
     */
    protected static void listAllParts(String uploadId) {
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, key, uploadId);
        // 获取上传的所有分块信息
        PartListing partListing = FileUploader.ossClient.listParts(listPartsRequest);
        // 获取分块的大小
        int partCount = partListing.getParts().size();
        // 遍历所有分块
        for (int i = 0; i < partCount; i++) {
            PartSummary partSummary = partListing.getParts().get(i);
            logger.info("分块编号 " + partSummary.getPartNumber() + ", ETag=" + partSummary.getETag());
        }
    }
}
