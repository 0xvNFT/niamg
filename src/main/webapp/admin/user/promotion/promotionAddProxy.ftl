<form action="${adminBase}/promotion/doAddProxy.do" class="form-submit" id="promotionAddProxyFormId">
<div class="modal-dialog modal-lg">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.add.proxy.extend.link"/></h4>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="20%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
						<td width="30%"><input type="text" name="username" class="form-control"/></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.extend.page"/>：</td>
                        <td>
                            <select name="accessPage" class="form-control">
                                <option value="1"><@spring.message "admin.register"/></option>
                                <option value="2"><@spring.message "admin.main.page"/></option>
                                <option value="3"><@spring.message "admin.discount.activit"/></option>
                            </select>
                        </td>
                    </tr>
					<tr>
                        <td class="text-right media-middle"><@spring.message "admin.extend.domain"/>：</td>
						<td><input name="domain" class="form-control"/></td>
						<td class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
						<td class="media-middle"><#if proxyModel==1 || proxyModel==2><label class="radio-inline"><input type="radio" name="type" value="120"checked> <@spring.message "admin.withdraw.type.proxy"/></label>&nbsp;&nbsp;</#if>
							<#if proxyModel!=1><label class="radio-inline"><input type="radio" name="type" value="130"checked> <@spring.message "admin.withdraw.type.member"/></label></#if>
                        </td>
					</tr>
					<#if fixRebate>
					<tr>
                        <#if game.live==2>
                        <td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td>${sr.live}</td></#if>
                        <#if game.egame==2>
                        <td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td>${sr.egame}</td></#if>
                    </tr>
					<tr>
                        <#if game.chess==2>
                        <td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td>${sr.chess}</td></#if>
                        <#if game.esport==2>
                        <td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td>${sr.esport}</td></#if>
					</tr>
					<tr>
                        <#if game.sport==2>
                        <td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td>${sr.sport}</td></#if>
                        <#if game.fishing==2>
                        <td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td>${sr.fishing}</td></#if>
					</tr>
					<tr>
                        <#if game.lottery==2>
                        <td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td>${sr.lottery}</td></#if>
					</tr>
					<#else>
					<tr>
                        <#if game.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td><select name="live" class="form-control">
                            <#list liveArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if game.egame==2><td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td><select name="egame" class="form-control">
                            <#list egameArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
					<tr>
                        <#if game.chess==2><td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td><select name="chess" class="form-control">
                            <#list chessArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if game.sport==2><td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td><select name="sport" class="form-control">
                            <#list sportArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                        <#if game.esport==2><td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td><select name="esport" class="form-control">
                            <#list esportArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if game.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td><select name="fishing" class="form-control">
                            <#list fishingArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                    	<#if game.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td><select name="lottery" class="form-control">
                            <#list lotteryArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
					</#if>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
            <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
            <button class="btn btn-primary"><@spring.message "admin.save"/></button>
        </div>
	</div>
</div><#if !fixRebate>
<script type="text/javascript">
requirejs(['jquery'], function () {
    var $form = $("#promotionAddProxyFormId"),$proxyName=$form.find("[name='username']");
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
</script></#if>