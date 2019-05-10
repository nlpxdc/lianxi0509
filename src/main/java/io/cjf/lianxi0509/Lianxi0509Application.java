package io.cjf.lianxi0509;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.cjf.lianxi0509.dao")
public class Lianxi0509Application {

    public static void main(String[] args) {
        SpringApplication.run(Lianxi0509Application.class, args);
    }

}
