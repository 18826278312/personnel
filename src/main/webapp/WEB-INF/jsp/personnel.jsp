<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<input type="button" style="WIDTH:140px;HEIGHT:20px" value="添加" onclick="addList('Process')">
<input type="button" style="WIDTH:140px;HEIGHT:20px" value="清空" onclick="emptyList('Process')">
<div>
	<SELECT id="ProcessSelect" style="WIDTH:140px;HEIGHT:250px" size=15 onclick="clickSelect(this,'Process')">
	</SELECT>
	<SELECT id="ProcessList" style="WIDTH:140px;HEIGHT:250px" size=15 onclick="clickList(this)">
	</SELECT>
</div>
<script src="../js/jquery-1.11.0.min.js"></script>
<script src="../js/personnel.js"></script>
<script>
var array = [];
var ProcessCurrent = null;
var ReadCurrent = null;
//初始化select
window.onload = function(){
	var select = $("#ProcessSelect");
 	$.post("/PersonnelController/listPersonnel",{},function(data){
 		if(data.status == "success"){
 			array = data.list;
 			for(var i=0,l=array.length;i<l;i++){
 				//如果该元素为对象，则表示该元素是一个部门
 				if(isJson(array[i])){
 					select.append("<OPTION value='\\" + Object.keys(array[i])[0] + "'>[+]" + Object.keys(array[i])[0] + "</OPTION>");
 				}
 				//如果该元素为字符串，则表示该元素一个员工
 				else{
 					select.append("<OPTION value='\\" + array[i] + "'>" + array[i] + "</OPTION>");
 				}
 			}
 		}else {
 			alert(data.info);
 		}
	})
}

function emptyList(name){
	$("#"+name+"List").empty();
}

function addList(name){
	var currentOption = null;
	if(name=="Process"){
		currentOption = ProcessCurrent;
	}else if(name=="Read"){
		currentOption = ReadCurrent;
	}
	if(currentOption!=null){
		//获取选中的option
		var option = $("option:selected",currentOption);
		//获取菜单
		var val = (option.val()).split('\\');
		var arr = array;
		//循环当前点击option的多级菜单
		for(var i = 1;i < val.length;i++){
			//循环人员名单数组
			for(var j=0;j < arr.length;j++){
				//如果当前元素与这一级菜单匹配，则将该元素赋值给arr并进入下一次循环
				if(isJson(arr[j]) && Object.keys(arr[j])[0] == val[i]){
					arr = arr[j][Object.keys(arr[j])[0]];
	                break;
				}
			}
		}
		
		var status = false;
		//循环菜单内容并填充
		for(var x = arr.length-1;x >= 0;x--){
			status = false;
			if(!isJson(arr[x])){
				//遍历所有option 
			    $("#" + name + "List option").each(function(){ 
			    	if($(this).text() == arr[x]){
			    		status = true;
			    	}
			    });
			    if(!status){
			    	$("#" + name +"List").append("<OPTION>" + arr[x] + "</OPTION>");
			    }
			}
		}
	}
}

function clickSelect(current,name){
	var arr = array;
	//获取选中的option
	var option = $("option:selected",current);
	//获取菜单
	var val = (option.val()).split('\\');
	//获取内容
	var text = option.text();
	var status = between(text);
	//status为+表示展开菜单
	if(status == "+"){
		if(name=="Process"){
			ProcessCurrent = current;
		}else if(name=="Read"){
			ReadCurrent = current;
		}
		console.log("展开菜单");
		var space = "";
		//循环当前点击option的多级菜单
		for(var i = 1;i < val.length;i++){
			//每一级菜单都做一次缩进
			space = space + "　";
			//循环人员名单数组
			for(var j=0;j < arr.length;j++){
				//如果当前元素与这一级菜单匹配，则将该元素赋值给arr并进入下一次循环
				if(isJson(arr[j]) && Object.keys(arr[j])[0] == val[i]){
					arr = arr[j][Object.keys(arr[j])[0]];
	                break;
				}
			}
		}
		//循环菜单内容并填充
		for(var x = 0;x < arr.length;x++){
			if(isJson(arr[x])){
				option.after("<OPTION value='"+ option.val() +"\\" + Object.keys(arr[x])[0] + "'>" + space + "[+]" + Object.keys(arr[x])[0] + "</OPTION>" )
			}else{
				option.after("<OPTION value='"+ option.val() +"\\" 
						+ arr[x] + "'>" + space + arr[x] + "</OPTION>" )
			}
		}
		//将菜单前的[+]改为[-]
		option.text(text.replace('[+]', '[-]'));
	}
	//status为-表示收缩菜单
	else if(status == "-"){
		console.log("收缩菜单");
		//将菜单前的[-]改为[+]	
		option.text(text.replace('[-]', '[+]'));
		//获取当前select的js对象(jq用索引删除option有问题)
		var obj = document.getElementById('ProcessSelect');
		//获取点击菜单的索引
		var index = option.index();
		//获取点击菜单的value
		var value = option.val();
		while(true){
			//如果下一个菜单包涵点击菜单，且包涵的位置为0，则删除该菜单
			if(option.next().val().indexOf(value) == 0){
				obj.options.remove(index+1);
			}else{
				break;
			}
		}
	}
	//status为空串表示点击的是具体人员
	else if(status == ""){
		if(name=="Process"){
			ProcessCurrent = null;
		}else if(name=="Read"){
			ReadCurrent = null;
		}
		currentOption = null;
		console.log("具体人员");
		var text = option.text().trim("　");
		var status = false;
		//遍历所有option 
	    $("#ProcessList option").each(function(){ 
	    	if($(this).text() == text){
	    		status = true;
	    	}
	    });
		if(!status){
			$("#ProcessList").append("<OPTION>" + text + "</OPTION>");
		}
	}
}

function clickList(current){
	$("option:selected",current).remove();
}

//判断该参数是否为对象
function isJson(str) {
	if(typeof(str)=="object"){
		return true;
	}else{
		false;
	}
    
}

//截取字符[ ]中间的值
function between(text){
	try{
		return text.match(/\[(\S*)\]/)[1];
	}catch(err){
		return "";
	}
}
</script>
</body>
</html>