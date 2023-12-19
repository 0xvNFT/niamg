<form action="${adminBase}/redPacket/addFissionSave.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.seed.red"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="20%"><input name="title" class="form-control" /></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.cash.money.paper"/>：</td>
                        <td width="30%"><input name="totalMoney" class="form-control money" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.number.count"/>：</td>
                        <td><input name="totalNumber" class="form-control digits" /></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.money.min"/>：</td>
                        <td width="30%"><input name="minMoney" class="form-control money" value="0.01"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
                        <td><input name="betRate" class="form-control money" value="1"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.ip.count.num"/>：</td>
                        <td><input name="ipNumber" class="form-control digits" value="1"/><@spring.message "admin.ip.grab.one.day"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td><div class="input-group">
                                <input type="text" class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" name="begin" placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td><div class="input-group">
                                <input type="text" class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" name="end" placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div></td>
                    </tr>
                    <tr >
                        <td class="text-right media-middle"><@spring.message "admin.deposit.money.max"/>：</td>
                        <td colspan="1">
                            <input name="maxMoney" class="form-control money"/>
                        </td>
                        <td colspan="2" class="media-middle"><@spring.message "admin.one.vip.red.max"/></td>
                    </tr>
                    <tr >
                        <td class="text-right media-middle"><@spring.message "admin.invite.depositer.num"/>：</td>
                        <td colspan="1">
                            <input name="inviteDepositerNum" class="form-control required"/>
                        </td>
                        <td colspan="2" class="media-middle"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/>：</td>
                        <td colspan="3">
                            <#list degrees as l>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${l.id }" checked>${l.name}</label>
                            </#list>
                        </td>

                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/>: </td>
                        <td colspan="3">
                            <#list groups as le><label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${le.id }" checked>${le.name}</label>
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
<script>
    $("input[name='todayDeposit']").click(function () {
        var value = $(this).val();
        var $monyBase  = $(".monyBase");
        if (value == 1) {
            $monyBase.hide();
        }else {
            $monyBase.show();
        }
    });

    $("input[name='rednumType']").click(function () {
        var value = $(this).val();
        var $redNumTypeCl1  = $(".redNumTypeCl1");
        var $redNumTypeCl2  = $(".redNumTypeCl2");
        if (value == 1) {
            $redNumTypeCl1.show();
            $redNumTypeCl2.hide();
        }else if (value == 2){
            $redNumTypeCl2.show();
            $redNumTypeCl1.hide();
        }else{
            $redNumTypeCl2.hide();
            $redNumTypeCl1.hide();
        }
    });

    function changeView(v){
        var objDisplay = document.getElementById(v);
        var $trv  = $("#tr_"+v);
        if(strictType==2){
            if (objDisplay.checked) {
                $trv.show();
            }else{
                $trv.hide();
            }
        }
    }
</script>
