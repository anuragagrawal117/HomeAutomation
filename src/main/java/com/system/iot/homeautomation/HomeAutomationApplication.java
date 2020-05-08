package com.system.iot.homeautomation;

import com.system.iot.homeautomation.model.DeviceConfiguration;
import com.system.iot.homeautomation.repository.DeviceConfigurationRepository;
import com.system.iot.homeautomation.service.DeviceConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class HomeAutomationApplication implements CommandLineRunner {

	@Autowired
	private DeviceConfigurationService deviceConfigurationService;

	@Autowired
	private DeviceConfigurationRepository deviceConfigurationRepository;

	public static void main(String[] args) {

		SpringApplication.run(HomeAutomationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deviceConfigurationService.saveAllDeviceConfigurations();

		System.out.println(deviceConfigurationRepository.findByType("Fan_SCC_492134"));
		System.out.println(deviceConfigurationRepository.findByType("Light_AAA_hg11"));
	}
}
