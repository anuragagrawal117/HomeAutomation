package com.system.iot.homeautomation.exception;

public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(String id) {
        super("Could not find device with id " + id);
    }
}
