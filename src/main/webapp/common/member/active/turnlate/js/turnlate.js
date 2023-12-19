//弹窗start
function fadeAlert(t,b){
	$('div.sign-info #fade-sign-info').html(t);
	$('div.sign-info #fade-sign-body').html(b);
	/*if(awardsType == 3){
		$('div.sign-info #fade-sign-img').html('<img src="'+productImage+'" id="shan-img-two"/>')
	}*/
	$(".sign-info").fadeIn();
}

var turnplate={
		restaraunts:[],				//大转盘奖品名称
		colors:[],					//大转盘奖品区块对应背景颜色
	    productImages:[],
		productNames:[],
		outsideRadius:200,			//大转盘外圆的半径
		textRadius:170,				//大转盘奖品位置距离圆心的距离
		insideRadius:68,			//大转盘内圆的半径
		startAngle:0,				//开始角度		
		bRotate:false				//false:停止;ture:旋转
};

$(document).ready(function(){
	//动态添加大转盘的奖品与奖品区域背景颜色
	//turnplate.restaraunts = awards;
	turnplate.colors = awardsColor;
    turnplate.productImages = awardImagesTwo;

    turnplate.productNames = awardsName;
	var rotateTimeOut = function (){
		$('#wheelcanvas').rotate({
			angle:0,
			animateTo:2160,
			duration:8000,
			callback:function (){
				fadeAlert(Admin.message,Admin.networkError);
			}
		});
	};

	$('.pointer').click(function (){
		//转盘动作
		if(turnplate.bRotate)return;
		turnplate.bRotate = !turnplate.bRotate;
		//获取随机数(奖品个数范围内)
		//var item = rnd(1,turnplate.restaraunts.length);
		//奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
		//rotateFn(item, turnplate.restaraunts[item-1]);
		trunMain();//转动大转盘
		//trunRecord();
	});
	
	$(".maskbox").click(function(){
		$(".maskbox").fadeOut();
		$(".sign-info").fadeOut();
	});

	$(".fade-sign-know").click(function(){
		$(".sign-info").fadeOut();
	});
});

//旋转转盘 item:奖品位置; txt：提示语;
var rotateFn = function (item, txt){
	var angles = item * (360 / turnplate.productNames.length) - (360 / (turnplate.productNames.length*2));
	if(angles<270){
		angles = 270 - angles; 
	}else{
		angles = 360 - angles + 270;
	}
	$('#wheelcanvas').stopRotate();
	$('#wheelcanvas').rotate({
		angle:0,
		animateTo:angles+1800,
		duration:8000,
		callback:function (){
			fadeAlert(Admin.message,txt);
			turnplate.bRotate = !turnplate.bRotate;
		}
	});
};

function trunMain(){
	if(!activeId){
		fadeAlert(Admin.message,Admin.activityNotStart);
		return;
	}
    var activeUrl = base + "/userTurnlate/award.do";
    $.ajax({
		"url" : activeUrl,
		"type" : "POST",
	    "dataType" : "json",
	    "data" : {
	    	'activeId' : activeId
	    },
	    "contentType" : "application/x-www-form-urlencoded",
		success : function(result, textStatus, xhr) {
			var ceipstate = xhr.getResponseHeader("ceipstate")
			if (!ceipstate || ceipstate == 1) {// 正常响应
				rotateFn(result.index, turnplate.productNames[result.index-1]);
				$("#userScore").html(result.userScore);
				var a = $("#adminJf").text();
				var c = parseInt(a) - awardsScore;
				if (c && c >= 0) {
					$(".breadcrumb #adminJf").text(c.toFixed(2));
				}else{
					$(".breadcrumb #adminJf").text('0.00');
				}
			} else if (ceipstate == 2) {// 后台异常
				fadeAlert(Admin.message,result.msg);
			} else if (ceipstate == 3) { // 业务异常
				fadeAlert(Admin.message,result.msg);
			}
		}
	});
}

function trunRecord(){
	if(!activeId){
		return;
	}
	var awardHtml = new Array();
	$.ajax({
		"url" : base + "/userTurnlate/records.do",
		"type" : "POST",
	    "dataType" : "json",
	    "data" : {
	    	'activeId' : activeId,
	    	'limit' : 200
	    },
	    "contentType" : "application/x-www-form-urlencoded",
		success : function(result, textStatus, xhr) {
			var ceipstate = xhr.getResponseHeader("ceipstate")
			if (!ceipstate || ceipstate == 1) {// 正常响应
				if(result){
					for(var i in result){
						awardHtml.push('<div class="clearfix pb10">');
						awardHtml.push('<div class="col-xs-4 clearPadding clearPadding1">'+result[i].username+'</div>');
                        var im = result[i].itemName;
                        if(im){
                        	awardHtml.push('<div class="col-xs-4 clearPadding clearPadding2 text-red">'+result[i].itemName+'</div>');

							/*awardHtml.push('<div class="col-xs-4 clearPadding clearPadding2 text-red"><img src='+result[i].imgPath+'></a></div>');*/
                        }else{
                        	awardHtml.push('<div class="col-xs-4 clearPadding clearPadding2 text-red">'+result[i].winMoney+'</div>');
                        }
						awardHtml.push('<div class="col-xs-4 clearPadding clearPadding3">'+result[i].winTime+'</div></div>');
					}
					$('#awardRecordNum').html(result.length);
					$('.prizebox #demo1').html(awardHtml.join(''));
				}
			
			} else if (ceipstate == 2) {// 后台异常
				fadeAlert(Admin.message,result.msg);
			} else if (ceipstate == 3) { // 业务异常
				fadeAlert(Admin.message,result.msg);
			}
		}
	});
}

function fadeUnloginAlert(t,b){
	$('div.unlogin #fade-sign-info').html(t);
	$('div.unlogin #fade-sign-body').html(b);
	$(".unlogin").fadeIn();
}

function rnd(n, m){
	var random = Math.floor(Math.random()*(m-n+1)+n);
	return random;
	
}
//页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
window.onload=function(){
	drawRouletteWheel();
};

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18

/*Date.prototype.Format = function (fmt) { // author: meizz
	var o = {
		"M+": this.getMonth() + 1, // 月份
		"d+": this.getDate(), // 日
		"h+": this.getHours(), // 小时
		"m+": this.getMinutes(), // 分
		"s+": this.getSeconds(), // 秒
		"q+": Math.floor((this.getMonth() + 3) / 3), // 季度
		"S": this.getMilliseconds() // 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};*/



function drawRouletteWheel() {
	var canvas = document.getElementById("wheelcanvas");
	let num =  turnplate.productImages.length;
	var width = 211
	// let arc = Math.PI / (num / 2);
	if (canvas.getContext) {
		var arc = Math.PI / (num/2);
		var ctx = canvas.getContext("2d");
		//在给定矩形内清空一个矩形
		ctx.clearRect(0,0,width * 2,width * 2);
		//strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式
		ctx.strokeStyle = "#FFBE04";
		//strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式
		//ctx.strokeStyle = "#fff";
		//font 属性设置或返回画布上文本内容的当前字体属性
		ctx.font = '16px Microsoft YaHei';
		for (let i = 0; i < num; i++) {
			let angle = 0 + i * arc;
			ctx.save();
			/*if ((i + 1) % 2 == 0) {
				ctx.fillStyle = "#fff";
			} else {
				ctx.fillStyle = "#ffcb3f";
			}*/
			ctx.fillStyle = turnplate.colors[i];
			ctx.beginPath();
			ctx.arc(width, width, turnplate.outsideRadius, angle, angle + arc, false);
			ctx.arc(width, width, turnplate.insideRadius, angle + arc, angle, true);
			ctx.stroke();
			ctx.fill();
			//锁画布(为了保存之前的画布状态)
			ctx.save();
			//奖品默认字体颜色
			// this.ctx.fillStyle = "#fff";
			ctx.fillStyle = "#000";
			let text =   turnplate.productImages[i].productName;
			ctx.translate(
				width + Math.cos(angle + arc / 2) * (width - 20 ),
				width + Math.sin(angle + arc / 2) * (width - 20)
			);
			ctx.rotate(angle + arc / 2 + Math.PI / 2);
			//将字体绘制在对应坐标
			ctx.fillText(text, -ctx.measureText(text).width / 2, 20);
			//设置字体
			// this.ctx.font = " 14px Microsoft YaHei";
			ctx.restore();
			//绘制奖品图片
			if (turnplate.productImages[i].productImg) {
				let img = new Image();
				img.src = turnplate.productImages[i].productImg;
				img.onload = () => {
					ctx.save();
					ctx.translate(
						width + Math.cos(angle + arc / 2) * (width - 40),
						width + Math.sin(angle + arc / 2) * (width - 40)
					);
					ctx.rotate(angle + arc / 2);
					ctx.drawImage(img, -ctx.measureText(text).width / 2 - 60, -35, 60, 60);
					ctx.restore();
				};
			}else if (text.indexOf(awardsNameOther) >= 0) {
				var imgO = document.getElementById("shan-img");
				imgO.onload = function () {
					ctx.save();
					ctx.translate(
						width + Math.cos(angle + arc / 2) * (width - 40),
						width + Math.sin(angle + arc / 2) * (width - 40)
					);
					ctx.rotate(angle + arc / 2);
					ctx.drawImage(imgO, -ctx.measureText(text).width / 2 - 60, -35, 60, 60);
					//ctx.drawImage(img,-15,10);
				};
				ctx.drawImage(imgO, -55, 105)
				ctx.restore();
			}
		}
	}
}







/*
function drawRouletteWheel() {
  var canvas = document.getElementById("wheelcanvas");
  if (canvas.getContext) {
	  //根据奖品个数计算圆周角度
	  var arc = Math.PI / (turnplate.restaraunts.length/2);
	  var ctx = canvas.getContext("2d");
	  //在给定矩形内清空一个矩形
	  ctx.clearRect(0,0,422,422);
	  //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式
	  ctx.strokeStyle = "#FFBE04";
	  //font 属性设置或返回画布上文本内容的当前字体属性
	  ctx.font = '16px Microsoft YaHei';
	  for(var i = 0; i < turnplate.restaraunts.length; i++) {
		  var angle = turnplate.startAngle + i * arc;
		  ctx.fillStyle = turnplate.colors[i];
		  ctx.beginPath();
		  //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）
		  ctx.arc(211, 211, turnplate.outsideRadius, angle, angle + arc, false);
		  ctx.arc(211, 211, turnplate.insideRadius, angle + arc, angle, true);
		  ctx.stroke();
		  ctx.fill();
		  //锁画布(为了保存之前的画布状态)
		  ctx.save();

		  //----绘制奖品开始----
		  ctx.fillStyle = "#E5302F";
		  var text = turnplate.restaraunts[i];
		  var line_height = 17;
		  //translate方法重新映射画布上的 (0,0) 位置
		  ctx.translate(211 + Math.cos(angle + arc / 2) * turnplate.textRadius, 211 + Math.sin(angle + arc / 2) * turnplate.textRadius);
		  console.log(text)
		  //rotate方法旋转当前的绘图
		  ctx.rotate(angle + arc / 2 + Math.PI / 2);

			/!** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **!/
		  // if(text.indexOf("M")>0){//流量包
			//   var texts = text.split("M");
			//   for(var j = 0; j<texts.length; j++){
			// 	  ctx.font = j == 0?'bold 20px Microsoft YaHei':'16px Microsoft YaHei';
			// 	  if(j == 0){
			// 		  ctx.fillText(texts[j]+"M", -ctx.measureText(texts[j]+"M").width / 2, j * line_height);
			// 	  }else{
			// 		  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
			// 	  }
			//   }
			// }else 
			if(text.length>0){//奖品名称长度超过一定范围
				// text = text.substring(0,2)+"||"+text.substring(2,4)+'||'+text.substring(4);
				var texts = text.split(" ");

			  for(var j = 0; j<texts.length; j++){
				  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
			  }
			}
			// else {
			//   //在画布上绘制填色的文本。文本的默认颜色是黑色
			//   //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
			//   ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
		  // }

		  //添加对应图标
		  var productName;
		  var image2;
		  var g;
		  var e=0;
// 		  if(text.indexOf("闪币")>0){
			  //var img= document.getElementById("shan-img");
		  for (g =i; g < turnplate.productNames.length; g++) {
			  productName = turnplate.productNames[g];
			  if (productName.length > 0) {
				  var imageNames = productName.split(" ");
				  for (var w = 0; w < imageNames.length; w++) {

				  if (awardsType == 3 && text.indexOf(imageNames[w]) > 0) {
					  for (i = 0; i < turnplate.productImages.length; i++) {

						  image2 = turnplate.productImages[i].productImg;
						  if (image2.length > 0) {
							  var image2s = image2.split(" ");

							  for (e = i; e < image2s.length; e++) {
								  //$("#divOne").append("<img id='shan-img-two' style='display:none;'/>");
								  /!* if(e === g){
                                       $("#shan-img-two").attr("src", image2s[g]);
                                   }else {*!/
								  $("#shan-img-two").attr("src", image2s[e]);
								  //}

								  //$("#shan-img-three").attr("src", image2s.get(1));

								  var imgPath = document.getElementById("shan-img-two");
								  imgPath.onload = function () {
									  //ctx.drawImage(imgPath, -15, 45);
									  ctx.drawImage(imgPath, -ctx.measureText(text).width / 2 - 60, -35, 60, 60);
								  };
								  //ctx.drawImage(imgPath, -15, 45);


								  /!* var imgPathThree = document.getElementById("shan-img-three");
                                   imgPathThree.onload = function () {
                                       ctx.drawImage(imgPathThree, -15, 45);
                                   };
                                   ctx.drawImage(imgPathThree, -15, 45);*!/

							  }
						  }
					  }
				  }
						  //var img2 = document.getElementById("shan-img-two").src = image2;
					  }
				  } else if (text.indexOf(awardsNameOther) >= 0) {
					  var img = document.getElementById("shan-img");
					  img.onload = function () {
						  ctx.drawImage(img, -15, 45);
						  //ctx.drawImage(img,-15,10);
					  };
					  ctx.drawImage(img, -15, 45);
					  //ctx.drawImage(img,-15,10);

				  }


				  /!*else if(awardsTypeOther == 1) {
				  img.onload=function(){
					  ctx.drawImage(img,-15,45);
				  };

				  ctx.drawImage(img,-15,45);
			  }else if(text.indexOf("No")>=0){*!/

				  }
			  }
		  }
// 		  }else if(text.indexOf("谢谢参与")>=0){
// 			  var img= document.getElementById("sorry-img");
// 			  img.onload=function(){  
// 				  ctx.drawImage(img,-15,10);      
// 			  };  
// 			  ctx.drawImage(img,-15,10);  
// 		  }
		  //把当前画布返回（调整）到上一个save()状态之前
		  ctx.restore();
		  //----绘制奖品结束----

  		}
*/
