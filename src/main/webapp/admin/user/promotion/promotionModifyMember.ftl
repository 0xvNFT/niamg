<form action="${adminBase}/promotion/doModifyMember.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.vip.first.link.update"/></h4>
		</div>
		<div class="modal-body"><input type="hidden" name="id" value="${p.id}"/>
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="30%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
						<td>${p.username}</td>
                    </tr>
					<tr>
                        <td class="text-right media-middle"><@spring.message "admin.extend.domain"/>：</td>
						<td><input name="domain" class="form-control"value="${p.domain}"/></td>
					</tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "admin.extend.page"/>：</td>
                        <td>
                            <select name="accessPage" class="form-control">
                                <option value="1"<#if p.accessPage==1>selected</#if>><@spring.message "admin.register"/></option>
                                <option value="2"<#if p.accessPage==2>selected</#if>><@spring.message "admin.main.page"/></option>
                                <option value="3"<#if p.accessPage==3>selected</#if>><@spring.message "admin.discount.activit"/></option>
                            </select>
                        </td>
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
