<form action="${managerBase}/adminUser/modify.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.update.station.admin"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped" style="clear: both">
				<tbody>
					<tr><input type="hidden" name="id" value="${adminUser.id}"><input type="hidden" name="stationId" value="${adminUser.stationId}">
						<td width="30%" class="text-right vmid"><@spring.message "manager.username.input"/>：</td>
						<td >${adminUser.username}</td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.deposit.table.depositType"/>：</td>
						<td><select class="form-control" name="type">
							<#list types as s><#if s.type!=40 && s.type!=80><option value="${s.type}"<#if adminUser.type==s.type>selected</#if>>${s.desc}</option></#if></#list>
						</select></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.group.types"/>：</td>
						<td><select class="form-control" name="groupId">
							<#list groups as g><option value="${g.id}"<#if adminUser.groupId==g.id>selected</#if>>${g.name}</option></#list>
						</select></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.withdraw.remark"/>：</td>
						<td><input type="text" class="form-control" name="remark" value="${adminUser.remark}"/></td>
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