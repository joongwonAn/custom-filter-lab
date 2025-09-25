package com.sprint.team2.customfilterlab.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class PublicController {
    @GetMapping("/public/hello")
    public String hello(@RequestParam(defaultValue = "world") String name,
                        @RequestParam(required = false) String password) {
        // password 파라미터는 로깅 필터에서 마스킹되는지 확인용
        return "Hello, " + name + "!";
    }
}
