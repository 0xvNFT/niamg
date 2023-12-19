<form action="${base}/admin/appGameFoot/addSave.do" id="add_category_form" class="form-submit" method="post">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.foot.print"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <input type="hidden" name="gameCode" />
                    <input type="hidden" name="parentGameCode" />
                    <input type="hidden" name="isSubGame" />
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.game.type.selected"/>：</td>
                        <td class="text-left">
                            <select name="gameType" class="form-control">
                                <#list typeList as type>
                                    <option value="${type.type}">${type.title}</option>
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
                        <td class="text-left"><select name="status" class="form-control">
                                <option value="2" selected><@spring.message "admin.enable"/></option>
                                <option value="1"><@spring.message "admin.disabled"/></option>
                            </select></td>
                    </tr>

                    <tr class="hidden isShowTitle">
                        <td class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td class="text-left"><input type="text" name="gameName" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>" /></td>
                    </tr>

                    <!-- 宣传图地址 -->
                    <tr class="hidden isShowGameIcon">
                        <td class="text-right media-middle"><@spring.message "admin.game.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="customIcon" class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <!-- 宣传图点击跳转链接地址 -->
                    <tr class="hidden isShowGameLink">
                        <td class="text-right media-middle"><@spring.message "admin.game.link.address"/>：</td>
                        <td class="text-left"><input type="text" name="customLink" class="form-control" placeholder="<@spring.message "admin.input.link.address"/>"/></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.visit.count"/>：</td>
                        <td class="text-left"><input type="text" name="visitNum" class="form-control" placeholder="<@spring.message "admin.input.visit.count"/>" /></td>
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

<script type="text/javascript">
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

    var selectedGameCode = '${hotGame.gameCode}';
    var selectedParentGameCode = '${hotGame.parentGameCode}';
</script>

<script src="${base}/common/js/app/hotgame.js?v=2.8"></script>
