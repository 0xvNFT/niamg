<form action="${base}/admin/appIndexMenu/addSave.do" id="add_category_form" class="form-submit" method="post">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.main.menu"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.menu.features"/>：</td>
                        <td class="text-left">
                            <select name="code" class="form-control">
                                <#list typeList as type>
                                    <option value="${type.code}">${type.title}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                                <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                                <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                            </select></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.serial.val.min.font"/>：</td>
                        <td class="text-left">
                            <input type="text" name="sortNo" class="form-control" value=""/>
                        </td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.define.title"/>：</td>
                        <td class="text-left"><input type="text" name="name" class="form-control" placeholder="<@spring.message "admin.input.string.title"/>"/></td>
                    </tr>

                    <!-- 地址 -->
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.define.pic.address"/>：</td>
                        <td class="text-left"><input type="text" name="icon" class="form-control" placeholder="<@spring.message "admin.input.pic.address"/>"/></td>
                    </tr>

                    <!-- 点击跳转链接地址 -->
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.define.link.address"/>：</td>
                        <td class="text-left"><input type="text" name="link" class="form-control" placeholder="<@spring.message "admin.input.link.address"/>"/></td>
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

