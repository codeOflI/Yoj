package com.yoj.controller.user;

import com.yoj.model.entity.User;
import com.yoj.constant.consist.RoleName;
import com.yoj.model.vo.Msg;
import com.yoj.utils.cache.EmailCache;
import com.yoj.service.UserService;
import com.yoj.utils.EmailSender;
import com.yoj.utils.VerifyImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 注册控制层
 * @Author: lmz
 * @Date: 2019/9/27
 */
@RestController
@RequestMapping("/user/r")
public class RegisterController {
    @Autowired
    UserService userService;
    @Autowired
    EmailSender emailSender;
    @Autowired
    EmailCache emailCache;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    VerifyImageUtil verifyImageUtil;

    @PostMapping("/register")
    public Msg register(@RequestBody @Valid User user, BindingResult bindingResult,
                        HttpServletRequest httpServletRequest) {

        Msg msg = new Msg();
        msg.setSuccess(true);

        if (!verifyImageUtil.verify(httpServletRequest, user.getImageCode())) {
            return Msg.fail().add("imageCode", "验证码错误");
        }
        if (bindingResult.hasErrors()) {
            msg.setSuccess(false);
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError: fieldErrors){
                msg.add(fieldError.getField(),fieldError.getDefaultMessage());
//                System.out.println(fieldError.getField() + ":" + fieldError.getDefaultMessage());
            }
            return msg;
        }
        if (userService.getUserByName(user.getUsername() )!= null) {
            msg.setSuccess(false);
            msg.add("userName", "用户名已存在");
        }
        String checkCode = emailCache.getEmailCheckCode(user.getEmail());
        if (checkCode == null || !checkCode.equals(user.getEmailCode())) {
            msg.setSuccess(false);
            msg.add("emailCode", "邮箱验证码错误");
        }
        if (!msg.isSuccess()) {
            return msg;
        }
        //
        user.setRole(RoleName.USER.toString());
        if (userService.insertUserUseCache(user) == null) {
            return Msg.fail("系统错误");
        }
        emailCache.delEmailCheckCode(user.getEmail());
        return Msg.success();
    }


    @GetMapping("/getEmailCheckCode/{email}")
    public Msg getCheckCode(@PathVariable(value = "email") String email) {
//        int i = 1 / 0;
        if (userService.queryExistByEmail(email)) {
            return Msg.fail("邮箱已被注册");
        }
        emailSender.sendRegisterEmail(email);
        return Msg.success();
    }
}
