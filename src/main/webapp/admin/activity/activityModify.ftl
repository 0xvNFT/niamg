<form action="${adminBase}/activity/modifySave.do" class="form-submit" id="agent_artivity_edit_formId">
    <div class="modal-dialog modal-lg"><input name="id" type="hidden" value="${activity.id}">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.modify.discount.act"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="35%" class="text-left"><input type="text" name="title" class="form-control" value="${activity.title}"/></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.title.picture"/>：</td>
                        <td width="35%" class="text-left"><input type="text" name="titleImgUrl" value="${activity.titleImgUrl}"class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td class="text-left"><div class="input-group">
                            <input type="text" class="form-control fui-date" name="updateTime" value="${activity.updateTime}"placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td class="text-left"><div class="input-group">
                            <input type="text" class="form-control fui-date" name="overTime" value="${activity.overTime}"placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.index.select.language"/>：</td>
                        <td >
                            <select name="language" class="form-control">
                                <#list languages?keys as key>
                                    <option <#if activity.language!=null && key==activity.language>selected</#if> value="${key}">${languages[key]}</option>
                                </#list>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.deviceType"/>：</td>
                        <td class="text-left">
                            <select name="deviceType" class="form-control">
                                <#list deviceTypes as deviceType>
                                    <option value="${deviceType.type}" <#if activity.deviceType == deviceType.type>selected</#if>>
                                        <@spring.message "admin.${deviceType.desc}"/>
                                    </option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="2"<#if activity.status==2 >selected</#if>><@spring.message "admin.enable"/></option>
                            <option value="1"<#if activity.status==1 >selected</#if>><@spring.message "admin.disabled"/></option>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td class="text-left"><input type="text" name="sortNo" value="${activity.sortNo}"class="form-control digits"/></td>
                    </tr>
                    <tr>
                        <td class="text-left" colspan="4"><script name="content" id="agent_artivity_edit_editor" type="text/plain" style="width:860px;height:300px;"></script></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div><div class="hidden edit_content">${activity.content}</div>
</form>
<script>
    require(['ueditor_zh'], function(){
    var $form=$("#agent_artivity_edit_formId");
    $form.find(".fui-close").data("cancelFun",function(){
    UM.delEditor('agent_artivity_edit_editor');
    })
    var ue = UM.getEditor('agent_artivity_edit_editor');
    ue.ready( function( editor ) {
    ue.setContent($form.find(".edit_content").html());
    $form.find(".edit_content").remove();
    });
    $form.data("paramFn",function(){
    var paramArr=$form.serializeArray()
    var params={};
    $.each( paramArr, function(i, field){
    params[field.name]=field.value;
    });
    params.content=ue.getContent();
    return params;
    });
    })
</script>
