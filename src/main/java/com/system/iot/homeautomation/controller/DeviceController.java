package com.system.iot.homeautomation.controller;

import com.system.iot.homeautomation.enums.DeviceConfigurationType;
import com.system.iot.homeautomation.exception.DeviceConfigurationNotFoundException;
import com.system.iot.homeautomation.exception.DeviceNotFoundException;
import com.system.iot.homeautomation.model.Device;
import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import com.system.iot.homeautomation.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

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
        Optional.ofNullable(deviceConfiguration)
                .orElseThrow(() -> new DeviceConfigurationNotFoundException());
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
}
