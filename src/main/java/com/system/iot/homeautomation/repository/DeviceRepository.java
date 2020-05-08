package com.system.iot.homeautomation.repository;

import com.system.iot.homeautomation.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DeviceRepository extends MongoRepository<Device, String> {
}
