<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <select class="form-control" name="stationId">
                <option value="">所有站点</option>
                <#list stations as s>
                    <option value="${s.id}">${s.name}</option></#list>
            </select>
            <select class="form-control" name="platform">
                <option value="">所有平台</option>
                <#list platforms as s>
                    <option value="${s.value}">${s.title}</option></#list>
            </select>
            <button class="btn btn-primary fui-date-search">查询</button>
        </div>
    </div>
</form>

<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var stations = {}, platforms = {};
        <#list stations as s>stations["${s.id}"] = "${s.name}";
        </#list>
        <#list platforms as s>platforms["${s.value}"] = "${s.title}";
        </#list>
        Fui.addBootstrapTable({
            url: '${managerBase}/thirdPlatform/list.do',
            columns: [{
                field: 'stationId',
                title: '站点',
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    return stations["" + value];
                }
            }, {
                field: 'platform',
                title: '三方平台',
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    return platforms["" + value];
                }
            }, {
                field: 'status',
                title: '状态',
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    return Fui.statusFormatter({
                        val: value,
                        url: "${managerBase}/thirdPlatform/changeStatus.do?id=" + row.id + "&status="
                    });
                }
            }]
        });
    });
</script>
