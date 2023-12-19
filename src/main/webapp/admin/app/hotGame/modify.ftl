<form action="${base}/admin/appHotGame/modifySave.do" id="add_category_form" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.hot.game"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <input type="hidden" name="code" />
                    <input type="hidden" name="id" value="${hotGame.id}" />
                    <input type="hidden" name="parentCode" value="${hotGame.parentCode}"/>
                    <input type="hidden" name="isSubGame" />

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.game.type.selected"/>：</td>
                        <td class="text-left">
                            <select name="type" class="form-control">
                                <#list typeList as type>
                                    <option value="${type.type}" <#if (type.type == hotGame.type)>selected</#if>>${type.title}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>

                    <!-- 顶级游戏 -->
                    <tr class="isShowTopGame">
                        <td class="text-right media-middle"><@spring.message "admin.one.level"/>：</td>
                        <td class="text-left">
                            <select name="topGame" class="form-control">

                            </select>
                        </td>
                    </tr>

                    <!-- 二级游戏 -->
                    <tr class="hidden isShowSecondGame">
                        <td class="text-right media-middle"><@spring.message "admin.two.level"/>：</td>
                        <td class="text-left">
                            <select name="secondGame" class="form-control">

                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left">
                            <select name="status" class="form-control">
                                <#if (hotGame.status == 2)>
                                    <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                                    <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                                <#else>
                                    <option value="2"><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                                    <option value="1" selected><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                                </#if>
                            </select></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.serial.val.max.font"/>：</td>
                        <td class="text-left">
                            <input type="text" name="sortNo" class="form-control" value="${hotGame.sortNo}"/>
                        </td>
                    </tr>

                    <tr class="hidden isShowTitle">
                        <td class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td class="text-left"><input type="text" name="name" value="${hotGame.name}" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>"/></td>
                    </tr>

                    <!-- 宣传图地址 -->
                    <tr class="hidden isShowGameIcon">
                        <td class="text-right media-middle"><@spring.message "admin.game.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="icon" ${hotGame.icon} class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <!-- 宣传图点击跳转链接地址 -->
                    <tr class="hidden isShowGameLink">
                        <td class="text-right media-middle"><@spring.message "admin.game.link.address"/>：</td>
                        <td class="text-left"><input type="text" name="link" ${hotGame.link} class="form-control" placeholder="<@spring.message "admin.input.link.address"/>"/></td>
                    </tr>

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
    var baseUrl = '${base}';
    var $form=$("#add_category_form");//表单控件

    var $name=$form.find("[name='name']");
    var $code=$form.find("[name='code']");
    var $type=$form.find("[name='type']");//游戏类型
    var $parentGameCode=$form.find("[name='parentCode']");
    var $icon=$form.find("[name='icon']");
    var $link=$form.find("[name='link']");

    var $topGame=$form.find("[name='topGame']");
    var $secondGame=$form.find("[name='secondGame']");
    var $isSubGame=$form.find("[name='isSubGame']");

    var selectedGameCode = '${hotGame.code}'
    var selectedParentGameCode = '${hotGame.parentCode}'
</script>
<script src="${base}/common/js/app/hotgame.js?v=2.9"></script>
