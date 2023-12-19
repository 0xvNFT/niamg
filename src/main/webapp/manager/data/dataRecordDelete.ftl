<div class="container">
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.betting.record"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/bettingLog.do?type=lottery"><@spring.message "manager.lottery.record.del"/></button></div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/bettingLog.do?type=sport"><@spring.message "manager.sport.record.del"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.account.record"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/financeLog.do"><@spring.message "manager.account.record.del"/></button></div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/betnumLog.do"><@spring.message "manager.account.code.change.del"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.charge.record"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/payonlineLog.do"><@spring.message "manager.charge.record.del"/></button></div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/withdrawLog.do"><@spring.message "manager.draw.record.rec"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.activity.rec"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/redPagRecordLog.do"><@spring.message "manager.red.activity.rec.del"/></button></div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/signRecordLog.do"><@spring.message "manager.sign.rec.del"/></button></div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/activeRecordLog.do"><@spring.message "manager.round.red.del"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.live.transand.rec"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/realchangerdLog.do"><@spring.message "manager.live.transand.rec.del"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.manange.log.rec"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/operationLog.do"><@spring.message "manager.manage.log.rec.del"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.login.log.rec"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/loginLog.do"><@spring.message "manager.login.log.rec.del"/></button></div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/loginFailLog.do"><@spring.message "manager.login.fail.del"/></button></div>
		</div>
		<div class="row list-group">
		  <div class="col-md-2"><@spring.message "manager.report.rec"/>:</div>
		  <div class="col-md-2"><button class="btn btn-primary" url="${managerBase}/dataRecordDelete/reportLog.do"><@spring.message "manager.report.rec.del"/></button></div>
		</div>
	</div>
	<!-- 新增 -->
	<div class="modal fade" id="newModel" tabindex="-1" role="dialog" aria-labelledby="editLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content" style="width: 500px;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="editLabel"><@spring.message "manager.data.del"/></h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered table-striped"
						style="clear: both">
						<tbody>
							<tr>
								<td class="text-right"><@spring.message "manager.station.point"/>：</td>
								<td class="text-left">
								<select id="drdStationId" class="form-control">
								<#list stations as s>
               						 <option value="${s.id}">${s.name}[${s.code}]</option>
               					</#list>
								</select>
								</td>
							</tr>
							<tr>
								<td class="text-right"><@spring.message "admin.deposit.table.opDatetime"/>：</td>
								<td class="text-left">
									<input type="text" class="form-control fui-date" id="drdTimeLen" name="drdTimeLen" format="YYYY-MM-DD"
                        value="${startTime}" autocomplete="off">
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><@spring.message "admin.deposit.handle.close"/></button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="deleteDataRecordBtn"><@spring.message "manager.sure.name"/></button>
				</div>
			</div>
		</div>
	</div>
	
	
	<script type="text/javascript">
		$(function() {
			var delUrl="";
			$(".btn[url]").click(function(){
				delUrl=$(this).attr("url");
				$("#editLabel").html($(this).text());
				$("#newModel").modal('toggle');
			});
			$("#deleteDataRecordBtn").click(function(){
				if(delUrl){
					$.post(delUrl,{stationId:$("#drdStationId").val(),timeLen:$("#drdTimeLen").val()},function(d){
						layer.alert(d.msg||"<@spring.message "manager.delete.success"/>");
					},"json");
					delUrl="";
				}
			});
		})
	</script>