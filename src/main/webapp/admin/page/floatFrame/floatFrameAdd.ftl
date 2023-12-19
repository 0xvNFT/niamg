<style>.font-show-center{margin-top:25px;}</style>
<form action="${adminBase}/floatFrame/doAdd.do" class="form-submit" id="station_float_frame_add_formId">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.float.win"/><em class="text-danger">(<@spring.message "admin.notice.same.info"/>)</em></h4>
            </div>
            <div class="modal-body" style="max-height: 650px;overflow: auto;">
            <input type="hidden" name="frImgUrls"/><input type="hidden" name="frImgHoverUrls"/>
			<input type="hidden" name="frImgSorts"/><input type="hidden" name="frLinkTypes"/><input type="hidden" name="frLinkUrls"/>
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="13%" class="text-right media-middle"><@spring.message "admin.float.title"/></td>
                        <td width="20%"><input type="text" name="title" class="form-control"/></td>
                        <td width="10%" class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="bottom" data-toggle="tooltip" title="<h3><@spring.message "admin.turn.pc.valid"/>:<@spring.message "admin.picture.display"/><br/><@spring.message "admin.build.house"/>:<@spring.message "admin.pic.up.down.display"/></h3>"><@spring.message "admin.pic.types"/></a></td>
                        <td width="20%">
	                        <select name="imgType" class="form-control">
	                            <option value="2"><@spring.message "admin.page.turn.pic"/></option>
	                            <option value="1" selected><@spring.message "admin.build.house"/></option>
	                        </select>
                        </td>
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.status"/></td>
                        <td width="20%">
	                        <select name="status" class="form-control">
	                            <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
	                            <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
	                        </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.language.types"/></td>
                        <td>
							<select name="language" class="form-control">
								<#list languages as l>
                                    <option value="${l.locale.language}">${l.lang }</option>
								</#list>
							</select>
						</td>
                        <td class="text-right media-middle"><@spring.message "admin.display.terminal"/></td>
                        <td><select name="platform" class="form-control">
                            <option value="1" selected>WEB</option>
                            <option value="2">WAP</option>
                            <option value="3">APP</option>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.display.pages"/></td>
                        <td>
                        	<select name="showPage" class="form-control">
	                            <option value="1"><@spring.message "admin.website.page"/></option>
	                            <option value="2" selected><@spring.message "admin.personal.center"/></option>
	                            <option value="3"><@spring.message "admin.login.page"/></option>
	                            <option value="4"><@spring.message "admin.domain.homeRegister"/></option>
                        	</select>
						</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.begin.time"/></td>
                        <td><div class="input-group">
                            <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${beginDate}" name="beginTimes" placeholder="<@spring.message "manager.begin.time"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                        </div></td>
                        <td class="text-right media-middle"><@spring.message "manager.end.time"/></td>
                        <td>
	                        <div class="input-group">
	                            <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" name="overTimes" value="${endDate}" placeholder="<@spring.message "manager.end.time"/>"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
	                        </div>
                        </td>
                        <td class="text-right media-middle"><a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.cover.up.login.pos"/></h3>"><@spring.message "admin.display.position"/></a></td>
                        <td>
						    <select name="showPosition" class="form-control">
	                            <option value="left_top"><@spring.message "admin.left.up"/></option>
	                            <option value="left_middle_top"><@spring.message "admin.left.center.up"/></option>
	                            <option value="left_middle"><@spring.message "admin.left.middle.center"/></option>
	                            <option value="left_middle_bottom"><@spring.message "admin.left.middle.down"/></option>
	                            <option value="left_bottom"><@spring.message "admin.left.down"/></option>
	                            <option value="right_top"><@spring.message "admin.right.up"/></option>
	                            <option value="right_middle_top"><@spring.message "admin.right.middle.up"/></option>
	                            <option value="right_middle" selected><@spring.message "admin.right.center"/></option>
	                            <option value="right_middle_bottom"><@spring.message "admin.right.middle.down"/></option>
	                            <option value="right_bottom"><@spring.message "admin.right.down"/></option>
	                            <option value="middle_top"><@spring.message "admin.page.center.up"/></option>
	                            <option value="middle_bottom"><@spring.message "admin.page.center.down"/></option>
                        	</select>
						</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.valid.member.level"/></td>
                        <td colspan="2"><#list degrees as le><label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${le.id }" checked>${le.name}</label></#list></td>
                        <td class="text-right media-middle"><@spring.message "admin.vaild.vip.group"/></td>
                        <td colspan="2"><#list groups as le><label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${le.id }" checked>${le.name}</label></#list></td>
                    </tr>
                <div class="float_frame_clone">
                    <tr class="float_frame_clone_body">
                        <td width="10%" class="text-right media-middle"><@spring.message "admin.picture.address"/></td>
                        <td width="20%"><input type="text" class="frImgUrl form-control font-show-center"/></td>
                        <td width="10%" class="text-right media-middle">
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.mouse.move.up.address"/></h3>"><@spring.message "admin.picture.display.turn"/></a>
                        	<p></p>
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.serial.max.up"/></h3>"><@spring.message "admin.picture.serial"/></a>
                        </td>
                        <td width="20%">
                        	<input type="text" placeholder="<@spring.message "admin.mouse.pass.pic.address"/>" class="frImgHoverUrl form-control"/>
                        	--
                        	<input type="number" placeholder="<@spring.message "admin.picture.serial"/>" class="frImgSort form-control"/>
                        </td>
                        <td width="10%" class="text-right media-middle">
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.link.open.way"/></h3>"><@spring.message "admin.link.types"/></a>
                        	<p></p>
                        	<a href="javascript:;" class="tooltip-show" data-placement="right" data-toggle="tooltip" title="<h3><@spring.message "admin.link.open.address"/><h5><@spring.message "admin.close.press.not.address"/></h5></h3>"><@spring.message "admin.link.address"/></a>
                        </td>
                        <td width="20%">
                        	<select class="frLinkType form-control">
	                            <option value="1"><@spring.message "admin.common.turn"/></option>
	                            <option value="2" selected><@spring.message "admin.new.win.open"/></option>
	                            <option value="4"><@spring.message "admin.windows.open"/></option>
	                            <option value="3"><@spring.message "admin.close.press"/></option>
                        	</select>
                        	--
                        	<input type="text" class="frLinkUrl form-control" placeholder="<@spring.message "admin.link.address"/>"/>
                       </td>
                    </tr>
                </div>
                    </tbody>
                </table>
            </div>
       		<div class="modal-footer">
				<button class="btn btn-success addOne" type="button"><@spring.message "admin.add.picture.one"/></button>
				<button class="btn btn-danger delOne" type="button"><@spring.message "admin.del.down.picture"/></button>
			</div>
            <div class="modal-footer">
            	<em class="text-danger"><@spring.message "admin.skill.blue.font.dis"/></em>
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary checkFloatFrameInfo"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div>
</form>
<script>
requirejs(['jquery'],function(){
	var $form=$("#station_float_frame_add_formId");
	$form.find('.addOne').click(function(){
		var addHtml = $form.find('.float_frame_clone_body').clone()[0];
		if(addHtml){
			$form.find('.float_frame_clone_body:last').after(addHtml);
		}
		return false;
	})
	$form.find(".tooltip-show").tooltip({html : true }).hover(function(){
		$(this).tooltip('show');
	},function(){
		$(this).tooltip('hide');
	});
	$form.find('.delOne').click(function(){
		var delHtml = $form.find('.float_frame_clone_body:last');
		var delLength = $form.find('.float_frame_clone_body').length;
		if(delLength <= 1){
		   layer.msg('<@spring.message "admin.one.picture.del.not"/>!');
		   return false;			
		}
		if(delHtml){
			delHtml.remove();
		}
		return false;
	})
	$form.find('.checkFloatFrameInfo').click(function(){
		var isChecked = false;
		var frImgUrl=[];
		var frImgHoverUrl=[];
		var frImgSort=[];
		var frLinkType=[];
		var frLinkUrl=[];
		$form.find('.float_frame_clone_body').each(function(i,j){
			var frImgUrls = $(this).find('.frImgUrl').val();//图片地址
			var frImgHoverUrls = $(this).find('.frImgHoverUrl').val();//鼠标经过图片地址
			var frImgSorts = $(this).find('.frImgSort').val();//图片序号
			var frLinkTypes = $(this).find('.frLinkType').val();//图片链接类型
			var frLinkUrls = $(this).find('.frLinkUrl').val();//图片链接地址
			if(!frImgUrls){
			    layer.msg('<@spring.message "admin.float.pic.address.blank"/>!');
			    return false;
			}
			if(!frImgHoverUrls){
				frImgHoverUrls = '&_&';
			}
			if(!frImgSorts){
				frImgSorts = 1;
			}else if(frImgSorts < 0){
				layer.msg('<@spring.message "admin.pic.serial.big.zero"/>!');
			    return false;				
			}
			if(!frLinkTypes){
				layer.msg('<@spring.message "admin.pic.address.not.blank"/>!');
			    return false;
			}else if(frLinkTypes == 3){
				frLinkUrls = '&_&';
			}else if(!frLinkUrls && frLinkTypes != 3){
				frLinkUrls = '&_&';
			}
			frImgUrl.push(frImgUrls);
			frImgHoverUrl.push(frImgHoverUrls);
			frImgSort.push(frImgSorts);
			frLinkType.push(frLinkTypes);
			frLinkUrl.push(frLinkUrls);
			isChecked = true;
		})
		if(!isChecked){
			return false;
		}
		$form.find('[name="frImgUrls"]').val(frImgUrl);
		$form.find('[name="frImgHoverUrls"]').val(frImgHoverUrl);
		$form.find('[name="frImgSorts"]').val(frImgSort);
		$form.find('[name="frLinkTypes"]').val(frLinkType);
		$form.find('[name="frLinkUrls"]').val(frLinkUrl);
	})
})
</script>