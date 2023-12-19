<style>
    input[type="checkbox"] {
        position: relative;
        margin-right: 9px;
        margin-top: 5px;
    }

    element.style {
        z-index: 19891021;
        top: 136px;
        left: 510px;
    }
</style>
<form action="${adminBase}/replaceWithdraw/save.do" class="form-submit" id="replaceWithdrawAdd_from">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.replaceWithdraw.addsettings"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.online.title"/>：</td>
                        <td width="30%">
                            <select id="icon" name="icon" class="form-control selectpicker"
                                    data-live-search="true">
                                <#list payCombos as pay>
                                    <option value="${pay.code }">${pay.payName }</option>
                                </#list>
                            </select></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.online.merchantcode"/>：</td>
                        <td width="30%"><input placeholder="<@spring.message "admin.deposit.online.enter.correct.merchantcode"/>" name="merchantCode" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.merchantkey.identification"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.merchantkey.privatekey"/>" name="merchantKey"
                                               class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.payment.accountnumber"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.payment.accountnumber.emailaddress"/>" name="account"
                                               class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">APPID：</td>
                        <td><input placeholder="APPID" name="appid" class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td><select name="status" class="form-control">
                                <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                                <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                            </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.interfacedomain"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.correct.interfacedomain"/>" type="text" name="url" class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.alternative.parameters"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.alternative.parameters"/>" type="text" name="extra"
                                   class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.minimum.paymentamount"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.min.right"/>" name="min" class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.maximum.paymentamount"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.max.right"/>" name="max" class="form-control money"/></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.payment.gateway"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.correct.payment.gateway"/>" type="text" name="payGetway"
                                               class="form-control"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.payment.inquiry.gateway"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.menu.replaceWithdraw.enter.correct.inquirygateway"/>" name="searchGetway" class="form-control"
                                               type="text"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.whitelist.ip"/>：</td>
                        <td colspan="3"><input id="whiteListIp" name="whiteListIp" class="form-control"
                                               type="text"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.def"/>：</td>
                        <td><select name="def" class="form-control">
                                <option value="1"><@spring.message "admin.withdraw.info.boolean.no"/></option>
                                <option value="2" selected><@spring.message "admin.withdraw.info.boolean.yes"/></option>
                            </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.sortNo.tip"/>" name="sortNo" class="form-control digits" type="number"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td colspan="3"><input type="text" name="remark"
                                               class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>
                            ：
                        </td>
                        <td colspan="3">
                            <#list degrees as d>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${d.id }"
                                                                      checked>${d.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.groupIds"/>：
                        </td>
                        <td colspan="3">
                            <#list groups as g>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${g.id }"
                                                                      checked>${g.name }</label>
                            </#list>
                        </td>
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
<script type="text/javascript">
    $(document).ready(function () {
        requirejs(['jquery'], function () {
            var $form = $("#replaceWithdrawAdd_from");
            $form.find("[name='icon']").change(function () {
                var $it = $(this)
                    , payId = $it.val()
                    , iconCss = $it.find("option[value=" + payId + "]").attr("iconCss");
                $form.find(".third-pay").removeClass("hidden");
            }).change();
        });
    })
    $("#payPlatformCode").change(function () {//点击下拉框获取值
        // getPaymentTypeAjax()
    });

</script>