<form class="fui-search table-tool" method="post" id="member_manager_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" value="${startTime}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "manager.find.account.one"/>" style="width:125px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="usernameLike" placeholder="<@spring.message "manager.like.account.one"/>" style="width:125px;">
            </div>
            <div class="input-group">
                <select class="form-control" name="userType">
                    <option value=""><@spring.message "admin.deposit.type.all"/></option>
                    <#if proxyModel!=1><option value="130"><@spring.message "admin.withdraw.type.member"/></option></#if>
                    <#if proxyModel!=4><option value="120"><@spring.message "admin.withdraw.type.proxy"/></option></#if>
                </select>
            </div>
            <div class="input-group">
                <select name="degreeId" style="width: 100px" class="form-control selectpicker" title="<@spring.message "admin.deposit.all.degree"/>" data-live-search="true" multiple>
                    <#list degrees as le>
                        <option value="${le.id}"<#if degreeId == le.id>selected</#if> >${le.name}</option>
                    </#list>
                </select>
            </div>
            <div class="input-group">
                <select name="groupId" style="width: 100px" class="form-control selectpicker" title="<@spring.message "manager.all.group.types"/>" data-live-search="true" multiple>
                    <#list groups as le>
                        <option value="${le.id}"<#if groupId == le.id>selected</#if> >${le.name}</option>
                    </#list>
                </select>
            </div>
            <input type="hidden"  name="degreeIds" value="${degreeId}">
            <input type="hidden"  name="groupIds" value="${groupId}">
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:user:add")>
            <#if proxyModel!=1><button class="btn btn-primary open-dialog" type="button" url="${proxyBase}/user/showAddMember.do"><@spring.message "manager.vip.add"/></button></#if>
            <#if proxyModel!=4><button class="btn btn-primary open-dialog" type="button" url="${proxyBase}/user/showAddProxy.do"><@spring.message "manager.proxy.add"/></button></#if>
            </#if>
            <button class="btn btn-primary" id="member_manager_reset_btn" type="button"><@spring.message "manager.resetting"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${endTime}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="proxyName" value="${proxyName}" placeholder="<@spring.message "admin.deposit.parent"/>" style="width:100px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="agentName" value="${agentName}" placeholder="<@spring.message "admin.withdraw.agentUser"/>" style="width:100px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="recommendName" value="${recommendName}" placeholder="<@spring.message "admin.recommendUsername"/>" style="width:100px;">
            </div>
            <select class="form-control" name="depositStatus">
                <option value=""><@spring.message "admin.deposit.num"/></option>
                <option value="1" <#if depositStatus ==1>selected</#if> ><@spring.message "admin.have.pay"/></option>
                <option value="2" <#if depositStatus ==2>selected</#if> ><@spring.message "admin.not.have.pay"/></option>
                <option value="3"><@spring.message "admin.first.pay"/></option>
            </select>
            <select class="form-control" name="drawStatus">
                <option value=""><@spring.message "admin.withdraw.num"/></option>
                <option value="1"><@spring.message "admin.withdraw.have"/></option>
                <option value="2"><@spring.message "admin.withdraw.have.not"/></option>
            </select>
            <div class="input-group">
                <input type="text" class="form-control" name="minMoney" placeholder="<@spring.message "admin.value.bigger"/>" style="width:100px;">
            </div>
            <#if permAdminFn.hadAnyOnePermission("admin:moneyOpe","admin:user:update:degree","admin:user:update:status","admin:user:update:proxy","admin:user:update:group")>
            <div class="btn-group"><button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><@spring.message "admin.bat.oper"/><span class="caret"></span></button>
                <ul class="dropdown-menu">
                <#if permAdminFn.hadPermission("admin:moneyOpe")>
                    <li><a class="open-dialog" href="${proxyBase}/finance/moneyOpe/showBatchAdd.do"><@spring.message "admin.bat.add.money"/></a></li>
                </#if>
                <#if permAdminFn.hadPermission("admin:moneyOpe")>
                    <li><a class="open-dialog" href="${proxyBase}/finance/moneyOpe/showBatchSub.do"><@spring.message "admin.bat.decode.money"/></a></li>
                </#if>
                <#if permAdminFn.hadPermission("admin:user:update:degree")>
                    <li><a class="open-dialog" href="${proxyBase}/user/showBatchModifyDegree.do"><@spring.message "admin.bat.update.level"/></a></li>
                </#if>
                <#if proxyModel!=4 && permAdminFn.hadPermission("admin:user:update:proxy")>
                    <li><a class="open-dialog" href="${proxyBase}/user/showBatchModifyProxy.do"><@spring.message "admin.bat.update.upproxy"/></a></li>
                </#if>
                <#if permAdminFn.hadPermission("admin:user:update:group")>
                    <li><a class="open-dialog" href="${proxyBase}/user/showBatchModifyGroup.do"><@spring.message "admin.bat.update.group"/></a></li>
                </#if>
                <#if permAdminFn.hadPermission("admin:user:update:status")>
                    <li><a class="open-dialog" href="${proxyBase}/user/showBatchDisableStatus.do"><@spring.message "admin.bat.dis.user"/></a></li>
                </#if>
                <#if permAdminFn.hadPermission("admin:user:update:status")>
                    <li><a class="open-dialog" href="${proxyBase}/user/showBatchEnableStatus.do"><@spring.message "admin.bat.able.user"/></a></li>
                </#if>
                <#if permAdminFn.hadPermission("admin:user:update:status")>
                    <li><a class="open-dialog" href="${proxyBase}/scoreHistory/showBatchAdd.do"><@spring.message "admin.bat.score"/></a></li>
                </#if>
            </ul></div></#if>
        </div>
        <div class="form-inline mt5px">
        	<div class="input-group">
                <select class="form-control" name="keywordType">
                    <option value="1"><@spring.message "admin.deposit.table.realName"/></option>
                    <option value="2"><@spring.message "admin.mobile.num"/></option>
                    <option value="3">QQ</option>
                    <option value="4"><@spring.message "admin.mail"/></option>
                    <option value="5"><@spring.message "admin.wechat"/></option>
                    <option value="6"><@spring.message "admin.facebook.user"/></option>
                    <option value="7"><@spring.message "admin.regist.ip"/></option>
                    <option value="8"><@spring.message "admin.regist.from"/></option>
                    <option value="9"><@spring.message "admin.vip.remark"/></option>
                    <option value="10"><@spring.message "admin.line"/></option>
                </select>
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="keyword" placeholder="<@spring.message "admin.keyword.user"/>" style="width:100px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="proxyPromoCode" value="${proxyPromoCode}" placeholder="<@spring.message "admin.proxy.code.extend"/>" style="width:100px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" style="width:100px;" name="notLoginDay" placeholder="<@spring.message "admin.logout.days.not"/>">
            </div>
            <div class="input-group">
                <select class="form-control" name="level">
                    <option value=""><@spring.message "admin.floor.level.degree"/></option>
                    <#if (lowestLevel>0)><#list 1.. lowestLevel as i><option value="${i}">${i}<@spring.message "admin.level.degree"/></option></#list></#if>
                </select>
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="lastLoginIp" placeholder="<@spring.message "admin.last.logout.ip"/>" style="width:125px;">
            </div>
            <#if permAdminFn.hadPermission("admin:user:resetScore")>
                <a class="btn btn-primary todo-ajax" href="${proxyBase}/user/scoreToZero.do" title="<@spring.message "admin.sure.all.user.score.zero"/>？"><@spring.message "admin.all.score.zero"/></a>
            </#if>
            <#if permAdminFn.hadPermission("admin:user:export")>
                <a class="btn btn-primary exportBtn" href="#" title="<@spring.message "admin.export.vip"/>？"><@spring.message "admin.deposit.button.export"/></a>
            </#if>
            <#if oldProxyName!=null><a class="btn btn-primary open-tab"href="${proxyBase}/user/index.do?proxyName=${oldProxyName}"><@spring.message "admin.back"/></a></#if>
        </div>
    </div>
</form>
<div style="padding: 10px;">
    <div style="color:red;"><@spring.message "admin.query.where.regist.vip"/></div>
</div>
<#if permAdminFn.hadPermission("admin:user:export")>
<form id="member_manager_form_export_fmId" action="${proxyBase}/user/export.do" target="_blank" method="post">
    <input type="hidden" name="startTime"/>
    <input type="hidden" name="usernameLike"/>
    <input type="hidden" name="username"/>
    <input type="hidden" name="degreeIds"/>
    <input type="hidden" name="groupIds"/>
    <input type="hidden" name="keywordType"/>
    <input type="hidden" name="keyword"/>
    <input type="hidden" name="proxyPromoCode"/>
    <input type="hidden" name="endTime">
    <input type="hidden" name="proxyName">
    <input type="hidden" name="agentName">
    <input type="hidden" name="recommendName">
    <input type="hidden" name="depositStatus">
    <input type="hidden" name="drawStatus">
    <input type="hidden" name="minMoney">
    <input type="hidden" name="notLoginDay">
    <input type="hidden" name="userType">
    <input type="hidden" name="level">
    <input type="hidden" name="lastLoginIp">
</form></#if>
<table class="fui-default-table" id="user_manager_datagrid_id"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    	var degrees={},groups={};<#list degrees as le>degrees['${le.id}']='${le.name}';</#list>
    	<#list groups as le>groups['${le.id}']='${le.name}';</#list>
        var $form = $("#member_manager_form_id"), $table = $("#user_manager_datagrid_id");
        <#if permAdminFn.hadPermission("admin:user:export")>$form.find(".exportBtn").click(function () {
            var $form1 = $("#member_manager_form_export_fmId");
            $form1.find("input[name='startTime']").val($form.find("input[name='startTime']").val());
	        $form1.find("input[name='usernameLike']").val($form.find("input[name='usernameLike']").val());
	        $form1.find("input[name='username']").val($form.find("input[name='username']").val());
	        $form1.find("input[name='degreeIds']").val($form.find("input[name='degreeIds']").val());
	        $form1.find("input[name='groupIds']").val($form.find("input[name='groupIds']").val());
	        $form1.find("select[name='keywordType']").val($form.find("input[name='keywordType']").val());
	        $form1.find("input[name='keyword']").val($form.find("input[name='keyword']").val());
	        $form1.find("input[name='proxyPromoCode']").val($form.find("input[name='proxyPromoCode']").val());
	        $form1.find("input[name='endTime']").val($form.find("input[name='endTime']").val());
	        $form1.find("input[name='proxyName']").val($form.find("input[name='proxyName']").val());
	        $form1.find("input[name='agentName']").val($form.find("input[name='agentName']").val());
	        $form1.find("input[name='recommendName']").val($form.find("input[name='recommendName']").val());
	        $form1.find("select[name='depositStatus']").val($form.find("select[name='depositStatus']").val());
	        $form1.find("select[name='drawStatus']").val($form.find("select[name='drawStatus']").val());
	        $form1.find("input[name='minMoney']").val($form.find("input[name='minMoney']").val());
	        $form1.find("input[name='notLoginDay']").val($form.find("input[name='notLoginDay']").val());
	        $form1.find("select[name='userType']").val($form.find("select[name='userType']").val());
	        $form1.find("select[name='level']").val($form.find("select[name='level']").val());
	        $form1.find("input[name='lastLoginIp']").val($form.find("input[name='lastLoginIp']").val());
            $form1.submit();
        });</#if>
        $form.find("#member_manager_reset_btn").click(function(){
        	$form.find("input[name='startTime']").val('');
	        $form.find("input[name='usernameLike']").val('');
	        $form.find("input[name='username']").val('');
	        $form.find("select[name='degreeId']").selectpicker('deselectAll');
	        $form.find("input[name='degreeIds']").val('');
	        $form.find("select[name='groupId']").selectpicker('deselectAll');
	        $form.find("input[name='groupIds']").val('');
	        $form.find("select[name='keywordType']").val('1');
	        $form.find("input[name='keyword']").val('');
	        $form.find("input[name='proxyPromoCode']").val('');
	        $form.find("input[name='endTime']").val('');
	        $form.find("input[name='proxyName']").val('');
	        $form.find("input[name='recommendName']").val('');
	        $form.find("input[name='agentName']").val('');
	        $form.find("select[name='depositStatus']").val('');
	        $form.find("select[name='drawStatus']").val('');
	        $form.find("input[name='minMoney']").val('');
	        $form.find("input[name='notLoginDay']").val('');
	        $form.find("select[name='userType']").val('');
	        $form.find("select[name='level']").val('');
	        $form.find("input[name='lastLoginIp']").val('');
        });
        $form.find("[name='degreeId']").change(function () {
            var types = $(this).val();
            if (types) {
                $form.find("[name='degreeIds']").val(types.join(","));
            } else {
                $form.find("[name='degreeIds']").val("");
            }
        });
        $form.find("[name='groupId']").change(function () {
            var types = $(this).val();
            if (types) {
                $form.find("[name='groupIds']").val(types.join(","));
            } else {
                $form.find("[name='groupIds']").val("");
            }
        });
        Fui.addBootstrapTable({
            url: '${proxyBase}/user/list.do',
            columns: [{
                title: '<input type="checkbox" class="check_all">',
                align: 'center',
                vilign: 'middle',
                formatter: function(value, row, index) {
		            return '<input type="checkbox" value="' + row.username + '">';
		        }
            }, {
                field: 'username',
                title: Admin.userName,
                align: 'center',
                valign: 'middle',
                formatter: usernameFormatter
            <#if stationAdmin && viewContact>},{
                field: 'realName',
                title: Admin.realName,
                align: 'center',
                valign: 'middle',
                formatter: realNameFormatter
            },{
                field: 'phone',
                title: Admin.mobileNum,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                	return (value||"")+'<br>'+(row.facebook||"");
                }
            </#if>
            
            },{
                title: "<@spring.message "admin.proxy"/>/<@spring.message "admin.recommendUsername"/>",
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: proxyRecomFormatter
            },{
                field: 'type',
                title: Admin.type,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: userTypeFormatter
            }, {
                field: 'money',
                title: Admin.accountVal,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            <#if showScore>}, {
                field: 'score',
                title: Admin.scoreVal,
                align: 'center',
                sortable: true,
                valign: 'middle'</#if>
            }, {
                field: 'degreeId',
                title: Admin.memberLevel,
                align: 'center',
                valign: 'middle',
                formatter: degreeFormatter
            }, {
                field: 'groupId',
                title: Admin.memberGroup,
                align: 'center',
                valign: 'middle',
                formatter: groupFormatter
            },{
                field: 'createDatetime',
                title: Admin.registerTime,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: function (value, row, index) {
                    var s = row.registerIp || "";
                    s = s + "<br>";
                    s = s + (value ? Fui.formatDatetime(value) : "");
                    return s;
                }
            }, {
                field: 'lastLoginDatetime',
                title: Admin.lastLogin,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: function (value, row, index) {
                    var s = row.lastLoginIp || "";
                    s = s + "<br>";
                    s = s + (value ? Fui.formatDatetime(value) : "");
                    return s;
                }
            }]
        });

        $table.on("click", "input.check_all", function () {
            var $this = $(this), isChecked = $this.prop('checked');
            $table.find('tbody input:checkbox').prop('checked', isChecked);
        });
        function proxyRecomFormatter(value, row, index){
           var s = '';
           if (row.proxyName) {
                var oldProxyName = $form.find("[name='proxyName']").val();
                s = s + '</br><a class="open-tab" href="${proxyBase}/user/index.do?proxyName=' + row.proxyName + '&oldProxyName=' + oldProxyName + '">' + row.proxyName + '</a>';
            }
            <#if permAdminFn.hadPermission("admin:user:update:proxy")>
            if(("${proxyModel<3}"=="true" && row.type==120)||("${proxyModel!=4}"=="true" && row.type!=120)){
            	s += '&nbsp;&nbsp<a class="open-dialog" href="${proxyBase}/user/showModifyProxy.do?id=' + row.id + '" title="<@spring.message "admin.update.belong.proxy"/>"><i class="fa fa-pencil-alt"></i></a>';
            }
            </#if>
            if(row.recommendUsername){
            	s+= '<br><span class="text text-danger">'+row.recommendUsername+'</span >'
            }
            return s;
        }
        function userTypeFormatter(value, row, index) {
            var s = '';
            if (value == 130) {
                s = row.level + '<@spring.message "admin.level.member"/>';
            } else if (value == 120) {
            	var oldProxyName = $form.find("[name='proxyName']").val();
                s = row.level + '<@spring.message "admin.proxy.level"/>&nbsp;&nbsp<a class="open-tab" href="${proxyBase}/user/index.do?proxyName='+row.username + '&oldProxyName=' + oldProxyName +'">[' + (row.underNum || 0) + ']</a>';
            }
            if (row.proxyPromoCode) {
                s+= '<br><span class="text text-danger">['+row.proxyPromoCode+']</span >'
            }
            return s;
        }

        function realNameFormatter(value, row, index) {
            var s = "";
            if (row.realName) {
                s = s + row.realName;
            }
            <#if permAdminFn.hadPermission("admin:user:update:realName")>
            s += '&nbsp;&nbsp<a class="open-dialog" href="${proxyBase}/user/showModifyRealName.do?id=' + row.id + '" title="<@spring.message "admin.update.real.name"/>"><i class="fa fa-pencil-alt"></i></a>';
            </#if>
            return s;
        }
        function usernameFormatter(value, row, index) {
            var s = '<a class="open-tab text-danger" data-refresh="true" href="${proxyBase}/user/detail.do?id=' + row.id + '" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew">' + value + '</a>';
            if (row.agentName) {
                s += '<br><span class="text text-success" title="<@spring.message "admin.withdraw.agentUser"/>">' + row.agentName + "</span>"
            }
            return s;
        }
        function degreeFormatter(value, row, index) {
            if (!value) {
                value = '';
            }
            var str = degrees[''+value];
            if(row.lockDegree==2){
				str += '<span class="label label-danger"><@spring.message "admin.deposit.status.be.lock"/></span>';
            }
            <#if permAdminFn.hadPermission("admin:user:update:degree")>
            str += '</br><a class="open-dialog" href="${proxyBase}/user/showModifyDegree.do?id=' + row.id + '" title="<@spring.message "admin.update.member.level"/>"><i class="fa fa-pencil-alt"></i></a>';
            </#if>
            return str;
        }
        function groupFormatter(value, row, index) {
            if (!value) {
                value = '';
            }
            var str = groups[''+value];
            if(!str){str="";}
            <#if permAdminFn.hadPermission("admin:user:update:group")>
            str += '</br><a class="open-dialog" href="${proxyBase}/user/showModifyGroup.do?id=' + row.id + '" title="<@spring.message "admin.update.member.group"/>"><i class="fa fa-pencil-alt"></i></a>';
            </#if>
            return str;
        }
        function remarkFormatter(value, row, index) {
            var content = value;
            if (content && content.length > 6) {
                content = content.substring(0, 6) + "...";
            }
            <#if permAdminFn.hadPermission("admin:user:update:remark")>
            if (!content || !$.trim(value)) {
                content = "<@spring.message "admin.deposit.remark.add"/>";
            }
            return '<a class="open-dialog text-danger" href="${proxyBase}/user/showModifyRemark.do?id=' + row.id + '" title="' + value + '">' + content + '</a>';
            <#else>
            return content;
            </#if>
        }
        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return value;
            }
            value=Math.round(parseFloat(value) * 100) / 100;
            var s = '';
            if (value > 0) {
                s += '<span class="text-danger">';
            } else {
                s += '<span class="text-promary">';
            }
            s +=value+'</span>';
            if (row.moneyStatus == 1) {
                s += '<span class="label label-danger"><@spring.message "admin.have.forze"/></span>';
            }
            <#if permAdminFn.hadPermission("admin:user:freezeMoney")>
            if (row.moneyStatus == 1) {
                s += '<br><a class="todo-ajax" href="${proxyBase}/user/freezeMoney.do?id=' + row.id + "&status=2\" title=\"<@spring.message "admin.sure.concel.vip.value"/>\"><@spring.message "admin.fix.money"/></a></br>";
            } else {
                s += '<br><a class="todo-ajax" href="${proxyBase}/user/freezeMoney.do?id=' + row.id + "&status=1\" title=\"<@spring.message "admin.sure.foze.vip.value"/>\"><@spring.message "admin.forze.money"/></a></br>";
            }
            </#if>
            return s;
        }

        function onlineFormatter(value, row, index) {
            if (value === 2 && row.onlineWarn === 2) {
                return "<a class='label label-danger todo-ajax' href='${proxyBase}/user/changeOnlineWarnStatus.do?id=" + row.id + "&status=1' title=\"<@spring.message "admin.sure.concel.vip.alert"/>\"><@spring.message "admin.line.on"/></a>";
            }
            if (value === 2) {
                return '<a class="label label-success todo-ajax" href="${proxyBase}/user/changeOnlineWarnStatus.do?id=' + row.id + "&status=2\" title=\"<@spring.message "admin.sure.vip.alert.add"/>\"><@spring.message "admin.line.on"/></a>";
            }
            if (row.onlineWarn === 2) {
                return '<a class="label label-warning todo-ajax" href="${proxyBase}/user/changeOnlineWarnStatus.do?id=' + row.id + "&status=1\" title=\"<@spring.message "admin.sure.concel.vip.alert"/>\"><@spring.message "admin.line.off"/></a>";
            }
            return '<a class="label label-default todo-ajax" href="${proxyBase}/user/changeOnlineWarnStatus.do?id=' + row.id + "&status=2\" title=\"<@spring.message "admin.sure.vip.alert.add"/>\"><@spring.message "admin.line.off"/></a>";
        }

        function statusFormatter(value, row, index) {
            <#if permAdminFn.hadPermission("admin:user:update:status")>
            return Fui.statusFormatter({
                val: value,
                url: "${proxyBase}/user/changeStatus.do?id=" + row.id + "&status="
            });
            <#else>
            if (value == 1) {
                return '<span class="label label-danger"><@spring.message "admin.deposit.bank.bankCard.no"/></span>';
            } else {
                return '<span class="label label-success"><@spring.message "admin.deposit.bank.bankCard.yes"/></span>';
            }
            </#if>
        }

        function operateFormatter(value, row, index) {
            var str = "";
            <#if permAdminFn.hadPermission("admin:user:update:contact")>
            str += '<a class="open-dialog" href="${proxyBase}/user/showModifyContact.do?id=' + row.id + '" title="<@spring.message "admin.update.contact"/>"><@spring.message "admin.update.contact"/></a></br>';
            </#if>
            <#if permAdminFn.hadPermission("admin:user:updatePwd")>
            str += '<a class="open-dialog" href="${proxyBase}/user/showModifyPwd.do?id=' + row.id + '""><@spring.message "manager.password.update"/></a></br>';
            </#if>
            <#if permAdminFn.hadPermission("admin:user:updatePayPwd")>
            str += '<a class="open-dialog" href="${proxyBase}/user/showModifyPayPwd.do?id=' + row.id + '""><@spring.message "admin.update.pay.pass"/></a></br>';
            </#if>
            return str;
        }
    });
</script>
