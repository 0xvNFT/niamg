<form action="${adminBase}/turntable/doModifyScore.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content"><input type="hidden" name="id" value="${active.id }">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.round.act"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.act.name"/>：</td>
                        <td width="35%"><input type="text" name="name" class="form-control" value="${active.name }"/></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.score.consumer"/>：</td>
                        <td><input name="score" class="form-control number" value="${active.score }"/></td>
                    </tr>
                    <input type="hidden" name="joinType" value="3">
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td><div class="input-group"><input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${active.beginDatetime}" name="begin" placeholder="<@spring.message "admin.startTime"/>"><span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span></div></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td><div class="input-group"><input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${active.endDatetime}" name="end" placeholder="<@spring.message "admin.endTime"/>"><span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span></div></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.count.num"/>：</td>
                        <td><input name="playCount" class="form-control digits" value="${active.playCount }"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.userAvatar.url"/>：</td>
                        <td><input type="text" name="imgPath" class="form-control" value="${active.imgPath}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.act.rule"/>：</td>
                        <td colspan="3"><textarea name="activeHelp" class="form-control">${active.activeHelp}</textarea></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.qualifications"/>：</td>
                        <td><textarea name="activeRole" class="form-control">${active.activeRole}</textarea></td>
                        <td class="text-right media-middle"><@spring.message "admin.act.statement"/>：</td>
                        <td><textarea name="activeRemark" class="form-control">${active.activeRemark}</textarea></td>
                    </tr>
                    <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/></td>
	                    <td colspan="3"><#list degrees as lv><label class="checkbox-inline"><input type="checkbox"name="degreeIds" value="${lv.id}"<#if degreeSet?seq_contains(lv.id)>checked</#if>>${lv.name}</label></#list></td>
                    </tr>
                    <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/></td>
	                    <td colspan="3"><#list groups as lv><label class="checkbox-inline"><input type="checkbox"name="groupIds" value="${lv.id}"<#if groupSet?seq_contains(lv.id)>checked</#if>>${lv.name}</label></#list></td>
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
