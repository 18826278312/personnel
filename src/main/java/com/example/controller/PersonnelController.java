package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/PersonnelController")
public class PersonnelController {

	@RequestMapping(value="/listPersonnel")
	@ResponseBody
	public String listPersonnel(){
		return "123";
	}
	
	@RequestMapping(value="/personnel")
	public String personnel(){
		return "personnel";
	}
	
	public static void main(String[] args){
		try {
			List<String> list = new ArrayList<String>();
			List<Object> resultList = new ArrayList<Object>();
			//读取文件
			File file = new File("/Users/zheng/Desktop/collect_addlist_info(1).txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String lineTxt = null;
			//读取文件中的记录并添加到list里
			while ((lineTxt = br.readLine()) != null) {
				list.add(lineTxt);
			}
			//遍历list
			for(int i=0;i<list.size();i++){
				//将文件的每一条记录根据字符"\"进行分解
				String[] menus = list.get(i).split("\\\\");
				//如果分解出三部分，则表示该记录为一级菜单，这里对每一个一级菜单进行处理
				if (menus.length==3) {
					//拿到一级菜单
					String parentMenu = "\\" + menus[1] + "\\";
					//拿到子菜单
					String[] sonMent = menus[2].split(",");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	//递归处理子菜单算法
	public static List<Object> listPerson(String parentMenu,String[] sonMent,List<String> list){
		List<Object> resultList = new ArrayList<Object>();
		//遍历当前一级菜单
		for(int i=0;i<sonMent.length;i++){
			//遍历文件部门列表
			for(int j=0;j<list.size();j++){
				if (list.get(j).equals(sonMent[i])) {
					
				}
			}
		}
		return resultList;
	}
}
