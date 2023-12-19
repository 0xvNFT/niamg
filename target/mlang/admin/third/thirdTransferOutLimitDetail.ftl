<form action="${adminBase}/transferOutLimit/save.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.new.add.third.val"/></h4>
            </div>
            <input type="hidden" value="${limit.id}" name="id">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.platform.sys"/>：</td>
                        <td width="35%" class="text-left">
                            <select name="platform" class="form-control">
                                <option value=""><@spring.message "admin.select.platform.sys"/></option>
                                 <#list platforms as p>
                                     <option value="${p.value}" <#if limit.platform==p.value>selected</#if>>${p.title}</option>
                                 </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.min.turn.val"/>：</td>
                        <td width="35%" class="text-left">
                            <input name="minMoney" value="${limit.minMoney}" class="form-control required" type="text">
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.max.turn.val"/>：</td>
                        <td width="35%" class="text-left">
                            <input name="maxMoney" value="${limit.maxMoney}" class="form-control required" type="text">
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.limit.vip.member"/>：</td>
                        <td width="35%" class="text-left">
                            <textarea rows="5" cols="5" class="form-control" placeholder="<@spring.message "admin.config.vip.acc.valid"/>," name="limitAccounts">${limit.limitAccounts }</textarea>
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
