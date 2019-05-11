package io.cjf.lianxi0509.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class TempController {

    @Value("${db.urlPara}")
    private String dbUrl;

    @GetMapping("/test")
    public void test(){

    }
}
