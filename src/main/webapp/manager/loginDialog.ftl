<form method="post" action="${managerBase}/login.do" unReloadTable='true' class="form-submit form-horizontal" id="login_dialog_form_id">
<div class="modal-dialog fui-box">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "admin.index.login.timeout"/></h4>
		</div>
		<div class="modal-body">
			<fieldset>
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i class="glyphicon glyphicon-user red"></i></span>
					<input name="username" type="text" class="form-control" placeholder="<@spring.message "manager.username.input"/>" readonly>
				</div>
				<div class="clearfix"></div><br>
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i class="glyphicon glyphicon-lock red"></i></span>
					<input name="password" type="password" class="form-control required" placeholder="<@spring.message "manager.password.input"/>">
				</div>
				<div class="clearfix"></div><br>
				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i class="glyphicon glyphicon-check red"></i></span>
					<input name="verifyCode" id="loginDialog_verifyCode" class="form-control required" placeholder="<@spring.message "manager.check.code"/>" type="text" minlength="4" maxlength="6">
					<span class="input-group-addon"><img id="verifyImg" src="${managerBase}/logVerifycode.do" border="0" width="89" height="38" alt="<@spring.message "manager.refash.code"/>"></span>
				</div>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary"><@spring.message "manager.logout.name"/></button>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
$(function() {
	var $form=$("#login_dialog_form_id");
	$form.find("[name='username']").val(baseInfo.username);
	$form.data("errorcallback",function(){
		$("#verifyImg").attr("src","${managerBase}/logVerifycode.do?time=" + (new Date()).getTime());
		$("#loginDialog_verifyCode").val("");
	}).data("resultCallback",function(json){
		if(Fui.config.loginSuccessFn){
			Fui.config.loginSuccessFn(json);
		}
	});
	$("#verifyImg").click(function(){
		$(this).attr("src","${managerBase}/logVerifycode.do?time=" + (new Date()).getTime());
		$("#loginDialog_verifyCode").val("");
	}).click();
});
</script>