<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
	        <input class="form-control" name="username" type="text" placeholder="<@spring.message "admin.administrator"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
			<a class="btn btn-primary open-dialog" href="${adminBase}/googleAuth/showAdd.do"><@spring.message "admin.menu.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<h3 style="color:red;font-size: 16px">
    <div><@spring.message "admin.googleValid1"/></div>
    <div><@spring.message "admin.googleValid2"/></div>
    <div><@spring.message "admin.googleValid3"/></div>
    <div><@spring.message "admin.googleValid4"/></div>
</h3>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : '${adminBase}/googleAuth/list.do',
		columns : [ { 
			field : 'adminUsername',
			title : Admin.administrator,
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value){
					return value;
				}
                return Admin.universal;
            }
        }, {
			field : 'remark',
			title : Admin.remark,
			align : 'center',
			valign : 'middle'
		}, {
			field : 'status',
			title : Admin.status,
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value==1){
					return '<span class="label label-danger"><@spring.message "admin.doNotVerify"/></span>';
				}
                return '<span class="label label-success"><@spring.message "admin.needVerify"/></span>';
            }
		}]
	});
});
</script>
