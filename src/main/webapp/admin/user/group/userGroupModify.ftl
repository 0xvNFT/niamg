<form action="${adminBase}/userGroup/doModify.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.userGroup.modifyTitle"/></h4>
		</div>
		<div class="modal-body"><input type="hidden" name="id" value="${group.id}"/>
			<table class="table table-bordered table-striped">
				<tbody>
				<tr>
					<td width="15%" class="text-right media-middle"><@spring.message "admin.name"/>：</td>
					<td width="35%" class="text-left">
						<select name="name" class="form-control">
							<option value="默认组别" <#if group.name==springMacroRequestContext.getMessage("base.groupNormal")>selected="selected"</#if>><@spring.message "base.groupNormal"/></option>
							<option value="组别1" <#if group.name==springMacroRequestContext.getMessage("base.group1")>selected="selected"</#if> ><@spring.message "base.group1"/></option>
							<option value="组别2" <#if group.name==springMacroRequestContext.getMessage("base.group2")>selected="selected"</#if> ><@spring.message "base.group2"/></option>
							<option value="组别3" <#if group.name==springMacroRequestContext.getMessage("base.group3")>selected="selected"</#if> ><@spring.message "base.group3"/></option>
							<option value="组别4" <#if group.name==springMacroRequestContext.getMessage("base.group4")>selected="selected"</#if> ><@spring.message "base.group4"/></option>
							<option value="组别5" <#if group.name==springMacroRequestContext.getMessage("base.group5")>selected="selected"</#if> ><@spring.message "base.group5"/></option>
							<option value="组别6" <#if group.name==springMacroRequestContext.getMessage("base.group6")>selected="selected"</#if> ><@spring.message "base.group6"/></option>
							<option value="组别7" <#if group.name==springMacroRequestContext.getMessage("base.group7")>selected="selected"</#if> ><@spring.message "base.group7"/></option>
							<option value="组别8" <#if group.name==springMacroRequestContext.getMessage("base.group8")>selected="selected"</#if> ><@spring.message "base.group8"/></option>
							<option value="组别9" <#if group.name==springMacroRequestContext.getMessage("base.group9")>selected="selected"</#if> ><@spring.message "base.group9"/></option>
							<option value="组别10" <#if group.name==springMacroRequestContext.getMessage("base.group10")>selected="selected"</#if> ><@spring.message "base.group10"/></option>
						</select>
					</td>
				</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.userGroup.dailyDrawNum"/>：</td>
						<td><input type="text" name="dailyDrawNum" value="${group.dailyDrawNum}" class="form-control"placeholder="<@spring.message "admin.empty.unlimit"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.userGroup.maxDrawMoney"/>：</td>
						<td ><input name="maxDrawMoney" class="form-control" value="${group.maxDrawMoney}" placeholder="<@spring.message "admin.empty.unlimit"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.userGroup.minDrawMoney"/>：</td>
						<td ><input name="minDrawMoney" class="form-control" value="${group.minDrawMoney}"placeholder="<@spring.message "admin.empty.unlimit"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
						<td ><textarea name="remark" class="form-control">${group.remark}</textarea></td>
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
