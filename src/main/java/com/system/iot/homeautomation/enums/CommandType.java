package com.system.iot.homeautomation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandType {

    OnOff(0),
    SetFanSpeed(1),
    SetBrightness(2);

    private final Integer type;
}
