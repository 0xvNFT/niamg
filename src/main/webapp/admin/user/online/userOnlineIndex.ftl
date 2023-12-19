<form class="fui-search table-tool" method="post" id="member_online_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.deposit.handle.username"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="proxyName" placeholder="<@spring.message "admin.proxyUsername"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="agentUser" placeholder="<@spring.message "admin.merchant.account"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="realName" placeholder="<@spring.message "admin.deposit.table.realName"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="lastLoginIp" placeholder="<@spring.message "admin.last.logout.ip"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <@spring.message "admin.deposit.money.range"/>:
                <div class="input-group">
                    <input type="text" style="width:105px;" class="form-control" name="minMoney" value="${minMoney}"
                           placeholder="<@spring.message "admin.deposit.money.min"/>">
                </div>
                <@spring.message "admin.until"/>
                <div class="input-group">
                    <input type="text" style="width:105px;" class="form-control" name="maxMoney" value="${maxMoney}"
                           placeholder="<@spring.message "admin.deposit.money.max"/>">
                </div>
            </div>
            <div class="input-group">
                <select class="form-control" name="warn">
                    <option value=""><@spring.message "admin.deposit.type.all"/></option>
                    <option value="1"><@spring.message "admin.normal"/></option>
                    <option value="2" <#if warn==2>selected</#if>><@spring.message "admin.alert.notice"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="terminal">
                    <option value=""><@spring.message "admin.login.equ"/></option>
                    <option value="1">PC</option>
                    <option value="2"><@spring.message "admin.mobile.web"/></option>
                    <option value="3"><@spring.message "admin.android.app"/></option>
                    <option value="4"><@spring.message "admin.apple.app"/></option>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:user:offline")>
                <a class="btn btn-primary offlineBtn" href="#" title="<@spring.message "admin.sure.vip.offline"/>？"><@spring.message "admin.condition.offline"/></a>
            </#if>
        </div>
    </div>
</form>
<div style="padding: 10px;">
    <div style="color:red;"><@spring.message "admin.query.where.regist.vip"/></div>
</div>
<table class="fui-default-table" id="member_online_griddata_id"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#member_online_form_id");
        $form.find(".offlineBtn").click(function () {
            layer.confirm('<@spring.message "admin.sure.online.vip.offline"/>?', function(index){
                $.ajax({
                    url : "${adminBase}/user/online/batchOffline.do",
                    data : $form.serialize(),
                    success : function(data) {
                        layer.closeAll();
                        layer.msg("<@spring.message "admin.operate.succ"/>！");
                        $("#member_online_griddata_id").bootstrapTable('refresh');
                    },
                    error:function(data){
                        layer.msg("<@spring.message "admin.operate.fail"/>！"+data.msg);
                    }
                });
            });
        });
        Fui.addBootstrapTable({
            url: '${adminBase}/user/online/list.do',
            showPageSummary: true,
            showAllSummary: true,
            showFooter: true,
            columns: [{
                field: 'username',
                title: Admin.memberAcc,
                align: 'center',
                valign: 'middle',
                formatter: usernameFormatter
            },{
                field: 'proxyName',
                title: Admin.belongProxyLevel,
                align: 'center',
                valign: 'middle',
                pageSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.page.sum"/>:";
                },
                allSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.all.sum"/>:";
                }
            }, {
                field: 'money',
                title: Admin.accountVal,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        var r = rows[i];
                        if (!r["money"]) {
                            continue;
                        }
                        total += r["money"];
                    }
                    return total.toFixed(2) + "";
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00";
                    }
                    return (aggsData.totalMoney ? aggsData.totalMoney.toFixed(2) : "0.00") ;
                }
            }, {
                field: 'realName',
                title: Admin.vipName,
                align: 'center',
                valign: 'middle'
            },{
                field: 'remark',
                title: Admin.vipRemark,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value) {
                        if (value.length > 6) {
                            return '<a class="open-text" dialog-text="' + value + '" dialog-title="<@spring.message "admin.view.vip.remark.info"/>" title="' + value + '">'+value.substr(0, 6) + '...</span>';
                        }
                        return value;
                    }
                    return '';
                }
            }, {
                field: 'terminal',
                title: Admin.loginEqu,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter:function(value,row,inde){
                		if(!value){
                		return "";
                		}
                		switch(value){
                			case 1:
		                    return '<span class="label label-primary">PC</span>';
		                case 2:
		                    return '<span class="label label-success"><@spring.message "admin.mobile.web"/></span>';
		                case 3:
		                    return '<span class="label label-danger"><@spring.message "admin.android.app"/></span>';
                			case 4:
		                    return '<span class="label label-warning"><@spring.message "admin.apple.app"/></span>';
                		}
                		return "-";
                }
            }, {
                field: 'createDatetime',
                title: Admin.registerTime,
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                width: '80',
                sortable: true,
                valign: 'middle',
                formatter: statusFormatter
            }, {
                field : 'domain',
                title : Admin.domain,
                align : 'center',
                valign : 'middle'
            }, {
                field: 'lastLoginIp',
                title: Admin.lastLoginIp,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'lastLoginIpAddress',
                title: Admin.lastLoginIpAdd,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'onlineWarn',
                title: Admin.alertStatus,
                align: 'center',
                valign: 'middle',
                formatter:onlineFormatter
           <#if permAdminFn.hadPermission("admin:user:offline")> }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                width: '120',
                formatter: function(value, row, index) {
					return '<a class="todo-ajax" href="${adminBase}/user/online/offline.do?userId=' + row.id + '" title="<@spring.message "admin.is.not.offline"/>"><@spring.message "admin.offLine"/></a> &nbsp; ';
          		}</#if>
            }]
        });

        function usernameFormatter(value, row, index) {
           <#if permAdminFn.hadPermission("admin:user")>
            return '<a class="open-tab text-danger" data-refresh="true" href="${adminBase}/user/detail.do?id=' + row.id + '" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew">' + value + '</a>';
           <#else>
            return value;
           </#if>
        }

        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return value;
            }
            var f = parseFloat(value);
            f = Math.round(f * 100) / 100;
            if (value > 0) {
                return '<span class="text-danger">'+f+'</span>';
            }
            return '<span class="text-primary">'+f+'</span>';
        }

        function statusFormatter(value, row, index) {
            if (value == 1 || value == '1') {
                return "<@spring.message "admin.deposit.bank.bankCard.no"/>";
            } else {
                return "<@spring.message "admin.deposit.bank.bankCard.yes"/>"
            }
        }

        function onlineFormatter(value, row, index) {
        	<#if permAdminFn.hadPermission("admin:user:update:status")>
            if (row.onlineWarn === 2 && row.betting) {
                return '<a class="text-muted label label-danger todo-ajax" href="${adminBase}/user/online/changeOnlineWarnStatus.do?id=' + row.id + '&status=1" title="<@spring.message "admin.sure.concel.vip.alert"/>？"><@spring.message "admin.pushing.one"/></a>';
            }
            if (row.onlineWarn === 2) {
                return '<a class="text-muted label label-warning todo-ajax" href="${adminBase}/user/online/changeOnlineWarnStatus.do?id=' + row.id + '&status=1" title="<@spring.message "admin.sure.concel.vip.alert"/>？"><@spring.message "admin.online.alert"/></a>';
            }
            return '<a class="text-muted label label-success todo-ajax" href="${adminBase}/user/online/changeOnlineWarnStatus.do?id=' + row.id + '&status=2" title="<@spring.message "admin.sure.vip.alert.add"/>？"><@spring.message "admin.normal"/></a>';
            <#else>return "";</#if>
        }
    });
</script>
