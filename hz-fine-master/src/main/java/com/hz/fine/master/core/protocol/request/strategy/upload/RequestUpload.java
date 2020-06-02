package com.hz.fine.master.core.protocol.request.strategy.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @Description 协议：七牛图片上传
 * @Author yoko
 * @Date 2020/6/2 11:01
 * @Version 1.0
 */
public class RequestUpload implements Serializable {
    private static final long   serialVersionUID = 1233283332519L;

    public MultipartFile image;
    public MultipartFile image1;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getImage1() {
        return image1;
    }

    public void setImage1(MultipartFile image1) {
        this.image1 = image1;
    }
}
