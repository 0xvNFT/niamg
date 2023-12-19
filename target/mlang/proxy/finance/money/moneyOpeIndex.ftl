<div style="padding: 10px;">
    <div style="color:red;font-size: 20px"><@spring.message "admin.select.deposit.page.score.bet"/>!!!</div>
</div>
<form id="money_ope_form_id" method="post">
    <div class="form-inline">
        <div class="input-group">
            <input type="text" class="form-control" name="searchText" placeholder="<@spring.message "admin.input.username.query.find"/>" value="${username }">
        </div>
        <button class="btn btn-primary select-btn" type="button"><@spring.message "admin.search"/></button>
        <button class="btn btn-primary reset-btn" type="button"><@spring.message "manager.resetting"/></button>
        <button class="btn btn-primary open-dialog" type="button" url="${proxyBase}/finance/moneyOpe/showBatchAdd.do"><@spring.message "admin.bat.add.money"/></button>
        <button class="btn btn-primary open-dialog" type="button" url="${proxyBase}/finance/moneyOpe/showBatchSub.do"><@spring.message "admin.bat.decode.money"/></button>
    </div>
    <div class="hidden money-div" style="margin-top:10px;">
        <input type="hidden" name="userId" value=""/>
        <table class="table table-bordered">
            <tbody>
            <tr>
                <td width="30%" class="text-right active"><@spring.message "admin.money.history.username"/>：</td>
                <td class="text-primary username-td"></td>
            </tr>
            <tr>
                <td class="text-right active"><@spring.message "admin.deposit.table.realName"/>：</td>
                <td class="text-primary real-name-td"></td>
            </tr>
            <tr>
                <td class="text-right active"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                <td class="text-primary level-name-td"></td>
            </tr>
            <tr>
                <td class="text-right active"><@spring.message "admin.vip.remark"/>：</td>
                <td class="text-primary remark-td"></td>
            </tr>
            <tr>
                <td class="text-right active"><@spring.message "admin.superior.proxy"/>：</td>
                <td class="text-primary proxy-name-td"></td>
            </tr>
            <tr>
                <td class="text-right active"><@spring.message "admin.account.money.val"/>：</td>
                <td class="text-danger money-td"></td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "manager.oper.type"/>：</td>
                <td>
                    <label class="radio-inline"><input type="radio" name="type" value="1" checked><@spring.message "admin.worker.add.cash"/></label>
                    <label class="radio-inline"><input type="radio" name="type" value="999" checked><@spring.message "admin.deposit.strategy.cash"/></label>
                    <label class="radio-inline"><input type="radio" name="type" value="2"><@spring.message "admin.worker.deduce.cash"/></label>
                    <label class="radio-inline"><input type="radio" name="type" value="35"><@spring.message "admin.color.deduce.cash"/></label>
                    <label class="radio-inline"><input type="radio" name="type" value="90"><@spring.message "admin.other.thing"/></label>
                </td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "admin.cash.money.paper"/>：</td>
                <td><input type="number" step="any" max="9999999999" class="form-control" name="money" autocomplete="off"/></td>
            </tr>
            <tr class="money-dml hidden">
                <td class="text-right active media-middle"><@spring.message "admin.cash.betnum.double"/>：</td>
                <td><input type="number" step="any" max="1000" class="form-control" name="betNumMultiple"  value="1"/>
                    <span><@spring.message "admin.remark.zero.cash.not.bet"/></span>
                </td>
            </tr>
            <tr class="money-dml hidden not-other">
                <td class="text-right active media-middle"><@spring.message "admin.color.cash"/>：</td>
                <td><input type="number" step="any" max="9999999" class="form-control" autocomplete="off" name="giftMoney"/></td>
            </tr>
            <tr class="money-dml hidden not-other">
                <td class="text-right active media-middle"><@spring.message "admin.color.cash.bet.num"/>：</td>
                <td><input type="number" step="any" max="1000" class="form-control" name="giftBetNumMultiple" value="1"/></td>
            </tr>
            <tr class="money-dml hidden not-other">
                <td class="text-right active media-middle"><@spring.message "admin.scores"/>：</td>
                <td><input type="number" max="999999999" class="form-control" autocomplete="off" name="score"/></td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "admin.operate.reason"/>：</td>
                <td><textarea class="form-control" name="remark"></textarea></td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "admin.deposit.bgRemark"/>：</td>
                <td><textarea class="form-control" name="bgRemark"></textarea></td>
            </tr>
             <#if isReceiptPwd>
             <tr>
                 <td class="text-right media-middle"><@spring.message "admin.deposit.handle.password"/>：</td>
                 <td><input type="password" class="form-control" name="password" required/></td>
             </tr>
             </#if>
            <tr>
                <td colspan="2" class="text-center">
                    <button class="btn btn-primary"><@spring.message "admin.confirmed"/></button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form>
<script type="text/javascript">
requirejs(['jquery'], function () {
    var $form = $("#money_ope_form_id"), $searchText = $form.find("[name='searchText']");
	<#if username>
	search();
	</#if>
    function search() {
        var acc = $searchText.val();
        if (acc == '') {
            layer.msg("<@spring.message "admin.member.acc.not.blank"/>");
            return false;
        }
        $.ajax({
            url: "${proxyBase}/finance/moneyOpe/getMoney.do?username=" + acc,
            type: "GET",
            success: function (data) {
                if (data.success === false) {
                    layer.msg(data.msg);
                    return;
                }
                reset();
                $searchText.val(acc);
                $form.find("[name='userId']").val(data.userId);
                if(data.userType==150){
                	$form.find(".username-td").html(data.username+"<span class='text-danger'>(<@spring.message "admin.test.vip"/>)</span>");
                }else{
                	$form.find(".username-td").html(data.username+"(<@spring.message "admin.true.vip"/>)");
                }
                
                $form.find(".remark-td").html(data.remark ? data.remark : '');
                $form.find(".real-name-td").html(data.realName ? data.realName : '');
                $form.find(".level-name-td").html(data.degreeName ? data.degreeName : '');
                $form.find(".proxy-name-td").html(data.parentNames ? data.parentNames : '');
                $form.find(".money-td").html(data.money);
                $form.find(".money-div").removeClass("hidden");
            },
            dataType: "JSON",
            errorFn: function () {
                reset();
                $searchText.val(acc);
            }
        });
    }
    $form.find(".select-btn").click(search);
    $form.find(".reset-btn").click(reset)
    $searchText.keydown(function (e) {
        if (e.keyCode == 13) {
            search();
            return false;
        }
    });

    function reset() {
        $searchText.val("");
        $form.find("[name='userId']").val("");
        $form.find(".account-td").html("");
        $form.find(".money-td").html("");
        $form.find(".proxy-name-td").html("");
        $form.find(".real-name-td").html("");
        $form.find(".level-name-td").html("");
        $form.find(".remark-td").html("");
        $form.find("[name='money']").val("");
        $form.find("[name='type'][value='1']").prop("checked","checked").change();
        $form.find("[name='useStrategy'][value='true']").prop("checked","checked").change();
        $form.find("[name='remark']").val("");
        $form.find("[name='bgRemark']").val("");
        $form.find(".memmny-div").addClass("hidden") ;
        $form.find("[name='giftMoney']").val("");
        $form.find("[name='giftBetNumMultiple']").val("1");
        $form.find("[name='betNumMultiple']").val("1");
        $form.find("[name='score']").val("");
        $form.find("[name='password']").val("");
        $form.find("[name='checkBetNum'][value='1']").click();
    }

    $form.find("input[type=radio][name=type]").change(function () {
        var val = $(this).val();
        if (val == 1) {
            $form.find(".money-dml").removeClass("hidden");
            $form.find("[name='checkBetNum'][value='1']").prop("checked");
        } else if (val == 2 || val == 35){
            $form.find(".money-dml").addClass("hidden");
            $form.find("[name='checkBetNum'][value='1']").click();
        }else if(val == 90){
            $form.find(".money-dml").removeClass("hidden");
            $form.find(".not-other").addClass("hidden");
            $form.find("[name='giftMoney']").val("");
            $form.find("[name='giftBetNumMultiple']").val("");
            $form.find("[name='score']").val("");
        }else {
            $form.find(".money-dml").addClass("hidden");
            $form.find("[name='money']").removeClass("hidden");
        }
    });

    var saveExe = false;
    $form.submit(function () {
        if (saveExe) {
            layer.msg("<@spring.message "admin.submit.add.cash.wait"/>");
            return false;
        }
        saveExe = true;
        $.ajax({
            url: "${proxyBase}/finance/moneyOpe/save.do",
            data: $form.serialize(),
            success: function (data) {
                layer.msg(data.msg || "<@spring.message "admin.operate.succ"/>！");
                search();
                saveExe = false;
            },
            error: function (data) {
                layer.msg(data.msg || "<@spring.message "admin.operate.fail"/>！");
                saveExe = false;
            },
            errorFn: function (data) {
                layer.msg(data.msg || "<@spring.message "admin.operate.fail"/>！");
                saveExe = false;
            }
        });
        return false;
    })
});
</script>
