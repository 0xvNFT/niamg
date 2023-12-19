<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" name="begin" format="YYYY-MM-DD HH:mm:ss"
                       placeholder="<@spring.message "manager.begin.date"/>" value="${cusDate} 00:00:00" autocomplete="off"> <span
                        class="glyphicon glyphicon-th form-control-feedback"
                        aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "manager.before.day"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "manager.before.month"/></button>
            <select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select>
            <input class="form-control" name="username" type="text" placeholder="<@spring.message "manager.username.input"/>">
            <input class="form-control" name="ip" type="text" placeholder="IP">
            <button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="end" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss"
                       placeholder="<@spring.message "manager.end.date"/>" value="${cusDate} 23:59:59" autocomplete="off"> <span
                        class="glyphicon glyphicon-th form-control-feedback"
                        aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "manager.last.day"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "manager.after.month"/></button>
            <select class="form-control" name="userType">
                <option value=""><@spring.message "manager.user.types"/></option>
                <#list logTypes as t>
                    <option value="${t.type}">${t.desc}</option></#list>
            </select>
            <select class="form-control" name="type">
                <option value=""><@spring.message "manager.all.oper.type"/></option>
                <#list types as t><option value="${t.type}">${t.desc}</option></#list>
            </select>
            <input class="form-control" name="content" type="text" placeholder="<@spring.message "manager.oper.rec"/>">
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var typeMap = {}, logTypeMap = {},stations={},partners={};
        <#list stations as s>stations["${s.id}"]="${s.name}";</#list>
        <#list types as t>typeMap["${t.type}"] = "${t.desc}";</#list>
        <#list logTypes as t>logTypeMap["${t.type}"] = "${t.desc}";</#list>
        <#list partners as p>partners["${p.id}"]="${p.name}";</#list>
        Fui.addBootstrapTable({
            url: '${managerBase}/log/list.do',
            columns: [{
				field : 'partnerId',
				title : '<@spring.message "manager.coper"/>',
				align : 'center',
				valign : 'middle',
				formatter:function(value,row,index){
					if(value){
						return partners[value+''];
					}
					return "-";
				}
			},{
				field : 'stationId',
				title : '<@spring.message "manager.station.point"/>',
				align : 'center',
				valign : 'middle',
				formatter:function(value,row,index){
					if(value){
						return stations[value+''];
					}
					return "-";
				}
			},{
				field: 'username',
                title: '<@spring.message "manager.account.number"/>',
                align: 'center',
                valign: 'middle'
            }, {
                field: 'createDatetime',
                title: '<@spring.message "manager.datetime.create"/>',
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
                title: '<@spring.message "manager.address"/>',
                align: 'center',
                valign: 'middle'
            }, {
                field: 'userType',
                title: '<@spring.message "manager.user.types"/>',
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value) {
                        return logTypeMap[value + ''];
                    }
                    return "";
                }
            }, {
                field: 'type',
                title: '<@spring.message "manager.oper.type"/>',
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
                title: '<@spring.message "manager.oper.rec"/>',
                align: 'center',
                valign: 'middle',
                formatter: contentFormatter
            }, {
                title: '<@spring.message "admin.withdraw.table.lockFlag"/>',
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
            return '<a class="open-dialog" href="${managerBase}/log/detail.do?id=' + row.id + '&createTime=' + row.createDatetime + '"><@spring.message "admin.deposit.bank.bankCard.detail"/></a>';
        }
    });
</script>
