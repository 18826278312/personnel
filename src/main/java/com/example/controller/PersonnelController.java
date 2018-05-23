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
		System.out.println("listPersonnel");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		List<Object> resultList = new ArrayList<Object>();
		try {
			//读取文件
			File file = new File("/Users/zheng/Desktop/collect_addlist_info(1).txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String lineTxt = null;
			//读取文件中的记录并添加到list里
			while ((lineTxt = br.readLine()) != null) {
				list.add(lineTxt);
			}
			//遍历人员名单list
			for(int i=0;i<list.size();i++){
				//将文件的每一条记录根据字符"\"进行分解
				String[] menus = list.get(i).split("\\\\");
				//如果分解出三部分，则表示该记录为一级菜单，这里对每一个一级菜单进行处理
				if (menus.length==3) {
					//拿到一级菜单
					String parentMenu = "\\" + menus[1] + "\\";
					//拿到子菜单
					String[] sonMent = menus[2].split(",");
					Map<String, List<Object>> map = new HashMap<String, List<Object>>();
					map.put(menus[1], personnelService.listPerson(parentMenu, sonMent, list, menus.length));
					resultList.add(map);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		resultMap.put("list", resultList);
		return resultMap;
	}
	
	@RequestMapping(value="/personnel")
	public String personnel(){
		return "personnel";
	}
}
