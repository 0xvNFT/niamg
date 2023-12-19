<form action="${adminBase}/banner/addSave.do" class="form-submit" id="bannerAddFormId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.turn.pic"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="30%"><input type="text" name="title" class="form-control"/></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "manager.language.types"/>：</td>
                        <td><select name="language" class="form-control">
		                    <option value=""><@spring.message "admin.language.all.types"/></option>
		                    <#list languages as l>
                                <option value="${l.locale.language}">${l.lang }</option>
                            </#list>
		                </select></td>
                    </tr>
                    <tr>
                        <td  class="text-right media-middle"><@spring.message "admin.turn.types"/>：</td>
                        <td>
                            <select name="code" class="form-control">
                                <option value="1"><@spring.message "admin.front.turn.pic"/></option>
                                <option value="5"><@spring.message "admin.mobile.turn"/></option>
                                <option value="2"><@spring.message "admin.head.pic"/></option>
                                <option value="3"><@spring.message "admin.news.pic"/></option>
                                <option value="4"><@spring.message "admin.gps.pic"/></option>
                                <#if onOffwebpayGuide><option value="8"><@spring.message "admin.online.pay.pic"/></option>
                                <option value="6"><@spring.message "admin.quickly.pay.pic"/></option>
                                <option value="7"><@spring.message "admin.bank.pay.pic"/></option></#if>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td><input type="number" name="sortNo" class="form-control digits"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.title.pic.address"/>(520x200)：</td>
                        <td><input type="text" name="titleImg" class="form-control"/></td>
                        <td class="text-right media-middle" id="producttxt"><@spring.message "admin.title.link.address"/>：</td>
                        <td><input type="text" name="titleUrl" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td><div class="input-group">
                            <input type="text" class="form-control fui-date" value="${beginDate }" name="updateTime" placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td><div class="input-group">
                            <input type="text" class="form-control fui-date" name="overTime" value="${endDate }" placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
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
$(function(){
	var $form=$("#bannerAddFormId");
	$form.find("select[name='code']").change(function(j){
		var v = $(this).val();
		if(v == 6 ||v == 7||v == 8){
			$form.find('#producttxt').html('<@spring.message "admin.pay.overview"/>：');
			$form.find('input[name=titleUrl]').attr('placeholder','<@spring.message "admin.subject.not.long.dis"/>');
		}else{
			$form.find('#producttxt').html('<@spring.message "admin.title.link.address"/>：');
			$form.find('input[name=titleUrl]').attr('placeholder','');
		}
	});
})
</script>