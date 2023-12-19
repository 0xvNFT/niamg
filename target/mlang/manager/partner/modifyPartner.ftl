<form action="${managerBase}/partner/doModify.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "manager.update.operation"/></h4><input type="hidden" class="form-control" name="id" value="${p.id}">
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "manager.blank.user"/>：</td>
							<td><input type="text" name="name" class="form-control required" value="${p.name}"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.deposit.remark.bgRemark"/>：</td>
							<td><textarea name="remark" class="form-control">${p.remark}</textarea></td>
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