package com.system.iot.homeautomation.repository;

import com.system.iot.homeautomation.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
}
