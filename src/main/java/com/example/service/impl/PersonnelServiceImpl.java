package com.example.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.service.PersonnelService;

@Service
public class PersonnelServiceImpl implements PersonnelService{

	@Override
	public List<Object> listPerson(String parentMenu, String[] sonMent, List<String> list, int length) {
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
					map.put(sonMent[i], listPerson(currentMenu, currentSonMent, list, length+1));
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

}
