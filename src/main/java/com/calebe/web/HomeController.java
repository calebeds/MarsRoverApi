package com.calebe.web;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.calebe.dto.HomeDto;
import com.calebe.response.MarsRoverApiResponse;
import com.calebe.service.MarsRoverApiService;


//Controller | mvc = Modern View Controller

@Controller
public class HomeController {
	
	@Autowired
	private MarsRoverApiService roverService;// Springboot way of initializing object without using new Anything()
	
	@GetMapping(value="/")
	public String getHomeView
		(
			ModelMap model, 
			HomeDto homeDto
		) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
		{
			// If request param is empty, then set a default value for marApiRoverData
			if(StringUtils.isEmpty(homeDto.getMarsApiRoverData())) 
				homeDto.setMarsApiRoverData("Opportunity");
			
			// Same thing with marsSol
			if(homeDto.getMarsSol() == null)
				homeDto.setMarsSol(1);
			
			HashMap<String, List<String>> validCameras = (HashMap<String, List<String>>) roverService.getValidCameras();
				
			
			MarsRoverApiResponse roverData =
					roverService.getRoverData(homeDto);
			
			model.put("roverData", roverData);
			model.put("homeDto", homeDto);
			model.put("validCameras", roverService.getValidCameras().get(homeDto.getMarsApiRoverData()));//Get the valid cameras from the rover that have already been chosen
			
			return "index";
		}
	
	@PostMapping(value="/")
	public String postHomeView(HomeDto homeDto) {
		System.out.println(homeDto);
		return "redirect:/";
	}
	
}
