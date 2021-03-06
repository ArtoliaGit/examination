package com.bsoft.examination.service.auth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.common.auth.UserInfo;
import com.bsoft.examination.domain.auth.User;
import com.bsoft.examination.mapper.auth.UserMapper;
import com.bsoft.examination.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户业务类
 * @author artolia
 */
@Service("userService")
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserInfo userInfo;

    public UserService(UserInfo userInfo, UserMapper userMapper) {
        this.userInfo = userInfo;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    public Result getUserInfo() {
        Result<User> result = new Result<>();
        try {
            User user = userInfo.getUser();
            user.setPassword("");
            result.setCode(200);
            result.setMessage("获取用户信息成功");
            result.setData(user);
        } catch (Exception e) {
            result.setCode(500);
            result.setMessage("获取用户信息失败");
        }

        return result;
    }

    /**
     * 获取用户列表
     * @param params 参数
     * @return Result
     */
    public Result getUserList(Map<String, Object> params) {
        Result<List<User>> result = new Result<>();

        try {
            String pageNum = (String) params.get("page");
            String pageSize = (String) params.get("pageSize");
            Page<User> page = new Page<>(1, 10);

            if (StringUtils.isNoneBlank(pageNum, pageNum)) {
                page.setCurrent(Long.parseLong(pageNum));
                page.setSize(Long.parseLong(pageSize));
            }

            IPage<User> userIPage = userMapper.getUserList(page, params);
            long total = userIPage.getTotal();
            List<User> userList = userIPage.getRecords();

            result.setData(userList);
            result.setTotal(total);
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }

        return result;
    }

    /**
     * 保存或者更新用户
     * @param user 用户实体
     * @param op 操作
     * @return Result
     */
    public Result save(User user, String op) {
        Result<User> result = new Result<>();
        try {
            final String username = user.getUsername();
            user.setCreateTime(new Date());
            if ("create".equals(op)) {
                if (userMapper.findByUsername(username) != null) {
                    result.setCode(HttpStatus.NO_CONTENT);
                    result.setMessage("用户名已存在");
                    return result;
                }
//                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//                final String password = user.getPassword();
//                user.setPassword(encoder.encode(password));
                user.setCreateUser(userInfo.getUsername());
                user.setUserId(UUIDUtil.generateTimeUUID());
                userMapper.save(user);
            } else {
                userMapper.updateById(user);
            }
            userMapper.deleteRoles(user);
            userMapper.saveRoles(user);
            result.setCode(HttpStatus.OK);
            result.setMessage("保存成功");
            result.setData(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动回滚事务
        }
        return result;
    }

    /**
     * 修改密码
     * @param user 用户实体
     * @return Result
     */
    public Result updatePassword(User user) {
        Result result = new Result();
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String password = user.getPassword();
            user.setPassword(encoder.encode(password));
            userMapper.updateById(user);
            result.setCode(HttpStatus.OK);
            result.setMessage("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("更新失败");
        }
        return result;
    }

    /**
     * 删除用户
     * @param userId 用户id
     * @return Result
     */
    public Result delete(String userId) {
        Result result = new Result();

        try {
            User user = new User();
            user.setUserId(userId);
            user.setStatus("0");
            user.setDisableTime(new Date());
            user.setCreateTime(new Date());
            user.setCreateUser(userInfo.getUsername());
            user.setCreateUnit(userInfo.getOrgan());
            userMapper.updateById(user);
            result.setCode(HttpStatus.OK);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("删除失败");
        }
        return result;
    }
}
