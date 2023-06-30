package controller;

import annotation.AutoWired;
import annotation.Component;
import annotation.Level;
import annotation.RequestMapping;
import com.alibaba.fastjson2.JSON;
import pojo.User;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author javaok
 * 2023/6/21 8:14
 */
@Component(level = Level.CONTROLLER)
@RequestMapping("/jav")
public class UserController {

    @AutoWired
    IUserService userService;

    @RequestMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login.html");
    }

    @RequestMapping(value = "/login", method = "POST")
    public Long login(String username, String password, HttpServletResponse response) {
        return userService.login(username, password).getId();
    }

    @RequestMapping("/registry")
    public void registry(HttpServletResponse response) throws IOException {
        response.sendRedirect("/registry.html");
    }

    @RequestMapping(value = "/registry", method = "POST")
    public void registry(String username, String nickname, String password, String password2, HttpServletRequest request, HttpServletResponse response) {
        if (userService.register(username, nickname, password, password2)) {
            try {
                response.sendRedirect("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/is_exists_user", method = "POST")
    public Boolean isExitsUser(String username) {
        return userService.isExitsUser(username);
    }

    @RequestMapping(value = "/query_user_by_id", method = "POST")
    public User queryUserById(Long id) {
        return userService.queryUserById(id);
    }

    @RequestMapping(value = "/logout", method = "POST")
    public void logout(Long id) {
        userService.logout(id);
    }
}
