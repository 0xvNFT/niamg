<form class="table-tool" method="post" id="report_member_data_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startDate" value="${startTime}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" id="username" value="${username}" placeholder="<@spring.message "admin.vip.account"/>">
            </div>
            <button class="btn btn-primary search-btn fui-date-search" type="button"><@spring.message "manager.check.select"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endDate" class="form-control fui-date" value="${endTime}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
        </div>
    </div>
</form>
<div id="remark_div" style="margin-bottom: 10px;">
    <div style="color:blue;"><@spring.message "admin.total.result.time"/>。
    </div>
</div>
<div id="user_data_container_id" style="display:none">
	<div class="col-lg-12" style="padding-left:0">
	    <div class="panel panel-default"  style="border-radius:0">
	        <table class="table table-bordered report-table">
	        <tr>
	        	<th><@spring.message "manager.account.number"/></th>
	        	<th><@spring.message "admin.deposit.table.depositType"/></th>
	        	<th><@spring.message "admin.withdraw.table.proxyName"/></th>
	        	<th><@spring.message "admin.user.info.createDate"/></th>
	        	<th><@spring.message "admin.money.values"/></th>
	        	<th><@spring.message "admin.current.pay.weight"/></th>
	        	<th><@spring.message "admin.found.need.weight"/></th>
	        	<th><@spring.message "admin.all.found.weight"/></th>
	        </tr>
	        <tr id="report_member_data_tb_base"></tr>
	        </table>
	    </div>
	</div>
	<div class="col-lg-12" style="padding-left:0">
	    <div class="panel panel-default"  style="border-radius:0">
	        <table class="table table-bordered report-table">
	        <tr>
	        	<th><@spring.message "admin.online.draw.total"/></th>
	        	<th><@spring.message "admin.hand.cash.total"/></th>
	        	<th><@spring.message "manager.hand.input.money"/></th>
	        	<th><@spring.message "admin.last.pay.cash"/></th>
	        	<th><@spring.message "admin.last.pay.time"/></th>
	        	<th><@spring.message "admin.last.pay.overview"/></th>
	        </tr>
	        <tr id="report_member_data_tb_deposit"></tr>
	        </table>
	    </div>
	</div>
	<div class="col-lg-12" style="padding-left:0">
	    <div class="panel panel-default"  style="border-radius:0">
	        <table class="table table-bordered report-table">
	        <tr>
	        	<th><@spring.message "admin.online.withdraw.total"/></th>
	        	<th><@spring.message "admin.hand.deduce.cash.total"/></th>
	        	<th><@spring.message "admin.color.deduce.total"/></th>
	        	<th><@spring.message "admin.last.draw.cash"/></th>
	        	<th><@spring.message "admin.last.draw.time"/></th>
	        	<th><@spring.message "admin.last.draw.overview"/></th>
	        </tr>
	        <tr id="report_member_data_tb_draw"></tr>
	        </table>
	    </div>
	</div>
	<div class="col-lg-12" style="padding-left:0">
	    <div class="panel panel-default"  style="border-radius:0">
	        <table class="table table-bordered report-table">
	        <tr>
	        	<th><@spring.message "admin.deposit.give.total"/></th>
	        	<th><@spring.message "admin.act.jack.total"/></th>
	        	<th><@spring.message "admin.red.packet.jack.total"/></th>
	        	<th><@spring.message "admin.backwater.total"/></th>
	        </tr>
	        <tr id="report_member_data_tb_gift"></tr>
	        </table>
	    </div>
	</div>
	<div class="col-lg-12" style="padding-left:0">
	    <div class="panel panel-default" style="border-radius:0">
	        <table class="table table-striped table-bordered report-table">
	        <tr>
	        	<th width="80px"></th>
	        	<#if game.live==2><th><@spring.message "manager.live"/></th></#if>
	        	<#if game.egame==2><th><@spring.message "manager.egame"/></th></#if>
	        	<#if game.sport==2><th><@spring.message "manager.sport"/></th></#if>
	        	<#if game.chess==2><th><@spring.message "manager.chess"/></th></#if>
	        	<#if game.esport==2><th><@spring.message "manager.esport"/></th></#if>
	        	<#if game.fishing==2><th><@spring.message "manager.fish"/></th></#if>
	        	<#if game.lottery==2><th><@spring.message "manager.lottery"/></th></#if>
	        </tr>
	        <tbody id="report_member_data_tb_bet">
	        <tr><th><@spring.message "admin.pay.rec"/></th></tr>
	        <tr><th><@spring.message "admin.valid.pay"/></th></tr>
	        <tr><th><@spring.message "admin.withdraw.info.lotteryWinAmount"/></th></tr>
	        <tr><th><@spring.message "admin.win.lose"/></th></tr>
	        </tbody></table>
	    </div>
	</div>
</div>
<script type="text/javascript">
requirejs(['${base}/common/js/admin/userData.js?v=6'], function (report) {
    report.render({live:"${game.live==2}",egame:"${game.egame==2}",sport:"${game.sport==2}",
 		chess:"${game.chess==2}",esport:"${game.esport==2}",fishing:"${game.fishing==2}",lottery:"${game.lottery==2}"});
});
</script>
