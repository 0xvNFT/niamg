<form action="${adminBase}/userDegree/doAdd.do" class="form-submit">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.degree.addTitle"/></h4>
		</div>
		<div class="modal-body">
		<input type="hidden" name="type" value="${defaultDegree.type}"/>
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="15%" class="text-right media-middle"><@spring.message "admin.name"/>：</td>
						<td width="35%" class="text-left">
							<select name="name" class="form-control">
								<option value="默认会员等级"><@spring.message "base.levelNormal"/></option>
								<option value="会员等级1"><@spring.message "base.level1"/></option>
								<option value="会员等级2"><@spring.message "base.level2"/></option>
								<option value="会员等级3"><@spring.message "base.level3"/></option>
								<option value="会员等级4"><@spring.message "base.level4"/></option>
								<option value="会员等级5"><@spring.message "base.level5"/></option>
								<option value="会员等级6"><@spring.message "base.level6"/></option>
								<option value="会员等级7"><@spring.message "base.level7"/></option>
								<option value="会员等级8"><@spring.message "base.level8"/></option>
								<option value="会员等级9"><@spring.message "base.level9"/></option>
								<option value="会员等级10"><@spring.message "base.level10"/></option>
								<option value="会员等级11"><@spring.message "base.level11"/></option>
								<option value="会员等级12"><@spring.message "base.level12"/></option>
								<option value="会员等级13"><@spring.message "base.level13"/></option>
								<option value="会员等级14"><@spring.message "base.level14"/></option>
								<option value="会员等级15"><@spring.message "base.level15"/></option>
								<option value="会员等级16"><@spring.message "base.level16"/></option>
								<option value="会员等级17"><@spring.message "base.level17"/></option>
								<option value="会员等级18"><@spring.message "base.level18"/></option>
								<option value="会员等级19"><@spring.message "base.level19"/></option>
								<option value="会员等级20"><@spring.message "base.level20"/></option>
							</select>
						</td>
					</tr>

					<#if defaultDegree.type== 1 || defaultDegree.type == 3>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.depositMoney"/>：</td>
						<td ><input name="depositMoney" class="form-control" /></td>
					</tr>
					</#if>
					<#if defaultDegree.type == 2 || defaultDegree.type == 3>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.betNum"/>：</td>
						<td ><input name="betNum" class="form-control" /></td>
					</tr>
					</#if>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.degree.icon"/>：</td>
						<td><input type="text" name="icon" class="form-control" placeholder="<@spring.message "admin.degree.iconPath"/>"/>
							<font color="red"><@spring.message "admin.degree.iconSize"/></font></td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.degree.sendMsg1"/>：</td>
                        <td >
                            <label class="radio-inline">
                                <input type="radio" name="upgradeSendMsg" checked value="1"> <@spring.message "admin.degree.sendMsg.no"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="upgradeSendMsg" value="2"> <@spring.message "admin.degree.sendMsg.yes"/>
                            </label>
						</td>
                    </tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.degree.upgradeMoney"/>：</td>
						<td ><input name="upgradeMoney" class="form-control"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
						<td ><input name="betRate" class="form-control" value="1"/></td>
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
