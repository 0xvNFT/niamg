<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select>
			<div class="input-group">
                <select name="type" class="form-control">
                    <option value=""><@spring.message "manager.all.types"/></option>
                    <option value="2"><@spring.message "manager.white.ip"/></option>
                    <option value="1"><@spring.message "manager.black.ip"/></option>
                </select>
            </div>
			<input name="ip" class="form-control" type="text" placeholder="IP">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/adminWhiteIp/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>

<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations={};<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/adminWhiteIp/list.do',
		columns : [ { 
			field : 'ip',
			title : 'IP',
			align : 'center',
			valign : 'middle'
		},{
            field: 'type',
            title: "<@spring.message "admin.deposit.table.depositType"/>",
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index) {
            	if(value==2){
            		return '<span class="label label-success"><@spring.message "manager.white.ip"/></span>';
            	}
            	return '<span class="label label-default"><@spring.message "manager.black.ip"/></span>';
            }
		},{
			field : 'stationId',
			title : '<@spring.message "manager.station.point"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return stations[""+value];
			}
		}, {
            field: 'remark',
            title: '<@spring.message "admin.withdraw.remark"/>',
            align: 'center',
            valign: 'middle',
            formatter: contentFormatter
        },{
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter :  Fui.formatDatetime
		},{
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});
	function operateFormatter(value, row, index) {
	    return '<a class="todo-ajax" href="${managerBase}/adminWhiteIp/delete.do?id='+row.id+'" title="<@spring.message "manager.sure.del.ip"/>【'+row.ip+'】"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';
	}
	function contentFormatter(value, row, index) {
        if (!value) {
            return "-";
        }
        value = value.replace(/[<>&"]/g, function (c) {
            return {'<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;'}[c];
        });
        if (value.length > 20) {
            return '<a href="javascript:void(0)" class="open-text" dialog-text="' + value + '">' + value.substr(0, 20) + '...</a>';
        } else {
            return value;
        }
    }
});
</script>
