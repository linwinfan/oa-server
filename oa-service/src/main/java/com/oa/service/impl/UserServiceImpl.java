package com.oa.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.oa.base.Constant;
import com.oa.entity.Menu;
import com.oa.service.IMenuService;
import com.oa.service.IUserService;
import com.oa.service.IUserToRoleService;
import com.oa.entity.User;
import com.oa.entity.UserToRole;
import com.oa.mapper.UserMapper;
import com.oa.util.GenerationSequenceUtil;
import com.oa.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IUserToRoleService userToRoleService;
    @Autowired
    private IMenuService menuService;

    @Autowired
    private UserMapper mapper;

    @Override
//    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public User getUserByUserName(String username) {
        System.out.println("执行getUserByUserName方法了.....");
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.where("user_name={0}", username);
        return this.selectOne(ew);
    }

    @Override
    public User getUserByMobile(String mobile) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("mobile", mobile);
        return this.selectOne(ew);
    }

    @Override
    public User register(User user, String  roleCode) {
        user.setUserNo(GenerationSequenceUtil.generateUUID("user"));
        user.setCreateTime(System.currentTimeMillis());
        boolean result = this.insert(user);
        if (result) {
            UserToRole userToRole  = UserToRole.builder().userNo(user.getUserNo()).roleCode(roleCode).build();
            userToRoleService.insert(userToRole);
        }
        return user;
    }

    @Override
    public Map<String, Object> getLoginUserAndMenuInfo(User user) {
        Map<String, Object> result = new HashMap<>();
        UserToRole userToRole = userToRoleService.selectByUserNo(user.getUserNo());
        user.setToken(JWTUtil.sign(user.getUserNo(), user.getPassword()));
        result.put("user",user);
        List<Menu> buttonList = new ArrayList<Menu>();
        //根据角色主键查询启用的菜单权限
        List<Menu> menuList = menuService.findMenuByRoleCode(userToRole.getRoleCode());
        List<Menu> retMenuList = menuService.treeMenuList(Constant.ROOT_MENU, menuList);
        for (Menu buttonMenu : menuList) {
            if(buttonMenu.getMenuType() == Constant.TYPE_BUTTON){
                buttonList.add(buttonMenu);
            }
        }
        result.put("menuList",retMenuList);
        result.put("buttonList",buttonList);
        return result;
    }

    @Override
    public boolean deleteByUserNo(String userNo) {
        EntityWrapper<UserToRole> ew = new EntityWrapper<>();
        ew.eq("user_no", userNo);
        boolean resultRole = userToRoleService.delete(ew);
        boolean  resultUser = this.deleteById(userNo);
        return resultRole && resultUser;
    }

    @Override
    public Page<User> selectPageByConditionUser(Page<User> userPage, String info, Integer[] status, String startTime, String endTime) {
        //注意！！ 分页 total 是经过插件自动 回写 到传入 page 对象
        return userPage.setRecords(mapper.selectPageByConditionUser(userPage, info,status,startTime,endTime));
    }


}
