package com.hz.fine.master.core.controller.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 策略表：关于一些策略配置的部署的Controller层
 * @Author yoko
 * @Date 2020/5/19 14:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/stg")
public class StrategyController {

    private static Logger log = LoggerFactory.getLogger(StrategyController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;

}
