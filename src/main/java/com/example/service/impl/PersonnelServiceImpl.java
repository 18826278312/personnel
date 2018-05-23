package com.example.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.service.PersonnelService;

@Service
public class PersonnelServiceImpl implements PersonnelService{

	@Override
	public List<Object> recursiveMenu(String parentMenu, String[] sonMent, List<String> list, int length) {
		List<Object> resultList = new ArrayList<Object>();
		//遍历菜单
		for(int i=0;i<sonMent.length;i++){
			Boolean menuStatus = true;
			//遍历人员名单list
			for(int j=0;j<list.size();j++){
				//将文件的每一条记录根据字符"\"进行分解
				String[] menus = list.get(j).split("\\\\");
				//若当前菜单包涵于当前list元素中，则表示该菜单为部门，需要进一步递归处理
				if (menus.length == length + 1 && list.get(j).indexOf(parentMenu + sonMent[i] + "\\") != -1) {
					Map<String, List<Object>> map = new HashMap<String, List<Object>>();
					//拿到当前菜单
					String currentMenu = parentMenu + sonMent[i] + "\\";
					//拿到子菜单
					String[] currentSonMent = menus[length].split(",");
					//递归处理子菜单
					map.put(sonMent[i], recursiveMenu(currentMenu, currentSonMent, list, length+1));
					resultList.add(map);
					menuStatus = false;
					break;
				}
			}
			if (menuStatus) {
				resultList.add(sonMent[i]);
			}
		}
		return resultList;
	}

	@Override
	public List<Object> listPersonnel() throws Exception{
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
				map.put(menus[1], recursiveMenu(parentMenu, sonMent, list, menus.length));
				resultList.add(map);
			}
		}
		return resultList;
	}

}
