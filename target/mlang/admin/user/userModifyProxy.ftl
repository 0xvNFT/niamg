<form action="${adminBase}/user/doModifyProxy.do" class="form-submit" id="proxy_manager_modify_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.belong.proxy"/></h4>
            </div>
            <input type="hidden" name="id" value="${member.id }">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%"class="text-right media-middle"><@spring.message "admin.current.user"/>：</td>
                        <td width="30%" class="media-middle">${member.username }</td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.point.proxy"/>：</td>
                        <td><input type="text" name="proxyName" value="${member.proxyName}" class="form-control"/></td>
                    </tr>
                    <#if fixRebate>
                	<tr>
                        <#if thirdGame.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td>${sr.live}</td></#if>
                        <#if thirdGame.egame==2><td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td>${sr.egame}</td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.chess==2><td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td>${sr.chess}</td></#if>
                        <#if thirdGame.sport==2><td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td>${sr.sport}</td></#if>
                    </tr>
                    <tr>
                        <#if thirdGame.esport==2><td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td>${sr.esport}</td></#if>
                        <#if thirdGame.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td>${sr.fishing}</td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td>${sr.lottery}</td></#if>
                    </tr>
                    <#else>
                    <tr>
                        <#if thirdGame.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td><select name="live" class="form-control">
                            <#list liveArray as k><option value="${k.value}"<#if rebate.live==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.egame==2><td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td><select name="egame" class="form-control">
                            <#list egameArray as k><option value="${k.value}"<#if rebate.egame==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.chess==2><td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td><select name="chess" class="form-control">
                            <#list chessArray as k><option value="${k.value}"<#if rebate.chess==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.sport==2><td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td><select name="sport" class="form-control">
                            <#list sportArray as k><option value="${k.value}"<#if rebate.sport==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                        <#if thirdGame.esport==2><td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td><select name="esport" class="form-control">
                            <#list esportArray as k><option value="${k.value}"<#if rebate.esport==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td><select name="fishing" class="form-control">
                            <#list fishingArray as k><option value="${k.value}"<#if rebate.fishing==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td><select name="lottery" class="form-control">
                            <#list lotteryArray as k><option value="${k.value}"<#if rebate.lottery==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
requirejs(['jquery'], function () {
    var $form = $("#proxy_manager_modify_form_id"),$proxyName = $form.find("[name='proxyName']");
    $proxyName.blur(function () {
	    var proxyName = $proxyName.val();
	    if (proxyName == '') {
	        return;
	    }
        $.ajax({
            url: "${adminBase}/user/getUserRebate.do",
            data: {'proxyName': proxyName},
            type: 'get',
            success: function (result) {
                if (result.success == false) {
                    $proxyName.focus();
                    layer.msg(result.msg);
                    return;
                }
                if (result.sportArray) {
                    var options = "";
                    $.each(result.sportArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='sport']").html(options);
                }
                if (result.egameArray) {
                    var options = "";
                    $.each(result.egameArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='egame']").html(options);
                }
                if (result.liveArray) {
                    var options = "";
                    $.each(result.liveArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='live']").html(options);
                }
                if (result.chessArray) {
                    var options = "";
                    $.each(result.chessArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='chess']").html(options);
                }
                if (result.esportArray) {
                    var options = "";
                    $.each(result.esportArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='esport']").html(options);
                }
                if (result.fishingArray) {
                    var options = "";
                    $.each(result.fishingArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='fishing']").html(options);
                }
                if (result.lotteryArray) {
                    var options = "";
                    $.each(result.lotteryArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='lottery']").html(options);
                }
            }
        });
    });
});
</script>