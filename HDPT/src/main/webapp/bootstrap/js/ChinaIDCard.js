function isChinaIDCard(StrNo){
	
	StrNo = StrNo.toString()
	if (StrNo.length==18){
	       var a,b,c,r="";
	       if (!isInteger(StrNo.substr(0,17))) {return false}
	       a=parseInt(StrNo.substr(0,1))*7+parseInt(StrNo.substr(1,1))*9+parseInt(StrNo.substr(2,1))*10;
	       a=a+parseInt(StrNo.substr(3,1))*5+parseInt(StrNo.substr(4,1))*8+parseInt(StrNo.substr(5,1))*4;
	       a=a+parseInt(StrNo.substr(6,1))*2+parseInt(StrNo.substr(7,1))*1+parseInt(StrNo.substr(8,1))*6; 
	       a=a+parseInt(StrNo.substr(9,1))*3+parseInt(StrNo.substr(10,1))*7+parseInt(StrNo.substr(11,1))*9; 
	       a=a+parseInt(StrNo.substr(12,1))*10+parseInt(StrNo.substr(13,1))*5+parseInt(StrNo.substr(14,1))*8; 
	       a=a+parseInt(StrNo.substr(15,1))*4+parseInt(StrNo.substr(16,1))*2;
	       b=a%11;
	       if (b==2){
	       		c=StrNo.substr(17,1).toUpperCase(); //转为大写X
	       }else{
	       		c=parseInt(StrNo.substr(17,1));
	       }
	       
	       
	       switch(b){
		       case 0: if ( c!=1 ) {r = "请输入有效的身份证号码";}break;
		       case 1: if ( c!=0 ) {r = "请输入有效的身份证号码";}break;
		       case 2: if ( c!="X") {r= "请输入有效的身份证号码";}break;
		       case 3: if ( c!=9 ) {r = "请输入有效的身份证号码";}break;
		       case 4: if ( c!=8 ) {r= "请输入有效的身份证号码";}break;
		       case 5: if ( c!=7 ) {r= "请输入有效的身份证号码";}break;
		       case 6: if ( c!=6 ) {r= "请输入有效的身份证号码";}break;
		       case 7: if ( c!=5 ) {r= "请输入有效的身份证号码";}break;
		       case 8: if ( c!=4 ) {r= "请输入有效的身份证号码";}break;
		       case 9: if ( c!=3 ) {r= "请输入有效的身份证号码";}break;
		       case 10: if ( c!=2 ){r= "请输入有效的身份证号码";}
	       }
	       if(r!=""){
				return false;
	       }
	}else{
		//15位身份证号
		if (!isInteger(StrNo)){
			return false;
		} 
	}
	
	switch(StrNo.length){
		case 15: 
			var temp = isValidDate("19"+StrNo.substr(6,2),StrNo.substr(8,2),StrNo.substr(10,2));
	          if(temp==true){
	          	return true;
	          }else{
	          	return false;
	          }
		case 18: 
			var temp = isValidDate(StrNo.substr(6,4),StrNo.substr(10,2),StrNo.substr(12,2));
	          if (temp==true){
	          	return true;
	          }else{
	          	return false;
	          }
	}
	return false
}

function isValidDate(iY, iM, iD) { 
	var a=new Date(iY,iM-1,iD);
	var y=a.getFullYear();
	var m=a.getMonth()+1;
	var d=a.getDate();
	if (y!=iY || m!=iM || d!=iD){
		return '身份证号码内日期错误！';
	}
	
	return true
}

function isInteger(str) {
	//if (/[^\d]+$/.test(str)){
	if (!/^\d+$/.test(str)){
		return false;
	}
	return true;
}


function IDUpdate(StrNo){

if (!isChinaIDCard(StrNo)) {return false}
if (StrNo.length==15)
{
       var a,b,c
       StrNo=StrNo.substr(0,6)+"19"+StrNo.substr(6,9)
       a=parseInt(StrNo.substr(0,1))*7+parseInt(StrNo.substr(1,1))*9+parseInt(StrNo.substr(2,1))*10;
       a=a+parseInt(StrNo.substr(3,1))*5+parseInt(StrNo.substr(4,1))*8+parseInt(StrNo.substr(5,1))*4;
       a=a+parseInt(StrNo.substr(6,1))*2+parseInt(StrNo.substr(7,1))*1+parseInt(StrNo.substr(8,1))*6; 
       a=a+parseInt(StrNo.substr(9,1))*3+parseInt(StrNo.substr(10,1))*7+parseInt(StrNo.substr(11,1))*9; 
       a=a+parseInt(StrNo.substr(12,1))*10+parseInt(StrNo.substr(13,1))*5+parseInt(StrNo.substr(14,1))*8; 
       a=a+parseInt(StrNo.substr(15,1))*4+parseInt(StrNo.substr(16,1))*2;
       b=a%11;

       switch(b)
       {
       case 0: {StrNo=StrNo+"1";}break;
       case 1: {StrNo=StrNo+"0";}break;
       case 2: {StrNo=StrNo+"X";}break;
       case 3: {StrNo=StrNo+"9";}break;
       case 4: {StrNo=StrNo+"8";}break;
       case 5: {StrNo=StrNo+"7";}break;
       case 6: {StrNo=StrNo+"6";}break;
       case 7: {StrNo=StrNo+"5";}break;
       case 8: {StrNo=StrNo+"4";}break;
       case 9: {StrNo=StrNo+"3";}break;
       case 10: {StrNo=StrNo+"3";}
       }
       }
       return StrNo;
}
function idcard_getsex(id){
	var id=String(id);
	return sex=id.slice(14,17)%2?"男":"女"
}

function idcard_getbirthday(id){
	var id=String(id);
	var birthday;
	if(id.length==15){
		birthday=(new Date(id.substr(6,2),id.substr(8,2)-1,id.substr(10,2))).toLocaleDateString() 
	}else if(id.length==18){
		birthday=(new Date(id.slice(6,10),id.slice(10,12)-1,id.slice(12,14))).toLocaleDateString() 
	}else{
		return false;
	}
	return birthday;
}

