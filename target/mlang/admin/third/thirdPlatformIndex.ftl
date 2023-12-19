<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><@spring.message "admin.thirdPlatform.kg1"/><em class="text-danger"><@spring.message "admin.thirdPlatform.kg1Tip"/></em></h3>
    </div>
    <table class="table table-striped table-bordered">
        <tr>
            <td class="text-right"><@spring.message "admin.live"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.live==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=live&status=" data-on-value="2" data-off-value="1"></td>
            <td class="text-right"><@spring.message "admin.egame"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.egame==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=egame&status=" data-on-value="2" data-off-value="1"></td>
            <td class="text-right"><@spring.message "admin.chess"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.chess==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=chess&status=" data-on-value="2" data-off-value="1"></td>
            <td class="text-right"><@spring.message "admin.esport"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.esport==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=esport&status=" data-on-value="2" data-off-value="1"></td>
            <td class="text-right"><@spring.message "admin.sport"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.sport==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=sport&status=" data-on-value="2" data-off-value="1"></td>
            <td class="text-right"><@spring.message "admin.fishing"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.fishing==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=fishing&status=" data-on-value="2" data-off-value="1"></td>
             <td class="text-right"><@spring.message "manager.lottery"/>：</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if game.lottery==2>checked</#if> data-url="${adminBase}/thirdPlatform/changeGameStatus.do?type=lottery&status=" data-on-value="2" data-off-value="1"></td>
        </tr>
    </table>
</div>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><@spring.message "admin.thirdPlatform.kg"/>：</h3>
    </div>
    <table class="table table-striped table-bordered">
        <tr>
            <th width="30%" class="text-right"><@spring.message "admin.thirdPlatform.pname"/>：</th>
            <th><@spring.message "admin.status"/>：</th>
        </tr>
        <#list platforms as p>
        <tr>
            <td class="text-right">${p.name}</td>
            <td><input class="fui-switch" type="checkbox" data-on-text="<@spring.message "admin.opend"/>" data-off-text="<@spring.message "admin.closed"/>"<#if p.status==2>checked</#if> data-url="${adminBase}/thirdPlatform/changePlatformStatus.do?id=${p.id}&status=" data-on-value="2" data-off-value="1"></td>
        </tr>
        </#list>
    </table>
</div>