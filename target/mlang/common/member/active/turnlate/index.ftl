<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${pageTitle}-<@spring.message "admin.lucky.round"/></title>
<meta name="keywords" content="${keywords}">
<meta name="description" content="${description}">
<#assign res="${base}/common/member/active/turnlate">
<link rel="stylesheet" href="${res }/css/bootstrap3/css/bootstrap.min.css">
<link href="${res}/css/style.css?v=1.0.6" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${base}/common/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${res}/js/awardRotate.js?v=1.0.3"></script>
<script src="${base}/common/lang/${language}.js?v=5"></script>
    <!-- 弹窗 -->
    <div class="sign-info">
        <div class="text-center text-green font18 sign-status"><strong id="fade-sign-info"></strong></div>
        <div class="text-center ptb15" id="fade-sign-body"></div>
        <div id="fade-sign-img"></div>
        <div class="text-center"><button class="btn btn-login fade-sign-know"><@spring.message "admin.deposit.handle.money.confirm.ok"/></button></div>
    </div>
    <script src="${base}/common/js/jquery.SuperSlide.2.1.1.js"></script>

    <script>
        <#if !isNotTurnlate>
        $('div.sign-info #fade-sign-info').html(Admin.message);
        $('div.sign-info #fade-sign-body').html(Admin.activityNotStart);
        $(".sign-info").fadeIn();
        $(".fade-sign-know").click(function () {
            window.close();
        });
        </#if>
    </script>
<script>

	var isLogin = '${isLogin}';
	var base = '${base}';
	<#if isLogin>
	//var awards = ${awards};
	var awardsColor = ${awardsColor};
	var awardsScore =  parseInt('${awardsScore}');
	var activeId = '${activeId}';
	var awardType = ${awardsType};
    var awardsName = ${awardsName};
    var awardsNameOther = '${awardsNameOther}';
    var awardImagesTwo = ${awardImagesTwo};
    if(${awardsType} == 3){
        var awardsType = ${awardsType};
        var productImage = ${productImg};
        awardsName = ${awardsName};
    }

   /* if(awardType == 1) {
        var awardsTypeOther =${awardsTypeOther};
        awardsNameOther = '${awardsNameOther}';
    }
    if(awardType != 3) {
        var awardsType = ${awardsType};
        var awardsNameOther = '${awardsNameOther}';
    }*/
	</#if>
</script>
<script type="text/javascript" src="${res}/js/turnlate.js?v=1.0.78"></script>
</head>
<body class="keBody">

<div class="kePublic">
    <#if backImg!>
        <img class="banner-img" src="${backImg}" alt="">
    <#else>
        <img class="banner-img" src="${res}/images/${language}/turbanner_${language}.png" alt="">
    </#if>

<!--效果html开始-->
    <div style="" id="divOne">
        <img src="${res}/images/1.png" id="shan-img" style="display:none;" />
        <img id="shan-img-two" style="display:none;" />
        <#--<img id="shan-img-three" style="display:none;" />-->
        <img src="${res}/images/2.png" id="sorry-img" style="display:none;" />
        <div class="dzp-tips">
            <p><@spring.message "admin.jack.item.seed"/></p>
            <br/><p><@spring.message "admin.money.values"/>: R$${userMoney} ,
                <span id="userScore"><@spring.message "admin.scores"/>：${userScore}</span></p>
        </div>
        <div class="dial-body">
            <div class="dial-body-l">
                <div class="banner">
                    <div class="turnplate" style="background-image:url(${res}/images/turnplate-bg.png);background-size:100% 100%;">
                        <canvas class="item" id="wheelcanvas" width="422px" height="422px"></canvas>

                        <img class="pointer" src="${res}/images/${language}/turnplate-pointe_${language}.png"/>
                    </div>
                </div>
            </div>
            <div class="dial-body-r">
                <div class="slide">
                    <div class="slide-r"><@spring.message "admin.round.jack.record"/></div>
                    <div class="slide-h"></div>
                    <div class="slide-c">
                        <div class="slide-c-h">
                            <span><@spring.message "admin.jack.list.winner"/></span>
                            <span class="rs"><@spring.message "admin.recent"/><span id="number-span">0</span><@spring.message "admin.person"/></span>
                        </div>
                        <div class="slide-list-box">
                            <ul id="slide-list-ul">

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div id="recordHistory">
                <div id="dateSearchForm" style="float: right;margin-right: 20px;margin-bottom:5px;">
                    <span class="date">
                        <input type="text" id="startTime"  style="width: 155px;height: 30px;color:#fff;" name="startTime" value="${searchDate}"placeholder="<@spring.message "admin.startTime"/>">
                    </span>
                    <span style="color:#fff;">--</span>
                    <span class="date">
                        <input type="text" id="endTime"  style="width: 155px;height: 30px;color:#fff;" name="endTime" value="${searchDate}"placeholder="<@spring.message "admin.endTime"/>">
                    </span>
                    <span class="nametitle"> <input type="text" id="productName"  style="width: 130px;height: 30px" name="productName" value="${productName}"placeholder="<@spring.message "admin.jiangpinmingcheng"/>" autocomplete="off"></span>
                    <span><a id="dateSearch" href="javascript:void(0)" class="btn btn-primary" data-bind="click:searchData"><@spring.message "admin.chaxun"/></a></span>
                </div>
                <div id="table" style="height: 500px;overflow-y: auto;display: inline-block">
                    <table id="recordHistoryList">
                        <thead>
                        <tr>
                            <th style="width: 8%"><@spring.message "admin.yonghu"/></th>
                            <th style="width: 10%"><@spring.message "admin.jiangpinmingcheng"/></th>
                            <th style="width: 8%"><@spring.message "admin.zhongjiangmianzhi"/></th>
                            <th style="width: 8%"><@spring.message "admin.jiangpinleixing"/></th>
                            <th style="width: 8%"><@spring.message "admin.chulizhuangtai"/></th>
                            <th style="width: 20%"><@spring.message "admin.zhongjiangshijian"/></th>
                            <th style="width: 10%"><@spring.message "admin.beizhu"/></th>
                        </tr>
                        </thead>
                        <tbody id="recordHistoryBody"></tbody>
                    </table>
                </div>
                <div id="closeBtn" style="text-align: center;"> <span class="btn btn-primary open-dialog" style="width: 130px;background-color:#d03333;border-radius:7px;border-color:#d03333" ><@spring.message "admin.close"/></span></div>
            </div>
        </div>
        <div class="dzp-title">
            <p><@spring.message "admin.jack.rule"/></p>
        </div>
        <div class="regulation-content">
        	<#if activeRole?has_content>
            <div class="regulation-content-l">
                <div class="regulation-content-title">
                    <h4><@spring.message "admin.jack.qualifications"/></h4>
                </div>
                <div class="content-text">${activeRole}</div>
            </div>
            </#if>
            <#if activeHelp?has_content>
            <div class="regulation-content-c">
                    <div class="regulation-content-title">
                        <h4><@spring.message "admin.act.rule"/></h4>
                    </div>
                    <div class="content-text">${activeHelp}</div>
            </div>
            </#if>
            <#if activeRemark?has_content>
            <div class="regulation-content-r">
                <div class="regulation-content-title">
                    <h4><@spring.message "admin.act.statement"/></h4>
                </div>
                <div class="content-text">${activeRemark}</div>
            </div>
            </#if>
        </div>
    </div>
<!--效果html结束-->
<div class="clear"></div>
</div>


<!-- 未登录弹窗 -->
<div class="unlogin">
	<div class="text-center text-green font18 sign-status"><strong id="fade-sign-info"></strong></div>
	<div class="text-center ptb15"></div>
	<div class="text-center"><button class="btn btn-login" id="fade-sign-body" onclick="location.href = '${base}/index.do'"></button></div>
</div>




</body>
<script type="text/javascript" src="/common/member/js/laydate/laydate.js"></script>
<script>
	if(isLogin && isLogin == 'true'){
		trunRecord();//中奖记录
	}else{
		fadeUnloginAlert('<@spring.message "admin.not.login.haven"/>!','<a href="javascript:;"><@spring.message "admin.go.login"/></a></button>');
	}
    laydate.render({
        elem: '#startTime',
        type: 'datetime',
        lang: 'en',
        theme: 'molv'
    });
    laydate.render({
        elem: '#endTime',
        type: 'datetime',
        lang: 'en',
        theme: 'molv'
    });
    function trunRecord(){
        if(!activeId){
            return;
        }
        var awardHtml = new Array();
        $.ajax({
            "url" : base + "/userTurnlate/records.do",
            "type" : "POST",
            "dataType" : "json",
            "data" : {
                'activeId' : activeId
            },
            "contentType" : "application/x-www-form-urlencoded",
            success : function(result, textStatus, xhr) {
                var ceipstate = xhr.getResponseHeader("ceipstate");
                if (!ceipstate || ceipstate == 1) {// 正常响应
                    if(result){
                        for(var i in result){
                            awardHtml.push('<li>');
                            awardHtml.push('<span class="result-span">'+result[i].username+'</span>');
                            var im = result[i].itemName;
                            if(im){
                            	awardHtml.push('<span class="productName-span">'+result[i].itemName+'</span>');
                            }else{
                            	awardHtml.push('<span class="productName-span">'+result[i].winMoney+'</span>');
                            }
                            awardHtml.push('<span class="createDatetime-span">'+timestampToTime(result[i].winTime)+'</span></li>');
                        }
                        $('#number-span').html(result.length);
                        $('#slide-list-ul').html(awardHtml.join(''));
                        //文字滚动
                        $(".slide-c").slide({
                            mainCell:".slide-list-box ul",
                            autoPage:true,
                            effect:"topMarquee",
                            autoPlay:true,
                            vis:5,
                            interTime:50
                        });
                    }
                } else if (ceipstate == 2) {// 后台异常
                    fadeAlert('消息',result.msg);
                } else if (ceipstate == 3) { // 业务异常
                    fadeAlert('消息',result.msg);
                }
            }
        });
    }

    $(".slide-r").click(function(){
      //  $('#recordHistoryBody').empty();
     //   $('#recordHistoryBody').html("");
        turnlateRecord();
    });

    $("#closeBtn").click(function(){
        $('#recordHistory').hide();
       // $('#recordHistoryList').html("");
     //   $('#recordHistoryBody').html("");
       // $("#endTime").val("");
      //  $("#startTime").val("");
     //   $("#productName").val("");
    });
    $("#dateSearch").click(function(){
       $('#recordHistoryBody').empty();
        turnlateRecord();
    });

    function turnlateRecord (type) {
        if(!activeId){
            return;
        }
        var endTime =$("#endTime").val();
        var startTime =$("#startTime").val();
        var productName =$("#productName").val();

        $.ajax({
            "url" : base + "/userTurnlate/turnlateRecordList.do",
            "type" : "POST",
            "dataType" : "json",
            "data" : {
                'activeId' : activeId,
                'endTime' : endTime,
                'startTime' : startTime,
                'productName' : productName,
                'pageNumber':1,
                'pageSize':100000
            },
            "contentType" : "application/x-www-form-urlencoded",
            success : function(result, textStatus, xhr) {
                var ceipstate = xhr.getResponseHeader("ceipstate");
                if (!ceipstate || ceipstate == 1) {// 正常响应\
                    $('#recordHistory').fadeIn();
                    var html=" ";
                    if(result){
                        for(var i in result){
                            var data =result[i];
                         //   var type = data.awardType == 1 ?'不中奖' : data.awardType == 2 ? '现金' : data.awardType == 3 ? '奖品':'积分';
                            var type = data.awardType == 1 ?'no win' : data.awardType == 2 ? 'cash' : data.awardType == 3 ? 'prize  ':'integral';
                        //    var status = data.status == 1 ?'未处理' : data.status == 2 ? '已处理' :'处理失败';
                            var status = data.status == 1 ?'untreated' : data.status == 2 ? 'processed' :'failed';
                            var giftName = data.giftName == undefined ?'' : data.giftName;

                            var remark = data.remark == undefined ?'' : data.remark =='自动结算'?'auto':data.remark;
                            html +="<tr><th>"+data.username+"</th><th>"+giftName+"</th><th>"+data.awardValue+"</th><th>"+type+"</th><th>"+status+"</th><th>"+timestampToTime(data.createDatetime)+"</th><th>"+remark+"</th></tr>";
                        }
                    }
                    $('#recordHistoryBody').empty();
                    $('#recordHistoryBody').html(html);
                } else if (ceipstate == 2) {// 后台异常
                    fadeAlert('消息',result.msg);
                } else if (ceipstate == 3) { // 业务异常
                    fadeAlert('消息',result.msg);
                }
            }
        });
    };
    function timestampToTime(timestamp) {
        var date = new Date(timestamp);
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
        var h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
        var m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
        var s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());

        strDate = Y+M+D+h+m+s;
        return strDate;

    }
     function formatDatetime(time) {
        if (!time) {
            return "";
        }
        var date = new Date(time);
        return date.format("yyyy-MM-dd hh:mm:ss");
    }



    var starMin = new Date();
    starMin.setHours(0, 0, 0);
    starMin.setDate(starMin.getDate() - 40);
    startEvent = function (data, event) {
        laydate.render({
            elem: '#start'
            , eventElem: '.date-input-icon1'
            , trigger: 'click'
            ,type: 'datetime'
        });
    };
    endEvent = function (data, event) {
        laydate.render({
            elem: '#end'
            ,eventElem: '.date-input-icon2'
            , trigger: 'click'
            ,type: 'datetime'
        });
    };
    setDate = function (offset) {
        var that = this;
        var o = {};
        var endDate = new Date();
        if (offset !== 0){endDate.setDate(new Date().getDate() + offset);}
        var startDate = new Date(endDate.getTime());
        o.from = startDate.format('yyyy-MM-dd') + ' 00:00:00';
        o.to = endDate.format('yyyy-MM-dd') + ' 23:59:59';
        $("#start").val(o.from);
        $("#end").val(o.to);
        searchData();
    };

    setWeek = function (offset) {
        var that = this;
        var date = new Date();
        var w = date.getDay();
        w = w === 0 ? 7 : w;
        var o = {};
        date.setDate(date.getDate() - w + 1 + 7 * offset);
        o.from = date.format('yyyy-MM-dd') + ' 00:00:00';
        date.setDate(date.getDate() + 6);
        o.to = date.format('yyyy-MM-dd') + ' 23:59:59';
        $("#start").val(o.from);
        $("#end").val(o.to);
        searchData();
    };

    setMonth = function (offset) {
        var that = this;
        var date = new Date(), o = {};
        ///date.setDate(new Date().getDate());
        date.setDate(1);
        date.setMonth(date.getMonth() + offset);
        o.from = date.format('yyyy-MM-dd') + ' 00:00:00';
        date.setMonth(date.getMonth() + 1);
        date.setDate(date.getDate()-1);
        o.to = date.format('yyyy-MM-dd') + ' 23:59:59';
        $("#start").val(o.from);
        $("#end").val(o.to);
        searchData();
    };

    searchData = function () {
        $("#dateSearchForm").submit();
    }
</script>
</html>
</html>