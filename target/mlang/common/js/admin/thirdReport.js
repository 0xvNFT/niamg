define(['jquery','bootstrap','Fui','bootstrap_editable'],function(){
	function typeFormmater(value,row,index){
		switch(value-0){
		case 1:return "AG";
		case 2:return "BBIN";
		case 3:return "MG";
		case 4:return "QT";
		case 5:return "ALLBET";
		case 6:return "PT";
		case 7:return "OG";
		case 8:return "DS";
		case 9:return "NB";
		case 10:return "SKYWIND";
		case 11:return "KY";
		case 20:return"BG";
		case 21 :return"DG";
		case 22 :return Admin.crownLive;
		case 23 :return Admin.sexCourt;
		case 24 :return Admin.esportsCQ;
		case 25 :return Admin.ebetLive;
		case 26 :return Admin.chessGame;
		case 27 :return Admin.ygGame;
		case 28 :return Admin.enjoyChess;
		case 29 :return Admin.peopleChess;
		case 30 :return Admin.asiaEsports;
		case 31 :return Admin.vrLottery;
		case 32 :return Admin.sboSports;
		case 33 :return Admin.oneEightSports;
		case 34 :return Admin.ybChess;
		case 35 :return Admin.pgEgame;
		case 36 :return Admin.happyChess;
		case 37 :return Admin.gpkFish;
		case 38 :return Admin.shaSports;
		case 39 :return Admin.imEsports;
		case 40 :return Admin.fayaEsports;
		case 41 :return Admin.financeChess;
		case 42 :return Admin.skyChess;
		case 43 :return Admin.liboLive;
		case 44 :return Admin.egLive;
		case 45 :return Admin.aiEsports;
		case 46 :return Admin.leihuoEs;
		case 48 :return Admin.iboLive;
		case 49 :return Admin.cowrnSports;
		case 60 :return Admin.pgEgame;
		case 70 :return "EVO";
		case 80 :return "EVOLUTION";
		case 90 :return "PP";
		case 100 :return "FG";
		case 110 :return Admin.jlEgame;
		case 120 :return "JDB";
		case 130 :return "TADA";
		case 140 :return "BS";
		case 150 :return Admin.fbSports;
		case 170 :return "AWC";
		case 180 :return "V8POKER";
		//case 47 :return "申博娱乐";
		}
	}
	function moneyFormmater(value, row, index){
		if(value !=undefined){
			return value.toFixed(2);
		}
		return '-'
	}
	function dangerMoneyFormmater(value, row, index){
		if(value !=undefined){
			return '<span class="label label-danger">'+value.toFixed(2)+'</span>';
		}
		return '-'
	}
	function successMoneyFormmater(value, row, index){
		if(value !=undefined){
			return '<span class="label label-success">'+value.toFixed(2)+'</span>';
		}
		return '-'
	}
	function getShuYing(value){
		value=value||0;
		if(value>0){
			return '<span class="label label-warning">'+value+"</span>"
		}else if(value==0){
			return "0";
		}
		return '<span class="label label-danger">'+value+"</span>"
	}
	function primaryMoneyFormmater(value, row, index){
		if(value !=undefined){
			return '<span class="label label-primary">'+value.toFixed(2)+'</span>';
		}
		return '-'
	}
	function infoMoneyFormmater(value, row, index){
		if(value !=undefined){
			return '<span class="label label-info">'+value.toFixed(2)+'</span>';
		}
		return '-'
	}
	function getJackPokeTotal(rows,bet,win) {
		var total = 0;
		for(var i=0;i< rows.length;i++){
			var r = rows[i];
			if(r[bet] && r.type!=9 && r.type!=11){
				total +=r[bet];
			}
			if(win && r[win] && r.type!=9 && r.type!=11){
				total -=r[win];
			}
		}
		return total.toFixed(2)+"";
	}
	function getTotal(rows,bet,win){
		var total = 0;
		for(var i=0;i< rows.length;i++){
			var r = rows[i];
			if(r[bet]){
				total +=r[bet];
			}
			if(win && r[win]){
				total -=r[win];
			}
		}
		return total.toFixed(2)+"";
	}
	var columns=[{
		field : 'type',
		title : Admin.gameTypes,
		width : '120',
		align : 'center',
		valign : 'middle',
		formatter:typeFormmater,
		pageSummaryFormat:function(rows,aggsData){
			return "<span class='text-primary'>"+Admin.subtotal+":</span>";
		}
	},{
		field : 'statProfitAmount',
		title : Admin.totalLoseOrWin,
		align : 'center',
		width : '100',
		valign : 'middle',
		formatter:dangerMoneyFormmater,
		pageSummaryFormat:function(rows,aggsData){
			return getTotal(rows,'statProfitAmount');
		}
	}, {
		field : 'jackpot',
		title : 'Jackpot',
		align : 'center',
		width : '80',
		valign : 'middle',
		formatter :function(value, row, index){
			if(value !=undefined && row.type!=9 && row.type!=11){
				return '<span class="label label-success">'+value.toFixed(2)+'</span>';
			}
			return '-'
		},
		pageSummaryFormat:function(rows,aggsData){
			return getJackPokeTotal(rows,'jackpot');
		}
	}, {
		field : 'allProfitAmount',
		title : Admin.allWinOrLose,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : getShuYing,
		pageSummaryFormat:function(rows,aggsData){
			return getShuYing(getTotal(rows,'allProfitAmount'));
		}
	}, {
		field : 'allBetAmount',
		title : Admin.allPay,
		width : '60',
		align : 'center',
		valign : 'middle',
		formatter : primaryMoneyFormmater,
		pageSummaryFormat:function(rows,aggsData){
			return getTotal(rows,'allBetAmount');
		}
	}, {
		field : 'allWinAmount',
		title : Admin.allSendWin,
		width : '80',
		align : 'center',
		valign : 'middle',
		formatter : infoMoneyFormmater,
		pageSummaryFormat:function(rows,aggsData){
			return getTotal(rows,'allWinAmount');
		}
	}, {
		title : Admin.liveGame,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : function(vaule,row,index){
			var r=Admin.onePay+':'+(row.realBetAmount||0);
			r=r+'</br>'+Admin.sendWin+':'+(row.realWinAmount||0);
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(row.realProfitAmount);
			return r; 
		},
		pageSummaryFormat:function(rows,aggsData){
			var r=Admin.onePay+':'+getTotal(rows,'realBetAmount');
			r=r+'</br>'+Admin.sendWin+':'+getTotal(rows,'realWinAmount');
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(getTotal(rows,'realProfitAmount'));
			return r; 
		}
	}, {
		title : Admin.electronic,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : function(vaule,row,index){
			var r=Admin.onePay+':'+(row.egameBetAmount||0);
			r=r+'</br>'+Admin.sendWin+':'+(row.egameWinAmount||0);
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(row.egameProfitAmount);
			return r; 
		},
		pageSummaryFormat:function(rows,aggsData){
			var r=Admin.onePay+':'+getTotal(rows,'egameBetAmount');
			r=r+'</br>'+Admin.sendWin+':'+getTotal(rows,'egameWinAmount');
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(getTotal(rows,'egameProfitAmount'));
			return r; 
		}
	}, {
		title : Admin.chess,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : function(vaule,row,index){
			var r=Admin.onePay+':'+(row.chessBetAmount||0);
			r=r+'</br>'+Admin.sendWin+':'+(row.chessWinAmount||0);
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(row.chessProfitAmount);
			return r; 
		},
		pageSummaryFormat:function(rows,aggsData){
			var r=Admin.onePay+':'+getTotal(rows,'chessBetAmount');
			r=r+'</br>'+Admin.sendWin+':'+getTotal(rows,'chessWinAmount');
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(getTotal(rows,'chessProfitAmount'));
			return r; 
		}
	}, {
		title : Admin.fish,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : function(vaule,row,index){
			var r=Admin.onePay+':'+(row.hunterBetAmount||0);
			r=r+'</br>'+Admin.sendWin+':'+(row.hunterWinAmount||0);
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(row.hunterProfitAmount);
			return r; 
		},
		pageSummaryFormat:function(rows,aggsData){
			var r=Admin.onePay+':'+getTotal(rows,'hunterBetAmount');
			r=r+'</br>'+Admin.sendWin+':'+getTotal(rows,'hunterWinAmount');
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(getTotal(rows,'hunterProfitAmount'));
			return r; 
		}
	}, {
		title : Admin.sports,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : function(vaule,row,index){
			var r=Admin.onePay+':'+(row.sportBetAmount||0);
			r=r+'</br>'+Admin.sendWin+':'+(row.sportWinAmount||0);
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(row.sportProfitAmount);
			return r; 
		},
		pageSummaryFormat:function(rows,aggsData){
			var r=Admin.onePay+':'+getTotal(rows,'sportBetAmount');
			r=r+'</br>'+Admin.sendWin+':'+getTotal(rows,'sportWinAmount');
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(getTotal(rows,'sportProfitAmount'));
			return r; 
		}
	}, {
		title : Admin.eleSports,
		align : 'center',
		width : '120',
		valign : 'middle',
		formatter : function(vaule,row,index){
			var r=Admin.onePay+':'+(row.esportBetAmount||0);
			r=r+'</br>'+Admin.sendWin+':'+(row.esportWinAmount||0);
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(row.esportProfitAmount);
			return r;
		},
		pageSummaryFormat:function(rows,aggsData){
			var r=Admin.onePay+':'+getTotal(rows,'esportBetAmount');
			r=r+'</br>'+Admin.sendWin+':'+getTotal(rows,'esportWinAmount');
			r=r+'</br>'+Admin.winOrLose+':'+getShuYing(getTotal(rows,'esportProfitAmount'));
			return r;
		}
	}, {
		field : 'tip',
		title : Admin.redMoney,
		align : 'center',
		width : '80',
		valign : 'middle'
	}, {
		field : 'otherMoney',
		title : Admin.othMoney,
		align : 'center',
		width : '80',
		valign : 'middle'
	}];
	return {
		render:function(type){
			Fui.addBootstrapTable({
				showPageSummary:true,
				showFooter : true,
				pagination:false,
				sidePagination:'client',
				columns:columns,
				url:baseInfo.adminBaseUrl +'/thirdReport/list.do'
			});
		}
	};
});