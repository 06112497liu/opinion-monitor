/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户权限参数
 * @author tjwang
 * @version $Id: UserPermmisionVo.java, v 0.1 2017/9/27 0027 17:24 tjwang Exp $
 */
@ApiModel
public class UserPermmisionVo {

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "用户不能为空")
    private Long       userId;

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID列表")
    private List<Long> permissionIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
