package net.stopno.xingda.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("net.stopno.xingda.demo.mapper")
public class XingdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(XingdaApplication.class, args);
    }

}
