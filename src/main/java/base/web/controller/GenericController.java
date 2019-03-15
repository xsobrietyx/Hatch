package base.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.Enumeration;
import service.IOTServiceImpl;
import service.abstarctions.DeviceType;
import service.abstarctions.RequestedInformation;

import javax.annotation.PostConstruct;
import java.util.HashMap;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by xsobrietyx on 12-March-2019 time 18:55
 */
@RestController
public class GenericController {
    private HashMap<String, Enumeration.Value> devicesMapping;

    @PostConstruct
    private void initDataStreaming() {
        IOTServiceImpl.init();
        devicesMapping = new HashMap<>();
        devicesMapping.put("thermostat", DeviceType.thermostat());
        devicesMapping.put("heartRateMeter", DeviceType.heartRateMeter());
        devicesMapping.put("musicPlayer", DeviceType.musicPlayer());
    }

    @RequestMapping("/")
    public String index() {
        String separator = "<br>";
        return "All available endpoints is:" + separator +
                "/{type}/min" + separator +
                "/{type}/max" + separator +
                "/{type}/median" + separator +
                "/{type}/average" + separator +
                "types: thermostat, heartRateMeter, musicPlayer";
    }

    @RequestMapping(value = "/{type}/min", method = GET)
    public String getMin(@PathVariable String type) {
        return "min:" + IOTServiceImpl.getData(devicesMapping.get(type), RequestedInformation.min());
    }

    @RequestMapping(value = "/{type}/max", method = GET)
    public String getMax(@PathVariable String type) {
        return "max:" + IOTServiceImpl.getData(devicesMapping.get(type), RequestedInformation.max());
    }

    @RequestMapping(value = "/{type}/median", method = GET)
    public String getMedian(@PathVariable String type) {
        return "median:" + IOTServiceImpl.getData(devicesMapping.get(type), RequestedInformation.median());
    }

    @RequestMapping(value = "/{type}/average", method = GET)
    public String getAverage(@PathVariable String type) {
        return "average:" + IOTServiceImpl.getData(devicesMapping.get(type), RequestedInformation.average());
    }

}