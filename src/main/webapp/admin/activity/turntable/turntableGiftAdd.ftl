<form action="${adminBase}/turntableGift/doAdd.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.round.jack"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="35%" class="text-right media-middle"><@spring.message "admin.jack.name"/>：</td>
                        <td class="text-left"><input type="text" name="productName" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td width="35%" class="text-right media-middle"><@spring.message "admin.jack.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="productImg" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.value"/>：</td>
                        <td class="text-left"><input type="text" name="price" class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.detail"/>：</td>
                        <td class="text-left"><textarea name="productDesc" class="form-control"></textarea></td>
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