<form action="${managerBase}/station/add.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "manager.insert.station"/></h4>
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "manager.blank.user"/>：</td>
							<td><input type="text" name="name" class="form-control required"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.money.history.id"/>：</td>
							<td><input type="text" name="code" class="form-control required"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.money.types"/>：</td>
							<td><select class="form-control" name="currency">
								<#list currencies as c><option value="${c.name()}">${c.desc}</option></#list>
								</select></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.default.languages"/>：</td>
							<td><select class="form-control" name="language">
								<#list languages as c><option value="${c.name()}">${c.desc}</option></#list>
								</select></td>
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