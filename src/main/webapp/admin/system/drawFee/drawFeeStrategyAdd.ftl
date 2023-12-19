<form action="${adminBase}/drawFeeStrategy/doAdd.do" class="form-submit" id="draw_fee_strategy_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.draw.fee.strategy.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.fee.type"/>：</td>
                        <td width="30%">
                            <select  name="feeType" class="form-control">
                                <option value="2" selected><@spring.message "admin.draw.fee.float"/></option>
                                <option value="1"><@spring.message "admin.draw.fee.fixed"/></option>
                            </select>
                        </td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.draw.fee.value"/>：</td>
                        <td width="30%"><input name="feeValue" type="number" placeholder="<@spring.message "admin.float.fee.value.per"/>" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.fee.draw.count"/>：</td>
                        <td><input name="drawNum" type="number"  placeholder="<@spring.message "admin.blank.zero.fee"/>" class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
                        <td><input  name="remark" class="form-control"/></td>
                    </tr>
                    <tr class="fixed_fee_type">
                        <td class="text-right media-middle"><@spring.message "admin.draw.fee.top"/>：</td>
                        <td><input placeholder="<@spring.message "admin.blank.zero.fee.not.limit"/>" type="number" type="text" name="upperLimit"class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.draw.fee.down"/>：</td>
                        <td><input placeholder="<@spring.message "admin.blank.zero.fee.min.limit"/>" type="number" name="lowerLimit"class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/>：</td>
                        <td colspan="3">
                            <#list degrees as le>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="degreeIds" value="${le.id }" checked>${le.name }
                                </label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/>：</td>
                        <td colspan="3">
                            <#list groups as le>
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="groupIds" value="${le.id }" checked>${le.name }
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
    var $form = $("#draw_fee_strategy_form_id");
    $form.find("[name='feeType']").change(function () {
        if ($(this).val() == 1) {
            $form.find(".fixed_fee_type").hide();
        } else {
            $form.find(".fixed_fee_type").show();
        }
    });
});
</script>