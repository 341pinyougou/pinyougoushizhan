package cn.itcast.core.controller;

import cn.itcast.common.utils.PhoneFormatCheckUtils;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.PatternSyntaxException;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    //发送验证码
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        //手机是否合法  正则表达式

        try {
            if (PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                //合法
                userService.sendCode(phone);

            } else {
                //不合法
                return new Result(false, "手机号不正确");
            }
            return new Result(true, "发送成功");

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            return new Result(false, "发送失败");
        }

    }
    //添加
    @RequestMapping("/add")
    public Result add(@RequestBody User user, String smscode) {
        try {

            userService.add(user,smscode);

            return new Result(true, "注册成功");


        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }

    }

    //完善用户信息
    @RequestMapping("/perfectionMessage")
    public Result perfectionMessage(@RequestBody User user){
        try {
            //获取当前登录用户名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            user.setUsername(name);
            userService.perfectionMessage(user);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }

    //回显用户信息
    @RequestMapping("/findVO")
    public User findVO(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findVO(name);
    }
}
