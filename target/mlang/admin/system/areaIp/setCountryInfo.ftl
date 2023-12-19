
<form class="fui-search table-tool" method="post" id="countryIpFormId">
	<div class="form-group">
	<div class="form-inline">
		<div class="input-group">
			<input type="hidden" value="1" name="countryType">
			<button class="btn btn-primary open-dialog cached" url="${adminBase}/stationWhiteArea/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></button>
		</div>
		<input type="text" class="form-control" name="searchText" placeholder="<@spring.message "admin.EnteranIPsearch"/>" autocomplete="on">
		<button class="btn btn-primary select-btn" type="button"><@spring.message "admin.SearchthecountrytowhichtheIPbelongs"/></button>
		 <div class="input-group">
			<h4><span class="label label-danger" id="countrySpanTT" style="display: none;"><font id="countryTT"></font></span></h4>
		</div>
	</div>
	</div>
</form>
<table id="agentcountry_type_datagrid_tb"></table>
<h3>
<#--	<span class="label label-danger">添加国家/地区后，请到“系统设置-》网站基本设定-》系统开关”中开启“前台地区访问白名单”，否则国家/地区限制不起作用</span><br>-->
<#--<%--	<span class="label label-danger">当“系统设置-》网站基本设定-代理后台地区访问限制开关”开启以后，会禁止类型为代理后台的地区访问代理后台</span>--%>-->
</h3>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    	var $form=$("#countryIpFormId")
		,$searchText=$form.find("[name='searchText']");
        Fui.addBootstrapTable({
        	id : 'agentcountry_type_datagrid_tb',
    		url : '${adminBase}/stationWhiteArea/list.do',
            columns: [ {
                field: 'countryName',
                title: '<@spring.message "admin.CountryCitywhitelist"/>',
                align: 'center',
                valign: 'middle'
            }, {
    			field : 'status',
    			title : '<@spring.message "admin.status"/>',
    			align : 'center',
    			width : '200',
    			valign : 'middle',
    			sortable:true,
    			formatter : statusFormatter
    		}, {
            //     field: 'limitType',
            //     title: '类型',
            //     align: 'center',
            //     valign: 'middle',
            //     sortable:true,
            //     formatter: limitTypeFormatter
            // }, {
    			title : '<@spring.message "admin.opering"/>',
    			align : 'center',
    			width : '200',
    			valign : 'middle',
    			formatter : operateFormatter
    		}]
        });
        
        function limitTypeFormatter(value, row, index) {
            if (value && value == 2) {
                return "代理后台";
            }
            return "会员前台";
        }
        
        function statusFormatter(value, row, index) {
    		return Fui.statusFormatter({val:value,url:"${adminBase}/stationWhiteArea/update.do?id="+row.id+"&status="});
    	}
        function operateFormatter(value, row, index) {
    		return [ 
				'<a class="todo-ajax" href="${adminBase}/stationWhiteArea/delete.do?id=',row.id,'" title="<@spring.message "manager.sure.del.ip"/>“',row.countryName,'”？">',
    				'<i class="glyphicon glyphicon-remove"></i>', '</a>' ]
    				.join('');
    	}
        $form.find(".select-btn").click(search);
        function search(){
    		var acc=$searchText.val();
    		$("#countryTT").html("");
			$("#countrySpanTT").hide();
    		if(acc==''){
    			layer.msg("<@spring.message "admin.EnteranIPsearch"/>");
    			return false;
    		}
    		$.ajax({
    			url:"${adminBase}/stationWhiteArea/findContryByIp.do?ip="+acc,
    			type:"GET",
    			success:function(data){
    				$searchText.val(acc);
    				$("#countryTT").html(data.data);
					$("#countrySpanTT").show();
    			},
    			dataType:"JSON",
    			errorFn:function(){
    				$searchText.val(acc);
    			}
    		});
    	}
        
    });
</script>
