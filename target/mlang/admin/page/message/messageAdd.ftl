<form action="${adminBase}/message/doAdd.do" class="form-submit" id="message_add_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.send.message.station"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.receive.person"/>：</td>
                        <td><select name="receiveType" class="form-control">
                            <option value="1" selected><@spring.message "admin.simple.vip"/></option>
                            <option value="2"><@spring.message "admin.point.multi.vip"/></option>
                            <option value="3"><@spring.message "manager.all.send"/></option>
                            <option value="4"><@spring.message "admin.withdraw.table.degreeName"/></option>
                            <option value="5"><@spring.message "admin.vip.group"/></option>
                            <option value="6"><@spring.message "admin.proxy.line.multi"/></option>
                        </select></td>
                    </tr>
                    <tr class="lastLogin">
                        <td class="text-right media-middle"><@spring.message "admin.vip.login.time"/>：</td>
                        <td>
                            <select name="lastLogin" class="form-control">
                                <option value="1"><@spring.message "admin.one.week"/></option>
                                <option value="2"><@spring.message "admin.two.week"/></option>
                                <option value="3" selected><@spring.message "admin.one.month"/></option>
                                <option value="4"><@spring.message "admin.two.month"/></option>
                            </select></td>
                    </tr>
                    <tr class="accountT1">
                        <td class="text-right media-middle"><@spring.message "admin.receive.per"/>：</td>
                        <td><input type="text" class="form-control" name="receiveUsername"/></td>
                    </tr>
                    <tr class="accountT2">
                        <td class="text-right media-middle"><@spring.message "admin.member.table.show"/>：</td>
                        <td><input type="text" placeholder="<@spring.message "admin.eng.string.separate"/>" class="form-control" name="usernames"/></td>
                    </tr>
                    <tr class="accountT4">
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                        <td><select name="degreeId" class="form-control"><#list degrees as le><option value="${le.id}">${le.name}</option></#list></select></td>
                    </tr>
                    <tr class="accountT5">
                        <td class="text-right media-middle"><@spring.message "admin.vip.group"/>：</td>
                        <td><select name="groupId" class="form-control"><#list groups as le><option value="${le.id}">${le.name}</option></#list></select></td>
                    </tr>
                    <tr class="accountT6">
                        <td class="text-right media-middle"><@spring.message "admin.proxy.account.det"/>：</td>
                        <td><input type="text" class="form-control" name="proxyName"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.station.title.info"/>：</td>
                        <td><input type="text" class="form-control" name="title"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.station.content.info"/>：</td>
                        <td><textarea class="form-control" name="content"></textarea></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.is.not.to.show"/>：</td>
                        <td><select name="popStatus" class="form-control"><option value="1" selected><@spring.message "admin.withdraw.info.boolean.no"/></option><option value="2"><@spring.message "admin.withdraw.info.boolean.yes"/></option></select></td>
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
<script type="text/javascript">
requirejs(['jquery'], function () {
    var $form = $("#message_add_form_id");
    $form.on("change", "select[name='receiveType']", function () {
        var type = $(this).val();
        $form.find(".accountT1").hide();
        $form.find(".accountT2").hide();
        $form.find(".accountT4").hide();
        $form.find(".accountT5").hide();
        $form.find(".accountT6").hide();
        $form.find(".lastLogin").hide();
	    if (type != 3) {
	        $form.find(".accountT"+type).show();
	    }
	    if(type>2){
	    	$form.find(".lastLogin").show();
	    }
    });
    $form.find("select[name='receiveType']").change();
});
</script>
