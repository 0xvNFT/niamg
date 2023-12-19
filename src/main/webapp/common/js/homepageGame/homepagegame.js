$(function() {
	requirejs(['jquery', 'bootstrap', 'Fui'], function() {
		$selctpicker.change(function() {
			var val = $(this).val();
			// console.log("selctpicker value is: " + val);
			if (val && val !== '') {
				$secondGameValues.val(val.join(","));
				// console.log("secondGameValues is not null = ",document.getElementById("gameCodeArr_id").value);
			} else {
				$secondGameValues.val("");
			}
		});

		// 菜单功能 gameTabId 事件
		var gameTabIdVal = gameTabIdValue;
		// console.log("gameTabIdVal is: " + gameTabIdVal);
		$gameTabId.change(function () {
			var val = $(this).val();
			// console.log("gameTabId is: " + val);
			gameTabIdVal = val;

			//触发一级游戏change事件
			$topGame.trigger("change");
		});

		//游戏类型change事件
		$type.change(function() {
			var v = $(this).val();
			var type = convertGameType(v);
			//var type = $(this).val();
			var url = baseUrl + '/admin/stationHomepageGame/getGameList.do?type=' + type;
			// console.log("gameType value is: " + v);
			//获取某类游戏数据
			$.ajax({
				type: "GET",
				url: url,
				success: function(result) {
					if (!result) {
						return;
					}
					if (result.success) {
						$topGame.empty();
						$icon.val("");
						$link.val("");
						$.each(result.content, function(key, value) {
							if (value.subData && !value.isListGame) {//彩票时，再次遍历每个分组下的子彩票
								$.each(value.subData, function(key, caipiao) {
									if (selectedParentGameCode && selectedParentGameCode == caipiao.code) {
										$topGame.append("<option selected='selected' gametype='" + caipiao.code + "' value='" + caipiao.code + "'>" + caipiao.name + "</option>");
									} else {
										$topGame.append("<option gametype='" + caipiao.code + "' value='" + caipiao.code + "'>" + caipiao.name + "</option>");
									}
								});
							} else {
								if (selectedParentGameCode && selectedParentGameCode == value.czCode) {
									$topGame.append("<option selected='selected' gametype='" + value.czCode + "' listgame='" + value.isListGame + "' value='" + value.czCode + "'>" + value.name + "</option>");
								} else {
									$topGame.append("<option gametype='" + value.czCode + "' listgame='" + value.isListGame + "' value='" + value.czCode + "'>" + value.name + "</option>");
								}
							}
						});
						//获取并填充一级游戏数据时，触发一级游戏select的change事件
						if (result.content.length > 0) {
							$topGame.trigger("change");
							//获取到一级游戏时，先赋值选中的一级游戏code
							$parentGameCode.val($topGame.find("option:selected").val());
						} else {
							$form.find(".isShowSecondGame").addClass("hidden");
							$name.val("");
							$code.val("");
							$icon.val("");
							$link.val("");
							$parentGameCode.val("");
						}
					}
				},
				error: function(error) {
					console.log(error);
				}
			});

			if (v == 8) {//自定义
				$icon.val("");
				$link.val("");
				$name.val("");
				$form.find(".isShowGameIcon").removeClass("hidden");
				$form.find(".isShowGameLink").removeClass("hidden");
				$form.find(".isShowTitle").removeClass("hidden");
			} else {
				$form.find(".isShowGameIcon").addClass("hidden");
				$form.find(".isShowGameLink").addClass("hidden");
				$form.find(".isShowTitle").addClass("hidden");
			}
			//选彩票则隐藏二级游戏，并清空之前选择的二级select;
			if (v == 1) {
				$form.find(".isShowSecondGame").addClass("hidden");
			}
		});
		//触发游戏类型select的change事件
		$type.trigger("change");
		//一级游戏change事件
		$topGame.change(function() {
			var isListGame = $(this).find("option:selected").attr("listgame");
			var gameType = $(this).find("option:selected").attr("gametype");
			var gameCode = $(this).val();
			// console.log("gameType is: " + gameType);
			// console.log("isListGame is: " + isListGame);
			// console.log("gameCode is: " + gameCode);
			$parentGameCode.val(gameCode);//一级游戏选择时，赋值游戏code

			//清空 二级select中option选项
			$secondGameValues.val("");
			document.getElementById("second_game_select_id").options.length = 0;

			if (isListGame && isListGame == 1) {
				var gameList={};
				$.ajax({
					url: baseUrl + '/admin/stationHomepageGame/getList.do',
					type: "GET",
					data: {parentGameCode: gameType, gameTabId: gameTabIdVal},
					async: false,
					success: function (res) {
						gameList = res;
					}
				});
					
				$form.find(".isShowSecondGame").removeClass("hidden");
				//获取某类游戏子游戏列表
				var url = baseUrl + '/native/v2/get_game_datas.do?gameType=' + gameType;
				$.ajax({
					type: "GET",
					url: url,
					success: function(result) {
						if (!result) {
							return;
						}
						if (result.success) {
							$.each(result.content, function(key, value) {
								if (gameList.length > 0 && gameList.includes(value.lapisId)) {
									$secondGameSelect.append("<option selected='selected' jumplink='" + value.finalRelatveUrl + "' gameimg='" +
										value.buttonImagePath + "' value='" + value.lapisId + "'>" + value.displayName + "</option>");
								} else {
									$secondGameSelect.append("<option jumplink='" + value.finalRelatveUrl + "' gameimg='" +
										value.buttonImagePath + "' value='" + value.lapisId + "'>" + value.displayName + "</option>");
								}
							});
							$selctpicker.selectpicker("refresh");
							//成功获取到二级游戏时，将'isSubGame'表单参数赋值为1
							$isSubGame.val(1);
							//获取到二级游戏时，先赋值选中的二级游戏code
							$isSubGame.val(1);
						}
					},
					error: function(error) {
						console.log(error);
					}
				});
			} else {
				$form.find(".isShowSecondGame").addClass("hidden");
				var gameName = $(this).find("option:selected").text();
				$name.val(gameName);
				$code.val(gameCode);
				$icon.val("");
				$link.val("");
				$isSubGame.val(0);
			}
		});
	})
});

function convertGameType(v) {
	var type = 3;
	switch (parseInt(v)) {
		case 1://彩票
			type = 3;
			break;
		case 2://真人
			type = 1;
			break;
		case 3://电子
			type = 2;
			break;
		case 4://体育
			type = 0;
			break;
		case 5://电竞
			type = 6;
			break;
		case 6://捕鱼
			type = 7;
			break;
		case 7://棋牌
			type = 4;
			break;
		case 9://红包
			type = 5;
			break;
		default:
			type = 3;
	}
	return type;
}


