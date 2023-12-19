<form class="fui-search table-tool" method="post" id="member_guest_manager_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${startTime}"
                       name="startDate" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "manager.find.account.one"/>" style="width:150px;">
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:accGuest:add")>
                <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/accGuest/addGuest.do"><@spring.message "admin.accGuest.add"/></button>
                <#--<button class="btn btn-primary open-dialog" type="button" url="${adminBase}/accGuest/batchAddGuest.do"><@spring.message "admin.accGuest.addBatch"/></button>-->
            </#if>

            <#--<button class="btn btn-primary open-dialog cached" type="button" url="${adminBase}/accGuest/batchChangeGuestStatusPage.do"><@spring.message "admin.accGuest.modifyStatusBatch"/></button>-->
            <#if permAdminFn.hadPermission("admin:accGuest:modify:sensitive")>
                <button class="btn btn-danger resetGuestMoney"><@spring.message "admin.accGuest.resetBal"/></button>
            </#if>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="endDate" value="${endTime}" format="YYYY-MM-DD HH:mm:ss"
                       class="form-control fui-date" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true">
                </span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
        </div>
    </div>
</form>

<table class="fui-default-table" id="guest_manager_datagrid_id"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#member_guest_manager_form_id"), $table = $("#guest_manager_datagrid_id");
        <#if permAdminFn.hadPermission("admin:accGuest:modify:sensitive")>
        $form.find(".resetGuestMoney").click(function (e) {
            e.preventDefault()
            let users = '';
            $table.find("tbody input:checkbox:checked").each(function (i, j) {
                j = $(j);
                if (j.val() === 'on') {
                    return;
                }
                users += j.val() + ",";
            });
            if (!users) {
                layer.msg('<@spring.message "admin.select.user.who"/>');
                return;
            }
            layer.open({
                title: '<@spring.message "admin.accGuest.resetBal"/>',
                content: '<input type="number" class="form-control" id="resetGuestMoneyInputId" placeholder="<@spring.message "admin.cash.money.paper"/>">',
                btn: ['<@spring.message "manager.resetting"/>'],
                yes: function (index, layero) {
                    $.ajax({
                        url: '${adminBase}/accGuest/resetGuestMoney.do',
                        data: {'ids': users, 'money': $("#resetGuestMoneyInputId").val()},
                        success: function (dt) {
                            $table.find('.check_all').prop("checked", false);
                            layer.closeAll();
                            layer.msg(dt.msg);
                            $table.bootstrapTable('refresh');
                        }
                    });
                }
            });
        });
        </#if>

        var degrees={},groups={};
        <#list degrees as le>degrees['${le.id}']='${le.name}';</#list>
        <#list groups as le>groups['${le.id}']='${le.name}';</#list>

        Fui.addBootstrapTable({
            url: '${adminBase}/accGuest/list.do',
            columns: [{
                field: 'checkbox',
                title: '<input type="checkbox" class="check_all">',
                align: 'center',
                vilign: 'middle',
                formatter: operateCheckboxMatter
            }, {
                field: 'username',
                title: Admin.userName,
                align: 'center',
                valign: 'middle',
                formatter: accountFormatter
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
            }, {
                field: 'money',
                title: Admin.accountVal,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'createDatetime',
                title: Admin.registerTime,
                align: 'center',
                valign: 'middle',
                formatter : Fui.formatDatetime
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                width: '80',
                valign: 'middle',
                formatter: statusFormatter
            }, {
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                width: '120',
                formatter: operateFormatter
            }]
        });

        $table.on("click", "input.check_all", function () {
            var $this = $(this), isChecked = $this.prop('checked');
            $table.find('tbody input:checkbox').prop('checked', isChecked);
        });

        function operateCheckboxMatter(value, row, index) {
            const dom = ['input', {
                props: {
                    type: "checkbox",
                    value: row.id,
                    "data-name": row.username
                }
            }]
            return createDynamicsDomOuterHTML(dom);
        }

        function degreeFormatter(value, row, index) {
            if(row.id==-100){
                return "";
            }
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
            if(row.id==-100){
                return "";
            }
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

        function operateFormatter(value, row, index) {
            let domList = []
            domList.push(['span', {test: ""}])
            <#if permAdminFn.hadPermission("admin:accGuest:modify")>
            domList.push(['a', {
                classes: ["open-dialog"],
                props: {
                    href: `${adminBase}/accGuest/pwdModify.do?id=` + row.id,
                    title: "<@spring.message "admin.modify.pwd"/>"
                },
                text: "<@spring.message "admin.modify.pwd"/>"
            }])
            </#if>
            return domList.map(dom => createDynamicsDomOuterHTML(dom)).join("");
        }

        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/accGuest/changeStatus.do?userId=" + row.id + "&status="
            });
        }

        function accountFormatter(value, row, index) {
            let domList = []
            domList.push(
                ['span', {
                    text: value
                }],
                ['br', {}]
            )
            if (row.realName) {
                domList.push(['span', {text: row.realName}])
            }
            return domList.map(dom => createDynamicsDomOuterHTML(dom)).join("");
        }

        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return value;
            }
            var f = parseFloat(value);
            f = Math.round(f * 100) / 100;
            if (value > 0) {
                const dom = ['span', {
                    classes: ["text-danger"],
                    text: f
                }]
                return createDynamicsDomOuterHTML(dom);
            }
            const dom = ['span', {
                classes: ["text-primary"],
                text: f
            }]
            return createDynamicsDomOuterHTML(dom);
        }

    });

</script>
