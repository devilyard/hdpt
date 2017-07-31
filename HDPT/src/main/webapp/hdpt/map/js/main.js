	// 百度地图API功能
	var map = new BMap.Map("map");          // 创建地图实例
	var point = new BMap.Point(113.433986,22.511841);  // 创建点坐标
	map.centerAndZoom(point, 15);                 // 初始化地图，设置中心点坐标和地图级别
	map.enableScrollWheelZoom();
	map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
	var customLayer, 
		keyword     = "",   //检索关键词
	    page        = 0;
	function addCustomLayer(keyword) {
	    if (customLayer) {
	        map.removeTileLayer(customLayer);
	    }
	    customLayer=new BMap.CustomLayer({
	        geotableId: 124249,
	        q: keyword, //检索关键字
	        tags: '', //空格分隔的多字符串
	        filter: '' //过滤条件,参考http://developer.baidu.com/map/lbs-geosearch.htm#.search.nearby
	    });
	    map.addTileLayer(customLayer);
	    customLayer.addEventListener('hotspotclick',callback);
	}
	addCustomLayer();

	function callback(e)//单击热点图层
	{
	        var customPoi = e.customPoi;//poi的默认字段
	        var contentPoi=e.content;//poi的自定义字段
	        var content = "<p>名称：" + customPoi.title + "</p>" +
            			  "<p>地址：" + customPoi.address + "</p>";
	        //创建检索信息窗口对象
	        var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
	            title: customPoi.title,  //标题
	            width: 290,              //宽度
	            panel  : "panel",        //检索结果面板
	            height: 40,              //高度
	            enableAutoPan : true,    //自动平移
	            enableSendToPhone: true, //是否显示发送到手机按钮
	            searchTypes :[
	                BMAPLIB_TAB_SEARCH,   //周边检索
	                BMAPLIB_TAB_TO_HERE,  //到这里去
	                BMAPLIB_TAB_FROM_HERE //从这里出发
	            ]
	        });
	        var point = new BMap.Point(customPoi.point.lng, customPoi.point.lat);
	        searchInfoWindow.open(point);
	}


	//绑定检索按钮事件
    $('#searchBtn').bind('click', function(){
        keyword = $('#keyword').val();
        searchAction(keyword);
    });
    
    
    /**
     * 进行检索操作
     * @param 关键词
     * @param 当前页码
     */
    function searchAction(keyword, page) {
        page = page || 0;
        var url = "http://api.map.baidu.com/geosearch/v3/local?callback=?";
        $.getJSON(url, {
            'q'          : keyword, //检索关键字
            'page_index' : page,  //页码
            'region'     : '中山', 
            'geotable_id': 124249,
          	'ak'         : 'f0TPvNIxMF3oL2ZwfB78ywG8'  //用户ak
        },function(e) {
            renderMap(e, page + 1);
        });
    }
    
    /**
     * 渲染地图模式
     * @param result
     * @param page
     */
    function renderMap(res, page) {
        var content = res.contents;
        $('#mapList').html('');
        if (content.length == 0) {
            $('#mapList').append($('<p style="border-top:1px solid #DDDDDD;padding-top:10px;text-align:center;text-align:center;font-size:18px;" class="text-warning">抱歉，没有找到您想要的信息，请重新查询</p>'));
            return;
        }

        $.each(content, function(i, item){
            var point = new BMap.Point(item.location[0], item.location[1])
            var tr = $("<tr><td width='75%'><a style='cursor: pointer;'>" + item.title + "<a/><br/>" + item.address + "</td></tr>").click(showInfo);
            $('#mapList').append(tr);
            function showInfo() {
                var content = "<p>名称：" + item.title + "</p>" +
                              "<p>地址：" + item.address + "</p>";
                //创建检索信息窗口对象
                var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
                    title  : item.title,       //标题
                    width  : 290,             //宽度
                    panel  : "panel",         //检索结果面板
                    enableAutoPan : true,     //自动平移
                    searchTypes   :[
                        BMAPLIB_TAB_SEARCH,   //周边检索
                        BMAPLIB_TAB_TO_HERE,  //到这里去
                        BMAPLIB_TAB_FROM_HERE //从这里出发
                    ]
                });
                searchInfoWindow.open(point);
            };
        });
        
		/**
         * 分页
         */
        var pagecount = Math.ceil(res.total / 10);
        if (pagecount > 76) {
            pagecount = 76; //最大页数76页
        }
        function PageClick (pageclickednumber) {
            pageclickednumber = parseInt(pageclickednumber);
            $("#mapPager").pager({ pagenumber: pageclickednumber, pagecount: pagecount, showcount: 3, buttonClickCallback: PageClick });
            searchAction(keyword, pageclickednumber -1);
        }
        $("#mapPager").pager({ pagenumber: page, pagecount: pagecount, showcount:3, buttonClickCallback: PageClick });


        
    };
    
    var isPanelShow = false;
    //显示结果面板动作
    $("#showPanelBtn").bind('click',function(){
        if (isPanelShow == false) {
            isPanelShow = true;
            $("#showPanelBtn").css("right","300px") ;
            $("#panelWrap").css("width","300px") ;
            $("#map").css("marginRight","300px");
            $("#showPanelBtn").html("隐藏检索结果面板<br/>>");
        } else {
            isPanelShow = false;
            $("#showPanelBtn").css("right","0px");
            $("#panelWrap").css("width","0px");
            $("#map").css("marginRight","0px");
            $("#showPanelBtn").html("显示检索结果面板<br/><");
        }
    })
    
	searchAction(keyword);
	//$(document).ready(function(){
	 	//var mapBoxHeight = $(window).height()  - $('#pageHeader').height() - $('#pageMiddle').height() - 38;
	   // $('#mapBox').css({height: mapBoxHeight + 'px'});
	    //$('#keyword').val('');
	//});