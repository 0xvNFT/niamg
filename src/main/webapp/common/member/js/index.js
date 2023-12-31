$(function() {
	var websocket,xintiaoTime,count = 0;
	// 首先判断是否 支持 WebSocket
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://"+localhost+"/websocket.do");
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws://"+localhost+"/websocket.do");
	} else {
		websocket = new SockJS("http://"+localhost+"/sockjs/websocket.do");
	}
	// 打开时
	websocket.onopen = function(evnt) {
		xintiaoTime = window.setInterval(function(){
			count = count + 1;
			console.info(count);
		},1000)
		console.log("  websocket.onopen  ");
	};
	// 处理消息时
	websocket.onmessage = function(evnt) {
		$("#msg").append("<p>(<font color='red'>" + evnt.data + "</font>)</p>");
		console.log("  websocket.onmessage   ");
	};
	websocket.onerror = function(evnt) {
		console.log("  websocket.onerror  ");
	};
	websocket.onclose = function(evnt) {
		clearInterval(xintiaoTime);
		console.log("  websocket.onclose  ");
	};
	// 点击了发送消息按钮的响应事件
	$("#TXBTN").click(function() {
		// 获取消息内容
		var text = $("#tx").val();
		// 判断
		if (text == null || text == "") {
			alert(" content  can not empty!!");
			return false;
		}
		var msg = {
			msgContent : text,
			postsId : 1
		};
		// 发送消息
		websocket.send(JSON.stringify(msg));

	});
});