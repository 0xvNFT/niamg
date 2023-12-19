function init() {
	var curPlatform = document.getElementById("curPlatform");
    var type = $(".nav li[data-type]:first").data('type');
    if(curPlatform.innerHTML != undefined && curPlatform.innerHTML != null && curPlatform.innerHTML != "" ){
        type = curPlatform.innerHTML;	
	}
    if (type == 'mg') {
        searchData = mgdata;
    } else if (type == 'pt') {
        searchData = ptdata;
    } else if (type == 'skywind') {
        searchData = skyWindData;
    } else if (type == 'evo') {
        searchData = evodata;
    } else if (type == 'pp') {
        searchData = ppdata;
    } else if (type == 'fg') {
        searchData = fgdata;
    }else if (type == 'jl') {
        searchData = jldata;
    }else if (type == 'tada') {
        searchData = tadadata;
    }else if (type == 'bs') {
        searchData = bsdata;
    }else if (type == 'pg') {
        searchData = pgdata;
    }else if (type == 'jdb') {
        searchData = jdbdata;
    }else if (type == 'bbin') {
        searchData = bbindata;
    }else if (type == 'cq9') {
        searchData = cq9data;
    }else if (type == 'ag') {
        searchData = agdata;
    }else if (type == 'es') {
        searchData = esdata;
    }

    //数据获取
    new Vue({
        el: '#cbox',
        data: {
            searchData: searchData,
            tabTxt: type,
            gameLength: searchData.length
        },
        methods: {
            // 根据传参显示对应内容
            tabChange: function (type) {
                this.tabTxt = type;
                if (type == 'ag') {
                    this.searchData = agdata;
                } else if (type == 'mg') {
                    this.searchData = mgdata;
                } else if (type == 'pt') {
                    this.searchData = ptdata;
                } else if (type == 'skywind') {
                    this.searchData = skyWindData;
                } else if (type == 'evo') {
                    this.searchData = evodata;
                } else if (type == 'pp') {
                    this.searchData = ppdata;
                } else if (type == 'fg') {
                    this.searchData = fgdata;
                } else if (type == 'jl') {
                    this.searchData = jldata;
                } else if (type == 'tada') {
                    this.searchData = tadadata;
                }else if (type == 'bs') {
                    this.searchData = bsdata;
                }else if (type == 'pg') {
                    this.searchData = pgdata;
                }else if (type == 'jdb') {
                    this.searchData = jdbdata;
                }else if (type == 'bbin') {
                    this.searchData = bbindata;
                }else if (type == 'cq9') {
                    this.searchData = cq9data;
                }else if (type == 'es') {
                    this.searchData = esdata;
                }
                this.gameLength = this.searchData.length;
            },

            // 面包屑导航
            gameType: function (tab, type) {
                if (tab == 'ag') {
                    if (type == 'agdata') this.searchData = agdata;
                } else if (tab == 'mg') {
                    if (type == 'mgdata') this.searchData = mgdata;
                } else if (tab == 'skywind') {
                    if (type == 'skywinddata') this.searchData = skyWindData;
                } else if (tab == 'pt') {
                    switch (type) {
                        case 'ptdata':
                            this.searchData = ptdata;
                            break;
                        default:
                            var d = [];
                            for (var i = 0; i < ptdata.length; i++) {
                                if (ptdata[i].tId == type) {
                                    d.push(ptdata[i]);
                                }
                            }
                            this.searchData = d;
                    }
                } else if (tab == 'evo') {
                    if (type == 'evodata') this.searchData = evodata;
                } else if (tab == 'pp') {
                    if (type == 'ppdata') this.searchData = ppdata;
                } else if (tab == 'fg') {
                    if (type == 'fgdata') this.searchData = fgdata;
                }else if (tab == 'jl') {
                    if (type == 'jldata') this.searchData = jldata;
                }else if (tab == 'tada') {
                    if (type == 'tadadata') this.searchData = tadadata;
                }else if (tab == 'bs') {
                    if (type == 'bsdata') this.searchData = bsdata;
                }else if (type == 'pg') {
                    if (type == 'pgdata') this.searchData = pgdata;
                }else if (type == 'jdb') {
                    if (type == 'jdbdata') this.searchData = jdbdata;
                }else if (type == 'bbin') {
                    if (type == 'bbindata') this.searchData = bbindata;
                }else if (type == 'cq9') {
                    if (type == 'cq9data') this.searchData = cq9data;
                }else if (type == 'es') {
                    if (type == 'esdata') this.searchData = esdata;
                }
                this.gameLength = this.searchData.length;
            },

            // 搜索指定游戏
            searchGame: function () {
                var searchGameName = $('#elenew-search-game').val(), allData;
                if (this.tabTxt == 'ag') {
                    allData = agdata;
                }
                if (this.tabTxt == 'mg') {
                    allData = mgdata;
                }
                if (this.tabTxt == 'pt') {
                    allData = ptdata;
                }
                if (this.tabTxt == 'skywind') {
                    allData = skyWindData;
                }
                if (this.tabTxt == 'evo') {
                    allData = evodata;
                }
                if (this.tabTxt == 'pp') {
                    allData = ppdata;
                } 
                if (this.tabTxt == 'fg') {
                    allData = fgdata;
                }
                if (this.tabTxt == 'jl') {
                    allData = jldata;
                }
                if (this.tabTxt == 'tada') {
                    allData = tadadata;
                }
                if (this.tabTxt == 'bs') {
                    allData = bsdata;
                }
                if (this.tabTxt == 'pg') {
                    allData = pgdata;
                }
                if (this.tabTxt == 'jdb') {
                    allData = jdbdata;
                }
                if (this.tabTxt == 'bbin') {
                    allData = bbindata;
                }
                if (this.tabTxt == 'cq9') {
                    allData = cq9data;
                }
                if (this.tabTxt == 'es') {
                    allData = esdata;
                }
                if (searchGameName) {
                    searchGameName = searchGameName.toLowerCase();
                    this.searchData = [];
                    for (var i = 0; i < allData.length; i++) {
                        if (allData[i].name.toLowerCase().includes(searchGameName)) {
                            this.searchData.push(allData[i]);
                        }
                    }
                } else {
                    this.searchData = allData;
                }
                this.gameLength = this.searchData.length;
            },

            forwardGame1: function (isLogin, gameId, className) {
                className = className || "ctl-btn-lite";
                if (!isLogin) {
                    return '<div class="elenew-game-ctl-links"><a href="javascript:void(0);"class="unlogin ' + className + '">Start Game</a></div>';
                }
                var s = '<div class="elenew-game-ctl-links"><a href="' + base;
                switch (this.tabTxt) {
                    case "ag":
                        s = s + '/third/forwardAg.do?gameType=';
                        break;
                    case "pt":
                        s = s + '/third/forwardPt.do?gameType=';
                        break;
                    case "mg":
                        s = s + '/third/forwardMg.do?gameType=';
                        break;
                    case "skywind":
                        s = s + '/third/forwardSkyWind.do?gameType=';
                        break;
                    case "evo":
                        s = s + '/third/forwardEvo.do?gameType=';
                        break;
                    case "pp":
                        s = s + '/third/forwardPP.do?gameType=';
                        break;
                   case "fg":
                        s = s + '/third/forwardFg.do?gameType=';
                        break;
                    case "jl":
                        s = s + '/third/forwardJl.do?gameType=';
                        break;
                    case "tada":
                        s = s + '/third/forwardTada.do?gameType=';
                        break;
                    case "bs":
                        s = s + '/third/forwardBs.do?gameType=';
                        break;
                    case "pg":
                        s = s + '/third/forwardPg.do?gameType=';
                        break;
                    case "jdb":
                        s = s + '/third/forwardJdb.do?gameType=';
                        break;
                    case "bbin":
                        s = s + '/third/forwardBbin2.do?gameType=';
                        break;
                    case "cq9":
                        s = s + '/third/forwardCq9.do?gameType=';
                        break;
                    case "es":
                        s = s + '/third/forwardEs.do?gameType=';
                        break;


                }
                return s + gameId + '" target="_blank" class="' + className + '">Start Game</a></div>';
            }
        }
    })

    $('.elenew-gn-btn').hover(function () {
        $('.elenew-gn-icon').toggleClass('on');
        $('.elenew-gn-wrap').slideToggle();
    });

    // 游戏展示方式切换
    $('.elenew-viewbtn-mini').click(function () {
        $('.elenew-game-wrap').removeClass('elenew-view-block');
        $('.elenew-game-wrap').addClass('elenew-view-mini');
        $(this).addClass('view-active');
        $('.elenew-viewbtn-block').removeClass('view-active');
    });
    $('.elenew-viewbtn-block').click(function () {
        $('.elenew-game-wrap').removeClass('elenew-view-mini');
        $('.elenew-game-wrap').addClass('elenew-view-block');
        $(this).addClass('view-active');
        $('.elenew-viewbtn-mini').removeClass('view-active');
    });
}

(function ($) {
    // 备份jquery的ajax方法
    var _ajax = $.ajax;

    // 重写jquery的ajax方法
    $.ajax = function (opt) {
        if (!opt.dataType) {
            opt.dataType = "json";
        }
        if (!opt.type) {
            opt.type = "post";
        }
        // 备份opt中error和success方法
        var fn = {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            success: function (data, textStatus, xhr) {
            }
        }
        if (opt.error) {
            fn.error = opt.error;
        }
        if (opt.success) {
            fn.success = opt.success;
        }

        // 扩展增强处理
        var _opt = $.extend(opt, {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var statusCode = XMLHttpRequest.status;
                // 错误方法增强处理
                if (statusCode == 404) {
                    layer.alert("[" + opt.url + "] 404 not found");
                } else {
                    fn.error(XMLHttpRequest, textStatus, errorThrown);
                }
            },
            success: function (data, textStatus, xhr) {
                var ceipstate = xhr.getResponseHeader("ceipstate")
                if (ceipstate == 1) {// 正常响应
                    fn.success(data, textStatus, xhr);
                } else if (ceipstate == 2) {// 后台异常
                    layer.alert(data.msg);
                } else if (ceipstate == 3) { // 业务异常
                    layer.alert(data.msg);
                } else if (ceipstate == 4) {// 未登陆异常
                    layer.alert(data.msg);
                } else if (ceipstate == 5) {// 没有权限
                    layer.alert(data.msg);
                } else {
                    fn.success(data, textStatus, xhr);
                }
            }
        });
        _ajax(_opt);
    };
    init();
    $("#cbox").on("click", ".unlogin", function () {
        layer.open({
            type: 1,
            title: Admin.notLogin,
            shadeClose: true,
            shade: 0.8,
            area: ['380px', '200px'],
            content: $('#loginfrom')
        });
    });
})(jQuery);
