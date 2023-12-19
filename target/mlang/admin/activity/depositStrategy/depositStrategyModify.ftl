<form action="${adminBase}/depositStrategy/modifySave.do" class="form-submit" id="mny_com_strategy_update_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.modify.deposit.act"/></h4>
            </div>
            <div class="modal-body"><input type="hidden" name="id" value="${com.id}">
                <div style="color:#a94442;margin-top:5px;"><@spring.message "admin.one.pay.way.cash.level"/></div>
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.styles"/>：</td>
                        <td width="30%"><select name="depositType" class="form-control">
                            <option value="7"<#if com.depositType==7>selected</#if>><@spring.message "admin.deposit.type.bank"/></option>
                            <option value="1"<#if com.depositType==1>selected</#if>><@spring.message "admin.deposit.type.manual"/></option>
                            <option value="5"<#if com.depositType==5>selected</#if>><@spring.message "admin.deposit.type.online"/></option>
                        </select></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.all.give.types"/>：</td>
                        <td width="30%"><select name="giftType" class="form-control">
                            <option value="1"<#if com.giftType==1>selected</#if>><@spring.message "admin.fixed.num"/></option>
                            <option value="2"<#if com.giftType==2>selected</#if>><@spring.message "admin.float.scale"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.give.type"/>：</td>
                        <td><select name="valueType" class="form-control">
                            <option value="1"<#if com.valueType==1>selected</#if>><@spring.message "admin.color.cash"/></option>
                            <option value="2"<#if com.valueType==2>selected</#if>><@spring.message "admin.scores"/></option>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.give.frequency"/>：</td>
                        <td><select name="depositCount" class="form-control">
                            <option value="0"<#if com.depositCount>selected</#if>><@spring.message "admin.daily.pay"/></option>
                            <option value="1"<#if com.depositCount==1>selected</#if>><@spring.message "admin.first.pay"/></option>
                            <option value="444"<#if com.depositCount==444>selected</#if>><@spring.message "admin.daily.first.charge"/></option>
                            <option value="2"<#if com.depositCount==2>selected</#if>><@spring.message "admin.second.pay"/></option>
                            <option value="3"<#if com.depositCount==3>selected</#if>><@spring.message "admin.third.pay"/></option>
                            <option value="222"<#if com.depositCount==222>selected</#if>><@spring.message "admin.two.pay.before"/></option>
                            <option value="333"<#if com.depositCount==333>selected</#if>><@spring.message "admin.three.pay.before"/></option>
                        </select></td>
                    </tr>
                    <tr class="<#if com.giftType==2>hidden </#if>giftValue">
                        <td class="text-right media-middle"><@spring.message "admin.give.val"/>：</td>
                        <td colspan="3"><input type="text" name="giftValue" class="form-control money" value="${com.giftValue}"/></td>
                    </tr>
                    <tr class="<#if com.giftType==1>hidden </#if>rollBackRate">
                        <td class="text-right media-middle"><@spring.message "admin.give.percent"/>：</td>
                        <td><div class="input-group">
					      <input name="rollBackRate" type="text" class="form-control money" value="${com.giftValue}" placeholder="<@spring.message "admin.no.input.mess"/>%">
					      <span class="input-group-addon">%</span>
					    </div></td>
                        <td class="text-right media-middle"><@spring.message "admin.give.limit"/>：</td>
                        <td><input type="text" name="upperLimit" class="form-control money" value="${com.upperLimit}" placeholder="<@spring.message "admin.zero.top"/>"/></td>
                    </tr>
                    <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.charge.cash.more.equal"/>：</td>
	                    <td><input type="text" name="minMoney" class="form-control money" value="${com.minMoney}" placeholder="<@spring.message "admin.blank.zero.not.limit"/>"/></td>
	                    <td class="text-right media-middle"><@spring.message "admin.charge.cash.less.equal"/>：</td>
	                    <td><input type="text" name="maxMoney" class="form-control money" value="${com.maxMoney}" placeholder="<@spring.message "admin.blank.zero.not.limit"/>"/></td>
                    </tr>
                    <tr class="<#if com.valueType==2>hidden </#if> dayUpperLimit">
	                    <td class="text-right media-middle"><@spring.message "admin.single.day.give.top"/>：</td>
	                    <td colspan="3"><input type="text" name="dayupperLimit" class="form-control money" value="${com.dayupperLimit}" placeholder="<@spring.message "admin.zero.no.top.max.give"/>" /></td>
	                </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.turn.over.num"/>：</td>
                        <td colspan="3"><input type="text" name="betRate" value="${com.betRate}" class="form-control money" placeholder="<@spring.message "admin.charge.give.turn.over.bet"/>"/></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.all.give.types"/>：</td>
                        <td><select name="backGiftType" class="form-control">
                                <option value="1" <#if com.backGiftType==1>selected</#if>><@spring.message "admin.fixed.num"/></option>
                                <option value="2" <#if com.backGiftType==2>selected</#if>><@spring.message "admin.float.scale"/></option>
                            </select>
                        </td>
                        <td class="text-right media-middle backRollbackValueFix2"><@spring.message "admin.give.val.back"/>：</td>
                        <td class="backRollbackValueFix2"><input type="text" name="bonusBackValue" class="form-control money" placeholder="<@spring.message "admin.define.zero"/>" value="${com.bonusBackValue}"/></td>
                        <td class="hidden text-right media-middle backRollbackValueFloat2"><@spring.message "admin.give.percent.back"/>：</td>
                        <td class="hidden backRollbackValueFloat2">
                            <div class="input-group">
                                <input name="bonusRollbackRate" type="text" class="form-control money" value="${com.bonusBackValue}" placeholder="<@spring.message "admin.no.input.mess"/>%">
                                <span class="input-group-addon">%</span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.turn.over.num.back"/>：</td>
                        <td><input type="text" name="bonusBackBetRate" class="form-control money" value="${com.bonusBackBetRate}"/></td>

                        <td class="text-right media-middle"><@spring.message "admin.back.bonus.level.distance"/>：</td>
                        <td class="backRollbackValueFix2"><input type="text" value="${com.backBonusLevelDiff}" name="backBonusLevelDiff" class="form-control money" placeholder="<@spring.message "admin.define.zero"/>" value="0"/></td>
                        <td class="hidden backRollbackValueFloat2">
                            <div class="input-group">
                                <input name="backBonusLevelDiffRate" type="text" value="${com.backBonusLevelDiff}" class="form-control money" placeholder="<@spring.message "admin.no.input.mess"/>%">
                                <span class="input-group-addon">%</span>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startDate"/>：</td>
                        <td>
                            <div class="input-group">
                            	<input type="text" class="form-control fui-date required"format="YYYY-MM-DD HH:mm:ss" name="startTime" value="${com.startDatetime}" placeholder="<@spring.message "admin.act.begin.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.endDate"/>：</td>
                        <td>
                            <div class="input-group">
                            	<input type="text" class="form-control fui-date required"format="YYYY-MM-DD HH:mm:ss" name="endTime" value="${com.endDatetime}" placeholder="<@spring.message "admin.act.end.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/>：</td>
	                    <td colspan="3">
	                        <#list degrees as d>
	                            <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${d.id}"
	                            <#if com.degreeIds?has_content && com.degreeIds?contains(","+d.id+",")>checked</#if>>${d.name}</label>
	                        </#list>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/>：</td>
	                    <td colspan="3">
	                        <#list groups as d>
	                            <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${d.id}"
	                            <#if com.groupIds?has_content && com.groupIds?contains(","+d.id+",")>checked</#if>>${d.name}</label>
	                        </#list>
	                    </td>
	                </tr><#if com.depositType!=1>
	                <tr class="deposit-config-check">
	                    <td class="text-right media-middle"><@spring.message "admin.deposit.bank.payPlatformCode"/>：</td>
	                    <td colspan="3" class="deposit-config-check-tr">
	                        <#list depositList as d>
                            <#if com.depositType==7><label class="checkbox-inline"><input type="checkbox" name="depositConfigIds" value="${d.id }"<#if com.depositConfigIds?has_content && com.depositConfigIds?contains(","+d.id+",")>checked</#if>>
	                        ${d.bankName}(${d.creatorName})</#if></label>
                            <#if com.depositType==5><label class="checkbox-inline"><input type="checkbox" name="depositConfigIds" value="${d.id }"<#if com.depositConfigIds?has_content && com.depositConfigIds?contains(","+d.id+",")>checked</#if>>
                            ${d.payName}</#if></label>
	                        </#list>
	                     </td>
	                </tr></#if>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td colspan="3"><input type="text" name="remark" class="form-control" value="${com.remark}"/></td>
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
    requirejs(['jquery'], function () {
        var $form = $("#mny_com_strategy_update_form_id")
                , $rollBackRate = $form.find(".rollBackRate")
                , $giftValue = $form.find(".giftValue")
                // ,$backGiftValue=$form.find(".backRollbackValueFix")
                // ,$backRollBackRate=$form.find(".backRollbackValueFloat")
                ,$backGiftValue2=$form.find(".backRollbackValueFix2")
                ,$backRollBackRate2=$form.find(".backRollbackValueFloat2")
                $dayUpperLimit=$form.find(".dayUpperLimit");
        $form.find("[name='giftType']").change(function(){
            var selval = $(this).val();
            $rollBackRate.addClass("hidden");
            $giftValue.addClass("hidden");
            if (selval == 1) {
                $giftValue.removeClass("hidden");
                // $backRollBackRate.addClass('hidden');
                // $backGiftValue.removeClass('hidden');
            } else if (selval == 2) {
                $rollBackRate.removeClass("hidden");
                // $backRollBackRate.removeClass('hidden');
                // $backGiftValue.addClass('hidden');
            }
        });
        $form.find("[name='backGiftType']").change(function(){
            var selval = $(this).val();
            $backRollBackRate2.addClass("hidden");
            $backGiftValue2.addClass("hidden");
            if (selval == 1) {
                $backRollBackRate2.addClass('hidden');
                $backGiftValue2.removeClass('hidden');
            } else if (selval == 2) {
                $backRollBackRate2.removeClass('hidden');
                $backGiftValue2.addClass('hidden');
            }
        });
        $form.find("[name='valueType']").change(function(){
            var selval = $(this).val();
            $dayUpperLimit.addClass("hidden");
            if (selval == 1) {
                $dayUpperLimit.removeClass("hidden");
            } else if (selval == 2) {
                $dayUpperLimit.addClass("hidden");
            }
        });
        $form.find("[name='giftType']").trigger('change');
        $form.find("[name='backGiftType']").trigger('change');
    });
</script>