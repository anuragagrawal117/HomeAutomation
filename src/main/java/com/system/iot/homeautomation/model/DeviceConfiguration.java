package com.system.iot.homeautomation.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.system.iot.homeautomation.enums.DeviceConfigurationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.HashMap;


@Builder
@Getter
@Setter
@Accessors(fluent = true)
@ToString
public class DeviceConfiguration {

        @JsonProperty
        @Id
        private String id;

        @JsonProperty
        private DeviceConfigurationType type;

        @JsonProperty
        private String description;

        @JsonProperty("hardware_configuration")
        private HashMap<String, Object> hardwareConfiguration;

        @JsonProperty
        private HashMap<String, Object> parameters;

        @JsonProperty("default_attributes")
        private HashMap<String, Object> defaultAttributes;

}
