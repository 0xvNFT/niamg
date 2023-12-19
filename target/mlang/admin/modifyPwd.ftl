<form action="${adminBase}/updloginpwd.do" class="form-submit" unReloadTable="true">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.modify.pwd"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped" style="clear: both">
				<tbody>
					<tr>
						<td width="30%" class="text-right vmid"><@spring.message "admin.username"/>：</td>
						<td ><input type="text" class="form-control" value="${loginUser.username}"disabled="disabled" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.password.old"/>：</td>
						<td><input type="password" class="form-control" name="opwd" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.password.new"/>：</td>
						<td><input type="password" class="form-control" name="pwd" /></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "admin.password.verify"/>：</td>
						<td><input type="password" class="form-control" name="rpwd" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
			<button type="submit" class="btn btn-primary"><@spring.message "admin.save"/></button>
		</div>
	</div>
</div>
</form>