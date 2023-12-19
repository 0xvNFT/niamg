<div id="certDomainSearch" class="form-inline">
    <div class="form-group">
        <div class="input-group">
            <select class="form-control" id="stationFolder">
                <option value="">选择站点</option>
                <#list stations as s><option value="${s.code}">${s.code}</option></#list>
            </select>
        </div>
        <div class="input-group">
            <input type="text" class="form-control" id="domainName" placeholder="域名">
        </div>
        <div class="input-group">
            <input type="text" class="form-control" id="certId" placeholder="证书编号">
        </div>
    </div>
    <button class="btn btn-primary" onclick="search();">查询</button>
    <button class="btn btn-primary" type="button" onclick="setOwnCert();">一键配置证书</button>
    <button class="btn btn-primary" type="button" onclick="setHttps();">一键配置跳转</button>
    <button class="btn btn-primary" type="button" onclick="save();">保存</button>
</div>
<table id="cert_domain_datagrid_tb"></table>
<script type="text/javascript">
    var certDomains=null;
    requirejs(['jquery', 'bootstrap','fui_table', 'Fui'], function () {
        $('#cert_domain_datagrid_tb').bootstrapTable({
            url: '${managerBase}/certDomain/list.do',
            method: 'post',
		    sidePagination: 'client',
			showRefresh:true,
			showToggle:true,
			toolbar : $('#certDomainSearch'),
            columns: [{
                field: 'domainName',
                title: '域名',
                align: 'center'
            }, {
                field: 'folder',
                title: '编号',
                align: 'center'
            }, {
                field: 'certId',
                title: '所属证书',
                align: 'center',
                formatter: certIdFormatter
            }, {
                field: 'ownCertId',
                title: '配置证书',
                align: 'center',
                formatter: ownCertIdFormatter
            }, {
                field: 'https',
                title: 'HTTPS跳转',
                align: 'center',
                formatter: httpsFormatter
            }, {
                title: '操作',
                align: 'center',
                formatter: operatorFormatter
            }],
            responseHandler: function (res) {
                certDomains = res;
                return {
                    data : res
                };
            },
        });

        function httpsFormatter(value, row, index) {
            if (value == true) {
                return '<span class="label label-primary">是</span>';
            }
            return '<span class="label label-default">否</span>';
        }

        function certIdFormatter(value, row, index) {
            if (!value) {
                return "-";
            }
            if (row.ownCertId && row.ownCertId != value) {
                return '<span class="label label-warning">' + value + '</span>';
            }
            return '<span class="label label-primary">' + value + '</span>';
        }

        function ownCertIdFormatter(value, row, index) {
            if (!value) {
                return "-";
            }
            if (row.certId && row.certId != value) {
                return '<span class="label label-success">' + value + '</span>';
            }
            return '<span class="label label-primary">' + value + '</span>';
        }

        function operatorFormatter(value, row, index) {
            var s = "";
            if (row.certId && row.ownCertId == row.certId) {
                s = s + '<a onclick=\'confUnCert(this,'+JSON.stringify(row)+')\' href="javascript:void(0)" title="取消配置证书">取消配置证书</a>  ';
            } else {
                s = s + '<a onclick=\'confCert(this,'+JSON.stringify(row)+')\' href="javascript:void(0)" title="配置证书">配置证书</a>    ';
            }
            if (row.https == true) {
                s = s + '<a onclick=\'confUnHttps(this,'+JSON.stringify(row)+')\' href="javascript:void(0)" title="配置不跳转">配置不跳转</a>    ';
            } else {
                s = s + '<a onclick=\'confHttps(this,'+JSON.stringify(row)+')\' href="javascript:void(0)" title="配置跳转">配置跳转</a>    ';
            }
            s = s + '<a onclick=\'del(this,'+JSON.stringify(row)+')\' href="javascript:void(0)" title="删除">删除</a>';
            return s;
        }
    });
    function setOwnCert() {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            item.ownCertId = item.certId;
        }
        search();
    }

    function setHttps() {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            item.https = true;
        }
        search();
    }

    function search() {
        var data = [], folder = $("#stationFolder").val(),
                domainName = $("#domainName").val().replace(/(^\s*)|(\s*$)/g, ''),
                certId = $("#certId").val().replace(/(^\s*)|(\s*$)/g, '');
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            if (folder && folder != '') {
                if (!item.folder || item.folder.indexOf(folder) == -1) {
                    continue;
                }
            }
            if (domainName && domainName != '') {
                if (!item.domainName || item.domainName.indexOf(domainName) == -1) {
                    continue;
                }
            }
            if (certId && certId != '') {
                if ((!item.certId || item.certId != certId) && (!item.ownCertId || item.ownCertId != certId)) {
                    continue;
                }
            }
            data.push(item);
        }
        
        $("#cert_domain_datagrid_tb").bootstrapTable('refreshOptions', {data: data,url:""});
    }

    function save() {
        var item = null, msg = "";
        var newArr = [];
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9876.com") > -1) {
                continue;
            }
            if (!item.folder) {
                msg = msg + "域名：" + item.domainName + " 没有对应的站点<br>";
            }
            if (item.certId != item.ownCertId) {
                msg = msg + "域名：" + item.domainName + " 证书不一致<br>";
            }
            if (item.certId) {
                newArr.push(certDomains[i]);
            }
        }
        if (msg != "") {
            layer.confirm(msg, {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: '${managerBase}/certDomain/save.do',
                    dataType: "json",
                    type: "POST",
                    data: {data: JSON.stringify(newArr)},
                    success: function (data) {
                        layer.msg(data.msg || '保存成功');
                        $("#cert_domain_datagrid_tb").bootstrapTable('refreshOptions', {url:"${managerBase}/certDomain/list.do"});
                    }
                });
            });
        } else {
            $.ajax({
                url: '${managerBase}/certDomain/save.do',
                dataType: "json",
                type: "POST",
                data: {data: JSON.stringify(newArr)},
                success: function (data) {
                    layer.msg(data.msg || '保存成功');
                    $("#cert_domain_datagrid_tb").bootstrapTable('refreshOptions', {url:"${managerBase}/certDomain/list.do"});
                }
            });
        }
    }

    function confHttps(e, row) {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            if (item.domainName === row.domainName) {
                item.https = true;
                search();
                return;
            }
        }
    }

    function confUnHttps(e, row) {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            if (item.domainName == row.domainName) {
                item.https = false;
                search();
                return;
            }
        }
    }

    function confUnCert(e, row) {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            if (item.domainName == row.domainName) {
                item.ownCertId = "";
                search();
                return;
            }
        }
    }

    function confCert(e, row) {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            if (item.domainName == row.domainName) {
                item.ownCertId = item.certId;
                search();
                return;
            }
        }
    }

    function del(e, row) {
        var item = null;
        for (var i = 0, len = certDomains.length; i < len; i++) {
            item = certDomains[i];
            if (item.domainName.indexOf("yunji9.com") > -1) {
                continue;
            }
            if (item.domainName == row.domainName) {
                certDomains.splice(i, 1);
                search();
                return;
            }
        }
    }
</script>
