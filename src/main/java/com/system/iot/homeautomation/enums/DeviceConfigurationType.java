package com.system.iot.homeautomation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceConfigurationType {

    Light_AAA_hg11(1),

    Fan_SCC_492134(2);

    private Integer type;
}
