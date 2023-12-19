<form class="form-submit" id="money_ope_batch_add_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <input type="hidden" name="type" value="1">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.cash.batch.strategy"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.add.cash.model"/>:</td>
                        <td>
                            <select name="modelType" class="form-control">
                                <option value="1" selected><@spring.message "admin.define.add.cash.color"/></option>
                                <option value="2"><@spring.message "admin.add.cash.color"/></option>
                                <option value="3"><@spring.message "admin.customize.add.other.cash"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.username"/>：</td>
                        <td>
                            <textarea name="usernames" style="height:150px;width:100%;"></textarea>
                            <div class="modelType1">
                                <br><@spring.message "admin.data.format.member.account.divide"/>
                                <br><@spring.message "admin.just"/>：aaaaaa 300 1 <span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;bbbbbb,200,0.8 <span
                                    style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;cccccc 100 <span style="font-size:10px;color:red"><@spring.message "admin.blank.divide.color.cash"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;dddddd 0 12 <span style="font-size:10px;color:blue"><@spring.message "admin.blank.divide.give.color"/></span>
                            </div>
                            <div  class="modelType2">
                                <br><@spring.message "admin.data.format.blank"/>
                                <br>如：aaaaaa bbbbbb cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;aaaaaa,bbbbbb,cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;aaaaaa<br> &nbsp; &nbsp; &nbsp; &nbsp;bbbbbb <span style="font-size:10px;color:red"><@spring.message "admin.change.line.cow"/></span>
                            </div>
                            <div class="modelType3">
                                <br><@spring.message "admin.cow.data.format.divide"/>
                                <br>如<@spring.message ""/>：aaaaaa 300 <span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;bbbbbb,200 <span
                                    style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
                            </div>
                        </td>
                    </tr>
                    <tr class="modelType2">
                        <td class="text-right active media-middle"><@spring.message "admin.cash.money.paper"/>：</td>
                        <td><input type="text" autocomplete="off" class="form-control" name="money" /></td>
                    </tr>
                    <tr>
                        <td class="text-right active media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
                        <td><input type="text" class="form-control money" name="betNumMultiple" value="1"/></td>
                    </tr>
                    <tr class="modelType2">
                        <td class="text-right active media-middle"><@spring.message "admin.color.cash"/>：</td>
                        <td><input type="text" autocomplete="off" class="form-control" name="giftMoney"/></td>
                    </tr>
                    <tr class="betNumType1">
                        <td class="text-right active media-middle"><@spring.message "admin.give.color.cash.bet.num"/>：</td>
                        <td><input type="text" class="form-control money" name="giftBetNumMultiple" value="1"/></td>
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
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
                <button class="btn btn-primary" type="button"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
requirejs(['jquery'], function () {
    var $form = $("#money_ope_batch_add_form_id");
    $form.find(".modelType2").hide();
    $form.find(".modelType3").hide();
    $form.find(".modelType1").show();
    $form.find(".betNumType1").show();
    $form.find("select[name='modelType']").change(function () {
        var type = $(this).val();
        if (type === 1 || type ==='1' ) {
            $form.find(".modelType2").hide();
            $form.find(".modelType3").hide();
            $form.find(".modelType1").show();
            $form.find(".betNumType1").show();
        }else if (type === 2 || type ==='2'){
            $form.find(".modelType2").show();
            $form.find(".modelType1").hide();
            $form.find(".modelType3").hide();
            $form.find(".betNumType1").show();
        }else {
            $form.find(".modelType2").hide();
            $form.find(".modelType1").hide();
            $form.find(".modelType3").show();
            $form.find(".betNumType1").hide();
        }
    });
    var usernames = '';
    $("#user_manager_datagrid_id").find('tbody input:checkbox:checked').each(function (i, j) {
        j = $(j);
        if (j.val()=='on') {
            return ;
        }
        usernames += j.val() + ",";
    });
    if (usernames) {
        $form.find("select[name='modelType']").val(2).change();
        usernames = usernames.substring(0, usernames.length - 1);
        $form.find('textarea[name="usernames"]').html(usernames);
    }

    $form.find('.btn-primary').click(function () {
        let $this = $(this);
        $this.prop("disabled", true).html('<img src="${base}/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
        $.ajax({
            url: "${adminBase}/finance/moneyOpe/batchAddMoney.do",
            data: $form.serialize(),
            success: function (data) {
                if (data.msg.search("<@spring.message "admin.operate.succ"/>") != -1) {
                	layer.closeAll();
                    layer.msg(data.msg);
                }else {
                    $this.prop("disabled", false).html('<@spring.message "admin.search"/>');
                    layer.alert(data.msg);
                }

            },
            errorFn: function (json) {
                layer.msg(json.msg);
                $this.prop("disabled", false).html('<@spring.message "admin.search"/>');
            }
        });
        return false;
    })
});
</script>
