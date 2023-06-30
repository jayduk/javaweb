package service.impl;

import annotation.AutoWired;
import annotation.Component;
import annotation.Level;
import dao.ICollectDao;
import exception.BusinessException;
import pojo.Collect;
import service.ICollectService;

import java.util.List;

/**
 * @author javaok
 * 2023/6/27 10:18
 */
@Component(level = Level.SERVICE, value = "collectService")
public class CollectServiceImpl implements ICollectService {
    @AutoWired
    private ICollectDao collectDao;

    private void paramCheck(Long userId, Long goodId) {
        if (userId == null || goodId == null) {
            throw new BusinessException(BusinessException.PARAM_ERR, "参数不能为空");
        }
    }


    @Override
    public void collect(Long userId, Long goodId) {
        paramCheck(userId, goodId);
        collectDao.collect(userId, goodId);
    }

    @Override
    public void disCollect(Long userId, Long goodId) {
        paramCheck(userId, goodId);
        collectDao.disCollect(userId, goodId);
    }

    @Override
    public boolean queryCollectState(Long userId, Long goodId) {
        paramCheck(userId, goodId);
        return collectDao.queryCollectState(userId, goodId);
    }

    @Override
    public List<Collect> queryCollectOfUser(Long userId) {
        if (userId == null) {
            throw new BusinessException(BusinessException.PARAM_ERR, "参数不能为空");
        }
        return collectDao.queryCollectOfUser(userId);
    }

    @Override
    public void removeCollectById(Long id) {
        if (id == null) {
            throw new BusinessException(BusinessException.PARAM_ERR, "参数错误");
        }
        collectDao.removeCollectById(id);
    }
}
