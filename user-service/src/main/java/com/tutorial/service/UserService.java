package com.tutorial.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tutorial.entity.User;
import com.tutorial.feignclients.BikeFeignClient;
import com.tutorial.feignclients.CarFeignClient;
import com.tutorial.model.Bike;
import com.tutorial.model.Car;
import com.tutorial.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CarFeignClient carFeignClient;
	
	@Autowired
	BikeFeignClient bikeFeignClient;
	
	public List<User> getAll(){
		return userRepository.findAll();
	}
	
	public User getUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User save(User user) {
		User userNew = userRepository.save(user);
		return userNew;
	}
	
	public List<Car> getCars(int userId){
		List<Car> cars = restTemplate.getForObject("http://localhost:8002/car/byuser/" + userId, List.class);
		return cars;
	}
	
	public List<Bike> getBikes(int userId){
		List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/bike/byuser/" + userId, List.class);
		return bikes;
	}
	
	public Car saveCar(int userId, Car car) {
		car.setUserId(userId);
		Car carNew = carFeignClient.save(car);
		return carNew;
	}
	
	public Bike saveBike(int userId, Bike bike) {
		bike.setUserId(userId);
		Bike bikeNew = bikeFeignClient.save(bike);
		return bikeNew;
	}
	
	public Map<String,Object> getUserAndVehicles(int userId){
		Map<String, Object> result = new HashMap<>();
		User user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			result.put("Mensaje", "No existe el usuario");
			return result;
		}
		result.put("User", user);
		List<Car> cars = carFeignClient.getCars(userId);
		if (cars == null) {
			result.put("Cars", "Ese user no tiene coche");
		}else {
			result.put("Cars", cars);
		}
		List<Bike> bikes = bikeFeignClient.getBikes(userId);
		if (bikes == null) {
			result.put("Bikes", "Ese user no tiene bike");
		}else {
			result.put("Bikes", bikes);
		}
		return result;
	}
}
