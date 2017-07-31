var IDCard_err =false;
var password_err =false;
var verifyCode_err =false;
var yj_list =null;
var tj_list =null;
var culength =null;
var cufirst =null;

//刷新验证码
function reloadVerifyCode(obj) {
	obj.src = "validateCode.ehr?d=" + new Date();
	$("#verifyCode").val("");
}
$(document).ready(function() {
//	$("#phoneno").blur(function() {
//		checkIDCard()
//	});
	$("#phoneno").click(function() {
		restIDCard()
	});
	$("#psword").click(function() {
		restPassword()
	});
	$("#verifyCode").click(function() {
		restVerifyCode()
	});
});

//后台请求
function doAjax(u,param,callback){
      $.ajax({
            type:'POST',
            url:u,
            data:param,
            dataType : 'json',
			cache : false,
			error : function() {
				alert('服务请求失败！！');
				return;
			},
            success:callback
      });
}

//检查卡号
function checkIDCard(){
	var loginname = $("#loginname").val();
//	var b ;
	//卡号为空
	if(loginname==""||loginname==null){
		alert("账号不能为空!");
		$('#loginname').addClass('highlight2'); 
		IDCard_err = true;
		return false;
	}
//	var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
//	if(!myreg.test(loginname)) 
//	{ 
//	    alert('请输入有效的手机号码！'); 
//	    $('#loginname').addClass('highlight2'); 
//		IDCard_err = true;
//	    return false; 
//	} 
//		b	= isChinaIDCard(phoneno);
//		if(b!=true){
//			alert("账号格式不正确!");
//			$('#phoneno').addClass('highlight2'); 
//			IDCard_err = true;
//			return false;
//		}
	return true;
}

//检查密码
function checkPassword(){
	var password = $('#psword').val();
	//检查为空
	if(password==""||password==null){
		alert("密码不能为空!!");
		$('#psword').addClass('highlight2'); 
		password_err = true;
		return false;
	}
	return true;
}

//卡号重置
function restIDCard(){
	if(IDCard_err == true){
		$('#phoneno').removeClass('highlight2');
		$('#phoneno').val("");
		$('#phoneno_msg').html("");
		IDCard_err = false;
	}
}

//密码重置
function restPassword(){
	if(password_err == true){
		$('#psword').removeClass('highlight2');
		$('#psword').val("");
		$('#password_msg').html("");
		password_err = false;
	}
}

//验证码重置
function restVerifyCode(){
	if(verifyCode_err == true){
		$('#verifyCode').removeClass('highlight2');
		$('#verifyCode').val("");
		$('#verifyCode_msg').html("");
		verifyCode_err = false;
	}
}

//登录
function logon(){
	if(checkIDCard()&&checkPassword()){
		var loginname = $("#loginname").val();
		var password = $('#psword').val();
		var verifyCode = $("#verifyCode").val();
	   	param = {'loginname':loginname,'password':password,'verifyCode':verifyCode};
	   	u='logon.ehr';
	   	doAjax(u,param,deallogin);
	}
}
//登录处理
function deallogin(data, textStatus){
	if (data["code"] == 200) {
		saveUserInfo()
		var rs = 'hdpt/index.html';
		top.location.href = rs;
		return;
	}
	else if(data["code"] == 404){
		alert("验证码不正确或验证码已过期!");
		$('#verificationCode').attr("src","validateCode.ehr?d=" + new Date() ); 
		$('#verifyCode').addClass('highlight2'); 
		 verifyCode_err =true;
	}else{
		alert("您输入的账户名和密码不匹配!");
		$('#psword').addClass('highlight2'); 
		$("#verifyCode").val("");
		$("#psword").val("");
		$('#verificationCode').attr("src","validateCode.ehr?d=" + new Date() ); 
		 password_err =true;
	}
}

//检验列表
function labreportList(data, textStatus) {
		$('#loading').css({
	   	    'display' : 'none'
	   	}); 
		if (data["code"] != 200) {
			alert(153)
			var rs = 'index.html';
			top.location.href = rs;
			return;
		}
			var jsonData = data.body;
			var length = jsonData.length;
			if(length==0){
				$("#mainlist").html("");
				alert("您的检验记录未被找到!!");
				return;
			}
			$("#jybg").hover( function () { $(this).addClass("hover"); }, function () { $(this).removeClass("hover"); });
//			length= 1
			var bz ="";
			var url_int = "gethtml/lab/"+jsonData[0].DCID+".ehr" ;
			var JYRQ = null;
			for (var i = 0; i < length; i++) {
//				bz = bz + '<li><a  href="gethtml/lab/'+jsonData[i].DCID+'.ehr" target="report" >'+jsonData[i].JYRQ+'</a></li>'
				var url = "'gethtml/lab/"+jsonData[i].DCID+".ehr'" ;
				if(jsonData[i].JYRQ!=JYRQ){
					if(JYRQ!=null){
						bz = bz +"</ul></li>";
					}
					JYRQ = jsonData[i].JYRQ;
					bz = bz + '<a class="menu_bg" href="javascript:void(0);" onClick="expandMenu(this);" id="menu'+i+'"><span class="ArrowUp" id="menu'+i+'Arrow"></span>'+JYRQ+'</a>'
					bz = bz + '<ul id="menu'+i+'s" style="display:none"><li><a style="cursor: pointer;" onclick="doload('+url+',null,labreport)" title="'+jsonData[i].JYBGDMC+'">'+jsonData[i].JYBGDMC+'</a></li>'
					//bz = bz + '<li><a style="cursor: pointer;" title="'+jsonData[i].AUTHORORGANIZATION_TEXT+'">'+JYRQ+'</a><ul class="sub-menu">';
					//bz = bz +'<li><a  class ="sublis" style="font-size:11px;cursor: pointer;" onclick="doload('+url+',null,labreport)" title="'+jsonData[i].JYBGDMC+'">'+jsonData[i].JYBGDMC+'</a></li>';
				}
				else{
					bz = bz +'<li><a style="cursor: pointer;" onclick="doload('+url+',null,labreport)" title="'+jsonData[i].JYBGDMC+'">'+jsonData[i].JYBGDMC+'</a></li>';
				}
				
			
			}
			bz = bz +"</ul></li>";
			$("#mainlist").html(bz);
			doAjax(url_int,null,labreport);
			// Store variables
			var accordion_head = $('.accordion > a'),
				accordion_body = $('.accordion > ul > li');
			// Open the first tab on load
			$('.accordion > a> .ArrowUp').first().addClass('ArrowDown');
			accordion_head.first().next().slideDown('normal');
			// Click function
			accordion_body.first().addClass('current');
}

//体检预处理
function precur(){
	if(culength!=null&&culength>0){
		$("#report").css({
	   	    'display' : 'none'
	   	}); 
		$('#loading').css({
	   	    'display' : 'block'
   		}); 
		doAjax('getlist/cu.ehr',null,cureportList)
	}
}

//体检列表
function cureportList(data, textStatus) {
		$('#loading').css({
	   	    'display' : 'none'
	   	}); 
		if (data["code"] != 200) {
			var rs = 'index.html';
			top.location.href = rs;
			return;
		}
			var jsonData = data.body;
			culength = jsonData.length;
			if(culength==0){
				$("#tjbg").css({
			   	    'cursor' : ''
			   	}); 
				$("#tjbg > h4").css({
			   	    'color' : "#C2C2C2"
			   	}); 
				$("#tjbg").attr({
			   	    'active' : 'none'
			   	}); 
				return;
			}
			$("#tjbg").hover( function () { $(this).addClass("hover"); }, function () { $(this).removeClass("hover"); });
			if(cufirst==null){
				cufirst= 1;
				return
			}
//			length= 1
			var bz ="";
			$("#date").html("体检日期")
			var url_int = "gethtml/cu/"+jsonData[0].DCID+".ehr" ;
			var TJRQ = null;
			for (var i = 0; i < culength; i++) {
//				bz = bz + '<li><a  href="gethtml/lab/'+jsonData[i].DCID+'.ehr" target="report" >'+jsonData[i].JYRQ+'</a></li>'
				var url = "'gethtml/cu/"+jsonData[i].DCID+".ehr'" ;

					bz = bz +'<a  class ="menu_bg" style="cursor: pointer;" onclick="doload('+url+',null,labreport)" title="'+jsonData[i].AUTHORORGANIZATION_TEXT+'"><div style="margin-left: 10px">'+jsonData[i].TJRQ+'</div></a>';

			}
			$("#mainlist").html(bz);
			doAjax(url_int,null,labreport);
			// Store variables
			var accordion_head = $('.accordion > .menu_bg');
			accordion_head.first().removeClass();
			accordion_head.first().addClass('menu_bg_1')
			accordion_head.on('click', function(event) {
				// Disable header links
				//event.preventDefault();
				// Show and hide the tabs on click
				if ($(this).attr('class') != 'menu_bg_1'){
					//accordion_head.removeClass('menu_bg_1');
					accordion_head.addClass('menu_bg');
					$(this).removeClass();
					$(this).addClass('menu_bg_1');
				}
			});
}

function doload(u,param,callback) {
	$("#report").css({
   	    'display' : 'none'
   	}); 
	
	$('.accordion > ul > li').on('click', function(event) {
		// Disable header links
		event.preventDefault();
		// Show and hide the tabs on click
		$('.accordion > ul > li').removeClass();
		$(this).addClass('current');
		
	});
	
	doAjax(u,param,callback);
	$('#loading').css({
   	    'display' : 'block'
   	}); 
}

//检验单显示
function labreport(data, textStatus) {
	$('#loading').css({
   	    'display' : 'none'
   	}); 
	var iframe=window.frames['report'];
	
	var jsonData = data.body;
	if (data["code"] != 200) {
//		jsonData ="检验数据异常!无法显示!"
		iframe=$(window.parent.document).find("#report");
		iframe.attr("src","err.htm"); 
		$("#report").css({
	   	    'display' : 'block'
	   	}); 
		return
	}
	$("#report").css({
   	    'display' : 'block'
   	}); 
	iframe.document.open();
	iframe.document.write(jsonData);
	iframe.document.close();
}

//基本信息设置
function setMpiInfo(data, textStatus) {
	var jsonData = data.body;
	var days = new Date();
	var gyear = days.getFullYear();
	$("#name").html(jsonData.personName);
	$("#sex").html(jsonData.sexCode_text);
	$("#age").html(gyear-parseInt(jsonData.birthday.substring(0,4)));
	
	$("#idcard").html(jsonData.idCard.substring(0,4)+"******"+jsonData.idCard.substring(10));
	$("#address").html(jsonData.address);
}

//保存用户信息 
function saveUserInfo() { 
	   if ($("#autologin").attr("checked") == "checked") { 
		   var userName = $("#phoneno").val(); 
		   var loginname = $("#loginname").val(); 
		   var passWord = $("#psword").val(); 
		   $.cookie("autologin", "checked", { expires: 7 }); // 存储一个带7天期限的 cookie 
		   $.cookie("loginname", loginname, { expires: 7 }); // 存储一个带7天期限的 cookie 
		   $.cookie("phoneno", userName, { expires: 7 }); // 存储一个带7天期限的 cookie 
		   $.cookie("psword", passWord, { expires: 7 }); // 存储一个带7天期限的 cookie 
	   } 
	   else { 
		   $.cookie("autologin", "false", { expires: -1 }); 
		   $.cookie("phoneno", '', { expires: -1 }); 
		   $.cookie("psword", '', { expires: -1 }); 
	   } 
}
