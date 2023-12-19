<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
    	<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control fui-date" name="begin" format="YYYY-MM-DD HH:mm:ss" value="${cusDate} 00:00:00" placeholder="<@spring.message "admin.srartTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
	        <input class="form-control" name="username" type="text" placeholder="<@spring.message "admin.username"/>">
	        <input class="form-control" name="loginIp" type="text" placeholder="<@spring.message "admin.ip"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="end" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${cusDate} 23:59:59" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <select class="form-control" name="type">
                <option value=""><@spring.message "admin.opLogType"/></option>
                <#list types as t><option value="${t.type}">${t.desc}</option></#list>
            </select>
            <input class="form-control" name="content" type="text" placeholder="<@spring.message "admin.opLogContent"/>">
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var typeMap = {};
        <#list types as t>typeMap["${t.type}"] = "${t.desc}";</#list>
        Fui.addBootstrapTable({
            url: '${adminBase}/oplog/list.do',
            columns: [{
				field: 'username',
                title: Admin.username,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                field: 'ip',
                title: 'IP',
                align: 'center',
                valign: 'middle'
            }, {
                field: 'ipStr',
                title: Admin.ipAddress,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'type',
                title: Admin.opLogType,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value) {
                        return typeMap[value + ''];
                    }
                    return "";
                }
            }, {
                field: 'content',
                title: Admin.opLogContent,
                align: 'center',
                valign: 'middle',
                formatter: contentFormatter
            }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });

        function contentFormatter(value, row, index) {
            value = value.replace(/[<>&"]/g, function (c) {
                return {'<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;'}[c];
            });
            if (!value) {
                return "-";
            }
            if (value.length > 20) {
                return '<a href="javascript:void(0)" class="open-text" dialog-text="' + value + '">' + value.substr(0, 20) + '...</a>';
            } else {
                return value;
            }
        }

        function operateFormatter(value, row, index) {
            return '<a class="open-dialog" href="${adminBase}/oplog/detail.do?id=' + row.id + '&createTime=' + row.createDatetime + '">'+Admin.detail+'</a>';
        }
    });
</script>
