<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.username"/>" style="width:150px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="proxyUsername"  placeholder="<@spring.message "admin.proxyUsername"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <select class="form-control" name="userType">
                    <option value=""><@spring.message "admin.userType.all"/></option>
                    <option value="130"><@spring.message "admin.member"/></option>
                    <option value="120"><@spring.message "admin.proxy"/></option>
                    <option value="150"><@spring.message "admin.guest"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="type">
                    <option value=""><@spring.message "admin.perType.all"/></option>
                    <#list perms as p><option value="${p.type}">${p.desc}</option></#list>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
        </div>
    </div>
</form>
<table class="fui-default-table" ></table>
<script type="text/javascript">
    requirejs(['jquery','bootstrap','Fui'],function(){
    var perms={};<#list perms as p>perms["${p.type}"]="${p.desc}";</#list>
        Fui.addBootstrapTable({
            url : '${adminBase}/userPerm/list.do',
            columns : [ {
                field : 'username',
                title : Admin.username,
                align : 'center',
                valign : 'middle',
                formatter : accountFormatter
            },  {
                field : 'userType',
                title : Admin.userType,
                align : 'center',
                valign : 'middle',
                formatter : userTypeFormatter
            },{
                field : 'type',
                title : Admin.userPermType,
                align : 'center',
                valign : 'middle',
                formatter :  function(value, row, index) {
                	return perms[''+value];
                }
            },{
                field : 'status',
                sortable: true,
                title : Admin.status,
                align : 'center',
                valign : 'middle',
                formatter :  function(value, row, index) {
                	 return Fui.statusFormatter({
		                val: value,
		                url: "${adminBase}/userPerm/updStatus.do?id=" + row.id + "&status="
		            });
                }
            }]
        });
        function accountFormatter(value, row, index) {
           <#if permAdminFn.hadPermission("admin:user")>
            return '<a class="open-tab text-danger" href="${adminBase}/user/detail.do?id='+row.userId+'"><span class="text-danger">'+value+'</span></a>';
           <#else>
            return value;
           </#if>
        }
        function userTypeFormatter(value, row, index) {
            if (value == 120){
                return '<span class="label label-danger">'+Admin.proxy+'</span>'
            }else if(value == 150 || value == 160){
                return '<span class="label label-warning">'+Admin.guest+'</span>';
            }else{
            	 return '<span class="label label-success">'+Admin.member+'</span>';
            }
        }
    });
</script>
