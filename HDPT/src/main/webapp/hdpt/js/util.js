function checkrole() {
	var flag = false;
	$.ajax({
		type : "post",
		url : "../../getAdminLoginInfo.ehr",
		dataType : 'json',
		async : false,
		success : function(json) {
			console.log(json);
			// 位机构管理员
			if (json.data.roletype == null) {
				alert("机构管理员没有操作权限！");
				flag = true;
			}
		}
	});
	return flag;
}