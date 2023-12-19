$(function(){
	var $balance=$('.station_balance'),
		$thirdName=$('#thirdName'),
		$thirdMoney=$('#thirdMoney'),
		$out=$('.balance-select select[name="outcash"]'),
		$in=$('.balance-select select[name="incash"]');
		
	function refreshBalance(){
		$balance.html(Admin.refreshIngDo);
	    $.ajax({
	        url: base + "/userInfo/getInfo.do",
	        type: "GET",
	        success: function (j) {
	            if (j) {
	                $balance.html(j.money);
	            }
	        }
	    })
	}
	$(".refresh").click(function(){
		refreshBalance();
	});
	refreshBalance();
	
	function initRealMoney(){
		var thirdParam = $in.find("option:selected");
		if(thirdParam.val() == 'sys'){
			thirdParam = $out.find("option:selected");
		}
		$.ajax({
			url : base + '/thirdTrans/getBalance.do',
			data : {platform : thirdParam.val()},
			type : 'POST',
			success : function(j){
				if(j.success){
					$thirdName.html(thirdParam.text()+"：")
					$thirdMoney.html(j.money)
					$balance.html(j.sysMoney)
				}else{
					$thirdName.html(thirdParam.text()+"：")
					//$thirdMoney.html(j.msg)
					$thirdMoney.html(j.code)
				}
			}
		})
	}
	initRealMoney();
	
	$out.change(function(){
		var inVal = $in.find("option:selected").val(),
		    outVal = $out.find("option:selected").val();
		if(outVal != 'sys'){
			$in.val('sys');
		}
		if(outVal == 'sys' && inVal == 'sys'){
			$in.find("option:first").prop('selected','selected');
		}
        initRealMoney();
	})

	$in.change(function(){
		var inVal = $in.find("option:selected").val(),
		    outVal = $out.find("option:selected").val();
		if(inVal != 'sys'){
			$out.val('sys');
		}
		if(outVal == 'sys' && inVal == 'sys'){
			$out.find("option:last").prop('selected','selected');
		}
		initRealMoney();
	});
	
	var confirmChangeDoing=false;
	$("#change_btn").click(function(){
		if(confirmChangeDoing)return;
		confirmChangeDoing=true;
		var cash = $('.balance-select input[name="outCashInput"]').val(),
			s1 = $('.balance-select select[name="outcash"]'),
			t1= s1.find("option:selected").text(),
			v1 = s1.find("option:selected").val(),
			s2 = $('.balance-select select[name="incash"]'),
			t2= s2.find("option:selected").text(),
		    v2 = s2.find("option:selected").val();
		if(!t1 || !t2){
			//layer.alert(Admin.errorPara);
			warnMsg(Admin.errorPara);
			confirmChangeDoing=false;
			return;
		}
		if(!v1 || !v2){
			//layer.alert(Admin.errorPara);
			warnMsg(Admin.errorPara);
			confirmChangeDoing=false;
			return;
		}
		if(!cash){
			//layer.alert(Admin.exchangeCashNotNull);
			warnMsg(Admin.exchangeCashNotNull);
			confirmChangeDoing=false;
			return;
		}
		if(v1 == v2){
			//layer.alert(Admin.exchangePlatNotSame);
			warnMsg(Admin.exchangePlatNotSame);
			confirmChangeDoing=false;
			return;
		}
		$.ajax({
	        url: base + '/thirdTrans/thirdRealTransMoney.do',
	        type:'POST',
	        data:{changeFrom: v1, changeTo: v2, money: cash},
	        success:function(json,status,xhr){
	        	var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {
		        	if(json.success){
		        		//刷新真人额度
		        		initRealMoney();
		        		//layer.alert(t1 + Admin.successExchangeTo + t2);
						warnMsg(t1 + Admin.successExchangeTo + t2);
		        	}else{
		        		//layer.alert(json.msg);
						//layer.alert(json.code);
						errorMsg(json.msg);
		        	}
				}else {// 登录异常
					//layer.alert(json.msg);
					errorMsg(json.msg);
					//layer.alert(json.code);
					//isLayui.alert(json.code);
				}
	        },
	        complete: function () {
	            confirmChangeDoing=false;
	        }
	    });
	});
})