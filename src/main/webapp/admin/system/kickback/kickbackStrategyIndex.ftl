<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select name="type" class="form-control">
                <option value=""><@spring.message "admin.game.type.selected"/></option>
				<#if game.live==2><option value="1"><@spring.message "manager.live"/></option></#if>
				<#if game.egame==2><option value="2"><@spring.message "manager.egame"/></option></#if>
				<#if game.chess==2><option value="3"><@spring.message "manager.chess"/></option></#if>
				<#if game.fishing==2><option value="4"><@spring.message "manager.fish"/></option></#if>
				<#if game.esport==2><option value="5"><@spring.message "manager.esport"/></option></#if>
				<#if game.sport==2><option value="6"><@spring.message "manager.sport"/></option></#if>
				<#if game.lottery==2><option value="7"><@spring.message "manager.lottery"/></option></#if>
            </select>
			<button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
			<#if permAdminFn.hadPermission("admin:kickbackStrategy:add")>
		        <button class="btn btn-primary open-dialog cached" url="${adminBase}/kickbackStrategy/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></button>
		    </#if>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<div id="remark_div" style="padding: 10px;">
    <span class="text-danger"><@spring.message "admin.kickback.setting.rebate"/>。</span><br>
    <span><@spring.message "admin.kickback.compute.time"/>：</span>
    <span class="text-danger"><@spring.message "admin.kickback.detail.time"/></span><br>
    <span><@spring.message "admin.hand.kickback.time"/>：</span>
    <span class="text-danger"><@spring.message "admin.kickback.time.info"/></span>
</div>
<script type="text/javascript">
	var ds={},gs={};<#list degrees as l>ds["${l.id}"]="${l.name}";</#list><#list groups as l>gs["${l.id}"]="${l.name}";</#list>
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: "${adminBase}/kickbackStrategy/list.do",
            columns: [{
                field: 'type',
                title: Admin.type,
                align: 'center',
                valign: 'middle',
                formatter: typeFormatter
            }, {
                field: 'kickback',
                title: Admin.backWaterRet,
                align: 'center',
                valign: 'middle'
             }, {
                title: Admin.memberLevel,
                align: 'center',
                valign: 'middle',
                formatter: degreeFormatter
            }, {
                title: Admin.memberGroup,
                align: 'center',
                valign: 'middle',
                formatter: groupFormatter
            }, {
                field: 'minBet',
                title: Admin.dailyMinPay,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'betNum',
                title: Admin.betWeightNum,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'maxBack',
                title: Admin.backWaterMax,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter: statusFormatter
            }, {
                field: 'remark',
                title: Admin.remark,
                align: 'center',
                valign: 'middle',
                formatter: remarkFormatter
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });
		function degreeFormatter(value,row,index){
			if(!row.degreeIds){return '<span class="label label-success"><@spring.message "admin.not.limited"/></span>';}
			var s='';
			for(var i in ds){
				if(row.degreeIds.indexOf(","+i+",")>=0){
					s=s+' <span class="label label-warning">'+ds[i]+'</span> ';
				}
			}
			return s;
		}
		function groupFormatter(value,row,index){
			if(!row.groupIds){return '<span class="label label-success"><@spring.message "admin.not.limited"/></span>';}
			var s='';
			for(var i in gs){
				if(row.groupIds.indexOf(","+i+",")>=0){
					s=s+' <span class="label label-warning">'+gs[i]+'</span> ';
				}
			}
			return s;
		}
        function operateFormatter(value, row, index) {
            return [
            <#if permAdminFn.hadPermission("admin:kickbackStrategy:update")>
                '<a class="open-dialog" href="${adminBase}/kickbackStrategy/showModify.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>">',
                '<i class="glyphicon glyphicon-pencil"></i>', '</a>  ',
            </#if>
            <#if permAdminFn.hadPermission("admin:kickbackStrategy:delete")>
                '<a class="todo-ajax" href="${adminBase}/kickbackStrategy/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>？">',
                '<i class="glyphicon glyphicon-remove"></i>', '</a>'</#if>]
                    .join('');
        }

        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/kickbackStrategy/changeStatus.do?id=" + row.id + "&status="
            });
        }

        function remarkFormatter(value, row, index) {
            if (!value) {
                return "";
            }
            if (value.length < 20) {
                return value;
            }
            return ['<a class="open-text" dialog-text="' + value + '" dialog-title="<@spring.message "admin.money.history.remark.detail"/>" title="' + value + '">', '</span>'].join(value.substr(0, 20) + "...");
        }

        function typeFormatter(value, row, index) {
           if (value == 1) {
                return "<@spring.message "admin.live.backwater"/>";
            } else if (value == 2) {
                return "<@spring.message "admin.egame.backwater"/>";
            } else if (value == 6) {
                return "<@spring.message "admin.sprots.backwater"/>";
            } else if (value == 7) {
                return "<@spring.message "admin.lottery.backwater"/>";
            } else if (value == 3) {
                return "<@spring.message "manager.chess"/>"
            }else if (value == 5) {
                return "<@spring.message "manager.esport"/>"
            } else if(value == 4) {
                return "<@spring.message "manager.fish"/>"
            }
        }
    });
</script>

