<form action="${adminBase}/article/addSave.do" class="form-submit" id="agent_article_add_formId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="35%" class="text-left"><input type="text" name="title" class="form-control"/></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.type"/>：</td>
                        <td width="35%" class="text-left">
                            <select name="type" class="form-control">
                                <#list types as type>
                                    <option value="${type.code}">${type.name}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr class="hidden isShowTc">
                        <td class="text-right media-middle"><@spring.message "admin.page.display.position"/>：</td>
                        <td class="text-left" colspan='3'>
                            <div>
                                <label class="checkbox-inline">
                                    <input type="checkbox" checked="checked" name="isIndex" value="true"> <@spring.message "admin.main.page"/>
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" checked="checked" name="isReg" value="true"> <@spring.message "admin.regist.page"/>
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" checked="checked" name="isDeposit" value="true"> <@spring.message "admin.charge.page"/>
                                </label>
                                <label class="checkbox-inline">
                                    <input type="checkbox" checked="checked" name="isBet" value="true"> <@spring.message "admin.pay.page"/>
                                </label>
                            </div>
                        </td>
                    </tr>
                    <tr class="hidden isShowTc">
                        <td class="text-right media-middle"><@spring.message "admin.limit.vip.level"/>：</td>
                        <td colspan="3">
                            <#list degrees as l>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${l.id }">${l.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr class="hidden isShowTc">
                        <td class="text-right media-middle"><@spring.message "admin.limit.vip.group.level"/>：</td>
                        <td colspan="3">
                            <#list groups as l>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${l.id }">${l.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.index.select.language"/>：</td>
                        <td >
                            <select name="language" class="form-control">
                            <#list languages?keys as key>
                                <option value="${key}">${languages[key]}</option>
                            </#list>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.popupStatus"/>：</td>
                        <td class="text-left">
                            <select name="popupStatus" class="form-control">
                                <option value="2" <#if type != 13>selected</#if>><@spring.message "admin.enable"/></option>
                                <option value="1" <#if type == 13>selected</#if>><@spring.message "admin.disabled"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td class="text-left">
                            <div class="input-group">
                                <input type="text" class="form-control fui-date" value="${beginDate}" name="updateTime"
                                       placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback"
                                                                 aria-hidden="true"></span>
                            </div>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td class="text-left">
                            <div class="input-group">
                                <input type="text" class="form-control fui-date" value="${endDate }" name="overTime"
                                       placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback"
                                                                 aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="2" selected><@spring.message "admin.enable"/></option>
                            <option value="1"><@spring.message "admin.disabled"/></option>
                        </select></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.serial.val.max.end"/>：</td>
                        <td width="35%" class="text-left">
                        	<input type="text" name="sortNo" class="form-control" value="${article.sortNo }"/>
                        </td>                        
                    </tr>
                    <tr>
                        <td class="text-left" colspan="4">
                            <script name="content" id="agent_article_add_editor" type="text/plain" class="pre-scrollable"
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
</form>
<script type="application/javascript">
    require(['ueditor_zh'], function () {
        var $form = $("#agent_article_add_formId");
        $form.find(".fui-close").data("cancelFun", function () {
            UM.delEditor('agent_article_add_editor');
        });
        $form.find("[name='type']").change(function () {
            if ($(this).val() == 9 || $(this).val() == 19) {
                $form.find(".isShowTc").removeClass("hidden");
            } else {
                $form.find(".isShowTc").addClass("hidden");
            }
            updatePopupStatus();
        });

        function updatePopupStatus() {
            var type = $form.find("[name='type']").val();
            var popupStatusDropdown = $form.find("[name='popupStatus']");
            if (type == 9 || type == 19) {
                popupStatusDropdown.prop('disabled', false); // Enable the dropdown
                popupStatusDropdown.val(2);
            } else {
                popupStatusDropdown.val(1);
                popupStatusDropdown.prop('disabled', true); // Disable the dropdown
            }
        }
        updatePopupStatus();

        var um = UM.getEditor('agent_article_add_editor');
        $form.data("paramFn", function () {
            var paramArr = $form.serializeArray();
            var params = {};
            var groupDegreeIds="";
            var groupDegree="";
            $.each(paramArr, function (i, field) {
                params[field.name] = field.value;
                if(field.name=='degreeIds'){
                    groupDegreeIds+=field.value+",";
                }
                if(field.name=='groupIds'){
                    groupDegree+=field.value+",";
                }
            });
            params.content = um.getContent();
            params.modelStatus = $form.find("[name='status']").val();
            params.degreeIds = groupDegreeIds.substring(0, groupDegreeIds.length - 1);
            params.groupIds = groupDegree.substring(0, groupDegree.length - 1);
            return params;
        });
    })
</script>
