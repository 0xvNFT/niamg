<form action="${adminBase}/user/doModifyDegree.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.member.level"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${member.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                        <td>
                            <select name="degreeId" class="form-control">
                                <#list degrees as l>
                                    <option value="${l.id }"<#if l.id==member.degreeId>selected</#if>>${l.name }</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                         <td class="text-right media-middle"><@spring.message "admin.lock.level"/>：</td>
                         <td>
                             <label class="radio-inline">
                                 <input type="radio" name="lockDegree"<#if member.lockDegree==1> checked</#if> value="1"><@spring.message "admin.not.lock"/>
                             </label>
                             <label class="radio-inline">
                                 <input type="radio" name="lockDegree"<#if member.lockDegree==2> checked</#if> value="2"><@spring.message "admin.deposit.status.be.lock"/>
                             </label>
                             <div class="text-danger"><@spring.message "admin.lock.pay.level.not.change"/></div>
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