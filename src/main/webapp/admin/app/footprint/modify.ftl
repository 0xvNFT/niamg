<form action="${base}/admin/appGameFoot/modifySave.do" id="add_category_form" class="form-submit" method="post">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.foot.print"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <input type="hidden" name="gameCode" />
                    <input type="hidden" name="id" value="${hotGame.id}" />
                    <input type="hidden" name="parentGameCode" value="${hotGame.parentGameCode}"/>
                    <input type="hidden" name="isSubGame" value="${hotGame.isSubGame}" />
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.game.type.selected"/>：</td>
                        <td class="text-left">
                            <select name="gameType" class="form-control">
                                <#list typeList as type>
                                    <option value="${type.type}" <#if (type.type == hotGame.gameType)>selected</#if>>${type.title}</option>
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
                                    <option value="2" selected><@spring.message "admin.enable"/></option>
                                    <option value="1"><@spring.message "admin.disabled"/></option>
                                <#else>
                                    <option value="2"><@spring.message "admin.enable"/></option>
                                    <option value="1" selected><@spring.message "admin.disabled"/></option>
                                </#if>
                            </select></td>
                    </tr>

                    <tr class="hidden isShowTitle">
                        <td class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td class="text-left"><input type="text" value="${hotGame.gameName}" name="gameName" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>" /></td>
                    </tr>

                    <tr class="hidden isShowGameIcon">
                        <td class="text-right media-middle"><@spring.message "admin.game.pic.address"/>：</td>
                        <td class="text-left"><input type="text" value="${hotGame.customIcon}" name="customIcon" class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <tr class="hidden isShowGameLink">
                        <td class="text-right media-middle"><@spring.message "admin.game.link.address"/>：</td>
                        <td class="text-left"><input type="text" value="${hotGame.customLink}" name="customLink" class="form-control" placeholder="<@spring.message "admin.input.link.address"/>"/></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.visit.count"/>：</td>
                        <td class="text-left"><input type="text" value="${hotGame.visitNum}" name="visitNum" class="form-control" placeholder="<@spring.message "admin.input.visit.count"/>" /></td>
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
    var $type=$form.find("[name='gameType']");//游戏类型
    var $topGame=$form.find("[name='topGame']");
    var $secondGame=$form.find("[name='secondGame']");

    var $name=$form.find("[name='gameName']");
    var $code=$form.find("[name='gameCode']");
    var $parentGameCode=$form.find("[name='parentGameCode']");
    var $icon=$form.find("[name='customIcon']");
    var $link=$form.find("[name='customLink']");
    var $isSubGame=$form.find("[name='isSubGame']");

    var selectedGameCode = '${hotGame.gameCode}'
    var selectedParentGameCode = '${hotGame.parentGameCode}'
</script>

<script src="${base}/common/js/app/hotgame.js?v=2.8"></script>
