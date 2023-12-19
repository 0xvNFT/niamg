<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
            <h4 class="modal-title"><@spring.message "manager.info.view"/></h4>
        </div>
        <div class="modal-body">
            <table class="table table-bordered table-striped">
                <tbody>
                    <#list data as feedback>
                        <tr>
                            <#if (feedback.type == 1)>
                                <td width="30%" class="text-right vmid"><@spring.message "admin.member"/>：</td>
                            <#else>
                                <td width="30%" class="text-right vmid"><@spring.message "admin.admins"/>：</td>
                            </#if>
                            <td><textarea disabled style="height:150px;width: 100%">${feedback.content}</textarea></td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
        </div>
    </div>
</div>
