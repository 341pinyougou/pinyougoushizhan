package cn.itcast.core.controller;


/*
* 用户审核
* */

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/findAll")
    public List<User> findAll(){
        List<User> userList = userService.findAll();
        return userList;
    }


    @RequestMapping("/search")
    public PageResult search(Integer page,Integer rows,@RequestBody User user){
        return userService.search(page,rows,user);
    }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status){

        try {
            userService.updateStatus(ids,status);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }



}
