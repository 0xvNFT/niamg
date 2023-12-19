 <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.view.red.packet"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="20%"><input name="title" class="form-control"value="${curRedBag.title }" disabled /></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.cash.money.paper"/>：</td>
                        <td width="30%"><input name="totalMoney" class="form-control money" value="${curRedBag.totalMoney }" disabled /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.number.count"/>：</td>
                        <td><input name="totalNumber" class="form-control digits" value="${curRedBag.totalNumber }" disabled /></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.money.min"/>：</td>
                        <td><input name="minMoney" class="form-control money" value="${curRedBag.minMoney }" disabled /></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "admin.red.packet.val"/>：</td>
						<td><input name="title" class="form-control money" value="${curRedBag.totalMoney-grabMoney }"  disabled/></td>
						<td class="text-right media-middle"><@spring.message "admin.red.packet.count"/>：</td>
						<td><input name="totalMoney" class="form-control" value="${curRedBag.totalNumber- grabNum}" disabled/></td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
                        <td><input name="betRate" class="form-control money" value="${curRedBag.betRate }" disabled /></td>
                        <td class="text-right media-middle"><@spring.message "admin.ip.count.num"/>：</td>
                        <td><input name="ipNumber" class="form-control digits" value="${curRedBag.ipNumber }" disabled /><@spring.message "admin.ip.grab.one.day"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control"
                                       value="${curRedBag.beginDatetime?string('yyyy-MM-dd HH:mm:ss') }" disabled name="begin" placeholder="<@spring.message "admin.startTime"/>"> <span
                                    class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control"
                                       value="${curRedBag.endDatetime?string('yyyy-MM-dd HH:mm:ss') }" disabled name="end" placeholder="<@spring.message "admin.endTime"/>"> <span
                                    class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr >
                        <td class="text-right media-middle"><@spring.message "admin.deposit.money.max"/>：</td>
                        <td>
                            <input name="maxMoney" class="form-control money" value="${curRedBag.maxMoney }" disabled />
                        </td>
                        <td colspan="2" class="media-middle"><@spring.message "admin.one.vip.red.max"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.charge.value"/>：</td>
                        <td colspan="3">
                        	<#if curRedBag.todayDeposit == 2>
                            <label class="radio-inline">
                                <input type="radio" name="todayDeposit" value="2"checked disabled><@spring.message "admin.need.daily.value"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="todayDeposit" value="1" disabled ><@spring.message "admin.not.limit.one"/>
                            </label>
                            <#else>
                             <label class="radio-inline">
                                <input type="radio" name="todayDeposit" value="2" disabled><@spring.message "admin.need.daily.value"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="todayDeposit" value="1" checked disabled><@spring.message "admin.not.limit.one"/>
                            </label>
                            </#if>
                        </td>
                    </tr>

                    <#if curRedBag.inviteDepositerNum>
                        <td class="text-right media-middle"><@spring.message "admin.invite.depositer.num"/>：</td>
                        <td>
                            <input name="inviteDepositerNum" class="form-control" value="${curRedBag.inviteDepositerNum }" disabled />
                        </td>
                        <td colspan="2" class="media-middle"><@spring.message "admin.invite.depositer.num.info"/></td>
                    <#else>

                    </#if>

                    <#if curRedBag.todayDeposit == 2>
					<tr  class="monyBase">
					 <#else>
					 <tr  hidden="hidden" class="monyBase">
					 </#if>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.type.manual"/>：</td>
                        <td colspan="1">
                        <#if curRedBag.manualDeposit == 2>
                            <label class="radio-inline">
                                <input type="radio" name="manualDeposit" value="2" checked disabled><@spring.message "admin.effective"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="manualDeposit" value="1" disabled><@spring.message "admin.not.effective"/>
                            </label>
                             <#else>
                             <label class="radio-inline">
                                <input type="radio" name="manualDeposit" value="2" disabled><@spring.message "admin.effective"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="manualDeposit" value="1" checked disabled><@spring.message "admin.not.effective"/>
                            </label>
                             </#if>
                        </td>
                        <td colspan="2" class="media-middle"><@spring.message "admin.hand.val.is.not.daily.cash"/></td>
                    </tr>
                    <#if curRedBag.todayDeposit == 2>
					<tr  class="monyBase">
					 <#else>
					 <tr  hidden="hidden" class="monyBase">
					 </#if>
                        <td class="text-right media-middle"><@spring.message "admin.red.packet.item.config"/>：</td>
                        <td colspan="2">
                        <#if curRedBag.rednumType == 0>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="0" checked disabled><@spring.message "admin.not.use.item"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="1" disabled><@spring.message "admin.project.one"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="2" disabled><@spring.message "admin.project.two"/>
                            </label>
                           <#elseif curRedBag.rednumType == 1>
                           <label class="radio-inline">
                                <input type="radio" name="rednumType" value="0"  disabled><@spring.message "admin.not.use.item"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="1" checked disabled><@spring.message "admin.project.one"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="2" disabled><@spring.message "admin.project.two"/>
                            </label>
                            <#else>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="0"  disabled><@spring.message "admin.not.use.item"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="1"  disabled><@spring.message "admin.project.one"/>
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="rednumType" value="2" checked disabled><@spring.message "admin.project.two"/>
                            </label>
					 </#if>
                        </td>
                        <td colspan="1" class="media-middle"><@spring.message "admin.single.vip.red.item.select"/></td>
                    </tr>
                    
                    <#if curRedBag.rednumType == 1>
                    <tr  class="redNumTypeCl1">
                    <#else>
					 <tr  hidden="hidden" class="redNumTypeCl1">
					 </#if>
                  	<td class="text-right media-middle"><@spring.message "admin.red.receive.item"/>：</td>
                        <td colspan="1"><textarea name="moneyCustom"   class="form-control"   style="height: 60px;" disabled>${curRedBag.moneyCustom }</textarea></td>
                        <td colspan="2" class="media-middle">
                        	【<@spring.message "admin.config.one.item.valid"/>】<font color="red"><@spring.message "admin.need.charge.cash.red"/></font>,<@spring.message "admin.zero.display.one"/>;
                        	<font color="red"><@spring.message "admin.cash.val.divide.after"/></font>
                        	<span class="glyphicon glyphicon-circle-arrow-right" aria-hidden="true"></span> 
                        	<font color="red"><@spring.message "admin.first.red.charge.one.by.one"/></font>
                        </td>
                    </tr>
                    <#if curRedBag.rednumType == 2>
                    <tr  class="redNumTypeCl1">
                    <#else>
					 <tr hidden="hidden" class="redNumTypeCl2">
					 </#if>
                        <td class="text-right media-middle"><@spring.message "admin.one.red.cash.need"/>：</td>
                        <td colspan="1">
                            <input name="moneyBase" class="form-control money" placeholder="<@spring.message "admin.one.red.need.cash.zero"/>" value="${curRedBag.moneyBase }" disabled/>
                        </td>
                        <td colspan="2" class="media-middle">【<@spring.message "admin.config.two.valid"/>】<font color="red"><@spring.message "admin.one.red.need.cash.zero"/></font></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/>：</td>
                        <td colspan="3">${degreeNames}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/>：</td>
                        <td colspan="3">${groupNames}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
            </div>
        </div>
    </div>
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
