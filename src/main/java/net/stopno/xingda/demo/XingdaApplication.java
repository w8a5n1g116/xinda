package net.stopno.xingda.demo;

import net.stopno.xingda.demo.filter.SessionFilter;
import net.stopno.xingda.demo.mapper.BaseMapper;
import net.stopno.xingda.demo.service.IBaseService;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.Map;

@SpringBootApplication
@MapperScan("net.stopno.xingda.demo.mapper")
public class XingdaApplication {
    public static void main(String[] args) {
        SpringApplication.run(XingdaApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SessionFilter());
        registration.addUrlPatterns("/*");
        registration.setName("SessionFilter");
        registration.setOrder(1);
        return registration;
    }

}
