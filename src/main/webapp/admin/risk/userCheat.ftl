<div class="table-tool">
    <form class="fui-search table-tool" method="post" id="member_manager_form_id">
        <div class="form-group zhongjiang">
            <div class="form-group fui-data-wrap">
                <div class="form-inline">
                    <div class="input-group">
                        <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.username"/>" style="width:150px;">
                    </div>
                    <div class="input-group">
                        <select class="form-control" name="type">
                            <option value=""><@spring.message "admin.all"/></option>
                            <option value="1"><@spring.message "admin.sameIp"/></option>
                            <option value="2"><@spring.message "admin.sameOs"/></option>
                            <option value="3" selected><@spring.message "admin.sameIpAndOs"/></option>
                        </select>
                    </div>
                    <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
                </div>
            </div>
        </div>
    </form>
</div>
<table class="fui-default-table" id="user_manager_datagrid_id"></table>
<script type="text/javascript">
    requirejs(['jquery','bootstrap','Fui'],function(){
        var degrees={},groups={};<#list degrees as le>degrees['${le.id}']='${le.name}';</#list>
        <#list groups as le>groups['${le.id}']='${le.name}';</#list>
        var $form = $("#member_manager_form_id"), $table = $("#user_manager_datagrid_id");
        Fui.addBootstrapTable({
            url: '${adminBase}/userCheat/list.do',
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
            },
            {
                field: 'registerOs',
                title: Admin.loginEqu,
                align: 'center',
                valign: 'middle'
            }
            ,{
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
            }, {
                field: 'onlineStatus',
                title: Admin.onLine,
                width: '50',
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: onlineFormatter
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                width: '80',
                sortable: true,
                valign: 'middle',
                formatter: statusFormatter
            }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                width: '120',
            }, {
                field: 'remark',
                title: Admin.remarkCon,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: remarkFormatter
            }]
        });

        function statusFormatter(value, row, index) {
            <#if permAdminFn.hadPermission("admin:user:update:status")>
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/user/changeStatus.do?id=" + row.id + "&status="
            });
            <#else>
            if (value == 1) {
                return '<span class="label label-danger"><@spring.message "admin.deposit.bank.bankCard.no"/></span>';
            } else {
                return '<span class="label label-success"><@spring.message "admin.deposit.bank.bankCard.yes"/></span>';
            }
            </#if>
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
            return '<a class="open-dialog text-danger" href="${adminBase}/user/showModifyRemark.do?id=' + row.id + '" title="' + value + '">' + content + '</a>';
            <#else>
            return content;
            </#if>
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
            str += '</br><a class="open-dialog" href="${adminBase}/user/showModifyDegree.do?id=' + row.id + '" title="<@spring.message "admin.update.member.level"/>"><i class="fa fa-pencil-alt"></i></a>';
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
            str += '</br><a class="open-dialog" href="${adminBase}/user/showModifyGroup.do?id=' + row.id + '" title="<@spring.message "admin.update.member.group"/>"><i class="fa fa-pencil-alt"></i></a>';
            </#if>
            return str;
        }
        function onlineFormatter(value, row, index) {
            if (value === 2 && row.onlineWarn === 2) {
                return "<a class='label label-danger todo-ajax' href='${adminBase}/user/changeOnlineWarnStatus.do?id=" + row.id + "&status=1' title=\"<@spring.message "admin.sure.concel.vip.alert"/>\"><@spring.message "admin.line.on"/></a>";
            }
            if (value === 2) {
                return '<a class="label label-success todo-ajax" href="${adminBase}/user/changeOnlineWarnStatus.do?id=' + row.id + "&status=2\" title=\"<@spring.message "admin.sure.vip.alert.add"/>\"><@spring.message "admin.line.on"/></a>";
            }
            if (row.onlineWarn === 2) {
                return '<a class="label label-warning todo-ajax" href="${adminBase}/user/changeOnlineWarnStatus.do?id=' + row.id + "&status=1\" title=\"<@spring.message "admin.sure.concel.vip.alert"/>\"><@spring.message "admin.line.off"/></a>";
            }
            return '<a class="label label-default todo-ajax" href="${adminBase}/user/changeOnlineWarnStatus.do?id=' + row.id + "&status=2\" title=\"<@spring.message "admin.sure.vip.alert.add"/>\"><@spring.message "admin.line.off"/></a>";
        }

        function userTypeFormatter(value, row, index) {
            if (value == 120){
                return '<span class="label label-danger">'+Admin.proxy+'</span>'
            }else{
            	 return '<span class="label label-success">'+Admin.member+'</span>';
            }
        }
        function usernameFormatter(value, row, index) {
            var s = '<a class="open-tab text-danger" data-refresh="true" href="${adminBase}/user/detail.do?id=' + row.id + '" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew">' + value + '</a>';
            if (row.agentName) {
                s += '<br><span class="text text-success" title="<@spring.message "admin.withdraw.agentUser"/>">' + row.agentName + "</span>"
            }
            return s;
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
                s += '<br><a class="todo-ajax" href="${adminBase}/user/freezeMoney.do?id=' + row.id + "&status=2\" title=\"<@spring.message "admin.sure.concel.vip.value"/>\"><@spring.message "admin.fix.money"/></a></br>";
            } else {
                s += '<br><a class="todo-ajax" href="${adminBase}/user/freezeMoney.do?id=' + row.id + "&status=1\" title=\"<@spring.message "admin.sure.foze.vip.value"/>\"><@spring.message "admin.forze.money"/></a></br>";
            }
            </#if>
            return s;
        }

        function realNameFormatter(value, row, index) {
            var s = "";
            if (row.realName) {
                s = s + row.realName;
            }
            <#if permAdminFn.hadPermission("admin:user:update:realName")>
            s += '&nbsp;&nbsp<a class="open-dialog" href="${adminBase}/user/showModifyRealName.do?id=' + row.id + '" title="<@spring.message "admin.update.real.name"/>"><i class="fa fa-pencil-alt"></i></a>';
            </#if>
            return s;
        }
        function proxyRecomFormatter(value, row, index){
            var s = '';
            if (row.proxyName) {
                var oldProxyName = $form.find("[name='proxyName']").val();
                s = s + '</br><a class="open-tab" href="${adminBase}/user/index.do?proxyName=' + row.proxyName + '&oldProxyName=' + oldProxyName + '">' + row.proxyName + '</a>';
            }
            <#if permAdminFn.hadPermission("admin:user:update:proxy")>
            if(("${proxyModel<3}"=="true" && row.type==120)||("${proxyModel!=4}"=="true" && row.type!=120)){
                s += '&nbsp;&nbsp<a class="open-dialog" href="${adminBase}/user/showModifyProxy.do?id=' + row.id + '" title="<@spring.message "admin.update.belong.proxy"/>"><i class="fa fa-pencil-alt"></i></a>';
            }
            </#if>
            if(row.recommendUsername){
                s+= '<br><span class="text text-danger">'+row.recommendUsername+'</span >'
            }
            return s;
        }
    });
</script>
