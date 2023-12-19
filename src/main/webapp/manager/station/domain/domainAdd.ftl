<form action="${managerBase}/stationDomain/add.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.station.domain.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "manager.station.point"/>：</td>
                        <td width="30%"><select class="form-control" name="stationId">
                            <option value=""><@spring.message "manager.all.station"/></option>
							<#list stations as s><option value="${s.id}">${s.name}</option></#list>
                        </select></td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
                        <td><select class="form-control" name="type">
                            <option value="1" selected><@spring.message "manager.common.use"/></option>
                            <option value="2"><@spring.message "manager.station.admin.back"/></option>
                            <option value="3"><@spring.message "manager.proxy.per.admin"/></option>
                            <option value="4"><@spring.message "manager.font"/></option>
                            <option value="5"><@spring.message "manager.app.domain"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.domain"/>：</td>
                        <td colspan="2"><textarea name="domains" class="form-control" rows="3"></textarea></td>
                        <td><@spring.message "manager.data.format.domain.account"/><br/><@spring.message "manager.domain.proxy.account"/><br/><@spring.message "manager.domain.not.proxy.account"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.default.proxy"/>：</td>
                        <td><input type="text" class="form-control" name="proxyUsername"/></td>
                        <td class="text-right media-middle"><@spring.message "manager.default.main"/>：</td>
                        <td><input type="text" class="form-control" name="defaultHome"/></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "manager.default.proxy.per"/>：</td>
						<td><input type="text" class="form-control" name="agentName"/></td>
						<td class="text-right media-middle"><@spring.message "manager.get.ip.model"/>：</td>
						<td>
							<label class="radio-inline"><input type="radio" name="ipMode" value="1" checked><@spring.message "manager.safe.pattern"/></label>
							<label class="radio-inline"><input type="radio" name="ipMode" value="2"><@spring.message "manager.common.pattern"/></label>
						</td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
                        <td colspan="3"><input type="text" class="form-control" name="remark"/></td>
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
<script>
    $(function () {
        var stationId = $("#choseStationId").val();
        if(stationId) {
            $("select[name='stationId']").val(stationId);
        }
    });
</script>
