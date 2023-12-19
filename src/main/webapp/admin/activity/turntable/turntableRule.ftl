<form action="${adminBase}/turntable/saveAward.do" class="form-submit" closeTabId="turntable_set_rule_id" id="turntable_set_rule_formId_id">
    <div class="panel panel-default"><input type="hidden" name="id" value="${active.id }">
        <div class="panel-heading">
            <span>${active.name} <@spring.message "manager.setting.is"/></span>
        </div>
        <div class="panel-body">
            <table class="table table-bordered">
                <tbody>
                <tr style="background-color: #f5f5f5;">
                    <td width="15%" class="text-center"><@spring.message "admin.config.project"/></td>
                    <td width="35%"><@spring.message "admin.config.val"/></td>
                    <td width="50%"><@spring.message "admin.effect.pic"/></td>
                </tr>
                <tr>
                    <td class="text-center media-middle"><@spring.message "admin.jack.num"/></td>
                    <td class="text-left media-middle"><select class="form-control awardCount"></select></td>
                    <td class="text-left"><div id="awardImgChart" style="height:400px"></div></td>
                </tr>
                </tbody>
            </table>
            <div class="text-center">
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div>
</form>
<script src="${base}/common/lang/${language}.js?v=5"></script>
<script>
requirejs(['${base}/common/js/admin/turntableRule.js?v=2','jquery'],function(setRule){
	var gifts=${gifts},
	awards=${awards};
    setRule.render(gifts,awards);
});
</script>
