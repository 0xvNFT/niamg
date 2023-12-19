<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<input name="ip" class="form-control" type="text" placeholder="<@spring.message "admin.ip"/>">
			<div class="input-group">
                <select name="type" class="form-control">
                    <option value=""><@spring.message "manager.all.types"/></option>
                    <option value="2"><@spring.message "manager.white.ip"/></option>
                    <option value="1"><@spring.message "manager.black.ip"/></option>
                </select>
            </div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
			<a class="btn btn-primary open-dialog" href="${adminBase}/adminWhiteIp/showAdd.do"><@spring.message "admin.add"/></a>
		</div>
	</div>
</form>

<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : '${adminBase}/adminWhiteIp/list.do',
		columns : [ { 
			field : 'ip',
			title : 'IP',
			align : 'center',
			valign : 'middle'
		},{
            field: 'type',
            title: Admin.type,
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index) {
            	if(value==2){
            		return '<span class="label label-success"><@spring.message "manager.white.ip"/></span>';
            	}
            	return '<span class="label label-default"><@spring.message "manager.black.ip"/></span>';
            }
		}, {
            field: 'remark',
            title: Admin.remark,
            align: 'center',
            valign: 'middle',
            formatter: contentFormatter
        },{
			field : 'createDatetime',
			title : Admin.createTime,
			align : 'center',
			valign : 'middle',
			formatter :  Fui.formatDatetime
		},{
			title : Admin.operating,
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});
	function operateFormatter(value, row, index) {
	    return '<a class="todo-ajax" href="${adminBase}/adminWhiteIp/delete.do?id='+row.id+'" title="'+Admin.delConfirm.replace("{0}",row.ip)+'">'+Admin.delete+'</a>';
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
