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
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.username"/>" style="width:120px;">
            </div>
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
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/userDegreeLog/list.do',
            columns: [{
                field: 'username',
                title: Admin.username,
                align: 'center',
                valign: 'middle',
                formatter : accountFormatter
            }, {
                field: 'content',
                title: Admin.changeContent,
                align: 'center',
                valign: 'middle',
                formatter : remarkFormatter
            }, {
                field: 'oldName',
                title: Admin.beforeDegree,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'newName',
                title: Admin.afterDegree,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'operator',
                title: Admin.operator,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }]
        });

        function accountFormatter(value, row, index) {
        <#if permAdminFn.hadPermission("admin:user")>
            return '<a class="open-dialog" href="${adminBase}/user/detail.do?id='+row.userId+'"><span class="text-danger">'+value+'</span></a>';
        <#else>
            return value;
        </#if>
        }

        function remarkFormatter(value, row, index) {
            var content = value||'';
            if (content && content.length < 6) {
               	return content;
            }
            return '<a class="open-text" dialog-text="'+content+'" dialog-title="'+Admin.changeDetail+'" title="'+content+'">'+content.substring(0, 6)+'...</span>';
        }
    });
</script>
