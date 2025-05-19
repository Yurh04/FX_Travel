package org.fxtravel.fxspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.fxtravel.fxspringboot.mapper")
public class FxSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(FxSpringbootApplication.class, args);
    }
}
