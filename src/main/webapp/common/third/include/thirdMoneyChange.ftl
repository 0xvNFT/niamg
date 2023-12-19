<header>
    <div class="header_top">
        <ul>
			<li><@spring.message "admin.system.val"/>：<span class="station_balance">0</span><button class="refresh"><@spring.message "admin.fresh"/></button></li>
            <li><span id="thirdName"><@spring.message "admin.third.value.info"/>：</span><span id="thirdMoney">0</span></li>
            <li class="recharge"><a class="money" href="${base}/userCenter/index.do#/recharge" target="_parent"><@spring.message "admin.quick.pay"/></a></li>
        </ul>
        <!--
    	<#if thirdAutoExchange><font color="taeTip"><@spring.message "admin.notice.turn.game.val"/>。</font></#if>
        -->
    </div>
    <ul class="balance-select">
        <li>
            <span><@spring.message "admin.turn.out"/></span>
			<select name="outcash">
                <option value="sys">
                    <@spring.message "admin.system.cash"/>
                </option>
                <#list third as t>
                    <option value="${t.platform}">${t.name}<@spring.message "admin.cash.v"/>
                    </option>
                </#list>
            </select>
        </li>
        <li>
            <span><@spring.message "admin.turn.into"/></span>
            <select name="incash">
                <#list third as t>
                    <option value="${t.platform}">${t.name }<@spring.message "admin.cash.v"/>
                    </option>
                </#list>
                <option value="sys"><@spring.message "admin.system.cash"/></option>
            </select>
        </li>
        <li>
            <span><@spring.message "admin.input.cash.v"/></span>
            <input type="number" placeholder="<@spring.message "admin.input.cash.v"/>" name="outCashInput">
        </li>
        <li>
            <button type="button" class="balance-lastli" id="change_btn"><@spring.message "admin.sure.input.cash"/></button>
        </li>
	</ul>
</header>