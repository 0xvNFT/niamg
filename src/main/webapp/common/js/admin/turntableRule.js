define(['template','eChart','jquery','bootstrap','Fui'],function(template,echarts){
    var source='<tr class="appendtr">'
        +'<td class="text-center media-middle" rowspan="{{data.length}}">'+Admin.prizesProbability+'</td>'
        +'{{each data as award i}}'
        +'{{if i > 0}}<tr class="appendtr">{{/if}}'
        +'<td colspan="2" class="form-inline">'
        +'<div class="form-group" style="color: {{colors[i]}}"> {{award}}</div>'
        +'<div class="form-group"><select name="awardType"class="form-control">' +
        '<option value="1" selected>'+Admin.notWinning+'</option>' +
        '<option value="2">'+Admin.cash+'</option>' +
        '<option value="4">'+Admin.integral+'</option>' +
        '<option value="3">'+Admin.prize+'</option>' +
        '</select>' +
        '</div>'
        +'<div class="form-group"><div class="input-group">' +
        '<div class="input-group-addon">'+Admin.name+'</div>' +
        '<input type="text" name="awardName"class="form-control" style="width:100px"></input></div></div>'
        +'<div class="form-group hidden awardProudctWrap">' +
        '<div class="input-group">' +
        '<div class="input-group-addon">'+Admin.prize+'</div><select name="giftId"class="form-control awardProudct">'
        +'{{each products as p j}}<option value="{{p.id}}">{{p.productName}}</option>{{/each}}'
        +'</select></div></div>'
        +'<div class="form-group hidden awardMoneyWrap">' +
        '<div class="input-group">' +
        '<div class="input-group-addon">'+Admin.faceValue+'</div>' +
        '<input type="text" name="awardMoney" class="form-control awardMoney" style="width:60px"></div></div>'
        +'<div class="form-group hidden awardMoneybetRate">' +
        '<div class="input-group">' +
        '<div class="input-group-addon">'+Admin.multipleAmount+'</div>' +
        '<input type="number" name="betRate" class="form-control awardMoney" style="width:60px"></div></div>'
        +'<div class="form-group"><div class="input-group">' +
        '<div class="input-group-addon">'+Admin.probabilityBase+'</div>' +
        '<input type="text" name="chance" class="form-control form-text" style="width:60px">' +
        '</div>' +
        '</div>'
        +'<div class="form-group">'+Admin.probability+'：<span class="text-primary chance_span"></span></div>'
        +'</td></tr>{{/each}}';
    return {
        render:function(products,awardDatas){
            var $form=$("#turntable_set_rule_formId_id")
                ,$table=$form.find("table")
                ,$awardCount=$table.find(".awardCount")
                ,awardImgChart=echarts.init(document.getElementById('awardImgChart'));
            //奖项类型事件
            $table.on("change","select[name='awardType']",function(){
                var $it=$(this),val=$it.val(),$parent=$it.parents("td:first");
                if (val == 4) {
                    $parent.find(".awardProudctWrap").addClass("hidden");
                    $parent.find(".awardMoneybetRate").addClass("hidden");
                    $parent.find(".awardMoneyWrap").removeClass("hidden");
                }else if(val == 2 ) {
                    $parent.find(".awardProudctWrap").addClass("hidden");
                    $parent.find(".awardMoneyWrap").removeClass("hidden");
                    $parent.find(".awardMoneybetRate").removeClass("hidden");
                } else if (val == 3) {
                    $parent.find(".awardProudctWrap").removeClass("hidden");
                    $parent.find(".awardMoneyWrap").addClass("hidden");
                    $parent.find(".awardMoneybetRate").addClass("hidden");
                } else {
                    $parent.find(".awardProudctWrap").addClass("hidden");
                    $parent.find(".awardMoneyWrap").addClass("hidden");
                    $parent.find(".awardMoneybetRate").addClass("hidden");
                }
            });


            var h='';
            for(var i=5;i<21;i++){
                h+='<option value="'+i+'">'+i+'</option>';
            }
            $awardCount.html(h).change(function(){
				var val = $(this).val(),cavDatas = [],cavLables = [],cavColors = [],c;
                for (var i = 1; i <= val; i++) {
                    cavLables.push("");
					c=randomColor();
                    cavColors.push(c);
                }
                $(".appendtr",$table).remove();
                $table.append(template.compile(source)({"data" : cavLables,"colors" : cavColors,products:products}));
                setAwardData();
                balance();
                var numArr = new Array();
                $("input[ name='chance' ]").each(function(){
                    numArr.push($(this).val());//添加至数组
                });
                for (var i = 0; i < numArr.length; i++) {
                    cavLables.push(Admin.awards + (i+1))
                    c=randomColor();
                    cavDatas.push({value:numArr[i], name:Admin.awards + (i+1),itemStyle:{color:c}});
                    cavColors.push(c);
                }
                awardImgChart.setOption({tooltip:{trigger:'item',formatter: '{b}: {c} ({d}%)'},
                    legend:{orient:'vertical',left: 10,data:cavLables},
                    series:[{type:'pie',radius:['50%','70%'],avoidLabelOverlap: false,
                        label:{show:false,position:'center'},
                        data:cavDatas
                    }]});

            });
            $awardCount.val(awardDatas.length||5).change();
			//随机颜色
            function randomColor() {
                return '#'+ ('00000' + (Math.random() * 0x1000000 << 0).toString(16)).substr(-6);
            }
            //设置活动的奖项内容
            function setAwardData() {
                if (awardDatas) {
                    var award = null
                        ,$appendtrs=$table.find(".appendtr")
                        ,$tr=null
                        ,trlen=$appendtrs.length;
                    for (var i = 0; i < awardDatas.length; i++) {
                        if(i>=trlen){
                            return;
                        }
                        award = awardDatas[i];
                        award.awardIndex = i;
                        $tr=$appendtrs.eq(i);
                        $tr.find("[name='awardType']").val(award.awardType).change();
                        $tr.find("[name='awardName']").val(award.awardName);
                        $tr.find("[name='giftId']").val(award.giftId);
                        $tr.find("[name='awardMoney']").val(award.awardValue);
                        $tr.find("[name='chance']").val(award.chance);
                        $tr.find("[name='betRate']").val(award.betRate);
                    }
                }
            }
            //绑定自动结算概率
            $table.on("blur","input[name='chance']",function(){
                var $it=$(this),val=$it.val();
                if (!isNaN(val)) {
                    $it.val(Math.abs(val));
                    balance();
                }
            });
            //通过奖项基数计算概率
            function balance() {
                var awardCount = $awardCount.val()
                    ,total = 0
                    ,single = 0
                    ,$appendtrs=$table.find(".appendtr")
                    ,$tr=null;
                if (awardCount) {
                    for (var i = 0; i < awardCount; i++) {
                        $tr=$appendtrs.eq(i);
                        single = $tr.find("input[name='chance']").val();
                        if (single) {
                            total = accAdd(total, single * 1);
                        }
                    }
                    for (var i = 0; i < awardCount; i++) {
                        $tr=$appendtrs.eq(i);
                        single = $tr.find("input[name='chance']").val();
                        if (single) {
                            $tr.find(".chance_span").html(Fui.toDecimal(accDiv(single * 100, total),2)+ "%");
                        } else {
                            $tr.find(".chance_span").html("0.00%");
                        }
                    }
                }
            }
            //除法
            function accDiv(arg1, arg2) {
                var t1 = 0, t2 = 0, r1, r2;
                try {
                    t1 = arg1.toString().split(".")[1].length
                } catch (e) {
                }
                try {
                    t2 = arg2.toString().split(".")[1].length
                } catch (e) {
                }
                with (Math) {
                    r1 = Number(arg1.toString().replace(".", ""))
                    r2 = Number(arg2.toString().replace(".", ""))
                    return accMul((r1 / r2), pow(10, t2 - t1));
                }
            }
            //加法
            function accAdd(arg1, arg2) {
                var r1, r2, m;
                try {
                    r1 = arg1.toString().split(".")[1].length
                } catch (e) {
                    r1 = 0
                }
                try {
                    r2 = arg2.toString().split(".")[1].length
                } catch (e) {
                    r2 = 0
                }
                m = Math.pow(10, Math.max(r1, r2))
                return (arg1 * m + arg2 * m) / m
            }

            //乘法
            function accMul(arg1, arg2) {
                var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
                try {
                    m += s1.split(".")[1].length
                } catch (e) {
                }
                try {
                    m += s2.split(".")[1].length
                } catch (e) {
                }
                return Number(s1.replace(".", "")) * Number(s2.replace(".", ""))
                    / Math.pow(10, m)
            }
            $form.data("paramFn",function(){
                var data=getAwardDatas();
                var params={
                    "id" : $form.find("[name='id']").val(),
                    "dataJson" : JSON.stringify(data)
                };
                return params;
            }).data("resultCallback",function(json){
                if(json.success){
                    awardDatas=getAwardDatas();
                }
            });
            //得到页面的奖项内容集合
            function getAwardDatas() {
                var awards = []
                    ,award = null
                    ,$appendtrs=$table.find(".appendtr")
                    ,$tr=null
                    ,trlen=$appendtrs.length;

                if (!trlen) {
                    return awards;
                }
                for (var i = 0; i < trlen; i++) {
                    $tr=$appendtrs.eq(i);
                    award = {};
                    award.awardIndex = i + 1;
                    award.awardName = $tr.find("[name='awardName']").val();
                    award.awardType = $tr.find("[name='awardType']").val();
                    award.chance = $tr.find("[name='chance']").val();
                    award.awardValue = $tr.find("[name='awardMoney']").val();
                    award.giftId = $tr.find("[name='giftId']").val();
                    award.betRate = $tr.find("[name='betRate']").val();
                    awards.push(award);
                }
                return awards;
            }
        }
    }
});
