<!-- 新增 -->
<form action="${man}/cert/upload.do" class="form-submit" method="post" enctype="multipart/form-data"
      id="updateCertFormId">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title">编辑证书</h4>
        </div>
        <div class="modal-body">
            <table class="table table-bordered table-striped" style="margin-bottom:0px">
                <tbody>
                <tr>
                    <td width="30%" class="text-right media-middle">证书ID：</td>
                    <td><input name="certId" class="form-control required" value="${certId}" type="text"/></td>
                </tr>
                <tr>
                    <td width="30%" class="text-right media-middle">证书1：</td>
                    <td><input name="crt1" class="form-control required" type="file"/></td>
                </tr>
                <tr>
                    <td class="text-right media-middle">证书2：</td>
                    <td><input name="crt2" class="form-control required" type="file"/></td>
                </tr>
                <tr>
                    <td class="text-right media-middle">key文件：</td>
                    <td><input name="keyFile" class="form-control required" type="file"/></td>
                </tr>
                <tr>
                    <td class="text-right media-middle">csr文件：</td>
                    <td><input name="csrFile" class="form-control required" type="file"/></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default fui-close">关闭</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="saveFn();">保存
            </button>
        </div>
</form>
<script>
    function saveFn() {
        var $form = $("#updateCertFormId");
        $.ajax({
            type: 'POST',
            url: "${managerBase}/cert/upload.do",
            data: new FormData($form[0]),
            dataType: "json",
            cache: false,
            contentType: false,
            processData: false,
            success: function (json) {
                $form[0].reset();
                parent.layer.closeAll();
                layer.msg(json.msg || "上传成功");
                var $table=$(".fui-box.active").data("bootstrapTable");
				if($table && $table.length){
					$table.bootstrapTable('refresh');
				}
            },
            error: function () {
                parent.layer.closeAll();
                layer.msg('网络异常');
            }
        });
    }
</script>
