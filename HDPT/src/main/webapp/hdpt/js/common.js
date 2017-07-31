var ehrid = Request("uuid")
//sessionId
var uuid =  "UUID";
var mpiid = uuid.split("-")[1];
var sessionid= uuid.split("-")[0];
//var host ="http://127.0.0.1:4444/ehrview"
var host ="http://172.16.101.202:8080/ehrview"
//var host ="http://localhost:8081/ehrview"
var domain = "ehr";

//弹出窗口
function openwin(url){
	parent.$.layer({//在父窗口弹层
	    type : 2,
	    title : '<font color="red">本站提供的市民健康信息仅供参考，不具法律效力</font>',
	    iframe : {src : url,scrolling: 'auto'},
	    maxmin: false,
	    shadeClose: true,
	    area : ['900px' , '450px'],
	    offset : [($(top.window).height() - 450) * 0.5 +'px',($(top.window).width()-800) * 0.5+'px']
	});
}	
function openwin1(url,dw,dh){
	parent.$.layer({//在父窗口弹层
	    type : 2,
	    title : '<font color="red">本站提供的市民健康信息仅供参考，不具法律效力</font>',
	    iframe : {src : url,scrolling: 'no'},
	    maxmin: false,
	    shadeClose: true,
	    area : ['1050'+'px' , '580'+'px'],
	    offset : [($(top.window).height() - 580)/2 +'px',($(top.window).width() - 1050)/2 +'px']
	});
}	

function mainChange(tab) {
//	alert(tab)
//	document.getElementById("main").src = tab
//	return false; 
	$('.current').removeClass('current');
	$(tab).addClass('current');
}

function clear_active(tab) {
	$('.current').removeClass('current');
//	$(tab).addClass('current');
}

//后台请求
function doAjax(u, param, dataType, callback) {
	$.ajax({
		type : 'POST',
		url : u,
		data : param,
		dataType : dataType,
		cache : false,
		error : function() {
			alert('服务请求失败！！');
			return;
		},
		success : callback
	});
}

//后台请求
function doGetAjax(u, param, dataType, callback) {
	$.ajax({
		type : 'get',
		url : u,
		data : param,
		dataType : dataType,
		cache : false,
		error : function() {
			alert('服务请求失败！！');
			return;
		},
		success : callback
	});
}

//数组去重
function arrChange(arrDatas){
	var res=[]
	var has={}
	for(var i=0;i<arrDatas.length;i++){
		var key= typeof(arrDatas[i])+arrDatas[i]
		if(has[key]!=1){
			res[i]=arrDatas[i]
			has[key]=1
		}
	}
	return res
}

//获得地址栏参数
function Request(strName){  
	var strHref = location.href; 
	var intPos = strHref.indexOf("?");  
	var strRight = strHref.substr(intPos + 1);  
	var arrTmp = strRight.split("&");  
	for(var i = 0; i < arrTmp.length; i++) {  
	var arrTemp = arrTmp[i].split("=");  
	if(arrTemp[0].toUpperCase() == strName.toUpperCase()) return decodeURI(arrTemp[1]);  
	}  
	return "";  
}  

//字符组转xml
function createXml(str){ 
	if(document.all){ 
	var xmlDom=new ActiveXObject("Microsoft.XMLDOM") 
	xmlDom.loadXML(str) 
		return xmlDom 
	} 
	else 
	return new DOMParser().parseFromString(str, "text/xml") 
}

Date.prototype.Format = function(fmt) 
{ //author: meizz 
  var o = { 
    "M+" : this.getMonth()+1,                 //月份 
    "d+" : this.getDate(),                    //日 
    "h+" : this.getHours(),                   //小时 
    "m+" : this.getMinutes(),                 //分 
    "s+" : this.getSeconds(),                 //秒 
    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
    "S"  : this.getMilliseconds()             //毫秒 
  }; 
  if(/(y+)/.test(fmt)) 
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o) 
    if(new RegExp("("+ k +")").test(fmt)) 
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
  return fmt; 
}

//报告单显示
function htmreport(data, iframeId) {
	$('#info_loading').css({
   	    'display' : 'block'
   	}); 
	var jsonDatas = data.body;
	var iframe=window.frames[iframeId];
	iframe.document.open();
	iframe.document.write(jsonDatas);
	iframe.document.close();
	$('#info_loading').css({
   	    'display' : 'none'
   	}); 
}

function opendiv(dcid,report_htm,iframeId){
	var url = host + '/'+report_htm+'/getHtmlDocument?vk=UUID&dcId=' +  dcid
	param = {
		'url' : url
	};
	doAjax("getdata.px", param, "json", function(data, textStatus) {
		htmreport(data,iframeId)
	});
}

var html
//列表标题
function createTR(da,db,dc,dd,de){
	$("#query").html("")
	html='<thead><tr id="aa">'
	for(var i=0;i<arguments.length;i++){
		html+='<td class="current" align="center"><strong>'+arguments[i]+'</strong></td>'}
	html+='</tr></thead>'
	return html
} 
//列表内容
function createBTR(da,db,dc,dd,de){
	if(da==null) da=' '
	if(db==null) db=' '
	if(dc==null) dc=' '
	if(dd==null) dd=' '
	if(de==null) de=' '
	html+='<tr>'
	for(var i=0;i<arguments.length;i++){
		html+='<td class="jkzb" align="center" bgcolor="#FFFFFF">'+arguments[i]+'</td>'}
	html+='</tr>'
return html
} 

//显示模板
function templateView(url){
	$("#report").html("");
	$("#loading").css({
   	    'display' : 'block'
   	}); 
	var param = {'url' : url};
	doAjax("getdata.px", param, "json", function(data,textStatus) {
		var jsonData = data.body;
		if (data["code"] != 200) {
			var iframe=$(window.document).find("#report");
			iframe.attr("src","err.htm"); 
			$("#loading").css({
		   	    'display' : 'none'
		   	});
			return
		}
		var iframe=window.frames["report"];
		iframe.document.open();
		iframe.document.write(jsonData);
		iframe.document.close();
		$("#loading").css({
	   	    'display' : 'none'
	   	}); 
	});
}

var numArry={"zsNum":null,"ssNum":null,"jcNum":null,"jyNum":null,"cfNum":null,
		 "baIptNum":null,"lyIptNum":null,"ssIptNum":null,"cyIptNum":null,"yzIptNum":null,"jyIptNum":null,"jcIptNum":null};
function tmplNumPop(moduleName,moduleId){
	var moduleNum="#"+moduleId;
	if(numArry[moduleId]!=null){
		$(moduleNum).html(numArry[moduleId]);
		return;
	}
	var searchentryName=moduleName;
	var url = host + searchentryName+ '?vk=UUID&visitId='+searchKey+'&authorOrganization='+Authororganization;
	var param = {'url' : url};
	doAjax("getdata.px", param, "json", function(data,textStatus) {
		if(data.body && data.body.length>0){
			$(moduleNum).html(data.body.length);
		}else{
			$(moduleNum).html("0");
		}
		numArry[moduleId]=$(moduleNum).html();
	});
}

function numArryClr(){
	for(var numId in numArry){
		numArry[numId]=null;
	}
}
//Trim() , Ltrim() , RTrim()   
String.prototype.Trim = function(){    
	return this.replace(/(^\s*)|(\s*$)/g, "");    
}    
String.prototype.LTrim = function(){    
	return this.replace(/(^\s*)/g, "");    
}    
String.prototype.RTrim = function(){    
	return this.replace(/(\s*$)/g, "");    
}

function newDate(str) { 
	str = str.split('-'); 
	var date = new Date(); 
	date.setUTCFullYear(str[0], str[1] - 1, str[2]); 
	date.setUTCHours(0, 0, 0, 0); 
	return date; 
} 

//过长隐藏
function subName(str)
{
	num=11
	if(str!=null&&str.length > num)
	{
		str = str.substr(0,num - 2);
		str = str + "...";
	}
	return str;
}
