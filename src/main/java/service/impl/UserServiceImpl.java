package service.impl;

import annotation.AutoWired;
import annotation.Component;
import annotation.Level;
import dao.IUserDao;
import exception.BusinessException;
import pojo.User;
import service.IUserService;
import util.Constant;
import util.StringUtil;

import java.util.Objects;

/**
 * @author javaok
 * 2023/6/21 8:13
 */
@Component(level = Level.SERVICE, value = "userService")
public class UserServiceImpl implements IUserService {

    @AutoWired
    private IUserDao userDao;

    @Override
    public boolean isExitsUser(String username) {
        return userDao.queryUser(username);
    }

    private void userPasswdCheck(String username, String password) {
        if (StringUtil.isAnyEmpty(username, password)) {
            throw new BusinessException(BusinessException.PARAM_ERR, "账号密码不能为空");
        }
        if (username.length() < Constant.min_username_len) {
            throw new BusinessException(BusinessException.PARAM_ERR, "用户名过短");
        }
        if (username.length() > Constant.max_username_len) {
            throw new BusinessException(BusinessException.PARAM_ERR, "用户名过长");
        }
        if (password.length() < Constant.min_password_len) {
            throw new BusinessException(BusinessException.PARAM_ERR, "密码过短");
        }
        if (password.length() > Constant.max_password_len) {
            throw new BusinessException(BusinessException.PARAM_ERR, "密码过长");
        }
    }

    @Override
    public User login(String username, String password) {
        userPasswdCheck(username, password);

        User user = userDao.queryUserByUsername(username);

        if (!Objects.equals(password, user.getPassword())) {
            throw new BusinessException(BusinessException.PARAM_ERR, "密码错误");
        }

        return user;
    }

    @Override
    public boolean register(String username, String nickname, String password, String password2) {
        userPasswdCheck(username, password);
        if (!password.equals(password2)) {
            throw new BusinessException(BusinessException.PARAM_ERR, "两次输入密码不一致");
        }
        if (userDao.queryUser(username)) {
            throw new BusinessException(BusinessException.PARAM_ERR, "该用户名已存在");
        }
        if (nickname.length() > Constant.max_nickname_len) {
            throw new BusinessException(BusinessException.PARAM_ERR, "昵称过长");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);

        userDao.insert(user);
        return true;
    }

    @Override
    public User queryUserById(Long id) {
        return userDao.queryUserById(id);
    }

    @Override
    public void logout(Long id) {
        userDao.logout(id);
    }
}
