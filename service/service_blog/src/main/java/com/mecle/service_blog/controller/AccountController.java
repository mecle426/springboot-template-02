package com.mecle.service_blog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mecle.common_utils.Result;
import com.mecle.service_base.handler.CustomExceptionHandler;
import com.mecle.service_blog.entity.MUser;
import com.mecle.service_blog.entity.md.LoginMD;
import com.mecle.service_blog.entity.md.SingInMD;
import com.mecle.service_blog.service.MUserService;
import com.mecle.shiro.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/service_blog/account")
public class AccountController {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private MUserService userService;
    /**
     *
     * @param loginMD   登陆实体对象
     * @param response 我们做登陆，需要将jwt放在header中，这就需要HttpServletResponse
     * @return
     */
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginMD loginMD, HttpServletResponse response) {
        MUser user = userService.getOne(new QueryWrapper<MUser>().eq("username", loginMD.getUsername()));
        Assert.notNull(user, "用户不存在");
        if(!user.getPassword().equals(SecureUtil.md5(loginMD.getPassword()))) {
            return Result.error().message("密码错误！");
        }
        String jwt = jwtUtils.generateToken(user.getId().toString());
        System.out.println("11");
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 用户可以另一个接口
        return Result.ok().data("loginMD",MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    // 退出
    @GetMapping("/logout")
    //@RequiresAuthentication//必须认证通过才能退出
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }

    @PostMapping("signIn")
    public Result sigIn(@Validated @RequestBody SingInMD singInMD){
        /**
         * 判断
         */
        MUser username = userService.getOne(new QueryWrapper<MUser>().eq("username", singInMD.getUsername()));
        if(!StringUtils.isEmpty(username)){
            throw new CustomExceptionHandler(20001,"用户名已注册");
        }else{
            username = userService.getOne(new QueryWrapper<MUser>().eq("email", singInMD.getEmail()));
            if(!StringUtils.isEmpty(username)){
                throw new CustomExceptionHandler(20001,"该邮箱已注册");
            }
        }

        MUser user=new MUser();


        BeanUtils.copyProperties(singInMD,user);//将singInMD中的所有相符属性，全部拷贝到user对象
        user.setPassword(SecureUtil.md5(singInMD.getPassword()));//加密密码
        user.setStatus(1);//初始状态为1，表示正常
        user.setIsDelete(false);//初始逻辑删除为false就是0表示没有删除
        boolean save = userService.save(user);
        if(save){
            return Result.ok();
        }
        throw new CustomExceptionHandler(20001,"注册失败");
    }

}

