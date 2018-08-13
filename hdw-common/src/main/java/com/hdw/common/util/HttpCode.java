package com.hdw.common.util;

/**
 * @author TuMinglong
 * @version 1.0.0
 * @description Ajax 请求时的自定义查询状态码，主要参考Http状态码
 * @date 2018年4月19日 下午3:57:46
 */
public enum HttpCode {
    /**
     * 200请求成功
     */
    OK(200),
    /**
     * 207频繁操作
     */
    MULTI_STATUS(207),
    /**
     * 400请求参数出错
     */
    BAD_REQUEST(400),
    /**
     * 401没有登录
     */
    UNAUTHORIZED(401),
    /**
     * 402登录失败
     */
    LOGIN_FAIL(402),
    /**
     * 403没有权限
     */
    FORBIDDEN(403),
    /**
     * 404找不到页面
     */
    NOT_FOUND(404),
    /**
     * 405请求方法不能被用于请求相应的资源
     */
    METHOD_NOT_ALLOWED(405),
    /**
     * 406内容特性不满足
     */
    NOT_ACCEPTABLE(406),
    /**
     * 408请求超时
     */
    REQUEST_TIMEOUT(408),
    /**
     * 409发生冲突
     */
    CONFLICT(409),
    /**
     * 410已被删除
     */
    GONE(410),
    /**
     * 411没有定义长度
     */
    LENGTH_REQUIRED(411),
    /**
     * 412条件不满足
     */
    PRECONDITION_FAILED(412),
    /**
     * 413数据太大
     */
    ENTITY_TOO_LARGE(413),
    /**
     * 415不是服务器中所支持的格式
     */
    UNSUPPORTED_MEDIA_TYPE(415),
    /**
     * 421连接数过多
     */
    TOO_MANY_CONNECTIONS(421),
    /**
     * 423已被锁定
     */
    LOCKED(423),
    /**
     * 451法律不允许
     */
    UNAVAILABLE_LEGAL(451),
    /**
     * 500服务器出错
     */
    INTERNAL_SERVER_ERROR(500),
    /**
     * 501不支持当前请求所需要的某个功能
     */
    NOT_IMPLEMENTED(501),
    /**
     * 503服务器升级中,暂时不可用
     */
    SERVICE_UNAVAILABLE(503),
    /**
     * 501获取资源所需要的策略并没有被满足
     */
    NOT_EXTENDED(510);

    private final Integer value;

    private HttpCode(Integer value) {
        this.value = value;
    }


    public Integer value() {
        return this.value;
    }

    public String toString() {
        return this.value.toString();
    }
}
