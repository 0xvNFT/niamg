define(['jquery', 'bootstrap', 'Fui', 'fui_table'], function () {
    return {
        render: function (game) {
			var $form = $("#report_member_data_form_id"),$container=$("#user_data_container_id");
		    $form.find(".search-btn").click(function () {
				 var it = $(this);
                if (it.prop("disabled") == true) {
                    return false;
                }
                it.prop("disabled", true).html('<img src="' + baseInfo.baseUrl + '/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
                var username = $form.find("[name='username']").val();
                if (!username) {
                    it.prop("disabled", false).html(Admin.search);
                    layer.msg(Admin.adminInputVipAccount);
					$container.hide();
                    return false;
                }
		        $.ajax({
		            url: baseInfo.adminBaseUrl+"/userData/list.do",
		            data: $form.serialize(),
		            success: function (result) {
		                it.prop("disabled", false).html(Admin.search);
		                if (result.msg) {
		                    layer.msg(result.msg);
							$container.hide();
		                    return;
		                }
		                addTables(result,username);
		            },
		            errorFn: function (obj) {
		                it.prop("disabled", false).html(Admin.search);
						$container.hide();
		            }
		        });
		        return false;
		    });
			if ($form.find("[name='username']").val()) {
                $form.find(".search-btn").click();
            }
 			function moneyFormatter(value, row, index) {
                if (value === undefined) {
                    return "<span class='text-primary'>0.00</span>";
                }
                if (value > 0) {
                    return '<span class="text-danger">'+Fui.toDecimal(value, 4)+'</span>'
                }
                return '<span class="text-primary">'+Fui.toDecimal(value, 4)+'</span>'
            }
		    function addTables(data,username){
		    	addBaseInfo(data,username);
				addDeposit(data,username);
				addWithdraw(data,username);
				addGiftTb(data,username);
				addBetWinMoney(data,username);
				addAllMoney(data);
				$container.show();
		    }
			var $base=$("#report_member_data_tb_base"),
				$deposit=$("#report_member_data_tb_deposit"),
				$withdraw=$("#report_member_data_tb_draw"),
				$gifttb=$("#report_member_data_tb_gift"),
				$betWinMoney=$("#report_member_data_tb_bet");
			function addBaseInfo(data,username){
				var tds="<td>" + data.username + "</td>"
					+"<td>" + (data.userType==120?Admin.proxy:Admin.member) + "</td>"
					+"<td>"+(typeof(data.proxyName) =='undefined'?'':data.proxyName) +"</td>"
					+"<td>"+data.createDatetime+"</td>"
					+"<td>"+moneyFormatter(data.money)+"</td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/betNumHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.betNum) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/betNumHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.drawNeed) + "</a></td>"
					+"<td>"+moneyFormatter(data.totalBetNum)+"</td>";
				$base.html(tds);
				Fui.initUI($base);
			}
			function addDeposit(data,username){
				var tds="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/deposit/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.depositAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=1'>" + moneyFormatter(data.depositArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/deposit/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.depositHandlerArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/deposit/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.lastDepositMoney) + "</a></td>"
					+"<td>"+(data.lastDepositTime||"")+"</td>"
					+"<td>"+(data.lastDepositRemark||"")+"</td>";
				$deposit.html(tds);
				Fui.initUI($deposit);
			}
			function addWithdraw(data,username){
				var tds="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/withdraw/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.withdrawAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=2'>" + moneyFormatter(data.withdrawArtificial) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=35'>" + moneyFormatter(data.subGiftAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/withdraw/index.do?username="+username+"&startTime={[name=\"startDate\"]:} 00:00:00&endTime={[name=\"endDate\"]:} 23:59:59'>" + moneyFormatter(data.lastDrawMoney) + "</a></td>"
					+"<td>"+(data.lastDrawTime||"")+"</td>"
					+"<td>"+(data.lastDrawRemark||"")+"</td>";
				$withdraw.html(tds);
				Fui.initUI($withdraw);
			}
			function addGiftTb(data,username){
				var tds="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=17'>" + moneyFormatter(data.depositGiftAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=18,20,24,27'>" + moneyFormatter(data.activeAwardAmount) + "</a></td>"
					+"<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/finance/moneyHistory/index.do?username="+username+"&startTime={[name=\"startDate\"]:}&endTime={[name=\"endDate\"]:} 23:59:59&moneyType=23'>" + moneyFormatter(data.redActiveAwardAmount) + "</a></td>"
					+"<td>" + moneyFormatter(data.liveRebateAmount) + "</td>";
				$gifttb.html(tds);
				Fui.initUI($gifttb);
			}
			function addBetWinMoney(data,username){
				var bet = "<tr><th>" + Admin.bet + "</th>";
				var dm = "<tr><th>" + Admin.liveBetNum + "</th>";
				var win="<tr><th>" + Admin.win + "</th>";
				var profit="<tr><th>" + Admin.profit + "</th>";
				if(game.live){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/liveRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.liveBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.liveBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.liveWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.liveWinOrLose)+"</td>";
				}
				if(game.egame){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/egameRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.egameBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.egameBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.egameWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.egameWinOrLose)+"</td>";
				}
				if(game.sport){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/sportRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.sportBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.sportBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.sportWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.sportWinOrLose)+"</td>";
				}
				if(game.chess){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/chessRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.chessBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.chessBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.chessWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.chessWinOrLose)+"</td>";
				}
				if(game.esport){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/esportRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.esportBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.esportBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.esportWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.esportWinOrLose)+"</td>";
				}
				if(game.fishing){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/fishingRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.fishingBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.fishingBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.fishingWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.fishingWinOrLose)+"</td>";
				}
				if(game.lottery){
					bet+="<td><a class='open-tab' href='" + baseInfo.adminBaseUrl + "/lotteryRecord/index.do?username="+username+"&begin={[name=\"startDate\"]:}&end={[name=\"endDate\"]:} 23:59:59&proxyName={[name=\"proxyName\"]:}'>" + moneyFormatter(data.lotteryBetAmount)+"</a></td>";
					dm+="<td>" + moneyFormatter(data.lotteryBetNum)+"</td>";
					win+="<td>" + moneyFormatter(data.lotteryWinAmount)+"</td>";
					profit+="<td>" + moneyFormatter(data.lotteryWinOrLose)+"</td>";
				}
				$betWinMoney.html(bet+"</tr>"+dm+"</tr>"+win+"</tr>"+profit+"</tr>");
				Fui.initUI($betWinMoney);
			}
			function addAllMoney(data){
				var tds="<td>"+moneyFormatter(data.totalBetAmount)+"</td>"
					+"<td>"+moneyFormatter(data.totalWinAmount)+"</td>"
					+"<td>"+moneyFormatter(data.totalWinOrLose)+"</td>";
				// $allMoney.html(tds);
			}
		}
	}
});