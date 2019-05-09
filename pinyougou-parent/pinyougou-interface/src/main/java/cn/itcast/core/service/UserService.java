package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import entity.PageResult;

import java.util.List;

public interface UserService {
    void sendCode(String phone);

    void add(User user, String smscode);

    //查询所有用户
    List<User> findAll();

    //添加查询
    PageResult search(Integer pageNum, Integer pageSize, User user);

    //更新状态
    void updateStatus(Long[] ids, String status);
    //完善用户信息
    void perfectionMessage(User user);
    //回显用户信息
    User findVO(String name);
}
