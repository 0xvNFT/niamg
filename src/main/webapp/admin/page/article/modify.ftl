<form action="${adminBase}/article/modifySave.do" class="form-submit" id="agent_article_edit_formId">
    <div class="modal-dialog modal-lg"><input name="id" type="hidden" value="${article.id}">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.bank.bankCard.modify"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="35%" class="text-left"><input type="text" name="title" class="form-control"
                                                                 value="${article.title }"/></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
                        <td class="text-left">
                            <select name="type" class="form-control" disabled="disabled">
                                <#list types as type>
                                    <option value="${type.code}" <#if type.code == article.type>selected</#if>>${type.name}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr class="hidden isShowTc">
                        <td class="text-right media-middle"><@spring.message "admin.page.display.position"/>：</td>
                        <td class="text-left" colspan='3'>
                            <div>
                                <label class="checkbox-inline">
                                    <input type="checkbox"
                                    <#if article.index > checked="checked"</#if>
                                    name="index" value="true"> <@spring.message "admin.main.page"/>
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox"
                                    <#if article.reg > checked="checked"</#if>
                                    name="reg" value="true"> <@spring.message "admin.member.register.page"/>
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox"
                                    <#if article.deposit > checked="checked"</#if>
                                    name="deposit" value="true"> <@spring.message "admin.charge.page"/>
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox"
                                    <#if article.bet > checked="checked"</#if>
                                    name="bet" value="true"> <@spring.message "admin.pay.page"/>
                                </label>
                            </div>
                        </td>
                    </tr>
                    <tr class="hidden isShowTc">
                        <td class="text-right media-middle"><@spring.message "admin.limit.vip.level"/>：</td>
                        <td colspan="3">
                            <#list degrees as l>
                            <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${l.id }"
                            <#if article.degreeIds?has_content && article.degreeIds?contains(","+l.id+",")>
                                checked="checked"
                            </#if>
                            >${l.name }</label>
                        </#list>
                        </td>
                    </tr>
                    <tr class="hidden isShowTc">
                        <td class="text-right media-middle"><@spring.message "admin.limit.vip.group.level"/>：</td>
                        <td colspan="3">
                            <#list groups as l>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${l.id }"
                                <#if article.groupIds?has_content && article.groupIds?contains(","+l.id+",")>checked="checked"</#if>
                                >${l.name }</label>
                            </#list>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.index.select.language"/>：</td>
                        <td>
                            <select name="language" class="form-control">
                                <#list languages?keys as key>
                                    <option <#if article.language!=null && key==article.language>selected</#if> value="${key}">${languages[key]}</option>
                                </#list>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.popupStatus"/>：</td>
                        <td class="text-left">
                            <select name="popupStatus" class="form-control" <#if article.type != 9 && article.type !=19>disabled="disabled"</#if>>
                                <option value="2" <#if article.popupStatus == 2>selected</#if>><@spring.message "admin.enable"/></option>
                                <option value="1" <#if article.popupStatus == 1 || (article.type != 9 && article.type !=19)>selected</#if>><@spring.message "admin.disabled"/></option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td class="text-left">
                            <div class="input-group">
                                <input type="text" class="form-control fui-date" name="updateTime"
                                       value="${article.updateTime}"
                                placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback"
                                                          aria-hidden="true"></span>
                            </div>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td class="text-left">
                            <div class="input-group">
                                <input type="text" class="form-control fui-date" name="overTime"
                                       value="${article.overTime}"
                                placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback"
                                                          aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="2"<#if article.status==2>selected</#if>><@spring.message "admin.enable"/></option>
                            <option value="1"<#if article.status==1>selected</#if>><@spring.message "admin.disabled"/></option>
                        </select></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.serial.val.max.end"/>：</td>
                        <td width="35%" class="text-left">
                        	<input type="text" name="sortNo" class="form-control" value="${article.sortNo }"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-left" colspan="4">
                            <script name="content" id="agent_article_edit_editor" type="text/plain"
                                    style="width:860px;height:300px;"></script></td>
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
    <div class="hidden edit_content">${article.content}</div>
</form>
<script type="application/javascript">
    require(['ueditor_zh'], function(){
    var $form=$("#agent_article_edit_formId");
    $form.find(".fui-close").data("cancelFun",function(){
    UM.delEditor('agent_article_edit_editor');
    })
    var $code=$form.find("[name='code']");
    $code.change(function(){
    if($(this).val() == 9 || $(this).val() == 19){
    $form.find(".isShowTc").removeClass("hidden");
    }else{
    $form.find(".isShowTc").addClass("hidden");
    }
    });
    $code.val("${article.code}").change().prop("disabled",true);
    var ue = UM.getEditor('agent_article_edit_editor');
    ue.ready( function( editor ) {
    ue.setContent($form.find(".edit_content").html());
    $form.find(".edit_content").remove();
    });
    $form.data("paramFn",function(){
    var paramArr=$form.serializeArray()
    var params={};
    var groupDegreeIds="";
    var groupDegarees="";
    $.each( paramArr, function(i, field){
    params[field.name]=field.value;
    if(field.name=='degreeIds'){
        groupDegreeIds+=field.value+",";
    }
    if(field.name=='groupIds'){
        groupDegarees+=field.value+",";
    }
    });
    params.content=ue.getContent();
    params.degreeIds = groupDegreeIds.substring(0, groupDegreeIds.length - 1);
    params.groupIds = groupDegarees.substring(0, groupDegarees.length - 1);
    return params;
    });
    })
</script>
