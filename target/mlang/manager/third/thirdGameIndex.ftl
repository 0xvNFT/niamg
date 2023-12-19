<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
		</div>
	</div>
</form>

<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations={};<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/thirdGame/list.do',
		columns : [{
			field : 'stationId',
			title : '<@spring.message "manager.station.point"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return stations[""+value];
			}
		},{
			field : 'live',
			title : '<@spring.message "manager.live"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=live&stationId="+row.stationId+"&status="});
			}
		},{
			field : 'egame',
			title : '<@spring.message "manager.egame"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=egame&stationId="+row.stationId+"&status="});
			}
		},{
			field : 'chess',
			title : '<@spring.message "manager.chess"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=chess&stationId="+row.stationId+"&status="});
			}
		},{
			field : 'fishing',
			title : '<@spring.message "manager.fish"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=fishing&stationId="+row.stationId+"&status="});
			}
		},{
			field : 'esport',
			title : '<@spring.message "manager.esport"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=esport&stationId="+row.stationId+"&status="});
			}
		},{
			field : 'sport',
			title : '<@spring.message "manager.sport"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=sport&stationId="+row.stationId+"&status="});
			}
		},{
			field : 'lottery',
			title : '<@spring.message "manager.lottery"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/thirdGame/changeStatus.do?type=lottery&stationId="+row.stationId+"&status="});
			}
		}]
	});
});
</script>
