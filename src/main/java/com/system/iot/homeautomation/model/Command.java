package com.system.iot.homeautomation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.system.iot.homeautomation.enums.CommandType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Builder
@Getter
@Setter
@Accessors(fluent = true)
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Command {

    @JsonProperty
    private CommandType type;

    @JsonProperty
    private HashMap<String, Object> params;
}
