package base.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.IOTServiceImpl;
import service.abstarctions.RequestedInformation;

import javax.annotation.PostConstruct;

/**
 * Created by xsobrietyx on 12-March-2019 time 18:55
 */
@RestController
public class SomeController {
    @PostConstruct
    private void initDataStreaming() {
        IOTServiceImpl.init();
    }

    @RequestMapping("/")
    public String index() {
        return "min:" + IOTServiceImpl.getData(RequestedInformation.min()).toString()
                + " max:" + IOTServiceImpl.getData(RequestedInformation.max())
                + " median:" + IOTServiceImpl.getData(RequestedInformation.median())
                + " average:" + IOTServiceImpl.getData(RequestedInformation.average());
    }

}
/*
    TODO: split service method calls to different endpoints
    TODO: add java docs
    TODO: few additional dataSources should be created (thermostat etc.) and additional REST endpoints should be built for them
 */