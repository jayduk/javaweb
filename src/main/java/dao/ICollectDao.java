package dao;

import pojo.Collect;

import java.util.List;

/**
 * @author javaok
 * 2023/6/27 10:19
 */
public interface ICollectDao {
    void collect(Long userId, Long goodId);

    void disCollect(Long userId, Long goodId);

    boolean queryCollectState(Long userId, Long goodId);

    List<Collect> queryCollectOfUser(Long userId);

    void removeCollectById(Long id);
}
