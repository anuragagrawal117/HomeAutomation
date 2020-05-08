package com.system.iot.homeautomation.repository;

import com.system.iot.homeautomation.model.DeviceConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceConfigurationRepository extends MongoRepository<DeviceConfiguration, String> {

    public DeviceConfiguration findByType(String type);
}
