package com.ypp.yppjwt;

import com.ypp.yppjwt.JwtUtil;
import com.ypp.yppjwt.annotation.CheckLogin;
import com.ypp.yppjwt.annotation.CheckPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ypp
 * @since 2020-08-31
 */
@RestController
@RequestMapping("/student")
public class MockController {

    private static final Logger logger = LoggerFactory.getLogger(MockController.class);


    @PostMapping("/login")
    Map<String, String> login() {
        String token = JwtUtil.issue(new UserAuthVo(1, "ypp"));

        Map<String, String> map = new HashMap<>();
        map.put("access-token", token);
        return map;
    }

    @CheckLogin
    @GetMapping("/auth")
    Object auth() {
        UserAuthVo studentAuthVo = (UserAuthVo) JwtUtil.extract();
        logger.info(studentAuthVo.toString());
        return "success";
    }

    @CheckLogin
    @CheckPermission("student")
    @GetMapping("/perm1")
    Object perm1() {
        return "success";
    }

    @CheckLogin
    @CheckPermission("user")
    @GetMapping("/perm2")
    Object perm2() {
        return "success";
    }

}
