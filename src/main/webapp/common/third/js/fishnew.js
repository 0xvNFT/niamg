function init() {
	// var curPlatform = document.getElementById("curPlatform");
    var type = $(".nav li[data-type]:first").data('type');
    // if(curPlatform.innerHTML != undefined && curPlatform.innerHTML != null && curPlatform.innerHTML != "" ){
    //     type = curPlatform.innerHTML;
	// }
    if (type == 'bbinFish') {
        searchData = bbinFishdata;
    } else if (type == 'cq9Fish') {
        searchData = cq9FishData;
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
                if (type == 'bbinFish') {
                    this.searchData = bbinFishdata;
                } else if (type == 'cq9Fish') {
                    this.searchData = cq9FishData;
                }

                this.gameLength = this.searchData.length;
            },

            // 面包屑导航
            gameType: function (tab, type) {
                if (tab == 'bbinFish') {
                    if (type == 'bbinFishdata')
                        this.searchData = bbinFishdata;
                } else if (tab == 'cq9Fish') {
                    if (type == 'cq9FishData')
                        this.searchData = cq9FishData;
                }
                this.gameLength = this.searchData.length;
            },

            // 搜索指定游戏
            searchGame: function () {
                var searchGameName = $('#elenew-search-game').val(), allData;
                if (this.tabTxt == 'bbinFish') {
                    allData = bbinFishdata;
                }
                if (this.tabTxt == 'cq9Fish') {
                    allData = cq9FishData;
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

                    case "bbinFish":
                        s = s + '/third/forwardBbin2.do?gameType=';
                        break;
                    case "cq9Fish":
                        s = s + '/third/forwardCq9.do?gameType=';
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
