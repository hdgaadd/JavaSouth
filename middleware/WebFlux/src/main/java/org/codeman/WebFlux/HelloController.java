package org.codeman.WebFlux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author hdgaadd
 * created on 2024/01/03
 *
 * <a href="http://localhost:8080/hello0">...</a>、<a href="http://localhost:8080/hello1">...</a>
 */
@RestController
public class HelloController {

    @GetMapping("/hello0")
    public String hello0() {
        return "Spring MVC";
    }

    @GetMapping("/hello1")
    public Mono<String> hello1() {
        return Mono.just("WebFlux");
    }
}