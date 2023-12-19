<form id="guest_batch_manager_batch_add_form_id" method="post">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title">批量添加试玩会员</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <input type="hidden" name="modifyType" value="1">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle">登录账号：</td>
                        <td colspan="3">
                            <textarea name="guestUsernames" style="height:150px;width:100%;"></textarea>
                            <br><span style="font-size:15px;color:red">数据格式：多个账号以空格、半角逗号或者换行进行分隔</span>
                            <br><span style="font-size:15px;color:red">一次最多执行添加 10 个账号</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">会员组别：</td>
                        <td class="text-left">
                            <select name="groupId" class="form-control">
                                <#list groups as group>
                                    <option value="${group.id}" >${group.name}</option>
                                </#list>
                            </select>
                        </td>
                        <td class="text-right media-middle">会员等级：</td>
                        <td class="text-left">
                            <select name="levelId" class="form-control">
                                <#list levels as level>
                                    <option value="${level.id}" >${level.name}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">会员状态：</td>
                        <td class="text-left">
                            <select name="status" class="form-control">
                                <option value="2" selected>启用</option>
                                <option value="1">禁用</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">密码：</td>
                        <td class="text-left">
                            <input type="password" class="form-control" name="password"/>
                        </td>
                        <td class="text-right media-middle">确认密码：</td>
                        <td class="text-left">
                            <input type="password" class="form-control" name="rpassword"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">提款密码：</td>
                        <td class="text-left">
                            <input type="password" class="form-control" name="receiptPwd"/>
                        </td>
                        <td class="text-right media-middle">确认提款密码：</td>
                        <td class="text-left">
                            <input type="password" class="form-control" name="rReceiptPwd"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">电话：</td>
                        <td class="text-left">
                            <input type="text" class="form-control phone" name="phone"/>
                        </td>
                        <td class="text-right media-middle">QQ：</td>
                        <td class="text-left">
                            <input type="text" class="form-control" name="qq"/>
                        </td>
                    </tr>
                    <#--<tr>
                        <td class="text-right media-middle">所属银行：</td>
                        <td class="text-left">
                            <select name="bankCode" class="form-control">
                                <option value="1">中国工商银行</option>
                                <option value="2">中国农业银行</option>
                                <option value="3">中国民生银行</option>
                                <option value="4">中国建设银行</option>
                                <option value="5">中国招商银行</option>
                                <option value="6">中国银行</option>
                                <option value="7">中国交通银行</option>
                                <option value="8">中国邮政银行</option>
                                <option value="9">中国兴业银行</option>
                                <option value="10">华夏银行</option>
                                <option value="11">浦发银行</option>
                                <option value="12">广州银行</option>
                                <option value="13">BEA东亚银行</option>
                                <option value="14">广州农商银行</option>
                                <option value="15">顺德农商银行</option>
                                <option value="16">北京银行</option>
                                <option value="17">平安银行</option>
                                <option value="18">杭州银行</option>
                                <option value="19">温州银行</option>
                                <option value="20">上海农商银行</option>
                                <option value="21">尧都信用社</option>
                                <option value="22">中国光大银行</option>
                                <option value="23">中信银行</option>
                                <option value="24">渤海银行</option>
                                <option value="25">浙商银行</option>
                                <option value="26">晋商银行</option>
                                <option value="27">汉口银行</option>
                                <option value="28">浙江稠州商业银行</option>
                                <option value="29">上海银行</option>
                                <option value="30">广发银行</option>
                                <option value="32">东莞银行</option>
                                <option value="33">宁波银行</option>
                                <option value="34">南京银行</option>
                                <option value="36">北京农商银行</option>
                                <option value="37">重庆银行</option>
                                <option value="38">广西壮族自治区农村信用社</option>
                                <option value="40">江苏银行</option>
                                <option value="41">吉林银行</option>
                                <option value="42">成都银行</option>
                                <option value="46">农村信用社</option>
                                <option value="47">晋城银行</option>
                                <option value="48">邯郸银行</option>
                                <option value="49">郑州银行</option>
                                <option value="50">九江银行</option>
                                <option value="51">财付通</option>
                                <option value="52">盛京银行</option>
                                <option value="53">安徽农村信用合作社</option>
                                <option value="54">甘肃银行</option>
                                <option value="55">甘肃农村信用社</option>
                                <option value="56">东莞农村商业银行</option>
                                <option value="57">广东农村商业银行</option>
                                <option value="58">Payoneer</option>
                                <option value="59">NETELLER</option>
                                <option value="60">Skrill</option>
                                <option value="61">PayPal</option>
                                <option value="62">其它银行</option>
                                <option value="63">锦州银行</option>
                                <option value="64">齐鲁银行</option>
                                <option value="65">QQ钱包</option>
                                <option value="66">贵阳银行</option>
                                <option value="67">深圳农商银行</option>
                                <option value="68">贵州省农村信用社</option>
                                <option value="69">樱花银行</option>
                                <option value="70">京东钱包</option>
                                <option value="71">甘肃银行</option>
                                <option value="72">兰州银行</option>
                                <option value="73">浙江农村信用社</option>
                            </select>
                        </td>
                        <td class="text-right media-middle">银行卡号：</td>
                        <td class="text-left">
                            <input type="text" class="form-control" name="cardNo"/>
                        </td>
                    </tr>
                    <input type="hidden" name="bankName">-->
                    <tr>
                        <td class="text-right media-middle">银行地址：</td>
                        <td class="text-left">
                            <input type="text" class="form-control" name="bankAddress"/>
                        </td>
                        <td class="text-right media-middle">邮箱：</td>
                        <td class="text-left">
                            <input type="text" class="form-control email" name="email"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">微信号：</td>
                        <td class="text-left">
                            <input type="text" class="form-control" name="wechat"/>
                        </td>
                        <td class="text-right media-middle">备注：</td>
                        <td class="text-left">
                            <input type="text" class="form-control" name="remark"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div style="margin-left: 17px; margin-bottom:10px">
                <span class="glyphicon glyphicon-info-sign">温馨提示：</span>
                <br/><span class="text-danger">至少要填写 银行卡号，才会存入银行卡！</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close">关闭</button>
                <button class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    requirejs(['jquery'], function () {
        var $guestBatchManagerBatchAddForm = $("#guest_batch_manager_batch_add_form_id");
        $guestBatchManagerBatchAddForm.submit(function () {
            var password = $guestBatchManagerBatchAddForm.find("[name='password']").val()
                    , rpassword = $guestBatchManagerBatchAddForm.find("[name='rpassword']").val();
            var receiptPwd = $guestBatchManagerBatchAddForm.find("[name='receiptPwd']").val()
                    , rReceiptPwd = $guestBatchManagerBatchAddForm.find("[name='rReceiptPwd']").val();
            $guestBatchManagerBatchAddForm.find("input[name='bankName']").val($guestBatchManagerBatchAddForm.find("select[name='bankCode'] option:selected").text());
            if (!password) {
                layer.msg("密码不能为空！");
                return false;
            }
            if (!rpassword) {
                layer.msg("确认密码不能为空！");
                return false;
            }
            if (password !== rpassword) {
                layer.msg("两次密码不一致！");
                return false;
            }
            if (!receiptPwd) {
                layer.msg("提款密码不能为空！");
                return false;
            }
            if (!rReceiptPwd) {
                layer.msg("确认提款密码不能为空！");
                return false;
            }
            if (receiptPwd !== rReceiptPwd) {
                layer.msg("两次提款密码不一致！");
                return false;
            }
            batchAddGuestConfirmCommit();
            return false;
        });

        function batchAddGuestConfirmCommit() {
            $.ajax({
                url: "${adminBase}/accGuest/batchAddGuestSave.do",
                data: $guestBatchManagerBatchAddForm.serialize(),
                success: function (result) {
                    layer.closeAll();
                    layer.msg("保存成功！");
                    var $table = $(".fui-box.active").data("bootstrapTable");
                    if ($table && $table.length) {
                        $table.bootstrapTable('refresh');
                    }
                }
            });
        }
    });
</script>
