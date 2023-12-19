<div class="modal-dialog modal-lg">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.level.seed.red"/></h4><em class="text-danger">(<@spring.message "admin.vip.level.up.red"/>)</em>
		</div>
		<div class="modal-body" style="max-height: 650px;overflow: auto;">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
						<td width="21%"><input name="title" class="form-control required" type="text" value="${curRedBag.title }" disabled/></td>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
						<td width="21%"><input name="betRate" class="form-control required" type="number" value='${curRedBag.betRate }' disabled/></td>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.attend.count"/>：</td>
						<td width="21%"><input name="ipNumber" class="form-control required" type="number" value="${curRedBag.ipNumber }" disabled/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
						<td>
							<input type="text" class="form-control"
                                       value="${curRedBag.beginDatetime?string('yyyy-MM-dd HH:mm:ss') }" disabled name="begin" placeholder="<@spring.message "admin.startTime"/>">
						</td>
						<td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
						<td>
							<input type="text" class="form-control"
                                       value="${curRedBag.endDatetime?string('yyyy-MM-dd HH:mm:ss') }" disabled name="end" placeholder="<@spring.message "admin.endTime"/>">
						</td>
						<td class="text-right media-middle"><@spring.message "admin.red.code"/>：</td>
						<td>
                            <input name="title" class="form-control" value="${curRedBag.id }" disabled/>
						</td>
					</tr>
					<div class="main_red_set_info">
						<#list list as lv>
						<tr class="main_red_set_info">
							<td width="12%" class="text-right media-middle">【<em style="color: red;">${lv.degreeName }</em>】<p></p><@spring.message "admin.red.count"/>：<br><@spring.message "admin.val.count"/>：</td>
							<td width="21%"><input class="form-control required" type="number" value="${lv.packetNumber }" disabled/><input class="form-control required" type="number" value="${lv.remainNumber }" disabled/></td>
							<td width="12%" class="text-right media-middle"><@spring.message "admin.deposit.money.min"/>：</td>
							<td width="21%"><input class="form-control money required" placeholder="<@spring.message "admin.last.min.val"/>" value='${lv.redBagMin }' disabled/></td>
							<td width="12%" class="text-right media-middle"><@spring.message "admin.deposit.money.max"/>：</td>
							<td width="21%"><input class="form-control money required" placeholder="<@spring.message "admin.last.max.val"/>" value='${lv.redBagMax }' disabled/></td>
						</tr>
						</#list>
					</div>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<label class="text-danger">【<@spring.message "admin.zero.red.level.act.not.valid"/>】</label>
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
		</div>
	</div>
</div>