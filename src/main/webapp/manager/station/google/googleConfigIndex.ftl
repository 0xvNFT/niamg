<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" name="stationId"><option value="">所有站点</option>
			<#list stations as s><option value="${s.id}">${s.name}[${s.code}]</option></#list>
			</select>
			<button class="btn btn-primary fui-date-search">查询</button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/googleAuthConfig/showEscape.do?stationId={select[name='stationId']:}">过滤管理员</a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	var stations=[];
<#list stations as s>stations["${s.id}"]="${s.name}[${s.code}]";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/googleAuthConfig/list.do',
		columns : [ { 
			field : 'stationId',
            title : '站点名称',
            align : 'center',
            valign : 'middle',
            formatter : function(value,row,index){
                return stations[value+''];
            }
		}, {
			field : 'adminUsername',
			title : '管理员',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value){
					return value;
				}
                return "通用";
            }
         }, {
			field : 'status',
			title : '状态',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value==1){
					return '<span class="label label-danger">不验证</span>';
				}
                return '<span class="label label-success">要验证</span>';
            }
        }, {
			field : 'remark',
			title : '备注',
			align : 'center',
			valign : 'middle'
		}, {
			title : '操作',
            align : 'center',
            valign : 'middle',
            formatter : function(value,row,index){
            		return  '<a class="todo-ajax" href="${managerBase}/googleAuthConfig/delete.do?id='+row.id+'" title="确定要删除？">删除</a>&nbsp;&nbsp;';
            }
		}]
	});
});
</script>
