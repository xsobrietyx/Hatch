package base.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import service.Device;
import service.IOTServiceImpl;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static scala.math.BigDecimal.int2bigDecimal;
import static service.abstarctions.DeviceType.heartRateMeter;
import static service.abstarctions.DeviceType.musicPlayer;
import static service.abstarctions.DeviceType.thermostat;

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
        LocalDateTime startTime = LocalDateTime.now();

        try {
            IOTServiceImpl.addDevice(new Device(thermostat(), int2bigDecimal(0), startTime));
            IOTServiceImpl.addDevice(new Device(heartRateMeter(), int2bigDecimal(50), startTime));
            IOTServiceImpl.addDevice(new Device(musicPlayer(), int2bigDecimal(100), startTime));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
}
