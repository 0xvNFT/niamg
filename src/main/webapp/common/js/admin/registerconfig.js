define(['jquery','bootstrap','Fui','fui_table'],function(){
	var url=baseInfo.adminBaseUrl+"/registConfig/list.do?platform=",
		cols=[ {
		field : 'name',
		title : Admin.propName,
		align : 'center',
		valign : 'middle',
	}, {
		field : 'show',
		title : Admin.show,
		align : 'center',
		valign : 'bottom',
		formatter : function(value, row, index){return showFormatter(value, row, 'show')}
	}, {
		field : 'required',
		title : Admin.required,
		align : 'center',
		valign : 'bottom',
		formatter : function(value, row, index){return showFormatter(value, row, 'required')}
	}, {
		field : 'validate',
		title : Admin.validate,
		align : 'center',
		valign : 'bottom',
		formatter : function(value, row, index){return showFormatter(value, row, 'validate')}
	}, {
		field : 'uniqueness',
		title : Admin.uniqueness,
		align : 'center',
		valign : 'bottom',
		formatter : function(value, row, index){return showFormatter(value, row, 'uniqueness')}
	},{
		field : 'sortNo',
		title : Admin.sortNo,
		align : 'center',
		valign : 'middle'
	},{
		field : 'tips',
		title : Admin.tips,
		align : 'center',
		valign : 'middle'
	}, {
		title : Admin.operating,
		align : 'center',
		valign : 'middle',
		formatter : operateFormatter
	}  ];
	function filterData(data) {
		return data.filter(function(row) {
			return row.eleName !== 'qq' && row.eleName !== 'wechat';
		});
	}
	function showFormatter(value, row, field) {
		if(value==3||row.eleName=='captcha' || row.eleName=='username'||row.eleName=='pwd'||row.eleName=='rpwd'){
			return "-";
		}
		if(row.eleName=='receiptPwd' && field!='show' && field!='required'){
			return "-";
		}
		return Fui.statusFormatter({val:value,onText:Admin.yes,offText:Admin.no,url:baseInfo.adminBaseUrl+"/registConfig/updateProp.do?id="+row.id+'&prop='+field+"&value="});
	}
	function operateFormatter(value, row, index) {
		return '<a class="open-dialog" href="'+baseInfo.adminBaseUrl+'/registConfig/showModify.do?id='+row.id+'">'+Admin.update+'</a>';
	}
	return {
		render:function(){
			var curBootstrapTable=null,$con=$("#station_registerconfig_con_warp_id");
			$con.find("a[oname]").click(function(){
				var $it=$(this),name=$it.attr("oname");
				switch(name){
				case "huiyuan":
					curBootstrapTable.bootstrapTable('refreshOptions', {
						columns : cols,
						url : url+130,
						sidePagination: 'client',
						pagination:false
					});
					break;
				case "daili":
					curBootstrapTable.bootstrapTable('refreshOptions', {
						columns : cols,
						url : url+120,
						sidePagination: 'client',
						pagination:false
					});
					break;
				}
			});
			Fui.addBootstrapTable({
				sidePagination: 'client',
				pagination:false,
				url : url+130,
				columns : cols,
				onCreatedSuccessFun:function($table){
					curBootstrapTable=$table;
				}
			});
		}
	}
});