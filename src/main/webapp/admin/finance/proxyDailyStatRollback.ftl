<form action="${adminBase}/proxyDailyStat/doRollback.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header"><input type="hidden" name="id" value="${stat.id}"/>
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.hand.proxy.back.point"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <th colspan="3" class="text-center media-middle"><@spring.message "admin.proxy"/>：${stat.username} <@spring.message "admin.at"/> ${date} <@spring.message "admin.back.point.total"/></td>
                    </tr>
                    <tr>
                        <td width="25%" class="text-right media-middle"><@spring.message "admin.lottery.record"/>：</td>
                        <td width="25%" class="media-middle">${stat.lotBet}</td>
                        <td><input type="text" class="form-control money" name="lotRollback" value="${stat.lotRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.live.record"/>：</td>
                        <td class="media-middle">${stat.realBet}</td>
                        <td><input type="text" class="form-control money" name="realRollback" value="${stat.realRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.egame.record"/>：</td>
                        <td class="media-middle">${stat.egameBet}</td>
                        <td><input type="text" class="form-control money" name="egameRollback" value="${stat.egameRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.chess.record"/>：</td>
                        <td class="media-middle">${stat.chessBet}</td>
                        <td><input type="text" class="form-control money" name="chessRollback" value="${stat.chessRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.sports.record"/>：</td>
                        <td class="media-middle">${stat.sportBet}</td>
                        <td><input type="text" class="form-control money" name="sportRollback" value="${stat.sportRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.power.record"/>：</td>
                        <td class="media-middle">${stat.esportBet}</td>
                        <td><input type="text" class="form-control money" name="esportRollback" value="${stat.esportRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.fish.record"/>：</td>
                        <td class="media-middle">${stat.fishingBet}</td>
                        <td><input type="text" class="form-control money" name="fishingRollback" value="${stat.fishingRollback}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.found.need.weight"/>：</td>
                        <td colspan="2"><input type="text" class="form-control money" name="drawNum" value="${stat.drawNum}"/></td>
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