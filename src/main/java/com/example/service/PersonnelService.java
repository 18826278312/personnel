package com.example.service;

import java.util.List;

public interface PersonnelService {
	
	/**
	 * 递归处理子菜单
	 * @param parentMenu
	 * @param sonMent
	 * @param list
	 * @param length
	 * @return
	 */
	List<Object> listPerson(String parentMenu,String[] sonMent,List<String> list,int length);
}
