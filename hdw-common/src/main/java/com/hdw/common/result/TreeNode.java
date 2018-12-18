package com.hdw.common.result;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @Description vue tree 插件的节点
 * @Author TuMinglong
 * @Date 2018/12/13 18:37
 */
public class TreeNode implements Serializable {

    private String id;    //节点id

    private String parentId;//父节点Id

    private String name;//节点名称

    private Boolean open;//是否打开节点

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<?> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public static TreeNode createParent() {
        TreeNode treeNode = new TreeNode();
        treeNode.setId("0");
        treeNode.setName("顶级");
        treeNode.setOpen(true);
        treeNode.setParentId("0");
        return treeNode;
    }
}
