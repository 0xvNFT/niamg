<form action="${adminBase}/scoreExchange/doModify.do" class="form-submit" id="scoreExchangeModifyFormId">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.modify.exchange.strategy"/></h4>
            </div>
            <div class="modal-body"><input type="hidden" name="id" value="${se.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.config.types"/>：</td>
                        <td class="text-left"><select name="type" class="form-control">
                            <option value="1"<#if se.type==1>selected</#if>><@spring.message "admin.cash.exchange.scores"/></option>
                            <option value="2"<#if se.type==2>selected</#if>><@spring.message "admin.score.exchange.cash"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.config.account"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="name" value="${se.name }"/></td>
                    </tr>
                    <tr class="hideTr" <#if se.type==1>hidden="hidden"</#if>>
                        <td class="text-right media-middle"><@spring.message "admin.weight.bet.num"/>：</td>
                        <td><input name="betRate" class="form-control money" value="${se.betRate }"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.exchange.scale"/>：</td>
                        <td class="text-left form-inline"><input type="text" name="numerator" maxlength="10" value="${se.numerator }" class="form-control"/>:<input type="text" maxlength="10" value="${se.denominator }"name="denominator" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.single.exchange.val.min"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="minVal" maxlength="10" value="${se.minVal}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.single.exchange.val.max"/>：</td>
                        <td class="text-left"><input type="text" class="form-control" name="maxVal" maxlength="10" value="${se.maxVal }"/></td>
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
	var $form=$("#scoreExchangeModifyFormId"),hideTr = $form.find(".hideTr");;
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