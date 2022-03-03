package com.calebe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.calebe.dto.HomeDto;
import com.calebe.response.MarsPhoto;
import com.calebe.response.MarsRoverApiResponse;
import com.calebe.respository.PreferencesRepository;

@Service
public class MarsRoverApiService {
	
	private static final String  API_KEY = "4AaDPr1wf1Y4dSxM6dtbhbkQo1H4KrBhNWUi0KwX";
	
	private Map<String, List<String>> validCameras = new HashMap<>();
	
	public MarsRoverApiService() {//Constructor will set valid cameras 
		validCameras.put("Curiosity", Arrays.asList("FHAZ", "RHAZ", "MAST", 
				"CHEMCAM", "MAHLI", "MARDI", "NAVCAM"));//Curiosity support this cameras
		
		validCameras.put("Opportunity", Arrays.asList("FHAZ", "RHAZ", "NAVCAM",
				"PANCAM", "MINITES"));//Opportunity likewise
		
		validCameras.put("Spirit", Arrays.asList("FHAZ", "RHAZ", "NAVCAM",
				"PANCAM", "MINITES"));//Spirit likewise
	}
	
	@Autowired
	private PreferencesRepository preferencesRepo; //This is a instance from the repository that will be autowired
	
	public MarsRoverApiResponse getRoverData(HomeDto homeDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		RestTemplate rt = new RestTemplate(); // Rest Template
		
		List<String> apiUrlEndpoints = getUrlApiEndpoints(homeDto);//Getting all the urls for the request
		List<MarsPhoto> photos = new ArrayList<>();//List for photos
		
		MarsRoverApiResponse response = new MarsRoverApiResponse();// Response that will be returned
		
		apiUrlEndpoints.stream()
			.forEach(url -> { //Getting the photos from each url and adding it to photos List
				MarsRoverApiResponse apiResponse = rt.getForObject(url, MarsRoverApiResponse.class);
				photos.addAll(apiResponse.getPhotos());
			});
		
		response.setPhotos(photos);//Adding all photos to this particular response
		
		return response;// Returning a MarsRoverApiResponse/Class created
	}

	public List<String> getUrlApiEndpoints (HomeDto homeDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		List<String> urls = new ArrayList<>();
		
		Method[] methods = homeDto.getClass().getMethods(); // Get all methods from HomeDto of this particular instance
		
		for(Method method: methods) {
			if(method.getName().indexOf("getCamera") > -1 //Check if the method is getCameraSomething
					&& Boolean.TRUE.equals(method.invoke(homeDto))) { //Invoke the method and check if is true, that is, if the checkbox is checked
				
				String cameraName = method.getName().split("getCamera")[1].toUpperCase();//Get the name after the getCamer then upperCase it
				
				if(validCameras.get(homeDto.getMarsApiRoverData()).contains(cameraName)) { //If the Rover contains this particular camera, move on
					urls.add("https://api.nasa.gov/mars-photos/api/v1/rovers/"
							+ homeDto.getMarsApiRoverData()  //Rover type
							+ "/photos?sol=" 
							+ homeDto.getMarsSol() //Sol/ mars day
							+ "&api_key=" + API_KEY
							+ "&camera=" + cameraName);
				}
			}
		}
		
		return urls;
	}
	
	public Map<String, List<String>> getValidCameras() {
		return validCameras;
	}

	public HomeDto save(HomeDto homeDto) {
		return preferencesRepo.save(homeDto);
	}

	public HomeDto findByUserId(Long userId) {
		return preferencesRepo.findByUserId(userId);
	}
	
}	
