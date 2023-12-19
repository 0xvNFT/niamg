<form class="fui-search table-tool" method="post" id="betrecord_tsport_form_id">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" value="${startTime}"format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.begin.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "manager.before.day"/></button>
			<div class="input-group">
				<input type="text" class="form-control" name="username"value="${username}" placeholder="<@spring.message "admin.money.history.username"/>">
			</div>
			<div class="input-group">
				<select class="form-control" name="platform">
					<option value=""><@spring.message "admin.deposit.type.all"/></option>
					<#list platforms2 as p><option value="${p.value}">${p.name}</option></#list>
				</select>
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" value="${endTime}"format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.end.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "manager.last.day"/></button>
			<div class="input-group">
				<input type="text" class="form-control" name="orderId" placeholder="<@spring.message "admin.third.order.num"/>">
			</div>
			<input type="text" class="form-control" name="proxyName" value="${proxyName}" placeholder="<@spring.message "admin.belong.proxy.account"/>">
			</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var platforms=[],platformMap2=[];
var htmlYing = '&nbsp;<label class="label label-success"><@spring.message "admin.win"/></label>',htmlShu = '&nbsp;<label class="label label-danger"><@spring.message "admin.lose"/></label>';
<#list platforms2 as p>platformMap2['${p.value}']="${p.name}";</#list>
<#list platforms as p>platforms['${p.value}']="${p.name()}";</#list>
var btable=null,$form=$("#betrecord_tsport_form_id");
$form.submit(function(){
	if(btable && !btable.resetOption){
		btable.bootstrapTable("refreshOptions",{url:'${proxyBase}/sportRecord/list.do'});
		btable.resetOption=true;
	}
});
	Fui.addBootstrapTable({
		url : '${proxyBase}/sportRecord/list.do',
		showPageSummary:true,
		showAllSummary:true,
		showFooter : true,
		onCreatedSuccessFun:function(r){
			btable=r;
		},
		columns : [ {
			field : 'type',
			title : Admin.systemType,
			align : 'center',
			valign : 'middle',
			formatter : platformFormatter
		}, {
			field : 'username',
			title : Admin.thisSysAcc,
			align : 'center',
			valign : 'middle',
			formatter:function (value,row,index) {
				return '<a class="open-tab text-danger" data-refresh="true" href="${proxyBase}/user/detail.do?proxyName=' + value + '" title="<@spring.message "admin.check.member.info"/>" id="fui_tab_detailNew">' + value + '</a>';
			}
		}, {
			title : Admin.payNum,
			align : 'center',
			valign : 'middle',
			formatter : betCodeFormatter
		}, {
			sortable:true,
			field : 'bettingTime',
			title : Admin.payTime,
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'oddsType',
			title : Admin.rebate,
			align : 'center',
			valign : 'middle',
			formatter : function (value, row, index) {
                if (value) {
                    return row.odds+"</br>"+value;
                }
                return "";
            }
		}, {
			field : 'bettingContent',
			title : Admin.payContent,
			align : 'center',
			valign : 'middle',
			formatter : contentFormatter,
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
					return "0.0000"
				}
				return aggsData.realBettingMoneyCount ? aggsData.realBettingMoneyCount.toFixed(4) : "0.0000";
			}
		}, {
			field : 'winMoney',
			sortable:true,
			title : Admin.loseOrWin,
			align : 'center',
			valign : 'middle',
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
				 switch (row.platformType) {
					case "TYSB":
                        switch (row.status) {
                        		case "waiting":
                        		case "running":return "";
                        	}
                        	break;
                    case "TYXJ":
					    switch (row.status) {
							case "1":return "";
						}
						break;
					case "SBTA":
					    switch (row.status) {
						    	case "0":
						    	case "2":return "";
					    }
						break;
				 	case "TYCR":
					 	switch (row.status) {
						 		case "0":
						 		case "D":
						 		case "A":
									return "";
					 	}
					 	break;
					 case "DJIM":
						 switch (row.status) {
							 case "0":return "";
						 }
						 break;
				}
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
			field : 'status',
			title : Admin.status,
			align : 'center',
			valign : 'middle',
			formatter : function (value, row, index) {
                if (value) {
                    switch (row.platformType) {
						case "TYSB":
                            switch (value) {
                                case "half won":
                                    return "<@spring.message "admin.half.win"/>";
                                case "half lose":
                                    return "<@spring.message "admin.half.lose"/>";
                                case "won":
                                    return "<@spring.message "admin.win"/>";
                                case "lose":
                                    return "<@spring.message "admin.lose"/>";
                                case "draw":
                                    return "<@spring.message "admin.peace"/>";
                                case "reject":
                                    return '<span class="label label-danger"><@spring.message "admin.alerd.cancel"/></span>';
                                case "waiting":
                                    return '<span class="label label-warning"><@spring.message "admin.waitting"/></span>';
                                case "running":
                                    return '<span class="label label-info"><@spring.message "admin.opering"/></span>';
                                case "void":
                                    return '<span class="label label-danger"><@spring.message "admin.unused"/></span>';
                                case "refund":
                                    return '<span class="label label-danger"><@spring.message "admin.pay.back"/></span>';
                            }
                            break;
						case "TYXJ":
						    switch (value) {
								case "1":
									return '<span class="label label-info"><@spring.message "admin.confirmed"/></span>';
								case "2":
								    return "<@spring.message "admin.bill.pay"/>";
								case "3":
								    return '<span class="label label-danger"><@spring.message "admin.alerd.cancel"/></span>';
								case "4":
								     return '<span class="label label-danger"><@spring.message "admin.unexpire"/></span>';
                            }
                            break;
						case "SBTA":
						    switch (value) {
								case "1":
								    return "<@spring.message "admin.payed"/>";
								case "0":
								    return '<span class="label label-info"><@spring.message "admin.not.payed"/></span>';
								case "-8":
								    return '<span class="label label-danger"><@spring.message "admin.cancel.this.one"/></span>';
								case "-9":
								    return '<span class="label label-danger"><@spring.message "admin.cancel.pointed.one"/></span>';
								case "2":
								    return '<span class="label label-info"><@spring.message "admin.pushing.one"/></span>';
								case "4":
								    return '<span class="label label-danger"><@spring.message "admin.cancel.cash"/></span>';
                            }
                            break;
                        case "SBO":
                            switch (value) {
                                case "half won":
                                    return "<@spring.message "admin.half.win"/>";
                                case "half lose":
                                    return "<@spring.message "admin.half.lose"/>";
                                case "won":
                                    return "<@spring.message "admin.win"/>";
                                case "lose":
                                    return "<@spring.message "admin.lose"/>";
                                case "draw":
                                    return "<@spring.message "admin.peace"/>";
                                case "reject":
                                    return '<span class="label label-danger"><@spring.message "admin.alerd.cancel"/></span>';
                                case "waiting":
                                    return '<span class="label label-warning"><@spring.message "admin.waitting"/></span>';
                                case "running":
                                    return '<span class="label label-info"><@spring.message "admin.opering"/></span>';
                                case "void":
                                    return '<span class="label label-danger"><@spring.message "admin.unused"/></span>';
                                case "refund":
                                    return '<span class="label label-danger"><@spring.message "admin.pay.back"/></span>';
                                case "waiting rejected":
                                    return '<span class="label label-danger"><@spring.message "admin.danger.unused"/></span>';
                            }
                            break;
                        case "TYCR":
                            switch (value) {
                                case "0":
                                    return '<span class="label label-info"><@spring.message "admin.not.payed"/></span>';
                                case "L":
                                    return "<@spring.message "admin.lose"/>";
                                case "W":
                                    return '<@spring.message "admin.win"/>';
                                case "P":
                                    return '<@spring.message "admin.equal"/>';
                                case "D":
                                    return '<span class="label label-danger"><@spring.message "admin.member.info.cancle"/></span>';
                                case "A":
                                    return '<span class="label label-danger"><@spring.message "admin.reback"/></span>';
                            }
                            break;
                        case "DJIM":
                            switch (value) {
                                case "0":
                                    return '<span class="label label-info"><@spring.message "admin.not.payed"/></span>';
                                case "L":
                                    return "<@spring.message "admin.lose"/>";
                                case "W":
                                    return '<@spring.message "admin.win"/>';
                                case "D":
                                    return '<@spring.message "admin.equal"/>';
                            }
                            break;
                    }
                }
                return "";
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
		return '<span class="text-primary" >' + row.orderId + '</span><br>' + Fui.formatDatetime(row.buyTime);
	}
	function platformFormatter(value, row, index) {
		if(platformMap2[row.platformType]){
			return platformMap2[row.platformType];
		}
		return platforms[value];
	}
	function contentFormatter(value, row, index) {
        if(row.type==49){
            return '<a href="${proxyBase}/sportRecord/viewDetail.do?id='+row.id+'&type='+row.type+'" target="_blank"><@spring.message "admin.deposit.table.detail"/></a>';
        }
		if (value && value.length > 10) {
			var vv=value.replace(/\n/g,"<br>");
			return "<a role='button' dialog-text='"+vv+"' dialog-title='<@spring.message "admin.deposit.bank.bankCard.detail"/>' class='open-text'>"+value.substring(0, 10) + "...<a>";
		}
		return value;
	}
});
</script>