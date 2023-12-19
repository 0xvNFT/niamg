<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <select name="language" class="form-control">
                    <option value=""><@spring.message "admin.language.all.types"/></option>
                    <#list languages as g><option value="${g.name()}">${g.desc }</option></#list>
                </select>
            </div>
            <div class="input-group">
                <select name="code" class="form-control">
                    <option value="1"><@spring.message "admin.front.turn.pic"/></option>
                    <option value="5"><@spring.message "admin.mobile.turn"/></option>
                    <option value="2"><@spring.message "admin.head.pic"/></option>
                    <option value="3"><@spring.message "admin.news.pic"/></option>
                    <option value="4"><@spring.message "admin.gps.pic"/></option>
                    <#if onWebpayGuide><option value="8"><@spring.message "admin.online.pay.pic"/></option>
                    <option value="6"><@spring.message "admin.quickly.pay.pic"/></option>
                    <option value="7"><@spring.message "admin.bank.pay.pic"/></option></#if>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
        <#if permAdminFn.hadPermission("admin:banner:add")>
        	<a class="btn btn-primary open-dialog cached" href="${adminBase}/banner/shoAdd.do"><@spring.message "admin.add"/></a>
        </#if>
        </div>
    </div>
</form>
<table id="agentlunbo_datagrid_tb"></table>
<h3><span class="label label-info"><@spring.message "admin.notice.info"/>!</span></h3>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
var langs={};<#list languages as g>langs["${g.name()}"]="${g.desc}";</#list>
    Fui.addBootstrapTable({
        id: 'agentlunbo_datagrid_tb',
        url: '${adminBase}/banner/list.do',
        columns: [{
            field: 'title',
            title: Admin.turnTitle,
            align: 'center',
            width: '200',
            valign: 'middle'
        },{
        	field: 'language',
            title: Admin.langTypes,
            align: 'center',
            width: '200',
            valign: 'middle'
        }, {
            field: 'code',
            title: Admin.turnTypes,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: articleType
        }, {
            field: 'titleImg',
            title: Admin.turnPicAdd,
            align: 'center',
            width: '180',
            valign: 'bottom',
            formatter: contentFormatter
        }, {
            field: 'titleUrl',
            title: Admin.turnLinkAdd,
            align: 'center',
            width: '180',
            valign: 'bottom',
            formatter: contentFormatter
        }, {
            field: 'status',
            title: Admin.actStatus,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: statusFormatter
        }, {
            field: 'updateTime',
            title: Admin.releaseTime,
            align: 'center',
            width: '300',
            valign: 'middle',
            formatter: Fui.formatDate
        }, {
            field: 'overTime',
            title: Admin.passTime,
            align: 'center',
            width: '300',
            valign: 'middle',
            formatter: Fui.formatDate
        }, {
            title: Admin.op,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });

    //格式化公告类型
    function articleType(value) {
        switch (value) {
            case 1:
                return '<@spring.message "admin.front.turn.pic"/>';
            case 5:
                return "<@spring.message "admin.mobile.turn"/>";
            case 2:
                return '<@spring.message "admin.head.pic"/>';
            case 3:
                return '<@spring.message "admin.news.pic"/>';
            case 4:
                return '<@spring.message "admin.gps.pic"/>';
            case 8:
                return '<@spring.message "admin.online.pay.pic"/>';
            case 6:
                return '<@spring.message "admin.quickly.pay.pic"/>';
            case 7:
                return '<@spring.message "admin.bank.pay.pic"/>';
        }
    }

    function statusFormatter(value, row, index) {
    <#if permAdminFn.hadPermission("admin:banner:modify")>
        return Fui.statusFormatter({
            val: value,
            url: "${adminBase}/banner/changeStatus.do?id=" + row.id + "&status="
        });
    <#else>
        return value == 1 ? "<@spring.message "admin.disabled"/>" : "<@spring.message "admin.enable"/>";
    </#if>
    }

    function operateFormatter(value, row, index) {
        return [
        <#if permAdminFn.hadPermission("admin:banner:modify")>
            '<a class="open-dialog" href="${adminBase}/banner/showModify.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>">',
            '<i class="glyphicon glyphicon-pencil"></i>', '</a>  ',
        </#if>
        <#if permAdminFn.hadPermission("admin:banner:delete")>
            '<a class="todo-ajax" href="${adminBase}/banner/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“', row.title, '”？">',
            '<i class="glyphicon glyphicon-remove"></i>', '</a>'</#if>]
                .join('');
    }

    function contentFormatter(value, row, index) {
        if (value == null || value == "") {
            return '';
        }
        if (value.length > 15) {
            return value.substr(0, 15) + '...';
        } else {
            return value;
        }
    }
});
</script>