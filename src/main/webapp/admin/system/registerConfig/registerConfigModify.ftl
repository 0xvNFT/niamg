<form action="${adminBase}/registConfig/modify.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "admin.registerConfig.modifyTitle"/></h4>
			</div>
			<div class="modal-body"><input type="hidden" name="id" value="${config.id}">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle"><@spring.message "admin.name"/>：</td>
							<td>
								<input type="text" name="name" class="form-control"
									   value="<@spring.message '${config.code}'/>"
									   placeholder="<@spring.message '${config.code}'/>"
								/>
							</td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.sortNo"/>：</td>
							<td><input type="text" name="sortNo" class="form-control" value="${config.sortNo}"/></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.tips"/>：</td>
							<td><input type="text" name="tips" class="form-control" value="${config.tips}"/></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
		</div>
	</div>
</form>