package com.system.iot.homeautomation.model;

import com.system.iot.homeautomation.enums.DeviceStatusType;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.HashMap;

@Getter
public class Device {

    @Id
    private String id;

    @Column(name = "device_conf_id")
    private String deviceConfId;

    @Column(name = "user_id")
    private String userId;

    private DeviceStatusType status;

    @Column(name = "connection_id")
    private String connectionId;

    @Column(name = "user_provided_description")
    private String userProvidedDescription;

    private HashMap<String, Object> attributes;
}
