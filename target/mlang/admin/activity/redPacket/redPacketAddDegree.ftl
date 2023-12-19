<form action="${adminBase}/redPacket/saveDegree.do" class="form-submit" method="POST" id="main_red_set_info_form_id">
<div class="modal-dialog modal-lg" style="width: 1000px">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.vip.level.seed.red"/></h4><em class="text-danger">(<@spring.message "admin.vip.level.up.red"/>)</em>
		</div>
		<div class="modal-body" style="max-height: 650px;min-height: 380px;overflow: auto;">
			<table class="table table-bordered table-striped">
				<tbody>
					<input name="redBagDid" type="hidden"/>
					<input name="redBagTmn" type="hidden"/>
					<input name="redBagMin" type="hidden"/>
					<input name="redBagMax" type="hidden"/>
					<tr>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
						<td width="21%"><input name="title" class="form-control required" type="text" placeholder="<@spring.message "admin.red.packet.title"/>"/></td>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
						<td width="21%"><input name="betRate" class="form-control required" type="number" placeholder="<@spring.message "admin.zero.not.produce.bet"/>"/></td>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.attend.count"/>：</td>
						<td width="21%"><input name="ipNumber" class="form-control required" type="number" placeholder="<@spring.message "admin.zero.not.limit.act"/>"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
						<td>
							<input class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" name="begin" placeholder="<@spring.message "admin.startTime"/>">
						</td>
						<td width="12%" class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
						<td>
							<input class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" name="end" placeholder="<@spring.message "admin.endTime"/>">
						</td>
						<td class="text-right media-middle"><@spring.message "admin.daily.pay.money"/>：</td>
						<td>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="2" checked><@spring.message "admin.need.want"/></label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="1"><@spring.message "admin.not.limited"/></label>
						</td>
					</tr>
					<div class="main_red_set_info">
						<#list degrees as lv>
						<tr class="main_red_set_info_${lv_index }">
							<td width="12%" class="text-right media-middle lvId_${lv_index }" data-id="${lv.id }" data-name="${lv.name }">【<em style="color: red;">${lv.name }</em>】<p></p><@spring.message "admin.red.count"/>：</td>
							<td width="21%"><input class="form-control required totalNumbers${lv_index }" type="number" placeholder="<@spring.message "admin.zero.not.start.act"/>"/></td>
							<td width="12%" class="text-right media-middle"><@spring.message "admin.deposit.money.min"/>：</td>
							<td width="21%"><input class="form-control money required minMoneys${lv_index }" placeholder="<@spring.message "admin.last.min.val"/>" value="1"/></td>
							<td width="12%" class="text-right media-middle"><@spring.message "admin.deposit.money.max"/>：</td>
							<td width="21%"><input class="form-control money required maxMoneys${lv_index }" placeholder="<@spring.message "admin.last.max.val"/>"/></td>
						</tr>
						</#list>
					</div>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<label class="text-danger">【<@spring.message "admin.zero.red.level.act.not.valid"/>】</label>
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
			<button class="btn btn-primary checkMainRedInfo"><@spring.message "admin.save"/></button>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
requirejs(['jquery'],function(){
	var $form=$("#main_red_set_info_form_id");
	$form.find('.checkMainRedInfo').click(function(){
		var lvNum = ${degreesNum};
		var dids=[];
		var tmns=[];
		var mins=[];
		var maxs=[];
		for(var i = 0;i < lvNum;i++){
			var did = $form.find('.lvId_'+i).data('id');
			var tmn = $form.find('.totalNumbers'+i).val();
			var min = $form.find('.minMoneys'+i).val();
			var max = $form.find('.maxMoneys'+i).val();
			if(!did){
				layer.msg('<@spring.message "admin.parameter.error.refresh.page"/>!');
			    return false;
			}
			if(!tmn){
				layer.msg('<@spring.message "admin.level.red.not.blank.zero"/>!');
			    return false;
			}
			if(!min){
				layer.msg('<@spring.message "admin.min.cash.not.blank"/>!');
			    return false;
			}
			if(!max){
				layer.msg('<@spring.message "admin.max.cash.not.blank"/>!');
			    return false;
			}
			if(min*1 > max*1){
				layer.msg($('#lvId_'+i).data('name')+'<@spring.message "admin.level.min.cash.max.cash.big"/>!');
			    return false;
			}
			dids.push(did);
			tmns.push(tmn);
			mins.push(min);
			maxs.push(max);
		}
		$form.find('[name="redBagDid"]').val(dids);
		$form.find('[name="redBagTmn"]').val(tmns);
		$form.find('[name="redBagMin"]').val(mins);
		$form.find('[name="redBagMax"]').val(maxs);
	});
});
</script>