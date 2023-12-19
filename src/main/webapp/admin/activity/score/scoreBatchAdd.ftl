<form action="${adminBase}/scoreHistory/doBatchAdd.do" class="form-submit" id="score_batch_add_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <input type="hidden" name="type" value="1">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.scoreHis.batchAdd"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.add.score.model"/>:</td>
                        <td>
                            <select name="modelType" class="form-control">
                                <option value="1" selected><@spring.message "admin.define.score.val"/></option>
                                <option value="2"><@spring.message "admin.fixed.score.val"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.username"/>：</td>
                        <td>
                            <textarea name="usernames" style="height:150px;width:100%;"></textarea>
                            <div class="modelType1">
                                <br><@spring.message "admin.row.data.score.divide"/>
                                <br>如：aaaaaa 10<span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;bbbbbb,20 <span
                                    style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
                            </div>
                            <div  class="modelType2">
                                <br><@spring.message "admin.data.format.divide"/>
                                <br>如：aaaaaa bbbbbb cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;aaaaaa,bbbbbb,cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
                            </div>
                        </td>
                    </tr>
                    <tr class="modelType2">
                        <td class="text-right active media-middle"><@spring.message "admin.scores"/>：</td>
                        <td><input type="text" class="form-control" name="score" /></td>
                    </tr>
                    <tr>
                        <td class="text-right active media-middle"><@spring.message "admin.operate.reason"/>：</td>
                        <td><textarea class="form-control" name="remark"></textarea></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
requirejs(['jquery'], function () {
    var $form = $("#score_batch_add_form_id");
    $form.find(".modelType2").hide();
    $form.find(".modelType1").show();
    $form.find("select[name='modelType']").change(function () {
        var type = $(this).val();
        if (type === 1 || type ==='1' ) {
            $form.find(".modelType2").hide();
            $form.find(".modelType1").show();
        }else if (type === 2 || type ==='2'){
            $form.find(".modelType2").show();
            $form.find(".modelType1").hide();
        }
    });
    var usernames = '';
    $("#user_manager_datagrid_id").find('tbody input:checkbox:checked').each(function (i, j) {
        j = $(j);
        if (j.val()=='on') {
            return ;
        }
        usernames += j.val() + ",";
    });
    if (usernames) {
        $form.find("select[name='modelType']").val(2).change();
        usernames = usernames.substring(0, usernames.length - 1);
        $form.find('textarea[name="usernames"]').html(usernames);
    }
});
</script>
