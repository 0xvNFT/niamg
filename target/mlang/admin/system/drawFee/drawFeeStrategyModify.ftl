<form action="${adminBase}/drawFeeStrategy/doModify.do" class="form-submit" id="modify_draw_fee_strategy_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.draw.fee"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${fee.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.fee.type"/>：</td>
                        <td width="30%">
                            <select  name="feeType" class="form-control">
                                <option value="2"<#if fee.feeType == 2>selected</#if>><@spring.message "admin.draw.fee.float"/></option>
                                <option value="1"<#if fee.feeType == 1>selected</#if>><@spring.message "admin.draw.fee.fixed"/></option>
                            </select>
                        </td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.draw.fee.value"/>：</td>
                        <td width="30%"><input name="feeValue" type="number" placeholder="<@spring.message "admin.float.fee.value.per"/>" value="${fee.feeValue}" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.fee.draw.count"/>：</td>
                        <td><input name="drawNum" type="number"  placeholder="<@spring.message "admin.blank.zero.fee"/>" value="${fee.drawNum }" class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
                        <td colspan="3"><input  name="remark" value="${fee.remark }" class="form-control"/></td>
                    </tr>
                    <tr class="fixed_fee_type">
                        <td class="text-right media-middle"><@spring.message "admin.draw.fee.top"/>：</td>
                        <td><input placeholder="<@spring.message "admin.blank.zero.fee.not.limit"/>" type="number" type="text" name="upperLimit" value="${fee.upperLimit }"class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.draw.fee.down"/>：</td>
                        <td><input placeholder="<@spring.message "admin.blank.zero.fee.min.limit"/>" type="number" name="lowerLimit" value="${fee.lowerLimit }"class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/>：</td>
                        <td colspan="3">
                            <#list degrees as le>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="degreeIds" value="${le.id }"<#if degreeSet?seq_contains(le.id)>checked</#if>>${le.name }
                                </label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/>：</td>
                        <td colspan="3">
                            <#list groups as le>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="groupIds" value="${le.id }"<#if groupSet?seq_contains(le.id)>checked</#if>>${le.name }
                                </label>
                            </#list>
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
    requirejs(['jquery'], function () {
        var $form = $("#modify_draw_fee_strategy_form_id");
        if ('${fee.feeType}' && '${fee.feeType}' =='1') {
            $form.find(".fixed_fee_type").hide();
        }
        $form.find("[name='feeType']").change(function () {
            debugger;
            if ($(this).val() == 1) {
                $form.find(".fixed_fee_type").hide();
            } else {
                $form.find(".fixed_fee_type").show();
            }
        });
    });
</script>