
<form class="table-tool" method="post" id="report_global_form_id">
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
                <input type="text" class="form-control" name="proxyName" placeholder="<@spring.message "admin.deposit.parent"/>">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="agentUser" placeholder="<@spring.message "admin.withdraw.agentUser"/>" value="${agentUser}">
            </div>
            <div class="input-group">
                <select class="form-control selectpicker" data-live-search="true" title="<@spring.message "admin.deposit.all.degree"/>" multiple id="globalReportUserDegreeId">
                    <#list degrees as le>
                        <option value="${le.id}">${le.name}</option>
                    </#list>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="userType">
                    <option value=""><@spring.message "admin.account.all.types"/></option>
                    <option value="130"><@spring.message "admin.withdraw.type.member"/></option>
                    <option value="120"><@spring.message "admin.withdraw.type.proxy"/></option>
                </select>
            </div>
            <input type="hidden" id="globalReportUserDegreeIdStr" name="degreeIds">
            <button class="btn btn-primary search-btn fui-date-search" type="button"><@spring.message "manager.check.select"/></button>
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
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.vip.account"/>" value="${username}">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="userRemark" placeholder="<@spring.message "admin.vip.remark"/>">
            </div>
            <div class="input-group">
                <select class="form-control" name="level">
                    <option value=""><@spring.message "admin.floor.level.degree"/></option>
                    <#list 1 .. lowestLevel as i><option value="${i}">${i}<@spring.message "admin.level.degree"/></option></#list>
                </select>
            </div>
        </div>
    </div>
</form>
<div class="col-lg-9" style="padding-left:0">
    <div class="panel panel-default"  style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.pay.draw.total"/></h3>
        </div>
        <table class="table table-bordered report-table">
        <tr>
        	<th><@spring.message "admin.online.draw.total"/></th>
        	<th><@spring.message "admin.deposit.type.manual"/></th>
        	<th><@spring.message "manager.total.money"/></th>
        	<th><@spring.message "admin.online.withdraw.total"/></th>
        	<th><@spring.message "admin.money.win.total"/></th>
        	<th><@spring.message "manager.hand.out.money"/></th>
        	<th><@spring.message "manager.hand.input.money"/></th>
        	<th><@spring.message "admin.other.add.cash"/></th>
        	<th><@spring.message "admin.color.cash.out"/></th>
        </tr>
        <tr id="report_global_deport_withdwar_tr"></tr>
        </table>
    </div>
</div>
<div class="col-lg-3">
<div class="panel panel-default" style="border-radius:0">
    <div class="panel-heading" style="border-radius:0">
        <h3 class="panel-title text-center"><@spring.message "admin.all.game.total"/></h3>
    </div>
    <table class="table table-bordered report-table">
	<tr>
    	<th><@spring.message "admin.pay.per.total"/></th>
    	<th><@spring.message "admin.charge.per.total"/></th>
    	<th><@spring.message "admin.draw.all.per.total"/></th>
    </tr>
    <tr id="report_global_stat_tr"></tr>
    </table>
</div>
</div>
<div class="col-lg-12" style="padding-left:0">
    <div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.activity.give.total"/></h3>
        </div>
        <table class="table table-bordered report-table">
        <tr>
        	<th><@spring.message "admin.proxy.seedback.total"/></th>
        	<th><@spring.message "admin.proxy.seecback.per.num"/></th>
        	<th><@spring.message "admin.backwater.total"/></th>
        	<th><@spring.message "admin.backwater.per.num"/></th>
        	<th><@spring.message "admin.deposit.give.total"/></th>
        	<th><@spring.message "admin.act.jack.total"/></th>
        	<th><@spring.message "admin.score.exchange"/></th>
        	<th><@spring.message "admin.red.packet.jack.total"/></th>
        </tr>
        <tr id="report_global_active_tr"></tr>
        </table>
    </div>
</div>
<div class="col-lg-12" style="padding-left:0">
    <div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.game.total"/></h3>
        </div>
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
        <tbody id="report_global_bet_win_tb">
        <tr><th><@spring.message "admin.pay.rec"/></th></tr>
        <tr><th><@spring.message "admin.valid.pay"/></th></tr>
        <tr><th><@spring.message "admin.withdraw.info.lotteryWinAmount"/></th></tr>
        <tr><th><@spring.message "admin.win.lose"/></th></tr>
        </tbody></table>
    </div>
</div>
<div class="col-lg-12" style="padding-left:0">
    <div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.deposit.table.all.sum"/></h3>
        </div>
        <table class="table table-bordered report-table">
        <tr>
        	<th><@spring.message "admin.pay.rec.total"/></th>
        	<th><@spring.message "admin.jack.total"/></th>
        	<th><@spring.message "admin.win.lose.total"/></th>
        </tr>
        <tr id="report_global_allbw_tr"></tr>
        </table>
    </div>
</div>
<div id="remark_div" style="padding: 10px;">
	<span><@spring.message "admin.act.jack.total"/>：</span><span class="text-danger"><@spring.message "admin.include.jack.vip.act"/></span><br>
    <span><@spring.message "admin.win.lose.formula"/>：</span>
    <span class="text-danger"><@spring.message "admin.pay.jack.win.lose"/></span><br>
    <span><@spring.message "admin.all.win.lose.formula"/>：</span>
    <span class="text-danger"><@spring.message "admin.compute.formula.all"/></span><br>
    <span><@spring.message "admin.jack.count.total"/>：</span>
    <span class="text-danger"><@spring.message "admin.jack.distance.time.task"/></span><br>
    <div style="color:blue;"><@spring.message "admin.total.result.time"/></div>
</div>
<script type="text/javascript">
requirejs(['${base}/common/js/admin/globalReport.js?v=4'],function(globalReport){
 	globalReport.render({live:"${game.live==2}",egame:"${game.egame==2}",sport:"${game.sport==2}",
 		chess:"${game.chess==2}",esport:"${game.esport==2}",fishing:"${game.fishing==2}",lottery:"${game.lottery==2}"});
});
</script>
