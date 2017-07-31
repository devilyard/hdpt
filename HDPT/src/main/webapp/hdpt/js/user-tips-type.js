var plantType = {
	"SQ1" : "高血压随访",
	"SQ2" : "糖尿病随访",
	"SQ3" : "肿瘤随访",
	"SQ4" : "老年人保健",
	"SQ5" : "儿童询问",
	"SQ6" : "儿童体格检查",
	"SQ7" : "体弱儿童随访",
	"SQ8" : "孕妇随访",
	"SQ9" : "孕妇高危随访",
	"SQ10" : "精神病随访",
	"SQ11" : "高血压询问"
};

var hospitalCode={
	"47024048X33010312A3001" :	"中山市下城区中西医结合医院",
	"47012316033011011A5201" :	"中山市安康医院",
	"47011661433010211A1001" :	"中山市第一人民医院",
	"47010492033010211A2241" :	"杭州推拿医院",
	"47010492033010211A1001" : "中山市第四人民医院",
	"47042200533012711A1001" : "淳安县第二人民医院",
	"47033249933018311H1191" : "富阳市精神病防治院",
	"73450350433018311A2221" : "富阳市新登中医骨伤科医院",
	"47039066433011011A2101" : "中山市余杭区中医院",
	"47002675833010411A5271" : "浙江民政康复中心",
	"47033128533018311G1001" : "富阳市妇幼保健院",
	"47032030833012211A1001" : "桐庐县第二人民医院",
	"PDY10003733010611A1001" : "浙江大学校医院",
	"47011663033010211A1001" : "中山市第三人民医院",
	"47042156333012711A2101" : "淳安县中医院",
	"75440502033010911A1001" : "浙江萧山医院",
	"47011672933010316A5282" : "中山市整形医院",
	"47039065633011011G1001" : "中山市余杭区妇幼保健院",
	"47033256033018311A2221" : "富阳市中医骨伤医院",
	"47036226833018511A1001" : "临安市於潜人民医院",
	"47032008433012211A1001" : "桐庐县第一人民医院",
	"47011667333010611A5201" : "中山市第七人民医院",
	"47039032233011011A1001" : "中山市余杭区第三人民医院",
	"47048378033018211A5201" : "建德市第四人民医院",
	"47036227633018511A2101" : "临安市中医院",
	"47045223933010911A1001" : "萧山区第三人民医院",
	"47042078X33012711G1001" : "淳安县妇幼保健院",
	"PDY10030133010511A1001" : "杭州钢铁集团公司职工医院",
	"47021107233010411A1001" : "中山市江干区笕桥医院",
	"248277933010411A1001" : "浙江省青春医院",
	"47011662233010511A1001" : "杭州师范大学附属医院",
	"47039033033011011A1001" : "中山市余杭区第二人民医院",
	"47048387933018211A1001" : "建德市第三人民医院",
	"47032007633012211A2101" : "桐庐县中医医院",
	"47042154733012711A1001" : "淳安县第一人民医院",
	"47045209533010911A2221" : "萧山区中医骨伤科医院",
	"47011666533010311A5211" : "中山市第六人民医院",
	"47011668133010611A2101" : "中山市中医院",
	"40000897633018211H1131" : "国家电网公司职业病防治院",
	"47048360933018211G1001" : "建德市妇幼保健院",
	"47045453333010911A1001" : "萧山区第四人民医院",
	"47045555233010912A1001" : "萧山经济技术开发区医院",
	"47045332X33010911A2101" : "中山市萧山区中医院",
	"47000413733010611A2101" : "浙江中医药大学附属第三医院2",
	"47048366833018211A1001" : "建德市第一人民医院",
	"47045349333010911A1001" : "中山市萧山区第一人民医院",
	"47048362533018211A1001" : "建德市第二人民医院",
	"47036230533018511A1001" : "临安市昌化人民医院",
	"47030048933012211G1001" : "桐庐县妇幼保健院",
	"47027033933010612A1001" : "中山市西湖区第二人民医院",
	"47045202833010911A1001" : "萧山区第二人民医院",
	"72909101533018311A2101" : "富阳市中医医院",
	"47036225X33018511A1001" : "临安市人民医院",
	"47048363333018214A2101" : "建德市中医医院",
	"47021106433010411A1001" : "中山市江干区人民医院",
	"47011671033010211G1001" : "中山市妇幼保健院",
	"47033127733018311A1001" : "富阳市人民医院",
	"47011665733010311A1001" : "中山市红十字会医院",
	"47039064833011011A1001" : "中山市余杭区第一人民医院",
	"47033129333018311A1001" : "富阳市第二人民医院"
};

var brbslbType={
		"1":"就诊",
		"2":"住院"
};

var drugCode = {
			"":"-",
			"4IW" : "",
			"BID" : "一天二次",
			"BIW" : "每周二次",
			"FIW" : "",
			"LS" : "备用时",
			"PRN" : "必要时",
			"Q12H" : "每12小时",
			"Q1H" : "每1小时",
			"Q2H" : "每2小时",
			"Q3D" : "",
			"Q3H" : "每3小时",
			"Q4H" : "每4小时",
			"Q5H" : "每周五次",
			"Q6H" : "每6小时",
			"Q8H" : "每8小时",
			"QD" : "一天一次",
			"QID" : "一天四次",
			"QIW" : "",
			"QN" : "每晚一次",
			"QOD" : "隔天一次",
			"QW" : "每周一次",
			"ST" : "临时",
			"TID" : "一天三次",
			"TIW" : "每周三次"
	};
