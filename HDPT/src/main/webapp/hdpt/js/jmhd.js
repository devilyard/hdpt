;
(function(jQuery) {
	jQuery.jkw = {
		send : function(setting) {
			new Jkw().send(setting);
		},

		query : function(setting) {
			new Jkw().query(setting);
		},
		
		submit : function(setting) {
			new Jkw().submit(setting);
		},

		load : function(setting) {
			new Jkw().load(setting);
		},
		
		check : function (setting){
			new Jkw().check(setting);
			
		},

		loadGrid : function(setting) {
			return new Jkw().loadGrid(setting);
		},

		path : function(){
			var strFullPath=window.document.location.href;
			var strPath=window.document.location.pathname;
			var pos=strFullPath.indexOf(strPath);
			var prePath=strFullPath.substring(0,pos);
			var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
			return(prePath+postPath);
		},

		dic : function(){
			alert("11");
		},

		alert : function(msg,options){
			var o = {
				title : "系统提示",
				close : function(event, ui) {
					location.href.reload();
				},
				buttons: {
					Ok: function() {
						$(this).dialog( "destroy" );
					}
				}
			};

			o = $.extend(o,options);

			alert = $("<p><span class='ui-icon ui-icon-alert' style='float:left; margin:0 7px 20px 0;'></span>"+msg+"</p>").dialog(o);

		},

		iframe : function(options){

			options = $.extend({
				height : 560,
				callback : null
			});

			var o = options;

			var pageHeight = document.documentElement.scrollHeight || document.body.scrollHeight;

			pageHeight = pageHeight < o.height ? o.height : pageHeight;
			
			//parent.resizeIframeHeight(pageHeight);

		},

		hover : function(options){

			options = $.extend({
				objId: "",
				overCss : "row-odd",
				outCss : "row"
			},options);

			var o = options;
			$("#" + o.objId + " tr").bind("mousemove",function(){
				$(this).removeClass(o.outCss).addClass(o.overCss);
			}).bind("mouseout",function(){
				$(this).removeClass(o.overCss).addClass(o.outCss);
			});
		}

	};

	function Jkw() {

		var _self = this;

		this.resultData = null,

		this.cfg = {
			debug : false,
			cache : false,//web服务器前面 oscache
			curRequest : 0,	//请求次数计数器
			maxRequest : 5,	//最大请求数
			timeout : 60000,	//超时时间
			reLoadStep : 5000,	//重新获取结果间隔
			templateIdx : 1,	//输出的数据行模板序号
			target_url : '',
			back_url : '',
			pagesize : 20,
			args : {},
			formid : '',
			tabid : '',
			pager : 'pager',
			data : '',	//action里mo后返回的数据
			query : '', // 表格查询字段数组
			before : function() {
				return true;
			},
			comeback : null,	//请求完成后回调Grid的行函数
			doTemplate : null,	//设置模板配置该参数后将不再调用doGridRow以及自动填充json的操作
			doGridRow : null,	//设置Grid的行函数
			type : 1   //检查session，1需要，0不需要
		},

		this.init = function(setting) {
			this.curRequest = 0;
			_self.cfg = jQuery.extend(_self.cfg, setting);
			this.check();
		},
		
		// 查询请求
		this.query = function(setting) {
			this.init(setting);
			if (_self.cfg.target_url == "") {
				alert("参数错误(send)！");
				return;
			}
			;
			_self.loading("show");
			jQuery.ajax({
				async : false,
     		   	cache : false,
				type : "POST",
				url : _self.cfg.target_url,
				data : _self.cfg.args,
				dataType : 'json',
				success : function(data) {
					_self.loading("hide");
					_self.cfg.comeback(data);
				},
				timeout : _self.cfg.timeout,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("出错啦！" + "\n\r" + textStatus + "\n\r" + errorThrown);
					_self.loading("hide");
				}
			});
		},

		// 提交请求
		this.send = function(setting) {

			this.init(setting);
			if (_self.cfg.target_url == "" || _self.cfg.back_url == "") {
				alert("参数错误(send)！");
				return;
			}
			;
			_self.loading("show");
			jQuery.ajax({
				type : "POST",
				url : _self.cfg.target_url,
				data : _self.cfg.args,
				dataType : 'json',
				success : function(data) {
					_self.resultData = data;
					//_self.call();
					_self.loading("hide");
					_self.cfg.comeback(data.data);
				},
				timeout : _self.cfg.timeout,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("出错啦！" + "\n\r" + textStatus + "\n\r" + errorThrown);
					_self.loading("hide");
				}
			});
		},

		// 提交表单
		this.submit = function(setting) {
			this.init(setting);

			if (_self.cfg.formid == "" || _self.cfg.back_url == "") {
				alert("参数错误(submit)!");
				return;
			}
			$('#' + _self.cfg.formid).ajaxForm({
				dataType : 'json',
				timeout : _self.cfg.timeout,
				success : function(data) {
					//_self.resultData = data;
					//_self.call();
					_self.cfg.comeback(data.data);
				},
				beforeSubmit : function() {
					_self.cfg.curRequest = 0;
				    return _self.cfg.before();
				}
			});
		},

		// 加载数据
		this.load = function(setting) {
			this.init(setting);
			this.loading("show");
			if (_self.cfg.back_url == "") {
				alert("参数错误(load)！");
				return;
			}
			;
			_self.loading("show");
			_self.resultData = setting.data;
			this.call();
		};
		
		
		this.check = function(){
			if(_self.cfg.type == 1){
				jQuery.ajax({
					type : "POST",
					url :"../jmhduser/check.ehr",
					dataType : 'json',
					success : function(data) {
						if(!data.success){
							top.location.href = '../logon.html';
							return;
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("出错啦！" + "\n\r" + textStatus + "\n\r" + errorThrown);
					}
				});
			}else if(_self.cfg.type == 2){
				var url = "../../check.ehr";
				var backurl = "../login.html";
				if(_self.cfg.tabid == "home"){
				  	url = "../check.ehr";
				  	backurl = "../admin/login.html";
				}
				jQuery.ajax({
					type : "POST",
					url : url,
					dataType : 'json',
					success : function(data) {
						if(!data.success){
							top.location.href = backurl;
							return;
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("出错啦！" + "\n\r" + textStatus + "\n\r" + errorThrown);
					}
				});
			}
				
		};

		// 加载表格数据
		this.loadGrid = function(setting) {
			var grid = this;
			this.init(setting);
			//console.log("loadGrid:"+_self.cfg.nowpage);
			var dataSecond=null;
			var queryFlag = false;
			var pagerInitFlag = false;
			var callBack = _self.cfg.comeback;

			_self.loading("show");
			if (_self.cfg.back_url == "" || _self.cfg.tabid == "") {
				alert("参数错误(loadGrid)！");
				return;
			};

			$("#" + _self.cfg.tabid + " tr").eq(1).hide();

			// 加载结果
			var cfg = {
				"back_url" : _self.cfg.back_url,
				"comeback" : doLoadTable,
				"data" : _self.cfg.data
			};

			var loadCfg = jQuery.extend(_self.cfg, cfg);
			$.jkw.load(loadCfg);

			function doLoadTable(data) {
				dataSecond=data;
				//console.log("doLoadTable");
				//console.log("nowpage："+$("#nowPage").val());
				showPager(data);
			}
			;

			this.query = function() {
				queryFlag = true;
				pagerInitFlag = false;
				pageChange(0, grid);
			};
			
			this.queryPage = function(pageNum) {
				queryFlag = true;
				pagerInitFlag = false;
				pageChangeInit(pageNum-1, grid);
			};

			function pageChangeInit(page_index, jq){
				if (pagerInitFlag) {
					pagerInitFlag = false;
					return;
				}
				//$("#nowPage").val(page_index+1);
				var args = {
					"page" : page_index + 1
				};
				// 处理查询参数
				$.each(_self.cfg.query, function(i, o) {
					args[o] = $("#" + o).val();
				});
				var cfg = {
					"type" : 0,
					"target_url" : _self.cfg.target_url,
					"back_url" : _self.cfg.back_url,
					"comeback" : function(data) {
						var pbCfg = data.pb;
						data.pb.page = page_index+1;
						showGrid(data);
						queryFlag = false;
						pagerInitFlag = true;
						console.log("pageChangeInit");
						showPager(data);
						_self.loading("hide");
						//if(callBack){
							//callBack(data);
						//}
					},
					"args" : args
				};
				$.jkw.send(cfg);
			};
			
			function pageChange(page_index, jq) {
				if (pagerInitFlag) {
					pagerInitFlag = false;
					return;
				}
				//$("#nowPage").val(page_index+1);
				var args = {
					"page" : page_index + 1
				};
				// 处理查询参数
				$.each(_self.cfg.query, function(i, o) {
					args[o] = $("#" + o).val();
				});
				if(dataSecond!=null){
					var pbCfg = dataSecond.pb;
					showGrid(dataSecond);
					if (queryFlag) {
						queryFlag = false;
						pagerInitFlag = true;
						console.log("pageChange");
						showPager(dataSecond);
					}
					_self.loading("hide");
					dataSecond=null;
					if(callBack){
						callBack(dataSecond);
					}
				}else{
					var cfg = {
						"type" : 0,
						"target_url" : _self.cfg.target_url,
						"back_url" : _self.cfg.back_url,
						"comeback" : function(data) {
							var pbCfg = data.pb;
							showGrid(data);
							if (queryFlag) {
								queryFlag = false;
								pagerInitFlag = true;
								console.log("pageChangeElse");
								showPager(data);
							}
							_self.loading("hide");
							if(callBack){
								callBack(data);
							}
						},
						"args" : args
					};
					$.jkw.send(cfg);
				}
			}
			;

			function showPager(data) {
				var pbCfg = data.pb;
				var pg = $("#" + _self.cfg.pager);
				var $top = $("a[name=top]");
				if(!$top.length){
					$("body").prepend($("<a name='top' href='#'></a>"));
				}
				//if($("#nowPage").val() != null){
					//console.log("nowPage!= null:"+$("#nowPage").val());
					//if($("#nowPage").val()!=""){
						//console.log("nowPage!= '':"+$("#nowPage").val());
					   //pbCfg.page = $("#nowPage").val();
					   //$("#nowPage").val("");
					//}
				//}
				console.log("showPager:"+pbCfg.page);
				if (pbCfg != null && pbCfg.pagecount > 0) {
					var opt = {
						//link_to : "#top",
						prev_text : "上一页",
						next_text : "下一页",
						items_per_page : data.pb.pagesize,
						current_page : pbCfg.page==0 ? pbCfg.page : pbCfg.page - 1,
						callback : pageChange
					};
					$(pg).pagination(pbCfg.recordCount, opt);
				} else {
					$(pg).text("无数据");
					_self.loading("hide");
				}
			};

			function showGrid(data){
				//var nowPage = data.pb.page;
				var tabid = _self.cfg.tabid;
				//$("#nowPage").val(nowPage);
				var tabObj = $("#" + tabid);
				var list = data.list;
				var baseTh=$("#" + tabid + " tr").eq(0);
				var tempTr=$("#" + tabid + " tr").eq(1);
				var template = $(tempTr).html();
				tabObj.empty().append(baseTh).append(tempTr);
				if(list!=null && list.length>0){
					//var items_per_page = data.pb.pagesize;
					//var page_index = data.pb.page==0 ? 0 : data.pb.page-1;
					//console.info(page_index);
					//var max_elem = Math.min((page_index+1) * items_per_page, data.pb.recordCount);
					//console.info(max_elem);
					// 获取加载元素
					for(var i=0;i<list.length;i++){
						var o = list[i];
						var newTrObj = $(tempTr).clone().show();
						var newTr = template;
						if(_self.cfg.doGridRow!=null){
							newTr = _self.cfg.doGridRow(newTr,o);
						}else{
							$.each(o,function(key,val){
								newTr = newTr.replaceAll("{"+key+"}",val);
							});
						}
						$(tabObj).append(newTrObj.html(newTr));
					}
					
					//$(" tr:odd",$(tabObj)).removeClass("row").removeClass("row-clk").addClass("row-odd");
					$(" tr",$(tabObj)).bind("mousemove",function(){
						$(this).removeClass("row").addClass("row-odd");
					}).bind("mouseout",function(){
						$(this).removeClass("row-odd").addClass("row");
					});
				}else{
					tempTr.clone().appendTo(tabObj);
				}
			};
			return grid;
		};

		this.call = function() {
			_self.loading("show");


			var data = _self.resultData;
			if (_self.cfg.maxRequest <= _self.cfg.curRequest) {
				_self.showTimeOut();
				return;
			}
			;

			_self.cfg.curRequest++;
			if (data.success) {
				var calldata = {
						"tag" : data.tag,
						"cacheKey" : data.cacheKey,
						"tsp" : Date.parse(new Date()),
						"type": data.type
					}
				calldata = $.extend(calldata,_self.cfg.data);
				$.getJSON(_self.cfg.back_url, 
						calldata, 
						function(retJson) {
				//	alert(_self.cfg.curRequest)
					if (retJson.success) {
						_self.cfg.comeback(retJson.data);
						_self.loading("hide");
					} else {
						// alert("计时器:::" + JSON.stringify(data));
						// var fun = Function.createDelegate(_self,_self.call);
						setTimeout(_self.call, _self.cfg.reLoadStep);
					}
				});
			}
			;
		};

		this.showTimeOut = function() {
			_self.loading("hide");
			alert("请求超时，请刷新页面后重试。");
		};

		this.loading = function(status) {
//			if ($("#loading").size() == 0) {
//				$("body").prepend('<div id="loading">加载中...</div>');
//			}
//			if (status == "show") {
//				$("#loading").fadeIn();
//			} else {
//				$("#loading").fadeOut();
//			}
		};
	}
	;




	/*
	tabs: $.fn.extend({
				tabs : function(options) {
					options = $.extend({
						event : 'mouseover',
						timeout : 0,
						auto : 0,
						highlight : 'current',
						hide : 'hide',
						callback : null
					}, options);
					var self = $(this), tab_div = $('div', self)
							.children('div'), ul = self.children('ul'), lis = ul
							.find('li');
					lis.bind(options.event, function() {
						delay(this, options.timeout);
						if (options.callback) {
							options.callback(self);
						}
					});
					var delay = function(that, t) {
						t > 0 ? setTimeout(function() {
							tab(that);
						}, t) : tab(that);
					}, tab = function(that) {
						$(that).siblings('li').removeClass(options.highlight)
								.end().addClass(options.highlight);
						tab_div.siblings('div').addClass(options.hide).end()
								.eq($(that).index()).removeClass(options.hide);
					};
					if (options.auto > 0) {
						startAuto();
					}
					self.hover(function() {
						clearInterval(self.timer);
					}, function() {
						startAuto();
					});
					function startAuto() {
						if (options.auto === 0)
							return;
						self.timer = setInterval(autoRun, options.auto);
					}
					function autoRun() {
						var li = ul.find('.' + options.highlight), first_li = lis
								.eq(0), len = lis.length, index = li.index() + 1;
						li.removeClass(options.highlight);
						index === len ? run(first_li, 0) : run($(li.next('li')
								.eq(0)), index);
						function run(li, index) {
							li.addClass(options.highlight);
							tab_div.siblings('div').addClass(options.hide)
									.end().eq(index).removeClass(options.hide);
						}
					}
					;

					this.selected = function(idx) {
						self.siblings('li').removeClass(options.highlight)
								.end().addClass(options.highlight);
						tab_div.siblings('div').addClass(options.hide).end()
								.eq(idx).removeClass(options.hide);
						if (options.callback) {
							options.callback(self);
						}
					};
					return this;
				}
			});
	table: $.fn.extend({
		table : function(options){
			var templateTr = "";
			options = $.extend({
				'gridid' : 'grid',
				'data' : [] ,
				'doTemplate' : function(){
					$.each(data,function(key,val){
						templateTr = templateTr.replaceAll("{"+key+"}", val);
					});

					return true;
				}
			}, options);

			var tabObj = $("#" + options.gridid );
			var baseTh=$("#" + options.gridid + " tr").eq(0);
			var baseRow = $("#" + options.gridid + " tr").eq(1);
			var template = $(baseRow).html();

			tabObj.empty().append(baseTh).append(template);
			if(list!=null && list.length>0){
				$.each(data,function(key,val){
					templateTr = templateTr.replaceAll("{"+key+"}", val);
				});


				$.each(list,function(idx,o){
					options.doTemplate();
					var newTr = template;
					var cache =  JSON.parse(o.cacheJson);
					newTr = newTr.replaceAll("{0}",o.title);
					newTr = newTr.replaceAll("{1}",o.createTime.formatDate("yyyy-MM-dd"));
					newTr = newTr.replaceAll("{2}",showCache(cache.indicator));
					newTr = newTr.replaceAll("{3}",o.id);
					$(tabObj).append($("<tr/>").html(newTr));
				});
			}else{
				tempTr.clone().appendTo(tabObj);
			}
		}
	});
	*/
})(jQuery);


//文本替换函数
String.prototype.replaceAll = function (AFindText, ARepText) {
	var raRegExp = new RegExp(AFindText.replace(/([\(\)\[\]\{\}\^\$\+\-\*\?\.\"\'\|\/\\])/g, "\\$1"), "ig");
	return this.replace(raRegExp, ARepText);
};
//日期格式化
Date.prototype.format = function(format){
    var o = {
        "M+" : this.getMonth()+1, // month
        "d+" : this.getDate(),    // day
        "h+" : this.getHours(),   // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth()+3)/3),  // quarter
        "S" : this.getMilliseconds() // millisecond
    };
    if(/(y+)/.test(format))
    	format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
    	if(new RegExp("("+ k +")").test(format))
    		format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
};
String.prototype.formatDate = function(format){
	return new Date(this.replace(/-/g,"/")).format(format);
};
Number.prototype.formatDate = function(format){
	format = format ? format : 'yyyy-MM-dd';
	return new Date(this).format(format);
};
String.prototype.subReplace = function(len){
	var l=parseInt(len)+2;
	if(this.length>=l){
		return this.substring(0,len)+"..";
	}else{
		return this;
	}
};
//截取字符串长度
function subString(obj){
    var objString = obj.values;
    var objLength = objString.length;
    var num = obj.num;
    if(objLength > num){
	        objString = objString.substring(0,num) + "...";
	    }
    return objString;
}
//显示省略标题
function getTitle(obj){
    var objString = obj.values;
    var objLength = objString.length;
    var num = obj.num;
    if(objLength <= num){
	        objString = "点击查看详情";
	    }
    return objString;
}

//将对象转化为文本
function objTostring(obj){
	var objString = "{";
	var i = 0;
	$.each(obj,function(key,o){
		if(i==0){
			objString = objString+"'"+key+"':'"+o+"'";
		}else{
			objString=objString+",";
			objString = objString+"'"+key+"':'"+o+"'";
		}
		i++;
	});
	objString = objString+"}";
	return objString;
}



//初始化页面高度
function initIframeSize(){
	$("a[target=main]").click(function(){
	 	$("#main").css("height",_defaultHeight);
	});
	$("#main").css("height",_defaultHeight);
	var iframeTag = document.getElementById('main');
 	iframeTag.style.display = 'block';
}
//根据嵌入页面重置页面高度
function resizeIframeHeight(h){
 	document.getElementById('main').style.height = (parseInt(h)) + 'px';
};

function initContAreaStyle(){
	var width=document.body.clientWidth-8;
	$("#contarea").css({width:width+'px',background:'#f3f7ff',margin:'4px',border:'1px solid #99bbe8'});
}


function AddFavorite(sURL, sTitle)
{
    try
    {
        window.external.addFavorite(sURL, sTitle);
    }
    catch (e)
    {
        try
        {
            window.sidebar.addPanel(sTitle, sURL, "");
        }
        catch (e)
        {
            alert("加入收藏失败，请使用Ctrl+D进行添加");
        }
    }
}
function SetHome(obj,vrl){
    try{
            obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);
    }
    catch(e){
            if(window.netscape) {
                    try {
                            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
                    }
                    catch (e) {
                            alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");
                    }
                    var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
                    prefs.setCharPref('browser.startup.homepage',vrl);
             }
    }
}
