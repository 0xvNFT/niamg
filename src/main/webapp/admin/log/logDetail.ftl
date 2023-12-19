<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.log.detail"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="30%" class="text-right"><@spring.message "manager.username.input"/>：</td>
						<td>${log.username }</td>
					</tr>
					<tr>
						<td class="text-right"><@spring.message "manager.log.types"/>：</td>
						<td>${typeName }</td>
					</tr>
					<tr>
						<td class="text-right"><@spring.message "manager.oper.time"/>：</td>
						<td>${log.createDatetime?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>
					<tr>
						<td class="text-right"><@spring.message "manager.oper.ip"/>：</td>
						<td>${log.ip }<#if ipArea?has_content>[${ipArea}]</#if></td>
					</tr>
					<tr>
						<td class="text-right"><@spring.message "admin.opLogContent"/>：</td>
						<td><textarea class="form-control">${log.content }</textarea></td>
					</tr>
					<tr>
						<td class="text-right"><@spring.message "manager.parm"/>：</td>
						<td><textarea class="form-control">${log.params }</textarea></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.closed"/></button>
		</div>
	</div>
</div>
