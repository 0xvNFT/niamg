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

    #queryGateway::-webkit-input-placeholder {
        color: red;
    }

    .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
        padding: 5px;
        line-height: 1.42857143;
        vertical-align: top;
        border-top: 1px solid #ddd;
    }

    @media screen and (max-width: 1920px) {
        .modal-body {
            height: 700px !important;
            overflow-y: auto;
        }
    }
</style>
<form action="${adminBase}/depositOnline/modifySave.do" class="form-submit" id="depositset_online_modify_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.online.modify.online.payment.method"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${online.id }">
                <input type="hidden" disabled="disabled" name="bankListData" value="${online.bankList}">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.online.title"/>：</td>
                        <td width="30%"><select id="payPlatformCode" name="payPlatformCode"
                                                class="form-control selectpicker"
                                                data-live-search="true" disabled="true">
                                <#list bankList as pay>
                                    <option value="${pay.code }"
                                            <#if pay.code == online.payPlatformCode>selected="selected"</#if>>${pay.payName }</option>
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
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.min"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.min.right"/>" name="min" value="${online.min }"
                                   class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.max"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.max.right"/>" name="max" value="${online.max }"
                                   class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.interfacedomain"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.correct.interfacedomain"/>" type="text" name="url" value="${online.url }"
                                   class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.payment.iconaddress"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.payment.correct.iconaddress"/>" name="icon" value="${online.icon }"
                                   class="form-control regexp" regexp="[^<]+" tip="<@spring.message "admin.deposit.online.payment.enter.iconaddress"/>"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.maximum.value.recharge"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.maximum.value.recharge.fen"/>" name="randomAdd" value="${online.randomAdd}"
                                   class="form-control digits" type="number"></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.payAlias"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.payAlias"/>" name="payAlias" class="form-control" value="${online.payAlias}"
                                   type="text"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.alternative.parameters"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.alternative.parameters"/>" type="text" name="alterNative"
                                   value="${online.alterNative }" class="form-control"></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bgRemark"/>：</td>
                        <td><input placeholder="<@spring.message "admin.remark"/>" type="text" name="bgRemark"
                                   value="${online.bgRemark }" class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.pc.alternative"/>：</td>
                        <td>
                            <input placeholder="<@spring.message "admin.deposit.online.front.desk.prompt"/>" name="pcRemark" value="${online.pcRemark}"
                                   class="form-control regexp" regexp="[^<]+" tip="<@spring.message "admin.deposit.online.please.enter.prompt"/>"/>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.wap.remarks"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.front.desk.prompt"/>" type="text" name="wapRemark" value="${online.wapRemark}"
                                   class="form-control regexp" regexp="[^<]+" tip="<@spring.message "admin.deposit.online.please.enter.prompt"/>"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.app.remarks"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.please.enter.app.prompt"/>" type="text" name="appRemark" value="${online.appRemark}"
                                   class="form-control regexp" regexp="[^<]+" tip="<@spring.message "admin.deposit.online.please.enter.prompt"/>"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.payment.gateway"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.deposit.online.correct.payment.gateway"/>" type="text" name="payGetway"
                                               value="${online.payGetway }" class="form-control"></td>
                    </tr>
                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.check.gateway"/>：</td>
                        <td colspan="3"><input id="queryGateway" placeholder="<@spring.message "admin.deposit.online.query.gateway.secondary.verification"/>" type="text"
                                               name="queryGateway"
                                               value="${online.queryGateway }" class="form-control"></td>
                    </tr>


                    <tr class="pay-gateway">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.whitelist.ip"/>：</td>
                        <td colspan="3"><input id="whiteListIp" placeholder="<@spring.message "admin.deposit.online.responsible.swiping.list"/>  "
                                               name="whiteListIp" value="${online.whiteListIp}" class="form-control"
                                               type="text"></td>
                    </tr>
                    <tr class="pay-type" id="pay-type" name="pay-type">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.payment.type"/>：</td>
                    </tr>
                    <tr id="pc" style="height: 100px">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.pc.display"/>：</td>
                        <td>
                            <div class="pcdiv">
                                <div class="pc">
                                    <input type="checkbox" id="check1" value="pcGateway" name="bankList">
                                    <label for="check1"><@spring.message "admin.deposit.online.online.banking"/></label>
                                </div>
                                <div class="pc">
                                    <input type="checkbox" id="check2" value="pcScan" name="bankList">
                                    <label for="check2"><@spring.message "admin.deposit.online.scan.code"/></label>
                                </div>
                                <div class="pc">
                                    <input type="checkbox" id="check3" value="pch5" name="bankList">
                                    <label for="check3">H5</label>
                                </div>
                            </div>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.mobile.display"/>：</td>
                        <td>
                            <div class="pcdiv">
                                <div class="pc">
                                    <input type="checkbox" id="check4" value="h5Gateway" name="bankList">
                                    <label for="check4"><@spring.message "admin.deposit.online.online.banking"/></label>
                                </div>
                                <div class="pc">
                                    <input type="checkbox" id="check5" value="h5Scan" name="bankList">
                                    <label for="check5"><@spring.message "admin.deposit.online.scan.code"/></label>
                                </div>
                                <div class="pc">
                                    <input type="checkbox" id="check6" value="h5h5" name="bankList">
                                    <label for="check6">H5</label>
                                </div>
                            </div>
                        </td>
                    </tr>
<#--                    <tr>-->
<#--                        <td class="text-right media-middle">手机设备：</td>-->
<#--                        <td><select name="systemType" class="form-control">-->
<#--                                <option value="0" <#if online.systemType==0>selected</#if> >全部</option>-->
<#--                                <option value="1" <#if online.systemType==1>selected</#if> >苹果</option>-->
<#--                                <option value="2" <#if online.systemType==2>selected</#if> >安卓</option>-->
<#--                            </select></td>-->
<#--                    </tr>-->
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.recharge.amount"/>：</td>
                        <td><select name="isFixedAmount" class="form-control">
                                <option value="1" <#if online.isFixedAmount == 1>selected</#if> >-<@spring.message "admin.deposit.online.amount.entered.default"/>-</option>
                                <option value="2" <#if online.isFixedAmount == 2>selected</#if> ><@spring.message "admin.deposit.online.single.fixed.amount"/></option>
                                <option value="3" <#if online.isFixedAmount == 3>selected</#if> ><@spring.message "admin.deposit.online.optional.fixed.amount"/></option>
                            </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.online.optional.fixed.for.amount"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.online.multiple.amounts.separated"/>" name="fixedAmount" value="${online.fixedAmount}"
                                    <#--class="form-control" regexp="([1-9][0-9]*,)*[1-9][0-9]*" tip="请输入正确的格式：10,20,30"/>-->
                                   class="form-control" regexp="(([0-9])+(\.[0-9]{2})*,)*([0-9])+(\.[0-9]{2})*"
                                   tip="<@spring.message "admin.deposit.online.enter.correct.format"/>：10,20,30 或 10.01,20.02,30.03"/>
                        </td>
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

                    <#--<tr>-->
                    <#--<td class="text-right media-middle">备注：</td>-->
                    <#--<td><input placeholder="备注" value="${online.remark2}" name="remark2"  class="form-control" type="text"></td>-->
                    <#--</tr>-->
                    <#--<tr>-->
                    <#--<td class="text-right media-middle">开始时间：</td>-->
                    <#--<td class="text-left"><div class="input-group">-->
                    <#--<input type="text" class="form-control fui-date" format=" HH:mm:ss" name="begin" placeholder="开始时间" value="${online.depositDailyStartTime }"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>-->
                    <#--</div></td>-->
                    <#--<td class="text-right media-middle">结束时间：</td>-->
                    <#--<td class="text-left"><div class="input-group">-->
                    <#--<input type="text" class="form-control fui-date" format=" HH:mm:ss" name="end" placeholder="结束时间" value="${online.depositDailyEndTime }"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>-->
                    <#--</div></td>-->
                    <#--</tr>-->
<#--                    <tr class="hidden third-pay">-->
<#--                        <td class="text-right media-middle">显示类型：</td>-->
<#--                        <td colspan="3">-->
<#--                            <select name="showType" class="form-control">-->
<#--                                <option value="all"-->
<#--                                        <#if online.showType=='all'>selected</#if>-->
<#--                                >所有终端都显示-->
<#--                                </option>-->
<#--                                <option value="pc"-->
<#--                                        <#if online.showType=='pc'>selected</#if>-->
<#--                                >pc端显示-->
<#--                                </option>-->
<#--                                <option value="mobile"-->
<#--                                        <#if online.showType=='mobile'>selected</#if>-->
<#--                                >手机端显示-->
<#--                                </option>-->
<#--                                <option value="app"-->
<#--                                        <#if online.showType=='app'>selected</#if>-->
<#--                                >app端显示-->
<#--                                </option>-->
<#--                            </select>-->
<#--                        </td>-->
<#--                    </tr>-->

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
                    <#if onOffReceiptPwd>
                        <tr>
                            <td class="text-right media-middle"><@spring.message "admin.deposit.handle.password"/>：</td>
                            <td colspan="3"><input type="password" class="form-control" name="password" required/></td>
                        </tr>
                    </#if>
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

    $(document).ready(function () {
        requirejs(['jquery'], function () {
            var $form = $("#depositset_online_modify_form_id");
            $form.find("[name='payPlatformId']").change(function () {
                var $it = $(this)
                    , payId = $it.val()
                    , iconCss = $it.find("option[value=" + payId + "]").attr("iconCss");
                $form.find(".third-pay").removeClass("hidden");
            }).change();
        });
        setTimeout(function () {
            getPaymentTypeAjax();
        }, 100);
    })

    function getPaymentTypeAjax() {
        var opt = $("#payPlatformCode option:selected").val();//默认获取选中的项
        $.ajax({
            url: baseInfo.adminBaseUrl + "/depositOnline/getPaymentTypeByName.do",
            type: "get",
            data: {name: opt},
            cache: true,
            dataType: "json",
            success: function (json) {
                var jsondata = (json.data).split("&");
                if (jsondata != "") {
                    var datahtml = '<td class="text-right media-middle"><@spring.message "admin.deposit.online.payment.type"/>：</td>';
                    datahtml += '<td colspan="3"><div class="bankDiv" style="height: 110px">'
                    for (var i = 0; i < jsondata.length; i++) {
                        if (jsondata[i].split(",")[0]) {
                            var value = jsondata[i].split(",")[0];
                            var name = jsondata[i].split(",")[1];
                            datahtml += '<div class="bankListDiv" style="width: 230px"><input type="checkbox" id=' + value + '   name="bankList"   value=' + value + ' ><label for=' + value + ' style="margin-right:6%">' + name + '</label> </div>'
                        }
                    }
                    datahtml += '</div></td>';
                    document.getElementById("pay-type").innerHTML = datahtml;

                    var bank = $("input[name='bankListData']").val();//得到复选框数据 并根据值 勾选
                    for (var i = 0; i < (bank.replaceAll("\\|", ",")).split(",").length; i++) {
                        cheBankData = bank.replaceAll("\\|", ",").split(",")[i]
                        $("input:checkbox[value='" + cheBankData + "']").attr('checked', 'true');
                    }
                } else {
                    document.getElementById("pay-type").innerHTML = ""
                    $('#pc').hide();
                }
            },
            error: function (e) {
            }
        });
    }
</script>
