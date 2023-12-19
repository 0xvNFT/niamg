<link href="${base}/common/member/floatFrame/css/floatFrame.css?v=1.0.3" rel="stylesheet">
<div id="main_float_frame_id">
	<#list floatFrameList as ffl>
		<ul class="float_frame_close_id_${ffl.id } main_float_frame_ul ${ffl.showPosition }">
				<#if ffl.imgType == 2>
					<div>
						<div id="floatFreameSlideBox" class="floatFreameSlideBox">
							<div class="fdhd dian">
								<ul>
									<#list ffl.afsList as afs>
										<li <#if afs_index == 0>class="on"</#if>></li>
									</#list>
								</ul>
							</div>
							<div class="fdbd lunbo">
								<ul>
									<#list ffl.afsList as afs>
										<li style="display: none;"><a href="<#if afs.linkType == 3>javascript:clearSlideBox(${ffl.id })<#else>${afs.linkUrl }</#if>" <#if afs.linkType == 2>target="_blank"</#if>><img src="${afs.imgUrl }"></a></li>
									</#list>
								</ul>
							</div>
							<a class="prev" href="javascript:void(0)"></a>
							<a class="next" href="javascript:void(0)"></a>
						</div>
						<a href="javascript:clearSlideBox(${ffl.id })" class="clear" style="position: absolute;display: inline-table;<#if ffl.showPosition == 'right_top' || ffl.showPosition == 'right_middle' || ffl.showPosition == 'right_bottom'>left: 0<#else>right: 0</#if>;top: 0">
							<img src="${base}/common/member/floatFrame/images/close.png">
						</a>
					</div>
				<#else>
					<#list ffl.afsList as afs>
						<li>
							<a data-id="${ ffl_index}${afs_index }" class="wh_common show-hide-over-img" href="<#if afs.linkType == 3>javascript:closeFloatFrame(${ffl.id })<#elseif afs.linkType == 4>javascript:toWin('${afs.linkUrl }','newWindowFloatFrame')<#else>${afs.linkUrl }</#if>" <#if afs.linkType == 2>target="_blank"</#if>>
								<img src="${afs.imgUrl }" class="wh_common_child">
								<#if afs.imgHoverUrl?has_content>
									<img src="${afs.imgHoverUrl }" class="over-img-id ${ffl.showPosition } over-img-id-${ ffl_index}${afs_index }" style="display: none">
								</#if>
							</a>
						</li>
					</#list>
				</#if>
		</ul>
	</#list>
</div>
<script src="${base}/common/member/floatFrame/js/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript">
$(function(){
	jQuery("#main_float_frame_id .floatFreameSlideBox").slide({mainCell:".fdbd.lunbo ul",interTime:"6000",autoPlay:true});
})
function clearSlideBox(fid){
	$('#main_float_frame_id .float_frame_close_id_'+fid).fadeOut();
}

function closeFloatFrame(fid){
	$('#main_float_frame_id .float_frame_close_id_'+fid).hide();
}
$('#main_float_frame_id .show-hide-over-img').hover(function(){
	$('.over-img-id-'+$(this).data('id')).show();
},function(){
	$('.over-img-id-'+$(this).data('id')).hide();
})
</script>