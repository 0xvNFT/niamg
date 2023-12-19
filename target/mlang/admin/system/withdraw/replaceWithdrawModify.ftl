<style>
    input[type="checkbox"] {
        position: relative;
        margin-right: 9px;
        margin-top: 5px;
    }

    .bankDiv {
        width: 100%;
        height: 64px
    }

    .bankListDiv {
        margin-top: 7px;
        float: left;
        height: 31%;
        width: 20%;
    }

    .bankList {
        position: relative;
        margin-right: 5px;
        margin-top: 10px;
    }

    .pcdiv {
        width: 100%;
        height: 20px
    }

    .pc {
        float: left;
        height: 31%;
        width: 31%
    }

    #whiteListIp::-webkit-input-placeholder {
        color: red;
    }
</style>
<form action="${adminBase}/replaceWithdraw/save.do" class="form-submit" id="replaceWithdrawModify_from">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.replaceWithdraw.modifysettings"/></h4>
            </div>
            <div class="modal-body"><input type="hidden" name="id" value="${online.id }">
                <input type="hidden" disabled="disabled" name="bankListData" value="${online.bankList}">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.online.title"/>：</td>
                        <td width="30%"><select id="icon" name="icon" class="form-control selectpicker"
                                                data-live-search="true">
                                <#list payCombos as pay>
                                    <option value="${pay.code }"
                                            <#if pay.code == online.icon>selected="selected"</#if>>${pay.payName }</option>
                                </#list>
                            </select></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.online.merchantcode"/>：</td>
                        <td width="30%"><input placeholder="<@spring.message "admin.deposit.online.enter.correct.merchantcode"/>" name="merchantCode"
                                               value="${online.merchantCode }" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.merchantkey.identification"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.merchantkey.privatekey"/>" name="merchantKey"
                                               value="${online.merchantKey }" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.payment.accountnumber"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.payment.accountnumber.emailaddress"/>" name="account"
                                               value="${online.account }" class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">APPID：</td>
                        <td><input placeholder="APPID" name="appid" value="${online.appid }" class="form-control">
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td><select name="status" class="form-control">
                                <option value="1"
                                        <#if online.status==1>selected</#if>
                                ><@spring.message "admin.deposit.bank.bankCard.status.no"/>
                                </option>
                                <option value="2"
                                        <#if online.status==2>selected</#if>
                                ><@spring.message "admin.deposit.bank.bankCard.status.yes"/>
                                </option>
                            </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.interfacedomain"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.correct.interfacedomain"/>" type="text" name="url" value="${online.url }"
                                   class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.alternative.parameters"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.alternative.parameters"/>" type="text" name="extra"
                                   value="${online.extra }" class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.minimum.paymentamount"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.min.right"/>" name="min" value="${online.min }"
                                   class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.maximum.paymentamount"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.max.right"/>" name="max" value="${online.max }"
                                   class="form-control money"/></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.payment.gateway"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.correct.payment.gateway"/>" type="text" name="payGetway"
                                               value="${online.payGetway }" class="form-control"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.menu.replaceWithdraw.payment.inquiry.gateway"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.menu.replaceWithdraw.enter.correct.inquirygateway"/>" name="searchGetway" class="form-control"
                                               value="${online.searchGetway }" type="text"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.whitelist.ip"/>：</td>
                        <td colspan="3"><input id="whiteListIp" name="whiteListIp" class="form-control"
                                               value="${online.whiteListIp }" type="text"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.def"/>：</td>
                        <td><select name="def" class="form-control">
                                <option value="1"
                                        <#if online.def==1>selected</#if>
                                ><@spring.message "admin.withdraw.info.boolean.no"/>
                                </option>
                                <option value="2"
                                        <#if online.def==2>selected</#if>
                                ><@spring.message "admin.withdraw.info.boolean.yes"/>
                                </option>
                            </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.sortNo.tip"/>" name="sortNo" value="${online.sortNo }"
                                   class="form-control digits" type="number"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td colspan="3"><input type="text" name="remark"
                                               value="${online.remark }" class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>：</td>
                        <td colspan="3">
                            <#list degrees as d>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${d.id }"
                                                                      <#if online.degreeIds?has_content && online.degreeIds?contains(","+d.id+",")>checked</#if>>${d.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.groupIds"/>：</td>
                        <td colspan="3">
                            <#list groups as g>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${g.id }"
                                                                      <#if online.groupIds?has_content && online.groupIds?contains(","+g.id+",")>checked</#if>>${g.name }</label>
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
            var $form = $("#replaceWithdrawModify_from");
            $form.find("[name='icon']").change(function () {
                var $it = $(this)
                    , payId = $it.val()
                    , iconCss = $it.find("option[value=" + payId + "]").attr("iconCss");
                $form.find(".third-pay").removeClass("hidden");
            }).change();
        });
    })

</script>