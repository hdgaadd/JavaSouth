package org.codeman.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hdgaadd
 * created on 2022/12/26
 */
@Component
public class HandleManager implements ApplicationContextAware {

    private final Map<String, HandleInterface> HANDLE_BEAN_MAP = new ConcurrentHashMap<>();

    public String fileHandle(HandleTypeEnum handleEnum, String parameter) {
        HandleInterface handleBean = HANDLE_BEAN_MAP.get(handleEnum.toString());

        if (handleBean != null) {
            return handleBean.handleFile(parameter);
        }
        return "does not exit this FileTypeHandleEnum";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, HandleInterface> beanMap = applicationContext.getBeansOfType(HandleInterface.class);
        beanMap.values().forEach(o -> HANDLE_BEAN_MAP.put(o.getType(), o));
    }
}
