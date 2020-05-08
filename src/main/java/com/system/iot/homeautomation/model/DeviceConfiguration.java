package com.system.iot.homeautomation.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.HashMap;


@Getter
@Builder
@Setter
@Accessors(fluent = true)
@ToString
public class DeviceConfiguration {

        @Id
        private String id;

        private String type;

        private String description;

        @Column(name = "hardware_configuration")
        private HashMap<String, Object> hardwareConfiguration;

        private HashMap<String, Object> parameters;

        @Column(name = "default_attributes")
        private HashMap<String, Object> defaultAttributes;

}
