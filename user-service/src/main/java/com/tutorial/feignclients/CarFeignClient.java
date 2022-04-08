package com.tutorial.feignclients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;

import com.tutorial.model.Car;

@FeignClient(name = "car-service", url = "http://localhost:8002", path = "/car")
//@RequestMapping("/car")
public interface CarFeignClient {
	
	@PostMapping()
	Car save(@RequestBody Car car);
	
	@GetMapping("byuser/{userId}")
	List<Car> getCars(@PathVariable("userId") int userId);
	
}
