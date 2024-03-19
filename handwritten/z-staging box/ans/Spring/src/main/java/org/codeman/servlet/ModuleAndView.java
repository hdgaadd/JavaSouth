package org.codeman.servlet;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hdgaadd
 * created on 2022/03/10
 */
@Data
public class ModuleAndView {

    /**
     * ModuleAndView标识名
     */
    private String viewName;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    public ModuleAndView() {

    }

    public ModuleAndView(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    /**
     * 添加模型数据
     * @param attributeName
     * @param attribute
     */
    public void addAttribute(String attributeName, Object attribute) {
        this.attributes.put(attributeName, attribute);
    }
}
