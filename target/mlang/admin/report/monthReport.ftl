<div id="report_comprehensive_container_id">
    <div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.win.lose.total.two"/></h3>
        </div>
        <table width="100%">
            <tr>
                <td width="50%"><div class="canavsShow" style="width:100%;height:200px"></div></td>
                <td width="50%">
                    <table>
                        <tr>
                            <td width="15%" class="text-center"><h4 class="text-muted"><@spring.message "admin.withdraw.info.lotteryBetAmount"/></h4></td>
                            <td width="15%" class="text-center"><h4 class="text-muted"><@spring.message "admin.pay.rec.yesterday"/></h4></td>
                        </tr>
                        <tr>
                            <td class="text-center"><h3 class="text-success betNumTdy">0.00</h3></td>
                            <td class="text-center"><h3 class="text-success betNumYes">0.00</h3></td>
                        </tr>
                        <tr>
                            <td class="text-center"><h4 class="text-muted"><@spring.message "admin.daily.win.lose"/></h4></td>
                            <td class="text-center"><h4 class="text-muted"><@spring.message "admin.yesterday.win.lose"/></h4></td>
                        </tr>
                        <tr>
                            <td class="text-center"><h3 class="bunkoTdy">0.00</h3></td>
                            <td class="text-center"><h3 class="bunkoYes">0.00</h3></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.month.total"/></h3>
        </div>
        <table class="table table-bordered report-table table-striped">
        <tr>
        	<th><@spring.message "admin.month.days"/></th>
        	<th><@spring.message "admin.pay.betnum"/></th>
        	<#if game.live==2><th><@spring.message "admin.live.win.lose"/></th></#if>
        	<#if game.egame==2><th><@spring.message "admin.egame.win.lose"/></th></#if>
        	<#if game.sport==2><th><@spring.message "admin.sports.win.lose"/></th></#if>
        	<#if game.chess==2><th><@spring.message "admin.chess.win.lose"/></th></#if>
        	<#if game.esport==2><th><@spring.message "admin.esports.win.lose"/></th></#if>
        	<#if game.fishing==2><th><@spring.message "admin.fish.win.lose"/></th></#if>
        	<#if game.lottery==2><th><@spring.message "admin.lottery.win.lose"/></th></#if>
        </tr>
        <tbody id="monthReportStatMonthTr"></tbody>
        </table>
    </div>
	<div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.total.overview"/></h3>
        </div>
        <table class="table table-bordered report-table">
        <tr>
        	<th><@spring.message "admin.member.user.num.total"/></th>
        	<th><@spring.message "admin.withdraw.type.proxy"/></th>
        	<th><@spring.message "admin.withdraw.type.member"/></th>
        	<th><@spring.message "admin.online.vip.num"/></th>
        	<th><@spring.message "admin.balance.all"/></th>
        </tr>
        <tr id="monthReportStatTr"></tr>
        </table>
    </div>
    <div class="panel panel-default" style="border-radius:0">
        <div class="panel-heading" style="border-radius:0">
            <h3 class="panel-title text-center"><@spring.message "admin.daily.yesterday.overview"/></h3>
        </div>
        <table class="table table-bordered report-table">
        <tr>
        	<th><@spring.message "admin.date.time.days"/></th>
        	<th><@spring.message "admin.register"/></th>
        	<th><@spring.message "admin.pay.rec"/></th>
        	<th><@spring.message "admin.jack.point"/></th>
        	<th><@spring.message "admin.pay.record"/></th>
        	<th><@spring.message "admin.withdraw"/></th>
        	<th><@spring.message "admin.back.point"/></th>
        	<th><@spring.message "admin.backwater"/></th>
        </tr>
        <tbody id="monthReport2DaysTr"></tbody>
        </table>
    </div>
    <div style="padding: 10px;">
        <span><@spring.message "admin.win.lose.formula.two"/>：</span>
        <span class="text-danger"><@spring.message "admin.win.lose.red.packet.act"/></span>
        <br>
        <span class="text-primary"><@spring.message "admin.total.result.time"/></span><br>
    </div>
</div>
<script src="${base}/common/lang/${language}.js?v=5"></script>
<script type="text/javascript">
    requirejs(['${base}/common/js/admin/monthReport.js?v=3.2'], function (compre) {
        compre.render({live:"${game.live==2}",egame:"${game.egame==2}",sport:"${game.sport==2}",chess:"${game.chess==2}",esport:"${game.esport==2}",fishing:"${game.fishing==2}",lottery:"${game.lottery==2}"});
    });
</script>
