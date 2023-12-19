<form action="${adminBase}/dummyData/saveWinData.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.insert.random.data"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.random.count"/>：</td>
                        <td class="text-left"><input name="generateNum" class="form-control" type="number"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.begin.time"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control fui-date required"name="winTimeStr" placeholder="<@spring.message "admin.jack.begin.time"/>" format="YYYY-MM-DD HH:mm:ss">
                                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.end.time"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control fui-date required"name="winTimeEnd" placeholder="<@spring.message "admin.jack.end.time"/>" format="YYYY-MM-DD HH:mm:ss">
                                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.type.win"/>：</td>
                        <td>
                            <select name="type" class="form-control" id="selectId">
                                <option value="1" selected><@spring.message "admin.red.jack.data"/></option>
                                <option value="2"><@spring.message "admin.round.jack.data"/></option>
                                <option value="3"><@spring.message "admin.invite.jack.data"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr class="tr1">
                        <td class="text-right media-middle"><@spring.message "admin.random.cash.period"/>：</td>
                        <td class="text-left">
                            <input name="winMoney" placeholder="<@spring.message "admin.min.cash.count"/>" class="form-control" type="number" style="width: 40%;display: initial;"> --
                            <input name="winMoneyMax" placeholder="<@spring.message "admin.max.cash.count"/>" class="form-control" type="number" style="width: 40%;display: initial;">
                        </td>
                    </tr>
                    <tr class="tr1">
                        <td class="text-right media-middle"><@spring.message "admin.jack.name"/>：</td>
                        <td class="text-left"><input name="itemName" class="form-control" type="text"></td>
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
<script>
    requirejs(['jquery'], function () {
        $("#selectId").change(function (){
            var options=$("#selectId");
            var value = options.val();
            if (value==2){
                $('.tr1').hide();
            }else{
                $('.tr1').show();
            }
        });

    });
</script>
