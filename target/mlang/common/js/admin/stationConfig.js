define(['template', 'moment', 'jquery', 'bootstrap', 'Fui', 'bootstrap_editable'], function (template, moment) {
    var configSource = '{{each configGroups as gp index}}<div class="panel panel-default" id="station_config_gps_{{index}}"><div class="panel-heading">{{gp}}</div>'
        + '<div class="panel-body"><table class="table table-bordered table-striped" style="clear: both"><tbody>{{each configs[gp] as conf}}'
        + '<tr><td width="30%" class="text-center">{{conf.title}}</td><td width="70%"><a style="word-break: break-all;max-width:50%;display:inline-block;"'
        + 'href="#" data-type="{{conf.eleType}}" data-dtype="{{conf.dataType}}"　 data-value="{{conf.value}}" data-pk="{{conf.key}}" data-title="{{conf.title}}"'
        + '></a></td></tr>{{/each}}</tbody></table></div></div>{{/each}}',
        navSource = '{{each configGroups as d index}}{{if index == 0}}<a href="#station_config_gps_{{index}}" class="btn btn-default active">{{d}}</a>'
            + '{{else}}<a href="#station_config_gps_{{index}}" class="btn btn-default">{{d}}</a>{{/if}}{{/each}}';
    window.moment = moment;

    function initXeditable1($content) {
        $content.find("a").each(function () {

            var options = {
                url: baseInfo.adminBaseUrl + '/stationConfig/saveConfig.do',
                emptytext: Admin.notConfig,
                params: function (params) {
                    return params;
                },
                placement: 'right',
                ajaxOptions: {
                    dataType: "json",
                    success: function (json) {
                        layer.msg(json.msg || Admin.updateSuccess);
                    }
                }
            }, type = $(this).attr("data-type"), dtype = $(this).attr("data-dtype"), val = $(this).attr("data-value");
            if (val !== undefined && val !== "") {
                $(this).html(val.replace(/</g, "&lt;"));
            }
            if (type === 'select') {
                switch (dtype) {
                    case "switchSelect":
                        options.source = [{"off": Admin.close}, {"on": Admin.opened}];
                        break;
                    case 'vCodeSelect':
                        options.source = [{"a": Admin.ordinary}, {"s": Admin.algorithm}, {"a1": Admin.fiveCharacters}, {"b": Admin.pureNumbers}, {"b1": Admin.fivePureNumbers}, {"c": Admin.pureDigitalDistortion}, {"c1": Admin.fivePureDigitalDistortion}, {"d": Admin.distortedCharacters}, {"d1": Admin.twistFiveEnglish}, {"f": Admin.gifCharacters}, {"f1": Admin.gifFiveCharacters}];
                        break;
                    case 'prModeSelect':
                        options.source = [{"timely": Admin.rebateEveryTime}, {"auto": Admin.rebateNextDay}, {"manual": Admin.manualRebateNextDay}];
                        break;
                    case 'mobileGameMenuSelect':
                        options.source = [{"list": Admin.listMode}, {"cat": Admin.classificationMode}, {"simple": Admin.conciseMode}, {"lottery": Admin.colorPattern}];
                        break;
                    case 'nativeMenuColorSelect':
                        options.source = [{"red": Admin.red}, {"green": Admin.green}, {"blue": Admin.blue}, {"simple": Admin.simplicity}, {"blur_orange": Admin.frostedGlassOrange}, {"blur_blue": Admin.frostedGlassBlue}, {"blur_purple": Admin.frostedGlassPurple}, {"realman": Admin.livePoster}];
                        break;
                    case 'nativeStyleSelect':
                        options.source = [{"1": Admin.groupingMode}, {"2": Admin.classicMode}];
                        break;
                    case 'prompLinkStyleSelect':
                        options.source = [{"v1": Admin.appMode}, {"v2": Admin.pcMode}];
                        break;
                    case 'mobilePageSelect':
                        options.source = [{"v1": Admin.version1}, {"v2": Admin.version2}];
                        break;
                    case 'registerModeSwitchSelect':
                        options.source = [{"v1": Admin.usernameRegistration}, {"v2": Admin.emailRegistration}, {"v3": Admin.mobileRegistration}];
                        break;
                    case 'loginModeSwitchSelect':
                        options.source = [{"v1": Admin.usernameLogin}, {"v2": Admin.emailLogin}, {"v3": Admin.mobileLogin}];
                        break;
                    case 'numberOfPeopleOnline':
                        options.source = [{"v2": 100}];
                        break;
                    case 'numberOfWinners':
                        options.source = [{"v2": 200}];
                        break;
                    case 'homeTabModelSelect':
                        options.source = [{"v1": Admin.horizontalScrollingMode}, {"v2": Admin.pullDownMode},{"v3": Admin.listTileMode},{"v4": Admin.horizontalScrollingMode2}];
                        break;
                    case 'chargePageStyleSelect':
                        options.source = [{"v1": Admin.groupingMode}, {"v2": Admin.classicMode}];
                        break;
                    case 'memberCenterStyleSelect':
                        options.source = [{"1": Admin.groupingMode}, {"2": Admin.classicMode}];
                        break;
                    case 'robpacketSelect':
                        options.source = [{"v1": Admin.classicVersion}, {"v2": Admin.optimizedVersion}];
                        break;
                    case 'onlineServiceOpenSelect':
                        options.source = [{"v1": Admin.browserOpens}, {"v2": Admin.openInsideApp}];
                        break;
                    case 'mobilepaySelect':
                        options.source = [{"V1": Admin.onlinePayOne}, {"V2": Admin.onlinePayTwo}];
                        break;
                    case 'templateSelect':
                        options.source = [{"one": Admin.templateOne}, {"two": Admin.templateTwo}];
                        break;
                    case 'mainpageVersionSelect':
                        options.source = [
                            {"V1": Admin.versionOne},
                            {"V2": Admin.versionTwo},
                            {"V3": Admin.versionThree},
                            {"V4": Admin.versionFour},
                            {"V5": Admin.versionFive},
                            {"V6": Admin.versionSix},
                            {"V7": Admin.versionSeven},
                            {"V8": Admin.versionEight},
                            {"V9": Admin.versionNine},
                            {"V10": Admin.versionTen},
                            {"V11": Admin.versionEleven},
                            {"V12": Admin.versionTwelve}];
                        break;
                    case 'pushIntervalSelect':
                        options.source = [{"1": Admin.oneMinute}, {"2": Admin.twoMinute}, {"5": Admin.fiveMinute}, {"10": Admin.tenMinute}, {"15": Admin.fifteenMinute}];
                        break;
                    case 'nativeChargeVersionSelect':
                        options.source = [{"V1": Admin.nativeVersion}, {"V2": Admin.wapVersion}];
                        break;
                    case 'openResultVersionSelect':
                        options.source = [{"V1": Admin.versionOne}, {"V2": Admin.optimizedVersion2}];
                        break;
                    case 'introduceShowSelect':
                        options.source = [{"V1": Admin.pageDisplay}, {"V2": Admin.popupDisplay}];
                        break;
                    case 'skinVersionSelect' :
                        options.source = [{"skin1": Admin.skinOne}, {"skin2": Admin.skinTwo}];
                        break;
					case 'fastDepositAddMoneySelect':
                        options.source = [{"0": Admin.close}, {"1": Admin.yuan}, {"2": Admin.minute}];
                        break;
					case 'proxySelect':
						options.source = [{"1":Admin.allAgents}, {"2": Admin.multiLevelAgentAndMember}, {"3": Admin.firstAgentAndMember}, {"4": Admin.membersOnly}];
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
            var $con = $("#station_confg_tabs_wrap_id");
            $.getJSON(baseInfo.adminBaseUrl + "/stationConfig/list.do", function (json) {
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

                    initXeditable1($con.find(".settingsCon"));
                }
                $con.find(".eleTabs").html(template.compile(navSource)({configGroups: configGroups})).on("click", "a", function () {
                    $(this).addClass("active").siblings().removeClass("active");
                });
            });
        }
    }
});
