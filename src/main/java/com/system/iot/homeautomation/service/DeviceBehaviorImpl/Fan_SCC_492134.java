package com.system.iot.homeautomation.service.DeviceBehaviorImpl;

import com.system.iot.homeautomation.enums.DeviceConfigurationType;
import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.service.DeviceBehavior;

import java.util.ArrayList;
import java.util.HashMap;

public class Fan_SCC_492134 implements DeviceBehavior {

    @Override
    public DeviceConfiguration getInstance() {
        String description = "Fans can be turned on and off and have speed settings";
        HashMap<String, Object> hardwareConf = new HashMap<>();
        hardwareConf.put("manufacturer", "SCC");
        hardwareConf.put("model", "492134");

        HashMap<String, Object> parameters = new HashMap<>();
        ArrayList<String> availableSpeeds = new ArrayList<>();
        availableSpeeds.add("S1");
        availableSpeeds.add("S2");
        availableSpeeds.add("S3");
        availableSpeeds.add("S4");
        availableSpeeds.add("S5");

        parameters.put("available_speeds", availableSpeeds);

        HashMap<String, Object> defAttributes = new HashMap<>();
        defAttributes.put("on", false);
        defAttributes.put("speed", "S3");

        DeviceConfiguration deviceConf = DeviceConfiguration.builder()
                .type(DeviceConfigurationType.valueOf(this.getClass().getSimpleName()))
                .description(description)
                .hardwareConfiguration(hardwareConf)
                .parameters(parameters)
                .defaultAttributes(defAttributes)
                .build();

        return deviceConf;
    }
}
