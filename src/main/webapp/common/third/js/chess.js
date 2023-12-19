var baisonChessData = [{
	activeClass : '31',
	name : Admin.threeDfirdFl,
	id : '1303',
	img : "bs/31.png"
}];
var vgqpChessData = [ {
	activeClass : '1',
	name : Admin.fightOwner,
	id : '49',
	img : "cs/17.png"
}];
function loginfrom() {
    layer.open({
        type : 1,
        title : Admin.notLogin,
        shadeClose : true,
        shade : 0.8,
        area : [ '380px', '200px' ],
        content : $('#loginfrom')
    });
}
(function($) {
	new Vue({
		el : '#latest-game',
		data : {
			baisonChessData : baisonChessData,
            vgqpChessData : vgqpChessData
		}
	});
	// 左边列表鼠标hover
    var hll=$(".hover-list li");
	hll.on("mouseenter", function(){
        $(this).addClass("hover").siblings().removeClass("hover");
        gogo($(this).index())
    });
	hll.first().addClass("hover");
	gogo(0)
    // 切换游戏
    function gogo(data){
        $('.XQ-list-Max').children().each(function(j){
            if(j==data){
                this.className=' '+'shownow'
            }
            else{
                this.className=' '+'hidenow'
            }
        })
    };
})(jQuery);