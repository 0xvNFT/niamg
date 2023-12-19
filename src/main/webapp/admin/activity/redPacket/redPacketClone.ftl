<form action="${adminBase}/redPacket/doCloneOne.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.red.packet.clone"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.red.packet.identity"/>：</td>
						<td><input name="rid" class="form-control required" type="number" placeholder="<@spring.message "admin.need.clone.red.id"/>" style="width: 196px;"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
						<td><div class="input-group">
							<input type="text" class="form-control fui-date required" value="${startTime }" format="YYYY-MM-DD HH:mm:ss" name="begin" placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
						</div></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
						<td><div class="input-group">
							<input type="text" class="form-control fui-date required" value="${endTime }" format="YYYY-MM-DD HH:mm:ss" name="end" placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
						</div></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.append.days"/>：</td>
						<td><div class="input-group">
							<input type="number" class="form-control" value="0" name="addDay" placeholder="<@spring.message "admin.append.days"/>">
						</div></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer"><span class="text-danger"><@spring.message "admin.append.days.not.write.red"/>。<br/>(<@spring.message "admin.five.append.red.act"/>)</span></div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
			<button class="btn btn-primary"><@spring.message "admin.save"/></button>
		</div>
	</div>
</div>
</form>