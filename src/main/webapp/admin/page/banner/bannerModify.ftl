<form action="${adminBase}/banner/modifySave.do" class="form-submit" id="bannerModifyFormId">
    <div class="modal-dialog modal-lg"><input name="id" type="hidden" value="${banner.id}">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.turn.pic"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                    	<td width="20%" class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td width="30%"><input type="text" name="title"value=" ${banner.title }" class="form-control"/></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "manager.language.types"/>：</td>
                        <td><select name="language" class="form-control">
		                    <option value=""><@spring.message "admin.language.all.types"/></option>
		                    <#list languages as g><option value="${g.name()}"<#if banner.language==g.name()>selected</#if>>${g.desc }</option></#list>
		                </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.turn.types"/>：</td>
                        <td>
                            <select name="code" class="form-control">
                                <option value="1"<#if banner.code==1>selected</#if>><@spring.message "admin.front.turn.pic"/></option>
                                <option value="5"<#if banner.code==5>selected</#if>><@spring.message "admin.mobile.turn"/></option>
                                <option value="2"<#if banner.code==2>selected</#if>><@spring.message "admin.head.pic"/></option>
                                <option value="3"<#if banner.code==3>selected</#if>><@spring.message "admin.news.pic"/></option>
                                <option value="4"<#if banner.code==4>selected</#if>><@spring.message "admin.gps.pic"/></option>
                               <#if onOffwebpayGuide> <option value="8"<#if banner.code==8>selected</#if>><@spring.message "admin.online.pay.pic"/></option>
                                <option value="6"<#if banner.code==6>selected</#if>><@spring.message "admin.quickly.pay.pic"/></option>
                                <option value="7"<#if banner.code==7>selected</#if>><@spring.message "admin.bank.pay.pic"/></option></#if>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td><input type="number" name="sortNo" value="${banner.sortNo}" class="form-control digits"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.title.pic.address"/>(520x200)：</td>
                        <td><input type="text" name="titleImg" class="form-control"value="${banner.titleImg }"/></td>
                        <td class="text-right media-middle" id="producttxt"><#if banner.code==8||banner.code==7||banner.code==6><@spring.message "admin.pay.overview"/>：<#else><@spring.message "admin.title.link.address"/>：</#if></td>
                        <td><input type="text" name="titleUrl" class="form-control" value="${banner.titleUrl }"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control fui-date" name="updateTime"
                                       value="${banner.updateTime}" placeholder="<@spring.message "admin.startTime"/>"> <span
                                    class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td>
                            <div class="input-group">
                                <input type="text" class="form-control fui-date" name="overTime"
                                       value="${banner.overTime}" placeholder="<@spring.message "admin.endTime"/>"> <span
                                    class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                            </div>
                        </td>
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
	var $form=$("#bannerModifyFormId");
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