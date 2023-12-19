<form class="fui-search table-tool" method="post" id="proxy_daily_stat_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
           <div class="input-group">
				<input type="text" class="form-control fui-date" name="startDate" value="${curDate}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <select class="form-control" name="status">
                    <option value="" class="text-warning" selected><@spring.message "admin.withdraw.type.all"/></option>
                    <option value="1" class="text-primary"><@spring.message "admin.unseed.back"/></option>
                    <option value="2" class="text-warning"><@spring.message "admin.seed.back"/></option>
                </select>
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="username" value="${username}" placeholder="<@spring.message "admin.proxyUsername"/>">
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
            <div class="form-inline mt5px">
                <div class="input-group">
					<input type="text" name="endDate" class="form-control fui-date" value="${curDate}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
				</div>
				<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
	            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
                <input type="text" class="form-control" name="operator" placeholder="<@spring.message "admin.deposit.opuser"/>">
            </div>
        </div>
    </div>
</form>
<div style="padding: 10px;">
    <div style="color:red;"><@spring.message "admin.hand.seed.back.point"/></div>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/proxyDailyStat/list.do',
            showPageSummary: true,
            showAllSummary: true,
            showFooter: true,
            columns: [{
                title: Admin.proxyDate,
                align: 'center',
                valign: 'middle',
                formatter: proxyStatDateFormatter
            },{
                field: 'status',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter: statusFormatter,
	            pageSummaryFormat:function(rows,aggsData){
					return "<@spring.message "admin.deposit.table.page.sum"/>:";
				},
				allSummaryFormat:function(rows,aggsData){
					return "<@spring.message "admin.deposit.table.all.sum"/>:";
				}
            }, {
                field: 'lotBet',
                title: Admin.lotteryPay,
                sortable :true,
                align: 'center',
                valign: 'middle',
                formatter: function(value, row, index) {
	        		var s = Fui.toDecimal(value,2);
	        		if(row.status==2){
	        			s=s+'<br>'+Fui.toDecimal(row.lotRollback,4);
	        		}
	        		return s;
		        },
		        pageSummaryFormat:function(rows,aggsData){
		        	return getTotal2(rows,"lotBet","lotRollback");
				},
				allSummaryFormat:function(rows,aggsData){
					var s = (aggsData && aggsData.lotBetAll) ? Fui.toDecimal(aggsData.lotBetAll,4) : "0.0000";
	        			s=s+'<br>'+((aggsData && aggsData.lotRollbackAll) ? Fui.toDecimal(aggsData.lotRollbackAll,4) : "0.0000");
		        		return s;
				}
		     }, {
                field: 'liveBet',
                title: Admin.livePay,
                align: 'center',
                sortable :true,
                valign: 'middle',
                formatter: function(value, row, index) {
	        		var s = Fui.toDecimal(value,2);
	        		if(row.status==2){
	        			s=s+'<br>'+Fui.toDecimal(row.liveRollback,4);
	        		}
	        		return s;
		        },
		        pageSummaryFormat:function(rows,aggsData){
		        	return getTotal2(rows,"liveBet","liveRollback");
				},
				allSummaryFormat:function(rows,aggsData){
					var s = (aggsData && aggsData.liveBetAll) ? Fui.toDecimal(aggsData.liveBetAll,4) : "0.0000";
	        			s=s+'<br>'+((aggsData && aggsData.liveRollbackAll) ? Fui.toDecimal(aggsData.liveRollbackAll,4) : "0.0000");
		        		return s;
				}
		     }, {
                field: 'egameBet',
                title: Admin.egamePay,
                align: 'center',
                sortable :true,
                valign: 'middle',
                formatter: function(value, row, index) {
	        		var s = Fui.toDecimal(value,2);
	        		if(row.status==2){
	        			s=s+'<br>'+Fui.toDecimal(row.egameRollback,4);
	        		}
	        		return s;
		        },
		        pageSummaryFormat:function(rows,aggsData){
	        		return getTotal2(rows,"egameBet","egameRollback");
				},
				allSummaryFormat:function(rows,aggsData){
					var s = (aggsData && aggsData.egameBetAll) ? Fui.toDecimal(aggsData.egameBetAll,4) : "0.0000";
	        			s=s+'<br>'+((aggsData && aggsData.egameRollbackAll) ? Fui.toDecimal(aggsData.egameRollbackAll,4) : "0.0000");
		        		return s;
				}
		     }, {
                field: 'chessBet',
                title: Admin.chessPay,
                align: 'center',
                sortable :true,
                valign: 'middle',
                formatter: function(value, row, index) {
	        		var s = Fui.toDecimal(value,2);
	        		if(row.status==2){
	        			s=s+'<br>'+Fui.toDecimal(row.chessRollback,4);
	        		}
	        		return s;
		        },
		        pageSummaryFormat:function(rows,aggsData){
	        		return getTotal2(rows,"chessBet","chessRollback");
				},
				allSummaryFormat:function(rows,aggsData){
					var s = (aggsData && aggsData.chessBetAll) ? Fui.toDecimal(aggsData.chessBetAll,4) : "0.0000";
	        			s=s+'<br>'+((aggsData && aggsData.chessRollbackAll) ? Fui.toDecimal(aggsData.chessRollbackAll,4) : "0.0000");
		        		return s;
				}
		     }, {
                field: 'sportBet',
                title: Admin.sportsPay,
                align: 'center',
                sortable :true,
                valign: 'middle',
                formatter: function(value, row, index) {
	        		var s = Fui.toDecimal(value,2);
	        		if(row.status==2){
	        			s=s+'<br>'+Fui.toDecimal(row.sportRollback,4);
	        		}
	        		return s;
		        },
		        pageSummaryFormat:function(rows,aggsData){
					return getTotal2(rows,"sportBet","sportRollback");
				},
				allSummaryFormat:function(rows,aggsData){
					var s = (aggsData && aggsData.sportBetAll) ? Fui.toDecimal(aggsData.sportBetAll,4) : "0.0000";
		        		s=s+'<br>'+((aggsData && aggsData.sportRollbackAll) ? Fui.toDecimal(aggsData.sportRollbackAll,4) : "0.0000");
		        		return s;
				}
		     }, {
                field: 'esportBet',
                title: Admin.esportPay,
                align: 'center',
                sortable :true,
                valign: 'middle',
                formatter: function(value, row, index) {
                    var s = Fui.toDecimal(value,2);
                    if(row.status==2){
                        s=s+'<br>'+Fui.toDecimal(row.esportRollback,4);
                    }
                    return s;
                },
                pageSummaryFormat:function(rows,aggsData){
                    return getTotal2(rows,"esportBet","esportRollback");
                },
                allSummaryFormat:function(rows,aggsData){
                    var s = (aggsData && aggsData.esportBetAll) ? Fui.toDecimal(aggsData.esportBetAll,4) : "0.0000";
                    s=s+'<br>'+((aggsData && aggsData.esportRollbackAll) ? Fui.toDecimal(aggsData.esportRollbackAll,4) : "0.0000");
                    return s;
                }
            }, {
                field: 'fishingBet',
                title: Admin.fishPay,
                align: 'center',
                sortable :true,
                valign: 'middle',
                formatter: function(value, row, index) {
                    var s = Fui.toDecimal(value,2);
                    if(row.status==2){
                        s=s+'<br>'+Fui.toDecimal(row.fishingRollback,4);
                    }
                    return s;
                },
                pageSummaryFormat:function(rows,aggsData){
                    return getTotal2(rows,"fishingBet","fishingRollback");
                },
                allSummaryFormat:function(rows,aggsData){
                    var s = (aggsData && aggsData.fishingBetAll) ? Fui.toDecimal(aggsData.fishingBetAll,4) : "0.0000";
                    s=s+'<br>'+((aggsData && aggsData.fishingRollbackAll) ? Fui.toDecimal(aggsData.fishingRollbackAll,4) : "0.0000");
                    return s;
                }
            }, {
                field: 'drawNum',
                title: Admin.drawMoneyWei,
                align: 'center',
                valign: 'middle',
		        pageSummaryFormat:function(rows,aggsData){
					return getTotal(rows,"drawNum");
				},
				allSummaryFormat:function(rows,aggsData){
					return (aggsData && aggsData.drawNumAll) ? Fui.toDecimal(aggsData.drawNumAll,4) : "0.0000";
		        }
		     }, {
                field: 'operator',
                title: Admin.operator,
                align: 'center',
                valign: 'middle',
                formatter: function(value, row, index) {
	        		if(row.updateTime){
	        			return value+'<br>'+Fui.formatDate(row.updateTime);
	        		}
	        		return value;
				}
		     },{
				title : Admin.op,
				align : 'center',
				width : '80',
				valign:'middle',
				formatter : optFormatter
            }]
        });
        function proxyStatDateFormatter(value, row, index) {
        	return row.username+'<br>'+Fui.formatDate(row.statDate);
        }
        function statusFormatter(value, row, index) {
            if (value == 1 || value == '1') {
                return '<span class="label label-default"><@spring.message "admin.unseed.back"/></span>';
            } else {
                return '<span class="label label-success"><@spring.message "admin.seed.back"/></span>';
            }
        }
        function optFormatter(value, row, index) {
			if(row.status==1){
				if(row.statDate<new Date().getTime()){
					return '<a class="label label-primary open-dialog" href="${adminBase}/proxyDailyStat/manualRollback.do?id='+row.id+'"><@spring.message "admin.menu.pds.manual"/></a>';
				}
			}else if(row.status==2){
				return '<a class="todo-ajax label label-danger" href="${adminBase}/proxyDailyStat/cancel.do?id='+row.id+'" title="<@spring.message "admin.sure.roll.back"/>？"><@spring.message "admin.roll.back"/></a>';
			}
		}
		function getTotal(rows,itemKey){
			var total = 0;
			for(var i=0;i<rows.length;i++){
				var r = rows[i];
				if(!r[itemKey]){
					continue;
				}
				total += r[itemKey];
			}
			return Fui.toDecimal(total,2);
		}
		function getTotal2(rows,itemKey,itemKey2){
			var total = 0,total2=0;
			for(var i=0;i<rows.length;i++){
				var r = rows[i];
				total += r[itemKey]?r[itemKey]:0;
				if(r.status==2){
					total2 += r[itemKey2]?r[itemKey2]:0;
				}
			}
			return Fui.toDecimal(total,2)+'<br>'+Fui.toDecimal(total2,2);
		}
    });

</script>
