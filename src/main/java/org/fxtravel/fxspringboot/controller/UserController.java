package org.fxtravel.fxspringboot.controller;

import jakarta.servlet.http.HttpSession;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.user.GrantAdminRequest;
import org.fxtravel.fxspringboot.pojo.dto.user.UpdateUserDataRequest;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userdata")
    public ResponseEntity<?> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/userdata/update")
    public ResponseEntity<?> updateUserData(HttpSession session, @RequestBody UpdateUserDataRequest request) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        userService.updateUserData(user ,request.getUsername(),request.getGender());
        return ResponseEntity.ok().build();
    }

    // 仅管理员可访问
    @PostMapping("/grant")
    public ResponseEntity<?> grantAdminRole(HttpSession session, @RequestBody GrantAdminRequest request) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }
        if(user.getRole().equals(Role.REGULAR)){
            return ResponseEntity.badRequest().body(Map.of("error", "权限不足"));
        }
        boolean success = userService.grantAdminRole(request.getUserId());

        if (success) {
            return ResponseEntity.ok(Map.of("message", "已成功授予管理员权限"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "用户不存在或操作失败"));
        }
    }
}
