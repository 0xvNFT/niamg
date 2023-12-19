<div class="modal-dialog" id="tran_log_hand_dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.hand.update"/>(<#if log.type==2><@spring.message "admin.platform.turn.third"/><#else><@spring.message "admin.third.turn.platform"/></#if>)</h4>
		</div>
		<div class="modal-body">
			<form class="tran_log_form">
				<span class="text-danger"></span><br/>
				<button type="button" class="btn btn-primary get-data-btn"><@spring.message "admin.obtain.record.new"/></button>
				<span class="orderStatus"> </span>
				<div class="radio">
				    <label>
				      <input type="radio"  class="status-success" name="status" value="2"> <font color="green"><@spring.message "admin.exchange.success"/></font><#if log.type==1>(<@spring.message "admin.need.hand.pay"/>${log.money}<@spring.message "admin.deposit.table.money.unit"/>)</#if>
				    </label>
				 </div>
				<div class="radio">
				    <label>
				      <input type="radio" class="status-fairue" name="status" value="1"> <font color="red"><@spring.message "admin.exchange.fail"/></font><#if log.type==2>(<@spring.message "admin.need.hand.pay"/>${log.money}<@spring.message "admin.deposit.table.money.unit"/>)</#if>
				    </label>
				 </div>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary save-data-btn"><@spring.message "admin.save"/></button>
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
		</div>
	</div>
</div>

<script type="text/javascript">
requirejs(['jquery'],function(){
	var $win = $("#tran_log_hand_dialog");
	var $form = $win.find(".tran_log_form");
	$win.find(".get-data-btn").bind("click",function(){
		var $btn = $(this);
		var $msgSpan = $form.find(".orderStatus").html("<@spring.message "admin.read.order.status"/>");
		$btn.attr("disabled","disabled");
		$.ajax({
			url:'${adminBase}/transferRecord/getOrderStatus.do',
			type:'POST',
			dataType:"json",
			cache: false,
			data:{id : ${log.id}},
			success:function(data){
				if(data.success){
					var cls = data.status ? "status-success" : "status-fairue";
					$form.find("."+cls).click();
					$msgSpan.html("<@spring.message "admin.order.status.is"/>:"+(data.status ?"<font color='green'><@spring.message "admin.deposit.status.be.success"/></font>":"<font color='red'><@spring.message "admin.deposit.status.be.fail"/></font>"));
				}else{
					$msgSpan.html(data.msg);
				}
			},
			errorFn:function(){
				$msgSpan.html("<@spring.message "admin.order.status.read.fail"/>");
			},
			complete:function(){
				 $btn.removeAttr("disabled");
			}
		});
	});
	
	$win.find(".save-data-btn").bind("click",function(){
		var status = $form.find("input[name='status']:checked").val();
		if(!status){
			layer.msg('<@spring.message "admin.select.order.status.is"/>');
			return;
		}
		$.ajax({
			url:'${adminBase}/transferRecord/updStatus.do',
			type:'POST',
			dataType:"json",
			cache: false,
			data:{
				id :${log.id},
				status :status
			},
			success:function(data){
				layer.closeAll();
				layer.msg('<@spring.message "admin.order.update.success"/>');
				var $table=$(".fui-box.active").data("bootstrapTable");
				if($table && $table.length){
					$table.bootstrapTable('refresh');
				}
			}
		});
	});
});
</script>