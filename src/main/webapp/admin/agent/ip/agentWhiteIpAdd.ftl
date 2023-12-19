<form action="${adminBase}/agentWhiteIp/add.do" class="form-submit">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "admin.insert.system.ip"/></h4>
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td width="30%" class="text-right media-middle">IP：</td>
							<td><textarea name="ip" class="form-control required"></textarea>
							<div><@spring.message "admin.adminWhiteIp.addTip"/></div></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.proxy.merchant"/>：</td>
							<td><select name="agentId" class="form-control">
			                    <#list agents as g><option value="${g.id }">${g.name }</option></#list>
			                </select></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
							<td><select name="type" class="form-control">
			                    <option value="2"selected><@spring.message "manager.white.ip"/></option>
			                    <option value="1"><@spring.message "manager.black.ip"/></option>
			                </select></td>
						</tr>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
							<td><input type="text" name="remark" class="form-control"/></td>
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