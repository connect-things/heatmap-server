package com.francisco.heatmap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heatmap")
public class HeatmapRest {

	@Autowired
	private Subscriber sub;
	
	@GetMapping
	public Map<String, Object> get(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", sub.getDataTemp().values());
		return result;
	}
	
}
