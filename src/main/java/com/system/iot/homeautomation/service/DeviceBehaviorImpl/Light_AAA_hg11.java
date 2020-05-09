package com.system.iot.homeautomation.service.DeviceBehaviorImpl;

import com.system.iot.homeautomation.enums.DeviceConfigurationType;
import com.system.iot.homeautomation.enums.DeviceStatusType;
import com.system.iot.homeautomation.exception.DeviceOfflineException;
import com.system.iot.homeautomation.exception.InvalidCommandException;
import com.system.iot.homeautomation.model.Command;
import com.system.iot.homeautomation.model.Device;
import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import com.system.iot.homeautomation.repository.DeviceRepository;
import com.system.iot.homeautomation.service.DeviceBehavior;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class Light_AAA_hg11 implements DeviceBehavior {

    @Autowired
    private DeviceRepository deviceRepository;

    private final Integer MAX_BRIGHTNESS = 100;
    private final Integer MIN_BRIGHTNESS = 0;

    @Override
    public DeviceConfiguration getInstance() {
        String description = "Lights can be turned on and off and adjusted for brightness.";
        HashMap<String, Object> hardwareConf = new HashMap<>();
        hardwareConf.put("manufacturer", "AAA");
        hardwareConf.put("model", "hg11");

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("max_brightness", MAX_BRIGHTNESS);
        parameters.put("min_brightness", MIN_BRIGHTNESS);

        HashMap<String, Object> defAttributes = new HashMap<>();
        defAttributes.put("on", false);
        defAttributes.put("brightness", 60);

        DeviceConfiguration deviceConf = DeviceConfiguration.builder()
                .type(DeviceConfigurationType.valueOf(this.getClass().getSimpleName()))
                .description(description)
                .hardwareConfiguration(hardwareConf)
                .parameters(parameters)
                .defaultAttributes(defAttributes)
                .build();

        return deviceConf;
    }

    public ArrayList<String> executeCommand(Device device, Command command){
        if(device.status().equals(DeviceStatusType.OFFLINE)) throw new DeviceOfflineException();
        ArrayList<String> performedActions = new ArrayList<>();
        HashMap<String, Object> commandParams = command.params();

        switch(command.type()){
            case OnOff:
            {
                if(commandParams.get("on") == null) throw new InvalidCommandException("On missing!");
                String action = setOnOff(device, (Boolean)commandParams.get("on"));
                if(action != null) performedActions.add(action);
                break;
            }
            case SetBrightness:
            {
                if(commandParams.get("brightness") == null
                        || MAX_BRIGHTNESS < (Integer)commandParams.get("brightness")
                        || MIN_BRIGHTNESS > (Integer)commandParams.get("brightness"))
                    throw new InvalidCommandException("brightness missing or not valid!");
                String action = setOnOff(device, true);
                if(action != null) performedActions.add(action);
                action = setBrightness(device, (Integer)commandParams.get("brightness"));
                if(action != null) performedActions.add(action);
                break;
            }
            default:
            {
                throw new InvalidCommandException("command invalid for device configuration!");
            }
        }
        return performedActions;
    }

    private String setOnOff(Device device, Boolean on){
        if(device.attributes().get("on") != on){
            device.attributes().put("on", on);
            deviceRepository.save(device);
            return "light switched " + (on ? "On" : "Off");
        }else{
            log.info("light is already " + (on ? "On" : "Off"));
            return null;
        }
    }

    private String setBrightness(Device device, Integer brightness){
        if(device.attributes().get("brightness") != brightness){
            device.attributes().put("brightness", brightness);
            deviceRepository.save(device);
            return "light brightness changed to " + brightness;
        }else{
            log.info("light brightness is already " + brightness);
            return null;
        }
    }
}
