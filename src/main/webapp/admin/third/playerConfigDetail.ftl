<form action="${adminBase}/playerConfig/save.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.insert.player.config"/></h4>
            </div>
            <input type="hidden" value="${config.id}" name="id">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.deposit.username"/>：</td>
                        <td width="35%" class="text-left">
                            <input name="username" value="${config.username}" class="form-control required" type="text">
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.config.item"/>：</td>
                        <td width="35%" class="text-left">
                            <select name="configName" class="form-control">
                                <#list configs as conf>
                                    <option value="${conf.name}" data-type="${conf.type}"
                                            <#if config.configName == conf.name>selected</#if>>${conf.label}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.config.val"/>：</td>
                        <td width="35%" class="text-left configValue">
                            <input name="configValue" value="${config.configValue}" class="form-control required"
                                   type="text">
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
<script>
    var defConfigValue = '${config.configValue}';
    requirejs(['${base}/common/js/admin/thirdConfig.js'], function () {
        configSelect();
        $("select[name='configName']").change(function () {
            configSelect();
        });
    });
</script>
