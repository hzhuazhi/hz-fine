package com.hz.fine.master.core.runner;

import com.hz.fine.master.core.common.redis.RedisIdService;
import com.hz.fine.master.core.common.redis.RedisService;
import com.hz.fine.master.core.common.utils.constant.LoadConstant;
import com.hz.fine.master.core.service.*;
import com.hz.fine.master.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Order(0)
public class AutowireRunner implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(AutowireRunner.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;


    Thread runThread = null;

    @Autowired
    private RedisIdService redisIdService;
    @Autowired
    private RedisService redisService;

    @Resource
    private LoadConstant loadConstant;

    @Autowired
    private QuestionMService questionMService;

    @Autowired
    private QuestionDService questionDService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskHodgepodgeService taskHodgepodgeService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private DidService didService;

    @Autowired
    private DidLevelService didLevelService;

    @Autowired
    private DidCollectionAccountService didCollectionAccountService;

    @Autowired
    private MobileCardService mobileCardService;

    @Autowired
    private MobileCardDataService mobileCardDataService;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankTransferService bankTransferService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private DidRechargeService didRechargeService;

    @Autowired
    private BankCollectionService bankCollectionService;

    @Autowired
    private DidRewardService didRewardService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxService wxService;

    @Autowired
    private WxClerkService wxClerkService;

    @Autowired
    private WxClerkDataService wxClerkDataService;

    @Autowired
    private CatAllDataService catAllDataService;

    @Autowired
    private CatDataService catDataService;

    @Autowired
    private DidCollectionAccountQrCodeService didCollectionAccountQrCodeService;

    @Autowired
    private WxClerkUnboundService wxClerkUnboundService;

    @Autowired
    private DidOnoffService didOnoffService;

    @Autowired
    private DidBalanceDeductService didBalanceDeductService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UpgradeService upgradeService;

    @Autowired
    private ConsultService consultService;

    @Autowired
    private QuestionDDService questionDDService;

    @Autowired
    private ClientCollectionDataService clientCollectionDataService;

    @Autowired
    private ConsultAskService consultAskService;

    @Autowired
    private ConsultAskReplyService consultAskReplyService;

    @Autowired
    private StatisticsClickPayService statisticsClickPayService;

    @Autowired
    private OperateService operateService;








    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AutowireRunner ...");
        ComponentUtil.redisIdService = redisIdService;
        ComponentUtil.redisService = redisService;
        ComponentUtil.loadConstant = loadConstant;
        ComponentUtil.questionMService = questionMService;
        ComponentUtil.questionDService = questionDService;

        ComponentUtil.taskService = taskService;
        ComponentUtil.taskHodgepodgeService = taskHodgepodgeService;
        ComponentUtil.regionService = regionService;
        ComponentUtil.didService = didService;
        ComponentUtil.didLevelService = didLevelService;
        ComponentUtil.didCollectionAccountService = didCollectionAccountService;
        ComponentUtil.mobileCardService = mobileCardService;
        ComponentUtil.mobileCardDataService = mobileCardDataService;
        ComponentUtil.bankService = bankService;
        ComponentUtil.bankTransferService = bankTransferService;
        ComponentUtil.strategyService = strategyService;
        ComponentUtil.didRechargeService = didRechargeService;
        ComponentUtil.bankCollectionService = bankCollectionService;
        ComponentUtil.didRewardService = didRewardService;
        ComponentUtil.orderService = orderService;
        ComponentUtil.wxService = wxService;
        ComponentUtil.wxClerkService = wxClerkService;
        ComponentUtil.wxClerkDataService = wxClerkDataService;
        ComponentUtil.catAllDataService = catAllDataService;
        ComponentUtil.catDataService = catDataService;
        ComponentUtil.didCollectionAccountQrCodeService = didCollectionAccountQrCodeService;
        ComponentUtil.wxClerkUnboundService = wxClerkUnboundService;
        ComponentUtil.didOnoffService = didOnoffService;
        ComponentUtil.didBalanceDeductService = didBalanceDeductService;
        ComponentUtil.noticeService = noticeService;
        ComponentUtil.upgradeService = upgradeService;
        ComponentUtil.consultService = consultService;
        ComponentUtil.questionDDService = questionDDService;
        ComponentUtil.clientCollectionDataService = clientCollectionDataService;
        ComponentUtil.consultAskService = consultAskService;
        ComponentUtil.consultAskReplyService = consultAskReplyService;
        ComponentUtil.statisticsClickPayService = statisticsClickPayService;
        ComponentUtil.operateService = operateService;

        runThread = new RunThread();
        runThread.start();






    }

    /**
     * @author df
     * @Description: TODO(模拟请求)
     * <p>1.随机获取当日金额的任务</p>
     * <p>2.获取代码信息</p>
     * @create 20:21 2019/1/29
     **/
    class RunThread extends Thread{
        @Override
        public void run() {
            log.info("启动啦............");
        }
    }




}
