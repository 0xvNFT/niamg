<form action="${managerBase}/adminUserGroup/add.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.station.admin.group.add"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped" style="clear: both">
				<tbody>
					<tr>
						<td width="30%" class="text-right vmid"><@spring.message "manager.belong.to.station"/>：</td>
						<td ><select class="form-control" name="stationId">
							<#list stations as s><option value="${s.id}">${s.name}</option></#list>
							</select></td>
					</tr>
					<tr>
						<td class="text-right vmid"><@spring.message "manager.blank.user"/>：</td>
						<td><input type="text" class="form-control" name="name" /></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
						<td class="media-middle"><label class="radio-inline"><input type="radio" name="type" value="1"><@spring.message "manager.control.update.perm"/></label>&nbsp;
						<label class="radio-inline"><input type="radio" name="type" value="2"> <@spring.message "manager.coper.marchant.look"/></label>&nbsp;
						<label class="radio-inline"><input type="radio" name="type" value="3"> <@spring.message "manager.coper.marchant.update.perm"/></label>&nbsp;
						<label class="radio-inline"><input type="radio" name="type" value="4"> <@spring.message "manager.station.only.look"/></label>&nbsp;
						<label class="radio-inline"><input type="radio" name="type" value="5" checked="checked"> <@spring.message "manager.station.update.perm"/></label></td>
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