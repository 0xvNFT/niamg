/**
 *
 *  @Title: windown  国际化版
 *  @Author: shiTou
 *
 */

//MSG BTN
//btn_ok = "ยืนยัน";
btn_back = "Back";
btn_cancel = "Cancel";

//msg_title = "ข้อมูล";

msg_common_error = "System error!";
msg_null_data_error="Error, data is empty!";

//OPTION SUCCESS MSG
option_option = "Option";
option_insert = "Insert";
option_add = "Save";
option_delete = "Delete";
option_update = "Update";

function warnMsg(msg) {
    if (msg == null || msg == undefined) {
        msg = msg_common_error;
    }
    layer.alert(msg, {title: Admin.msg_title, btn: Admin.btn_ok, icon: 3, end: unDisableButton()});
}

function errorMsg(msg) {
    if (msg == null || msg == undefined) {
        msg = msg_common_error;
    }
    layer.alert(msg, {title: Admin.msg_title, btn: Admin.btn_ok, icon: 2, end: unDisableButton()});
}


function errorMsgCommon() {
    layer.alert(msg_common_error, {title:Admin.msg_title, btn: Admin.btn_ok, icon: 2, time: 2000, end: unDisableButton()});
}

function errorNotCloseMsgCommon(errorMsg) {
    layer.alert(errorMsg, {title: Admin.msg_title, btn: Admin.btn_ok, icon: 2, end: unDisableButton()});
}

function closeLayer(layero) {
    layer.close(layero);//关闭confirm弹框
}

function closeAllLayer() {
    parent.layer.closeAll();
}

function disableButton() {
    $(".btn").prop("disabled", true);
}

function unDisableButton() {
    $(".btn").prop("disabled", false);
}