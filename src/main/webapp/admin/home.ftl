<#if permAdminFn.hadPermission("admin:indexData")>
    <div class="col-md-8">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><@spring.message "admin.all.game.total"/>：</h3>
                <a href="${adminBase}/home.do" class="open-tab" id="home" title="<@spring.message "admin.main.page"/>" data-unbreadcrumb="true">
                    <i class="glyphicon glyphicon-refresh pflush"></i>
                </a>
            </div>
            <table class="table table-striped table-bordered">
                <tr>
                    <td class="text-right"><@spring.message "admin.deposit.table.people.num"/></td>
                    <td><a class="open-tab" title="<@spring.message "admin.menu.user.manager"/>" 
                           href="/admin/globalReport/index.do" id="userDrawId"></a></td>
                    <td class="text-right"><@spring.message "base.regisVipNum"/></td>
                    <td><a class="open-tab" title="<@spring.message "admin.menu.user.manager"/>"
                           href="/admin/user/index.do" id="depositId"></a></td>
                    <td class="text-right"><@spring.message "admin.today.bet.people.num"/></td>
                    <td><a class="open-tab" title="<@spring.message "admin.menu.globalReport"/>"
                           href="/admin/globalReport/index.do" id="betUserNumId"></a></td>
                    <td class="text-right"><@spring.message "base.realPayCount"/></td>
                    <td><a class="open-tab" title="<@spring.message "base.stationMemberRecord"/>"
                           href="/admin/finance/deposit/index.do" id="depositUserId"></a></td>
                    <td class="text-right"><@spring.message "admin.today.withdrawals.num"/>：</td>
                    <td><a class="open-tab" title="<@spring.message "base.stationMemberDrawRec"/>"
                           href="/admin/finance/withdraw/index.do" id="drawUserNumId"></a></td>
                    <td class="text-right"><@spring.message "admin.today.max.noline.num"/></td>
                    <td><a id="today-highest-online-num" class="open-tab" title="<@spring.message "admin.today.max.noline.analytics"/>"
                           href="/admin/globalReport/index.do"></a></td>
                </tr>
            </table>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default ">
                <div class="panel-heading">
                    <h3 class="panel-title"><@spring.message "admin.threeday.users.statistics"/>：</h3>
                </div>
                <div id="memberReportChart" style="height: 360px"></div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><@spring.message "admin.threeday.bet.statistics"/>：</h3>
                </div>
                <div id="betReportChart" style="height: 360px"></div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title"><@spring.message "admin.threeday.financial.statistics"/>：</h3></div>
                <div id="financeReportChart" style="height: 360px"></div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title"><@spring.message "admin.threeday.winorlose.statistics"/>：</h3></div>
                <div id="winLoseReportChart" style="height: 360px"></div>
            </div>
        </div>
    </div>
    <#--<script src="${base}/common/js/admin/home.js?v=3.6"></script>-->
    <script src="${base}/common/js/admin/barGraph.js?v=3"></script>
</#if>
<div class="col-md-4">
    <#--<#if onOffReal || onOffEgame || onOffChess>-->
        <div class="panel panel-default">
            <input id="thirdQuotaAlertMoney" type="hidden" value="${thirdQuotaAlertMoney}">
            <div class="panel-heading">
                <h3 class="panel-title"><@spring.message "admin.menu.transferOutLimit"/>：</h3>
                <a href="#" class="real-quota-flush" title="<@spring.message "admin.fresh"/>">
                    <i class="glyphicon glyphicon-refresh pflush"></i>
                </a>
            </div>
            <table class="table table-striped table-bordered" id="realSportQuotaView">

            </table>
        </div>
    <#--</#if>-->
    <div class="col-md-12" id="public_info_content">
        <#assign noticeDialog=0>
        <#list noticeList as notice><#if notice.dialogFlag==1><#assign noticeDialog=noticeDialog+1></#if>
            <div class="panel panel-default">
                <div class="panel-heading"><h3 class="panel-title">${notice.startDatetime}</h3></div>
                <div class="panel-body">
                    ${notice.content}
                </div>
            </div>
        </#list>
    </div>
</div>
<script>
    $(function () {
        $.ajax({
            type:'GET',
            url:baseInfo.adminBaseUrl +'/indexGlobleReport.do',
            dataType:"json",
            success: function(json){
               $("#userDrawId").text(json.totalNum);
                $("#depositId").text(json.effectiveNum);
                $("#betUserNumId").text(json.betUserNum);
                $("#depositUserId").text(json.depositUserNum);
                $("#drawUserNumId").text(json.drawUserNum);
                $("#today-highest-online-num").text(json.onlineCount);
                //$("#depositId").val()
            }
        });
    })

</script>

<style>
    #public_info_content .panel-body img{
        max-width: 100%;
    }
</style>


