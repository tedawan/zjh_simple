package com.addplus.server.oss.uploader;

import com.addplus.server.oss.OssClientUtils;
import com.addplus.server.oss.model.FileResource;
import com.addplus.server.oss.utils.FileUtils;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * 文件操作
 *
 * @author fyq
 * @version V1.0
 * @date 2017年05月25日 16:08
 */
public class FileUploader {

    private static Logger logger = LoggerFactory.getLogger(FileUploader.class);

    protected static OSS ossClient = null;

    private ExecutorService executorService;

    private int corePoolSize = 5;

    private int maximumPoolSize = 5;

    private long keepAliveTime = 0L;

    private int count = 20;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucket;

    private Set<Pattern> excludeNetUrl = new HashSet<>();

    public FileUploader(String accessKeyId, String accessKeySecret, String endpoint, String bucket) {
        this.bucket = bucket;
        ossClient = OssClientUtils.createOssClient(accessKeyId, accessKeySecret, endpoint);String initUrl = bucket + "." + endpoint;
        Set<Pattern> patterns = new HashSet<>();
        patterns.add(Pattern.compile("http://" + initUrl));
        patterns.add(Pattern.compile("https://" + initUrl));
        this.addAllExcludeNetUrl(patterns);
    }

    /**
     * 关闭Oss
     */
    public void close() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    public void setMaximumPoolSize(int size) {
        this.corePoolSize = size;
    }

    public void setCorePoolSize(int size) {
        this.maximumPoolSize = size;
    }

    public void setKeepAliveTime(long time) {
        this.keepAliveTime = time;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addExcludeNetUrl(Pattern pattern) {
        this.excludeNetUrl.add(pattern);
    }

    public void addAllExcludeNetUrl(Set<Pattern> patterns) {
        this.excludeNetUrl.addAll(patterns);
    }

    public String netWorkStreamUpload(String url, String fileName) {
        return netWorkStreamUpload(url, fileName, true);
    }

    /**
     * 网络流上传
     *
     * @param url        网络地址
     * @param fileName   文件名
     * @param randomName 是否随机名称
     * @return 文件路径
     * @author fuyq
     * @date 2018/09/11
     */
    public String netWorkStreamUpload(String url, String fileName, boolean randomName) {
        for (Pattern p : this.excludeNetUrl) {
            if (p.matcher(url).find()) {
                return url;
            }
        }
        InputStream in = null;
        try {
            in = new URL(url).openStream();
            return upload(in, fileName, randomName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件分片上传
     *
     * @param file 文件
     * @return 文件路径
     */
    public String multipartFileUpload(MultipartFile file) {
        try {
            return upload(file.getInputStream(), file.getOriginalFilename(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件分片上传
     *
     * @param file       文件
     * @param randomName 是否随机文件名
     * @return 文件路径
     */
    public String multipartFileUpload(MultipartFile file, boolean randomName) {
        try {
            return upload(file.getInputStream(), file.getOriginalFilename(), randomName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件流分片上传
     *
     * @param inputStream 文件输入流
     * @param fileName    文件名
     * @return 文件路径
     */
    public String streamUpload(InputStream inputStream, String fileName) {
        return upload(inputStream, fileName, true);
    }

    /**
     * 文件流分片上传
     *
     * @param inputStream 文件输入流
     * @param fileName    文件名
     * @param randomName  是否随机文件名
     * @return 文件路径
     */
    public String streamUpload(InputStream inputStream, String fileName, boolean randomName) {
        return upload(inputStream, fileName, randomName);
    }

    /**
     * 分片上传
     *
     * @param fileStream       文件输入流
     * @param originalFileName 文件名
     * @param randomName       是否随机文件名
     * @return 文件路径
     */
    private String upload(InputStream fileStream, String originalFileName, boolean randomName) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("file-upload-pool-%d").build();
        executorService = new ThreadPoolExecutor(
                this.corePoolSize,
                this.maximumPoolSize,
                this.keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        // 获取文件名
        String key = FileUtils.getUploadPath(originalFileName, randomName);
        try {
            // 获取uploadId
            String uploadId = AliOssUpload.claimUploadId(bucket, key);

            // 设置每块为 5M(除最后一个分块以外，其他的分块大小都要大于5MB)
            final long partSize = 10 * 1024 * 1024L;
            // 计算分块数目
            byte[] out = IOUtils.toByteArray(fileStream);
            long streamSize = out.length;
            int partCount = (int) (streamSize / partSize);
            if (streamSize % partSize != 0) {
                partCount++;
            }
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (streamSize - startPos) : partSize;
                // 线程执行。将分好的文件块加入到list集合中
                executorService.execute(new AliOssUpload(out, startPos, curPartSize, i + 1, uploadId, key, bucket));
            }

            // 等待所有分片完毕
            // 关闭线程池（线程池不马上关闭），执行以前提交的任务，但不接受新任务。
            executorService.shutdown();
            // 如果关闭后所有任务都已完成，则返回 true。
            while (!executorService.isTerminated()) {
                try {
                    // 用于等待子线程结束，再继续执行下面的代码
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (AliOssUpload.PART_E_TAGS.size() != partCount) {
                throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
            } else {
                logger.info("将要上传的文件名  " + key);
            }

            // 列出文件所有的分块清单并打印到日志中，该方法仅仅作为输出使用
            AliOssUpload.listAllParts(uploadId);

            // 完成分块上传
            AliOssUpload.completeMultipartUpload(uploadId);

            return key;
        } catch (OSSException oe) {
            oe.printStackTrace();
            throw new OSSException(oe.getErrorCode());
        } catch (ClientException ce) {
            ce.printStackTrace();
            throw new ClientException(ce.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IllegalStateException(ioe.getMessage());
        } finally {
            AliOssUpload.PART_E_TAGS.clear();
        }
    }

    /**
     * 文件流下载
     *
     * @param fileName   自定义文件名
     * @param objectName 文件路径
     * @return 文件资源
     * @author fuyq
     * @date 2018/06/21
     */
    public FileResource download(String fileName, String objectName) {
        OSSObject ossObject = ossClient.getObject(bucket, objectName);
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.toBufferedInputStream(ossObject.getObjectContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileResource(fileName, inputStream);
    }

    public List<String> getAllObject() {
        return getAllObject(null, null);
    }

    public List<String> getAllObject(String prefix) {
        return getAllObject(this.count, prefix);
    }

    /**
     * 获取所有Object
     *
     * @param count  每次获取数量
     * @param prefix 指定前缀
     * @return object集合
     * @author fuyq
     * @date 2018/08/09
     */
    public List<String> getAllObject(Integer count, String prefix) {
        String nextMarker = null;
        ObjectListing objectListing;
        List<String> fileList = new ArrayList<>();
        ListObjectsRequest listObjectsRequest;
        do {
            listObjectsRequest = new ListObjectsRequest(this.bucket).withMarker(nextMarker);
            if (StringUtils.isNoneBlank(prefix)) {
                listObjectsRequest.withPrefix(prefix);
            }
            if (count != null) {
                listObjectsRequest.withMaxKeys(count);
            }
            objectListing = ossClient.listObjects(listObjectsRequest);
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary sum : sums) {
                fileList.add(sum.getKey());
            }
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        return fileList;
    }
}
