<form class="form-submit" action="${adminBase}/userDegree/doModifyUpgrade.do" id="doModifyUpgrade_formId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.degree.upgradeTitle"/></h4>
                <em class="text-danger"><@spring.message "admin.degree.upgradeTip"/></em>
            </div>
            <div class="modal-body" style="max-height: 600px;overflow: auto;">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <th><@spring.message "admin.name"/></th>
                        <th><#if defaultDegree.type == 1><@spring.message "admin.deposit"/><#elseif defaultDegree.type == 2><@spring.message "admin.betNum"/><#else><@spring.message "admin.deposit"/>/<@spring.message "admin.betNum"/></#if></th>
                        <th><@spring.message "admin.degree.upgradeMoney"/></th>
                        <th><@spring.message "admin.degree.skipMoney"/></th>
                        <th><@spring.message "admin.degree.betNum1"/></th>
                    </tr>
                    <#list allDegree as level>
                        <tr data-id="${level.id}" data-index="${level_index}" class="level_gift_con_tr">
                            <td class="media-middle">${level.name}：</td>
                            <td class="media-middle"><#if defaultDegree.type == 1>${level.depositMoney}<#elseif defaultDegree.type == 2>${level.betNum}<#else>${level.depositMoney}/${level.betNum}</#if></td>
                            <td class="media-middle">
                                <input <#if level_index == 0>disabled value="0" <#else>value="${level.upgradeMoney}" </#if> class="form-control upgradeGift"/>
                            </td>
                            <td class="media-middle">
                                <input readonly  class="form-control skipGift"/>
                            </td>
                            <td>
                                <input value="${level.betRate}"  class="form-control betRate"  placeholder="<@spring.message "admin.degree.betNum1"/>" />
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <input type="hidden" name="configs">
            <div class="modal-footer">
	            <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
	            <button class="btn btn-primary"><@spring.message "admin.save"/></button>
	        </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    requirejs([], function () {
        let $form = $("#doModifyUpgrade_formId");
        skipGiftHandle();

        $form.find(".upgradeGift").on("change",function (e) {
            skipGiftHandle();
        });

        function skipGiftHandle() {
            let skipGift = 0;
            $.each($form.find(".level_gift_con_tr"),function (index,value) {
                let upgradegift = $(value).find(".upgradeGift").val();
                if (upgradegift)  {
                    skipGift += parseInt(upgradegift);
                }
                $(value).find(".skipGift").val(skipGift);
            });
        }

        $form.submit(function () {
            let arr = new Array();
            $.each($form.find(".level_gift_con_tr"),function (index,value) {
                let v = {};
                v["id"] = $(value).data("id");
                v["upgradeGift"] = $(value).find(".upgradeGift").val();
                v["skipGift"] = $(value).find(".skipGift").val();
                v["betRate"] = $(value).find(".betRate").val();
                arr.push(v);
            });
            $form.find("input[name='configs']").val(JSON.stringify(arr));
        })
    })
</script>