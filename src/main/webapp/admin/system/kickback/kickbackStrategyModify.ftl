<form action="${adminBase}/kickbackStrategy/modifySave.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.daily.strategy.back"/></h4>
            </div>
            <div class="modal-body">

                <table class="table table-bordered table-striped">
                    <input name="id" type="hidden" value="${ks.id}">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.backwater.types"/>：</td>
                        <td>
                            <select name="type" class="form-control">
                            	<#if game.live==2><option value="1"<#if ks.type == 1>selected</#if>><@spring.message "manager.live"/></option></#if>
								<#if game.egame==2><option value="2"<#if ks.type == 2>selected</#if>><@spring.message "manager.egame"/></option></#if>
								<#if game.chess==2><option value="3"<#if ks.type == 3>selected</#if>><@spring.message "manager.chess"/></option></#if>
								<#if game.fishing==2><option value="4"<#if ks.type == 4>selected</#if>><@spring.message "manager.fish"/></option></#if>
								<#if game.esport==2><option value="5"<#if ks.type == 5>selected</#if>><@spring.message "manager.esport"/></option></#if>
								<#if game.sport==2><option value="6"<#if ks.type == 6>selected</#if>><@spring.message "manager.sport"/></option></#if>
								<#if game.lottery==2><option value="7"<#if ks.type ==7>selected</#if>><@spring.message "manager.lottery"/></option></#if>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.backwater.percent"/>(%):</td>
                        <td><input placeholder="<@spring.message "admin.input.right.percent"/>" value="${ks.kickback}" name="kickback" type="number" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.equals.weight"/>：</td>
                        <td><input type="number" name="minBet" value="${ks.minBet}"  class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
                        <td><input placeholder="<@spring.message "admin.blank.not.limited.zero"/>" type="number" name="betRate" value="${ks.betRate}" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.max.backwater.up"/>：</td>
                        <td><input type="number" name="maxBack" placeholder="<@spring.message "admin.blank.not.limited.zero"/>" value="${ks.maxBack}" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.table.status"/>：</td>
                        <td><select name="status" class="form-control">
                            <option value="1" <#if ks.status ==1>selected</#if>><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                            <option value="2" <#if ks.status ==2>selected</#if>><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.level"/>：</td>
                        <td colspan="3"><#list degrees as l><label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${l.id}"<#if ks.degreeIds?has_content && ks.degreeIds?contains(","+l.id+",")>checked</#if>> ${l.name}</label></#list></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/>：</td>
                        <td colspan="3"><#list groups as le><label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${le.id }"<#if ks.groupIds?has_content && ks.groupIds?contains(","+le.id+",")>checked</#if>>${le.name}</label></#list></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.describe"/>：</td>
                        <td colspan="3"><textarea name="remark" class="form-control">${ks.remark}</textarea></td>
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
