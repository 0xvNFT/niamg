function init() {
	// var curPlatform = document.getElementById("curPlatform");
    var type = $(".nav li[data-type]:first").data('type');
    // if(curPlatform.innerHTML != undefined && curPlatform.innerHTML != null && curPlatform.innerHTML != "" ){
    //     type = curPlatform.innerHTML;
	// }
    if (type == 'agLive') {
        searchData = agLiveData;
    } else if (type == 'bbinLive') {
        searchData = bbinLiveData;
    } else if (type == 'awcLive') {
        searchData = awcLiveData;
    }else if (type == 'mgLive') {
        searchData = mgLiveData;
    } else if (type == 'dgLive') {
        searchData = dgLiveData;
    } else if (type == 'ogLive') {
        searchData = ogLiveData;
    } else if (type == 'abLive') {
        searchData = abLiveData;
    } else if (type == 'bgLive') {
        searchData = bgLiveData;
    } else if (type == 'ebetLive') {
        searchData = ebetLiveData;
    } else if (type == 'evolutionLive') {
        searchData = evolutionLiveData;
    } else if (type == 'ppLive') {
        searchData = ppLiveData;
    }

    //数据获取
    new Vue({
        el: '#cboxLive',
        data: {
            searchData: searchData,
            tabTxt: type,
            gameLength: searchData.length
        },
        methods: {
            // 根据传参显示对应内容
            tabChange: function (type) {
                this.tabTxt = type;
                console.log("type = "+type)
                if (type == 'agLive') {
                    this.searchData = agLiveData;
                } else if (type == 'bbinLive') {
                    this.searchData = bbinLiveData;
                }
                else if (type == 'awcLive') {
                    this.searchData = awcLiveData;
                }
                else if (type == 'mgLive') {
                    this.searchData = mgLiveData;
                } else if (type == 'dgLive') {
                    this.searchData = dgLiveData;
                } else if (type == 'ogLive') {
                    this.searchData = ogLiveData;
                } else if (type == 'abLive') {
                    this.searchData = abLiveData;
                }else if (type == 'bgLive') {
                    this.searchData = bgLiveData;
                } else if (type == 'ebetLive') {
                    this.searchData = ebetLiveData;
                }else if (type == 'evolutionLive') {
                    this.searchData = evolutionLiveData;
                }else if (type =='ppLive'){
                    this.searchData = ppLiveData;
                }
                this.gameLength = this.searchData.length;
            },

            // 面包屑导航
            gameType: function (tab, type) {
                if (tab == 'agLive') {
                    if (type == 'agLiveData')
                        this.searchData = agLiveData;
                }else if (tab == 'awcLive') {
                    if (type == 'awcLiveData')
                        this.searchData = awcLiveData;
                } else if (tab == 'bbinLive') {
                    if (type == 'bbinLiveData')
                        this.searchData = bbinLiveData;
                } else if (tab == 'mgLive') {
                    if (type == 'mgLiveData')
                        this.searchData = mgLiveData;
                } else if (tab == 'dgLive') {
                    if (type == 'dgLiveData')
                        this.searchData = ogLiveData;
                }else if (tab == 'ogLive') {
                    if (type == 'sboSportdata')
                        this.searchData = sboSportdata;
                }else if (tab == 'abLive') {
                    if (type == 'abLiveData')
                        this.searchData = abLiveData;
                }else if (tab == 'bgLive') {
                    if (type == 'bgLiveData')
                        this.searchData = bgLiveData;
                }else if (tab == 'ebetLive') {
                    if (type == 'ebetLiveData')
                        this.searchData = ebetLiveData;
                }else if (tab == 'ppLive') {
                    if (type == 'ppLiveData')
                        this.searchData = ppLiveData;
                }
                this.gameLength = this.searchData.length;
            },

            // 搜索指定游戏
            searchGame: function () {
                var searchGameName = $('#elenew-search-game').val(), allData;
                if (this.tabTxt == 'agLive') {
                    allData = agLiveData;
                }
                if (this.tabTxt == 'bbinLive') {
                    allData = bbinLiveData;
                }
                if (this.tabTxt == 'mgLive') {
                    allData = mgLiveData;
                }
                if (this.tabTxt == 'dgLive') {
                    allData = dgLiveData;
                }
                if (this.tabTxt == 'ogLive') {
                    allData = ogLiveData;
                }
                if (this.tabTxt == 'abLive') {
                    allData = abLiveData;
                }
                if (this.tabTxt == 'bgLive') {
                    allData = bgLiveData;
                }
                if (this.tabTxt == 'ebetLive') {
                    allData = ebetLiveData;
                }
                if (this.tabTxt == 'ppLive') {
                    allData = ppLiveData;
                }
                if (this.tabTxt == 'awcLive') {
                    allData = awcLiveData;
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
                    case "agLive":
                        s = s + '/third/forwardAg.do?gameType=';
                        break;
                    case "awcLive":
                        s = s + '/third/forwardAwc.do?gameType=';
                        break;
                    case "bbinLive":
                        s = s + '/third/forwardBbin2.do?gameType=';
                        break;
                    case "mgLive":
                        s = s + '/third/forwardMg.do?gameType=';
                        break;
                    case "dgLive":
                        s = s + '/third/forwardDg.do?gameType=';
                        break;
                    case "ogLive":
                        s = s + '/third/forwardOg.do?gameType=';
                        break;
                    case "abLive":
                        s = s + '/third/forwardAb.do?gameType=';
                        break;
                    case "bgLive":
                        s = s + '/third/forwardBg.do?gameType=';
                        break;
                    case "ebetLive":
                        s = s + '/third/forwardEbet.do?gameType=';
                        break;
                    case "evolutionLive":
                        s = s + '/third/forwardEvolution.do?gameType=';
                        break;
                    case "ppLive":
                        s = s + '/third/forwardPP.do?gameType=';
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
    $("#cboxLive").on("click", ".unlogin", function () {
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
