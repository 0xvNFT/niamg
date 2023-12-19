<div class="table-tool">
    <div class="form-group messageTs">
    	<span class="glyphicon glyphicon-info-sign"><@spring.message "admin.wtips"/>：</span>
    	<#if defaultDegree.type == 1>
			<span class="text-danger"><@spring.message "admin.degree.by.deposit"/></span>
		</#if>
		<#if defaultDegree.type == 2>
			<span class="text-danger"><@spring.message "admin.degree.by.betNum"/></span>
		</#if>
		<#if defaultDegree.type == 3>
			<span class="text-danger"><@spring.message "admin.degree.by.both"/></span>
		</#if>
		<button class="btn btn-primary open-dialog" type="button" url="${adminBase}/userDegree/changeType.do"><@spring.message "admin.degree.changeType"/></button>
        <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/userDegree/showAdd.do"><@spring.message "admin.add"/></button>
        <button class="btn btn-primary todo-ajax" type="button" url="${adminBase}/userDegree/reStat.do"><@spring.message "admin.degree.reStat"/></button>
        <button class="btn btn-danger open-dialog" type="button" url="${adminBase}/userDegree/showModifyUpgrade.do"><@spring.message "admin.degree.upgrade1"/></button>
    </div>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: "${adminBase}/userDegree/list.do",
            columns: [{
                field: 'name',
                title: Admin.degreeName,
                align: 'center',
                valign: 'middle'
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
                field: 'depositMoney',
                title: Admin.depositMoney,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field : 'betNum',
                title : Admin.betNum,
                align : 'center',
                valign : 'middle',
                formatter : moneyFormatter
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
                field: 'upgradeMoney',
                title: Admin.upgradeMoney,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'skipMoney',
                title: Admin.skipMoney,
                align: 'center',
                valign: 'middle'
             }, {
                field: 'betRate',
                title: Admin.betRate,
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

        function memCountFormatter(value, row, index) {
            if (value && value > 0) {
                return '<a class="open-tab" href="${adminBase}/user/index.do?degreeId=' + row.id + '" id="fui_tab_49" title="<@spring.message "admin.menu.user"/>">' + value + '</a>';
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
            if (row.original == 2) {
                return Admin.enable;
            }
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/userDegree/updStatus.do?id=" + row.id + "&status="
            });
        }

        function operateFormatter(value, row, index) {
            var s = '<a class="open-dialog" href="${adminBase}/userDegree/showModify.do?id=' + row.id + '">'+Admin.update+'</a>&nbsp;&nbsp;'
                + '<a class="open-dialog" href="${adminBase}/userDegree/showTransfer.do?id=' + row.id + '">'+Admin.changeUserDegree+'</a>&nbsp;&nbsp;';
            if (row.original != 2) {
                s = s + '<a class="todo-ajax" href="${adminBase}/userDegree/delete.do?id=' + row.id + '" title="' +Admin.delConfirm.replace('{0}', row.name) + '”？">'+Admin.delete+'</a>';
            }
            return s;
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
