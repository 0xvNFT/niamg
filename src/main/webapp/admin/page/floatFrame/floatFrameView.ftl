<style>.font-show-center{margin-top: 25px;}</style>
<form class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.float.win.view"/></h4>
            </div>
            <div class="modal-body" style="max-height: 650px;overflow: auto;">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="13%" class="text-right media-middle"><@spring.message "admin.float.title"/></td>
                        <td width="20%" class="text-left"><input type="text" value="${ff.title }" class="form-control" disabled/></td>
                        <td width="10%" class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="bottom" data-toggle="tooltip" title="<h3><@spring.message "admin.turn.pc.valid"/>:<@spring.message "admin.picture.display"/><br/><@spring.message "admin.build.house"/>:<@spring.message "admin.pic.up.down.display"/></h3>"><@spring.message "admin.pic.types"/></a></td>
                        <td width="20%"><select name="imgType" class="form-control">
                            <option value="2"<#if ff.imgType == 2>selected</#if>><@spring.message "admin.page.turn.pic"/></option>
                            <option value="1"<#if ff.imgType == 1>selected</#if>><@spring.message "admin.build.house"/></option>
                        </select></td>
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.status"/></td>
                        <td width="20%">
	                        <select name="status" class="form-control">
	                            <option value="2"<#if ff.status == 2>selected</#if>><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
	                            <option value="1"<#if ff.status == 1>selected</#if>><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
	                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.language.types"/></td>
                        <td><select name="language" class="form-control"><#list languages as g><option value="${g.name()}"<#if ff.language==g.name()>selected</#if>>${g.desc }</option></#list></select></td>
                        <td class="text-right media-middle"><@spring.message "admin.display.terminal"/></td>
                        <td><select name="platform" class="form-control">
                            <option value="1"<#if ff.platform ==1>selected</#if>>WEB</option>
                            <option value="2"<#if ff.platform ==2>selected</#if>>WAP</option>
                            <option value="3"<#if ff.platform ==3>selected</#if>>APP</option>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.display.pages"/></td>
                        <td>
                        	<select name="showPage" class="form-control">
	                            <option value="1" <#if ff.showPage == 1>selected</#if>><@spring.message "admin.website.page"/></option>
	                            <option value="2" <#if ff.showPage == 2>selected</#if>><@spring.message "admin.personal.center"/></option>
	                            <option value="3" <#if ff.showPage == 3>selected</#if>><@spring.message "admin.login.page"/></option>
	                            <option value="4" <#if ff.showPage == 4>selected</#if>><@spring.message "admin.domain.homeRegister"/></option>
	                            <option value="5" <#if ff.showPage == 5>selected</#if>><@spring.message "admin.lottery.pay.page"/></option>
                        	</select>
						</td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "manager.begin.time"/></td>
                        <td><input type="text" class="form-control" value="${ff.beginTime}" name="beginTimes"/></td>
                        <td class="text-right media-middle"><@spring.message "manager.end.time"/></td>
                        <td><input type="text" class="form-control"value="${ff.overTime}"/></td>
                        <td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.cover.up.login.pos"/></h3>"><@spring.message "admin.display.position"/></a></td>
                        <td>
						    <select name="showPosition" class="form-control" disabled>
	                            <option value="left_top"<#if ff.showPosition == 'left_top'>selected</#if>><@spring.message "admin.left.up"/></option>
	                            <option value="left_middle_top"<#if ff.showPosition == 'left_middle_top'>selected</#if>><@spring.message "admin.left.center.up"/></option>
	                            <option value="left_middle"<#if ff.showPosition == 'left_middle'>selected</#if>><@spring.message "admin.left.middle.center"/></option>
	                            <option value="left_middle_bottom"<#if ff.showPosition == 'left_middle_bottom'>selected</#if>><@spring.message "admin.left.middle.down"/></option>
	                            <option value="left_bottom"<#if ff.showPosition == 'left_bottom'>selected</#if>><@spring.message "admin.left.down"/></option>
	                            <option value="right_top"<#if ff.showPosition == 'right_top'>selected</#if>><@spring.message "admin.right.up"/></option>
	                            <option value="right_middle_top"<#if ff.showPosition == 'right_middle_top'>selected</#if>><@spring.message "admin.right.middle.up"/></option>
	                            <option value="right_middle"<#if ff.showPosition == 'right_middle'>selected</#if>><@spring.message "admin.right.center"/></option>
	                            <option value="right_middle_bottom"<#if ff.showPosition == 'right_middle_bottom'>selected</#if>><@spring.message "admin.right.middle.down"/></option>
	                            <option value="right_bottom"<#if ff.showPosition == 'right_bottom'>selected</#if>><@spring.message "admin.right.down"/></option>
	                            <option value="middle_top"<#if ff.showPosition == 'middle_top'>selected</#if>><@spring.message "admin.page.center.up"/></option>
	                            <option value="middle_bottom"<#if ff.showPosition == 'middle_bottom'>selected</#if>><@spring.message "admin.page.center.down"/></option>
                        	</select>
						</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/></td>
                        <td colspan="2"><#list degrees as le><label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${le.id }"<#if degreeSet?seq_contains(le.id)>checked</#if>>${le.name}</label></#list></td>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/></td>
                        <td colspan="2"><#list groups as le><label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${le.id }"<#if groupSet?seq_contains(le.id)>checked</#if>>${le.name}</label></#list></td>
                    </tr>
                    <div class="float_frame_clone">
                    <#list affsList as affs>
                    <tr class="float_frame_clone_body">
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.picture.address"/></td>
                        <td width="20%" class="text-left"><img src="${affs.imgUrl }" style="width: 105px;height: 105px;"></td>
                        <td width="10%" class="text-right media-middle">
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.mouse.move.up.address"/></h3>"><@spring.message "admin.picture.display.turn"/></a>
                        	<p></p>
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.serial.max.down"/></h3>"><@spring.message "admin.picture.serial"/></a>
                        </td>
                        <td width="20%" class="text-left">
                        	<input type="text" placeholder="<@spring.message "admin.mouse.pass.pic.address"/>" disabled value="${affs.imgHoverUrl }" class="frImgHoverUrl form-control"/>
                        	--
                        	<input type="text" placeholder="<@spring.message "admin.picture.serial"/>" disabled value="${affs.imgSort }" class="frImgSort form-control"/>
                        </td>
                        <td width="10%" class="text-right media-middle">
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.link.open.way"/></h3>"><@spring.message "admin.link.types"/></a>
                        	<p></p>
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.link.open.address"/><h5><@spring.message "admin.close.press.not.address"/></h5></h3>"><@spring.message "admin.link.address"/></a>
                        </td>
                        <td width="20%" class="text-left">
                        	<select class="frLinkType form-control" disabled>
	                            <option value="1" <#if affs.linkType == 1>selected</#if>><@spring.message "admin.common.turn"/></option>
	                            <option value="2" <#if affs.linkType == 2>selected</#if>><@spring.message "admin.new.win.open"/></option>
	                            <option value="4" <#if affs.linkType == 4>selected</#if>><@spring.message "admin.windows.open"/></option>
	                            <option value="3" <#if affs.linkType == 3>selected</#if>><@spring.message "admin.close.press"/></option>
                        	</select>
                        	--
                        	<input type="text" class="frLinkUrl form-control" value="${affs.linkUrl }" disabled/>
                       </td>
                    </tr>
                    </#list>
                    </div>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
            </div>
        </div>
</form>
<script>
requirejs(['jquery'],function(){
	$(".tooltip-show").tooltip({html : true });
	$(".tooltip-show").hover(function(){
		$(this).tooltip('show');
	},function(){
		$(this).tooltip('hide');
	});
})
</script>