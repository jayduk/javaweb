package service;

import pojo.Collect;

import java.util.List;

/**
 * @author javaok
 * 2023/6/27 10:16
 */
public interface ICollectService {
    void collect(Long userId, Long goodId);

    void disCollect(Long userId, Long goodId);

    boolean queryCollectState(Long userId, Long goodId);

    List<Collect> queryCollectOfUser(Long userId);

    void removeCollectById(Long id);
}
