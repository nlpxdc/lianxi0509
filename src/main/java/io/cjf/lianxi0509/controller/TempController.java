package io.cjf.lianxi0509.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class TempController {

    @Value("${db.urlPara}")
    private String dbUrl;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/test")
    public void test(){
        logger.info("temp test");
    }
}
