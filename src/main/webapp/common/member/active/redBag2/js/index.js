function lunTopFn(obj,time){
	time = time ? time: 30;
	var len = ($(obj).children('ul').height()/2) + 100;
	var times = null;
	if($(obj)[0]){
		times = window.setInterval(function(){
			var t = $(obj).children('ul').css('top');
			t = t.replace('px','');
			if(t > -len){
				t--;
				$(obj).children('ul').css({top: t})
			}else{
				$(obj).children('ul').css({top: 10})
			}
		},time)
		$(obj).children('ul').hover(function(){
			window.clearInterval(times);
		},function(){
			times = window.setInterval(function(){
				var t = $(obj).children('ul').css('top');
				t = t.replace('px','');
				if(t > -len){
					t--;
					$(obj).children('ul').css({top: t})
				}else{
					$(obj).children('ul').css({top: 0})
				}
			},time)
		})
	}
}
//加载公告
function loadAnnounce(callback) {
	callback();
}
/**
 * 抢红包
 */
var sid = fixRedPacketId,stitle,sbeginDatetime,sendDatetime;
var redBagUtil = {
	   flag : false,
	   initRedBag : function(){
    	   if(this.flag){
    		   $("#hb-message").html(Admin.noRedEnvelopeActivity);
    		   	swal({
				  title: Admin.noRedEnvelopeActivity,
				  confirmButtonColor: "#e22000",
				  confirmButtonText: Admin.determine,
				  html: true 
				});
    		   return;
    	   }
    	   flag = true;
        	$.ajax({
        		url : base + "/userCenter/redpacket/curNew.do",
        		dataType  : "json",
				data : {
					packetId : sid
				},
        		success : function(j){
        			if(j && j.id){
        				sid = j.id;
        				stitle = j.title;
        				sbeginDatetime = j.beginDatetime;
        				sendDatetime = j.endDatetime;
        				$('#red-bag-title').html(stitle);
        				$(".count_down").countDown({
        					startTimeStr:sbeginDatetime,//开始时间
        		        	endTimeStr:sendDatetime,//结束时间
        		        	daySelector:".day_num",
        		            hourSelector:".hour_num",
        		            minSelector:".min_num",
        		            secSelector:".sec_num"
        				});
        				redBagUtil.getRedBagRecord(sid);
        			}else{
        				$("#hb-message").html(Admin.noRedEnvelopeActivity);
						swal({
						  title: Admin.noRedActivity,
						  showCancelButton: false,
						  confirmButtonColor: '#d63030',
						  confirmButtonText: Admin.determine,
						})
        			}
        		}
        	})
        },
        getPacket : function(){
        	if(sid == 0){
					swal({
					  title:Admin.noRedActivity,
					  showCancelButton: false,
					  confirmButtonColor: '#d63030',
					  confirmButtonText: Admin.determine,
					})
        		return;
        	}
        	$.ajax({
        		url : base + "/userCenter/redpacket/grab.do",
        		dataType  : "json",
        		data : {
        			redPacketId : sid
        		},
        		success : function(j){
					console.log(j.redPacketMoney);
        			if(j.msg){
						console.log("报错了。。。"+j.msg)
						swal({
        					  title: j.msg,
        					  showCancelButton: false,
        					  confirmButtonColor: '#d63030',
        					  confirmButtonText: Admin.determine,
        					}).then(function(){
								// 获取上一页的url
								const prevPageUrl = document.referrer;

								// 跳转上一页
								document.location.href = prevPageUrl;
							})
        			}else{
        				// sweetAlert(
        				// 		  Admin.congratulations+"<font class='red-bag-font-size20'>"+j+"</font> "+Admin.yuan
        				// 		)
						$('.mainBlur').show();
						$('.mainR').addClass('slide-in-fwd-center-top');
						$('.winmoney').text(j.redPacketMoney);
        			}
        		}
        	})
        },
        getRedBagRecord : function(sid){
        	$.ajax({
        		url : base + "/userCenter/redpacket/record.do",
        		dataType  : "json",
        		data : {
        			redPacketId : sid
        		},
        		success : function(j){
        			if(!j){
        				return;
        			}
        			var html = template('red_bag_recore_tpl', {list: j});
    				$('#announce').html(html);
    				 loadAnnounce(function() {
	    			 	 lunTopFn('.luntop2',120);
	    			 });
        		}
        	})
        }
}

$(document).ready(function(){
     $(".cha").click(function(){
        $(".kef").hide();
    });
    $(".chag").click(function(){
        $(".gonggao").hide();
    });
	$(".xiao").click(function(){
        $(".gonggao").toggleClass("cur");
    });

    $(window).scroll(function(){
        var sc = $(window).scrollTop();
        $('.kef').stop().animate({
            top: 80+sc
        },150)
        
    })

});

