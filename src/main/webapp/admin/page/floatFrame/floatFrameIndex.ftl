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
                <select name="platform" class="form-control">
                    <option value=""><@spring.message "admin.all.platform.sys"/></option>
                    <option value="1">WEB</option>
                    <option value="2">WAP</option>
                    <option value="3">APP</option>
                </select>
            </div>
            <div class="input-group">
                <select name="showPage" class="form-control">
                    <option value=""><@spring.message "admin.all.pages"/></option>
                    <option value="1"><@spring.message "admin.website.page"/></option>
                    <option value="2"><@spring.message "admin.personal.center"/></option>
                    <option value="3"><@spring.message "admin.login.page"/></option>
                    <option value="4"><@spring.message "admin.domain.homeRegister"/></option>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
        <#if permAdminFn.hadPermission("admin:floatFrame:add")>
        	<a class="btn btn-primary open-dialog cached" href="${adminBase}/floatFrame/showAdd.do"><@spring.message "admin.menu.add"/></a>
        </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
var langs={};<#list languages as g>langs["${g.name()}"]="${g.desc}";</#list>
	Fui.addBootstrapTable({
        url: "${adminBase}/floatFrame/list.do",
        columns: [{
            field: 'title',
            title: Admin.floatWin,
            align: 'center',
            width: '200',
            valign: 'middle'
        },{
        	field: 'language',
            title: Admin.langTypes,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: function(value){
            	return langs[value];
            }
        },{
        	field: 'paltform',
            title: Admin.platSystem,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: function(value){
            	if(value){
		      		var name = '<span class="text-success">WEB</span>';
		      		switch (value) {
						case 2:
							name = '<span class="text-danger">WAP</span>';
							break;
						case 3:
							name = '<span class="text-primary">APP</span>';
							break;
					}
			      	return name;
		      	}
		      	return '-';
            }
        },{
            field: 'showPage',
            title: Admin.displayPage,
            align: 'center',
            width: '180',
            valign: 'bottom',
            formatter: showPageFormatter
        },{
            field: 'updateTime',
            title: Admin.createTime,
            align: 'center',
            width: '300',
            valign: 'middle',
            formatter: Fui.formatDatetime
        },{
            field: 'showPosition',
            title: Admin.positionDis,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: showPositionFormatter
        },{
            field: 'imgType',
            title: Admin.pictureTypes,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: imgTypeFormatter
        },{
            field: 'status',
            title: Admin.floatState,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: statusFormatter
        },{
            field: 'beginTime',
            title: Admin.beginTime,
            align: 'center',
            width: '300',
            valign: 'middle',
            formatter: Fui.formatDatetime
        },{
            field: 'overTime',
            title: Admin.endTime,
            align: 'center',
            width: '300',
            valign: 'middle',
            formatter: Fui.formatDatetime
        },{
            title: Admin.op,
            align: 'center',
            width: '200',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });
	function statusFormatter(value, row, index) {
	<#if permAdminFn.hadPermission("admin:floatFrame:status")>
		return Fui.statusFormatter({val: value,url: "${adminBase}/floatFrame/changeStatus.do?id=" + row.id + "&status="});
    <#else>
        return value == 1 ? "<@spring.message "admin.disabled"/>" : "<@spring.message "admin.enable"/>";
    </#if>
    }
    function showPageFormatter(value, row, index) {
      	if(value){
      		var name = '<span class="text-success"><@spring.message "admin.website.page"/></span>';
      		switch (value) {
				case 2:
					name = '<span class="text-danger"><@spring.message "admin.personal.center"/></span>';
					break;
				case 3:
					name = '<span class="text-primary"><@spring.message "admin.login.page"/></span>';
					break;
				case 4:
					name = '<span class="text-warning"><@spring.message "admin.domain.homeRegister"/></span>';
					break;
				case 5:
					name = '<span class="text-info"><@spring.message "admin.pay.page"/></span>';
					break;
			}
	      	return name;
      	}
      	return '-';
    }
    function showPositionFormatter(value, row, index) {
      	var v = '-';
      	if(value){
      		switch (value) {
			case 'left_top':
				v = '<@spring.message "admin.page.left.up"/>';
				break;
			case 'left_middle_top':
				v = '<@spring.message "admin.page.left.center.up"/>';
				break;
			case 'left_middle':
				v = '<@spring.message "admin.page.left.center"/>';
				break;
			case 'left_middle_bottom':
				v = '<@spring.message "admin.page.left.center.down"/>';
				break;
			case 'left_bottom':
				v = '<@spring.message "admin.page.left.down"/>';
				break;
			case 'right_middle_top':
				v = '<@spring.message "admin.page.right.center.up"/>';
				break;
			case 'right_top':
				v = '<@spring.message "admin.page.right.up"/>';
				break;
			case 'right_middle':
				v = '<@spring.message "admin.page.right.center"/>';
				break;
			case 'right_middle_bottom':
				v = '<@spring.message "admin.page.right.center.down"/>';
				break;
			case 'right_bottom':
				v = '<@spring.message "admin.page.right.down"/>';
				break;
			case 'middle_top':
				v = '<@spring.message "admin.page.center.up"/>';
				break;
			case 'middle_bottom':
				v = '<@spring.message "admin.page.center.down"/>';
				break;
			}
      	}
        return v;
     }
     function imgTypeFormatter(value, row, index) {
      	if(value && value == 2){
      		return '<span class="text-success"><@spring.message "admin.page.turn.pic"/></span>';
      	}
      	return '<span class="text-info"><@spring.message "admin.build.house"/></span>';
     }
     function operateFormatter(value, row, index) {
          let s='';
          <#if permAdminFn.hadPermission("admin:floatFrame:viewDetail")>
              s=s+'<a class="open-dialog" href="${adminBase}/floatFrame/view.do?id='+row.id+'"><@spring.message "admin.menu.viewDetail"/></a>';
          </#if>
          <#if permAdminFn.hadPermission("admin:floatFrame:modify")>
              s=s+'&nbsp;&nbsp;<a class="open-dialog" href="${adminBase}/floatFrame/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>  ',
          </#if>
          <#if permAdminFn.hadPermission("admin:floatFrame:delete")>
              s=s+'&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/floatFrame/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.title+'”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a>'
          </#if>
          return s;
      }
});
</script>
