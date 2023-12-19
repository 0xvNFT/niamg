<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" value="${startTime}"format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.begin.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>


			<div class="input-group">
				<input type="text" class="form-control" name="username"value="${username}" placeholder="<@spring.message "manager.username.input"/>"/>
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" value="${endTime}"format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.end.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
			<div class="input-group">
				<input type="text" class="form-control" name="orderId" placeholder="<@spring.message "admin.third.order.num"/>"/>
			</div>
			<input type="text" class="form-control" name="proxyName" value="${proxyName}" placeholder="<@spring.message "admin.belong.proxy.account"/>">
			</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var htmlYing = '&nbsp;<label class="label label-success"><@spring.message "admin.win"/></label>';
var htmlShu = '&nbsp;<label class="label label-danger"><@spring.message "admin.lose"/></label>';
	Fui.addBootstrapTable({
		showPageSummary:true,
		showAllSummary:true,
		showFooter : true,
		url : '${proxyBase}/ptRecord/list.do',
		columns : [ {
			sortable:true,
			field : 'id',
			title : Admin.payNum,
			align : 'center',
			valign : 'middle',
			formatter : betCodeFormatter
		}, {
			field : 'username',
			title : Admin.thisSysAcc,
			align : 'center',
			valign : 'middle',
			formatter:function (value,row,index) {
				return '<a class="open-tab text-danger" data-refresh="true" href="${proxyBase}/user/detail.do?proxyName=' + value + '" title="<@spring.message "admin.check.member.info"/>" id="fui_tab_detailNew">' + value + '</a>';
			}
		}, {
			field : 'gameName',
			title : Admin.gameTypes,
			align : 'center',
			valign : 'middle'
		}, {	
			field : 'gameId',
			title : Admin.gameNums,
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
				return aggsData.bettingMoneyCount ? aggsData.bettingMoneyCount.toFixed(2) : "0.00";
			}
		}, {
			field : 'winMoney',
			sortable:true,
			title : Admin.loseOrWin,
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index){
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
			field : 'progressiveWin',
			title : 'jackpot',
			align : 'center',
			valign : 'middle'
		}, {
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
	function betCodeFormatter(value, row, index) {
		return '<span class="text-primary" >' + row.orderId + '</span><br>' + Fui.formatDatetime(row.bettingTime);
	}
});
</script>