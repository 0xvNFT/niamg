<form action="${adminBase}/scoreExchange/doAdd.do" class="form-submit" id="scoreExchangeAddFormId">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.insert.exchange.strategy"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.config.types"/>：</td>
                        <td class="text-left"><select name="type" class="form-control">
                            <option value="1" selected><@spring.message "admin.cash.exchange.scores"/></option>
                            <option value="2"><@spring.message "admin.score.exchange.cash"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.config.account"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="name"/></td>
                    </tr>
                    <tr class="hideTr" hidden="hidden">
                        <td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
                        <td><input name="betRate" class="form-control money" value="1"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.exchange.scale"/>：</td>
                        <td class="text-left form-inline"><input type="text" name="numerator" class="form-control"maxlength="10"/>:<input type="text"maxlength="10" name="denominator" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.single.exchange.val.min"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="minVal" maxlength="10"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.single.exchange.val.max"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="maxVal" maxlength="10"/></td>
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
requirejs(['jquery'],function(){
	var $form=$("#scoreExchangeAddFormId"),hideTr = $form.find(".hideTr");;
	$form.find("select[name='type']").change(function() {
		var value = $(this).val();
		if (value == 1) {
			hideTr.hide();
		} else {
			hideTr.show();
		}
	});
});
</script>