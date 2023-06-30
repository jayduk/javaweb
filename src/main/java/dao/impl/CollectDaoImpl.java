package dao.impl;

import annotation.Component;
import annotation.Level;
import dao.BaseDao;
import dao.ICollectDao;
import exception.BusinessException;
import pojo.Collect;
import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author javaok
 * 2023/6/27 10:20
 */
@Component(level = Level.DAO, value = "collectDao")
public class CollectDaoImpl extends BaseDao<Collect> implements ICollectDao {

    @Override
    public void collect(Long userId, Long goodId) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO collect(user_id, good_id) VALUES (?,?)");
            statement.setLong(1, userId);
            statement.setLong(2, goodId);
            int i = statement.executeUpdate();

            if (i != 1) {
                throw new BusinessException(BusinessException.SQL_ERR, "收藏失败");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disCollect(Long userId, Long goodId) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE  collect SET is_deleted=1 WHERE user_id = ? AND good_id = ?");
            statement.setLong(1, userId);
            statement.setLong(2, goodId);
            int i = statement.executeUpdate();

            if (i < 1) {
                throw new BusinessException(BusinessException.SQL_ERR, "取消收藏失败");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean queryCollectState(Long userId, Long goodId) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT id FROM collect WHERE user_id = ? AND good_id = ? AND is_deleted = 0");
            statement.setLong(1, userId);
            statement.setLong(2, goodId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Collect> queryCollectOfUser(Long userId) {
        return queries("SELECT collect.id,good.owner_id, good_id, collect.create_time, title, price, nickname " +
                "FROM collect, good, user " +
                "WHERE collect.user_id = ? " +
                "AND collect.good_id = good.id " +
                "AND user.id = good.owner_id " +
                "AND collect.is_deleted = 0", userId);
    }

    @Override
    public void removeCollectById(Long id) {
        Integer effect = update("UPDATE collect SET is_deleted = 1 WHERE id = ?", id);
        if (effect < 1) {
            throw new BusinessException(BusinessException.SQL_ERR, "删除收藏失败");
        }
    }
}
