package org.codeman;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author hdgaadd
 * created on 2023/01/14
 */
@RestController
public class OpenController {

    @GetMapping("/getBigList")
    public String getBigList() {
        int len = 1024 * 1024 * 1;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            list.add(i + "");
        }
        return list.toString();
    }
}
