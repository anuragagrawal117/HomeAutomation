package com.system.iot.homeautomation.service;

import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ServiceLoader;
import java.util.Set;

@Service
public class DeviceConfigurationService {

    @Autowired
    private DeviceConfigurationRepository deviceConfigurationRepository;

    public void saveAllDeviceConfigurations(){
        Reflections reflections = new Reflections("com.system.iot.homeautomation.service");
        Set<Class<? extends DeviceBehavior>> deviceBehaviors = reflections.getSubTypesOf(DeviceBehavior.class);

        for (Class deviceBehavior : deviceBehaviors) {
            try{
                DeviceConfiguration deviceConfiguration = ((DeviceBehavior)deviceBehavior.newInstance()).getInstance();
                DeviceConfiguration deviceConfigurationDb = deviceConfigurationRepository.findByType(deviceConfiguration.type());
                if(deviceConfigurationDb == null){
                    deviceConfigurationDb = deviceConfiguration;
                }else{
                    deviceConfigurationDb.description(deviceConfiguration.description())
                            .hardwareConfiguration(deviceConfiguration.hardwareConfiguration())
                            .parameters(deviceConfiguration.parameters())
                            .defaultAttributes(deviceConfiguration.defaultAttributes());
                }
                deviceConfigurationRepository.save(deviceConfigurationDb);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
