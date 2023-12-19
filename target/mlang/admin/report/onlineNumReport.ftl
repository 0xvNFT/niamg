<form class="table-tool" method="post" id="report_highest_online_num_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" value="${curDate} 00:00:00" placeholder="<@spring.message "manager.begin.date"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "manager.before.day"/></button>
            <button class="btn btn-primary search-btn fui-date-search" type="button"><@spring.message "manager.check.select"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${curDate} 23:59:59" placeholder="<@spring.message "manager.end.date"/>" autocomplete="off">
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
                    text: Admin.stationOnlineNum
                },
                tooltip: {
                    trigger: 'axis'
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
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: data.dayDate
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value} 人'
                    }
                },
                series: [
                    {
                        name: Admin.maxOnlineNum,
                        type: 'line',
                        data: data.onlineNum,
                        markPoint: {
                            data: [
                                {type: 'max', name: Admin.max},
                                {type: 'min', name: Admin.min}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: Admin.avgValue}
                            ]
                        }
                    },
                    {
                        name: Admin.loginNum,
                        type: 'line',
                        data: data.loginNum,
                        markPoint: {
                            data: [
                                {type: 'max', name: Admin.max},
                                {type: 'min', name: Admin.min}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: Admin.avgValue}
                            ]
                        }
                    }
                ]
            });
        }

        function getChart() {
            var start = $form.find("input[name='startTime']").val();
            var end = $form.find("input[name='endTime']").val();
            $.ajax({
                type: 'GET',
                url: baseInfo.adminBaseUrl + '/onlineNum/data.do',
                data:{'startTime':start,'endTime':end},
                dataType: "json",
                success: function (json) {
                    highestOnlineNumDataSet(json);
                }
            });
        }

        $(function () {
            getChart();
            $form.find(".search-btn").click(function () {
               getChart();
            });
        })
    });
</script>
