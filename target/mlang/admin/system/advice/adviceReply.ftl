<form action="${adminBase}/adviceFeedback/saveAdviceReply.do" class="form-submit" id="advice_add_form_id">
	<input type="hidden" value="${adviceId}" name="adviceId"/>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.reply.advice"/></h4>
            </div>
            <div class="modal-body" style="max-height: 550px;    min-height: 280px;overflow: auto;">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr style="color: red;">
                        <td class="text-right media-middle" style="width: 20%;"><@spring.message "admin.ques.detail"/>：</td>
                        <td class="text-left">${advcie.content }</td>
                    </tr>
                    <#list adviceList as ad>
                    <#if ad.contentType == 2>
                    <tr style="color: red;">
                        <td class="text-right media-middle" style="width: 20%;">${ad.account }：</td>
                        <td class="text-left">${ad.content }&nbsp;&nbsp;&nbsp;<@spring.message "admin.date.time.clock"/>:${ad.createTime}</td>
                    </tr>
                    <#else>
                    <tr>
                        <td class="text-right media-middle" style="width: 20%;"><@spring.message "admin.me.reply"/>：</td>
                        <td class="text-left">${ad.content }&nbsp;&nbsp;&nbsp;<@spring.message "admin.date.time.clock"/>:${ad.createTime}</td>
                    </tr>
                    </#if>
                    </#list>
                    <tr>
                        <td class="text-right media-middle" style="width: 20%;"><@spring.message "admin.reply.content"/>：</td>
                        <td class="text-left"><textarea class="form-control" name="content"></textarea></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
