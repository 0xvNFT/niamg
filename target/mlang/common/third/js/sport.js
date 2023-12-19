function init() {
	// var curPlatform = document.getElementById("curPlatform");
    var type = $(".nav li[data-type]:first").data('type');
    // if(curPlatform.innerHTML != undefined && curPlatform.innerHTML != null && curPlatform.innerHTML != "" ){
    //     type = curPlatform.innerHTML;
	// }
    if (type == 'agSport') {
        searchData = agSportdata;
    } else if (type == 'tyxjSport') {
        searchData = tyxjSportdata;
    } else if (type == 'tycrSport') {
        searchData = tycrSportdata;
    } else if (type == 'tysbSport') {
        searchData = tysbSportdata;
    } else if (type == 'sboSport') {
        searchData = sboSportdata;
    } else if (type == 'ppSport') {
        searchData = ppSportdata;
    } else if (type == 'fbSport') {
        searchData = fbSportdata;
    }

    //数据获取
    new Vue({
        el: '#cboxSport',
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
                if (type == 'agSport') {
                    this.searchData = agSportdata;
                } else if (type == 'tyxjSport') {
                    this.searchData = tyxjSportdata;
                } else if (type == 'tycrSport') {
                    this.searchData = tycrSportdata;
                } else if (type == 'tysbSport') {
                    this.searchData = tysbSportdata;
                } else if (type == 'sboSport') {
                    this.searchData = sboSportdata;
                } else if (type == 'ppSport') {
                    this.searchData = ppSportdata;
                }else if (type == 'fbSport') {
                    this.searchData = fbSportdata;
                }
                this.gameLength = this.searchData.length;
            },

            // 面包屑导航
            gameType: function (tab, type) {
                if (tab == 'agSport') {
                    if (type == 'agSportdata')
                        this.searchData = agSportdata;
                } else if (tab == 'tyxjSport') {
                    if (type == 'tyxjSportdata')
                        this.searchData = tyxjSportdata;
                } else if (tab == 'tycrSport') {
                    if (type == 'tycrSportdata')
                        this.searchData = tycrSportdata;
                } else if (tab == 'tysbSport') {
                    if (type == 'tysbSportdata')
                        this.searchData = tysbSportdata;
                }else if (tab == 'sboSport') {
                    if (type == 'sboSportdata')
                        this.searchData = sboSportdata;
                }else if (tab == 'ppSport') {
                    if (type == 'ppSportdata')
                        this.searchData = ppSportdata;
                }else if (tab == 'fbSport') {
                    if (type == 'fbSportdata')
                        this.searchData = fbSportdata;
                }
                this.gameLength = this.searchData.length;
            },

            // 搜索指定游戏
            searchGame: function () {
                var searchGameName = $('#elenew-search-game').val(), allData;
                if (this.tabTxt == 'agSport') {
                    allData = agSportdata;
                }
                if (this.tabTxt == 'tyxjSport') {
                    allData = tyxjSportdata;
                }
                if (this.tabTxt == 'tycrSport') {
                    allData = tycrSportdata;
                }
                if (this.tabTxt == 'tysbSport') {
                    allData = tysbSportdata;
                }
                if (this.tabTxt == 'sboSport') {
                    allData = sboSportdata;
                }
                if (this.tabTxt == 'ppSport') {
                    allData = ppSportdata;
                }
                if (this.tabTxt == 'fbSport') {
                    allData = fbSportdata;
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
                    return '<div class="elenew-game-ctl-links"><a href="javascript:void(0);"class="unlogin ' + className + '">Start Game1111</a></div>';
                }
                var s = '<div class="elenew-game-ctl-links"><a href="' + base;
                switch (this.tabTxt) {
                    case "agSport":
                        s = s + '/third/forwardAg.do?gameType=';
                        break;
                    case "tyxjSport":
                        s = s + '/third/forwardTyxj.do?gameType=';
                        break;
                    case "tycrSport":
                        s = s + '/third/forwardTycr.do?gameType=';
                        break;
                    case "tysbSport":
                        s = s + '/third/forwardTysb.do?gameType=';
                        break;
                    case "sboSport":
                        s = s + '/third/forwardSbo.do?gameType=';
                        break;
                    case "ppSport":
                        s = s + '/third/forwardPP.do?gameType=';
                        break;
                    case "fbSport":
                        s = s + '/third/forwardFB.do?gameType=';
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
    $("#cboxSport").on("click", ".unlogin", function () {
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
