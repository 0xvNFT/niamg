<form action="${base}/admin/appTab/modifySave.do" id="add_category_form" class="form-submit" method="post">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.main.menu"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <input type="hidden" name="id"  value="${menu.id}"/>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.menu.features"/>：</td>
                        <td class="text-left">
                            <select name="type" class="form-control">
                                <#list funcLists as type>
                                    <option value="${type.type}" <#if (type.type == menu.type)>selected</#if>>${type.tabName}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                                <#if (menu.status == 2)>
                                    <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                                    <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                                <#else>
                                    <option value="2"><@spring.message "admin.enable"/></option>
                                    <option value="1" selected><@spring.message "admin.disabled"/></option>
                                </#if>
                                </option>
                            </select></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.serial.val.min.font"/>：</td>
                        <td class="text-left">
                            <input type="text" name="sortNo" value="${menu.sortNo}" class="form-control" />
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.define.title"/>：</td>
                        <td class="text-left"><input type="text" name="customTitle" value="${menu.customTitle}" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>"/></td>
                    </tr>

                    <!-- 游戏图标地址 -->
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.define.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="icon" value="${menu.icon}" class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <!-- 选中图地址 -->
                    <tr>
                        <td class="text-right media-middle">selected <@spring.message "admin.define.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="selectedIcon" value="${menu.selectedIcon}" class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
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

