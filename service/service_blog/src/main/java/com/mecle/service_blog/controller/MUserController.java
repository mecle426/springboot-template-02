package com.mecle.service_blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mecle.common_utils.Result;
import com.mecle.service_blog.entity.MUser;
import com.mecle.service_blog.service.MUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mecle
 * @since 2020-08-18
 */
@RestController
@RequestMapping("/service_blog/m-user")
public class MUserController {
    @Autowired
    private MUserService mUserService;

    @RequiresAuthentication
    @GetMapping("selectAll")
    public Result selectAll() {
        List<MUser> list = mUserService.list();
        return Result.ok().data("all", list);
    }

    @GetMapping("selectById")
    public Result selectById(Long id) {
        MUser mUser = mUserService.getById(id);
        return Result.ok().data("user", mUser);
    }

    @GetMapping("selectByPage")
    public Result selectByPage(int page, int limit) {
        Page<MUser> page1 = new Page<>(page, limit);
        mUserService.page(page1, null);
        return Result.ok().data("all", page1.getRecords());
    }

    @GetMapping("update")
    public Result updateById(Long id) {
        MUser mUser = new MUser();
        mUser.setId(id);
        boolean i = mUserService.updateById(mUser);
        if(i) return Result.ok();
        return Result.error();
    }

    @GetMapping("delete")
    public Result deleteById(Long id) {
        boolean i = mUserService.removeById(id);
        if(i) return Result.ok();
        return Result.error();
    }

    @GetMapping("insert")
    public Result insert() {
        MUser mUser = new MUser();
        mUser.setAvatar("头像");
        mUser.setEmail("99242834@qq.com");
        mUser.setIsDelete(false);
        mUser.setPassword("23423423423");
        mUser.setUsername("狗东西");
        mUser.setStatus(1);
        boolean i = mUserService.save(mUser);
        if(i) return Result.ok();
        return Result.error();
    }

    @PostMapping("login")
    public Result login(@Validated @RequestBody MUser user){
        return Result.ok().data("user",user);
    }

}

