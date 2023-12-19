<form class="fui-search table-tool" method="post">
<div class="form-group fui-data-wrap">
        <div class="form-inline">
            <a class="btn btn-primary open-dialog" href="${managerBase}/cert/add.do">新增</a>
        </div>
</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${managerBase}/cert/list.do',
            pagination: false,
            responseHandler: function (res) {
                return {
                    rows: res
                };
            },
            columns: [{
                field: 'id',
                title: '编号',
                align: 'center'
            }, {
                field: 'name',
                title: '名称',
                align: 'center'
            }, {
                field: 'fileName',
                title: '证书文件名',
                align: 'center',
                formatter: function(value,row,index){
                		return '<a  href="${managerBase}/cert/download.do?fileName=' + value + '" target="_blank"  title="下载">下载' + value + '</a>';
                }
            }, {
                field: 'keyFile',
                title: 'key文件',
                align: 'center',
                formatter: keyFileFormatter
            }, {
                field: 'csrFile',
                title: 'csr文件',
                align: 'center',
                formatter: csrFileFormatter
            }, {
                title: '操作',
                align: 'center',
                formatter: operatorFormatter
            }]
        });

        function keyFileFormatter(value, row, index) {
            if (value) {
                return '<a href="${managerBase}/cert/download.do?fileName=' + value + '" target="_blank"  title="下载">下载' + value + '</a>';
            }
            return '<span class="label label-default">不存在</span>';
        }

        function csrFileFormatter(value, row, index) {
            if (value) {
                return '<a  href="${managerBase}/cert/viewCsr.do?id=' + row.id + '" class="open-dialog"  title="查看">查看' + value + '</a> &nbsp; '+
                	'<a href="${managerBase}/cert/download.do?fileName=' + value + '" target="_blank"  title="下载">下载' + value + '</a>';
            }
            return '<span class="label label-default">不存在</span>';
        }

        function operatorFormatter(value, row, index) {
            var s = '<a class="open-dialog"  href="${managerBase}/cert/add.do?certId=' + row.id + '" title="修改"><i class="glyphicon glyphicon-pencil"></i></a>';
            s += '<a class="todo-ajax" title="确定要删除“' + row.fileName + '”？" href="${managerBase}/cert/delete.do?id=' + row.id + '"><i class="glyphicon glyphicon-remove"></i></a>';
            return s;
        }
    });


</script>
