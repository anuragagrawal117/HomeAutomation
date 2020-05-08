package com.system.iot.homeautomation.service.DeviceBehaviorImpl;

import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import com.system.iot.homeautomation.service.DeviceBehavior;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class Light_AAA_hg11 implements DeviceBehavior {

    @Autowired
    private DeviceConfigurationRepository deviceConfRepository;

    @Override
    public DeviceConfiguration getInstance() {
        String description = "Lights can be turned on and off and adjusted for brightness.";
        HashMap<String, Object> hardwareConf = new HashMap<>();
        hardwareConf.put("manufacturer", "AAA");
        hardwareConf.put("model", "hg11");

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("max_brightness", 100);
        parameters.put("min_brightness", 0);

        HashMap<String, Object> defAttributes = new HashMap<>();
        defAttributes.put("on", false);
        defAttributes.put("brightness", 60);

        DeviceConfiguration deviceConf = DeviceConfiguration.builder()
                .type(this.getClass().getSimpleName())
                .description(description)
                .hardwareConfiguration(hardwareConf)
                .parameters(parameters)
                .defaultAttributes(defAttributes)
                .build();

        return deviceConf;

    }
}
