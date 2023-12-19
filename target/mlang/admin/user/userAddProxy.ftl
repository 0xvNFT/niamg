<form action="${adminBase}/user/doAddProxy.do" method="post" class="form-submit" id="member_manager_add_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.proxy.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="13%" class="text-right media-middle"><@spring.message "admin.account.login"/>：</td>
                        <td width="20%"><input type="text" class="form-control" name="username"/></td>
                        <td width="13%" class="text-right media-middle"><@spring.message "manager.password.input"/>：</td>
                        <td width="20%"><input type="password" class="form-control" name="pwd"/></td>
                        <td width="13%" class="text-right media-middle"><@spring.message "admin.proxy.merchant"/>：</td>
                        <td><input type="text" class="form-control" name="agentName"/></td>
                    </tr>
                    <tr>
                    	<#if proxyModel==1||proxyModel==2><td class="text-right media-middle"><@spring.message "admin.point.proxy"/>：</td>
                        <td><input type="text" name="proxyName" class="form-control"/></td></#if>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                        <td<#if proxyModel==3> colspan="2"</#if>>
                            <select name="degreeId" class="form-control">
                                <#list degrees as le><option value="${le.id}">${le.name}</option></#list>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.vip.group"/>：</td>
                        <td<#if proxyModel==3> colspan="2"</#if>>
                            <select name="groupId" class="form-control">
                                <#list groups as le><option value="${le.id}">${le.name}</option></#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "admin.deposit.table.realName"/>：</td>
                        <td><input type="text" class="form-control" name="realName"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.telephone"/>：</td>
                        <td><input type="text" class="form-control" name="phone"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.mail"/>：</td>
                        <td><input type="text" class="form-control email" name="email"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">QQ：</td>
                        <td><input type="text" class="form-control" name="qq"/></td>
                        <td class="text-right media-middle">Line：</td>
                        <td><input type="text" class="form-control" name="wechat"/></td>
                        <td class="text-right media-middle">Facebook：</td>
                        <td><input type="text" class="form-control" name="facebook"/></td>
                    </tr>
                    <#if fixRebate>
                	<tr>
                        <#if thirdGame.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td>${sr.live}</td></#if>
                        <#if thirdGame.egame==2><td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td>${sr.egame}</td></#if>
                        <#if thirdGame.chess==2><td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td>${sr.chess}</td></#if>
                    </tr>
                    <tr>
                        <#if thirdGame.sport==2><td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td>${sr.sport}</td></#if>
                        <#if thirdGame.esport==2><td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td>${sr.esport}</td></#if>
                        <#if thirdGame.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td>${sr.fishing}</td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td>${sr.lottery}</td></#if>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td colspan="3"><input type="text" class="form-control" name="remark"/></td>
                    </tr>
                    <#else>
                    <tr>
                        <#if thirdGame.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td><select name="live" class="form-control">
                            <#list liveArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.egame==2><td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td><select name="egame" class="form-control">
                            <#list egameArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.chess==2><td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td><select name="chess" class="form-control">
                            <#list chessArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                        <#if thirdGame.sport==2><td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td><select name="sport" class="form-control">
                            <#list sportArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.esport==2><td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td><select name="esport" class="form-control">
                            <#list esportArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td><select name="fishing" class="form-control">
                            <#list fishingArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td><select name="lottery" class="form-control">
                            <#list lotteryArray as k><option value="${k.value}">${k.label}</option></#list>
                        </select></td></#if>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td colspan="3"><input type="text" class="form-control" name="remark"/></td>
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
</form><#if !fixRebate>
<script type="text/javascript">
requirejs(['jquery'], function () {
    var $form = $("#member_manager_add_form_id"),$proxyName=$form.find("[name='proxyName']");
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
