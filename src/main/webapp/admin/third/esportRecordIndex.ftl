<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="startTime" value="${startTime }" placeholder="<@spring.message "manager.begin.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
			<div class="input-group">
				<input type="text" class="form-control" name="username"value="${username}" placeholder="<@spring.message "admin.money.history.username"/>">
			</div>
			<div class="input-group">
				<select class="form-control" name="platform">
					<option value=""><@spring.message "admin.deposit.type.all"/></option>
					<#list platforms2 as s><option value="${s.value}">${s.name}</option></#list>
				</select>
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${endTime }" placeholder="<@spring.message "manager.end.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
			<div class="input-group">
				<input type="text" class="form-control" name="orderId" placeholder="<@spring.message "admin.third.order.num"/>">
			</div>
			<input type="text" class="form-control" value="${proxyName}" name="proxyName" placeholder="<@spring.message "admin.belong.proxy.account"/>">
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	function getTotal(rows,itemKey,scale){
		var total = 0;
		for(var i=0;i<rows.length;i++){
			var r = rows[i];
			total +=r[itemKey]||0;
		}
		return total.toFixed(scale)+"";
	}
	var platforms=[],platformMap2=[];
	var htmlYing = '&nbsp;<label class="label label-success"><@spring.message "admin.win"/></label>';
	var htmlShu = '&nbsp;<label class="label label-danger"><@spring.message "admin.lose"/></label>';
<#list platforms2 as p>platformMap2['${p.value}']="${p.name}";</#list>
<#list platforms as p>platforms['${p.value}']="${p.name()}";</#list>
	Fui.addBootstrapTable({
		url : '${adminBase}/esportRecord/list.do',
		showPageSummary:true,
		showAllSummary:true,
		showFooter : true,
		columns : [ {
			sortable:true,
			field : 'orderId',
			title : Admin.platformTypes,
			align : 'center',
			valign : 'middle',
			formatter : platformFormatter
		}, {
			field : 'buyTime',
			title : Admin.payTime,
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'username',
			title : Admin.thisSysAcc,
			align : 'center',
			valign : 'middle',
			formatter : function (value , row, index) {
				return '<a class="open-tab text-danger" data-refresh="true" href="${adminBase}/user/detail.do?proxyName=' + value + '" title="<@spring.message "admin.check.member.info"/>" id="fui_tab_detailNew">' + value + '</a>';
			}
		}, {
			field : 'odds',
			title : Admin.rebate,
			align : 'center',
			valign : 'middle',
			pageSummaryFormat:function(rows,aggsData){
				return "<@spring.message "admin.deposit.table.page.sum"/>:";
			},
			allSummaryFormat:function(rows,aggsData){
				return "<@spring.message "admin.deposit.table.all.sum"/>:";
			}
		}, {
			field : 'bettingMoney',
			title : Admin.payMoney,
			align : 'center',
			valign : 'middle',
			pageSummaryFormat:function(rows,aggsData){
				return getTotal(rows,"bettingMoney",2);
			},
			allSummaryFormat:function(rows,aggsData){
				if(!aggsData){
					return "0.00"
				}
				return aggsData.bettingMoneyCount ? aggsData.bettingMoneyCount.toFixed(4) : "0.00";
			}
		}, {
			field : 'realBettingMoney',
			title : Admin.betNum,
			align : 'center',
			valign : 'middle',
			pageSummaryFormat:function(rows,aggsData){
				return getTotal(rows,"realBettingMoney",4);
			},
			allSummaryFormat:function(rows,aggsData){
				if(!aggsData){
					return "0.00"
				}
				return aggsData.realBettingMoneyCount ? aggsData.realBettingMoneyCount.toFixed(4) : "0.0000";
			}
		}, {
			field : 'winMoney',
			sortable:true,
			title : Admin.loseOrWin,
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index){
				if(row.platformType=='SBTA'){
					switch(row.playType){
					case "1":return value;
					case "0":return "<@spring.message "admin.not.payed"/>";
					case "-8":return "<@spring.message "admin.cancel.this.one"/>";
					case "-9":return "<@spring.message "admin.cancel.pointed.one"/>";
					case "2":return "<@spring.message "admin.pushing.one"/>";
					case "4":return "<@spring.message "admin.cancel.cash"/>";
					}
					return "";
				}	
				return value;
			},
			pageSummaryFormat:function(rows,aggsData){
				return getTotal(rows,"winMoney",4);
			},
			allSummaryFormat:function(rows,aggsData){
				if(!aggsData){
					return "0.0000"
				}
				return aggsData.winMoneyCount ? aggsData.winMoneyCount.toFixed(4) : "0.0000";
			}
		}, {
			field : 'yingkui',
			title : Admin.earnLose,
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index){
				var tempData = (row.winMoney - row.bettingMoney).toFixed(4) || 0;
				return tempData > 0?tempData+htmlYing:tempData+htmlShu;
			},
			pageSummaryFormat:function(rows,aggsData){
				var tempData = (getTotal(rows,"winMoney",4) - getTotal(rows,"bettingMoney",4)).toFixed(4) || 0;
				return tempData > 0?tempData+htmlYing:tempData+htmlShu;
			},
			allSummaryFormat:function(rows,aggsData){
				if(!aggsData){
					return "0.0000"
				}
				var tempData = (aggsData.winMoneyCount - aggsData.bettingMoneyCount).toFixed(4) || 0;
				return tempData > 0?tempData+htmlYing:tempData+htmlShu;
			}
		}, {
			title : Admin.gameTypes,
			align : 'center',
			valign : 'middle',
			formatter : function (value, row, index) {
                var game = '';
                if (row.leagueName) {
                    game = row.leagueName;
                }
                if (row.gameName) {
                    game = game + '<br>' + row.gameName
                }
                return game;
			}
		}, {
			title : Admin.gameNameMeth,
			align : 'center',
			valign : 'middle',
			formatter : function (value, row, index) {
                var player = '';
                if (row.matchName) {
                    player = row.matchName;
                }
                if (row.playName) {
                    player = player + '<br>' + row.playName;
                }
                return player;
			}
		}, {
			field : 'bettingContent',
			title : Admin.payContent,
			align : 'center',
			valign : 'middle',
			formatter : contentFormatter
		}]
	});
	function getTotal(rows,itemKey,scale){
		var total = 0;
		for(var i=0;i<rows.length;i++){
			var r = rows[i];
			total +=r[itemKey]||0;
		}
		return total.toFixed(scale)+"";
	}
	function platformFormatter(value, row, index) {
		if(platformMap2[row.platformType]){
			return platformMap2[row.platformType]+'<br>'+value;
		}
		return platforms[value]+'<br>'+value;
	}
	function contentFormatter(value, row, index) {
		if (value && value.length > 10) {
			return "<a role='button' dialog-text='"+value+"' dialog-title='<@spring.message "admin.deposit.bank.bankCard.detail"/>' class='open-text'>"+value.substring(0, 10) + "...<a>";
		}
		return value;
	}
});
</script>