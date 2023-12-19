<form action="${managerBase}/announcement/save.do" class="form-submit" id="announcement_add_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.station.notice.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
                        <td><select name="type" class="form-control">
                                <option value="1" selected><@spring.message "manager.all.send"/></option>
                                <option value="2"><@spring.message "manager.point.place"/></option>
                            </select></td>
                        <td class="text-right media-middle"><@spring.message "manager.is.not.out"/>：</td>
                        <td><select name="dialogFlag" class="form-control">
                                <option value="1" selected><@spring.message "admin.withdraw.info.boolean.yes"/></option>
                                <option value="2"><@spring.message "admin.withdraw.info.boolean.no"/></option>
                            </select></td>
                    </tr>
                    <tr class="hidden station_wrap">
                        <td class="text-right media-middle"><@spring.message "manager.station.selected"/>：</td>
                        <td colspan="3"><select name="stationIds" title="<@spring.message "manager.station.selected"/>" class="form-control selectpicker" data-live-search="true" multiple="multiple" data-max-options="100">
                                <#list stations as s><option value="${s.id}">${s.name}</option></#list></select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.save.station.types"/>：</td>
                        <td colspan="3">
                            <input type="checkbox" name="acceptType" value="1"checked><@spring.message "manager.live"/>&nbsp;
                            <input type="checkbox" name="acceptType" value="2"checked><@spring.message "manager.egame"/>&nbsp;&nbsp;
                            <input type="checkbox" name="acceptType" value="3"checked><@spring.message "manager.sport"/>&nbsp;&nbsp;
                            <input type="checkbox" name="acceptType" value="4"checked><@spring.message "manager.chess"/>&nbsp;&nbsp;
                            <input type="checkbox" name="acceptType" value="5"checked><@spring.message "manager.esport"/>&nbsp;&nbsp;
                            <input type="checkbox" name="acceptType" value="6"checked><@spring.message "manager.fish"/>&nbsp;&nbsp;
                            <input type="checkbox" name="acceptType" value="7"checked><@spring.message "manager.lottery"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.begin.time"/>：</td>
                        <td><input type="text" class="form-control fui-date" name="startDatetime"
                                   format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.begin.time"/>"></td>
                        <td class="text-right media-middle"><@spring.message "manager.end.time"/>：</td>
                        <td><input type="text" class="form-control fui-date" name="endDatetime"
                                   format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.end.time"/>"></td>
                    </tr>

                    <tr>
                        <td class="text-left" colspan="4">
                        	<script name="content" id="manager_announcement_add_editor" type="text/plain" style="width:860px;height:300px;"></script>
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
<script type="text/javascript">
    require(['ueditor_zh'], function () {
        var form = $("#announcement_add_form_id");
        form.find("[name='type']").change(function () {
            if ($(this).val() == 1) {
                form.find(".station_wrap").addClass("hidden");
            } else {
                form.find(".station_wrap").removeClass("hidden");
            }
        });
        form.find(".fui-close").data("cancelFun", function () {
            UM.delEditor('manager_announcement_add_editor');
        });
        var ue = UM.getEditor('manager_announcement_add_editor');
        form.data("paramFn", function () {
            var paramArr = form.serializeArray(),params = {},stationIds = '',acceptType='';
            $.each(paramArr, function (i, field) {
                if (field.name == 'stationIds') {
                    stationIds += ","+field.value;
                }else if (field.name == 'acceptType') {
                	acceptType += ","+field.value;
                }else {
                    params[field.name] = field.value;
                }
            });
            if (stationIds && stationIds != '') {
                stationIds = stationIds.substring(1);
                params['stationIds'] = stationIds;
            }
            if(acceptType && acceptType!=''){
            	acceptType=acceptType.substring(1);
            	params['acceptType']=acceptType;
            }
            params.content = ue.getContent();
            return params;
        });
    });
</script>
