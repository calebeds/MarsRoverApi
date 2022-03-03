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
import org.springframework.web.bind.annotation.ResponseBody;

import com.calebe.dto.HomeDto;
import com.calebe.response.MarsRoverApiResponse;
import com.calebe.respository.PreferencesRepository;
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
			Long userId,
			Boolean createUser
		) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
		{
		
			HomeDto homeDto = createDefaultHomeDto(userId);
			
			if (Boolean.TRUE.equals(createUser) && userId == null) {
				//Putting the default values inside the if
			      homeDto = roverService.save(homeDto);// Creates a row in the database
			} else {
				//Getting the homeDto from the actual database
			      homeDto = roverService.findByUserId(userId);  
			      if (homeDto == null) {//This will check if for some reason there is no row on the database
			        homeDto = createDefaultHomeDto(userId);
			      }
			}
			
			MarsRoverApiResponse roverData =
					roverService.getRoverData(homeDto);
			
			model.put("roverData", roverData);
			model.put("homeDto", homeDto);
			model.put("validCameras", roverService.getValidCameras().get(homeDto.getMarsApiRoverData()));//Get the valid cameras from the rover that have already been chosen
			
			
			if(!Boolean.TRUE.equals(homeDto.getRememberPreferences()) && userId != null) {
				HomeDto defaultHomeDto = createDefaultHomeDto(userId);
				roverService.save(defaultHomeDto);
			}
			
			return "index";
		}
	
	
	@GetMapping("/savedPreferences")
	@ResponseBody
	public HomeDto getSavedPreferences(Long userId) {
		if(userId != null)
			return roverService.findByUserId(userId);
		else
			return createDefaultHomeDto(userId);
	}

	private HomeDto createDefaultHomeDto(Long userId) {
		HomeDto homeDto = new HomeDto();//Declaring it
		homeDto.setMarsApiRoverData("Opportunity");
		homeDto.setMarsSol(1);
		homeDto.setUserId(userId);
		return homeDto;
	}
	
	@PostMapping(value="/")
	public String postHomeView(HomeDto homeDto) {
		homeDto = roverService.save(homeDto);//Saves on the database and returns the values saved
		System.out.println(homeDto);
		return "redirect:/?userId=" + homeDto.getUserId();
	}
	
}
