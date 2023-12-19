<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
        	<div class="input-group"><select name="mode" class="form-control">
        		<option value=""><@spring.message "admin.extend.types"/></option>
	            <#if proxyModel!=4><option value="1"><@spring.message "admin.proxy.extend"/></option></#if>
	            <#if memberRecommend><option value="2"><@spring.message "admin.member.first"/></option></#if>
	        </select></div>
	        <div class="input-group"><select name="type" class="form-control">
        		<option value=""><@spring.message "manager.all.types"/></option>
	            <#if proxyModel==1 || proxyModel==2><option value="120"><@spring.message "admin.withdraw.type.proxy"/></option></#if>
	            <#if proxyModel!=1><option value="130"><@spring.message "admin.withdraw.type.member"/></option></#if>
	        </select></div>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.user.account.detail"/>" style="width:150px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="code" placeholder="<@spring.message "admin.code.extend"/>" style="width:150px;">
            </div>
            <button class="btn btn-primary"><@spring.message "admin.deposit.button.search"/></button>
            <#if permAdminFn.hadPermission("admin:promotion:add")>
            <#if proxyModel!=4><button class="btn btn-primary open-dialog" url="${adminBase}/promotion/showAddProxy.do"><@spring.message "admin.add.proxy.extend.link"/></button></#if>
            <#if memberRecommend><button class="btn btn-primary open-dialog" url="${adminBase}/promotion/showAddMember.do"><@spring.message "admin.add.member.first"/></button></#if>
            </#if>
            <#if permAdminFn.hadPermission("admin:promotion:modify")>
            <button class="btn btn-primary open-dialog" url="${adminBase}/promotion/showModifyAccessPage.do"><@spring.message "admin.one.config.extend.page"/></button>
            </#if>
        </div>
    </div>
</form>
<div style="padding: 10px;">
    <div style="color:red;"><@spring.message "admin.query.where.regist.vip"/></div>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/promotion/list.do',
            columns: [{
                field: 'username',
                title: Admin.belongVip,
                align: 'center',
                valign: 'middle',
                formatter: usernameFormatter
            },{
                field: 'mode',
                title: Admin.extendType,
                align: 'center',
                valign: 'middle',
                formatter: modeFormatter
            },{
                field: 'type',
                title: Admin.type,
                align: 'center',
                valign: 'middle',
                formatter: typeFormatter
            },{
                title: Admin.proxyPoint,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                	var m=(row.mode==2),s='';
                    <#if game.live==2>s += '<@spring.message "manager.live"/>:' + rebateFormatter(m?${rebate.live}:row.live) + '‰';</#if>
                    <#if game.egame==2>s += '<br><@spring.message "manager.egame"/>:' + rebateFormatter(m?${rebate.egame}:row.egame) + '‰';</#if>
                    <#if game.sport==2>s += '<br><@spring.message "manager.sport"/>:' + rebateFormatter(m?${rebate.sport}:row.sport) + '‰';</#if>
                    <#if game.chess==2>s += '<br><@spring.message "manager.chess"/>:' + rebateFormatter(m?${rebate.chess}:row.chess) + '‰';</#if>
                    <#if game.esport==2>s += '<br><@spring.message "manager.esport"/>:' + rebateFormatter(m?${rebate.esport}:row.esport) + '‰';</#if>
                    <#if game.fishing==2>s += '<br><@spring.message "manager.fish"/>' + rebateFormatter(m?${rebate.fishing}:row.fishing) + '‰';</#if>
                    <#if game.lottery==2>s += '<br><@spring.message "manager.lottery"/>:' + rebateFormatter(m?${rebate.lottery}:row.lottery) + "‰";</#if>
                    return s;
                }
            },{
                field: 'linkUrl',
                title: Admin.visitAccess,
                align: 'center',
                valign: 'middle',
                formatter: urlFormatter
            },{
                field: 'accessPage',
                title: Admin.extendPage,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value === 2) {
                        return '<span class="label label-success"><@spring.message "admin.main.page"/></span>'
                    } else if (value === 3) {
                        return '<span class="label label-warning"><@spring.message "admin.discount.activit"/></span>'
                    } else {
                        return '<span class="label label-primary"><@spring.message "admin.regist.page"/></span>';
                    }
                }
            },{
                field: 'code',
                title: Admin.extendCode,
                align: 'center',
                valign: 'middle'
            },{
                field: 'registerNum',
                title: Admin.registerPer,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                <#if permAdminFn.hadPermission("admin:user")>
                     return "<a class='open-tab' href='${adminBase}/user/index.do?proxyPromoCode=" + row.code + "' title='<@spring.message "admin.member.manage"/>'>" + value + "</a>";
                <#else>
                     return value;
                </#if>
                }
            },{
                field: 'accessNum',
                title: Admin.visitPerson,
                align: 'center',
                valign: 'middle'
            },{
                field: 'createTime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            },{
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });
        function urlFormatter(value, row, index) {
            return '<@spring.message "admin.common.link"/>:' + value + '<br/><@spring.message "admin.asc.link"/>:' + row.linkUrlEn;
        }

        function usernameFormatter(value, row, index) {
           <#if permAdminFn.hadPermission("admin:user")>
            return '<a class="open-tab text-danger" data-refresh="true" href="${adminBase}/user/detail.do?id=' + row.userId + '" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew">' + value + '</a>';
           <#else>
            return value;
           </#if>
        }
		function modeFormatter(value, row, index) {
            if (value == 1) {
                return '<span class="label label-success"><@spring.message "admin.proxy.extend"/></span>';
            } else{
                return '<span class="label label-primary"><@spring.message "admin.member.first"/></span>'
            }
        }
        function typeFormatter(value, row, index) {
            if (value == 130) {
                return '<span class="label label-primary"><@spring.message "admin.withdraw.info.member"/></span>';
            } else if(value == 120) {
                return '<span class="label label-success"><@spring.message "admin.withdraw.type.proxy"/></span>'
            }
        }

        function operateFormatter(value, row, index) {
            var s = "";
            <#if permAdminFn.hadPermission("admin:promotion:delete")>
                s = '<a class="todo-ajax" href="${adminBase}/promotion/delete.do?id=' + row.id + '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>？"><i class="glyphicon glyphicon-remove"></i></a>';
            </#if>
            <#if permAdminFn.hadPermission("admin:promotion:modify")>
                s = '<a class="open-dialog" href="${adminBase}/promotion/showModify.do?id=' + row.id + '"><i class="glyphicon glyphicon-pencil"></i></a>';
            </#if>
            return s;
        }

        function rebateFormatter(value) {
            if (!value) {
                return 0;
            } else {
                return value;
            }
        }
    });
</script>
