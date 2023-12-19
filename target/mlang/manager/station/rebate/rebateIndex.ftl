<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group"><select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select></div>
	        <div class="input-group"><select class="form-control" name="userType"><option value=""><@spring.message "manager.turnback.types"/></option>
				<option value="1"><@spring.message "manager.proxy.points"/></option><option value="2"><@spring.message "manager.vip.member.points"/></option>
			</select></div>
			<div class="input-group"><select class="form-control" name="type"><option value=""><@spring.message "manager.turnback.model"/></option>
				<option value="1"><@spring.message "manager.level.same"/></option><option value="2"><@spring.message "manager.level.count"/></option>
			</select></div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations={};<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/stationRebate/list.do',
		columns : [{
			field : 'stationId',
			title : '<@spring.message "manager.station.name"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(!value){return "";}
				return stations[""+value];
			}
		}, {
			field : 'userType',
			title : '<@spring.message "manager.turnback.types"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value==1){return '<span class="label label-success"><@spring.message "manager.proxy.points"/></span>';}
				return '<span class="label label-info"><@spring.message "manager.vip.member.points"/></span>';
			}
		},{
			field : 'type',
			title : '<@spring.message "manager.turnback.model"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(row.userType==2){return '';}
				if(value==1){return '<span class="label label-success"><@spring.message "manager.level.same"/></span>';}
				return '<span class="label label-info"><@spring.message "manager.level.count"/></span>';
			}
		},{
			field : 'rebateMode',
			title : '<@spring.message "manager.back.types"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value==1){return '<span class="label label-success"><@spring.message "manager.day.back.points"/></span>';}
				if(value==2){return '<span class="label label-info"><@spring.message "manager.second.auto.points"/></span>';}
				return '<span class="label label-warning"><@spring.message "manager.hand.second.points"/></span>';
			}
		},{
			field : 'live',
			title : '<@spring.message "manager.live.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'egame',
			title : '<@spring.message "manager.egame.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'chess',
			title : '<@spring.message "manager.chess.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'esport',
			title : '<@spring.message "manager.esports.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'sport',
			title : '<@spring.message "manager.sprots.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'fishing',
			title : '<@spring.message "manager.fish.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'lottery',
			title : '<@spring.message "manager.lottery.points"/>',
			align : 'center',
			valign : 'middle',
			formatter : rebateFormatter
		},{
			field : 'betRate',
			title : '<@spring.message "manager.betting.num"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'levelDiff',
			title : '<@spring.message "manager.level.distance"/>/<@spring.message "manager.max.value"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(row.userType==2){return '';}
				return value+'&nbsp;/&nbsp;'+row.maxDiff;
			}
		},{
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				return '<a class="open-dialog" href="${managerBase}/stationRebate/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>';
			}
		} ]
	});
	function rebateFormatter(value,row,index){
		if(!value){return "0‰";}
		return value+'‰';
	}
});
</script>
