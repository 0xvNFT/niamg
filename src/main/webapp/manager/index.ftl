<!doctype html>
<html lang="en"><head>
    <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=yes,shrink-to-fit=no">
	<title><@spring.message "manager.center.manage.name"/></title>
	<link rel="stylesheet" href="${base}/common/css/font-awesome5/css/all.min.css" />
	<link rel="stylesheet" type="text/css" href="${base}/common/css/main.min.css?v=13">
	<style>#fui_sidebar_nars > .nav-stacked{width: 200px;}</style>
	<script src="${base}/common/js/require.js?v=2"></script>
	<script>
	 window.UMEDITOR_HOME_URL = "${base}/common/js/umeditor/";
	var baseInfo={username:"${loginUser.username}",adminBaseUrl:"${managerBase}"
		,baseUrl:"${base}",loginDialogUrl:"${managerBase}/loginDialog.do"
		,navUrl:"${managerBase}/nav.do"
		,homeUrl:"${managerBase}/home.do"};
	require.config({
		baseUrl: '${base}/common/js',
		paths: {
			jquery: 'jquery-1.12.4.min'
			,jquery_validation:'jquery-validate/jquery.validate.min'
			,bootstrap:'bootstrap/3.3.7/js/bootstrap.min'
			,bootstrap_editable:'bootstrap/editable/js/bootstrap-editable.min'
			,bootstrapTable:'bootstrap-table/bootstrap-table.min'
			,bootstrapTableLocale:'bootstrap-table/locale/bootstrap-table-zh-CN.min'
			,moment:'moment/moment.min'
			,moment_zh:'moment/locale/zh-cn'
			,datetimepicker: 'bootstrap-datetimepicker/js/bootstrap-datetimepicker.min'
			,layer:'layer/layer'
			,Fui:'fui/fui.js?v=3'
			,fui_table:'fui/fui-table.js?v=2'
			,fui_form:'fui/fui-form.js?v=1.0.0'
			,fui_datetime:'fui/fui-datetime.js?v=7'
			,fui_switch:'fui/fui-switch.js?v=1.0.0'
			,template:'artTemplate/template'
			,bootstrapSwitch:'bootstrap/switch/js/bootstrap-switch.min'
            ,eChart: 'echart/echarts.min'
			,bootstrapSelect:"bootstrap/select/js/bootstrap-select.min"
			,metisMenu:"metisMenu/jquery.metisMenu.js?v=2"
            , ueditor: 'umeditor/umeditor.min'
            , ueditor_zh: 'umeditor/lang/zh-cn/zh-cn'
		},
		map: {
	      '*': {
	        'css': 'require-lib/css.min'
	        ,'text': 'require-lib/text'
	      } 
	    },
		shim: {
			'bootstrap':['jquery']
			,'bootstrap_editable':['jquery','bootstrap','datetimepicker','css!${base}/common/js/bootstrap/editable/css/bootstrap-editable.css']
			,'bootstrapTable':['jquery','bootstrap','css!${base}/common/js/bootstrap-table/bootstrap-table.css']
			,'bootstrapTableLocale':['bootstrapTable']
	    	,'datetimepicker':['jquery','css!${base}/common/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css']
	    	,'bootstrapSwitch':['jquery','css!${base}/common/js/bootstrap/switch/css/bootstrap-switch.min.css']
	    	,'fineUploader':['jquery','css!${base}/common/js/fine-uploader/fine-uploader-gallery.min.css?v=11.2']
	    	,'bootstrapSelect':['css!${base}/common/js/bootstrap/select/css/bootstrap-select.min.css']
           , 'ueditor': {
                deps: ['${base}/common/js/umeditor/umeditor.config.js','css!${base}/common/js/umeditor/themes/default/css/umeditor.css'],
                exports: 'UM'
            }
            , 'ueditor_zh': ['ueditor']
		}
	});
	require(["${base}/common/js/main.js?v=5"],function(){
	});
	</script>
</head>
<body>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow: hidden">
	<nav class="navbar-default navbar-static-side" role="navigation">
	    <div class="sidebar-collapse">
			<i class="fa fa-angle-double-left navbar-min" title="<@spring.message "manager.menu.rec"/>"></i>
	        <ul class="nav fnav" id="side-menu"><li class="nav-header"><h3>${serverName}<@spring.message "manager.system.name"/></h3></li></ul>
	    </div>
	</nav>
	<div class="gray-bg dashbard-1" id="page-wrapper">
	    <div class="container-fluid border-bottom">
	        <nav class="navbar navbar-static-top" role="navigation">
	        		<div class="navbar-header"><h3>${serverName}<@spring.message "manager.system.name"/></h3></div>
	            <ul class="nav navbar-top-links navbar-right">
	                <li class="dropdown hidden-xs nav-item"><a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">${loginUser.username}&nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
						<ul class="dropdown-menu">
							<li><a href="${managerBase}/modifyPwd.do" class="open-dialog"><@spring.message "manager.password.update"/></a></li>
							<li class="divider"></li>
							<li><a tabindex="-1" href="${managerBase}/logout.do"><@spring.message "manager.back.exit"/></a></li>
						</ul>
						<i class="bottom-border"></i>
					</li>
	            </ul>
	        </nav>
	    </div>
		<ul class="nav nav-tabs fui-tabs" role="tablist" id="fui_tab_header"></ul>
		<div class="tab-content" id="fui_tab_content"></div>
	</div>
<#if  content_page?has_content >
<script type="text/html" class="direct_open_url" url="${base}${content_url}"><!--<#include "${content_page}.ftl"/>//--></script>
</#if>
</body></html>
