package com.system.iot.homeautomation.service;

import com.system.iot.homeautomation.enums.CommandType;
import com.system.iot.homeautomation.model.Command;
import com.system.iot.homeautomation.model.Device;
import com.system.iot.homeautomation.model.DeviceConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public interface DeviceBehavior {

    public DeviceConfiguration getInstance();

    public ArrayList<String> executeCommand(Device device, Command command);
}
