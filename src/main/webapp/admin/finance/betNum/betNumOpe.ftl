<form id="bet_num_ope_form_id">
    <div class="form-inline">
        <div class="input-group">
            <input type="text" class="form-control" name="searchText" placeholder="<@spring.message "admin.input.username.query.find"/>" value="${username }">
        </div>
        <button class="btn btn-primary select-btn" type="button"><@spring.message "manager.check.select"/></button>
        <button class="btn btn-primary reset-btn" type="button"><@spring.message "manager.resetting"/></button>
    </div>
    <div class="hidden memmny-div" style="margin-top:10px;"><input type="hidden" name="id"/>
        <table class="table table-bordered">
            <tbody>
            <tr>
                <td width="30%" class="text-right active"><@spring.message "admin.money.history.username"/>：</td>
                <td class="text-primary username-td"></td>
            </tr>
            <tr>
                <td class="text-right active"><@spring.message "admin.found.need.weight"/>：</td>
                <td>
                    <span class="text-danger draw-need"></span>
                    <span class="text-primary">(<@spring.message "admin.member.current.bet"/>：<span class="text-danger bet-num"></span>)</span>
                    <button type="button" id="clearBetBtn"><@spring.message "admin.clear.drawneed"/></button>
                </td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "manager.oper.type"/>：</td>
                <td>
                    <select name="type" class="form-control">
                    <option value="10" selected><@spring.message "admin.worker.add"/></option>
                    <option value="11"><@spring.message "admin.worker.deduce"/></option></select>
                </td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "admin.operate.cash.num"/>：</td>
                <td><input type="text" class="form-control" name="betNum" /></td>
            </tr>
            <tr>
                <td class="text-right active media-middle"><@spring.message "admin.operate.reason"/>：</td>
                <td><textarea class="form-control" name="remark"></textarea></td>
            </tr>
            <#if isReceiptPwd>
             <tr>
                 <td class="text-right media-middle"><@spring.message "admin.secondPwd"/>：</td>
                 <td><input type="password" class="form-control" name="password" required/></td>
             </tr>
            </#if>
            <tr>
                <td colspan="2" class="text-center"><button class="btn btn-primary"><@spring.message "admin.confirmed"/></button></td>
            </tr>
            </tbody>
        </table>
    </div>
</form>
<script type="text/javascript">
    requirejs(['jquery'],function(){
        var $form=$("#bet_num_ope_form_id") ,$searchText=$form.find("[name='searchText']");
        <#if username>
    	search();
    	</#if>
        function search(){
            var acc=$searchText.val();
            if(acc==''){
                layer.msg("<@spring.message "admin.member.acc.not.blank"/>");
                return false;
            }
            $.ajax({
                url:"${adminBase}/finance/betNumOpe/getBetInfo.do?username="+acc,
                type:"GET",
                success:function(data){
                    if(data.success == false){
                        reset();
                        layer.msg(data.msg);
                    }
                    reset();
                    $searchText.val(acc);
                    $form.find("[name='id']").val(data.id);
                    $form.find(".username-td").html(data.username);
                    $form.find(".draw-need").html(data.drawNeed);
                    $form.find(".bet-num").html(data.betNum);
                    $form.find(".memmny-div").removeClass("hidden");
                },
                dataType:"JSON",
                errorFn:function(){
                    reset();
                    $searchText.val(acc);
                }
            });
        }
        $form.find(".select-btn").click(search);
        $form.find(".reset-btn").click(reset);
        $searchText.keydown(function(e){
            if(e.keyCode==13){
                search();
                return false;
            }
        });
        function reset() {
            $searchText.val("");
            $form.find("[name='id']").val("");
            $form.find(".username-td").html("");
            $form.find(".draw-need").html('');
            $form.find(".bet-num").html('');
            $form.find("[name='betNum']").val("");
            $form.find("[name='type']").val(1).change();
            $form.find("[name='remark']").val("");
            $form.find("[name='password']").val("");
            $form.find(".memmny-div").addClass("hidden")
        }
        var saveExe=false;
        $form.submit(function(){
            if(saveExe){
                layer.msg("<@spring.message "admin.submit.add.bet.wait"/>");
                return false;
            }
            saveExe=true;
            $.ajax({
                url : "${adminBase}/finance/betNumOpe/save.do",
                data : $form.serialize(),
                success : function(data) {
                    layer.msg("<@spring.message "admin.operate.succ"/>！");
                    search();
                    saveExe=false;
                },
                error:function(data){
                    layer.msg("<@spring.message "admin.operate.fail"/>！"+data.msg);
                    saveExe=false;
                },
                errorFn:function(data){
                    layer.msg("<@spring.message "admin.operate.fail"/>！"+data.msg);
                    saveExe=false;
                }
            });
            return false;
        })
        function doClearBet() {
            var id = $form.find("[name='id']").val();
            if (!id) {
                layer.tips("record is empty");
                return;
            }
            var remark = $form.find("[name='remark']").val();
            $.ajax({
                type: "POST",
                url: "${adminBase}/finance/betNumOpe/clearbetNumOpe.do",
                data: {
                    id: id,
                    remark: remark,
                },
                success: function (data, textStatus, xhr) {
                    var ceipstate = xhr.getResponseHeader("ceipstate");
                    if (!ceipstate || ceipstate == 1) {// 正常响应
                        search();
                        saveExe=false;
                    } else {// 后台异常
                        var msg = data.msg || "后台异常，请联系管理员!";
                        layer.msg(msg, {icon: 5});
                    }
                }
            });
        }
        $("#clearBetBtn").click(function () {
            doClearBet();
            return false;
        });
    });
</script>
