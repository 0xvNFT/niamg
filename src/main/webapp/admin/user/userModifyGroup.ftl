<form action="${adminBase}/user/doModifyGroup.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.member.group"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${member.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.vip.group"/>：</td>
                        <td>
                            <select name="groupId" class="form-control">
                                <#list groups as l>
                                    <option value="${l.id }"<#if l.id==member.groupId>selected</#if>>${l.name }</option>
                                </#list>
                            </select>
                        </td>
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