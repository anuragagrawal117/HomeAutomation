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
public class Fan_SCC_492134 implements DeviceBehavior {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public DeviceConfiguration getInstance() {
        String description = "Fans can be turned on and off and have speed settings";
        HashMap<String, Object> hardwareConf = new HashMap<>();
        hardwareConf.put("manufacturer", "SCC");
        hardwareConf.put("model", "492134");

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("available_speeds", getAvailableSpeeds());

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
            case SetFanSpeed:
            {
                if(commandParams.get("speed") == null || !getAvailableSpeeds().contains(commandParams.get("speed")))
                    throw new InvalidCommandException("Speed missing or not valid!");
                String action = setOnOff(device, true);
                if(action != null) performedActions.add(action);
                action = setSpeed(device, (String)commandParams.get("speed"));
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
            return "fan switched " + (on ? "On" : "Off");
        }else{
            log.info("fan is already " + (on ? "On" : "Off"));
            return null;
        }
    }

    private String setSpeed(Device device, String speed){
        if(device.attributes().get("speed") != speed){
            device.attributes().put("speed", speed);
            deviceRepository.save(device);
            return "fan speed changed to " + speed;
        }else{
            log.info("fan speed is already " + speed);
            return null;
        }
    }

    private ArrayList<String> getAvailableSpeeds(){
        ArrayList<String> availableSpeeds = new ArrayList<>();
        availableSpeeds.add("S1");
        availableSpeeds.add("S2");
        availableSpeeds.add("S3");
        availableSpeeds.add("S4");
        availableSpeeds.add("S5");
        return availableSpeeds;
    }
}
