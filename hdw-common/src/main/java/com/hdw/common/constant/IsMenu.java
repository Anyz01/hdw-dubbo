package com.hdw.common.constant;

/**
 * @author TuMinglong
 * @Descriptin 是否是菜单的枚举
 * @Date 2018年5月6日 上午11:55:38
 */
public enum IsMenu {

    YES(0, "是"),
    NO(1, "不是"),//不是菜单的是按钮
    OPEN(0, "开启"),
    CLOSE(1, "关闭");

    int code;
    String message;

    IsMenu(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (IsMenu s : IsMenu.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
