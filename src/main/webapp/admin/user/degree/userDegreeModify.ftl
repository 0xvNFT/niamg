<form action="${adminBase}/userDegree/doModify.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.degree.modifyTitle"/></h4>
		</div>
		<div class="modal-body"><input type="hidden" name="id" value="${degree.id}"/>
		<input type="hidden" name="type" value="${defaultDegree.type}"/>
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="15%" class="text-right media-middle"><@spring.message "admin.name"/>：</td>
						<td width="35%" class="text-left">
							<select name="name" class="form-control">
								<option value="默认会员等级" <#if degree.name==springMacroRequestContext.getMessage("base.levelNormal")>selected="selected"</#if>><@spring.message "base.levelNormal"/></option>
								<option value="会员等级1" <#if degree.name==springMacroRequestContext.getMessage("base.level1")>selected="selected"</#if> ><@spring.message "base.level1"/></option>
								<option value="会员等级2" <#if degree.name==springMacroRequestContext.getMessage("base.level2")>selected="selected"</#if> ><@spring.message "base.level2"/></option>
								<option value="会员等级3" <#if degree.name==springMacroRequestContext.getMessage("base.level3")>selected="selected"</#if> ><@spring.message "base.level3"/></option>
								<option value="会员等级4" <#if degree.name==springMacroRequestContext.getMessage("base.level4")>selected="selected"</#if> ><@spring.message "base.level4"/></option>
								<option value="会员等级5" <#if degree.name==springMacroRequestContext.getMessage("base.level5")>selected="selected"</#if> ><@spring.message "base.level5"/></option>
								<option value="会员等级6" <#if degree.name==springMacroRequestContext.getMessage("base.level6")>selected="selected"</#if> ><@spring.message "base.level6"/></option>
								<option value="会员等级7" <#if degree.name==springMacroRequestContext.getMessage("base.level7")>selected="selected"</#if> ><@spring.message "base.level7"/></option>
								<option value="会员等级8" <#if degree.name==springMacroRequestContext.getMessage("base.level8")>selected="selected"</#if> ><@spring.message "base.level8"/></option>
								<option value="会员等级9" <#if degree.name==springMacroRequestContext.getMessage("base.level9")>selected="selected"</#if> ><@spring.message "base.level9"/></option>
								<option value="会员等级10" <#if degree.name==springMacroRequestContext.getMessage("base.level10")>selected="selected"</#if> ><@spring.message "base.level10"/></option>
							</select>
						</td>
					</tr>

					<#if defaultDegree.type== 1 || defaultDegree.type == 3>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.depositMoney"/>：</td>
						<td ><input name="depositMoney" class="form-control" value="${degree.depositMoney}"/></td>
					</tr>
					</#if>
					<#if defaultDegree.type == 2 || defaultDegree.type == 3>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.betNum"/>：</td>
						<td ><input name="betNum" class="form-control" value="${degree.betNum}"/></td>
					</tr>
					</#if>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.degree.icon"/>：</td>
						<td><input type="text" name="icon" class="form-control"value="${degree.icon}" placeholder="<@spring.message "admin.degree.iconPath"/>"/>
							<font color="red"><@spring.message "admin.degree.iconSize"/></font></td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.degree.sendMsg1"/>：</td>
                        <td >
                            <label class="radio-inline">
                                <input type="radio" name="upgradeSendMsg" value="1"<#if degree.upgradeSendMsg==1>checked</#if>> <@spring.message "admin.degree.sendMsg.no"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="upgradeSendMsg" value="2"<#if degree.upgradeSendMsg==2>checked</#if>> <@spring.message "admin.degree.sendMsg.yes"/>
                            </label>
						</td>
                    </tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.degree.upgradeMoney"/>：</td>
						<td ><input name="upgradeMoney" class="form-control"value="${degree.upgradeMoney}"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
						<td ><input name="betRate" class="form-control" value="${degree.betRate}"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
						<td ><textarea name="remark" class="form-control">${degree.remark}</textarea></td>
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
