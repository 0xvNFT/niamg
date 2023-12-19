<form action="${adminBase}/agent/doModify.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.modify.agent"/></h4>
            </div>
            <input type="hidden" name="id" value="${agent.id }">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.deposit.agent"/>：</td>
                        <td><input name="name" class="form-control required" type="text" value="${agent.name}"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.code.extend"/>：</td>
                        <td>
                            <input type="text" class="form-control" name="promoCode" placeholder="<@spring.message "admin.only.num.letter"/>" value="${agent.promoCode}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.link.access.page"/>：</td>
                        <td>
                            <select name="accessPage" class="form-control">
                                <option value="1"<#if agent.accessPage==1>selected</#if>><@spring.message "admin.domain.homeRegister"/></option>
                                <option value="2"<#if agent.accessPage==2>selected</#if>><@spring.message "admin.main.page"/></option>
                                <option value="3"<#if agent.accessPage==3>selected</#if>><@spring.message "admin.discount.activit"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td><input name="remark" class="form-control" type="text"value="${agent.remark}"/></td>
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