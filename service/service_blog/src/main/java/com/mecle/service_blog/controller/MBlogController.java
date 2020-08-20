package com.mecle.service_blog.controller;


import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mecle.common_utils.Result;
import com.mecle.service_base.handler.CustomExceptionHandler;
import com.mecle.service_blog.entity.MBlog;
import com.mecle.service_blog.entity.md.InsertBlog;
import com.mecle.service_blog.service.MBlogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mecle
 * @since 2020-08-18
 */
@RestController
@RequestMapping("/service_blog/m-blog")
public class MBlogController {
    @Autowired
    private MBlogService blogService;

    //2、分页查询讲师,其中所有@Api开头的注解都是swagger提供的，解释注解
    @ApiOperation(value = "分页查询所有博客")
    @GetMapping("limitSelect/{current}/{size}")
    public Result limitSelect(@ApiParam(name = "current", value = "当前页", required = true)
                              @PathVariable("current") Long current,
                              @ApiParam(name = "size", value = "每页记录数")
                              @PathVariable("size") Long size) {
        //1、创建page对象
        Page<MBlog> page = new Page<>(current, size);
        //2、通过接口中方法实现分页
        return Result.ok().data("limitBlog", blogService.page(page));
    }

    @ApiOperation("根据id获取博客")
    @GetMapping("blogById/{id}")
    public Result selectById(@ApiParam(name = "id", value = "博客id")
                             @PathVariable(name = "id") Long id) {
        MBlog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已删除或不存在！");
        return Result.ok().data("blog", blog);
    }

    //    @RequiresAuthentication//必须认证通过
    @ApiOperation(value = "根据id修改博客")
    @PutMapping("blogById")
    public Result updataById(@ApiParam(name = "blog", value = "博客信息")
                             @Validated @RequestBody MBlog blog) {
        boolean b = blogService.updateById(blog);
        if (b) {
            return Result.ok().message("修改成功");
        }
        throw new CustomExceptionHandler(20001, "修改失败");
    }

    //    @RequiresAuthentication//必须认证通过
    @ApiOperation("根据id删除博客")
    @DeleteMapping("blogById/{id}")
    public Result deleteById(@ApiParam(name = "id", value = "博客id")
                             @PathVariable String id) {
        boolean b = blogService.removeById(id);
        if (b) {
            return Result.ok().message("删除成功");
        }
        throw new CustomExceptionHandler(20001, "删除失败");
    }

    //    @RequiresAuthentication//必须认证通过
    @ApiOperation("添加博客")
    @PostMapping("insertBlog")
    public Result insertBlog(@ApiParam(name = "blog", value = "博客信息")
                             @Validated @RequestBody InsertBlog blog) {
        MBlog mBlog = new MBlog();
        BeanUtils.copyProperties(blog, mBlog);//把blog的属性给mBlog
        mBlog.setStatus(0);//0表示未发布状态
        mBlog.setIsDelete(false);//表示未删除
        boolean b = blogService.save(mBlog);
        if (b) {
            return Result.ok().message("保存成功");
        }
        throw new CustomExceptionHandler(20001, "保存失败");
    }
}

