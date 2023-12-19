<form action="${adminBase}/finance/deposit/confirmHandle.do"  id="pay_online_rd_hand_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.handle.title"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value='${com.id }'>
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right"><@spring.message "admin.deposit.handle.username"/>：</td>
                        <td>${com.username }</td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.deposit.handle.depositor" />：</td>
                        <td>${com.depositor }</td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.deposit.handle.payName"/>：</td>
                        <td>${com.payName }</td>
                    </tr>
                    <#if com.depositType == 3>
<#--                           <tr>-->
<#--                               <td class="text-right media-middle">银行转账方式：</td>-->
<#--                               <td><#if com.bankWay == 1>网银转账-->
<#--                               <#elseif com.bankWay == 2>ATM入款-->
<#--                               <#elseif com.bankWay == 3>银行柜台-->
<#--                               <#elseif com.bankWay == 4>手机转账-->
<#--                               <#elseif com.bankWay == 5>支付宝-->
<#--                               </#if>-->
<#--                               </td>-->
<#--                           </tr>-->
                         <tr>
                             <td class="text-right media-middle"><@spring.message "admin.deposit.handle.bankAddress"/>：</td>
                             <td>
                                 ${com.bankAddress}
                             </td>
                         </tr>
                    </#if>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.money"/>：</td>
                        <td>
                            <input name="money" class="form-control" autocomplete="off" value="${com.money }"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.createDatetime"/>：</td>
                        <td>
                        ${com.createDatetime }
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.deposit.handle.result"/>：</td>
                        <td>
                            <select name="status" class="form-control">
                            <#if status ==2>
                                <option value="2" selected><@spring.message "admin.deposit.handle.status.success"/></option>
                            <#else>
                            <option value="3"><@spring.message "admin.deposit.handle.status.fail"/></option>
                            </#if>
                            </select>
                        </td>
                    </tr>
                    <#if onOffReceiptPwd>
                           <tr>
                               <td class="text-right"><@spring.message "admin.deposit.handle.password"/>：</td>
                               <td>
                                   <input name="password" class="form-control" type="password" required/>
                               </td>
                           </tr>
                    </#if>
                    <tr class="opDesc_tr">
                        <td class="text-right"><@spring.message "admin.deposit.handle.remark"/>：</td>
                        <td>
                            <textarea name="remark" class="form-control"></textarea>
                        </td>
                    </tr>
                    <tr class="opDesc_tr">
                        <td class="text-right"><@spring.message "admin.deposit.handle.bgRemark"/>：</td>
                        <td>
                            <textarea name="bgRemark" class="form-control"></textarea>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close" id="esc-close"><@spring.message "admin.deposit.handle.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
<script>
    requirejs(['jquery'], function () {
        let $form = $("#pay_online_rd_hand_form_id");
        let initMoney = '${com.money}';
        $(window).keyup(function (event) {
            if(event.keyCode ==32){
                if (event.target.type=='textarea' || event.target.type =='text' || event.target.type =='password') {
                    return false;
                }
                $form.find(".btn-primary").trigger("click");
            }
            //按下Esc健要执行的方法
            if(event.keyCode == 27){
                $("#esc-close").click();
            }
        });
        $form.submit(function () {
            let money = $form.find("input[name='money']").val();
            if (parseFloat(initMoney) != money) {
                layer.confirm('<@spring.message "admin.deposit.handle.money.confirm"/>',['<@spring.message "admin.deposit.handle.money.confirm.ok"/>'],function (index) {
                    confirmSub();
                })
            }else {
                confirmSub();
            }
            return false;
        })

        function confirmSub() {
            $.ajax({
                url: "${adminBase}/finance/deposit/confirmHandle.do",
                data: $form.serialize(),
                type: 'POST',
                success: function (result) {
                    if (result.success) {
                        layer.closeAll();
                        layer.msg('<@spring.message "admin.deposit.handle.save.ok"/>');
                        var $table = $(".fui-box.active").data("bootstrapTable");
                        if ($table && $table.length) {
                            $table.bootstrapTable('refresh');
                        }
                    }
                }
            });
        }
    });
</script>
