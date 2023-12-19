<form action="${managerBase}/adminUserGroup/modify.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.station.admin.group.update"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped" style="clear: both">
				<tbody>
					<tr><input name="id" type="hidden" value="${group.id}"><input name="stationId" type="hidden" value="${group.stationId}">
						<td class="text-right vmid"><@spring.message "manager.blank.user"/>：</td>
						<td><input type="text" class="form-control" name="name" value="${group.name}"/></td>
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