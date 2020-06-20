package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidCollectionAccountQrCodeMapper;
import com.hz.fine.master.core.model.did.DidCollectionAccountQrCodeModel;
import com.hz.fine.master.core.service.DidCollectionAccountQrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Description 用户的收款账号二维码的Service层的实现层
 * @Author yoko
 * @Date 2020/6/17 15:07
 * @Version 1.0
 */
@Service
public class DidCollectionAccountQrCodeServiceImpl<T> extends BaseServiceImpl<T> implements DidCollectionAccountQrCodeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidCollectionAccountQrCodeMapper didCollectionAccountQrCodeMapper;

    public BaseDao<T> getDao() {
        return didCollectionAccountQrCodeMapper;
    }

    @Override
    public int updateBatchStatus(DidCollectionAccountQrCodeModel model) {
        return didCollectionAccountQrCodeMapper.updateBatchStatus(model);
    }

    @Override
    public int addBatchDidCollectionAccountQrCode(DidCollectionAccountQrCodeModel model) {
        try {
            return didCollectionAccountQrCodeMapper.addBatchDidCollectionAccountQrCode(model);
        }catch (Exception e){
            if (e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                //捕获一下唯一索引的异常，不进行处理。这里不影响程序；高并发时可能会出现这个异常
            } else {
                throw e;
            }
        }
        return 0;
    }
}
