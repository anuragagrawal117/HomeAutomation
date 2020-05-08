package com.system.iot.homeautomation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.system.iot.homeautomation.enums.DeviceConfigurationType;
import com.system.iot.homeautomation.enums.DeviceStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.HashMap;

@Builder
@Getter
@Setter
@Accessors(fluent = true)
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

    @JsonProperty
    @Id
    private String id;

    @JsonProperty("device_conf_type")
    private DeviceConfigurationType deviceConfType;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty
    private DeviceStatusType status;

    @JsonProperty("connection_id")
    private String connectionId;

    @JsonProperty("user_provided_description")
    private String userProvidedDescription;

    @JsonProperty
    private HashMap<String, Object> attributes;

    @JsonProperty("device_configuration")
    private transient DeviceConfiguration deviceConfiguration;

}
