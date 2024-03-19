package test.controller;

import org.codeman.annotation.Autowired;
import org.codeman.annotation.Controller;
import org.codeman.annotation.RequestMapping;
import test.service.UserService;

/**
 * @author hdgaadd
 * created on 2022/05/06
 */
@Controller
@RequestMapping("/test")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/methodOne")
    public String testOne() {
        return userService.getUser().toString();
    }
}
