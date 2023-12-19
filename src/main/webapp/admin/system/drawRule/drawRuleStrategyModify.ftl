<form action="${adminBase}/drawRuleStrategy/doModify.do" class="form-submit" id="modify_draw_rule_strategy_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
<#--                <h4 class="modal-title"><@spring.message "admin.draw.fee"/></h4>-->
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${fee.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.type"/>：</td>
                        <td width="30%">

                            <select  name="type" class="form-control">
                                <#list types as item>
                                    <option value="${item.type}" <#if item.type == fee.type>selected</#if>>${item.ruleName }</option>
                                </#list>
                            </select>
                        </td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.config.val"/>：</td>
                        <td width="30%"><input name="value" type="number" value="${fee.value}" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.draw.rule.limit.country"/>：</td>
                        <td><input name="limitCountry" value="${fee.limitCountry }" placeholder="<@spring.message "admin.draw.rule.limit.country.code"/>" class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
                        <td><input  name="remark" value="${fee.remark }" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.unit.day"/>：</td>
                        <td width="30%"><input name="days" value="${fee.days }" type="number" placeholder="<@spring.message "admin.howmany.day"/>" class="form-control"/></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.draw.rule.rate"/>：</td>
                        <td width="30%"><input name="rate" value="${fee.rate }" type="number" placeholder="0-1" class="form-control"/></td>
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
        <#--var $form = $("#modify_draw_rule_strategy_form_id");-->
        <#--if ('${fee.type}' && '${fee.type}' =='1') {-->
        <#--    $form.find(".fixed_fee_type").hide();-->
        <#--}-->
        // $form.find("[name='type']").change(function () {
        //     debugger;
        //     if ($(this).val() == 1) {
        //         $form.find(".fixed_fee_type").hide();
        //     } else {
        //         $form.find(".fixed_fee_type").show();
        //     }
        // });
    });
</script>