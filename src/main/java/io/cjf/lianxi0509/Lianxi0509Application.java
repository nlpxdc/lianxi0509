package io.cjf.lianxi0509;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("io.cjf.lianxi0509.dao")
@EnableCaching
public class Lianxi0509Application {

    public static void main(String[] args) {
        SpringApplication.run(Lianxi0509Application.class, args);
    }

}
