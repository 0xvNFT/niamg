    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.detail.advice"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.advice.types"/>：</td>
                        <td class="text-left"><select name="sendType" class="form-control" disabled>
                            <option value="1"<#if advcie.sendType == 1>selected</#if>><@spring.message "admin.submit.advice"/></option>
                            <option value="2"<#if advcie.sendType == 2>selected</#if> ><@spring.message "admin.complaint.me"/></option>
                        </select></td>
                    </tr>
                    <tr class="accountT1">
                        <td class="text-right media-middle"><@spring.message "admin.submit.person"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="sendAccount"
                                                     value="${advcie.sendAccount}" disabled/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.advice.content"/>：</td>
                        <td class="text-left"><textarea class="form-control" name="content" style="height: 120px;"
                                                        disabled>${advcie.content}</textarea></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
