package org.codeman;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdgaadd
 * created on 2022/09/14
 *
 * http://localhost:8066
 *
 * run: java -jar baby-1.0-SNAPSHOT.jar
 * stop: 1. netstat -aon | findstr "8066"
 *       2. taskkill /pid 18536 -t -f
 */
@RestController
public class Controller {

    final static String response = "halo, baby!";

    @GetMapping()
    public String robot() {
        return response;
    }

}
