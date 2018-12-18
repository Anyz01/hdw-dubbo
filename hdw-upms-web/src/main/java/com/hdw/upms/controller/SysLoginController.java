package com.hdw.upms.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.google.code.kaptcha.Producer;
import com.hdw.common.base.BaseController;
import com.hdw.common.config.redis.IRedisService;
import com.hdw.common.result.ResultMap;
import com.hdw.enterprise.entity.Enterprise;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.upms.entity.SysUserToken;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.ISysResourceService;
import com.hdw.upms.service.ISysUserService;
import com.hdw.upms.service.ISysUserTokenService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.aouth2.TokenGenerator;
import com.hdw.upms.shiro.form.SysLoginForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

/**
 * @Description 登录退出Controller
 * @Author TuMinglong
 * @Date 2018/6/11 17:07
 */
@Api(value = "登录退出", tags = {" 登录退出"})
@RestController
public class SysLoginController extends BaseController {

    @Reference
    private ISysUserService userService;

    @Reference
    private ISysUserTokenService userTokenService;

    @Reference
    private ISysResourceService resourceService;

    @Reference
    private IEnterpriseService enterpriseService;

    @Autowired
    private Producer producer;

    @Autowired
    private IRedisService redisService;

    //30分钟过期
    @Value("${redis.expire}")
    private int EXPIRE;

    @GetMapping("captcha.jpg")
    public void kaptcha(HttpServletResponse response, String uuid) throws IOException {
        logger.info("前台请求的UUID:" + uuid);
        if (StringUtils.isBlank(uuid)) {
            throw new RuntimeException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();
        redisService.set(uuid, code);

        response.setHeader("Cache-Control", "no-store,no-cache");
        response.setContentType("image/jpeg");

        BufferedImage image = producer.createImage(code);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        IOUtils.closeQuietly(outputStream);
    }


    /**
     * 登录
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/sys/login")
    public Object login(@RequestBody SysLoginForm form) {
        logger.info("POST请求登录");
        if (StringUtils.isBlank(form.getUsername())) {
            return ResultMap.error("用户名不能为空");
        }
        if (StringUtils.isBlank(form.getPassword())) {
            return ResultMap.error("密码不能为空");
        }
        if (StringUtils.isBlank(form.getCaptcha())) {
            return ResultMap.error("验证码不能为空");
        }
        if (StringUtils.isBlank(form.getUuid())) {
            return ResultMap.error("uuid不能为空");
        }
        String validateCode = (String) redisService.get(form.getUuid());
        logger.info("session中的图形码字符串:" + validateCode);

        //比对
        if (StringUtils.isBlank(form.getCaptcha()) || StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(form.getCaptcha())) {
            return ResultMap.error("验证码错误");
        }

        UserVo userVo =userService.selectByLoginName(form.getUsername());

        if (null == userVo) {
            return ResultMap.error("账号不存在");
        }
        if (!userVo.getPassword().equals(ShiroKit.md5(form.getPassword(), userVo.getLoginName() + userVo.getSalt()))) {
            return ResultMap.error("密码不正确");
        }
        //当企业不存在或者企业被禁用不允许登录
        if(userVo.getUserType()==1){
            Enterprise sysEnterprise=enterpriseService.getById(userVo.getEnterpriseId());
            if(null!=sysEnterprise && sysEnterprise.getStatus()==1){
                return ResultMap.error("企业被禁用，该账户不允许登录");
            }else if(null==sysEnterprise){
                return ResultMap.error("企业不存在，该账户不允许登录");
            }
        }

        // 清除验证码
        redisService.del(form.getUuid());

        //生成token，并保存到数据库
        return createToken(userVo.getId());
    }

    /**
     * 退出
     */
    @ApiOperation(value = "退出", notes = "退出")
    @PostMapping("/sys/logout")
    public ResultMap logout() {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //修改token
        SysUserToken tokenEntity = new SysUserToken();
        tokenEntity.setUserId(ShiroKit.getUser().getId());
        tokenEntity.setToken(token);
        userTokenService.updateById(tokenEntity);
        return ResultMap.ok();
    }

    public ResultMap createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        SysUserToken tokenEntity = userTokenService.getById(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            userTokenService.save(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            userTokenService.updateById(tokenEntity);
        }
        ResultMap r = ResultMap.ok().put("token", token).put("expire", EXPIRE);
        return r;
    }
}
