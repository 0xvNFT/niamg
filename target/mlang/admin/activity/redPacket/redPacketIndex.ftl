<div class="table-tool">
    <#if permAdminFn.hadPermission("admin:redPacket:add")>
		<button class="btn btn-primary open-dialog" url="${adminBase}/redPacket/showAdd.do"><@spring.message "admin.strategy.seed.red"/></button>
		<button class="btn btn-danger open-dialog" url="${adminBase}/redPacket/shoaAddDegree.do"><@spring.message "admin.vip.level.seed.red"/></button>
		<button class="btn btn-success open-dialog" url="${adminBase}/redPacket/showAddRecharge.do"><@spring.message "admin.charge.num.seed.red"/></button>
		<button class="btn btn-info open-dialog" url="${adminBase}/redPacket/cloneOneRedBag.do"><@spring.message "admin.red.packet.clone"/></button>
		<button class="btn btn-info open-dialog" url="${adminBase}/redPacket/showFissionAdd.do"><@spring.message "admin.red.packet.fession"/></button>
    </#if>
    <#if permAdminFn.hadPermission("admin:redPacket:delete")>
   		<button class="btn btn-primary" id="deleteRedPackets" type="button"><@spring.message "admin.batch.del"/></button>
    </#if>
</div>
<table class="fui-default-table" id="redpacket_tb_id"></table>
<h3>
	<span class="label label-primary"><@spring.message "admin.item.config.red"/></span>
	<span class="label label-danger"><@spring.message "admin.vip.level.red"/></span>
	<span class="label label-success"><@spring.message "admin.charge.red.bet"/></span>
</h3>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
	var $table=$("#redpacket_tb_id");
	$table.on("click", "input.check_all", function () {
        var $this = $(this), isChecked = $this.prop('checked');
        $table.find('tbody input:checkbox').prop('checked', isChecked);
        
    });
	$("#deleteRedPackets").click(function (e) {
        var ids = '';
        $table.find('tbody input:checkbox:checked').each(function (i, j) {
            j = $(j);
            if(j.val()!='on'){
            	ids += j.val() + ",";
            }
        })
        if (!ids) {
            layer.msg('<@spring.message "admin.sure.del.item"/>');
            return;
        }
        ids = ids.substring(0, ids.length - 1);
        layer.confirm('<@spring.message "admin.sure.del.selected.item"/>？', {btn: ['<@spring.message "manager.sure.name"/>', '<@spring.message "admin.member.info.cancle"/>']}, function (index) {
            $.ajax({
                url: '${adminBase}/redPacket/deleteAll.do',
                data: {ids: ids},
                success: function (res) {
                    if (res.success) {
                        layer.msg('<@spring.message "manager.delete.success"/>');
                        $table.find('input:checked').prop('checked', false);
                        $table.bootstrapTable('refresh');
                    } else {
                        layer.msg(res.msg);
                    }
                    layer.close(index);
                }
            })
        }, function () {
            $table.find('input:checked').prop('checked', false);
        })
    });
    Fui.addBootstrapTable({
        url: '${adminBase}/redPacket/list.do',
        columns: [{
            field: 'checkbox',
            title: '<input type="checkbox" class="check_all">',
            align: 'center',
            width : '50',
            vilign: 'middle',
            formatter: function(value, row, index) {
				return '<input type="checkbox" value="' + row.id + '">';
			}
        },{
            field: 'id',
            title: Admin.redPacCode,
            align: 'center',
            valign: 'middle'
        },{
            field: 'title',
            title: Admin.redItem,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'totalMoney',
            title: Admin.redCashVal,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }, {
            field: 'totalNumber',
            title: Admin.redNum,
            align: 'center',
            valign: 'middle',
           formatter: totalNumberFormatter
        }, {
            field: 'minMoney',
            title: Admin.minCash,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }/*, {
            field: 'ipNumber',
            title: '限制当天IP次数',
            align: 'center',
            valign: 'middle'
        }*/,{
            field: 'redBagType',
            title: Admin.redType,
            align: 'center',
            valign: 'middle',
            formatter: redBagTypeFormatter
        }, {
            field: 'todayDeposit',
            title: Admin.dailyCharge,
            align: 'center',
            valign: 'middle',
            formatter: depositFormatter
        }/*, {
            field: 'moneyBase',
            title: '充值基数',
            align: 'center',
            valign: 'middle'
        },{
            field: 'moneyCustom',
            title: '金额区间',
            align: 'center',
            valign: 'middle'
        }*/, {
            field: 'degreeNames',
            title: Admin.vaildVipLevel,
            align: 'center',
            valign: 'middle',
            formatter: degreeNamesFormatter
        }, {
            field: 'beginDatetime',
            title: Admin.beginTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'endDatetime',
            title: Admin.endTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'status',
            title: Admin.status,
            align: 'center',
            valign: 'middle',
            formatter: statusFormatter
        }, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });
    function moneyFormatter(value, row, index) {
        if (value === undefined) {
            return '';
        }
        if (value > 0) {
            return '<span class="text-danger">'+value+'</span>';
        }
        return '<span class="text-primary">'+value+'</span>';
    }
    function totalNumberFormatter(value, row, index) {
        if (value === undefined) {
            return '';
        }else{
        	return value;
        }
    }
    function redBagTypeFormatter(value, row, index) {
    	if(!row.redBagType || '1' == row.redBagType){
            return '<@spring.message "admin.strategy.red"/>';
        }else if( '2' == row.redBagType){
			return '<@spring.message "admin.level.red.packet"/>';
		}else if( '3' == row.redBagType){
			return '<@spring.message "admin.charge.bet.red"/>';
		}else if( '4' == row.redBagType){
			return '<@spring.message "admin.red.packet.fession"/>';
		}else{
			 return '--';
		}
    }
    function depositFormatter(value, row, index) {
        if(!value){
        	return '';
        }
        if(row.redBagType == 3){
            if(value == 2){
                return '<span class="text-primary"><@spring.message "admin.daily.have.charge.val"/></span>';
            }else if(value == 3){
                return '<span class="text-primary"><@spring.message "admin.red.act"/></span>';
            }else if(value == 4){
                return '<span class="text-success"><@spring.message "admin.single.charge"/></span>';
            }else if(value == 5){
                return '<span class="text-success"><@spring.message "admin.yesterday.charge"/></span>';
            }else if(value == 6){
                return '<span class="text-success"><@spring.message "admin.daily.first.charge"/></span>';
            }else if(value == 8){
                return '<span class="text-success"><@spring.message "admin.act.single.charge"/></span>';
            }else{
                return '<span class="text-success"><@spring.message "admin.history.charge"/></span>';
            }
        }else if(row.redBagType == 2){
            if(value == 2){
                return '<span class="text-primary"><@spring.message "admin.daily.have.charge.val"/></span>';
            }else if(value == 1){
                return '<span class="text-primary"><@spring.message "admin.not.limited"/></span>';
            }
        }else{
            if(value == 2){
                return '<span class="text-primary"><@spring.message "admin.daily.have.charge.val"/></span>';
            }else if(value == 1){
                return '<span class="text-primary"><@spring.message "admin.not.limited"/></span>';
            }else if(value === 7){
                return '<span class="text-danger"><@spring.message "admin.history.charge.not.daily"/></span>';
            }
        }
    }
    function degreeNamesFormatter(value, row, index) {
    	if(!row.redBagType || '1' == row.redBagType || '3' == row.redBagType || '4' == row.redBagType){
            return value;
        }else if( '2' == row.redBagType){
			return '<a class="open-dialog" href="${adminBase}/redPacket/viewLevel.do?id=' + row.id + '" title="<@spring.message "admin.menu.viewDetail"/>"><@spring.message "admin.deposit.table.detail"/></a>';
		}else{
			 return '--';
		}
    }
    function statusFormatter(value, row, index) {
        return Fui.statusFormatter({
            val: value,
            url: "${adminBase}/redPacket/updStatus.do?id=" + row.id + "&status="
        })
    }
    function operateFormatter(value, row, index) {
    	if(!row.redBagType || '1' == row.redBagType){
    		return '<a class="open-dialog" href="${adminBase}/redPacket/view.do?id='+row.id+'" title="<@spring.message "admin.menu.viewDetail"/>"><i class="glyphicon glyphicon-eye-open"></i></a>'+
	            '&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/redPacket/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.title+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
		}else if('2' == row.redBagType){
			return '<a class="open-dialog" href="${adminBase}/redPacket/viewLevel.do?id='+row.id+'" title="<@spring.message "admin.menu.viewDetail"/>"><i class="glyphicon glyphicon-eye-open"></i></a>'+
	            '&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/redPacket/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.title+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
		}else if('3' == row.redBagType){
			return '<a class="open-dialog" href="${adminBase}/redPacket/viewRecharge.do?id='+row.id+'" title="<@spring.message "admin.menu.viewDetail"/>"><i class="glyphicon glyphicon-eye-open"></i></a>'+
	            '&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/redPacket/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.title+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
		}else if('4' == row.redBagType){
			return '<a class="open-dialog" href="${adminBase}/redPacket/view.do?id='+row.id+'" title="<@spring.message "admin.menu.viewDetail"/>"><i class="glyphicon glyphicon-eye-open"></i></a>'+
					'&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/redPacket/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.title+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
		}
    }
});
</script>
