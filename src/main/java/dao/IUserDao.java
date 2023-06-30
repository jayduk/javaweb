package dao;

import pojo.User;

/**
 * @author javaok
 * 2023/6/21 8:12
 */
public interface IUserDao {
    User queryUserByUsername(String username);

    boolean queryUser(String username);

    void insert(User user);

    User queryUserById(Long id);

    void logout(Long id);
}
