requirejs(['eChart'], function (echarts) {
    var memberReportChart = echarts.init(document.getElementById('memberReportChart'));
    var betReportChart = echarts.init(document.getElementById('betReportChart'));
    var financeReportChart = echarts.init(document.getElementById('financeReportChart'));
    var winLoseReportChart = echarts.init(document.getElementById('winLoseReportChart'));
    var thirdQuotaAlertTimer;
    function memberDataSet(data) {
        memberReportChart.setOption({
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data:[Admin.proxyPromoteReg,Admin.officialReg,Admin.firstDepositCash,Admin.secondDepositCash]
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : [Admin.today,Admin.yesterday,Admin.dayAfterDay]
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:Admin.proxyPromoteReg,
                    type:'bar',
                    stack:'reg',
                    data:[data.proxyPromo[0],data.proxyPromo[1],data.proxyPromo[2]]
                },
                {
                    name:Admin.officialReg,
                    type:'bar',
                    stack:'reg',
                    data:[data.officialRe[0],data.officialRe[1],data.officialRe[2]]
                },
                {
                    name:Admin.firstDepositCash,
                    type:'bar',
                    data:[data.firstDeposit[0],data.firstDeposit[1],data.firstDeposit[2]]
                },
                {
                    name:Admin.secondDepositCash,
                    type:'bar',
                    data:[data.secDeposit[0],data.secDeposit[1],data.secDeposit[2]]
                }
            ]
        });
    }

    function betNumDataSet(data) {


        var arr1=[Admin.liveGame+Admin.today,Admin.liveGame+Admin.yesterday,Admin.liveGame+Admin.dayAfterDay, Admin.fish+Admin.today,Admin.fish+Admin.yesterday,Admin.fish+Admin.dayAfterDay, Admin.chess+Admin.today,Admin.chess+Admin.yesterday,Admin.chess+Admin.dayAfterDay, Admin.sports+Admin.today,Admin.sports+Admin.yesterday,Admin.sports+Admin.dayAfterDay, Admin.gameLottery+Admin.today,Admin.gameLottery+Admin.yesterday,Admin.gameLottery+Admin.dayAfterDay, Admin.electronic+Admin.today,Admin.electronic+Admin.yesterday,Admin.electronic+Admin.dayAfterDay, Admin.eleSports+Admin.today,Admin.eleSports+Admin.yesterday,Admin.eleSports+Admin.dayAfterDay];
        var arr2=[data.liveBetAmount[0],data.liveBetAmount[1],data.liveBetAmount[2],data.fishingBetAmount[0],data.fishingBetAmount[1],data.fishingBetAmount[2],data.chessBetAmount[0],data.chessBetAmount[1],data.chessBetAmount[1],data.sportBetAmount[0],data.sportBetAmount[1],data.sportBetAmount[2],data.lotBetAmount[0],data.lotBetAmount[1],data.lotBetAmount[2],data.egameBetAmount[0],data.egameBetAmount[1],data.egameBetAmount[2],data.esportBetAmount[0],data.esportBetAmount[1],data.esportBetAmount[2]];

        betReportChart.setOption({
            backgroundColor: '#404a59',  //整体背景
            title: {
                text: Admin.allGameLinePic,
                top: '10',
                left: 'center',
                textStyle: {
                    color: '#fff',
                    fontWeight: 100,
                    fontSize: 20
                }
            },
            legend: {
                data: [{            //必须与series一一对应
                    name: Admin.onePay
                }],
                orient: 'vertical',
                bottom: 0,
                right: 0,
                textStyle: {
                    color: '#fff'
                }
            },
            color: ['#c23531', '#ffc917', '#61a0a8', '#00ffec', '#91c7ae','#c23531', '#ffc917', '#61a0a8', '#00ffec', '#91c7ae','#c23531', '#ffc917', '#61a0a8', '#00ffec', '#91c7ae','#c23531', '#ffc917', '#61a0a8', '#00ffec', '#91c7ae', '#61a0a8'],
            tooltip: {
                trigger: 'axis',

                axisPointer: {
                    show:true,
                    type: 'cross',
                    crossStyle: {     //'cross'时候的样式
                        color: 'blue'
                    },
                    label: {
                        backgroundColor: '#ffc917',     //坐标轴指示器的文本标签样式
                        textStyle: {
                            color: '#fff'
                        }
                    }
                }
            },
            //直角坐标系
            grid: {
                left: '3%',     //坐标系到左边的距离
                right: '10%',
                top:70,
                bottom: '5%',
                containLabel: true //是否包含坐标轴的刻度标签。
            },
            yAxis: {
                type: 'value',
                splitLine: {                  //坐标轴在 grid 区域中的分隔线设置。
                    show: true,
                    lineStyle:{
                        color:'#fff'
                    }
                },
                axisLine:{                        //坐标轴轴线相关设置。
                    lineStyle:{
                        color:'#9fdabf'           //坐标轴颜色
                    }
                },
                axisTick: { show: false },     //坐标轴刻度相关设置。
                axisLabel: {                    //坐标轴标签的相关设置。轴下面的字
                    show: true,
                    color: 'cyan'
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,   //坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。
                data: arr1.reverse(),
                nameGap:0,
                splitLine: {                  //坐标轴在 grid 区域中的分隔线设置。
                    show: true,
                    lineStyle:{
                        color:'#fff'
                    }
                },
                axisLine:{                        //坐标轴轴线相关设置。
                    lineStyle:{
                        color:'#9fdabf'           //坐标轴颜色
                    }
                },
                axisTick: { show: false },     //坐标轴刻度相关设置。
                axisLabel: {                    //坐标轴标签的相关设置。轴下面的字
                    show: true,
                    color: 'cyan'
                }
            },

            series: [
                {
                    name: Admin.betNum,
                    type: 'line',
                    data: arr2.reverse(),
                    // step:true,      //是否是阶梯线图
                    // smooth: true,   //是否平滑曲线显示。
                    label:{         //图形上的文本标签
                        normal:{
                            show:false
                        }
                    },
                    itemStyle:{     //折线拐点标志的样式。
                        normal:{
                            shadowBlur: 10
                        }
                    },
                    lineStyle:{     //线条样式。
                        normal:{
                            width: 10
                        }
                    },
                    areaStyle:{     //区域填充样式。
                        normal:{
                            color: 'green'
                        }
                    },
                    markPoint: {
                        data: [
                            {type: 'max', name: Admin.max},
                            {type: 'min', name: Admin.min}
                        ]
                    }

                }
            ],
            animation: true
        });

    }


    function financeDataSet(data) {
        financeReportChart.setOption({
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data:[Admin.depositCash,Admin.drawMoneyCash,Admin.memberRebate,Admin.proxyPoint]
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : [Admin.today,Admin.yesterday,Admin.dayAfterDay]
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:Admin.depositCash,
                    type:'bar',
                    data:[data.depositAmount[0],data.depositAmount[1],data.depositAmount[2]]
                },
                {
                    name:Admin.drawMoneyCash,
                    type:'bar',
                    data:[data.withdrawAmount[0],data.withdrawAmount[1],data.withdrawAmount[2]]
                },
                {
                    name:Admin.memberRebate,
                    type:'bar',
                    data:[data.liveRebateAmount[0],data.liveRebateAmount[1],data.liveRebateAmount[2]]
                },
                {
                    name:Admin.proxyPoint,
                    type:'bar',
                    data:[data.proxyRebateNum[0],data.proxyRebateNum[1],data.proxyRebateNum[2]]
                }
            ]
        });

        /*if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }*/
    }

    function winLoseDataSet(data) {

        winLoseReportChart.setOption({

            backgroundColor: '#404a59',  //整体背景
            title: {
                text: Admin.allGameCountDays,
                left: 'center',
                top: 10,
                textStyle: {
                    color: '#fff'
                }
            },
            tooltip: {
                trigger: 'axis',           //提示框组件
                axisPointer: {
                    type: 'shadow'
                }
            },
            legend: {
                data: [Admin.winOrLose],    //必须与series一一对应
                orient: 'vertical',
                top: 'bottom',
                left: '10',
                textStyle: {
                    color: '#fff'
                }
            },
            //直角坐标系
            grid: {
                show:false,
                left: 100,                  //坐标系到左边的距离
                right: 45,                   //坐标系到右边的距离
                bottom:0,                   //坐标系到下边的距离
                containLabel: true           //是否包含坐标轴的刻度标签。
            },
            xAxis: {
                type: 'category',             //类目轴
                data: [Admin.chess, Admin.fish,Admin.liveGame, Admin.electronic,Admin.gameLottery , Admin.eleSports, Admin.sports],
                splitLine: {                  //坐标轴在 grid 区域中的分隔线设置。
                    show: true,
                    lineStyle:{
                        color:'#fff'
                    }
                },
                axisLine:{                        //坐标轴轴线相关设置。
                    lineStyle:{
                        color:'#9fdabf'           //坐标轴颜色
                    }
                },
                axisTick: { show: false },     //坐标轴刻度相关设置。
                axisLabel: {                    //坐标轴标签的相关设置。轴下面的字
                    show: true,
                    color: 'cyan'
                }
            },
            yAxis: {
                type: 'value',                //数值轴
                splitLine: {                  //坐标轴在 grid 区域中的分隔线设置。
                    show: true,
                    lineStyle:{
                        color:'#fff'
                    }
                },
                axisLine:{                    //坐标轴轴线相关设置。
                    lineStyle:{
                        color:'#9fdabf'           //坐标轴颜色
                    }
                },
                axisTick: { show: false },     //坐标轴刻度相关设置。
                axisLabel: {                    //坐标轴标签的相关设置。轴下面的字
                    show: true,
                    rotate:50,                  //刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠 旋转的角度从 -90 度到 90 度。
                    color: 'cyan'
                }

            },
            series: [
                {
                    name: Admin.winOrLose,
                    type: 'bar',
                    data: [data.chessWinOrLose,data.fishingWinOrLose,data.liveWinOrLose,data.egameWinOrLose,data.lotteryWinOrLose,data.esportWinOrLose,data.sportWinOrLose],
                    silent: true,
                    barWidth:20,        //柱条宽度
                    barGap:'1%',        //柱条间距
                    itemStyle: {
                        normal: { color: '#ffc917' }
                    }
                }
            ]


        });

    }
    function getQuotaView() {
        var thirdQuotaAlertMoney = $("#thirdQuotaAlertMoney").val();
        console.log("额度提醒=" + thirdQuotaAlertMoney);
        $.ajax({
            url: baseInfo.adminBaseUrl + '/transferRecord/getThirdQuota.do',
            success: function (data) {
                if (data) {
                    var htm = "<tr><th>"+Admin.platSystem+"</th><th>"+Admin.totalValues+"</th><th>"+Admin.haveUsed+"</th><th>"+Admin.remainValues+"</th>";
                    var alertHtm = '';
                    $.each(data, function (index, quota) {
                        htm = handleQuotaTr(quota.name, quota.nameCn, htm, quota);
                        if (quota.surplus < thirdQuotaAlertMoney) {
                            alertHtm += quota.nameCn + ".";
                        }
                    });
                    if (alertHtm != '') {
                        alertHtm += Admin.valueLessThan + thirdQuotaAlertMoney + ','+Admin.timesPayValues+'.';
                        var title = Admin.thirdValuesShow;
                        activeNarn('success',alertHtm,title);
                    }
                    $("#realSportQuotaView").html(htm);
                }else {
                    console.log("获取真人电子额度失败1data = " + data);
                    layer.msg(Admin.failMessage);
                }
            },
            errorFn: function (data, ceipstate) {
                //layer.msg("获取真人电子额度失败2");
            },
            error: function () {
                layer.msg(Admin.failCashCatch);
            }
        });
        thirdQuotaAlertTimer=setTimeout(getQuotaView, 1000*60*5);//5分钟获取一次
    }
	getQuotaView();
    function handleQuotaTr(gameName, gameNameCn, htm, data) {
        htm += '<tr><th>' + gameName+'('+gameNameCn+')</th><td><span class="label label-info">' + data.quota + '</span></td>';
        htm += '<td><span class="label label-success">' + data.used + '</span></td>';
        htm += '<td><span class="label ';
        if (data.surplus <= 5000) {
            htm += 'label-danger">' + data.surplus + '</span></td></tr>';
        } else {
            htm += 'label-warning">' + data.surplus + '</span></td></tr>';
        }
        return htm;
    }
    function activeNarn(type,txt,title) {
        naranja()[type]({
            title: title,
            text: txt,
            timeout: 20000,//'keep' 为keep时，弹窗不消失
            buttons: [{
                text: Admin.close,
                click: function (e) {
                    e.closeNotification()
                }
            }]
        })
    }


    function getChart() {
        $.ajax({
            type:'GET',
            url:baseInfo.adminBaseUrl +'/indexCharts.do',
            dataType:"json",
            success: function(json){
                memberDataSet(json.member);
                betNumDataSet(json.bet);
                winLoseDataSet(json.monthData);
                financeDataSet(json.finance);


            }
        });
    }
    $(function() {
        //if(baseInfo.haveIndexData === "true") {
            getChart();

        $(".real-quota-flush").click(function () {
            getQuotaView();
        })
       // }
        /*if (baseInfo.onOffReal === "true" || baseInfo.onOffEgame === "true") {
            getQuotaView();
        }
        $(".real-quota-flush").click(function () {
            getQuotaView();
        })*/
    })
});
