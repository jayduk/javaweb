package service;

import pojo.User;

/**
 * @author javaok
 * 2023/6/21 8:13
 */
public interface IUserService {
    boolean isExitsUser(String username);

    User login(String username, String password);

    boolean register(String username, String nickname, String password, String password2);

    User queryUserById(Long id);

    void logout(Long id);
}
