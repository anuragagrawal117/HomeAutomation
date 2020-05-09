package com.system.iot.homeautomation.controller;

import com.system.iot.homeautomation.enums.CommandType;
import com.system.iot.homeautomation.exception.DeviceConfigurationNotFoundException;
import com.system.iot.homeautomation.exception.DeviceNotFoundException;
import com.system.iot.homeautomation.model.Command;
import com.system.iot.homeautomation.model.Device;
import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import com.system.iot.homeautomation.repository.DeviceRepository;
import com.system.iot.homeautomation.service.DeviceBehavior;
import com.system.iot.homeautomation.service.DeviceBehaviorImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceConfigurationRepository deviceConfRepository;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    // Aggregate root

    @GetMapping("/devices")
    CollectionModel<EntityModel<Device>> all() {
        List<EntityModel<Device>> devices =  deviceRepository.findAll().stream()
                .map(device -> new EntityModel<>(device,
                        linkTo(methodOn(DeviceController.class).one(device.id())).withSelfRel(),
                        linkTo(methodOn(DeviceController.class).all()).withRel("devices")))
                .collect(Collectors.toList());

        return new CollectionModel<>(devices,
                linkTo(methodOn(DeviceController.class).all()).withSelfRel());
    }

    @PostMapping("/devices")
    Device newDevice(@RequestBody Device newDevice) throws DeviceConfigurationNotFoundException {
        DeviceConfiguration deviceConfiguration = deviceConfRepository.findByType(newDevice.deviceConfType());
        Optional.ofNullable(deviceConfiguration)
                .orElseThrow(() -> new DeviceConfigurationNotFoundException());
        newDevice.attributes(deviceConfiguration.defaultAttributes());
        return deviceRepository.save(newDevice);
    }

    // Single item

    @GetMapping("/devices/{id}")
    EntityModel<Device> one(@PathVariable String id) throws DeviceNotFoundException {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        DeviceConfiguration deviceConfiguration = deviceConfRepository.findByType(device.deviceConfType());
        device.deviceConfiguration(deviceConfiguration);

        return new EntityModel<>(device,
                linkTo(methodOn(DeviceController.class).one(id)).withSelfRel(),
                linkTo(methodOn(DeviceController.class).all()).withRel("devices"));
    }

    @PutMapping("/devices/{id}")
    Device replaceDevice(@RequestBody Device newDevice, @PathVariable String id) throws DeviceNotFoundException {

        return deviceRepository.findById(id)
                .map(device -> {
                    device.userId(newDevice.userId()).connectionId(newDevice.connectionId())
                            .userProvidedDescription(newDevice.userProvidedDescription())
                            .status(newDevice.status());
                    return deviceRepository.save(device);
                })
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    @DeleteMapping("/devices/{id}")
    void deleteDevice(@PathVariable String id) {
        deviceRepository.deleteById(id);
    }

    @PostMapping("/devices/{id}/execute")
    public ArrayList<String> executeCommand(@PathVariable String id, @RequestBody Command command) throws Exception{
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        DeviceBehavior deviceBehavior = (DeviceBehavior)Class.forName("com.system.iot.homeautomation.service.DeviceBehaviorImpl." +
                device.deviceConfType().name()).newInstance();
        autowireCapableBeanFactory.autowireBean(deviceBehavior);
        return deviceBehavior.executeCommand(device, command);
    }
}
