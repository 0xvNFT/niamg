<form action="${adminBase}/googleAuth/save.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.googleValid9"/></h4>
            </div>
            <div class="modal-body" style="max-height: 700px; min-height: 380px;overflow: auto;">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "manager.admin.account"/>：</td>
                        <td><dl style="margin-bottom:2px"><#list userGroupMap?keys as key>
                        		 <dt>${key}:</dt><dd style="margin-left:20px"><#list userGroupMap["${key}"] as u>
                        			<label class="checkbox-inline<#if hadSet?seq_contains("${u}")> disabled bg-danger</#if>">
								  <input type="checkbox" name="username" value="${u}"<#if hadSet?seq_contains("${u}")> disabled</#if>> ${u}
								</label>
                        		</#list></dd></#list></dl>
                        		<div style="color:red"><@spring.message "admin.googleValid10"/></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td><input name="remark" class="form-control" type="text"></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="media-middle text-center">
                            <img style="width:200px;height:200px" src="${adminBase}/googleAuth/getImage.do?v=${nowTime}" class="img-thumbnail">
                            <br><span ><@spring.message "manager.account.number"/>：International-${stationCode}&nbsp;&nbsp;<@spring.message "admin.key"/>：${key}</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>