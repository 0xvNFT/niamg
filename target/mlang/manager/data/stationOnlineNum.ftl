<form class="table-tool" method="post" id="report_highest_online_num_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="startTime" value="${curDate} 00:00:00" placeholder="<@spring.message "manager.begin.date"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "manager.before.day"/></button>
            <button class="btn btn-primary search-btn fui-date-search" type="button"><@spring.message "admin.deposit.button.search"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="endTime" format="YYYY-MM-DD HH:mm:ss" class="form-control fui-date" value="${curDate} 23:59:59" placeholder="<@spring.message "manager.end.date"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "manager.last.day"/></button>
        </div>
    </div>
</form>
<div class="panel panel-default" id="highestOnlineNumEcharts" style="width: 90%;height: 500px"></div>
<script>
requirejs(['eChart'], function (echarts) {
    var highestOnlineNumEcharts = echarts.init(document.getElementById('highestOnlineNumEcharts'));
    var $form = $("#report_highest_online_num_form_id");
    function highestOnlineNumDataSet(data) {
        highestOnlineNumEcharts.setOption({
            title: {
                text: '<@spring.message "manager.station.online.sum"/>'
            },
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data:data.lengend
            },
            toolbox: {
                show: true,
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    dataView: {readOnly: false},
                    magicType: {type: ['line', 'bar']},
                    restore: {},
                    saveAsImage: {}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data.dayData
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : data.service
        });
    }

    function getChart() {
        var start = $form.find("input[name='startTime']").val();
        var end = $form.find("input[name='endTime']").val();
        $.ajax({
            type: 'GET',
            url:  '${managerBase}/stationOnlineNum/data.do',
            data:{'startTime':start,'endTime':end},
            dataType: "json",
            success: function (json) {
                handelEcchatData(json);
            }
        });
    }

    function handelEcchatData(data) {
        var legend = new Array();
        var service = new Array();
        $.each(data.stations, function( index, value ) {
            legend[index] = value.code;
            var ser = {
                name:value.code,
                type:'line',
                stack: '<@spring.message "manager.sum"/>',
                areaStyle: {},
                data:data[value.code]
            };
            service[index] = ser;
        });
        debugger;
        var result = {legend:legend,service:service,dayData:data.dayData};
        highestOnlineNumDataSet(result);
    }
    $(function () {
        getChart();
        $form.find(".search-btn").click(function () {
            getChart();
        });
    })
});
</script>
