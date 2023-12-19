<div class="modal-dialog modal-lg" style="width: 1000px">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.red.code"/>:${curRedBag.id }<@spring.message "admin.deposit.bank.bankCard.detail"/></h4>
		</div>
		<div class="modal-body" style="max-height: 650px;overflow: auto;">
			<table class="table table-bordered table-striped" data-spy="scroll">
				<tbody id="red_bag_tbody_id">
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.red.packet.title"/></td>
						<td colspan="3"><input name="title" class="form-control required" type="text" value="${curRedBag.title }" disabled/></td>
						<td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/></td>
						<td colspan="3"><input name="betRate" class="form-control required" type="number" value='${curRedBag.betRate }' disabled/></td>
						<td class="text-right media-middle"><@spring.message "admin.charge.types"/></td>
						<td colspan="3" width="21%" style="padding-top: 14px;">
                            <#if curRedBag.todayDeposit == 1>
                            		<@spring.message "admin.history.charge.val"/>
                            <#elseif curRedBag.todayDeposit == 2>
                            	<@spring.message "admin.daily.accumulative.val"/>
                            <#elseif curRedBag.todayDeposit == 3>
                            	<@spring.message "admin.red.act"/>
                            <#elseif curRedBag.todayDeposit == 4>
                            	<@spring.message "admin.single.charge"/>
                            <#elseif curRedBag.todayDeposit == 5>
                            	<@spring.message "admin.yesterday.charge"/>
                            <#elseif curRedBag.todayDeposit == 6>
                            	<@spring.message "admin.daily.first.charge"/>
							<#elseif curRedBag.todayDeposit == 8>
								<@spring.message "admin.act.single.charge"/>
                            </#if>
                            /<#if curRedBag.selectMutilDeposit == 2>
                            	<@spring.message "admin.multi.interval"/>
                            <#else>
                            	<@spring.message "admin.single.interval"/>
                            </#if>
						</td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.startTime"/></td>
						<td colspan="3">
							<input type="text" class="form-control"
                                       value="${curRedBag.beginDatetime?string('yyyy-MM-dd HH:mm:ss') }" disabled name="begin" placeholder="<@spring.message "admin.startTime"/>">
						</td>
						<td class="text-right media-middle"><@spring.message "admin.endTime"/></td>
						<td colspan="3">
							<input type="text" class="form-control"
                                       value="${curRedBag.endDatetime?string('yyyy-MM-dd HH:mm:ss') }" disabled name="end" placeholder="<@spring.message "admin.endTime"/>">
						</td>
						<td class="text-right media-middle"><@spring.message "admin.ip.limit.count"/></td>
						<td colspan="3" width="21%" style="padding-top: 14px;">
                            <input name="title" class="form-control" value="${curRedBag.ipNumber}" disabled/>
						</td>
					</tr>
					<#if curRedBag.todayDeposit == 1>
						<tr>
							<td class="text-right media-middle"><@spring.message "admin.history.charge.begin.time"/></td>
							<td colspan="3">
								<input type="text" class="form-control"
									   value="${curRedBag.hisBegin?string('yyyy-MM-dd HH:mm:ss') }" disabled name="begin" placeholder="<@spring.message "admin.startTime"/>">
							</td>
							<td class="text-right media-middle"><@spring.message "admin.history.charge.end.time"/></td>
							<td colspan="3">
								<input type="text" class="form-control"
									   value="${curRedBag.hisEnd?string('yyyy-MM-dd HH:mm:ss') }" disabled name="end" placeholder="<@spring.message "admin.endTime"/>">
							</td>
						</tr>
					</#if>
					<div class="main_red_set_info">
						<#list list as lv>
						<tr class="main_red_bag_set_info">
							<td class="text-right media-middle"><@spring.message "admin.charge.cash"/></td>
							<td colspan="3">
								<input class="redBagRechargeMins form-control money required" value='${lv.redBagRechargeMin }' disabled/>
								至
								<input class="redBagRechargeMaxs form-control money required" value='${lv.redBagRechargeMax }' disabled/>
							</td>
							
							<td class="text-right media-middle"><@spring.message "admin.red.packet.parameter"/></td>
							<td colspan="3">
								<input class="totalNumbers form-control money required" value="<@spring.message "admin.red.all.count"/>：${lv.packetNumber }" disabled/>
								<input class="totalNumbers form-control money required" value="<@spring.message "admin.red.packet.count"/>：${lv.remainNumber }" disabled/>
								<input class="ipNumber form-control money required" value="<@spring.message "admin.player.count"/>：${lv.packetNumberGot }" disabled/>
							</td>
							
							<td class="text-right media-middle"><@spring.message "admin.draw.cash"/></td>
							<td colspan="3">
								<input class="redBagMins form-control money required" value='${lv.redBagMin }' disabled/>
								至<@spring.message ""/>
								<input class="redBagMaxs form-control money required" value='${lv.redBagMax }' disabled/>
							</td>
						</tr>
						</#list>
					</div>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.act.level"/>：</td>
						<td colspan="3">${degreeNames}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<label class="text-danger">【<@spring.message "admin.zero.red.level.act.not.valid"/>】</label>
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
		</div>
	</div>
</div>