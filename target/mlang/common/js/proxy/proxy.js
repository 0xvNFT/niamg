define(['jquery', 'Fui'], function () {
    var getBaseInfoTimer;
    function getBaseInfo() {
        $.ajax({
            url: baseInfo.adminBaseUrl + '/getBaseInfo.do',
            success: function (data) {
                $("#online_span").html(data.onlineUser);
            },
            errorFn: function (data, ceipstate) {
                if (ceipstate == 4) {
                    clearTimeout(getBaseInfoTimer);
                }
            },
            error: function () {
                clearTimeout(getBaseInfoTimer);
            }
        });
        getBaseInfoTimer = setTimeout(getBaseInfo, 10000);
    }
    getBaseInfo();
    Fui.config.loginSuccessFn = function () {
        getBaseInfo();
    };
});
