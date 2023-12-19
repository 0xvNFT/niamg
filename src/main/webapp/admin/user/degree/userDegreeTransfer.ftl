<form action="${adminBase}/userDegree/transfer.do" class="form-submit">
<div class="modal-dialog"><input type="hidden" value="${degree.id }" name="curId">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.degree.transferTitle"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="30%" class="text-right media-middle"><@spring.message "admin.degree.nameOld"/>：</td>
						<td class="text-left">${degree.name }</td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.degree.nameTo"/>：</td>
						<td class="text-left"><select name="nextId" class="form-control">
						<#list allDegree as l>
							<#if l.id!=degree.id><option value="${l.id }">${l.name }</option></#if>
						</#list></select></td>
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