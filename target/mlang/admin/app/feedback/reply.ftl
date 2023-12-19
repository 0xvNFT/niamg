<form action="${adminBase}/appFeedback/saveReply.do" class="form-submit">
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
            <h4 class="modal-title"><@spring.message "manager.info.view"/></h4>
        </div>
        <input type="hidden" name="id" value="${data.id}">
        <div class="modal-body">
            <table class="table table-bordered table-striped">
                <tbody>
                    <tr>
                        <#if (data.type == 1)>
                            <td width="30%" class="text-right vmid"><@spring.message "admin.member"/>：</td>
                            <td><textarea disabled style="height:150px;width: 100%">${data.content}</textarea></td>
                        <#else>
                            <td width="30%" class="text-right vmid"><@spring.message "admin.admins"/>：</td>
                            <td><textarea style="height:150px;width: 100%">${data.content}</textarea></td>
                        </#if>
                    </tr>
                    <tr>
                        <td width="30%" class="text-right vmid"><@spring.message "admin.reply.content"/>：</td>
                        <td><input name="content" placeholder="<@spring.message "admin.input.reply.content"/>。。。。" class="form-control" type="text"></td>
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
