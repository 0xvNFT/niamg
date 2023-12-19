<form action="${managerBase}/adminUser/updatePwd.do" class="form-submit" unReloadTable="true">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.password.update"/></h4>
		</div>
		<div class="modal-body"><input type="hidden" name="id" value="${adminUser.id}">
		<input type="hidden" name="stationId" value="${adminUser.stationId}">
			<table class="table table-bordered table-striped" style="clear: both">
				<tbody>
					<tr>
						<td width="30%" class="text-right vmid"><@spring.message "manager.username.input"/>：</td>
						<td ><input type="text" class="form-control" value="${adminUser.username}"disabled="disabled" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.new.password"/>：</td>
						<td><input type="password" class="form-control" name="pwd" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.confirm.password"/>：</td>
						<td><input type="password" class="form-control" name="rpwd" /></td>
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