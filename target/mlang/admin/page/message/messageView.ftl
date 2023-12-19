<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
            <h4 class="modal-title"><@spring.message "admin.station.info.view"/></h4>
        </div>
        <div class="modal-body">
            <table class="table table-bordered table-striped">
                <tbody>
                <tr>
                    <td width="30%" class="text-right media-middle"><@spring.message "admin.receive.person"/>：</td>
                    <td><select name="receiveType" class="form-control" disabled>
                            <option value="1"<#if message.receiveType == 1>selected</#if>><@spring.message "admin.simple.vip"/></option>
                            <option value="2"<#if message.receiveType == 2>selected</#if>><@spring.message "admin.point.multi.vip"/></option>
                            <option value="3"<#if message.receiveType == 3>selected</#if>><@spring.message "admin.all.group.send"/></option>
                            <option value="4"<#if message.receiveType == 4>selected</#if>><@spring.message "admin.withdraw.table.degreeName"/></option>
                            <option value="5"<#if message.receiveType == 5>selected</#if>><@spring.message "admin.vip.group"/></option>
                            <option value="6"<#if message.receiveType == 6>selected</#if>><@spring.message "admin.proxy.line.multi"/></option>
                        </select></td>
                </tr>
                <#if message.receiveType == 1><tr>
                    <td class="text-right media-middle"><@spring.message "admin.receive.per"/>：</td>
                    <td><input type="text" class="form-control" name="username" value="${username}" disabled/></td>
                </tr></#if>
                <#if message.receiveType == 6>
                <tr>
                    <td class="text-right media-middle"><@spring.message "admin.proxy.line.multi"/>：</td>
                    <td><input type="text" class="form-control" name="proxyName" value="${message.proxyName}" disabled/></td>
                </tr>
                </#if>
                <#if message.receiveType == 4>
                <tr>
                    <td class="text-right media-middle"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                    <td><select name="levelGroup" class="form-control" disabled>
                        <#list degrees as le>
                            <#if le.id == message.degreeId>
                                <option value="${le.id}" selected>${le.name}</option>
                            </#if>
                        </#list>
                    </select>
                    </td>
                </tr>
                </#if>
                <#if message.receiveType == 5>
                <tr>
                    <td class="text-right media-middle"><@spring.message "admin.vip.group"/>：</td>
                    <td><select name="levelGroup" class="form-control" disabled>
                        <#list groups as le>
                            <#if le.id == message.groupId>
                                <option value="${le.id}" selected>${le.name}</option>
                            </#if>
                        </#list>
                    </select>
                    </td>
                </tr>
                </#if>
                <tr>
                    <td class="text-right media-middle"><@spring.message "admin.station.title.info"/>：</td>
                    <td><input type="text" class="form-control" name="title" value="${message.title}" disabled/>
                    </td>
                </tr>
                <tr>
                    <td class="text-right media-middle"><@spring.message "admin.station.content.info"/>：</td>
                    <td><textarea class="form-control" name="content" disabled>${message.content}</textarea></td>
                </tr>
                <tr>
                    <td width="30%" class="text-right media-middle"><@spring.message "admin.is.not.to.show"/>：</td>
                    <td>
                        <input type="text" class="form-control" name="popStatus" value="<#if message.popStatus == 1><@spring.message "admin.withdraw.info.boolean.no"/></#if><#if message.popStatus == 2><@spring.message "admin.withdraw.info.boolean.yes"/></#if>" disabled/></td>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
