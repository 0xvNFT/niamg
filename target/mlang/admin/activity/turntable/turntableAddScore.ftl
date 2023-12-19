<form action="${adminBase}/turntable/doAddScore.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.act.bet"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.act.name"/>：</td>
                        <td width="35%"><input type="text" name="name" class="form-control"/></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.score.consumer"/>：</td>
                        <td width="35%"><input name="score" class="form-control number"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.startTime"/>：</td>
                        <td><div class="input-group">
                            <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="begin" placeholder="<@spring.message "admin.startTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/>：</td>
                        <td><div class="input-group">
                            <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="end" placeholder="<@spring.message "admin.endTime"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.daily.count.num"/>：</td>
                        <td><input name="playCount" class="form-control digits"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.userAvatar.url"/>：</td>
                        <td><input type="text" name="imgPath" class="form-control" /></td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "admin.act.rule"/>：</td>
                        <td colspan="3"><textarea name="activeHelp" class="form-control"></textarea></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.qualifications"/>：</td>
                        <td><textarea name="activeRole" class="form-control"></textarea></td>
                        <td class="text-right media-middle"><@spring.message "admin.act.statement"/>：</td>
                        <td><textarea name="activeRemark" class="form-control"></textarea></td>
                    </tr>
                    <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/></td>
	                    <td colspan="3"><#list degrees as lv><label class="checkbox-inline"><input type="checkbox"  name="degreeIds" value="${lv.id}"  checked>${lv.name}</label></#list></td>
                    </tr>
                    <tr>
	                    <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/></td>
	                    <td colspan="3"><#list groups as lv><label class="checkbox-inline"><input type="checkbox"  name="groupIds" value="${lv.id}"  checked>${lv.name}</label></#list></td>
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