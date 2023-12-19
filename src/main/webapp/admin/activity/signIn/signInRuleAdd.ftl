<form action="${adminBase}/signInRule/doAdd.do" class="form-submit" id="station_sign_rule_add_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.sign.rule.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.continue.sign.days"/>：</td>
                        <td><input type="text" name="days" class="form-control digits" oninput="value=value.replace(/[^\d]/g,'');if(value.length > 4)value=value.slice(0,4)" autocomplete="off"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.scores"/>：</td>
                        <td><input name="score" class="form-control digits" oninput="value=value.replace(/[^\d]/g,'');if(value.length > 8)value=value.slice(0,8)" autocomplete="off" placeholder="<@spring.message "admin.zero.blank.not.score"/>"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.color.cash"/>：</td>
                        <td><input name="money" class="form-control" oninput="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : '';if(value.length>10)value=value.slice(0,10)" autocomplete="off" placeholder="<@spring.message "admin.zero.blank.not.color.cash"/>"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.color.cash.need.bet"/>：</td>
                        <td><input name="betRate" class="form-control" oninput="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : '';if(value.length>7)value=value.slice(0,7)" autocomplete="off" placeholder="<@spring.message "admin.jack.give.color.cash"/>"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.charge.value"/>：</td>
                        <td colspan="3">
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="2"><@spring.message "admin.need.daily.value"/></label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="3"><@spring.message "admin.need.charge.arrive"/></label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="1" checked><@spring.message "admin.not.limited"/></label>
                        </td>
                    </tr>
                    <tr class="deposit-need" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.min.charge.value"/>：</td>
                        <td>
                            <input placeholder="<@spring.message "admin.zero.blank.not.min.cash"/>" name="depositMoney" type="number" oninput="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : '';if(value.length>12)value=value.slice(0,12)" autocomplete="off" class="form-control digits" />
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.bet.num"/>：</td>
                        <td colspan="3">
                            <label class="radio-inline"><input type="radio" name="todayBet" value="2"><@spring.message "admin.daily.need.bet.num"/></label>
                            <label class="radio-inline"><input type="radio" name="todayBet" value="3"><@spring.message "admin.arrive.need.bet.num"/></label>
                            <label class="radio-inline"><input type="radio" name="todayBet" value="1" checked><@spring.message "admin.not.limited"/></label>
                            <label class="radio-inline"><input type="radio" name="todayBet" value="4"><@spring.message "admin.need.bet.num.is.daily"/></label>
                        </td>
                    </tr>
                    <tr class="bet-need" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.min.bet.num"/>：</td>
                        <td><input placeholder="<@spring.message "admin.zero.blank.not.limit"/>" name="betNeed" type="number" oninput="this.value= this.value.match(/\d+(\.\d{0,2})?/) ? this.value.match(/\d+(\.\d{0,2})?/)[0] : '';if(value.length>12)value=value.slice(0,12)" autocomplete="off" class="form-control digits"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>：</td>
                        <td colspan="3">
                            <#list degrees as l>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${l.id }"checked>${l.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.limit.use.group"/>：</td>
                        <td colspan="3">
                            <#list groups as l>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${l.id }"checked>${l.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.sign.days.arrive.set"/>：</td>
                        <td colspan="3">
                            <label class="radio-inline"><input type="radio" name="isReset" value="2"><@spring.message "manager.resetting"/></label>
                            <label class="radio-inline"><input type="radio" name="isReset" value="1" checked><@spring.message "admin.not.resetting"/></label>
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
<script>
requirejs(['jquery'],function(){
    var $form = $("#station_sign_rule_add_form_id");
    $form.find("[name='todayDeposit']").click(function () {
        $form.find("[name='depositMoney']").val("");
        if ($(this).val() === 1 || $(this).val()==='1') {
            $form.find(".deposit-need").hide();
        }else if($(this).val() === 3 || $(this).val()==='3'){
            $form.find(".deposit-need").show();
            $form.find("[name='depositMoney']").attr("placeholder","<@spring.message "admin.not.days.charge.sign"/>");
        }else {
            $form.find(".deposit-need").show();
            $form.find("[name='depositMoney']").attr("placeholder","<@spring.message "admin.blank.zero.not.charge.limit"/>");
        }
    });
    $form.find("input[name='todayBet']").click(function () {
        $form.find("[name='betNeed']").val("");
        if ($(this).val() === 1 || $(this).val()==='1') {
            $form.find(".bet-need").hide();
        } else if($(this).val() === 3 || $(this).val()==='3'){
            $form.find(".bet-need").show();
            $form.find("[name='betNeed']").attr("placeholder","<@spring.message "admin.not.days.limit.arrive.sign"/>");
        } else if ($(this).val() === 2 || $(this).val()==='2') {
            $form.find(".bet-need").show();
            $form.find("[name='betNeed']").attr("placeholder","<@spring.message "admin.zero.blank.not.limit"/>");
        } else if ($(this).val() === 4 || $(this).val()==='4') {
            $form.find(".bet-need").show();
            $form.find("[name='betNeed']").attr("placeholder","<@spring.message "admin.need.charge.bet.config"/>");
        }
    });
})
</script>
