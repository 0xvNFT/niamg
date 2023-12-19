<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" name="begin" format="YYYY-MM-DD HH:mm:ss" value="${cusDate} 00:00:00" placeholder="<@spring.message "manager.begin.date"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "manager.before.day"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "manager.before.month"/></button>
            <select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select>
	        <input class="form-control" name="username" type="text" placeholder="<@spring.message "manager.username.input"/>">
	        <input class="form-control" name="loginIp" type="text" placeholder="<@spring.message "manager.logout.name.ip"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="end" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${cusDate} 23:59:59" placeholder="<@spring.message "manager.end.date"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "manager.last.day"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "manager.after.month"/></button>
			<select class="form-control" name="status"><option value=""><@spring.message "manager.all.status"/></option>
			<option value="1"><@spring.message "manager.login.fail"/></option>
			<option value="2"><@spring.message "manager.login.success"/></option>
			</select>
			<select class="form-control" name="userType"><option value=""><@spring.message "manager.all.types"/></option>
			<#list logTypes as t><option value="${t.type}">${t.desc}</option></#list>
			</select>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	var logTypeMap={},stations={},partners={};
	<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	<#list logTypes as t>logTypeMap["${t.type}"]="${t.desc}";</#list>
	<#list partners as p>partners["${p.id}"]="${p.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/loginLog/list.do',
		columns : [ { 
			field : 'username',
			title : '<@spring.message "manager.account.number"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.login.time"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'loginIp',
			title : '<@spring.message "manager.logout.name.ip"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'userType',
			title : '<@spring.message "admin.deposit.table.depositType"/>',
			align : 'center',
			valign : 'middle',
			formatter:function(value,row,index){
				if(value){
					return logTypeMap[value+''];
				}
				return "";
			}
		}, {
			field : 'partnerId',
			title : '<@spring.message "manager.coper"/>',
			align : 'center',
			valign : 'middle',
			formatter:function(value,row,index){
				if(value){
					return partners[value+''];
				}
				return "-";
			}
		}, {
			field : 'stationId',
			title : '<@spring.message "manager.station.point"/>',
			align : 'center',
			valign : 'middle',
			formatter:function(value,row,index){
				if(value){
					return stations[value+''];
				}
				return "-";
			}
		}, {
			field : 'loginIpStr',
			title : '<@spring.message "manager.address"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'loginOs',
			title : '<@spring.message "manager.oper.system"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'status',
			title : '<@spring.message "admin.status"/>',
			align : 'center',
			valign : 'middle',
			formatter:function(value,row,index){
				if(value==2){
					return '<span class="label label-success"><@spring.message "manager.login.success"/></span>';
				}
				var s= '<span class="label label-warning"><@spring.message "manager.login.fail"/></span>';
				if (!row.remark || row.remark.length < 6) {
	                return s+row.remark;
	            }
				return s=s+'<a class="open-text" dialog-text="'+row.remark+'" dialog-title="<@spring.message "admin.money.history.remark.detail"/>" title="'+row.remark+'">'+row.remark.substr(0,6)+'...</a>';
			}
		}, {
            field : 'userAgent',
            title : '<@spring.message "manager.use.ie"/>',
            align : 'center',
            valign : 'middle'
        } , {
            field : 'domain',
            title : '<@spring.message "manager.domain"/>',
            align : 'center',
            valign : 'middle'
        }  ]
	});
});
</script>
