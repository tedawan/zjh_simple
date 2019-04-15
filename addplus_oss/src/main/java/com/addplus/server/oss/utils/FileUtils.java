package com.addplus.server.oss.utils;


import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件处理工具类
 *
 * @author fyq
 * @version V1.0
 * @date 2017年05月24日 14:40
 */
public class FileUtils {

    private static Map<String, String> file_type_map = new HashMap<>();

    public static Map<String, String> getFileType() {
        return file_type_map;
    }

    private static Map<String, String> file_content_type = new HashMap<>();

    private static final String PREFIX_FILE = "file";

    public static String getPrefixFile() {
        return PREFIX_FILE;
    }

    private static final String PREFIX_IMAGE = "images";

    public static String getPrefixImage() {
        return PREFIX_IMAGE;
    }

    private static final String PREFIX_VIDEO = "video";

    public static String getPrefixVideo() {
        return PREFIX_VIDEO;
    }

    private static final String PREFIX_VOICE = "voice";

    public static String getPrefixVoice() {
        return PREFIX_VOICE;
    }

    private static String uploadPrefix = "server_{0}.{1}";

    static {
        init();
    }

    public static String getUploadPath(String fileName, boolean randomName) {
        String suffix = getSuffix(fileName);
        if (randomName) {
            fileName = MessageFormat.format(uploadPrefix, UUID.randomUUID().toString().replace("-", ""), suffix);
        }
        if (file_type_map.get(suffix) != null) {
            fileName = file_type_map.get(suffix).concat("/").concat(fileName);
        } else {
            fileName = PREFIX_FILE.concat("/").concat(fileName);
        }
        return fileName;
    }

    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).split("\\?")[0].toLowerCase();
    }

    public static String getFileContentType(String fileName) {
        String suffix = getSuffix(fileName);
        String contentType = "application/octet-stream";
        if (file_content_type.get(suffix) != null) {
            contentType = file_content_type.get(suffix);
        }
        return contentType;
    }

    private static void init() {
        /*图片格式 start*/
        file_type_map.put("jpg", PREFIX_IMAGE);
        file_type_map.put("png", PREFIX_IMAGE);
        file_type_map.put("jpeg", PREFIX_IMAGE);
        file_type_map.put("gif", PREFIX_IMAGE);
        file_type_map.put("bmp", PREFIX_IMAGE);
        file_content_type.put("png", "images/png");
        file_content_type.put("jpg", "images/jpeg");
        file_content_type.put("jpeg", "images/jpeg");
        file_content_type.put("gif", "images/gif");
        file_content_type.put("bmp", "images/bmp");
        /*图片格式 end*/
        /*视频格式 start*/
        file_type_map.put("mp4", PREFIX_VIDEO);
        file_type_map.put("avi", PREFIX_VIDEO);
        file_type_map.put("rm", PREFIX_VIDEO);
        file_type_map.put("rmvb", PREFIX_VIDEO);
        file_type_map.put("mpeg", PREFIX_VIDEO);
        file_type_map.put("mpg", PREFIX_VIDEO);
        file_type_map.put("wmv", PREFIX_VIDEO);
        file_type_map.put("mov", PREFIX_VIDEO);
        /*视频格式 end*/
        /*音频格式 start*/
        file_type_map.put("mp3", PREFIX_VOICE);
        file_type_map.put("wma", PREFIX_VOICE);
        file_type_map.put("rmvb", PREFIX_VOICE);
        file_type_map.put("mpeg", PREFIX_VOICE);
        file_type_map.put("wmv", PREFIX_VOICE);
        file_type_map.put("mov", PREFIX_VOICE);
        /*音频格式 end*/
    }
}
