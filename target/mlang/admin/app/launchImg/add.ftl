<!-- 新增 -->
<form action="${man}/cert/upload.do" class="form-submit" method="post" enctype="multipart/form-data"
      id="updateCertFormId">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title"><@spring.message "admin.upload.picture.start"/></h4>
        </div>
        <div class="modal-body">
            <table class="table table-bordered table-striped" style="margin-bottom:0px">
                <tbody>
                <tr>
                    <td width="30%" class="text-right media-middle"><@spring.message "admin.start.picture"/>：</td>
                    <td><input name="file" class="form-control required" type="file"/></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
            <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="saveFn();"><@spring.message "admin.save"/>
            </button>
        </div>
</form>
<script>
    function saveFn() {
        var $form = $("#updateCertFormId");
        $.ajax({
            type: 'POST',
            url: "${adminBase}/appLauncher/addSave.do",
            data: new FormData($form[0]),
            dataType: "json",
            cache: false,
            contentType: false,
            processData: false,
            success: function (json) {
                $form[0].reset();
                parent.layer.closeAll();
                layer.msg(json.msg || "<@spring.message "admin.upload.success"/>");
                var $table=$(".fui-box.active").data("bootstrapTable");
                if($table && $table.length){
                    $table.bootstrapTable('refresh');
                }
            },
            error: function () {
                parent.layer.closeAll();
                layer.msg('<@spring.message "admin.network.except"/>');
            }
        });
    }
</script>
