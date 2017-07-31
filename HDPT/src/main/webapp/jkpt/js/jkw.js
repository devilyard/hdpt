;
(function(jQuery) {
	jQuery.jkw = {
		send : function(setting) {
			new Jkw().send(setting);
		},

		submit : function(setting) {
			new Jkw().submit(setting);
		},

		load : function(setting) {
			new Jkw().load(setting);
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

			parent.resizeIframeHeight(pageHeight);

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
		var _alertStatus = false;
		this.resultData = null,

		this.cfg = {
			debug : false,
			curRequest : 0,	//请求次数计数器
			maxRequest : 5,	//最大请求数
			timeout : 30000,	//超时时间
			reLoadStep : 2000,	//重新获取结果间隔
			templateIdx : 1,	//输出的数据行模板序号
			target_url : '',
			back_url : '',
			pagesize : 20,
			args : {},
			formid : '',
			tabid : '',
			pager : 'pager',
			pager_text :{
				prev : "上一页",
				next : "下一页"
			},
			data : '',	//action里mo后返回的数据
			query : '', // 表格查询字段数组
			before : function() {
				return true;
			},
			comeback : null,	//请求完成后回调Grid的行函数
			doTemplate : null,	//设置模板配置该参数后将不再调用doGridRow以及自动填充json的操作
			doGridRow : null	//设置Grid的行函数
		},

		this.init = function(setting) {
			this.curRequest = 0;
			_self.cfg = jQuery.extend(_self.cfg, setting);
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
					_self.call();
				},
				timeout : _self.cfg.timeout,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("出错啦！" + "\n\r" + textStatus + "\n\r" + errorThrown);
					_self.loading("hide");
				}
			});
		};

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
					_self.resultData = data;
					_self.call();
				},
				beforeSubmit : function() {
					_self.cfg.curRequest = 0;
					_self.cfg.before();
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

		// 加载表格数据
		this.loadGrid = function(setting) {
			var grid = this;
			this.init(setting);
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
				showPager(data);
			}
			;

			this.query = function() {
				queryFlag = true;
				pagerInitFlag = false;
				pageChange(0, grid);
			};

			function pageChange(page_index, jq) {

				if (pagerInitFlag) {
					pagerInitFlag = false;
					return;
				}
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
						showPager(dataSecond);
					}
					if(callBack){
						callBack(dataSecond);
					}
					_self.loading("hide");
					dataSecond=null;
				}else{
					var cfg = {
						"target_url" : _self.cfg.target_url,
						"back_url" : _self.cfg.back_url,
						"comeback" : function(data) {
							var pbCfg = data.pb;
							showGrid(data);
							if (queryFlag) {
								queryFlag = false;
								pagerInitFlag = true;
								showPager(data);
							}
							if(callBack){
								callBack(data);
							}
							_self.loading("hide");
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
				if (pbCfg != null && pbCfg.pagecount > 0) {

					var prev_text = "上一页",next_text = "下一页";

					if(!_self.cfg.page_text){
						prev_text = _self.cfg.pager_text.prev;
						next_text = _self.cfg.pager_text.next;
					}

					var opt = {
						//link_to : "#top",
						prev_text : prev_text,
						next_text : next_text,
						items_per_page : _self.cfg.pagesize,
						current_page : pbCfg.page - 1,
						callback : pageChange
					};
					$(pg).pagination(pbCfg.recordCount, opt);
				} else {
					$(pg).text("没有数据");
					_self.loading("hide");
				}
			};

			function showGrid(data){
				var tabid = _self.cfg.tabid;
				var tabObj = $("#" + tabid);
				var list = data.list;
				var baseTh=$("#" + tabid + " tr").eq(0);
				var tempTr=$("#" + tabid + " tr").eq(1);
				var template = unescape($(tempTr).html());

				tabObj.empty().append(baseTh).append(tempTr);
				if(list!=null && list.length>0){
					$.each(list,function(idx,o){
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
					});
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

				$.ajax({
					url: _self.cfg.back_url,
					//dataType : "json",
					data: {
						"tag" : data.tag,
						"cacheKey" : data.cacheKey,
						"tsp" : Date.parse(new Date())
					},
					success: function(retJson){
						retJson = eval('('+retJson +')');

						if (retJson.success) {
							_self.cfg.comeback(retJson.data);
							_self.loading("hide");
						} else {
							// alert("计时器:::" + JSON.stringify(data));
							// var fun = Function.createDelegate(_self,_self.call);
							setTimeout(_self.call, _self.cfg.reLoadStep);
						}
					},
					error: function(jqXHR, textStatus, errorThrown){
						//alert("ERROR:" + errorThrown + textStatus);
					}
				});
				/*
				$.getJSON(_self.cfg.back_url, {
					"tag" : data.tag,
					"tsp" : Date.parse(new Date())
				}, function(retJson) {
					//retJson = JSON.parse(retJson);
					alert(retJson.success);
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
				*/
			}
			;
		};

		this.showTimeOut = function() {
			_self.loading("hide");
			_alertStatus = true;
			if(!_alertStatus){
				alert("请求超时，请刷新页面后重试。");
			}
		};

		this.loading = function(status) {
			if ($("#loading").size() == 0) {
			//	$("body").prepend('<div id="loading">加载中...</div>');
			}
			if (status == "show") {
				$("#loading").fadeIn();
			} else {
				$("#loading").fadeOut();
			}
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
	if(this==null&&this==undefined&&this=='')
		return false;
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
function getMyTime() {
	var date = new Date();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	var timeStr = '当前时间：' + date.getFullYear() + '年' + (date.getMonth()+1) + '月' + date.getDate() + '日 ';
	//timeStr += (h % 12 < 10 ? '0' : '') + (h < 12 ? h : (h - 12)) + '时';
	//timeStr += (m < 10 ? '0' : '') + m + '分';
	//timeStr += (s < 10 ? '0' : '') + s + '秒';
	return timeStr;
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


