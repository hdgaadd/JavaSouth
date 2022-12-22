package org.learn;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hdgaadd
 * created on 2022/12/22
 */
@ConfigurationProperties("learn-properties")
public class LearnConfigProperties {

    private boolean enable = true;

}
