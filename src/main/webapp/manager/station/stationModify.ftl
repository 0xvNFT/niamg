<form action="${managerBase}/station/modify.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "manager.update.stations"/></h4>
			</div><input type="hidden" name="id" value="${station.id}">
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "manager.blank.user"/>：</td>
							<td><input type="text" name="name" class="form-control required" value="${station.name}"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.default.languages"/>：</td>
							<td><select class="form-control" name="language">
								<#list languages as c><option value="${c.name()}"<#if c.name()==station.language>selected</#if>>${c.desc}</option></#list>
								</select></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.default.money.types"/>：</td>
							<td><select class="form-control" name="currency">
									<#list currencys as c><option value="${c.name()}"<#if c.name()==station.currency>selected</#if>>${c.desc}</option></#list>
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
