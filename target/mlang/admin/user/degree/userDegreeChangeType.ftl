<form action="${adminBase}/userDegree/doChangeType.do" class="form-submit" id="userDegree_changeType_formId">
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.degree.changeTypeTitle"/></h4>
		</div>
		<div class="modal-body" style="max-height: 700px;overflow: auto;">
			<table class="table table-bordered table-striped">
				<tbody>
					<tr>
						<td width="30%" class="text-right media-middle"><@spring.message "admin.mode"/>：</td>
						<td colspan="2">
							<label class="radio-inline"><input type="radio" name="type" <#if defaultDegree.type == 1>checked</#if> value="1" dirname="<@spring.message "admin.depositMoney"/>"/><@spring.message "admin.depositMoney"/></label>
							<label class="radio-inline"><input type="radio" name="type" <#if defaultDegree.type == 2>checked</#if> value="2" dirname="<@spring.message "admin.betNum"/>"/><@spring.message "admin.betNum"/></label>
							<label class="radio-inline"><input type="radio" name="type" <#if defaultDegree.type == 3>checked</#if> value="3" dirname="<@spring.message "admin.depositMoneyAdnBetNum"/>"/><@spring.message "admin.depositMoneyAdnBetNum"/></label>
						</td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "admin.name"/></td>
						<td class="text-center depositMoneyTitle" <#if defaultDegree.type == 2>style="display: none;" </#if> ><@spring.message "admin.depositMoney"/></td>
						<td class="text-center betNumTitle" <#if defaultDegree.type == 1>style="display: none;" </#if> ><@spring.message "admin.betNum"/></td>
					</tr>
					<#list allDegree as level>
						<tr>
							<td class="text-right media-middle">${level.name}：</td>
							<td <#if defaultDegree.type == 2>style="display: none;" </#if>><input name="depositMoney_${level.id}" value="${level.depositMoney}" class="form-control formDepsoit" placeholder="<@spring.message "admin.depositMoney"/>"/></td>
							<td <#if defaultDegree.type == 1>style="display: none;" </#if>><input name="betNum_${level.id}" value="${level.betNum}" class="form-control formBetNum" placeholder="<@spring.message "admin.betNum"/>"/></td>
						</tr>
					</#list>
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
<script type="text/javascript">
	$(function(){
		var $form=$("#userDegree_changeType_formId"),dirname;
		$form.find("input[name='type']").on("click",function(){
            var val = $(this).val();
            dirname = $(this).attr("dirname");
            if(val === "1"){
                $form.find(".formDepsoit").parent().show();
                $form.find(".formBetNum").parent().hide();
                $form.find(".depositMoneyTitle").show().siblings('.betNumTitle').hide();
            }else if(val === "2"){
                $form.find(".formDepsoit").parent().hide();
                $form.find(".formBetNum").parent().show();
                $form.find(".depositMoneyTitle").hide().siblings('.betNumTitle').show();
            }else{
                $form.find(".formDepsoit").parent().show();
                $form.find(".formBetNum").parent().show();
                $form.find(".depositMoneyTitle").show().siblings('.betNumTitle').show();
            }
		});
		$form.find("button.btn-primary").on("click",function(){
		    if(dirname) {
                $("body").find("div.messageTs").find("span.text-danger").text(Admin.degreeTypeTip.replace('{0}', dirname ));
            }
		})
	})
</script>