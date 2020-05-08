package com.system.iot.homeautomation.repository;

import com.system.iot.homeautomation.enums.DeviceConfigurationType;
import com.system.iot.homeautomation.model.DeviceConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DeviceConfigurationRepository extends MongoRepository<DeviceConfiguration, String> {

    public DeviceConfiguration findByType(DeviceConfigurationType type);
}
