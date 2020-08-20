package com.mecle.service_blog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.mecle.service_blog.entity.MUser;
import com.mecle.service_blog.service.MUserService;
import com.mecle.shiro.handler.AccountProfile;
import com.mecle.shiro.handler.AccountRealm;
import com.mecle.shiro.handler.JwtToken;
import com.mecle.shiro.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceAccountRealm extends AccountRealm {
    /**
     * 其实主要就是doGetAuthenticationInfo登录认证这个方法，可以看到我们通过jwt获取到用户信息，判断用户的状态，最后异常就抛出对应的异常信息，否者封装成SimpleAuthenticationInfo返回给shiro。
     * 接下来我们逐步分析里面出现的新类
     *
     * 作者：MarkerHub
     * 链接：https://juejin.im/post/5ecfca676fb9a04793456fb8
     * 来源：掘金
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    MUserService mUserService;
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwt = (JwtToken) token;
        log.info("jwt----------------->{}", jwt);
        String userId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        MUser mUser = mUserService.getById(Long.parseLong(userId));
        if(mUser == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if(mUser.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定！");
        }
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(mUser, profile);
        log.info("profile----------------->{}", profile.toString());
        return new SimpleAuthenticationInfo(profile, jwt.getCredentials(), getName());
    }
}

