<form action="${adminBase}/userAvatar/doModify.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header"><input type="hidden" name="id" value="${avatar.id }">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "admin.userAvatar.modifyTitle"/></h4>
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="20%" class="text-right media-middle"><@spring.message "admin.userAvatar.url"/>：</td>
							<td class="text-left"><input type="text" name="url"value="${avatar.url}" class="form-control" /></td>
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