package com.example.service.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.reflect.Field;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
		for(int i=0;i<resultList.size();i++){
			//修改下级菜单里部门和人员的显示顺序
			Map<String, List<Object>> map = (Map<String, List<Object>>) JSONObject.toJSON((resultList.get(i)));
			String oneKey = getMapOneKey(map);
			List<Object> menuList = map.get(oneKey);
			map.put(oneKey, changeOrder(menuList));
			resultList.set(i, map);
		}
		//提取要显示的一级菜单
		resultList = selectMenu(resultList);
		return resultList;
	}

	/**
	 * 调整部门子菜单的显示顺序
	 * @param list
	 * @return
	 */
	public List<Object> changeOrder(List<Object> menuList) {
		List<Object> list = new ArrayList<Object>();
		for (int i = menuList.size()-1; i >= 0; i--) {
			if (menuList.get(i) instanceof String) {
				list.add(menuList.get(i));
			}else{
				Map<String, List<Object>> map = (Map<String, List<Object>>) JSONObject.toJSON((menuList.get(i)));
				String oneKey = getMapOneKey(map);
				Map<String, List<Object>> resultMap = new HashMap<String, List<Object>>();
				resultMap.put(oneKey, changeOrder(map.get(oneKey)));
				list.add(resultMap);
			}
		}
		return list;
	}
	
	/**
	 * 获取map的第一个key
	 * @param map
	 * @return
	 */
	public String getMapOneKey(Map<String, List<Object>> map){
		String oneKey = null;
		for(String key : map.keySet()){
			oneKey = key;
			if (oneKey != null) {
				break;
			}
		}
		return oneKey;
	}
	
	/**
	 * 提取要显示的一级菜单
	 * @param resultList
	 * @return
	 */
	public List<Object> selectMenu(List<Object> resultList){
		String[] menus = {"市场部","业务支持中心"};
		List<Object> list = new ArrayList<Object>();
		for(int j=0;j<menus.length;j++){
			for(int i=0;i<resultList.size();i++){
				Map<String, List<Object>> map = (Map<String, List<Object>>) JSONObject.toJSON((resultList.get(i)));
				String oneKey = getMapOneKey(map);
				if (oneKey.equals(menus[j])) {
					list.add(resultList.get(i));
					break;
				}
			}
		}
		return list;
	}
}
