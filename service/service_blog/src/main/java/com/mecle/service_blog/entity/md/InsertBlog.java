package com.mecle.service_blog.entity.md;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class InsertBlog implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "简介内容")
    private String description;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发布状态")
    private Integer status;
}
