<form action="${managerBase}/user/addUser.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "manager.insert.user"/></h4>
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "manager.account.number.name"/>：</td>
							<td><input type="text" name="username" class="form-control required" /></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.password.input"/>：</td>
							<td><input type="password" name="password" class="form-control"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.deposit.table.status"/>：</td>
							<td><label class="radio-inline"><input type="radio" name="status" value="2" checked="checked"><@spring.message "admin.deposit.bank.bankCard.status.yes"/></label>&nbsp;
								<label class="radio-inline"><input type="radio" name="status" value="1"><@spring.message "admin.deposit.handle.close"/></label></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
				<button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
			</div>
		</div>
	</div>
</form>