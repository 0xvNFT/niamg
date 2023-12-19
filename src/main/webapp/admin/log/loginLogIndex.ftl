<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" name="begin" format="YYYY-MM-DD HH:mm:ss" value="${cusDate} 00:00:00" placeholder="<@spring.message "admin.srartTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
	        <input class="form-control" name="username" type="text" placeholder="<@spring.message "admin.username"/>">
	        <input class="form-control" name="loginIp" type="text" placeholder="<@spring.message "admin.login.ip"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="end" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${cusDate} 23:59:59" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
			<select class="form-control" name="status"><option value=""><@spring.message "admin.status.all"/></option>
			<option value="1"><@spring.message "admin.status.login.failed"/></option>
			<option value="2"><@spring.message "admin.status.login.success"/></option>
			</select>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	var logTypeMap={};
	<#list logTypes as t>logTypeMap["${t.type}"]="${t.desc}";</#list>
	Fui.addBootstrapTable({
		url : '${adminBase}/loginLog/list.do',
		columns : [ { 
			field : 'username',
			title : Admin.username,
			align : 'center',
			valign : 'middle'
		}, {
			field : 'createDatetime',
			title : Admin.loginTime,
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'loginIp',
			title : Admin.loginIp,
			align : 'center',
			valign : 'middle'
		}, {
			field : 'userType',
			title : Admin.type,
			align : 'center',
			valign : 'middle',
			formatter:function(value,row,index){
				if(value){
					return logTypeMap[value+''];
				}
				return "";
			}
		}, {
			field : 'loginIpStr',
			title : Admin.ipAddress,
			align : 'center',
			valign : 'middle'
		},{
			field : 'loginOs',
			title : Admin.os,
			align : 'center',
			valign : 'middle'
		}, {
			field : 'status',
			title : Admin.status,
			align : 'center',
			valign : 'middle',
			formatter:function(value,row,index){
				if(value==2){
					return '<span class="label label-success">'+Admin.loginSuccess+'</span>';
				}
				var s= '<span class="label label-warning">'+Admin.loginFailed+'</span>';
				if (!row.remark || row.remark.length < 6) {
	                return s+row.remark;
	            }
				return s=s+'<a class="open-text" dialog-text="'+row.remark+'" dialog-title="'+Admin.viewDetail+'" title="'+row.remark+'">'+row.remark.substr(0,6)+'...</a>';
			}
		}, {
            field : 'userAgent',
            title : Admin.browser,
            align : 'center',
            valign : 'middle'
        } , {
            field : 'domain',
            title : Admin.domain,
            align : 'center',
            valign : 'middle'
        }  ]
	});
});
</script>
