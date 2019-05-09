package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;

public interface UserService {
    void sendCode(String phone);

    void add(User user, String smscode);
    //完善用户信息
    void perfectionMessage(User user);
    //回显用户信息
    User findVO(String name);
}
