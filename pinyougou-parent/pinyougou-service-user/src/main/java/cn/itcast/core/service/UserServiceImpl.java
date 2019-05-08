package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    //查询所有用户
    public List<User> findAll() {
        List<User> userList = userDao.selectByExample(null);
        return userList;
    }


    //条件查询
    public PageResult search(Integer pageNum, Integer pageSize, User user) {

        //分页小助手
        PageHelper.startPage(pageNum, pageSize);

        //查询条件
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();

        //判断查询条件
        //状态
        if(null != user.getStatus() && !"".equals(user.getStatus().trim())){
            criteria.andStatusEqualTo(user.getStatus().trim());
        }
        //用户名称
        if(null != user.getUsername() && !"".equals(user.getUsername().trim())){
            criteria.andUsernameLike("%" + user.getUsername().trim() + "%");
        }
        //查询所有
        Page<User> page = (Page<User>) userDao.selectByExample(userQuery);

        return new PageResult(page.getTotal(),page.getResult());
    }

    //更新状态
    public void updateStatus(Long[] ids, String status) {
         //创建用户对象
        User user = new User();
        //设置审核状态
        user.setStatus(status);
        //遍历
        for (Long id : ids) {
            //修改用户的状态
            user.setId(id);
            userDao.updateByPrimaryKeySelective(user);
        }
    }
}
