<div class="panel panel-info" id="viewUserDetailDivId">
	<div class="panel-heading">
		<h3><@spring.message "admin.current.vip"/>：<span class="label label-warning">${user.username}</span>
		<#if loginInfo.onlineStatus ==2>
            <span class="label label-success"><@spring.message "admin.line.on"/></span>
        <#else>
            <span class="label label-default"><@spring.message "admin.line.off"/></span>
        </#if></h3>
	</div>
  	<div class="panel-body">
  		<div><h5>(于${user.createDatetime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.pass.agree"/><span class="text-danger">${user.registerOs }</span><@spring.message "admin.system.register"/>,
  			 <@spring.message "admin.regist.ip"/><span class="text-danger">${user.registerIp}</span>,
  			 <@spring.message "admin.regist.from"/>:<span class="text-danger">${user.registerUrl}</span>)</h5>
  		</div>
  		<legend><@spring.message "admin.basic.info"/></legend>
  		<table class="table table-striped table-bordered">
            <tbody>
            <tr>
                <td><@spring.message "admin.finance.all.money"/>:<span class="text-danger">${money}<#if user.moneyStatus==1><span class="text-danger"><@spring.message "admin.forze"/></span></#if></span>
                    &nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.vip.add.discount"/>" href="${adminBase}/finance/moneyOpe/index.do?username=${user.username}"><@spring.message "admin.worker.save"/></a>
                    <#if permAdminFn.hadPermission("admin:moneyHistory")>&nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.vip.account.manage"/>" href="${adminBase}/finance/moneyHistory/index.do?username=${user.username}"><@spring.message "admin.account.change"/></a></#if>
                    <#if permAdminFn.hadPermission("admin:globalReport")>&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/globalReport/index.do?username=${user.username}"title="<@spring.message "admin.all.report.manage"/>"><@spring.message "admin.report"/></a></#if>
                    <#if permAdminFn.hadPermission("admin:moneyReport")>&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/moneyReport/index.do?username=${user.username}"title="<@spring.message "admin.finance.report.manage"/>"><@spring.message "admin.finance.cash"/></a></#if>
                    <#if permAdminFn.hadPermission("admin:userData")>&nbsp;&nbsp;<a class="open-tab"href="${adminBase}/userData/index.do?username=${user.username}"title="<@spring.message "admin.member.info"/>"><@spring.message "admin.memo"/></a></#if>
                </td>
                <td><@spring.message "admin.current.status"/>: <#if user.status == 2><span class="text-danger label label-success"><@spring.message "admin.deposit.bank.bankCard.status.yes"/></span><#else><span class="text-muted label label-default"><@spring.message "admin.deposit.bank.bankCard.status.no"/></span></#if></td>
                <td><@spring.message "admin.deposit.table.proxyName"/>:&nbsp;&nbsp;&nbsp;&nbsp;${parentNames}</td>
                <td><@spring.message "admin.deposit.table.depositType"/>:&nbsp;&nbsp;<#if user.type==120><@spring.message "admin.withdraw.type.proxy"/><#elseif user.type==130><@spring.message "admin.true.vip"/><#else><@spring.message "admin.test.vip"/></#if></td>
                <td><@spring.message "base.stationMemberLevel"/>：<span class="text-danger">${degreeName}</span></td>
                <td><@spring.message "manager.group.types"/>：<span class="text-danger">${groupName}</span></td>
            </tr>
		<#if userInfo?has_content>
            <tr>
            	<td><@spring.message "admin.real.name"/>：${userInfo.realName}</td>
                <td><@spring.message "admin.mobile.num"/>：${userInfo.phone}</td>
                <td>Facebook: ${userInfo.facebook}</td>
                <td><@spring.message "admin.wechat"/>: ${userInfo.wechat }</td>
            	<td>Email：${userInfo.email}</td>
                <td>QQ：${userInfo.qq}</td>
            </tr>
        </#if>
        <#list banks as b>
        	<tr>
        		<td><@spring.message "admin.real.name"/>:&nbsp;&nbsp;${b.realName}</td>
                <td><@spring.message "admin.bank.num"/>:&nbsp;&nbsp;${b.cardNo}<#if permAdminFn.hadPermission("admin:userBank")><a class="open-tab" title="<@spring.message "admin.vip.bank.card.manage"/>" href="${adminBase}/userBank/index.do?username=${user.username}"><@spring.message "admin.view.more.bank.num"/></a></#if></td>
                <td><@spring.message "admin.withdraw.bankName"/>:&nbsp;&nbsp;${b.bankName}</td>
                <td><@spring.message "admin.deposit.bank.bankCard.bankAddress"/>:&nbsp;&nbsp;${b.bankAddress}</td>
                <td colspan="2"><@spring.message "admin.insert.time"/>:&nbsp;&nbsp;${b.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
            </tr>
        </#list>
            <tr>
            	<td><@spring.message "admin.types.diff"/>:&nbsp;&nbsp;<#if user.type==130>${user.level }<@spring.message "admin.level.member"/><#elseif user.type==120>${user.level }<@spring.message "admin.proxy.level"/><#else><@spring.message "admin.test.account"/></#if></td>
            	<td><@spring.message "admin.scores"/>:&nbsp;&nbsp;${userScore }</td>
                <td><@spring.message "admin.bit.weight"/>:&nbsp;&nbsp;${bet.betNum }<#if permAdminFn.hadPermission("admin:betNumOpe")>&nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.vip.bit.weight"/>" href="${adminBase}/finance/betNumOpe/index.do?username=${user.username}"><@spring.message "admin.add.discount.weight"/></a></#if>
                    <#if permAdminFn.hadPermission("admin:betNumHistory")>&nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.account.code.change.rec"/>" href="${adminBase}/finance/betNumHistory/index.do?username=${user.username}"><@spring.message "admin.account.code.change.rec"/></a></#if>
                </td>
                <td><@spring.message "admin.found.need.weight"/>:&nbsp;&nbsp;${bet.drawNeed }</td>
                <td><@spring.message "admin.all.found.weight"/>:&nbsp;&nbsp;${bet.totalBetNum }</td>
                <td><@spring.message "admin.withdraw.remark"/>:&nbsp;&nbsp;${user.remark }</td>
            </tr>
            <tr>
                <td><@spring.message "admin.line"/>: ${userInfo.line }</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>
        <legend><@spring.message "admin.third.value.info"/></legend>
        <table class="table table-striped table-bordered">
            <tbody>
            <tr>
            	<#list platforms as p>
            	<td>${p.name}<@spring.message "admin.money.values"/>:<span class="text-danger platform${p.platform}">0</span>&nbsp;<a href="javascript:void(0)" class="getBalance" platform="${p.platform}"><@spring.message "admin.fresh"/></a></td>
            	<#if p_index%5==4 && p_has_next></tr><tr></#if>
            	</#list>
            </tr>
            </tbody>
       	</table>
       	<#if rebate?has_content><legend><@spring.message "admin.backwater.point"/></legend>
        <table class="table table-striped table-bordered">
            <tbody>
            <tr><td width="150px"><#if user.type == 120><@spring.message "manager.proxy.points"/>：<#elseif user.type==130><@spring.message "manager.vip.member.points"/>：</#if></td>
            <td>
             	<#if thirdGame.live==2>&nbsp;&nbsp;<@spring.message "manager.live.points"/>：<span class="text-danger">${rebate.live}‰</span>;</#if>
                <#if thirdGame.egame==2>&nbsp;&nbsp;<@spring.message "manager.egame.points"/>：<span class="text-danger">${rebate.egame}‰</span>;</#if>
                <#if thirdGame.sport==2>&nbsp;&nbsp;<@spring.message "manager.sprots.points"/>：<span class="text-danger">${rebate.sport}‰</span>;</#if>
                <#if thirdGame.chess==2>&nbsp;&nbsp;<@spring.message "manager.chess.points"/>：<span class="text-danger">${rebate.chess}‰</span>;</#if>
                <#if thirdGame.esport==2>&nbsp;&nbsp;<@spring.message "manager.esports.points"/>：<span class="text-danger">${rebate.esport}‰</span>;</#if>
                <#if thirdGame.fishing==2>&nbsp;&nbsp;<@spring.message "manager.fish.points"/>：<span class="text-danger">${rebate.fishing}‰</span>;</#if>
                <#if thirdGame.lottery==2>&nbsp;&nbsp;<@spring.message "manager.lottery.points"/>：<span class="text-danger">${rebate.lottery}‰</span></#if>
            </td></tr>
            </tbody>
        </table></#if>
        <legend><@spring.message "admin.daily.record"/></legend>
        <table class="table table-striped table-bordered">
            <tbody>
            <tr>
        	<#if thirdGame.live==2>
                <th class="text-right"><@spring.message "admin.live.record"/>：</th>
                <td>${todayMoney.liveBetAmount}/${todayMoney.liveBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/liveRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
        	<#if thirdGame.egame==2>
                <th class="text-right"><@spring.message "admin.egame.record"/>：</th>
                <td>${todayMoney.egameBetAmount}/${todayMoney.egameBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/egameRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
            <#if thirdGame.chess==2>
                <th class="text-right"><@spring.message "admin.chess.record"/>：</th>
                <td>${todayMoney.chessBetAmount}/${todayMoney.chessBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/chessRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
            <#if thirdGame.sport==2>
                <th class="text-right"><@spring.message "admin.sports.record"/>：</th>
                <td>${todayMoney.sportBetAmount}/${todayMoney.sportBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/sportRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
            </tr>
            <tr>
            <#if thirdGame.esport==2>
                <th class="text-right"><@spring.message "admin.power.record"/>：</th>
                <td>${todayMoney.esportBetAmount}/${todayMoney.esportBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/esportRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
            <#if thirdGame.fishing==2>
                <th class="text-right"><@spring.message "admin.fish.record"/>：</th>
                <td>${todayMoney.fishingBetAmount}/${todayMoney.fishingBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/fishingRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
            <#if thirdGame.lottery==2>
                <th class="text-right"><@spring.message "admin.lottery.record"/>：</th>
                <td>${todayMoney.lotBetAmount}/${todayMoney.lotBetNum}&nbsp;&nbsp;<a class="open-tab" href="${adminBase}/lotRecord/index.do?username=${user.username}"><@spring.message "admin.view.record"/></a></td>
            </#if>
            </tr>
            </tbody>
        </table>
        <#if permAdminFn.hadPermission("admin:user:view:drawDepositInfo")>
        <legend><@spring.message "admin.save.draw.info"/></legend>
        <table class="table table-striped table-bordered">
            <tbody>
            <tr>
                <th class="text-right" width="10%" rowspan="4"><@spring.message "admin.draw.money.info"/>:</th>
                <th class="text-right" width="20%"><@spring.message "admin.count.draw.money"/>：</th>
                <td width="30%">${draw.times}&nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.website.record"/>" href="${adminBase}/finance/withdraw/index.do?username=${user.username}"><@spring.message "admin.view.draw.record"/></a></td>
                <th class="text-right" width="10%"><@spring.message "admin.all.draw.money"/>：</th>
                <td>${draw.totalMoney}</td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.withdraw.first"/>：</th>
                <td><#if draw.firstTime??>${draw.firstTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.withdraw"/><span class="text-danger">${draw.firstMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
                <th class="text-right"><@spring.message "admin.most.high.draw"/>：</th>
                <td><#if draw.maxTime??>${draw.maxTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.withdraw"/><span class="text-danger">${draw.maxMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.withdraw.second"/>：</th>
                <td><#if draw.secTime??>${draw.secTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.withdraw"/><span class="text-danger">${draw.secMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
                <th class="text-right"><@spring.message "admin.withdraw.third"/>：</th>
                <td><#if draw.thirdTime??>${draw.thirdTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.withdraw"/><span class="text-danger">${draw.thirdMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.daily.draw.count"/>：</th>
                <td>${todayDraw.times }</td>
                <th class="text-right"><@spring.message "admin.daily.draw.money"/>：</th>
                <td>${todayDraw.money }</td>
            </tr>
            <tr>
                <th class="text-right" rowspan="4"><@spring.message "admin.pay.info"/>:</th>
                <th class="text-right"><@spring.message "admin.pay.all.count"/>：</th>
                <td>${deposit.times}&nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.website.pay.record"/>" href="${adminBase}/finance/deposit/index.do?username=${user.username}"><@spring.message "admin.view.save.rec"/></a></td>
                <th class="text-right"><@spring.message "admin.pay.all.money"/>：</th>
                <td>${deposit.totalMoney}</td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.deposit.num.first"/>：</th>
                <td><#if deposit.firstTime??>${deposit.firstTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.pay.record"/><span class="text-danger">${deposit.firstMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
                <th class="text-right"><@spring.message "admin.best.high.pay"/>：</th>
                <td><#if deposit.maxTime??>${deposit.maxTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.pay.record"/><span class="text-danger">${deposit.maxMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.deposit.num.second"/>：</th>
                <td><#if deposit.secTime??>${deposit.secTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.pay.record"/><span class="text-danger">${deposit.secMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
                <th class="text-right"><@spring.message "admin.deposit.num.third"/>：</th>
                <td><#if deposit.thirdTime??>${deposit.thirdTime?string('yyyy-MM-dd HH:mm:ss')}<@spring.message "admin.pay.record"/><span class="text-danger">${deposit.thirdMoney}</span><@spring.message "admin.deposit.table.money.unit"/></#if></td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.daily.pay.count"/>：</th>
                <td>${todayDeposit.times }</td>
                <th class="text-right"><@spring.message "admin.daily.pay.money"/>：</th>
                <td>${todayDeposit.money }</td>
            </tr>
            <tr>
                <th class="text-right"><@spring.message "admin.save.money.draw.money"/>：</th>
                <td colspan="4">${deposit.totalMoney-draw.totalMoney}</td>
            </tr>
            </tbody>
        </table>
    </#if>
    <legend><@spring.message "admin.login.info"/></legend>
    <div><@spring.message "admin.last.logout.ip"/>：<span class="text-danger">${loginInfo.lastLoginIp }</span>
    	&nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.vip.login.log"/>" href="${adminBase}/loginLog/index.do?username=${user.username}"><@spring.message "admin.view.login.log"/></a>
        &nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.vip.login.log"/>" href="${adminBase}/loginLog/index.do?loginIp=${user.lastLoginIp }"><@spring.message "admin.view.ip.login.log"/></a>
        &nbsp;&nbsp;<a class="open-tab" title="<@spring.message "admin.oper.log"/>" href="${adminBase}/oplog/index.do?username=${user.username}"><@spring.message "admin.oper.log"/></a>
    </div>
    <#if loginInfo.lastLoginDatetime??><div><@spring.message "manager.last.logout.time"/>：${loginInfo.lastLoginDatetime?string('yyyy-MM-dd HH:mm:ss')}</div></#if>
    <#if loginInfo.terminal??><div><@spring.message "admin.login.equip"/>：<#if loginInfo.terminal==1>PC<#elseif loginInfo.terminal==2>WAP<#elseif loginInfo.terminal==3>APP</#if></div></#if>
  	</div>
</div>
<script type="application/javascript">
$(function () {
	let div=$("#viewUserDetailDivId");
	function getBalance(p) {
	    $.ajax({
	        type: 'GET',
	        url: '${adminBase}/thirdMoney/getBalance.do',
	        data: {'userId': '${user.id}', 'platform': p},
	        dataType: "json",
	        success: function (json) {
	            if (json.success || json.success === 'true') {
	                div.find(".platform" + p).html(json.money);
	            } else {
	                div.find(".platform" + p).html(0);
	            }
	        }, errorFn: function (json) {
	            div.find(".platform" + p).html(json.msg);
	        },
	        error: function () {
	            div.find(".platform" + p).html("<@spring.message "admin.execp"/>");
	        }
	    });
    }
    div.find(".getBalance").click(function(){
    	var p=$(this).attr("platform");
    	getBalance(p);
    });
});
</script>
            
            
