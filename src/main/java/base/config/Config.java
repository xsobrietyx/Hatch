package base.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import service.IOTServiceImpl;

import javax.annotation.PostConstruct;

/**
 * Created by xsobrietyx on 12-March-2019 time 18:57
 */
@SpringBootApplication
@ComponentScan("base.web")
public class Config {
    public static void main(String[] args) {
        SpringApplication.run(Config.class, args);
    }

    @PostConstruct
    public void initService() {
        IOTServiceImpl.init();
    }
}
