<form action="${adminBase}/activity/addSave.do" class="form-submit" id="agent_artivity_add_formId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.discount.act"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="35%" class="text-left"><input type="text" name="title" class="form-control"/></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.title.picture"/>：</td>
                        <td width="35%" class="text-left"><input type="text" name="titleImgUrl" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td class="text-left"><div class="input-group">
                            <input type="text" class="form-control fui-date" value="${beginDate }" name="updateTime" placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td class="text-left"><div class="input-group">
                            <input type="text" class="form-control fui-date" name="overTime" value="${endDate }" placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                    </tr>
                    <td class="text-right media-middle"><@spring.message "admin.index.select.language"/>：</td>
                    <td >
                        <select name="language" class="form-control">
                            <#list languages?keys as key>
                                <option value="${key}">${languages[key]}</option>
                            </#list>
                        </select>
                    </td>
                        <td class="text-right media-middle"><@spring.message "admin.deviceType"/>：</td>
                        <td class="text-left">
                            <select name="deviceType" class="form-control">
                                <#list deviceTypes as deviceType>
                                    <option value="${deviceType.type}"><@spring.message "admin.${deviceType.desc}"/></option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="2" selected><@spring.message "admin.enable"/></option>
                            <option value="1"><@spring.message "admin.disabled"/></option>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td class="text-left"><input type="text" name="sortNo" class="form-control digits"/></td>
                    </tr>
                    <tr>
                        <td class="text-left" colspan="4"><script name="content" id="agent_artivity_add_editor" type="text/plain" style="width:860px;height:300px;"></script></td>
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
<script>
    require(['ueditor_zh'], function(){
    var $form=$("#agent_artivity_add_formId");
    $form.find(".fui-close").data("cancelFun",function(){
    UM.delEditor('agent_artivity_add_editor');
    })
    var ue = UM.getEditor('agent_artivity_add_editor');
    $form.data("paramFn",function(){
    var paramArr=$form.serializeArray()
    var params={};
    $.each( paramArr, function(i, field){
    params[field.name]=field.value;
    });
    params.content=ue.getContent();
    params.status=$form.find("[name='status']").val();
    return params;
    });
    })
</script>