<form action="${base}/admin/stationHomepageGame/modifySave.do" id="add_category_form" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.hot.game"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <input type="hidden" name="gameCode" />
                    <input type="hidden" name="id" value="${homepageGame.id}" />
                    <input type="hidden" name="parentGameCode" value="${homepageGame.parentGameCode}"/>
                    <input type="hidden" name="isSubGame" />
 					<tr>
                        <td class="text-right media-middle"><@spring.message "admin.menu.features"/>：</td>
                        <td class="text-left">
                            <select name="gameTabId" class="form-control">
                                <#list gameTabList as type>
                                    <option value="${type.id}" <#if (type.id == homepageGame.gameTabId)>selected</#if>>${type.name}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.game.type.selected"/>：</td>
                        <td class="text-left">
                            <select name="gameType" class="form-control">
                                <#list gameTypeList as type>
                                    <option value="${type.type}" <#if (type.type == homepageGame.gameType)>selected</#if>>${type.title}</option>
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
                                <#if (homepageGame.status == 2)>
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
                            <input type="text" name="sortNo" class="form-control" value="${homepageGame.sortNo}"/>
                        </td>
                    </tr>

                    <tr class="hidden isShowTitle">
                        <td class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td class="text-left"><input type="text" name="gameName" value="${homepageGame.gameName}" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>"/></td>
                    </tr>

                    <!-- 宣传图地址 -->
                    <tr class="hidden isShowGameIcon">
                        <td class="text-right media-middle"><@spring.message "admin.game.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="imageUrl" ${homepageGame.imageUrl} class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <!-- 宣传图点击跳转链接地址 -->
                    <tr class="hidden isShowGameLink">
                        <td class="text-right media-middle"><@spring.message "admin.game.link.address"/>：</td>
                        <td class="text-left"><input type="text" name="thirdGameUrl" ${homepageGame.thirdGameUrl} class="form-control" placeholder="<@spring.message "admin.input.link.address"/>"/></td>
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

    var $name=$form.find("[name='gameName']");
    var $code=$form.find("[name='gameCode']");
    var $type=$form.find("[name='gameType']");//游戏类型
    var $parentGameCode=$form.find("[name='parentGameCode']");
    var $icon=$form.find("[name='imageUrl']");
    var $link=$form.find("[name='thirdGameUrl']");

    var $topGame=$form.find("[name='topGame']");
    var $secondGame=$form.find("[name='secondGame']");
    var $isSubGame=$form.find("[name='isSubGame']");

    var selectedGameCode = '${homepageGame.gameCode}'
    var selectedParentGameCode = '${homepageGame.parentGameCode}'
</script>
<script src="${base}/common/js/homepageGame/homepagegame.js?v=1.0"></script>
