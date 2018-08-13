package com.hdw.websocket.config;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author TuMinglong
 * @Description 页面推送消息
 * @date 2018/6/28 21:19
 */


@ServerEndpoint(value = "/ws/sms/{userId}")
@Component
public class SmsPushSocket {

    private final static Logger logger = LoggerFactory.getLogger(SmsPushSocket.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<SmsPushSocket> wsClientMap = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //用户Id
    private String userId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 当前会话session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        try {
            this.session = session;
            this.userId = userId;
            SmsPushSocket _this = getcurrentWenSocket(this.userId);//当前登录用户校验 每个用户同时只能连接一次
            if (_this != null) {
                //sendMessage("您已有连接信息，不能重复连接！");
                return;
            }
            wsClientMap.add(this);
            addOnlineCount();
            //sendMessage("连接成功！");
            logger.info(session.getId() + "有新链接加入，当前链接数为：" + wsClientMap.size());
        } catch (Exception e) {
            e.getMessage();
            logger.error("websocket 发送消息异常：登录用户ID=" + this.userId + ",Exception=" + e.getMessage());
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        boolean b = wsClientMap.remove(this);//从set中删除
        if (b && getOnlineCount() > 0) {
            subOnlineCount(); //在线人数减1
        }
        logger.info("有一连接关闭，当前在线人数为：" + wsClientMap.size());
    }

    /**
     * 收到客户端消息
     *
     * @param message 客户端发送过来的消息
     * @param session 当前会话session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            SmsPushSocket _this = null;
            for (SmsPushSocket item : wsClientMap) {
                if (item.session.getId() == session.getId()) {
                    _this = item;
                }
            }
            if (_this == null) {
                this.sendMessage("未连接不能发送消息！");
                return;
            }
            logger.info("来自客户端的消息:" + message);
            this.sendMessage("来自服务端的消息：<已读>" + message);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("websocket 发送消息异常：登录用户ID=" + this.userId + ",Exception=" + e.getMessage());
        }
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        logger.info("SmsPushSocket发生错误!");
    }

    /**
     * 给当前用户发送消息（单条）
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        logger.info("成功发送一条消息:" + message);
    }

    /**
     * 给指定用户发送消息（单人单条）
     *
     * @param userId
     * @param message
     * @throws IOException
     */
    public void sendMessage(String userId, String message) {
        try {
            if (userId == null || StringUtils.isBlank(message)) {
                return;
            }
            SmsPushSocket _this = getcurrentWenSocket(userId);
            if (_this == null) {
                //用户不在线
                return;
            }
            _this.sendMessage(message);
            logger.info("成功发送一条消息:" + message);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("websocket 发送消息异常 Exception=" + e.getMessage());
        }
    }

    /**
     * 给指定的用户发送消息（单条）
     *
     * @param userIds
     * @param message
     */
    public void sendMessageList(List<String> userIds, String message) {
        if (userIds == null || userIds.size() < 1 || StringUtils.isBlank(message)) {
            return;
        }
        for (String userId : userIds) {
            SmsPushSocket _this = getcurrentWenSocket(userId);
            if (_this == null) {
                //用户不在线
                logger.info("用户:" + userId + "不在线：" + message);
                continue;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("用户:" + userId + "在线：" + message);
                        _this.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("websocket 发送消息异常：登录用户ID=" + userId + ",Exception=" + e.getMessage());
                    }
                }
            }).start();
        }
        logger.info("成功群发一条消息:" + wsClientMap.size());
    }

    /**
     * 给所有在线用户发消息（单条）
     *
     * @param message
     */
    public void sendMessageAll(String message) {
        if (wsClientMap == null || wsClientMap.size() < 1 || StringUtils.isBlank(message)) {
            return;
        }
        for (SmsPushSocket item : wsClientMap) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        item.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("websocket 发送消息异常：登录用户ID=" + item.userId + ",Exception=" + e.getMessage());
                    }
                }
            }).start();
        }
        logger.info("成功群发一条消息:" + wsClientMap.size());
    }


    public static synchronized int getOnlineCount() {
        return SmsPushSocket.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SmsPushSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SmsPushSocket.onlineCount--;
    }

    /**
     * 根据当前登录用户ID获取websocket对象
     *
     * @param userId
     * @return
     */
    public static SmsPushSocket getcurrentWenSocket(String userId) {
        if (userId == null || wsClientMap == null || wsClientMap.size() < 1) {
            return null;
        }
        for (SmsPushSocket item : wsClientMap) {
            if (item.userId.equals(userId)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 根据当前登录用户ID获取websocket对象集合
     *
     * @param userId
     * @return
     */
    public static List<SmsPushSocket> getcurrentWenSocketList(String userId) {
        List<SmsPushSocket> list = new ArrayList<>();
        if (userId == null || wsClientMap == null || wsClientMap.size() < 1) {
            return null;
        }
        for (SmsPushSocket item : wsClientMap) {
            String tempId = item.userId.substring(0, item.userId.lastIndexOf("_"));
            logger.info("----" + "前台传递过来的ID：" + tempId + " 登录的ID：" + userId + "----");
            if (tempId.equals(userId)) {
                list.add(item);
            }
        }
        return list;
    }

}
