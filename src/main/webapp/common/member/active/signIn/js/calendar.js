var calUtil = {
  //当前日历显示的年份
  showYear:2018,
  //当前日历显示的月份
  showMonth:1,
  //当前日历显示的天数
  showDays:1,
  eventName:"load",
  //初始化日历
  init:function(signList){
    calUtil.draw(signList);
    calUtil.bindEnvent();
  },
  draw:function(signList){
    //绑定日历
    var str = calUtil.drawCal(calUtil.showYear,calUtil.showMonth,signList);
    $("#calendar").html(str);
    //绑定日历表头
    var calendarName=calUtil.showYear+"年"+calUtil.showMonth+"月";
    $(".calendar_month_span").html(calendarName);  
  },
  //绑定事件
  bindEnvent:function(){
    //绑定上个月事件
    $(".calendar_month_prev").click(function(){
      //ajax获取日历json数据
      calUtil.eventName="prev";
//      calUtil.initSign();
    });
    //绑定下个月事件
    $(".calendar_month_next").click(function(){
      //ajax获取日历json数据
      calUtil.eventName="next";
//      calUtil.initSign();
    });
  },
  //获取当前选择的年月
  setMonthAndDay:function(){
    switch(calUtil.eventName)
    {
      case "load":
        var current = new Date();
        calUtil.showYear=current.getFullYear();
        calUtil.showMonth=current.getMonth() + 1;
        break;
      case "prev":
        var nowMonth=$(".calendar_month_span").html().split("年")[1].split("月")[0];
        calUtil.showMonth=parseInt(nowMonth)-1;
        if(calUtil.showMonth==0)
        {
            calUtil.showMonth=12;
            calUtil.showYear-=1;
        }
        break;
      case "next":
        var nowMonth=$(".calendar_month_span").html().split("年")[1].split("月")[0];
        calUtil.showMonth=parseInt(nowMonth)+1;
        if(calUtil.showMonth==13)
        {
            calUtil.showMonth=1;
            calUtil.showYear+=1;
        }
        break;
    }
  },
  getDaysInmonth : function(iMonth, iYear){
   var dPrevDate = new Date(iYear, iMonth, 0);
   return dPrevDate.getDate();
  },
  bulidCal : function(iYear, iMonth) {
   var aMonth = new Array();
   aMonth[0] = new Array(7);
   aMonth[1] = new Array(7);
   aMonth[2] = new Array(7);
   aMonth[3] = new Array(7);
   aMonth[4] = new Array(7);
   aMonth[5] = new Array(7);
   aMonth[6] = new Array(7);
   var dCalDate = new Date(iYear, iMonth - 1, 1);
   var iDayOfFirst = dCalDate.getDay();
   var iDaysInMonth = calUtil.getDaysInmonth(iMonth, iYear);
   var iVarDate = 1;
   var d, w;
   aMonth[0][0] = "日";
   aMonth[0][1] = "一";
   aMonth[0][2] = "二";
   aMonth[0][3] = "三";
   aMonth[0][4] = "四";
   aMonth[0][5] = "五";
   aMonth[0][6] = "六";
   for (d = iDayOfFirst; d < 7; d++) {
    aMonth[1][d] = iVarDate;
    iVarDate++;
   }
   for (w = 2; w < 7; w++) {
    for (d = 0; d < 7; d++) {
     if (iVarDate <= iDaysInMonth) {
      aMonth[w][d] = iVarDate;
      iVarDate++;
     }
    }
   }
   return aMonth;
  },
  ifHasSigned : function(signList,day){
   var signed = false;
   $.each(signList,function(index,item){
    if(item.signDay == day) {
     signed = true;
     return false;
    }
   });
   return signed ;
  },
  drawCal : function(iYear, iMonth ,signList) {
   calUtil.showDays = 0;
   var myMonth = calUtil.bulidCal(iYear, iMonth);
   var htmls = new Array();
   htmls.push("<div class='sign_main' id='sign_layer'>");
   htmls.push("<div class='sign_succ_calendar_title'>");
//   htmls.push("<div class='calendar_month_next'>下月</div>");
//   htmls.push("<div class='calendar_month_prev'>上月</div>");
   htmls.push("<div class='calendar_month_span'></div>");
   htmls.push("<div class='qiandao-history qiandao-tran qiandao-radius' id='js-qiandao-history' onclick='openlay()'>我的签到</div>");
   htmls.push("</div>");
   htmls.push("<div class='sign' id='sign_cal'>");
   htmls.push("<table>");
   var d, w;
   for (w = 1; w < 7; w++) {
    htmls.push("<tr>");
    for (d = 0; d < 7; d++) {
     var ifHasSigned = calUtil.ifHasSigned(signList,myMonth[w][d]);
     var now_day = (!isNaN(myMonth[w][d]) ? myMonth[w][d] : " ");//当月每一天
     if(ifHasSigned){
      htmls.push("<td class='on'>" + now_day + "</td>");
     } else {
      htmls.push("<td>" + now_day + "</td>");
     }
     if(now_day && now_day != " "){
    	 calUtil.showDays += 1;
     }
    }
    htmls.push("</tr>");
   }
   htmls.push("</table>");
   htmls.push("</div>");
   htmls.push("</div>");
   return htmls.join('');
  },
  //签到业务逻辑
  initSign : function() {
	  calUtil.setMonthAndDay();//设置当前年月
	  $.ajax({
		  "url": base + "/m/signByMontht.do",
		  "data" : {
			  "signYear":calUtil.showYear,
			  "signMonth":calUtil.showMonth,
			  "signDay":calUtil.showDays
		  },
		  "type" : "POST",
		  "dataType" : "json",
		  "contentType" : "application/x-www-form-urlencoded",
		   success : function(result, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {// 正常响应
					signList = result;
					calUtil.init(signList);
				} else if (ceipstate == 2) {// 后台异常
					signList=[];
					calUtil.init(signList);
					alert(result.msg);
				} else if (ceipstate == 3) { // 业务异常
					signList=[];
					calUtil.init(signList);
					alert(result.msg);
				}
		   }
	  });
  },
  signToday : function() {
		$.ajax({
			"url" : base + "/userSign/sign.do",
			"type" : "POST",
		    "dataType" : "json",
		    "contentType" : "application/x-www-form-urlencoded",
			success : function(result, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {// 正常响应
					fadeAlert('签到成功',"连续签到有惊喜,明天记得再来哟~");
				} else if (ceipstate == 2) {// 后台异常
					fadeAlert('消息',result.msg);
				} else if (ceipstate == 3) { // 业务异常
					fadeAlert('消息',result.msg);
				}
			}
		});
  },
  signNotices : function(){
	  $.ajax({
		  "url" : base + '/index/newNotices.do',
			"type" : "POST",
		    "dataType" : "json",
		    "contentType" : "application/x-www-form-urlencoded",
			success : function(result, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {// 正常响应
					
				} else if (ceipstate == 2) {// 后台异常
					fadeAlert('消息',result.msg);
				} else if (ceipstate == 3) { // 业务异常
					fadeAlert('消息',result.msg);
				}
			}
	  })
  }
 
};


