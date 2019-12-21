
package com.fantasticfour.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fantasticfour.entity.LimitsConfig;

@RestController
public class LimitsServiceController {

	@Autowired
	Configuration configuration;
	
	@GetMapping("/limits")
	public LimitsConfig getConfig() {
		return new LimitsConfig(configuration.getMinimum(),configuration.getMaximum());
	}
}
