<button class="btn btn-success pull-right" style="position:relative;top:50px;" id="member_backwater_record_list_fan_shui_id"><@spring.message "admin.sure.back.water"/></button>
<form class="fui-search table-tool" method="post" id="member_backwater_record_list_fform_id">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" name="startDate" value="${curDate}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.deposit.handle.username"/>">
            </div>
			<div class="form-group">
				<div class="input-group">
					<select class="form-control" name="betType">
						<option value=""><@spring.message "admin.game.type.selected"/></option>
						<#if game.live==2><option value="1"><@spring.message "manager.live"/></option></#if>
						<#if game.egame==2><option value="2"><@spring.message "manager.egame"/></option></#if>
						<#if game.chess==2><option value="3"><@spring.message "manager.chess"/></option></#if>
						<#if game.fish==2><option value="4"><@spring.message "manager.fish"/></option></#if>
						<#if game.esport==2><option value="5"><@spring.message "manager.esport"/></option></#if>
						<#if game.sport==2><option value="6"><@spring.message "manager.sport"/></option></#if>
						<#if game.lottery==2><option value="7"><@spring.message "manager.lottery"/></option></#if>
					</select>
				</div>
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
			<#if permAdminFn.hadPermission("admin:kickback:export")>
				<button class="btn btn-primary exportBtn" type="button"><@spring.message "admin.deposit.button.export"/></button>
			</#if>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="endDate" class="form-control fui-date" value="${curDate}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
			<div class="form-group">
				<div class="input-group">
					<input type="text" class="form-control" name="realName" placeholder="<@spring.message "admin.real.name"/>">
				</div>
				<div class="input-group">
					<select class="form-control" name="status">
						<option value=""><@spring.message "admin.select.back.water.status"/></option>
						<option value="1"><@spring.message "admin.not.back.water"/></option>
						<option value="2"><@spring.message "admin.backed.water"/></option>
						<option value="3"><@spring.message "admin.rolled.back"/></option>
					</select>
				</div>
			</div>
		</div>
	</div>
</form>
<div class="right-btn-table"><table class="fui-default-table"></table></div>
<div style="padding: 10px;">
	<span class="glyphicon glyphicon-info-sign"><@spring.message "admin.wtips"/>：</span><span class="text-danger"><@spring.message "admin.daily.record.need.back"/>!</span>
	<br/><span class="text-danger"><@spring.message "admin.at.system.set"/>-><a href="${adminBase}/kickbackStrategy/index.do" class="open-tab"><@spring.message "admin.vip.back.water.strategy"/></a><@spring.message "admin.daily.two.clock.back.total"/>!</span>
</div>
<#if permAdminFn.hadPermission("admin:kickback:export")><div class="hidden">
	<form id="member_backwater_record_export_fan_shui_id" action="${adminBase}/kickback/export.do" target="_blank" method="post">
		<input type="hidden" name="username"/>
		<input type="hidden" name="realName"/>
		<input type="hidden" name="startDate"/>
		<input type="hidden" name="endDate"/>
		<input type="hidden" name="status"/>
		<input type="hidden" name="betType"/>
	</form>
</div></#if>
<script type="text/javascript">
requirejs(['jquery'],function(){
	var $fanDianBtn=$("#member_backwater_record_list_fan_shui_id"),$form=$("#member_backwater_record_list_fform_id");
	<#if permAdminFn.hadPermission("admin:kickback:export")>$form.find(".exportBtn").click(function () {
		var $form1 = $("#member_backwater_record_export_fan_shui_id");
		$form1.find("[name='username']").val($form.find("[name='username']").val());
		$form1.find("[name='realName']").val($form.find("[name='realName']").val());
		$form1.find("[name='startDate']").val($form.find("[name='startDate']").val());
		$form1.find("[name='endDate']").val($form.find("[name='endDate']").val());
		$form1.find("[name='status']").val($form.find("[name='status']").val());
		$form1.find("[name='betType']").val($form.find("[name='betType']").val());
		$form1.submit();
	});</#if>
	$fanDianBtn.click(function(){
		var $table=$fanDianBtn.parents(".fui-box").data("bootstrapTable");
		if(!$table || !$table.length){
			return false;
		}
		var list = $table.bootstrapTable('getSelections')
			,count = list.length
			,moneys='',money='' , fsa=null,it=null;
		if(count <= 0){
			layer.msg('<@spring.message "admin.select.need.back.water.rec"/>!');
  			return false;
		}else{
			for(var i=0;i<count;i++){
				it = list[i];
				if(it.id){
		        	fsa=$table.find("input[name='fanShuiAmount"+it.id+"']");
		        	if(fsa.length){
		        		money=fsa.val();
		        		if(!money || !/^[0-9]+(\.[0-9]{1,4})?$/.test(money)){
							layer.msg("<@spring.message "admin.input.right.back.water"/>");
							fsa.focus();
							return false;
						}
			        	moneys=moneys+it.id+":"+money+",";
		        	}
	        	}
			}
			moneys=moneys.substring(0,moneys.length-1);
			layer.confirm('<@spring.message "admin.sure.selected.rec.back"/>？', function() {
				$.ajax({
					url:"${adminBase}/kickback/dealRollIds.do",
					data:{moneys:moneys
						,startDate:$form.find("[name='startDate']").val()
						,endDate:$form.find("[name='endDate']").val()},
					success:function(j){
						layer.msg(j.msg)
						$table.bootstrapTable('refresh');
					}
				});
			});
		}
	});
	Fui.addBootstrapTable({
		url : '${adminBase}/kickback/list.do',
		showPageSummary:true,
		showAllSummary:true,
		showFooter : true,
		columns : [{
			field : 'betType',
			title : Admin.gameTypes,
			align : 'center',
			width : '60',
			valign:'middle',
			formatter:gameTypeFormatter
		},{
			field : 'username',
			title : Admin.betSlipAcc,
			align : 'center',
			width : '120',
			valign:'middle',
			formatter:usernameFormatter
		},{
			field : 'proxyName',
			title : Admin.belongProxy,
			align : 'center',
			width : '120',
			valign:'middle'
		},{
			field : 'realName',
			title : Admin.vipName,
			align : 'center',
			width : '120',
			valign:'middle'
		},{
            field : 'accLevelName',
            title : Admin.memberLevel,
            align : 'center',
            width : '120',
            valign:'middle'
        },{
			field : 'betDate',
			title : Admin.payDate,
			align : 'center',
			width : '100',
			valign:'middle',
			formatter: Fui.formatDate,
			pageSummaryFormat:function(rows,aggsData){
				return "<@spring.message "admin.deposit.table.page.sum"/>:";
			},
			allSummaryFormat:function(rows,aggsData){
				return "<@spring.message "admin.deposit.table.all.sum"/>:";
			}
		},{
			field : 'betMoney',
			title : Admin.payMoney,
			align : 'center',
			width : '60',
			valign:'middle',
			formatter : numFormatter,
			pageSummaryFormat:function(rows,aggsData){
				return getTotal(rows,"betMoney");
			},
			allSummaryFormat:function(rows,aggsData){
				return (aggsData && aggsData.betMoney) ? Fui.toDecimal(aggsData.betMoney,4) : "0.0000";
			}
		},{
			field : 'winMoney',
			title : Admin.jackMoney,
			align : 'center',
			width : '60',
			valign:'middle',
			formatter : numFormatter,
			pageSummaryFormat:function(rows,aggsData){
				return getTotal(rows,"winMoney");
			},
			allSummaryFormat:function(rows,aggsData){
				return (aggsData && aggsData.winMoney) ? Fui.toDecimal(aggsData.winMoney,4) : "0.0000";
			}
		}, {
			field : 'kickbackMoney',
			title : Admin.backWaterCash,
			align : 'center',
			width : '60',
			valign:'middle',
			formatter : fsdFormatter,
			pageSummaryFormat:function(rows,aggsData){
				var total = 0;
				for(var i=0;i<rows.length;i++){
					var r = rows[i];
					if(!r["kickbackMoney"] || r.status !=2){
						continue;
					}
					total += r["kickbackMoney"];
				}
				return Fui.toDecimal(total,4);
			},
			allSummaryFormat:function(rows,aggsData){
				return (aggsData && aggsData.kickbackMoney) ? Fui.toDecimal(aggsData.kickbackMoney,4) : "0.0000";
			}
		}, {
			field : 'kickbackRate',
			title : Admin.backWaterRet,
			align : 'center',
			width : '60',
			valign:'middle',
			formatter : numFormatter
		}, {
			field : 'status',
			title : Admin.backStatus,
			align : 'center',
			width : '60',
			valign:'middle',
			formatter : statusFormatter
		}, {
			field : 'operator',
			title : Admin.operator,
			width : '80',
			valign:'middle',
			align : 'center'
		},{
			title : Admin.op,
			align : 'center',
			width : '80',
			valign:'middle',
			formatter : optFormatter
		},{
			valign:'middle',
			checkbox : true
		}]
		,onLoadSuccess:function($con){
			$con.find(".unbatch_r").each(function(){
				$con.find('input[data-index="'+$(this).attr("tindex")+'"]').attr('type','hidden');
			});
			$con.find(".doFanShui").click(function(){
				var url=$(this).attr("params")
					,id=$(this).attr("aid")
					,money=$con.find("input[name='fanShuiAmount"+id+"']").val();
				if(!money || !/^[0-9]+(\.[0-9]{1,4})?$/.test(money)){
					layer.msg("<@spring.message "admin.input.right.back.water"/>");
					return false;
				}
				$.ajax({
					url:url+"&money="+money,
					success:function(j){
						layer.msg(j.msg)
						$fanDianBtn.parents(".fui-box").data("bootstrapTable").bootstrapTable('refresh');
					}
				});
			});
		}
	});
	function usernameFormatter(value, row, index) {
		if(row.status==2){
			return '<span class="unbatch_r" tindex="'+index+'">'+value+'</span>';
		}
		return value;
	}
	function getTotal(rows,itemKey){
		var total = 0;
		for(var i=0;i<rows.length;i++){
			var r = rows[i];
			if(!r[itemKey]){
				continue;
			}
			total += r[itemKey];
		}
		return Fui.toDecimal(total,2);
	}
	function gameTypeFormatter(value, row, index) {
		if(value==1){
			return "<@spring.message "admin.live"/>";
		}else if(value==2){
			return "<@spring.message "admin.egame"/>";
		}else if(value==3){
			return "<@spring.message "BetNumTypeEnum.chess"/>";
		}else if(value == 4){
			return "<@spring.message "BetNumTypeEnum.fishing"/>";
		}else if(value == 5){
			return "<@spring.message "BetNumTypeEnum.esport"/>";
		}else if(value == 6){
			return "<@spring.message "BetNumTypeEnum.sport"/>";
		}else if(value == 7){
			return "<@spring.message "BetNumTypeEnum.lottery"/>";
		}else if(value==12){
			return "<@spring.message "manager.chess"/>";
		}else if(value == 13){
			return "<@spring.message "manager.esport"/>";
		}else if(value == 14){
			return "<@spring.message "manager.fish"/>";
		}else if(value==15){
			return "<@spring.message "admin.third.lottery"/>";
		}
		return "";
	}
	function numFormatter(value, row, index){
		if(value || value === 0){
			return Fui.toDecimal(value,4);
		}
		return "<@spring.message "admin.not.strategy"/>";
	}
	//返水金额
	function fsdFormatter(value, row, index){
		if(row.status!=4 && new Date(row.betDate).getTime()<new Date().getTime()){
			return '<input class="form-control" name="fanShuiAmount'+row.id+'" type="text" value="'+(value?Fui.toDecimal(value,4):"0")+'" >';
		}
		if(value){
			return Fui.toDecimal(value,4);
		}
		return '';
	}
	function statusFormatter(value, row, index) {
		if(value==1){
			return '<span class="label label-danger"><@spring.message "admin.not.back.water"/></span>';
		}else if(value==2){
			return '<span class="label label-success"><@spring.message "admin.backed.water"/></span>';
		}else if(value==3){
			return '<span class="label label-warning"><@spring.message "admin.rolled.back"/></span>';
		}
		return "";
	}
	function optFormatter(value, row, index) {
		var value = row.status;
		if(value==1){
			if(new Date(row.betDate).getTime()<new Date().getTime()){
				return '<a class="doFanShui label label-primary" href="javascript:void(0)" aid="'+row.id+'" params="${adminBase}/kickback/manualRollback.do?betType='+row.betType+'&username='+row.username+'&betDate='+Fui.formatDate(row.betDate)+'" title="<@spring.message "admin.sure.hand.back.water"/>？"><i class="glyphicon glyphicon-wrench"></i><@spring.message "admin.hand.back.water"/></a>';
			}else{
				return "<span class='unbatch_r' tindex='"+index+"'><@spring.message "admin.daily.record.info"/></span>";
			}
		}else if(value==3){
			return '<a class="doFanShui label label-warning" href="javascript:void(0)" aid="'+row.id+'" params="${adminBase}/kickback/manualRollback.do?betType='+row.betType+'&username='+row.username+'&betDate='+Fui.formatDate(row.betDate)+'" title="<@spring.message "admin.sure.replay.back"/>？"><i class="glyphicon glyphicon-refresh"></i><@spring.message "admin.replay.water"/></a>';
		}else if(value==2){
			return '<a class="todo-ajax label label-danger" href="${adminBase}/kickback/cancel.do?betType='+row.betType+'&username='+row.username+'&betDate='+Fui.formatDate(row.betDate)+'" title="<@spring.message "admin.sure.roll.back"/>？"><i class="glyphicon glyphicon-repeat"></i><@spring.message "admin.roll.back"/></a>';
		}
		return "";
	}
});
</script>
