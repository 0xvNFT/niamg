<form action="${adminBase}/turntable/doAddMoney.do" class="form-submit" id="turntableAddMoneyFormId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content" style="width: 1000px">
            <input type="hidden" name="playConfig">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.insert.act.bet.charge"/></h4>
            </div>
            <div class="modal-body" style="max-height: 650px;min-height: 380px;overflow: auto;">
                <table class="table table-bordered table-striped" data-spy="scroll">
                    <tbody id="red_bag_tbody_id" style="max-height: 500px;overflow: scroll;">
                    <tr>
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.act.name"/></td>
                        <td width="20%"><input name="name" class="form-control required" type="text" placeholder="<@spring.message "admin.act.title"/>"/></td>
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.attend.types"/></td>
                        <td width="20%">
                            <label class="radio-inline"><input type="radio" name="joinType" value="1" checked><@spring.message "admin.pay.record"/></label>
                            <label class="radio-inline"><input type="radio" name="joinType" value="2"><@spring.message "admin.bit.weight"/></label>
                        </td>
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.statistics.type"/></td>
                        <td>
                            <label class="radio-inline"><input type="radio" name="countType" value="1" checked><@spring.message "admin.daily.data"/></label>
                            <label class="radio-inline"><input type="radio" name="countType" value="2"><@spring.message "admin.register.now.data"/></label>
                            <label class="radio-inline"><input type="radio" name="countType" value="3"><@spring.message "admin.daily.charge.type.config"/></label>
                            <label class="radio-inline"><input type="radio" name="countType" value="4"><@spring.message "admin.act.charge.attend.config"/></label>
                        </td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "admin.startTime"/></td>
                        <td><input class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" name="begin" placeholder="<@spring.message "admin.startTime"/>"></td>
                        <td class="text-right media-middle"><@spring.message "admin.endTime"/></td>
                        <td><input class="form-control fui-date required" format="YYYY-MM-DD HH:mm:ss" name="end" placeholder="<@spring.message "admin.endTime"/>"></td>
                        <td class="text-right media-middle"><@spring.message "admin.play.count.type.can"/></td>
                        <td style="padding-top: 14px;">
                            <label class="radio-inline"><input type="radio" name="playNumType" value="2"><@spring.message "admin.interval.count"/></label>
                            <label class="radio-inline"><input type="radio" name="playNumType" value="1" checked><@spring.message "admin.all.counts"/></label>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.jack.qualifications"/></td>
                        <td class="text-left" colspan="1"><textarea name="activeRole" class="form-control"></textarea></td>
                        <td class="text-right media-middle"><@spring.message "admin.act.statement"/></td>
                        <td class="text-left" colspan="1"><textarea name="activeRemark" class="form-control"></textarea></td>
                        <td class="text-right media-middle"><@spring.message "admin.act.rule"/></td>
                        <td class="text-left" colspan="1"><textarea name="activeHelp" class="form-control"></textarea></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.act.pic.path"/></td>
                        <td><input name="imgPath" class="form-control" type="text" placeholder="<@spring.message "admin.act.pic.path"/>"/></td>
                        <td class="text-right  media-middle"><@spring.message "admin.vaild.vip.group"/></td>
                        <td colspan="3"><#list groups as lv><label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${lv.id }"checked>${lv.name }</label></#list></td>
                    </tr>
                    <tr>
                        <td class="text-right  media-middle"><@spring.message "admin.valid.member.level"/></td>
                        <td colspan="5"><#list degrees as lv><label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${lv.id }"checked>${lv.name }</label></#list></td>
                    </tr>

                    <div class="main_red_set_info">
                        <tr class="main_red_bag_set_info">
                            <td class="text-right media-middle"><@spring.message "admin.min.charge.bet"/></td>
                            <td><input class="redBagRechargeMins form-control money " type="number" name="minNum" placeholder="<@spring.message "admin.min.charge.bet"/>"/></td>
                            <td class="text-right media-middle"><@spring.message "admin.max.charge.bet"/></td>
                            <td><input class="redBagRechargeMaxs form-control money " type="number" name="maxNum" placeholder="<@spring.message "admin.max.charge.bet"/>"/></td>
                            <td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.add.count.condition.max"/></h3>"><@spring.message "admin.play.count.may"/></a></td>
                            <td><input class="redBagRechargeMins form-control money " type="number" name="playNum" placeholder="<@spring.message "admin.play.count.may"/>"/></td>
                        </tr>
                    </div>

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button class="btn btn-success addOne"><@spring.message "admin.add.one.row"/></button>
                <button class="btn btn-danger delOne"><@spring.message "admin.last.one.row.del"/></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    requirejs(['jquery'], function () {
        var $form = $("#turntableAddMoneyFormId");
        $form.find('.addOne').click(function () {
            var addHtml = $('.main_red_bag_set_info').clone()[0];
            if (addHtml) {
                $('.modal-body tbody#red_bag_tbody_id').append(addHtml);
            }
            return false;
        });
        $form.find(".tooltip-show").tooltip({html: true}).hover(function () {
            $(this).tooltip('show');
        }, function () {
            $(this).tooltip('hide');
        });
        $form.find('.delOne').click(function () {
            var delHtml = $('.modal-body tbody#red_bag_tbody_id .main_red_bag_set_info:last');
            var delLength = $('.modal-body tbody#red_bag_tbody_id .main_red_bag_set_info').length;
            if (delLength <= 1) {
                layer.msg('<@spring.message "admin.have.not.last.one"/>!');
                return false;
            }
            if (delHtml) {
                delHtml.remove();
            }
            return false;
        });
        $form.submit(function () {
            var config = new Array();
            $form.find(".main_red_bag_set_info").each(function (i, v) {
                var $v = $(v), data = {}, min = 0, max = 0, num = 0;
                min = $v.find("input[name='minNum']").val();
                max = $v.find("input[name='maxNum']").val();
                num = $v.find("input[name='playNum']").val();
                data["minNum"] = min;
                data["maxNum"] = max;
                data["playNum"] = num;
                config.push(data);
            });
            $form.find("input[name='playConfig']").val(JSON.stringify(config));
        });
    });
</script>