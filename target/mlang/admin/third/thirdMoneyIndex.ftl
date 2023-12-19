<div id="real_game_user_balance_div_id">
	<div class="form-inline">
		<div class="input-group">
			<input type="text"  name="username" class="form-control" placeholder="<@spring.message "admin.user.account.num"/>">
		</div>
		<button class="btn btn-primary search-btn" type="button"><@spring.message "admin.deposit.button.search"/></button>
		<button class="btn btn-primary reset" type="button"><@spring.message "manager.resetting"/></button>
	</div>
	<div class="memmnyope-tb hidden" style="margin-top:10px;">
		<table class="table table-bordered">
              <tbody>
			<tr>
				<td width="20%" class="text-right active"><@spring.message "admin.money.history.username"/>：</td>
                <td width="20%"><span class="text-primary username-span"></span></td>
                <td width="60%"></td>
			</tr>
			<tr>
				<td class="text-right active"><@spring.message "admin.account.money.val"/>：</td>
                <td><span class="text-danger money-span"></span></td>
                <td></td>
			</tr>
			<tr>
				<td class="text-right active"><@spring.message "admin.real.name"/>：</td>
                <td><span class="text-danger realname-span"></span></td>
                <td></td>
			</tr> 
			<#list platforms as p> 
			<tr>
				<td class="text-right active">${p.title}<@spring.message "admin.money.values"/>：</td>
				<td><span class="text-danger balance-${p.value}">0</span></td>
    			<td>&nbsp;<a href="javascript:void(0)" class="refresh" data-thirdid="${p.value}"><@spring.message "admin.fresh"/></a>
    			&nbsp;&nbsp;<a href="javascript:void(0)" class="retrieve" data-thirdid="${p.value}"><@spring.message "admin.trans.out.money"/></a></td>
			</tr></#list>
		</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	var $con=$("#real_game_user_balance_div_id")
		,$username=$con.find("[name='username']")
		,curUserId=0;
	$con.find(".search-btn").click(function(){
		var un=$username.val();
		if(!un){
			layer.tip("<@spring.message "admin.input.vip.account"/>！",$username);
			return false;
		}
		$.ajax({
			url : "${adminBase}/thirdMoney/getMoney.do",
			data : {username : un},
			success : function(data) {
				if(data.userId){
					$con.find(".memmnyope-tb").removeClass("hidden");
					$con.find(".username-span").html(data.username);
					$con.find(".money-span").html(data.money);
					$con.find(".realname-span").html(data.realName);
					curUserId = data.userId;
					<#list platforms as p> refresh(${p.value});</#list>
				}else{
					layer.msg(data.msg||"<@spring.message "admin.user.not.exist"/>");
				}
			}
		});
	});
	$con.find(".reset").click(function(){
		curUserId=0;
		$con.find(".memmnyope-tb").addClass("hidden");
		$username.val("");
	});
	$con.find(".refresh").click(function(){
		if(!curUserId || curUserId==0){
			layer.msg("<@spring.message "admin.select.user.who"/>");
			return false;
		}
		refresh($(this).data("thirdid"));
	});
	function refresh(thirdId) {
		var $bal=$con.find(".balance-"+thirdId).html("<img width='20px' heigth='20px' src='${base }/common/images/ajax-loader.gif' />")
		$.ajax({
	        url:"${adminBase}/thirdMoney/getBalance.do",
	        type:'POST',
	        data:{platform:thirdId,userId: curUserId},
	        success:function(json){
				if (json.msg) {
					$bal.html(json.msg);
				}else {
					$bal.html(json.money);
				}
	        },
	        errorFn:function(json){
        		$bal.html(json.msg);
	        },
	        error:function(){
        		$bal.html("<@spring.message "admin.execp"/>");
	        }
	    });
		
	}
	$con.find(".retrieve").click(function(){
		if(!curUserId || curUserId==0){
			layer.msg("<@spring.message "admin.select.user.who"/>");
			return false;
		}
		var thirdId=$(this).data("thirdid")
			,$bal=$con.find(".balance-"+thirdId)
			,m=$bal.html();
		if(!/^[\d]+(\.[\d]+)?$/.test(m)){
			layer.msg("<@spring.message "admin.value.format.not.right"/>");
			return false;
		}
		$.ajax({
	        url:"${adminBase}/thirdMoney/retrieveBalance.do",
	        type:'POST',
	        data:{platform:thirdId,userId:curUserId,money:m},
	        success:function(json){
				if (json.msg) {
					$bal.html(json.msg);
				}else {
					refresh(thirdId);
				}
	        },
	        errorFn:function(json){
        		$bal.html(json.msg);
	        },
	        error:function(){
        		$bal.html("<@spring.message "admin.execp"/>");
	        }
	    });
	});
});
</script>