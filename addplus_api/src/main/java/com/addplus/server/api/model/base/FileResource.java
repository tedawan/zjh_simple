package com.addplus.server.api.model.base;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件资源
 *
 * @author fuyq
 * @date 2018/6/16
 */
public class FileResource {

    /**
     * 名称
     */
    private String fileName;

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 输出流
     */
    private OutputStream outputStream;

    public FileResource() {
    }

    public FileResource(String fileName) {
        this.fileName = fileName;
    }

    public FileResource(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    public FileResource(String fileName, OutputStream outputStream) {
        this.fileName = fileName;
        this.outputStream = outputStream;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
