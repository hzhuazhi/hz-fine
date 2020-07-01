package com.hz.fine.master.core.common.redis;

import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.did.DidOnoffModel;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @Description redis的数据失效监听
 * @Author yoko
 * @Date 2020/7/1 11:03
 * @Version 1.0
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // message.toString()可以获取失效的key
        String expiredKey = message.toString();
        System.out.println("expiredKey:" + expiredKey);
        if (expiredKey.indexOf("FN-30-") > -1){
            long did = Long.parseLong(expiredKey.substring("FN-30-".length()));
            // 把用户抢单上下线状态修改成下线状态

            // 先锁住这个用户
            String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ONOFF, did);
            boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
            if (flagLock_did){
                // 判断用户是否在上下线的表中有数据
                DidOnoffModel didOnoffQuery = HodgepodgeMethod.assembleDidOnoffQueryByDid(did);
                DidOnoffModel didOnoffData = (DidOnoffModel) ComponentUtil.didOnoffService.findByObject(didOnoffQuery);
                if (didOnoffData == null || didOnoffData.getId() <= 0){
                    // 添加数据
                    ComponentUtil.didOnoffService.add(didOnoffQuery);
                }else{
                    // 下线（取消抢单）
                    try{
                        DidOnoffModel didOnoffUpdate = HodgepodgeMethod.assembleDidOnoffUpdate(did, 1);
                        ComponentUtil.didOnoffService.updateDidOnoff(didOnoffUpdate);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                // 解锁
                ComponentUtil.redisIdService.delLock(lockKey_did);
            }
        }
    }
}
