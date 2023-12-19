<form action="${managerBase}/partnerUser/doUpdatePwd.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "manager.password.update"/></h4>
			</div><input type="hidden" name="id" value="${user.id}">
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "manager.account.number"/>：</td>
							<td>${user.username}</td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.password.input"/>：</td>
							<td><input type="password" name="pwd" class="form-control"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.confirm.password"/>：</td>
							<td><input type="password" name="pwd2" class="form-control"/></td>
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