<form action="${managerBase}/adminWhiteIp/add.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "manager.station.admin.ip.white"/></h4>
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle">IP：</td>
							<td><textarea name="ip" class="form-control required"></textarea>
							<div><@spring.message "manager.mutil.ip.blank"/></div></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
							<td><select name="type" class="form-control">
			                    <option value="2"selected><@spring.message "manager.white.ip"/></option>
			                    <option value="1"><@spring.message "manager.black.ip"/></option>
			                </select></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "manager.station.point"/>：</td>
							<td><select class="form-control" name="stationId">
								<#list stations as s><option value="${s.id}">${s.name}</option></#list>
								</select></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
							<td><input type="text" name="remark" class="form-control"/></td>
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