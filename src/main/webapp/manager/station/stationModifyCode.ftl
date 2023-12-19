<form action="${managerBase}/station/modifyCode.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title">修改站点编号</h4>
			</div><input type="hidden" name="id" value="${station.id}">
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "manager.blank.user"/>：</td>
							<td>${station.name}</td>
						</tr>
						<tr>
							<td class="text-right media-middle">编号：</td>
							<td><input type="text" name="code" class="form-control required" value="${station.code}"/></td>
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
