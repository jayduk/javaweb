package dao.impl;

import annotation.Component;
import annotation.Level;
import dao.IUserDao;
import exception.BusinessException;
import pojo.User;
import util.JdbcUtil;

import java.sql.*;

/**
 * @author javaok
 * 2023/6/21 8:12
 */

@Component(level = Level.DAO, value = "userDao")
public class UserDaoImpl implements IUserDao {
    @Override
    public boolean queryUser(String username) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id from user where username = ? and is_deleted = 0");
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            return set.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User queryUserByUsername(String username) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, nickname, username, password from user where username = ? and is_deleted = 0");
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                User user = new User();
                user.setPassword(set.getString("password"));
                user.setNickname(set.getString("nickname"));
                user.setUsername(set.getString("username"));
                user.setId(set.getLong("id"));

                set.close();
                statement.close();

                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(User user) {
        Connection conn = JdbcUtil.getConnection();
        String insertUserSql = "insert into user(username,password,nickname) values (?,?,?)";

        try (PreparedStatement statement = conn.prepareStatement(insertUserSql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getNickname());
            int effectCount = statement.executeUpdate();

            if (effectCount != 1) {
                throw new RuntimeException("插入用户失败");
            }
        } catch (SQLException e) {
            throw new BusinessException(BusinessException.SQL_ERR, "插入用户失败");
        }
    }

    @Override
    public User queryUserById(Long id) {
        Connection conn = JdbcUtil.getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, nickname, username from user where id = ? and is_deleted = 0");
            statement.setString(1, id.toString());
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                User user = new User();
                user.setNickname(set.getString("nickname"));
                user.setUsername(set.getString("username"));
                user.setId(set.getLong("id"));

                set.close();
                statement.close();

                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout(Long id) {
        Connection conn = JdbcUtil.getConnection();
        String insertUserSql = "update user set is_deleted = 1 where id = ?";

        try (PreparedStatement statement = conn.prepareStatement(insertUserSql)) {

            statement.setLong(1, id);
            int effectCount = statement.executeUpdate();

            if (effectCount != 1) {
                throw new RuntimeException("注销用户失败");
            }
        } catch (SQLException e) {
            throw new BusinessException(BusinessException.SQL_ERR, "注销用户失败");
        }
    }
}
