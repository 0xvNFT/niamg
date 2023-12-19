define(['eChart', 'jquery', 'bootstrap', 'Fui', 'fui_table'], function (echarts) {
    function depositFormatter(value,row,index) {
        return moneyFormatter((row.depositArtificial + row.depositAmount));
    }
    function moneyFormatter(value, row, index) {
        if (value === undefined) {
            return "<span class='text-primary'>0.00</span>";
        }
        if (value > 0) {
            return ['<span class="text-danger">', '</span>']
                .join(Fui.toDecimal(value, 2));
        }
        return ['<span class="text-primary">', '</span>']
            .join(Fui.toDecimal(value, 2));
    }

    function initChart(curData, $con) {
        var echart = echarts.init($con.find(".canavsShow").get(0));
		echart.setOption({tooltip: {trigger:'axis',axisPointer:{type: 'shadow'}},
    		legend:{data: [Admin.earnLose,Admin.prizeAmount]},
    		grid:{left:'3%',right:'4%',bottom:'3%',top:10,containLabel:true},
    		xAxis:[{type:'value'}],
    		yAxis:[{type:'category',axisTick:{show:false},data:[Admin.today,Admin.yesterday]}],
    		series:[
				{name: Admin.earnLose,type:'bar',label:{show: true,position: 'inside'},data: [curData.profitTo||0,curData.profitYes||0]},
        		{name: Admin.prizeAmount,type:'bar',label:{show:true},data: [curData.winTo||0, curData.winYes || 0]}
    		]});
    }

    return {
        render: function (game) {
            var $con = $("#report_comprehensive_container_id");
            $.ajax({
                url: baseInfo.adminBaseUrl + "/monthReport/list.do",
                success: function (result) {
                    if (result.msg) {
                        layer.msg(result.msg);
                        return;
                    }
                    $con.find(".betNumTdy").html(Fui.toDecimal(result.betTo || 0, 2));
                    $con.find(".betNumYes").html(Fui.toDecimal(result.betYes || 0, 2));
                    if (result.profitTo > 0) {
                        $con.find(".bunkoTdy").addClass("text-danger").html(Fui.toDecimal(result.profitTo || 0, 2));
                    } else {
                        $con.find(".bunkoTdy").addClass("text-primary").html(Fui.toDecimal(result.profitTo || 0, 2));
                    }
                    if (result.profitYes > 0) {
                        $con.find(".bunkoYes").addClass("text-danger").html(Fui.toDecimal(result.profitYes || 0, 2));
                    } else {
                        $con.find(".bunkoYes").addClass("text-primary").html(Fui.toDecimal(result.profitYes || 0, 2));
                    }
                    addTable(result,$con);
                    initChart(result, $con);
					addMonthTable(result.monthMoneyData,$con);
                }
            });
			function addMonthTable(data,$con){
				var trs = '';
                $.each(data, function (index, m) {
                    trs += '<tr><td>' + m.statDateStr + '</td>'
                        + '<td>' + m.betAmount + '</td>';
					if(game.live){
						trs+= '<td>' + m.liveProfit + '</td>';
					}
					if(game.egame){
						trs+= '<td>' + m.egameProfit + '</td>';
					}
					if(game.sport){
						trs+= '<td>' + m.sportProfit + '</td>';
					}
					if(game.chess){
						trs+= '<td>' + m.chessProfit + '</td>';
					}
					if(game.esport){
						trs+= '<td>' + m.esportProfit + '</td>';
					}
					if(game.fishing){
						trs+= '<td>' + m.fishingProfit + '</td>';
					}
					if(game.lottery){
						trs+= '<td>' + m.lotProfit + '</td>';
					}
                    trs += '</tr>';
                });

                $con.find("#monthReportStatMonthTr").html(trs);
			}
            function addTable(data,$con) {
				var tds="<td>"+data.totalNum+"</td>"
					+"<td>"+data.proxyNum+"</td>"
					+"<td>"+data.memberNum+"</td>"
					+"<td>"+data.onlineNum+"</td>"
					+"<td>"+moneyFormatter(data.money)+"</td>";
                $con.find("#monthReportStatTr").html(tds);

				tds="<tr><td>"+Admin.today+"</td><td>"+data.todayReg+"</td>";
				if(data.today){
					tds=tds+"<td>"+moneyFormatter(data.today.liveBetAmount)+"</td>"
					+"<td>"+moneyFormatter(data.today.liveWinAmount)+"</td>"
					+"<td>"+moneyFormatter(data.today.depositAmount+data.today.depositArtificial)+"</td>"
					+"<td>"+moneyFormatter(data.today.withdrawAmount)+"</td>"
					+"<td>"+moneyFormatter(data.today.proxyRebateAmount)+"</td>"
					+"<td>"+moneyFormatter(data.today.liveRebateAmount)+"</td>";
				}else{
					tds=tds+"<td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td>";
				}
				tds=tds+"</tr><tr><td>"+Admin.yesterday+"</td><td>"+data.yesterdayReg+"</td>";
				if(data.yes){
					tds=tds+"<td>"+moneyFormatter(data.yes.liveBetAmount)+"</td>"
					+"<td>"+moneyFormatter(data.yes.liveWinAmount)+"</td>"
					+"<td>"+moneyFormatter(data.yes.depositAmount+data.yes.depositArtificial)+"</td>"
					+"<td>"+moneyFormatter(data.yes.withdrawAmount)+"</td>"
					+"<td>"+moneyFormatter(data.yes.proxyRebateAmount)+"</td>"
					+"<td>"+moneyFormatter(data.yes.liveRebateAmount)+"</td></tr>";
				}else{
					tds=tds+"<td>0</td><td>0</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>";
				}
				$con.find("#monthReport2DaysTr").html(tds);
            }
        }
    };
});
