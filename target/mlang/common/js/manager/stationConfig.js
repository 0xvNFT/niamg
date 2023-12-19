define(['template', 'moment', 'jquery', 'bootstrap', 'Fui', 'bootstrap_editable'], function (template, moment) {
    var settingsSource = '{{each configGroups as gp}}<div class="checkbox"><label>'
        + '<input type="checkbox" class="setting_config" id="setting_config_{{configMap[gp].id}}" value="{{configMap[gp].id}}"> {{gp}}</label>'
        + '<div class="secCheckBox">{{each configMap[gp].sons as conf}}<label class="checkbox-inline">'
        + '<input type="checkbox" name="setting_confg_gp" parent="{{configMap[gp].id}}" value="{{conf.key}}" {{curConfigs[conf.key]}}> {{conf.name}}</label>'
        + '{{/each}}</div></div>{{/each}}',
        configSource = '{{each configGroups as gp index}}<div class="panel panel-default" id="station_config_gps_{{index}}"><div class="panel-heading">{{gp}}</div>'
            + '<div class="panel-body"><table class="table table-bordered table-striped" style="clear: both"><tbody>{{each configs[gp] as conf}}'
            + '<tr><td width="30%" class="text-center">{{conf.title}}</td><td width="70%"><a style="word-break: break-all;max-width:50%;display:inline-block;"'
            + 'href="#" data-type="{{conf.eleType}}" data-dtype="{{conf.dataType}}" data-value="{{conf.value}}" data-pk="{{conf.key}}" data-title="{{conf.title}}"'
            + '></a></td></tr>{{/each}}</tbody></table></div></div>{{/each}}',
        navSource = '{{each configGroups as d index}}{{if index == 0}}<a href="#station_config_gps_{{index}}" class="btn btn-default active">{{d}}</a>'
            + '{{else}}<a href="#station_config_gps_{{index}}" class="btn btn-default">{{d}}</a>{{/if}}{{/each}}';
    window.moment = moment;

    function loadSettings($con, stationId) {
        $.getJSON(baseInfo.adminBaseUrl + "/stationConfig/getSettings.do?stationId=" + stationId, function (json) {
            json.curConfigs = {};
            if (json.stationConfigs && json.stationConfigs.length > 0) {
                for (var i = 0; i < json.stationConfigs.length; i++) {
                    json.curConfigs[json.stationConfigs[i]] = "checked";
                }
            }
            $con.find(".settingsCon").html(template.compile(settingsSource)(json));
            var checkboxs = null;
            for (var i = 0; i < json.configGroups.length; i++) {
                checkboxs = $con.find("[parent='" + i + "']");
                if (checkboxs.filter(":checked").length == checkboxs.length) {
                    $con.find("#setting_config_" + i).prop("checked", true);
                }
            }
            $con.find(".setting_config").click(function () {
                var i = $(this).val();
                if ($(this).prop("checked")) {
                    $con.find("[parent='" + i + "']").prop("checked", true);
                } else {
                    $con.find("[parent='" + i + "']").prop("checked", false);
                }
            });
        });
    }

    function loadStationConfig($con, stationId) {
        $.getJSON(baseInfo.adminBaseUrl + "/stationConfig/getConfigs.do?stationId=" + stationId, function (json) {
            var configGroups = [];
            var arr = json.configs;
            if (arr && arr.length > 0) {
                var groupName = {}, item;
                for (var i = 0; i < arr.length; i++) {
                    item = arr[i];
                    if (!groupName[item.groupName]) {
                        groupName[item.groupName] = [];
                    }
                    if (item.eleType.indexOf("Select") > 0) {
                        item.dataType = item.eleType;
                        item.eleType = "select";
                    }
                    groupName[item.groupName].push(item);
                }
                for (var i = 0; i < json.configGroups.length; i++) {
                    if (groupName[json.configGroups[i]]) {
                        configGroups.push(json.configGroups[i]);
                    }
                }
                $con.find(".settingsCon").html(template.compile(configSource)({
                    configs: groupName,
                    configGroups: configGroups
                }));
                initXeditable1($con.find(".settingsCon"), stationId);
            }
            $con.find(".eleTabs").html(template.compile(navSource)({configGroups: configGroups})).on("click", "a", function () {
                $(this).addClass("active").siblings().removeClass("active");
            });
        });
    }

    function initXeditable1($content, stationId) {
        $content.find("a").each(function () {
            var options = {
                url: baseInfo.adminBaseUrl + '/stationConfig/saveConfig.do',
                emptytext: '无配置',
                params: function (params) {
                    params.stationId = stationId;
                    return params;
                },
                placement: 'right',
                ajaxOptions: {
                    dataType: "json",
                    success: function (json) {
                        layer.msg(json.msg || "修改成功");
                    }
                }
            }, type = $(this).attr("data-type")
                , dtype = $(this).attr("data-dtype")
                , val = $(this).attr("data-value");
            if (val !== undefined && val !== "") {
                $(this).html(val.replace(/</g, "&lt;"));
            }
            if (type === 'select') {
                switch (dtype) {
                    case "switchSelect":
                        options.source = [{"off": "关闭"}, {"on": "开启"}];
                        break;
                    case 'modelSelect':
                        options.source = [{"rand": "随机"}, {"pct": "中奖百分比"}];
                        break;
                    case 'vCodeSelect':
                        options.source = [{"a": "普通"},{"s": "算法"},  {"a1": "5字符"}, {"b": "纯数字"}, {"b1": "5纯数字"}, {"c": "纯数字扭曲"}, {"c1": "5纯数字扭曲"}, {"d": "扭曲字符"}, {"d1": "扭曲5英文"}, {"zz": "中文"}, {"e": "扭曲中文"}, {"e1": "扭曲中文2"}, {"f": "gif字符"}, {"f1": "gif5字符"}, {"g": "gif中文"}, {"g1": "gif中文1"}];
                        break;
                    case 'prModeSelect':
	                	options.source = [{"timely": "时时自动返点"}, {"auto": "第二天自动返点"}, {"manual": "第二天手动返点"}];
	                    break;
                    case 'mobileGameMenuSelect':
                        options.source = [{"list": "列表模式"}, {"cat": "分类模式"}, {"simple": "简洁模式"}, {"lottery": "彩种模式"}];
                        break;
                    case 'nativeMenuColorSelect':
                        options.source = [{"red": "红色"}, {"green": "绿色"}, {"blue": "蓝色"},{"simple": "简约"},{"blur_orange": "毛玻璃橙色"},{"blur_blue": "毛玻璃蓝色"},{"blur_purple": "毛玻璃紫色"},{"realman": "真人海报"}];
                        break;
                    case 'nativeStyleSelect':
                        options.source = [{"1": "分组模式"}, {"2": "经典模式"}];
                        break;
                    case 'prompLinkStyleSelect':
                        options.source = [{"v1": "app模式"}, {"v2": "pc模式"}];
                        break;
                    case 'mobilePageSelect':
                        options.source = [{"v1": "版本1"}, {"v2": "版本2"}];
                        break;
                    case 'registerModeSwitchSelect':
                        options.source = [{"v1": "用户名注册"}, {"v2": "邮件注册"}, {"v3": "手机注册"}];
                        break;
                    case 'loginModeSwitchSelect':
                        options.source = [{"v1": "用户名登录"}, {"v2": "邮件登录"}, {"v3": "手机登录"}];
                        break; 
                    case 'homeTabModelSelect':
                        options.source = [{"v1": "水平滚动模式"}, {"v2": "下拉模式"},{"v3": "列表平铺模式"},{"v4": "水平滚动模式2"}];
                        break;
                    case 'chargePageStyleSelect':
                        options.source = [{"v1": "分组模式"}, {"v2": "经典模式"}];
                        break;
                    case 'memberCenterStyleSelect':
                        options.source = [{"1": "分组模式"}, {"2": "经典模式"}];
                        break;
                    case 'robpacketSelect':
                        options.source = [{"v1": "经典版本"}, {"v2": "优化版本"}];
                        break;
                    case 'onlineServiceOpenSelect':
                        options.source = [{"v1": "浏览器打开"}, {"v2": "应用内部打开"}];
                        break;
                    case 'mobilepaySelect':
                        options.source = [{"V1": "版本1--在线支付模式:先选支付方式再选支付通道"}, {"V2": "版本2--在线支付模式:先选支付通道再选支付方式"}];
                        break;
                    case 'mainpageVersionSelect':
                        options.source = [{"V1": "版本1-经典版本"}, {"V2": "版本2--存款，取款分离版本"}, {"V3": "版本3--主页推广链接版本"}, {"V4": "版本4--主页额度转换版本"},
                            {"V5": "版本5--主页余额生金版本"}, {"V6": "版本6--主页聊天室版本"}, {"V7": "版本7--主页免费试玩版本"}, {"V8": "版本8--主页开奖网版本"}, {"V9": "版本9--主页余额生金和开奖网版本"},
                            {"V10":"版本10-主页优惠活动大厅版本"},
                            {"V11": "版本11-主页纯电竞版本"},
                            {"V12": "版本12-主页纯体育版本"}];
                        break;
                    case 'pushIntervalSelect':
                        options.source = [{"1": "1分钟"}, {"2": "2分钟"},{"5": "5分钟"},{"10": "10分钟"},{"15": "15分钟"}];
                        break;
                    case 'nativeChargeVersionSelect':
                        options.source = [{"V1": "版本1-原生版本"}, {"V2": "版本2--WAP版本"}];
                        break;
                    case 'openResultVersionSelect':
                        options.source = [{"V1": "版本1-经典版本"}, {"V2": "版本2--优化版本"}];
                        break;
                    case 'introduceShowSelect':
                        options.source = [{"V1": "页面展示"}, {"V2": "弹窗展示"}];
                        break;
                    case 'skinVersionSelect' :
                        options.source = [{"skin1": "皮肤一（红）"}, {"skin2": "皮肤二（紫）"}];
                        break;
                    case 'fastDepositAddMoneySelect':
                        options.source = [{"0": "关闭"}, {"1": "元"}, {"2": "分"}];
                        break;
					case 'proxySelect':
						options.source = [{"1": "全是代理"}, {"2": "多级代理+会员"}, {"3": "一级代理+会员"}, {"4": "只有会员"}];
                        break;
                }
                var arr = options.source;
                for (var i = 0; i < arr.length; i++) {
                    var obj = arr[i];
                    if (obj[val] !== undefined) {
                        $(this).html(obj[val]);
                        break;
                    }
                }
            } else if (type === 'combodate') {
                var fmt = 'HH:mm'
                    , cdconf = {
                    format: fmt
                    , minYear: 2015
                    , maxYear: 2030
                    , minuteStep: 1
                    , startView: 1
                    , minView: 0
                };
                options.template = fmt;
                options.format = fmt;
                options.viewformat = fmt;
                options.combodate = cdconf;
            } else if (type === 'textarea') {
                options.placement = 'top';
            }
            $(this).editable(options);
        });
    }

    return {
        render: function () {
            var $con = $("#station_confg_tabs_wrap_id"), curTab = 1;
            $con.find("a[oname]").click(function () {
                var $it = $(this), name = $it.attr("oname");
                switch (name) {
                    case "bangding":
                        $con.find(".saveAllConfig").removeClass("hidden").prop("disabled", true);
                        $con.find(".selectAll").removeClass("hidden").prop("disabled", true);
                        $con.find(".eleTabs").html("");
                        $con.find(".settingsCon").html("");
                        $con.find("[name='stationId']").val(0);
                        curTab = 1;
                        break;
                    case "shezhi":
                        curTab = 2;
                        $con.find(".saveAllConfig").addClass("hidden").prop("disabled", true);
                        $con.find(".selectAll").addClass("hidden").prop("disabled", true);
                        $con.find(".settingsCon").html("");
                        $con.find("[name='stationId']").val(0);
                        break;
                }
            });
            $con.find("[name='stationId']").change(function () {
                var stationId = $(this).val();
                if (stationId == 0) {
                    $con.find(".saveAllConfig").prop("disabled", true);
                    $con.find(".selectAll").prop("disabled", true);
                    return;
                }
                if (curTab == 1) {
                    $con.find(".saveAllConfig").prop("disabled", false);
                    $con.find(".selectAll").prop("disabled", false);
                    loadSettings($con, stationId);
                } else {
                    loadStationConfig($con, stationId);
                }
            });
            $con.find(".selectAll").click(function () {
                if ($(this).attr("checked1") == "checked") {
                    $con.find("[type='checkbox']").prop("checked", false);
                    $(this).attr("checked1", "");
                } else {
                    $con.find("[type='checkbox']").prop("checked", true);
                    $(this).attr("checked1", "checked");
                }
            });
            $con.find(".saveAllConfig").click(function () {
                var keys = [];
                $con.find("[name='setting_confg_gp']:checked").each(function () {
                    keys.push($(this).val());
                });
                $.ajax({
                    url: baseInfo.adminBaseUrl + "/stationConfig/saveSettings.do",
                    data: {
                        stationId: $con.find("[name='stationId']").val(),
                        keys: keys.join(",")
                    },
                    success: function (json) {
                        layer.msg(json.msg);
                    }
                });
            });
        }
    }
});
