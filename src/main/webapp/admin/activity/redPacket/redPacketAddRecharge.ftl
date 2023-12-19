<form action="${adminBase}/redPacket/saveRecharge.do" class="form-submit" method="POST" id="main_red_set_recharge_info_form_id">
<div class="modal-dialog modal-lg" style="width: 1000px">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.charge.num.seed.red"/></h4>
		</div>
		<div class="modal-body" style="max-height: 650px;    min-height: 380px;overflow: auto;">
			<table class="table table-bordered table-striped" data-spy="scroll">
				<tbody id="red_bag_tbody_id" style="max-height: 500px;overflow: scroll;">
					<input name="redBagRechargeMin" type="hidden"/>
					<input name="redBagRechargeMax" type="hidden"/>
					<input name="packetBumber" type="hidden"/>
					<input name="ipNumber" type="hidden"/>
					<input name="redBagMin" type="hidden"/>
					<input name="redBagMax" type="hidden"/>
					<tr>
						<td class="text-right media-middle" width="8%"><@spring.message "admin.red.packet.title"/></td>
						<td><input name="title" class="form-control required" type="text" placeholder="<@spring.message "admin.red.packet.title"/>"/></td>
						<td class="text-right media-middle" width="8%"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.zero.not.produce.bet"/></h3>"><@spring.message "admin.weight.bet.num"/></a></td>
						<td><input name="betRate" class="form-control required" type="number" placeholder="<@spring.message "admin.zero.not.produce.bet"/>"/></td>
						<td class="text-right media-middle" width="8%"><@spring.message "admin.receive.rule"/></td>
						<td>
                            <label class="radio-inline"><input type="radio" name="selectMutilDeposit" value="2"><@spring.message "admin.every.period"/></label>
                            <label class="radio-inline"><input type="radio" name="selectMutilDeposit" value="1" checked><@spring.message "admin.single.period"/></label>
						</td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.ip.limit.count"/></td>
						<td><input name="ipNumber1" class="form-control" type="text" placeholder="<@spring.message "admin.zero.blank.is.not.limit"/>"/><font color="red"><@spring.message "admin.limit.same.ip.red.count"/></font></td>
						<td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.startTime"/></h3>"><@spring.message "admin.startTime"/></a></td>
						<td class="media-middle"><input class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" value="${curDate}" name="begin" placeholder="<@spring.message "admin.startTime"/>"></td>
						<td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.endTime"/></h3>"><@spring.message "admin.endTime"/></a></td>
						<td class=" media-middle"><input class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" value="${afterYearDate }" name="end" placeholder="<@spring.message "admin.endTime"/>"></td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.charge.limit"/></td>
                        <td colspan="5">
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="2" checked><@spring.message "admin.daily.accumulative.val"/></label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="1"><@spring.message "admin.history.charge.val"/>(<font color="red"><@spring.message "admin.not.select.history.time.limit"/></font>)</label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="3"><@spring.message "admin.red.act"/></label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="5"><@spring.message "admin.yesterday.charge"/></label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="4"><@spring.message "admin.single.charge"/>(<font color="red"><@spring.message "admin.daily.last.new.pay.order"/></font>)</label>
                            <label class="radio-inline"><input type="radio" name="todayDeposit" value="6"><@spring.message "admin.daily.first.charge"/></label>
							<label class="radio-inline"><input type="radio" name="todayDeposit" value="8"><@spring.message "admin.act.single.charge"/>(<font color="red"><@spring.message "admin.act.last.new.pay.order"/></font>)</label>
                        </td>
					</tr>
					<tr style="display: none" class="deposit-his">
						<td class="text-right media-middle"><@spring.message "admin.history.charge.begin.time"/></td>
						<td><input class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="hisBeginStr" placeholder="<@spring.message "admin.startTime"/>"></td>
						<td class="text-right media-middle"><@spring.message "admin.history.charge.end.time"/></td>
						<td><input class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="hisEndStr" placeholder="<@spring.message "admin.endTime"/>"></td>
					</tr>
					<div class="main_red_set_info">
						<tr class="main_red_bag_set_info">
							<td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.user.point.cash.pay"/></h3>"><@spring.message "admin.charge.cash"/></a></td>
							<td >
								<input class="redBagRechargeMins form-control money required" type="number" placeholder="<@spring.message "admin.min.charge.value"/>"/>
								<@spring.message "admin.until"/>
								<input class="redBagRechargeMaxs form-control money required" type="number" placeholder="<@spring.message "admin.max.charge.value"/>"/>
							</td>
							<td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.red.all.count.one.pay.condition.count"/></h3>"><@spring.message "admin.red.packet.parameter"/></a></td>
							<td >
								<input class="totalNumbers form-control money required" type="number" placeholder="<@spring.message "admin.act.red.all.count"/>"/>
								--
								<input class="ipNumber form-control money required" type="number" placeholder="<@spring.message "admin.player.grab.count"/>"/>
							</td>
							
							<td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.arrive.charge.condition.cash"/></h3>"><@spring.message "admin.draw.cash"/></a></td>
							<td >
								<input class="redBagMins form-control money required" type="number" placeholder="<@spring.message "admin.min.draw.cash"/>"/>
								<@spring.message "admin.until"/>
								<input class="redBagMaxs form-control money required" type="number" placeholder="<@spring.message "admin.max.draw.cash"/>"/>
							</td>
						</tr>
					</div>
				</tbody>
				
			</table>
		</div>
		<div class="modal-footer">
			<button class="btn btn-success addOne" type="button"><@spring.message "admin.add.one.row"/></button>
			<button class="btn btn-danger delOne" type="button"><@spring.message "admin.last.one.row.del"/></button>
		</div>
		<div class="modal-footer">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.act.level"/>：</td>
						<td colspan="3"class="text-left">
							 <#list degrees as l>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${l.id }" checked>${l.name}</label>
                            </#list>
						 </td>
					</tr>
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
	var $form=$("#main_red_set_recharge_info_form_id"),$tr=$form.find('.main_red_bag_set_info'),$setInf=$form.find('#red_bag_tbody_id');
	$form.find('input[name="todayDeposit"]').click(function () {
		let type = $(this).val();
		$form.find('input[name="hisBegin"]').val("");
		$form.find('input[name="hisEnd"]').val("");
		if (type == 1) {
			$form.find('.deposit-his').show();
		}else {
			$form.find('.deposit-his').hide();
		}
	});
	$form.find(".tooltip-show").tooltip({html : true }).hover(function(){
        $(this).tooltip('show');
    },function(){
        $(this).tooltip('hide');
    });
	$form.find('.addOne').click(function(){
		var addHtml = $tr.clone()[0];
		if(addHtml){
			$setInf.append(addHtml);
		}
		return false;
	})
	$form.find('.delOne').click(function(){
		var delHtml = $setInf.find('.main_red_bag_set_info:last');
		var delLength = $setInf.find('.main_red_bag_set_info').length;
		if(delLength <= 1){
			layer.msg('<@spring.message "admin.last.act.del"/>!');
		   return false;			
		}
		if(delHtml){
			delHtml.remove();
		}
		return false;
	})
	$form.find('.checkMainRedInfo').click(function(){
		var isChecked = false;
		var redBagRechargeMinArry=[];
		var redBagRechargeMaxArry=[];
		var totalNumberArry=[];
		var ipNumberArry=[];
		var redBagMinArry=[];
		var redBagMaxArry=[];
		$form.find('.main_red_bag_set_info').each(function(i,j){
			var redBagRechargeMins = $(this).find('.redBagRechargeMins').val();//最低充值金额
			var redBagRechargeMaxs = $(this).find('.redBagRechargeMaxs').val();//最高充值金额
			var totalNumbers = $(this).find('.totalNumbers').val();//红包个数
			var ipNumbers = $(this).find('.ipNumber').val();//可抢次数
			var redBagMins = $(this).find('.redBagMins').val();//最低可抽金额
			var redBagMaxs = $(this).find('.redBagMaxs').val();//最高可抽金额
			if(!redBagRechargeMins){
				layer.msg('<@spring.message "admin.min.pay.cash.not.blank"/>!');
			   return false;
			}
			if(!redBagRechargeMaxs){
				layer.msg('<@spring.message "admin.max.pay.cash.not.blank"/>!');
			   return false;
			}
			if(redBagRechargeMaxs <= 0){
				layer.msg('<@spring.message "admin.must.charge.not.zero"/>!');
			   return false;
			}
			if(redBagMins*1 > redBagMaxs*1){
				layer.msg('<@spring.message "admin.min.pay.cash.not.max.cash"/>!');
			   return false;
			}
			if(!totalNumbers){
				layer.msg('<@spring.message "admin.red.count.not.blank.zero.not.limit"/>!');
			   return false;
			}
			if(!ipNumbers){
				layer.msg('<@spring.message "admin.red.grab.count.not.blank"/>!');
			   return false;
			}
			if(!redBagMins){
				layer.msg('<@spring.message "admin.min.draw.cash.not.blank"/>!');
			   return false;
			}
			if(!redBagMaxs){
				layer.msg('<@spring.message "admin.max.draw.cash.not.blank"/>!');
			   return false;
			}
			if(redBagMins*1 > redBagMaxs*1){
				layer.msg('<@spring.message "admin.min.draw.cash.bigger.max"/>!');
			   return false;
			}
			redBagRechargeMinArry.push(redBagRechargeMins);
			redBagRechargeMaxArry.push(redBagRechargeMaxs);
			totalNumberArry.push(totalNumbers);
			ipNumberArry.push(ipNumbers);
			redBagMinArry.push(redBagMins);
			redBagMaxArry.push(redBagMaxs);
			isChecked = true;
		})
		if(!isChecked){
			return false;
		}
		
		$form.find('[name="redBagRechargeMin"]').val(redBagRechargeMinArry);
		$form.find('[name="redBagRechargeMax"]').val(redBagRechargeMaxArry);
		$form.find('[name="packetBumber"]').val(totalNumberArry);
		$form.find('[name="ipNumber"]').val(ipNumberArry);
		$form.find('[name="redBagMin"]').val(redBagMinArry);
		$form.find('[name="redBagMax"]').val(redBagMaxArry);
	})
});

function NumberCheck(t){
    var num = t.value;
    var re=/^\d*$/;
    if(!re.test(num)){
        isNaN(parseInt(num))?t.value=0:t.value=parseInt(num);
    }
}
</script>