<form class="fui-search table-tool" method="post" id="blackIpFormId">
	<div class="form-group">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control" name="ip"  placeholder="输入ip查询">
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<button class="btn btn-primary open-dialog cached" url="${base}/agent/system/black/add.do">新增</button>
			<button class="btn btn-primary open-tab cached" url="${base}/agent/system/black/setCountryInfo.do" id="add-country-info">国家地区限制</button>
			<button class="btn btn-primary open-tab cached" url="${base}/agent/system/black/setChinaAreaInfo.do" id="add-ChinaArea-info">中国地区限制</button>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<h3>
<#--	<span class="label label-danger">添加黑名单IP后，请到“系统设置-》网站基本设定-》系统开关”中开启“前台访问黑名单”，否则黑名单不起作用</span><br>-->
<#--	<span class="label label-danger">当“系统设置-》网站基本设定-代理后台黑名单开关”开启以后，会禁止类型为代理后台的Ip访问代理后台</span>-->
</h3>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : "${base}/agent/system/black/list.do",
		columns : [ {
			field : 'ip',
			title : 'IP',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'type',
			title : Admin.type,
			align : 'center',
			valign : 'middle',
			formatter : typeFormatter
		}, {
			field : 'status',
			title : Admin.status,
			align : 'center',
			valign : 'middle',
			formatter : statusFormatter
		}, {
			field : 'createDatetime',
			title : Admin.createTime,
			align : 'center',
			width : '200',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
            field: 'remark',
            title: Admin.remark,
            align: 'center',
            valign: 'middle',
            formatter: remarkFormatter
        }, {
			title : Admin.op,
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		}]
	});
	

	
	function statusFormatter(value, row, index) {
		return Fui.statusFormatter({val:value,url:"${base}/agent/system/black/updateStatus.do?id="+row.id+"&status="});
	}
	
	function operateFormatter(value, row, index) {
		return [ '<a class="todo-ajax" href="${base}/agent/system/black/delete.do?id=',row.id,'" title="<@spring.message "manager.sure.del.ip"/>?"><@spring.message "admin.deposit.bank.bankCard.del"/></a>' ]
				.join('');
	}

});
console.log('${blackip}');
</script>