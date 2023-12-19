<form action="${managerBase}/stationRebate/doModifyProxy.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.update.proxy.points.config"/></h4>
            </div>
            <div class="modal-body"><input type="hidden"name="id" value="${rebate.id}"> 
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "manager.station.point"/>：</td>
                        <td width="30%" class="media-middle">${station.name}[${station.code}]</td>
                        <td width="20%" class="text-right media-middle"><@spring.message "manager.turnback.model"/>：</td>
                        <td><select class="form-control" name="type">
							<option value="1"<#if rebate.type==1>selected</#if>><@spring.message "manager.level.same"/></option><option value="2"<#if rebate.type==2>selected</#if>><@spring.message "manager.level.count"/></option>
						</select></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "manager.back.types"/>：</td>
                        <td><select class="form-control" name="rebateMode">
							<option value="1"<#if rebate.rebateMode==1>selected</#if>><@spring.message "manager.day.back.points"/></option>
							<option value="2"<#if rebate.rebateMode==2>selected</#if>><@spring.message "manager.second.auto.points"/></option>
							<option value="3"<#if rebate.rebateMode==3>selected</#if>><@spring.message "manager.hand.second.points"/></option>
						</select></td>
						<td class="text-right media-middle"><@spring.message "manager.betting.num"/>：</td>
                        <td><input type="text" class="form-control" name="betRate" value="${rebate.betRate}"/></td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "manager.level.distance"/>：</td>
                        <td><input type="text" class="form-control" name="levelDiff" value="${rebate.levelDiff}"/></td>
                        <td class="text-right media-middle"><@spring.message "manager.max.value"/>：</td>
                        <td><input type="text" class="form-control" name="maxDiff" value="${rebate.maxDiff}"/></td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "manager.live.points"/>：</td>
                        <td><input type="text" class="form-control" name="live" value="${rebate.live}"/></td>
                        <td class="text-right media-middle"><@spring.message "manager.egame.points"/>：</td>
                        <td><input type="text" class="form-control" name="egame" value="${rebate.egame}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.chess.points"/>：</td>
                        <td><input type="text" class="form-control" name="chess" value="${rebate.chess}"/></td>
                        <td class="text-right media-middle"><@spring.message "manager.sprots.points"/>：</td>
                        <td><input type="text" class="form-control" name="sport" value="${rebate.sport}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.esports.points"/>：</td>
                        <td><input type="text" class="form-control" name="esport" value="${rebate.esport}"/></td>
                        <td class="text-right media-middle"><@spring.message "manager.fish.points"/>：</td>
                        <td><input type="text" class="form-control" name="fishing" value="${rebate.fishing}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.lottery.points"/>：</td>
                        <td><input type="text" class="form-control" name="lottery" value="${rebate.lottery}"/></td>
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
