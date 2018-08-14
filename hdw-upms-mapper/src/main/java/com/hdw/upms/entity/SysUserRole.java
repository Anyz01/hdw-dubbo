package com.hdw.upms.entity;



import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.hdw.common.util.JacksonUtils;


/**
 * 用户角色
 */
@TableName("t_sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type=IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value="user_id")
    private Long userId;

    /**
     * 角色id
     */
    @TableField(value="role_id")
    private Long roleId;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }
}
