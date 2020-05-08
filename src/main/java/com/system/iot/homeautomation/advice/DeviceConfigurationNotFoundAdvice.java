package com.system.iot.homeautomation.advice;

import com.system.iot.homeautomation.exception.DeviceConfigurationNotFoundException;
import com.system.iot.homeautomation.exception.DeviceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DeviceConfigurationNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(DeviceConfigurationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String deviceConfNotFoundHandler(DeviceConfigurationNotFoundException ex) {
        return ex.getMessage();
    }
}
