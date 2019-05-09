package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 */
@Service
@Transactional
public class UserServiceImpl implements  UserService {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsDestination;
    @Autowired
    private UserDao userDao;
    //发送验证码
    @Override
    public void sendCode(String phone) {
        //1:获取验证码 随机6位数
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        //2:保存验证码到缓存中
        redisTemplate.boundValueOps(phone).set(randomNumeric,8, TimeUnit.HOURS);
        //3:发消息
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
/*
                1)验证码(数字)
                2)签名 (名称)
                3)模板 (ID)
                4)手机号*/

                MapMessage mapMessage = session.createMapMessage();
                //手机号
                mapMessage.setString("PhoneNumbers",phone);
                mapMessage.setString("SignName","品优购商城");
                mapMessage.setString("TemplateCode","SMS_126462276");
                mapMessage.setString("TemplateParam","{\"number\":\""+randomNumeric+"\"}");

                return mapMessage;
            }
        });


    }

    //完成注册

    @Override
    public void add(User user, String smscode) {
        //1:验证码判断
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if(null != code){

            //判断验证码是否正确
            if(code.equals(smscode)){

                //保存此用户

                //时间
                user.setCreated(new Date());
                user.setUpdated(new Date());

                //密码 加密
                //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

                //保存
                userDao.insertSelective(user);

            }else{
                throw new RuntimeException("验证码错误");
            }
        }else{
            //失效了
            throw new RuntimeException("验证码过期");
        }

    }

    //完善用户信息
    @Override
    public void perfectionMessage(User user) {
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        try {
            userDao.updateByExampleSelective(user,userQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回显用户信息
    @Override
    public User findVO(String name) {
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andUsernameEqualTo(name);
        List<User> userList = userDao.selectByExample(userQuery);
        return userList.get(0);
    }
}
