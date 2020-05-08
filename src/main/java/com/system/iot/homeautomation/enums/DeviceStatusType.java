package com.system.iot.homeautomation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceStatusType {

    OFFLINE(0),

    ONLINE(1);

    private Integer type;
}
