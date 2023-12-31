    //倒计时
    (function($){
            var intervalDate;
            var day,hour,min,sec;

            $.fn.extend({
              countDown: function(options){  //生成倒计时字符串
                var opts = $.extend({},defaults,options);
                this.each(function(){
                  var $this = $(this);
                  var nowTime = new Date().getTime();
                  var startTime = new Date(opts.startTimeStr).getTime(); //开始时间
                  var endTime = new Date(opts.endTimeStr).getTime();  //结束时间
                  endTime = endTime > startTime ? endTime : startTime;
                  startTime = endTime > startTime ? startTime : endTime;

                  intervalDate = setInterval(function(){
                      nowTime = new Date().getTime();
                      if(startTime >= nowTime){
                        // $this.beforeAction(opts);
                        // clearInterval(intervalDate);
                          $('#redBagDescTitle').html(Admin.leftBeforeTheEvent+':');
                          //显示倒计时
                          var t = startTime - nowTime;
                          day = Math.floor(t/1000/60/60/24);
                          hour = Math.floor(t/1000/60/60%24);
                          min = Math.floor(t/1000/60%60);
                          sec = Math.floor(t/1000%60);
                          $(opts.daySelector).html($this.doubleNum(day)+Admin.day);
                          $(opts.hourSelector).html($this.doubleNum(hour)+Admin.hour);
                          $(opts.minSelector).html($this.doubleNum(min)+Admin.minute);
                          $(opts.secSelector).html($this.doubleNum(sec)+Admin.second);

                      }else if(endTime >= nowTime){
                        $('#redBagDescTitle').html(Admin.leftAfterTheEvent+':');
                        //显示倒计时
                        var t = endTime - nowTime;
                        day = Math.floor(t/1000/60/60/24);
                        hour = Math.floor(t/1000/60/60%24);
                        min = Math.floor(t/1000/60%60);
                        sec = Math.floor(t/1000%60);
                        $(opts.daySelector).html($this.doubleNum(day)+Admin.day);
                        $(opts.hourSelector).html($this.doubleNum(hour)+Admin.hour);
                        $(opts.minSelector).html($this.doubleNum(min)+Admin.minute);
                        $(opts.secSelector).html($this.doubleNum(sec)+Admin.second);
                      }else if (endTime < nowTime){
                          window.location.reload();
                      }

                  },1000);
                  
                });
              },
              doubleNum:function(num){ //将个位数字变成两位
                if(num<10){
                    return "0"+num;
                }else{
                    return num+"";
                }
              },
              beforeAction:function(options){
                  $('#redBagDescTitle').html('');
                //父容器显示，特定文字
                $(options.daySelector).parent().html(Admin.stayTuned);
              },
              afterAction:function(options){
                  $('#redBagDescTitle').html('');
                //父容器显示，特定文字
                $(options.daySelector).parent().html(Admin.activityEnds);
              }
                   
            });

            var defaults = {
                    startTimeStr: "2017/01/10 00:00:00",
                    endTimeStr: "2017/01/17 23:59:59",
                    daySelector:".day",
                    hourSelector:".hour",
                    minSelector:".min",
                    secSelector:".sec"
            }


    })(jQuery)





















































