<form action="${adminBase}/user/doBatchModifyProxy.do" class="form-submit" id="member_proxy_batch_modify_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.bat.update.upproxy"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr><td width="30%" class="text-right media-middle"><@spring.message "admin.deposit.handle.username"/>：</td>
	                    <td colspan="3">
	                        <textarea name="usernames" style="height:150px;width:100%;"></textarea>
	                        <div>
	                            <br><@spring.message "admin.data.format.blank"/>
	                            <br>如：aaaaaa bbbbbb cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
	                            <br> &nbsp; &nbsp; &nbsp; &nbsp;aaaaaa,bbbbbb,cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
	                        </div>
	                    </td>
                    </tr>
                    <#if fixRebate>
                	<tr>
                		<td class="text-right media-middle"><@spring.message "admin.point.proxy"/>：</td>
                        <td><input type="text" name="proxyName" class="form-control"/></td>
                        <#if thirdGame.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td>${sr.live}</td></#if>
                    </tr>
                    <tr>
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
                    </tr>
                    <tr>
                    	<#if thirdGame.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td>${sr.fishing}</td></#if>
                    	<#if thirdGame.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td>${sr.lottery}</td></#if>
                    </tr>
                    <#else>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "admin.point.proxy"/>：</td>
                        <td><input type="text" name="proxyName" class="form-control"/></td>
                        <#if thirdGame.live==2><td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td><select name="liveRebate" class="form-control">
                            <#list liveArray as k><option value="${k.value}"<#if rebate.live==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.egame==2><td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td><select name="egameRebate" class="form-control">
                            <#list egameArray as k><option value="${k.value}"<#if rebate.egame==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    	<#if thirdGame.chess==2><td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td><select name="chessRebate" class="form-control">
                            <#list chessArray as k><option value="${k.value}"<#if rebate.chess==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                    	<#if thirdGame.sport==2><td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td><select name="sportRebate" class="form-control">
                            <#list sportArray as k><option value="${k.value}"<#if rebate.sport==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                        <#if thirdGame.esport==2><td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td><select name="esportRebate" class="form-control">
                            <#list esportArray as k><option value="${k.value}"<#if rebate.esport==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    </tr>
                    <tr>
                        <#if thirdGame.fishing==2><td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td><select name="fishingRebate" class="form-control">
                            <#list fishingArray as k><option value="${k.value}"<#if rebate.fishing==k.value>selected</#if>>${k.label}</option></#list>
                        </select></td></#if>
                    	<#if thirdGame.lottery==2><td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td><select name="lotteryRebate" class="form-control">
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
    var $form = $("#member_proxy_batch_modify_form_id"),usernames = '',$proxyName = $form.find("[name='proxyName']");
    $("#user_manager_datagrid_id").find('tbody input:checkbox:checked').each(function (i, j) {
        j = $(j);
        if (j.val()=='on') {
            return ;
        }
        usernames += j.val() + ",";
    });
    usernames = usernames.substring(0, usernames.length - 1);
    $form.find('textarea[name="usernames"]').html(usernames);
    
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
                    $form.find("[name='sportRebate']").html(options);
                }
                if (result.egameArray) {
                    var options = "";
                    $.each(result.egameArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='egameRebate']").html(options);
                }
                if (result.liveArray) {
                    var options = "";
                    $.each(result.liveArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='liveRebate']").html(options);
                }
                if (result.chessArray) {
                    var options = "";
                    $.each(result.chessArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='chessRebate']").html(options);
                }
                if (result.esportArray) {
                    var options = "";
                    $.each(result.esportArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='esportRebate']").html(options);
                }
                if (result.fishingArray) {
                    var options = "";
                    $.each(result.fishingArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='fishingRebate']").html(options);
                }
                if (result.lotteryArray) {
                    var options = "";
                    $.each(result.lotteryArray, function (index, el) {
                        options += "<option value='" + el.value + "'>" + el.label + "</option>";
                    });
                    $form.find("[name='lotteryRebate']").html(options);
                }
            }
        });
    });
});
</script>
