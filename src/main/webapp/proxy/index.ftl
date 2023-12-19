<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title><@spring.message "admin.title"/></title>
    <meta name="renderer" content="webkit">
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${base}/common/css/font-awesome5/css/all.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/common/css/main.min.css?v=2">
    <script src="${base}/common/js/require.js?v=1"></script>
    <script src="${base}/common/lang/${language}.js?v=4"></script>
    <script>
        var baseInfo = {
            username: "${loginUser.username}", adminBaseUrl: "${proxyBase}"
            , baseUrl: "${base}", loginDialogUrl: "${proxyBase}/loginDialog.do"
            , userId: "${loginUser.id}"
            , indexUrl: "${base}"
            , navUrl: "${proxyBase}/nav.do"
            , homeUrl: "${proxyBase}/home.do"
        };
        require.config({
            baseUrl: '${base}/common/js',
            paths: {
                jquery: 'jquery-1.12.4.min'
                , jquery_validation: 'jquery-validate/jquery.validate.min'
                , bootstrap: 'bootstrap/3.3.7/js/bootstrap.min'
                , bootstrap_editable: 'bootstrap/editable/js/bootstrap-editable.min'
                , bootstrapTable: 'bootstrap-table/bootstrap-table.min'
                , moment: 'moment/moment.min'
                <#switch language><#case 'en'>
                , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-en-US.min'
                , moment_zh: 'moment/locale/en'
                    <#break><#case 'vi'>
                , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-vi-VN.min'
                , moment_zh: 'moment/locale/vi'
                    <#break><#case 'id'>
                , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-id-ID.min'
                , moment_zh: 'moment/locale/id'
                    <#break><#case 'in'>
                ,bootstrapTableLocale:'bootstrap-table/locale/bootstrap-table-in-IN.min'
                ,moment_zh:'moment/locale/in'
                    <#break><#case 'ms'>
                , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-ms-MY.min'
                , moment_zh: 'moment/locale/ms'
                    <#break><#case 'th'>
                , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-th-TH.min'
                , moment_zh: 'moment/locale/th'
                    <#break><#case 'ja'>
			    , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-ja-JP.min'
			    , moment_zh: 'moment/locale/ja'
                    <#break><#case 'br'>
			    , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-pt-BR.min'
			    , moment_zh: 'moment/locale/br'
                    <#break><#case 'es'>
                ,bootstrapTableLocale:'bootstrap-table/locale/bootstrap-table-es-ES.min'
                ,moment_zh: 'moment/locale/es'
                <#break><#default>
                , bootstrapTableLocale: 'bootstrap-table/locale/bootstrap-table-zh-CN.min'
                , moment_zh: 'moment/locale/zh-cn'
                </#switch>
                , moment_zh: 'moment/locale/zh-cn'
                , datetimepicker: 'bootstrap-datetimepicker/js/bootstrap-datetimepicker.min'
                , layer: 'layer/layer'
                , Fui: 'fui/fui.js?v=2'
                , fui_table: 'fui/fui-table.js?v=2'
                , fui_form: 'fui/fui-form.js?v=1.0.0'
                , fui_datetime: 'fui/fui-datetime.js?v=7'
                , fui_switch: 'fui/fui-switch.js?v=1.0.0'
                , template: 'artTemplate/template'
                , bootstrapSwitch: 'bootstrap/switch/js/bootstrap-switch.min'
                , Chart: 'bootstrap/chart/2.4.0/Chart.min'
                , eChart: 'echart/echarts.min'
                , bootstrapSelect: "bootstrap/select/js/bootstrap-select.min"
                , metisMenu: "metisMenu/jquery.metisMenu.js?v=2"
                , ueditor: 'umeditor/umeditor.min'
                , ueditor_zh: 'umeditor/lang/zh-cn/zh-cn'
                , clipboard: "clipboard.min"
            },
            map: {
                '*': {
                    'css': 'require-lib/css.min'
                    , 'text': 'require-lib/text'
                }
            },
            shim: {
                'bootstrap': ['jquery']
                ,
                'bootstrap_editable': ['jquery', 'bootstrap', 'datetimepicker', 'css!${base}/common/js/bootstrap/editable/css/bootstrap-editable.css']
                ,
                'bootstrapTable': ['jquery', 'bootstrap', 'css!${base}/common/js/bootstrap-table/bootstrap-table.css']
                ,
                'bootstrapTableLocale': ['bootstrapTable']
                ,
                'datetimepicker': ['jquery', 'css!${base}/common/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css']
                ,
                'bootstrapSwitch': ['jquery', 'css!${base}/common/js/bootstrap/switch/css/bootstrap-switch.min.css']
                ,
                'fineUploader': ['jquery', 'css!${base}/common/js/fine-uploader/fine-uploader-gallery.min.css?v=11.2']
                ,
                'bootstrapSelect': ['css!${base}/common/js/bootstrap/select/css/bootstrap-select.min.css']
                ,
                'metisMenu': ['jquery']
                ,
                'ueditor': {
                    deps: ['${base}/common/js/umeditor/umeditor.config.js', 'css!${base}/common/js/umeditor/themes/default/css/umeditor.css'],
                    exports: 'UM'
                }
                ,
                'ueditor_zh': ['ueditor']
            }

        });
        require(["${base}/common/js/main.js?v=7"], function () {
            require(["${base}/common/js/proxy/proxy.js?v=1"]);
        });
    </script>
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow: hidden">
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <i class="fa fa-angle-double-left navbar-min" title="收起菜单"></i>
        <ul class="nav fnav" id="side-menu">
            <li class="nav-header"></li>
        </ul>
    </div>
</nav>
<div class="gray-bg dashbard-1" id="page-wrapper">
    <div class="container-fluid border-bottom">
        <nav class="navbar navbar-static-top" role="navigation">
            <div class="navbar-left"><h3><@spring.message "admin.title"/></h3></div>
            <ul class="nav navbar-top-links navbar-right">
            <#--                <li><a href="${proxyBase}/user/online/index.do" class="open-tab count-info" id="online_account_id"-->
            <#--                       title="<@spring.message "admin.index.onlineTip"/>" data-refresh="true"><span class="label label-warning" id="online_span"></span><@spring.message "admin.index.online"/></a>-->
            <#--                </li>-->
                <li class="dropdown hidden-xs"><a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
                                                  aria-haspopup="true"
                                                  aria-expanded="false"><@spring.message "admin.index.select.language"/>
                    &nbsp;&nbsp;<i class="fa fa-caret-down"></i></a>
                    <ul class="dropdown-menu">
                        <#list languages as l>
                            <li><a href="${proxyBase}/changeLanguage.do?lang=${l.name()}">${l.lang}</a></li>
                            <#if l_has_next>
                                <li class="divider"></li></#if>
                        </#list>
                    </ul>
                    <i class="bottom-border"></i>
                </li>
                <li class="dropdown hidden-xs"><a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
                                                  aria-haspopup="true" aria-expanded="false">${loginUser.username}&nbsp;&nbsp;<i
                        class="fa fa-caret-down"></i></a>
                    <ul class="dropdown-menu">
                        <li><a href="${proxyBase}/modifyPwd.do"
                               class="open-dialog"><@spring.message "admin.index.modify.pwd"/></a></li>
                        <li class="divider"></li>
                        <li><a tabindex="-1" href="${proxyBase}/logout.do"><@spring.message "admin.index.logout"/></a>
                        </li>
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
<script type="text/html" class="direct_open_url"
        url="${base}${content_url}"><!--<#include "${content_page}.ftl"/>//--></script>
</#if>
</body>
</html>
