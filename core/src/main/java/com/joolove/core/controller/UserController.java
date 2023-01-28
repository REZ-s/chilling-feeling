package com.joolove.core.controller;

import com.joolove.core.domain.member.User;
import com.joolove.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
//
//    @GetMapping("/")
//    public String getMainPage(Model model) {
//        model.addAttribute("allUserList", userService.findAll());
//        //ResponseEntity.status(HttpStatus.OK).header().body(userService.findAll());
//        return "test_main";
//    }

    @GetMapping("/join_new_user")
    public String createUser(Model model) {
        User user = User.builder().
                username(RandomString.make(10)).
                accountType((short)1).
                build();
        model.addAttribute("user", user);
        return "create_user";
    }

    @PostMapping("/insert")
    public String insertUser(@ModelAttribute("user") User user) {
        userService.join(user);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable(value = "id") UUID id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "update_user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") UUID id) {
        userService.leave(userService.findOne(id));
        return "redirect:/";
    }
}
