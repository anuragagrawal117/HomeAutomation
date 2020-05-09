package com.system.iot.homeautomation.exception;

public class DeviceOfflineException extends RuntimeException {

    public DeviceOfflineException() { super("device is offline!"); }
}
