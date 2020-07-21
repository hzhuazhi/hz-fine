package com.hz.fine.master.core.common.utils.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description 加载的配置文件
 * @Author yoko
 * @Date 2019/12/30 12:37
 * @Version 1.0
 */
@Component
public class LoadConstant {

    /**
     * 开发者应用公钥证书路径
     */
    @Value("${alipay.cert.path}")
    public String certPath;

    /**
     * 支付宝公钥证书文件路径
     */

    @Value("${alipay.public.cert.path}")
    public String alipayPublicCertPath;

    /**
     * 支付宝CA根证书文件路径
     */
    @Value("${alipay.root.cert.path}")
    public String rootCertPath;

    /**
     * 支付宝的支付默认金额
     */
    @Value("${alipay.total.amount}")
    public String totalAmount;

    /**
     * 二维码支付地址
     */
    @Value("${qrCode.url}")
    public String qrCodeUrl;

    /**
     * 支付宝支付地址
     */
    @Value("${zfb.qrCode.url}")
    public String zfbQrCodeUrl;

    /**
     * 微信群支付地址
     */
    @Value("${wx.group.qrCode.url}")
    public String wxGroupQrCodeUrl;


}
