package com.hdw.common.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hdw.common.constant.IsMenu;


/**
 * @author TuMinglong
 * @Descriptin 菜单的节点
 * @Date 2018年5月6日 上午11:56:22
 */
@SuppressWarnings("rawtypes")
public class MenuNode implements Comparable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 节点id
     */
    private Long id;

    /**
     * 父节点
     */
    private Long pid;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 按钮级别
     */
    @JsonInclude(Include.NON_NULL)
    private Integer levels;

    /**
     * 是否菜单（0-是，1-否）
     */
    @JsonInclude(Include.NON_NULL)
    private Integer ismenu;

    /**
     * 状态（0-正常，1-关闭）
     */
    @JsonInclude(Include.NON_NULL)
    private Integer status;

    /**
     * 按钮的排序
     */
    @JsonInclude(Include.NON_NULL)
    private Integer num;

    /**
     * 节点的url
     */
    private String url;

    /**
     * 节点图标
     */
    private String icon;

    /**
     * 子节点的集合
     */
    @JsonInclude(Include.NON_NULL)
    private List<MenuNode> children;

    /**
     * 查询子节点时候的临时集合
     */
    private List<MenuNode> linkedList = new ArrayList<MenuNode>();

    public MenuNode() {
        super();
    }

    public MenuNode(Long id, Long pid) {
        super();
        this.id = id;
        this.pid = pid;
    }

    public static MenuNode createRoot() {
        return new MenuNode(0L, -1L);
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getIsmenu() {
        return ismenu;
    }

    public void setIsmenu(Integer ismenu) {
        this.ismenu = ismenu;
    }

    @Override
    public String toString() {
        return "MenuNode{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", levels=" + levels +
                ", num=" + num +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", linkedList=" + linkedList +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        MenuNode menuNode = (MenuNode) o;
        Integer num = menuNode.getNum();
        if (num == null) {
            num = 0;
        }
        return this.num.compareTo(num);
    }

    /**
     * 构建整个菜单树
     *
     * @param nodeList
     */
    public void buildNodeTree(List<MenuNode> nodeList) {
        for (MenuNode treeNode : nodeList) {
            List<MenuNode> linkedList = treeNode.findChildNodes(nodeList, treeNode.getId());
            if (linkedList.size() > 0) {
                treeNode.setChildren(linkedList);
            }
        }
    }

    /**
     * 查询子节点的集合
     *
     * @param nodeList
     * @param pid
     * @return
     */
    public List<MenuNode> findChildNodes(List<MenuNode> nodeList, Long pid) {
        if (nodeList == null && pid == null)
            return null;
        for (Iterator<MenuNode> iterator = nodeList.iterator(); iterator.hasNext(); ) {
            MenuNode node = (MenuNode) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getPid() != 0 && pid.equals(node.getPid())) {
                recursionFn(nodeList, node, pid);
            }
        }
        return linkedList;
    }

    /**
     * 遍历一个节点的子节点
     *
     * @param nodeList
     * @param node
     * @param pId
     */
    public void recursionFn(List<MenuNode> nodeList, MenuNode node, Long pId) {
        List<MenuNode> childList = getChildList(nodeList, node);// 得到子节点列表
        if (childList.size() > 0) {// 判断是否有子节点
            if (node.getPid().equals(pId)) {
                linkedList.add(node);
            }
            Iterator<MenuNode> it = childList.iterator();
            while (it.hasNext()) {
                MenuNode n = (MenuNode) it.next();
                recursionFn(nodeList, n, pId);
            }
        } else {
            if (node.getPid().equals(pId)) {
                linkedList.add(node);
            }
        }
    }


    /**
     * 得到子节点列表
     *
     * @param list
     * @param node
     * @return
     */
    private List<MenuNode> getChildList(List<MenuNode> list, MenuNode node) {
        List<MenuNode> nodeList = new ArrayList<MenuNode>();
        Iterator<MenuNode> it = list.iterator();
        while (it.hasNext()) {
            MenuNode n = (MenuNode) it.next();
            if (n.getPid().equals(node.getId())) {
                nodeList.add(n);
            }
        }
        return nodeList;
    }

    /**
     * 清除掉按钮级别的资源
     *
     * @param nodes
     * @return
     */
    public static List<MenuNode> clearBtn(List<MenuNode> nodes) {
        ArrayList<MenuNode> noBtns = new ArrayList<MenuNode>();
        for (MenuNode node : nodes) {
            if (node.getIsmenu() == IsMenu.YES.getCode()) {
                noBtns.add(node);
            }
        }
        return noBtns;
    }

    /**
     * 清除所有二级菜单
     *
     * @param nodes
     * @return
     */
    public static List<MenuNode> clearLevelTwo(List<MenuNode> nodes) {
        ArrayList<MenuNode> results = new ArrayList<MenuNode>();
        for (MenuNode node : nodes) {
            Integer levels = node.getLevels();
            if (levels.equals(1)) {
                results.add(node);
            }
        }
        return results;
    }

    /**
     * 构建菜单列表
     *
     * @date 2017年2月19日 下午11:18:19
     */
    @SuppressWarnings("unchecked")
    public static List<MenuNode> buildTitle(List<MenuNode> nodes) {

        List<MenuNode> clearBtn = clearBtn(nodes);

        new MenuNode().buildNodeTree(clearBtn);

        List<MenuNode> menuNodes = clearLevelTwo(clearBtn);
        //对菜单排序
        Collections.sort(menuNodes);
        //对菜单的子菜单进行排序
        for (MenuNode menuNode : menuNodes) {
            if (menuNode.getChildren() != null && menuNode.getChildren().size() > 0) {
                Collections.sort(menuNode.getChildren());
            }
        }

        return menuNodes;
    }
}
