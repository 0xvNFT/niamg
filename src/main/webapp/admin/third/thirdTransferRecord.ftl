<form class="fui-search table-tool" method="post" id="real_game_trans_log_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" name="startTime" value="${curDate} 00:00:00"format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "manager.begin.date"/>">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "manager.before.day"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.username.acc.name"/>">
            </div>
            <div class="input-group">
                <select class="form-control" name="platform">
                    <option value="" selected><@spring.message "admin.all.platform.sys"/></option>
                    <#list platforms as s><option value="${s.value}">${s.title}</option></#list>
                </select>
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="endTime" class="form-control fui-date" value="${curDate} 23:59:59"format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "admin.endDate"/>">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <div class="input-group">
                <select class="form-control" name="status">
                    <option value="" selected><@spring.message "admin.deposit.status.all"/></option>
                    <option value="1"><@spring.message "admin.turn.fail"/></option>
                    <option value="2"><@spring.message "admin.turn.success"/></option>
                    <option value="3" class="text-danger"><@spring.message "admin.deposit.status.ing"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="type">
                    <option value="" selected><@spring.message "admin.all.turn.into.way"/></option>
                    <option value="2"><@spring.message "admin.sys.turn.into.third"/></option>
                    <option value="1"><@spring.message "admin.third.turn.into.sys"/></option>
                </select>
            </div>
        </div>

        <div class="form-inline mt5px"><@spring.message "admin.view.val.select.game"/>：&nbsp;&nbsp;
            <@spring.message "admin.total.cash"/>:<span class="maxquto" style="color: blue">0</span>&nbsp;&nbsp;
            <@spring.message "admin.used.total.cash"/>:<span class="useQuota" style="color: green">0</span>
            &nbsp;&nbsp;<@spring.message "admin.remain.total.cash"/>:<span class="remainQuota" style="color: red">0</span>
            <button class="btn btn-info refrash-trasn-limit" type="button"><@spring.message "admin.update.live.cash"/></button>
        </div>
        <div class="unknow_order_tip" style="font-size:18px;color:blue;cursor:pointer;display:none;"><@spring.message "admin.haven"/>&nbsp;<font
                    color='red' class="orders_count">n</font>&nbsp;<@spring.message "admin.not.handle.order"/>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var platforms = [];
        <#list platforms as p>platforms['${p.value}'] = "${p.title}";</#list>
        var $form = $("#real_game_trans_log_form_id"), $platform = $form.find("[name='platform']");
        $platform.on("change", function () {
            var v = $(this).val();
            if (!v || v == "") {
                return;
            }
            $.ajax({
                url: "${adminBase}/transferRecord/getThirdQuota.do",
                data: {platform: v},
                success: function (result) {
                    try {
                        $form.find(".useQuota").text(result.used);
                        $form.find(".maxquto").text(result.quota);
                        $form.find(".remainQuota").text(result.surplus)
                    } catch (e) {
                    }
                }
            });
        });
        $form.find(".refrash-trasn-limit").click(function () {
            $platform.change();
        });
        Fui.addBootstrapTable({
            url: '${adminBase}/transferRecord/list.do',
            showAllSummary:true,
			showFooter : true,
            onLoadSuccess: function (table, data) {
                var count = 0;
                if (data.aggsData && data.aggsData.unkonwCount) {
                    count = data.aggsData.unkonwCount;
                }
                var $div = table.parents(".fui-box").find(".unknow_order_tip");
                if (count == 0) {
                    $div.css("display", "none");
                } else {
                    $div.find(".orders_count").html(count);
                    $div.css("display", "block");
                }

                if (!$div.attr("has_bind_event")) {
                    $div.bind("click", function () {
                        $form.find("select[name='status']").val("3");
                        table.bootstrapTable('refreshOptions', {
                            pageNumber: 1
                        });
                    });
                    $div.attr("has_bind_event", "true")
                }
            },
            columns: [{
	                field: 'platform',
	                title: Admin.accPlat,
	                align: 'center',
	                formatter: gameTypeFormatter
           	 	},{
                    field: 'type',
                    title: Admin.turnTypeNum,
                    align: 'center',
                    width: '200',
                    formatter: transTypeFormatter
                }, {
                    field: 'beforeMoney',
                    title: Admin.beforeThird,
                    align: 'center'
                }, {
                    field: 'money',
                    title: Admin.turnCash,
                    align: 'center',
                    width: '100',
                    formatter:changeMoneyFormatter,
                    allSummaryFormat:function(rows,aggsData){
		 				if(!aggsData){
		 					return "0.00"
		 				}
		 				return aggsData.transMoney ? aggsData.transMoney.toFixed(2) : "0.00";
		 			}
                }, {
                    field: 'afterMoney',
                    title: Admin.afterThird,
                    align: 'center'
                },{
                    field: 'createDatetime',
                    title: Admin.turnDate,
                    align: 'center',
                    width: '180',
                    formatter: function(value, row, index) {
						if(!row.updateDatetime){
							return Fui.formatDatetime(value);
						}else{
							return Fui.formatDatetime(value)+"<br>"+Fui.formatDatetime(row.updateDatetime);
						}
                    }
                }, {
                    field: 'status',
                    title: Admin.turnState,
                    align: 'center',
                    width: '100',
                    formatter: transStatusFormatter
                 }, {
                    field: 'msg',
                    title: Admin.reason,
                    align: 'center',
                    width: '100',
                    formatter: msgFormatter
                },{
                    title: Admin.op,
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter
                }]
        });

        function gameTypeFormatter(value, row, index) {
            return row.username+"<br>"+platforms[value + ""];
        }
        function htmlEscape(text){
		  return text.replace(/[<>"&]/g, function(match, pos, originalText){
		    switch(match){
			    case "<": return "&lt;";
			    case ">":return "&gt;";
			    case "&":return "&amp;";
			    case "\"":return "&quot;";
			}
		  });
		}
        function msgFormatter(value, row, index) {
        		if(value){
	         	value=htmlEscape(value);
	         	if (value.length > 20) {
	                return '<a href="javascript:void(0)" class="open-text" dialog-text="' + value + '">' + value.substr(0, 20) + '...</a>';
	            } else {
	                return value;
	            }
            }
            return "";
        }

        function transTypeFormatter(value, row, index) {
            var mesg = platforms[row.platform + ""];
            if (value == 1) {
                return "<span class='label label-success'>" + mesg + '<@spring.message "admin.turn.acc"/></span><br>'+row.orderId;
            } else {
                return "<span class='label label-warning'><@spring.message "admin.acc.turn.to"/>" + mesg + '</span><br>'+row.orderId;
            }
        }

        function transStatusFormatter(value, row, index) {
            switch (value - 0) {
                case 1:
                    return "<span class='text-danger'><@spring.message "admin.deposit.status.be.fail"/></span>";
                case 2:
                    return "<span class='text-success'><@spring.message "admin.deposit.status.be.success"/></span>";
                case 3:
                    return "<span class='text-primary'><@spring.message "admin.withdraw.status.doing"/></span>";
            }
            return "";
        }

        function changeMoneyFormatter(value, row, index) {
            if (value === undefined) {
                return value;
            }
            if (row.type == 2) {
                return '<span style="color:green">'+value.toFixed(2)+'</span>';
            }else{
                return '<span style="color:red">'+value.toFixed(2)+'</span>';
            }
        }

        function operateFormatter(value, row, index) {
            if (row.status != 3) {
                return "";
            }
            return '<a class="open-dialog" href="${adminBase}/transferRecord/showHandler.do?id=' + row.id + '"><@spring.message "admin.deposit.handler.manual"/></a>';
        }

    });
</script>