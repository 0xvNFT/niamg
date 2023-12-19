<style>
    .need-copy{float: right;cursor: pointer}
</style>
<form id="memdrawrd_hand_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.withdraw.table.handle"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${draw.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="17%" class="text-right"><@spring.message "admin.deposit.handle.username" />：</td>
                        <td width="30%"><span>${draw.username }(${draw.degreeNmae}) </span><span
                                    class="label label-primary need-copy"
                                    data-clipboard-text="${draw.usernmae}"><@spring.message "admin.withdraw.copy"/></span>
                            <br/><span class="text-danger">&nbsp;&nbsp;余额：${account.money}</span>
                        </td>
                        <td width="17%" class="text-right"><@spring.message "admin.user.info.createDate"/>：</td>
                        <td width="30%"><#if createDate lte 3 >
                            <span class="text-danger">
                        </#if>
                                <#if createDate lte 7 && createDate gt 3 >
							<span class="text-warring">
                            </#if>
                                <#if createDate gt 7 >
							<span class="text-default">
                            </#if>
                                ${account.createDatetime}（${account.createDate}<@spring.message "admin.unit.day"/>）
                            <span></td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.withdraw.table.realName"/>：</td>
                        <td class="text-left copy-btn-wrap">
                            <span>${draw.realName }</span>
                            <span class="label label-primary need-copy" data-clipboard-text="${draw.realName}"><@spring.message "admin.withdraw.copy"/></span>
                            <#if nameUpdated>
                                <div style="color:red"><@spring.message "admin.withdraw.info.realname.not.same"/></div>
                            </#if>
                        </td>
                        <td class="text-right"><@spring.message "admin.withdraw.info.bankname"/>：</td>
                        <td>${draw.bankName}(${bankInfo.bankAddress})</td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.withdraw.info.cardNo"/>：</td>
                        <td class="text-left copy-btn-wrap">
                            <#if bankInfo.status == 2>
                                <span>${draw.cardNo }</span>
                            </#if>
                            <#if bankInfo.status == 1>
                                <span class="label label-danger" id="cardNoStatus_span"><@spring.message "admin.withdraw.info.card.ban"/></span>
                                <span style="color:red">${draw.cardNo }</span>
                            </#if>
                            <span class="label label-primary need-copy" data-clipboard-text="${draw.cardNo }"><@spring.message "admin.withdraw.copy"/></span>
                            <#if cardNoUpdated>
                                <div style="color:red"><@spring.message "admin.withdraw.info.card.not.same"/></div>
                            </#if>
                        </td>
                        <td class="text-right"><@spring.message "admin.withdraw.info.date"/>：</td>
                        <td>${draw.createDatetime}</td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.withdraw.info.drawNeed"/>：</td>
                        <td>${account.drawNeed }</td>
                        <td class="text-right"><@spring.message "admin.withdraw.info.betNum"/>：</td>
                        <td>${account.betNum }</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.info.real.money"/>：</td>
                        <td class="text-left copy-btn-wrap">
                            <span class="text-danger h4">${draw.trueMoney }（<@spring.message "admin.deposit.table.money.unit"/>）</span>
                            <span class="label label-primary need-copy"
                                  data-clipboard-text="${draw.trueMoney }"><@spring.message "admin.withdraw.copy"/></span>
                        </td>
                        <td class="text-right media-middle" style="font-size: 1.8rem;"><@spring.message "admin.withdraw.info.boolean"/>：</td>
                        <td class="media-middle" style="font-size: 1.8rem;">
                            <#if account.drawNeed lte account.betNum>
                                <span class="text-success"><@spring.message "admin.withdraw.info.boolean.yes"/></span>
                            </#if>
                            <#if account.drawNeed gt account.betNum>
                                <span class="text-danger"><@spring.message "admin.withdraw.info.boolean.no"/></span>
                            </#if>
<#--                            (<@spring.message "admin.withdraw.info.lotteryBetAmount"/>：${todayDate.lotteryBetAmount};-->
<#--                            <@spring.message "admin.withdraw.info.lotteryWinAmount"/>：${todayDate.lotteryWinAmount};-->
<#--                            <@spring.message "admin.withdraw.info.real.win"/>：${todayDate.lotteryWinAmount - todayDate.lotteryBetAmount})-->

                        </td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.withdraw.info.firsttime"/>：</td>
                        <td>${firstWithdrawTime }</td>
                        <td class="text-right"><@spring.message "admin.withdraw.info.firstmoney"/>：</td>
                        <td>${firstWithdrawMoney } （<@spring.message "admin.deposit.table.money.unit"/>）</td>
                    </tr>
                    <tr>
                        <#if onOffDrawFee>
                            <td class="text-right media-middle"><@spring.message "admin.withdraw.info.feemoney"/>：</td>
                            <td class="text-left copy-btn-wrap">
                                <span class="text-danger h4">${draw.feeMoney }（<@spring.message "admin.deposit.table.money.unit"/>）</span>
                            </td>
                        </#if>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.info.drawMoney"/>：</td>
                        <td class="text-left copy-btn-wrap" <#if !onOffDrawFee> colspan="3"</#if>>
                            <span class="text-danger h4">${draw.drawMoney }（<@spring.message "admin.deposit.table.money.unit"/>）</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.info.money.win"/>：</td>
                        <td class="text-left copy-btn-wrap" colspan="3">
                            <span> <@spring.message "admin.deposit"/>：${depositData.totalMoney!0 }<@spring.message "admin.deposit.table.money.unit"/>（${depositData.times!0}<@spring.message "admin.unit.ci"/>）
                            - <@spring.message "admin.withdraw"/>：${drawData.totalMoney!0 }<@spring.message "admin.deposit.table.money.unit"/>（${drawData.times!0 }<@spring.message "admin.unit.ci"/>） =
                                    <#if depositData.totalMoney gte drawData.totalMoney>
                                        <span class="text-success h4">${(depositData.totalMoney!0)-(drawData.totalMoney!0)}<@spring.message "admin.deposit.table.money.unit"/></span>
                                        <#else>
                                        <span class="text-danger h4">${(depositData.totalMoney!0)-(drawData.totalMoney!0)}<@spring.message "admin.deposit.table.money.unit"/></span>
                                    </#if>
                            </span>
                        </td>

                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.manaul.money"/>：</td>
                        <td class="text-left copy-btn-wrap">
<#--                            <span class="text-danger h4">${dailyData.depositHandlerArtificial}（${dailyData.depositHandlerArtificialTimes }<@spring.message "admin.unit.ci"/>）</span>-->
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.info.last.money.sum"/>：</td>
                        <td class="media-middle">
                            ${lastToNowData.amount}（${lastToNowData.time}）<@spring.message "admin.unit.ci"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.info.remark"/>：</td>
                        <td class="text-danger"><@spring.message "admin.withdraw.info.member"/>：${account.remark}<br><@spring.message "admin.withdraw.info.bankcard"/>：${bankInfo.remarks}</td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.result"/>：</td>
                        <td><select name="status" class="form-control">
                                <option value="2" selected><@spring.message "admin.deposit.handle.status.success"/></option>
                                <option value="3"><@spring.message "admin.deposit.handle.status.fail"/></option>
                            </select></td>
                    </tr>
                    <#if onOffReceiptPwd>
                        <tr>
                            <td class="text-right"><@spring.message "admin.deposit.handle.password"/>：</td>
                            <td colspan="3">
                                <input name="password" class="form-control" type="password" required/>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.info.result"/>：</td>
                        <td colspan="3"><input name="remark" class="form-control"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <#if notDraw><span class="text-danger" style="float: left;"><@spring.message "admin.withdraw.lasttime"/>(<font
                            color="black">${lastDrawTime}</font>)</span></#if>
                <button class="btn btn-danger open-tab fui-close" type="button" data-refresh="true" title="<@spring.message "admin.member.info"/>"
                        url="${proxyBase}/accData/index.do?username=${draw.username }&begin=${lastDrawTime}&end=${curDateTime}">
                    <@spring.message "admin.member.info.betnum"/>
                </button>
                <button type="button" class="btn btn-default fui-close" id="esc-close"><@spring.message "admin.deposit.handle.close"/></button>
                <button class="btn btn-primary" id="withHandleDrawSubmit"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
<script>
    requirejs(['clipboard', 'jquery', 'bootstrap', 'Fui'], function (Clipboard) {
        var $form = $("#memdrawrd_hand_form_id");
        $form.on("change", "[name='status']", function () {
            if ($(this).val() == 3) {
                $form.find(".eremark").removeClass("hidden");
            } else {
                $form.find(".eremark").addClass("hidden");
            }
        });
        $form.submit(function () {
            $("#withHandleDrawSubmit").attr("disabled",true);
            var cardNoStatus = '${cardStatus}';
            var cardNo = '${draw.cardNo }';

            if (cardNoStatus && cardNoStatus == '1') {
                layer.confirm('<@spring.message "admin.member.info.confirm1"/>' + cardNo + '<@spring.message "admin.member.info.confirm2"/>', {
                    btn: ['<@spring.message "admin.member.info.continue"/>', '<@spring.message "admin.member.info.cancle"/>']
                }, function () {
                    confirmCommit();
                });
            } else {
                confirmCommit();
            }
            return false;
        });

        function confirmCommit() {
            $.ajax({
                url: "${proxyBase}/finance/withdraw/doHandle.do",
                data: $form.serialize(),
                type: 'POST',
                success: function (result) {
                    if (result.success) {
                        layer.closeAll();
                        layer.msg("<@spring.message "admin.deposit.handle.save.ok"/>！");
                        var $table = $(".fui-box.active").data("bootstrapTable");
                        if ($table && $table.length) {
                            $table.bootstrapTable('refresh');
                        }
                    } else {
                        $("#withHandleDrawSubmit").removeAttr("disabled");
                        layer.msg(result.msg);
                    }
                }
            });
        }

        //初始化复制按钮
        $('.need-copy').click(function () {
            var needCopyBtns = $('.need-copy');
            if ($('.need-copy').length <= 0) return;
            var lens = needCopyBtns.length;
            for (var i = 0; i < lens; i++) {
                var cb = new Clipboard(needCopyBtns[i]);
                cb.on('success', function (e) {
                    layer.msg('<@spring.message "admin.deposit.table.copy.success"/>！');//, 复制内容: ' + e.text);
                    e.clearSelection();
                });
            }
        });

        $(window).keyup(function (event) {
            //按下Esc健要执行的方法
            if(event.keyCode == 27){
                $("#esc-close").click();
            }
        });
    });
</script>
