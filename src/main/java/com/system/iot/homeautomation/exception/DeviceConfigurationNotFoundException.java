package com.system.iot.homeautomation.exception;

public class DeviceConfigurationNotFoundException extends RuntimeException {

    public DeviceConfigurationNotFoundException(){
        super("device configuration not found!");
    }
}
