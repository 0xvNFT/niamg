define(['jquery', 'bootstrap', 'Fui', 'fui_table'], function () {
    return {
        render: function (game) {
			var $form = $("#report_global_form_id");
		    $form.find("#globalReportUserDegreeId").change(function () {
		        var types = $(this).val();
		        if (types) {
		            types = types.join(",");
		            $form.find("#globalReportUserDegreeIdStr").val(types);
		        } else {
		            $form.find("#globalReportUserDegreeIdStr").val("");
		        }
		    });
		    $form.find(".search-btn").click(function () {
		        $form.find(".search-btn").prop("disabled", true).html('<img src="'+baseInfo.baseUrl+'/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
		        $.ajax({
		            url: baseInfo.adminBaseUrl+"/globalReport/list.do",
		            data: $form.serialize(),
		            success: function (result) {
                        var str="<span>"+Admin.search+"</span>";
		                $form.find(".search-btn").prop("disabled", false).html(str);
		                if (result.msg) {
		                    layer.msg(result.msg);
		                    return;
		                }
		                addTables(result);
		            },
		            errorFn: function (obj) {
                        var str="<span>"+Admin.search+"</span>";
		                $form.find(".search-btn").prop("disabled", false).html(str);
		            }
		        });
		        return false;
		    }).click();
 			function moneyFormatter(value, row, index) {
                if (value === undefined) {
                    return "<span class='text-primary'>0.00</span>";
                }
                if (value > 0) {
                    return '<span class="text-danger">'+Fui.toDecimal(value, 4)+'</span>'
                }
                return '<span class="text-primary">'+Fui.toDecimal(value, 4)+'</span>'
            }
		    function addTables(data){
		    	addDepositWithdraw(data);
				addGlobalStat(data);
				addActiveStat(data);
				addBetWinMoney(data);
				addAllMoney(data);
		    }
			var $depositWithdraw=$("#report_global_deport_withdwar_tr"),
				$globalStat=$("#report_global_stat_tr"),
				$activeStat=$("#report_global_active_tr"),
				$betWinMoney=$("#report_global_bet_win_tb"),
				$allMoney=$("#report_global_allbw_tr");
			function addDepositWithdraw(data){
				var tds="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/deposit/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.depositAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=1&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.depositArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/deposit/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.depositAmount + data.depositArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/withdraw/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.withdrawAmount) + "</a></td>";
				var profit = (data.depositAmount + data.depositArtificial) - data.withdrawAmount;
            	if (profit === undefined) {
                	tds+="<td><span class='text-primary'>0.00</span></td>";
            	}else{
					if (profit > 0) {
						tds+="<td><span class='text-danger'>"+Fui.toDecimal(profit, 4)+"</span></td>";
					}else{
						tds+="<td><span class='text-primary'>"+Fui.toDecimal(profit, 4)+"</span></td>";
					}
				}
				tds=tds+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=2&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.withdrawArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/deposit/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.depositHandlerArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=90&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.giftOtherAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=35&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.subGiftAmount) + "</a></td>";
				$depositWithdraw.html(tds);
				Fui.initUI($depositWithdraw);
			}
			function addGlobalStat(data){
				var tds = '';
				if (data.betUserNum === undefined || data.depositUserNum === undefined || data.drawUserNum === undefined) {
					tds="<td><span class='text-primary'>0</span></td>"
						+"<td><span class='text-primary'>0</span></td>"
						+"<td><span class='text-primary'>0</span></td>";
				}else {
					tds="<td>"+data.betUserNum+"</td>"
						+"<td>"+data.depositUserNum+"</td>"
						+"<td>"+data.drawUserNum+"</td>";
				}
				$globalStat.html(tds);
			}
			function addActiveStat(data){
				var tds = '';
					if(data.proxyRebateUserNum === undefined || data.rebateUserNum === undefined){
						tds="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=12,13&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.proxyRebateAmount) + "</a></td>"
							+"<td><span class='text-primary'>0</span></td>"
							+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=10,11&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.liveRebateAmount) + "</a></td>"
							+"<td><span class='text-primary'>0</span></td>"
							+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=17&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.depositGiftAmount) + "</a></td>"
							+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=18,20,24,27&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.activeAwardAmount) + "</a></td>"
							+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=31&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.scoreToMoney) + "</a></td>"
							+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=23&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.redActiveAwardAmount) + "</a></td>";
					}else{
						tds="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=12,13&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.proxyRebateAmount) + "</a></td>"
						+"<td>"+data.proxyRebateUserNum+"</td>"
						+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=10,11&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.liveRebateAmount) + "</a></td>"
						+"<td>"+data.rebateUserNum+"</td>"
						+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=17&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.depositGiftAmount) + "</a></td>"
						+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=18,20,24,27&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.activeAwardAmount) + "</a></td>"
						+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=31&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.scoreToMoney) + "</a></td>"
						+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username={[name=\"username\"]:}&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=23&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.redActiveAwardAmount) + "</a></td>";
					}

				$activeStat.html(tds);
				Fui.initUI($activeStat);
			}
			function addBetWinMoney(data){
				var bet="<tr><th>"+Admin.onePay+"</th>";
				var dm=" <tr><th>"+Admin.validWeight+"</th>";
				var win="<tr><th>"+Admin.sendWin+"</th>";
				var profit="<tr><th>"+Admin.winOrLose+"</th>";
				if(game.live){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/liveRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.liveBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.liveBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.liveWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.liveWinOrLose)+"</td>";
				}
				if(game.egame){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/egameRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.egameBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.egameBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.egameWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.egameWinOrLose)+"</td>";
				}
				if(game.sport){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/sportRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.sportBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.sportBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.sportWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.sportWinOrLose)+"</td>";
				}
				if(game.chess){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/chessRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.chessBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.chessBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.chessWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.chessWinOrLose)+"</td>";
				}
				if(game.esport){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/esportRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.esportBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.esportBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.esportWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.esportWinOrLose)+"</td>";
				}
				if(game.fishing){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/fishingRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.fishingBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.fishingBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.fishingWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.fishingWinOrLose)+"</td>";
				}
				if(game.lottery){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/lotteryRecord/index.do?username={[name=\"username\"]:}&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.lotBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.lotBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.lotWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.lotteryWinOrLose)+"</td>";
				}
				$betWinMoney.html(bet+"</tr>"+dm+"</tr>"+win+"</tr>"+profit+"</tr>");
				Fui.initUI($betWinMoney);
			}
			function addAllMoney(data){
				var tds="<td>"+moneyFormatter(data.totalBetAmount)+"</td>"
					+"<td>"+moneyFormatter(data.totalWinAmount)+"</td>"
					+"<td>"+moneyFormatter(data.totalWinOrLose)+"</td>";
				$allMoney.html(tds);
			}
		}
	}
});