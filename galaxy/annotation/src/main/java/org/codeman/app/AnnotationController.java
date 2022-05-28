package org.codeman.app;

import org.codeman.component.Employee;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author hdgaadd
 * Created on 2022/05/28
 */
@RestController
@RequestMapping("/annotation")
public class AnnotationController {

    private String UNIFIED_RESULT = "默认返回结果";

    @GetMapping("/@Valid")
    public String valid(@Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return UNIFIED_RESULT;
    }

    @GetMapping("/@Valid/advance")
    public String valid(@Valid Employee employee) {
        return UNIFIED_RESULT;
    }
}
