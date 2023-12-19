<form action="${adminBase}/adminUserGroup/addAuth.do" class="form-submit" id="setAuthFormId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.messageArgs code="admin.adminUserGroup.setAuthTitle" args=["${group.name}"]/></h4>
            </div><input type="hidden" value="${group.id}" name="groupId">
            <div class="modal-body">
                <div class="main-panel" style="height:400px;overflow:auto;">
                    <label class="checkbox-inline"><input type="checkbox" name="selectAll"><@spring.message "admin.select.all"/></label><#t>
                <#list menus as me>
                    <div class="checkbox"><label><input type="checkbox" class="menu" name="menuId" value="${me.id}"<#if menuIds?seq_contains(me.id)>checked</#if>> ${me.title}</label><#t>
                        <div class="checkbox secCheckBox"><#t>
                            <#if me.childs?has_content><#list me.childs as me1>
                                <label><input type="checkbox" class="semenu" name="menuId" first="${me.id}" value="${me1.id}"<#if menuIds?seq_contains(me1.id)>checked</#if>> ${me1.title}</label><#t>
                                <div class="checkbox"><#t>
                                    <#if me1.childs?has_content><#list me1.childs as me2><label class="checkbox-inline thdCheckBox"><#t>
                                        <input type="checkbox" first="${me.id}" name="menuId" second="${me1.id}" value="${me2.id}"<#if menuIds?seq_contains(me2.id)>checked</#if>> ${me2.title}</label></#list></#if><#t>
                                </div><#t>
                            </#list></#if>
                        </div><#t>
                    </div></#list><#t>
                </div><#t>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                	<button class="btn btn-primary"><@spring.message "admin.save"/></button>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    requirejs(['jquery'],function(){
        var form=$("#setAuthFormId");
        form.find("[name='selectAll']").click(function(){
            var it=$(this);
            if(it.prop("checked")){
                form.find("[type='checkbox']").prop("checked",true);
            }else{
                form.find("[type='checkbox']").prop("checked",false);
            }
        });
        form.find(".menu").click(function(){
            var it=$(this),val=it.val();
            if(it.prop("checked")){
                form.find("[type='checkbox'][first='"+val+"']").prop("checked",true);
            }else{
                form.find("[type='checkbox'][first='"+val+"']").prop("checked",false);
            }
        }).each(function(){
            var it=$(this),val=it.val(),ops=$("[type='checkbox'][first='"+val+"']");
            if(ops.length==0)return;
            if(ops.length==ops.filter(":checked").length){
                it.prop("checked",true);
            }
        });
        form.find(".semenu").click(function(){
            var it=$(this),val=it.val();
            if(it.prop("checked")){
                form.find("[type='checkbox'][second='"+val+"']").prop("checked",true);
            }else{
                form.find("[type='checkbox'][second='"+val+"']").prop("checked",false);
            }
        }).each(function(){
            var it=$(this),val=it.val(),ops=$("[type='checkbox'][second='"+val+"']");
            if(ops.length==0)return;
            if(ops.length==ops.filter(":checked").length){
                it.prop("checked",true);
            }
        });

    });
</script>