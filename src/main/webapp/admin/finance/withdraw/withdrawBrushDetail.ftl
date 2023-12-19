<div class="modal-dialog modal-lg">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
            <h4 class="modal-title">${brush.brushName}</h4>
        </div>
        <div class="modal-body" style="max-height: 800px; min-height: 380px;overflow: auto;">
            <table class="table table-bordered table-striped" style="word-break:break-all; word-wrap:break-all;">
                    <tbody>
                        <#if brush.brushType==1>
                          <tr>
                                  <th width="15%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                  <td width="36%">${brush.startTime}——${brush.endTime}</td>
                                  <th  width="25%" class="text-right"><@spring.message "admin.pay.all.money"/>:<br/><@spring.message "admin.all.draw.money"/>:</th>
                                  <td>${brush.totalDeposit} <br/> ${brush.totalDraw}</td>
                          </tr>
                          <tr>
                              <th class="text-right"><@spring.message "admin.withdraw.info.drawdepositsub"/>:</th>
                              <td>${brush.diff}</td>
                              <th class="text-right"><@spring.message "admin.withdraw.info.morethan"/>:</th>
                              <td>${brush.standard}</td>
                          </tr>
                        </#if>
                        <#if brush.brushType==2>
                            <tr>
                                <th class="text-right"><@spring.message "admin.withdraw.info.thiswithdrawal"/>:</th>
                                <td>${brush.singleDraw}</td>
                                <th class="text-right"><@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td>${brush.standard}</td>
                            </tr>
                        </#if>
                        <#if brush.brushType==3>
                            <tr>
                                <th width="10%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                <td width="35%">${brush.startTime}——${brush.endTime}</td>
                                <th  width="23%" class="text-right"><@spring.message "admin.withdraw.info.sameip"/>:<br/><@spring.message "admin.withdraw.info.samesys"/>:</th>
                                <td>${brush.drawIp} <br/> ${brush.drawOs}</td>
                            </tr>
                            <tr>
                                <th></th>
                                <td></td>
                                <th class="text-right"><@spring.message "admin.draw.all.per.total"/>:<br/><@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td>${brush.relationCount} <br/> ${brush.standard}</td>
                            </tr>
                        </#if>
                        <#if brush.brushType==4>
                            <tr>
                                <th width="15%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                <td width="35%">${brush.startTime}——${brush.endTime}</td>
                                <th  width="25%" class="text-right"><@spring.message "admin.withdraw.info.sameip"/>:</th>
                                <td>${brush.drawIp}</td>
                            </tr>
                            <tr>
                                <th></th>
                                <td></td>
                                <th class="text-right"><@spring.message "admin.draw.all.per.total"/>:<br/><@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td >${brush.relationCount} <br/> ${brush.standard}</td>
                            </tr>
                        </#if>
                        <#if brush.brushType==5>
                            <tr>
                                <th width="10%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                <td width="35%">${brush.startTime}——${brush.endTime}</td>
                                <th width="20%" class="text-right"><@spring.message "admin.pay.all.count"/>:<br/><@spring.message "admin.withdraw.info.lessthan"/>:</th>
                                <td width="10%">${brush.relationCount} <br/> ${brush.standard}</td>
                            </tr>
                        </#if>
                        <#if brush.brushType==6>
                            <tr>
                                <th width="10%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                <td width="35%">${brush.startTime}——${brush.endTime}</td>
                                <th width="15%" class="text-right"><@spring.message "admin.withdraw.info.thirdprofit"/>:<br/><@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td width="10%">${brush.totalMoney} <br/> ${brush.standard}</td>
                            </tr>
                        </#if>
                        <#if brush.brushType==7>
                            <tr>
                                <th width="15%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                <td width="36%">${brush.startTime}——${brush.endTime}</td>
                                <th  width="25%" class="text-right"><@spring.message "admin.withdraw.info.invitecount"/>:<br/><@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td>${brush.inviteUserCount} <br/> ${brush.standard}</td>

                            </tr>
                            <tr>
                                <th></th>
                                <td></td>
                                <th class="text-right"><@spring.message "admin.withdraw.info.depositrate"/>:<br/><@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td>${brush.depositRate} <br/> ${brush.totalMoney}</td>
                            </tr>
                        </#if>
                        <#if brush.brushType==8>
                            <tr>
                                <th width="25%" class="text-right"><@spring.message "admin.withdraw.info.period"/>:</th>
                                <td width="35%">${brush.startTime}——${brush.endTime}</td>
                                <th  width="30%" class="text-right"><@spring.message "admin.withdraw.info.invitecount"/>:</th>
                                <td>${brush.inviteUserCount}</td>
                            </tr>
                            <tr>
                                <th class="text-right"><@spring.message "admin.pay.all.money"/>:<br/><@spring.message "admin.all.draw.money"/>:</th>
                                <td>${brush.totalDeposit} <br/> ${brush.totalDraw}</td>
                                <th class="text-right"><@spring.message "admin.withdraw.info.depositdrawsub"/>:</th>
                                <td>${brush.diff}</td>
                            </tr>
                            <tr>
                                <th class="text-right"><@spring.message "admin.withdraw.info.proxymoney"/>:</th>
                                <td>${brush.totalMoney}</td>
                                <th class="text-right"><@spring.message "admin.withdraw.info.sub"/>: <br/> <@spring.message "admin.withdraw.info.morethan"/>:</th>
                                <td>${brush.depositRate} <br/> ${brush.standard}</td>
                                <!--当brushType类型为8时,depositRate是  当前登录会员的推荐会员的充值总额 减去 (下级的打码返点+下级存款返佣+下级注册返佣总额)-->
                            </tr>
                        </#if>
                </tbody>
            </table>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
        </div>
    </div>
</div>