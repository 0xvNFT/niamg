<form class="fui-search table-tool" method="post">
	<div class="form-group">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control" name="thirdUsername" placeholder="<@spring.message "admin.third.account.name"/>">
			</div>
			<div class="input-group">
				<input type="text" name="username"  class="form-control" placeholder="<@spring.message "admin.local.account.name"/>">
			</div>
			<div class="input-group">
				<select class="form-control" name="platform">
					<option value=""><@spring.message "admin.deposit.type.all"/></option>
					<#list platforms as s><option value="${s.value}">${s.title}</option></#list>
				</select>
			</div>
			<button class="btn btn-primary"><@spring.message "admin.deposit.button.search"/></button>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var platforms = [];
        <#list platforms as p>platforms['${p.value}'] = "${p.title}";</#list>
	Fui.addBootstrapTable({
		url : '${adminBase}/thirdUser/list.do',
		columns : [{
			field : 'platform',
			title : Admin.thirdGameType,
			align : 'center',
			width : '180',
			valign : 'middle',
			formatter:function(value, row, index) {
		 		return platforms[value + ""];
			}
		},{
			field : 'thirdUsername',
			title : Admin.thirdAccount,
			align : 'center',
			width : '180',
			valign : 'middle'
		}, {
			field : 'password',
			title : Admin.thirdAccPass,
			align : 'center',
			width : '200',
			valign : 'middle'
		},{
			field : 'username',
			title : Admin.localSysAcc,
			align : 'center',
			width : '200',
			valign : 'middle'
		},{
			field : 'createTime',
			title : Admin.createTime,
			align : 'center',
			width : '200',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}]
	});
});
</script>