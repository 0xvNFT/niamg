<form action="${adminBase}/userGroup/doAdd.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.userGroup.addTitle"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped">
				<tbody>
				<tr>
					<td width="15%" class="text-right media-middle"><@spring.message "admin.name"/>：</td>
					<td width="35%" class="text-left">
						<select name="name" class="form-control">
							<option value="普通"><@spring.message "base.groupNormal"/></option>
							<option value="组别1"><@spring.message "base.group1"/></option>
							<option value="组别2"><@spring.message "base.group2"/></option>
							<option value="组别3"><@spring.message "base.group3"/></option>
							<option value="组别4"><@spring.message "base.group4"/></option>
							<option value="组别5"><@spring.message "base.group5"/></option>
							<option value="组别6"><@spring.message "base.group6"/></option>
							<option value="组别7"><@spring.message "base.group7"/></option>
							<option value="组别8"><@spring.message "base.group8"/></option>
							<option value="组别9"><@spring.message "base.group9"/></option>
							<option value="组别10"><@spring.message "base.group10"/></option>
						</select>
					</td>
				</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.userGroup.dailyDrawNum"/>：</td>
						<td><input type="text" name="dailyDrawNum" class="form-control"placeholder="<@spring.message "admin.empty.unlimit"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.userGroup.maxDrawMoney"/>：</td>
						<td ><input name="maxDrawMoney" class="form-control" placeholder="<@spring.message "admin.empty.unlimit"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.userGroup.minDrawMoney"/>：</td>
						<td ><input name="minDrawMoney" class="form-control"placeholder="<@spring.message "admin.empty.unlimit"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
						<td ><textarea name="remark" class="form-control"></textarea></td>
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
