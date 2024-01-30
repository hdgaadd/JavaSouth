package org.codeman;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * created on 2022/09/14
 *
 * nacos配置文件参考:
 *     Data ID: environment-dev.yaml
 *     Group: DEFAULT_GROUP
 *     配置内容:
 *     environment:
 *         content: "halo, dev"
 */
@RestController
@RefreshScope
public class Controller {

    @Value("${environment.content}")
    private String config;

    @GetMapping()
    public String robot() {
        return config;
    }

}
