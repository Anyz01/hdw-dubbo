package com.hdw.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

/**
 * @author TuMinglong
 * @Description vue tree 插件的节点
 * @date 2018/6/22 10:38
 */
public class TreeNode implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;    //节点id

    private String pId;//父节点id

    private String name;//节点名称

    private Boolean open;//是否打开节点

    private Boolean checked;//是否被选中

    @JsonInclude(Include.NON_NULL)
    private String iconCls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public static TreeNode createParent() {
        TreeNode treeNode = new TreeNode();
        treeNode.setChecked(true);
        treeNode.setId("0");
        treeNode.setName("顶级");
        treeNode.setOpen(true);
        treeNode.setpId("0");
        return treeNode;
    }
}
