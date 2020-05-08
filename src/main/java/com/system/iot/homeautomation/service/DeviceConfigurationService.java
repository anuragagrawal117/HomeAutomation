package com.system.iot.homeautomation.service;

import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ServiceLoader;
import java.util.Set;

@Service
@Slf4j
public class DeviceConfigurationService implements CommandLineRunner {

    @Autowired
    private DeviceConfigurationRepository deviceConfigurationRepository;

    @Autowired
    private DeviceConfigurationService deviceConfigurationService;

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
                log.info("loaded::" + deviceConfigurationRepository.save(deviceConfigurationDb));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("loading device configurations..");
        deviceConfigurationService.saveAllDeviceConfigurations();

    }

}
