<form action="${adminBase}/dummyData/doAdd.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.jack.add.new.data"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.username.input"/>：</td>
                        <td class="text-left"><input name="username" class="form-control" type="text"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.win.date"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control fui-date required" name="winTimeStr" placeholder="<@spring.message "admin.jack.win.date"/>" format="YYYY-MM-DD HH:mm:ss">
                                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.type.win"/>：</td>
                        <td>
                            <select name="type" class="form-control">
<#--                                <option value="1" selected><@spring.message "admin.red.jack.data"/></option>-->
                                <option value="2"><@spring.message "admin.round.jack.data"/></option>
                                <option value="3"><@spring.message "admin.invite.jack.data"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.cash.money.paper"/>：</td>
                        <td class="text-left"><input name="winMoney" class="form-control" type="number"></td>
                    </tr>
                    <tr>
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
