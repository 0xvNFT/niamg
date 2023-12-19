<form action="${managerBase}/adminUser/add.do" class="form-submit" id="admin_user_add_form_id">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.insert.station.admin"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped" style="clear: both">
				<tbody>
					<tr>
						<td width="30%" class="text-right vmid"><@spring.message "manager.belong.to.station"/>：</td>
						<td ><select class="form-control" name="stationId">
							<#list stations as s><option value="${s.id}">${s.name}</option></#list>
							</select></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.deposit.table.depositType"/>：</td>
						<td><select class="form-control" name="type">
							<#list types as s><#if s.type!=40 && s.type!=80><option value="${s.type}">${s.desc}</option></#if></#list>
						</select></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.group.types"/>：</td>
						<td><select class="form-control" name="groupId"></select></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.username.input"/>：</td>
						<td><input type="text" class="form-control" name="username" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.confirm.password"/>：</td>
						<td><input type="password" class="form-control" name="password" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.withdraw.remark"/>：</td>
						<td><input type="text" class="form-control" name="remark" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
			<button type="submit" class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
requirejs(['jquery'],function(){
	var form=$("#admin_user_add_form_id");
	form.find("[name='stationId']").change(function(){
		var val=$(this).val(); 
		if(val){
			$.get("${managerBase}/adminUser/getGroups.do?stationId="+val,function(v){
				if(v && v.length>0){
					var html="";
					for(var d in v){
						html=html+'<option value="'+v[d].id+'">'+v[d].name+'</option>';
					}
					form.find("[name='groupId']").html(html);
				}
			},"json");
		}else{
			form.find("[name='groupId']").html("");
		}
	}).change();
});
</script>