<div class="table-tool">
    <form class="fui-search table-tool" method="post" id="homepage_game_form_id">
        <div class="form-group zhongjiang">
            <div class="form-group fui-data-wrap">
                <div class="form-inline">
                    <div class="input-group">
                        <input type="text" class="form-control fui-date" name="startDate" value="${startDate}"
                               placeholder="<@spring.message "admin.startDate"/>" autocomplete="off">
                        <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
                    <div class="input-group">
		                <select name="gameTabId" class="form-control">
		                    <option value=""><@spring.message "manager.all.types"/></option>
		                </select>
           			</div>
                    <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
                    <button class="btn btn-primary open-dialog" url="${base}/admin/stationHomepageGame/add.do"><@spring.message "admin.add"/></button>
                    <button class="btn btn-primary batchDelete"><@spring.message "admin.batch.del"/></button>
                </div>
                <div class="form-inline mt5px">
                    <div class="input-group">
                        <input type="text" name="endDate" class="form-control fui-date"
                               value="${endDate}" placeholder="<@spring.message "admin.endDate"/>" autocomplete="off">
                        <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
                </div>
            </div>
        </div>
    </form>
</div>
<table class="fui-default-table" id="homepage_game_tb_id"></table>
<script type="text/javascript">
    var $form=$("#homepage_game_form_id");//表单控件
	var $gameTabSelect=$form.find("[name='gameTabId']");
    requirejs(['jquery','bootstrap','Fui'],function(){
      	var appTabList={};
        $.ajax({
                url: '${base}/admin/stationHomepageGame/getTabList.do',
                data: {},
                async: false,
                success: function (res) {
               		appTabList = res;
               		$.each(res, function(key, value) {
						$gameTabSelect.append("<option value='" + value.id + "'>" + value.name + "</option>");
					});
                }
        })
	
        Fui.addBootstrapTable({
            id: 'homepage_game_tb_id',
            url :'${base}/admin/stationHomepageGame/list.do',
            columns : [ {
                field: 'checkbox',
                title: '<input type="checkbox" class="check_all">',
                align: 'center',
                vilign: 'middle',
                formatter: operateCheckboxMatter
            }, {
                field : 'gameTabId',
                title : Admin.menuName,
                align : 'center',
                valign : 'middle',
                formatter : gameTabIdFormatter
            }, {
                field : 'gameName',
                title : Admin.gameNameMeth,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'parentGameCode',
                title : Admin.systemType,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'gameType',
                title : Admin.gameTypes,
                align : 'center',
                valign : 'middle',
                formatter : gameTypeFormatter
            }, {
                field : 'sortNo',
                title : Admin.sortNum,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'createDatetime',
                title : Admin.addDate,
                align : 'center',
                valign : 'middle',
                formatter : Fui.formatDatetime
            }, {
                field : 'status',
                title : Admin.status,
                align : 'center',
                valign : 'middle',
                formatter : statusFormatter
            }, {
                title : Admin.op,
                align : 'center',
                valign : 'middle',
                formatter : operateFormatter
            } ]
        });

        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({val:value,url:"${base}/admin/stationHomepageGame/updStatus.do?id="+row.id+"&status="});
        }

		function gameTabIdFormatter(value, row, index) {
			var tab = appTabList.find(item=> item.id==row.gameTabId);
			if(tab)
				return tab.name;
			else
				return "";
        }
        
        function gameTypeFormatter(value, row, index) {
            if (row.gameType == 1){
                return "<@spring.message "manager.lettory.name"/>";
            }else if (row.gameType == 2){
                return "<@spring.message "admin.live"/>";
            }else if (row.gameType == 3){
                return "<@spring.message "admin.egame"/>";
            }else if (row.gameType == 4){
                return "<@spring.message "admin.sport"/>";
            }else if (row.gameType == 5){
                return "<@spring.message "admin.esport"/>";
            }else if (row.gameType == 6){
                return "<@spring.message "admin.fishing"/>";
            }else if (row.gameType == 7){
                return "<@spring.message "admin.chess"/>";
            }else if (row.gameType == 8){
                return "<@spring.message "admin.define.info"/>";
            }else if (row.gameType == 9){
                return "<@spring.message "admin.red.packet"/>";
            }else {
                return "<@spring.message "admin.other.thing"/>";
            }
        }

        function operateFormatter(value, row, index) {
            return  '<a class="todo-ajax" href="${base}/admin/stationHomepageGame/delete.do?id='+row.id+'" title="<@spring.message "admin.sure.game.del"/>['+row.gameName+']？"><@spring.message "admin.menu.del"/></a>';
            	//'<a class="open-dialog" href="${base}/admin/stationHomepageGame/modify.do?id='+row.id+'"><@spring.message "admin.menu.modify"/></a>   '+
        }

        function operateCheckboxMatter(value, row, index) {
            return '<input type="checkbox" value="' + row.id + '">';
        }

        var $formId = $("#homepage_game_form_id"), $table = $("#homepage_game_tb_id");

        $formId.on("click", "button.batchDelete", function (e) {
            //取消原始的事件
            e.preventDefault();
            var ids = '';
            $table.find('tbody input:checkbox:checked').each(function (i, j) {
                j = $(j);
                ids += j.val() + ",";
            })
            if (!ids) {
                layer.msg('<@spring.message "admin.sure.del.item"/>');
                return;
            }
            ids = ids.substring(0, ids.length - 1);
            layer.confirm('<@spring.message "admin.sure.del.selected.item"/>？', {btn: ['<@spring.message "manager.sure.name"/>', '<@spring.message "admin.member.info.cancle"/>']}, function (index) {
                $.ajax({
                    url: '${adminBase}/stationHomepageGame/deleteBatch.do',
                    data: {ids: ids},
                    success: function (res) {
                        if (res.success) {
                            layer.msg('<@spring.message "manager.delete.success"/>');
                            $table.find('input:checked').prop('checked', false);
                            refresh();
                        } else {
                            layer.msg(res.msg);
                        }
                        layer.close(index);
                    }
                })
            }, function () {
                $table.find('input:checked').prop('checked', false);
            })
        })

        $table.on("click", "input.check_all", function () {
            var $this = $(this), isChecked = $this.prop('checked');
            $table.find('tbody input:checkbox').prop('checked', isChecked);
        });

        function refresh() {
            var $table = $formId.parents(".fui-box").data("bootstrapTable");
            if ($table && $table.length) {
                $table.bootstrapTable('refresh');
            }
        }

    });
</script>
