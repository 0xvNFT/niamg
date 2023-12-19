<form action="${base}/admin/stationHomepageGame/addSave.do" id="add_category_form" class="form-submit" method="post">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加主页游戏</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <input type="hidden" name="parentGameCode" />
                    <input type="hidden" name="gameCode" />
                    <input type="hidden" name="isSubGame" />
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.menu.features"/>：</td>
                        <td class="text-left">
                            <select name="gameTabId" id="gameTabIdValue" class="form-control">
                                <#list gameTabList as type>
                                    <option value="${type.id}">${type.name}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.game.type.selected"/>：</td>
                        <td class="text-left">
                            <select name="gameType" class="form-control">
                                <#list gameTypeList as type>
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
	                    <td class="text-right media-middle">
 							<div class="input-group">
					            <select name="second_game_select_name" id="second_game_select_id" class="form-control selectpicker" title="<@spring.message "admin.deposit.type.all"/>" data-live-search="true" multiple data-selected-text-format="count>2">
					            </select>
					            <input name="gameCodeArr" value="${gameCodeArr}" id="gameCodeArr_id" type="hidden">
					        </div>
	                    </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left">
                        	<select name="status" class="form-control">
                                <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                                <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                            </select></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.serial.val.max.font"/>：</td>
                        <td class="text-left">
                            <input type="text" name="sortNo" class="form-control" value=""/>
                        </td>
                    </tr>

                    <tr class="hidden isShowTitle">
                        <td class="text-right media-middle"><@spring.message "admin.title.heading"/>：</td>
                        <td class="text-left"><input type="text" name="gameName" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>"/></td>
                    </tr>

                    <tr class="hidden isShowGameIcon">
                        <td class="text-right media-middle"><@spring.message "admin.game.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="imageUrl" class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <tr class="hidden isShowGameLink">
                        <td class="text-right media-middle"><@spring.message "admin.game.link.address"/>：</td>
                        <td class="text-left"><input type="text" name="thirdGameUrl" class="form-control" placeholder="<@spring.message "admin.input.link.address"/>"/></td>
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
    var $gameTabId=$form.find("[name='gameTabId']");//游戏标签ID
    var $type=$form.find("[name='gameType']");//游戏类型
    var $topGame=$form.find("[name='topGame']");
    var $isSubGame=$form.find("[name='isSubGame']");

    var $name=$form.find("[name='gameName']");
    var $code=$form.find("[name='gameCode']");
    var $parentGameCode=$form.find("[name='parentGameCode']");
    var $icon=$form.find("[name='imageUrl']");
    var $link=$form.find("[name='thirdGameUrl']");

    var selectedGameCode = '${hotGame.code}';
    var selectedParentGameCode = '${hotGame.parentCode}';

    var gameTabIdValue=$("#gameTabIdValue option:selected").val(); // 首次点击弹出新增框时，获取当前选中 gameTabId 的值
    
    var gameCodeArr = '${gameCodeArr}';
    var $selctpicker = $("#second_game_select_id");
    var $secondGameSelect=$form.find("[name='second_game_select_name']");
    var $secondGameValues=$("#gameCodeArr_id");
    var secondGameValues={};

</script>

<script src="${base}/common/js/homepageGame/homepagegame.js?v=1.0"></script>
