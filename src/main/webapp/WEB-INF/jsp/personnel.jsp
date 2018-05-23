<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div>
<SELECT id="oneSelect" style="WIDTH:140px;HEIGHT:250px" size=15>
</SELECT>
<SELECT id="oneList" style="WIDTH:140px;HEIGHT:250px" size=15>
</SELECT>
</div>
<script src="../js/jquery-1.11.0.min.js"></script>
<script src="../js/personnel.js"></script>
<script>
var array = [
	{
		"潮南分公司":[
		{
			"市场部":[
			{
				"综合支撑部":["林敏","姚欣","曾素凤"]
			},
			{
				"全业务组":["沈海强"]
			},
			"李旭森"]
		},
		{
			"网络部":["陈楚山","纪达阳"]
		},
		"林凯","郭怀东"]
	},	
	{
		"潮阳分公司":["小明","小红"]
	},
]

//初始化select
window.onload = function(){
	var select = $("#oneSelect");
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
}

$("#oneSelect").on("click",function(){	
	var arr = array;
	//获取选中的option
	var option = $("option:selected",this);
	//获取菜单
	var val = (option.val()).split('\\');
	//获取内容
	var text = option.text();
	var status = between(text);
	//status为+表示展开菜单
	if(status == "+"){
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
	                continue;
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
		var obj = document.getElementById('oneSelect');
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
		console.log("具体人员");
		var select = $("#oneList");
		select.append("<OPTION>" + option.text().trim("　") + "</OPTION>");
	}
})

$("#oneList").on("click",function(){
	$("option:selected",this).remove();
})

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

function shrink(menu,arr){
	var str = menu;
	for(var i=0,l=arr.length;i<l;i++){
		if(isJson(arr[i])){
			str = menu + "\\\\" + Object.keys(arr[i])[0];
			console.log(str);
			$("option[value=" + str + "]:selected").remove();
			shrink(str,arr[i][Object.keys(arr[i])[0]]);
		}else{
			str = menu + "\\\\" + arr[i];
			console.log(str);
			$("option[value=" + str + "]:selected").remove();
		}
	}
}

//递归解析算法
function parseJson(arr){
	//遍历数组
	for(var i=0,l=arr.length;i<l;i++){
		//如果该元素为对象，则表示该元素是一个子部门，继续递归x
		if(isJson(arr[i])){
			for(var key in arr[i]){
				console.log(key+':');
				parseJson(arr[i][key]);
			}
		}
		//如果该元素为字符串，则表示该元素一个员工，则直接log
		else{
			console.log(arr[i]);
		}
	}
}
</script>
</body>
</html>