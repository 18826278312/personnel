package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.PersonnelService;

@Controller
@RequestMapping("/PersonnelController")
public class PersonnelController {

	@Resource
	private PersonnelService personnelService;
	
	@RequestMapping(value="/listPersonnel")
	@ResponseBody
	public Map<String, Object> listPersonnel(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap.put("list", personnelService.listPersonnel());
			resultMap.put("status", "success");
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("error", "success");
			resultMap.put("info", e.toString());
		}
		return resultMap;
	}
	
	@RequestMapping(value="/personnel")
	public String personnel(){
		return "personnel";
	}
}
