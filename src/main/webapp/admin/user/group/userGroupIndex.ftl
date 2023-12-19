<div class="table-tool">
    <div class="form-group messageTs">
        <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/userGroup/showAdd.do"><@spring.message "admin.add"/></button>
        <button class="btn btn-primary todo-ajax" type="button" url="${adminBase}/userGroup/reStat.do"><@spring.message "admin.degree.reStat"/></button>
    </div>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: "${adminBase}/userGroup/list.do",
            columns: [{
                field: 'name',
                title: Admin.userGroupName,
                align: 'center',
                valign: 'middle',
                formatter: groupNameFormatter
            }, {
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }, {
                field: 'remark',
                title: Admin.remark,
                align: 'center',
                valign: 'middle',
                formatter: remarkFormatter
            }, {
                field: 'memberCount',
                title: Admin.memberCount,
                align: 'center',
                valign: 'middle',
                formatter: memCountFormatter
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter: statusFormatter
            }, {
                field: 'dailyDrawNum',
                title: Admin.dailyDrawNum,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field : 'maxDrawMoney',
                title : Admin.maxDrawMoney,
                align : 'center',
                valign : 'middle',
                formatter : moneyFormatter
            
            }, {
                field: 'minDrawMoney',
                title: Admin.minDrawMoney,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }]
        });

        function groupNameFormatter(value, row, index) {
            const defaultGroups = ["The default group", "VIP", "Grupo padrão", "Nhóm mặc định", "กลุ่มเริ่มต้น", "默认组别", "Grupo predeterminado", "डिफ़ॉल्ट समूह", "デフォルトグループ", "Kumpulan Lalai"];

            if (defaultGroups.includes(value)) {
                return "<@spring.message 'admin.stationUserGroupDefault'/>";
            } else {
                const numericValue = value.replace(/\D/g, '');
                return value === "普通" ? "<@spring.message 'admin.groupNormal'/>" : "<@spring.message 'manager.group.types'/> " + numericValue;
            }
        }

        function memCountFormatter(value, row, index) {
            if (value && value > 0) {
                return '<a class="open-tab" href="${adminBase}/user/index.do?groupId=' + row.id + '" id="fui_tab_49" title="<@spring.message "admin.menu.user"/>">' + value + '</a>';
            }
            return value;
        }

        function moneyFormatter(value, row, index) {
            if (value && value > 0) {
                return '<span class="text-danger">' + value + '</span>';
            }
            return value;
        }

        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/userGroup/updStatus.do?id=" + row.id + "&status="
            });
        }

        function operateFormatter(value, row, index) {
            return '<a class="open-dialog" href="${adminBase}/userGroup/showModify.do?id=' + row.id + '">'+Admin.update+'</a>&nbsp;&nbsp;'
               + '<a class="todo-ajax" href="${adminBase}/userGroup/delete.do?id=' + row.id + '" title="' +Admin.delConfirm.replace('{0}', row.name) + '”？">'+Admin.delete+'</a>';
        }

        function remarkFormatter(value, row, index) {
            if (!value) {
                return "";
            }
            if (value.length < 6) {
                return value;
            }
            return ['<a class="open-text" dialog-text="' + value + '" dialog-title="'+Admin.viewDetail+'" title="' + value + '">', '</span>'].join(value.substr(0, 6) + "...");
        }
    });
</script>
