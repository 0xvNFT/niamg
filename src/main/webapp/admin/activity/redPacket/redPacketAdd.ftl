<form action="${adminBase}/redPacket/addSave.do" class="form-submit">
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
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.charge.value"/>：</td>
                        <td colspan="3">
                            <label class="radio-inline">
                                <input type="radio" name="todayDeposit" value="2"><@spring.message "admin.need.daily.value"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="todayDeposit" value="1" checked><@spring.message "admin.not.limit.one"/>
                            </label>
                        </td>
                    </tr>
					<tr hidden="hidden" class="monyBase">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.type.manual"/>：</td>
                        <td colspan="1">
                            <label class="radio-inline">
                                <input type="radio" name="manualDeposit" value="2" checked><@spring.message "admin.effective"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="manualDeposit" value="1"><@spring.message "admin.not.effective"/>
                            </label>
                        </td>
                        <td colspan="2" class="media-middle"><@spring.message "admin.hand.val.is.not.daily.cash"/></td>
                    </tr>
                    <tr hidden="hidden" class="monyBase">
                        <td class="text-right media-middle"><@spring.message "admin.red.packet.item.config"/>：</td>
                        <td colspan="2">
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="0" checked><@spring.message "admin.not.use.item"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="1"><@spring.message "admin.project.one"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="2"><@spring.message "admin.project.two"/>
                            </label>
                        </td>
                        <td colspan="1" class="media-middle"><@spring.message "admin.single.vip.red.item.select"/></td>
                    </tr>
                    <tr hidden="hidden" class="redNumTypeCl1">
                  	<td class="text-right media-middle"><@spring.message "admin.red.receive.item"/>：</td>
                        <td colspan="1"><input name="moneyCustom" type="text" class="form-control" placeholder="<@spring.message "admin.cash.val.divide.after"/>" value="0" style="height: 60px;"/></td>
                        <td colspan="2" class="media-middle">
                        	【<@spring.message "admin.config.one.item.valid"/>】<font color="red"><@spring.message "admin.need.charge.cash.red"/></font>,<@spring.message "admin.zero.display.one"/>;
                        	<font color="red"><@spring.message "admin.cash.val.divide.after"/></font>
                        	<span class="glyphicon glyphicon-circle-arrow-right" aria-hidden="true"></span> 
                        	<font color="red"><@spring.message "admin.first.red.charge.one.by.one"/></font>
                        </td>
                    </tr>
                    <tr hidden="hidden" class="redNumTypeCl2">
                        <td class="text-right media-middle"><@spring.message "admin.one.red.cash.need"/>：</td>
                        <td colspan="1">
                            <input name="moneyBase" class="form-control money" placeholder="<@spring.message "admin.one.red.need.cash.zero"/>" value="0"/>
                        </td>
                        <td colspan="2" class="media-middle">【<@spring.message "admin.config.two.valid"/>】<font color="red"><@spring.message "admin.one.red.need.cash.zero"/></font></td>
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
