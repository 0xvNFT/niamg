let configJs = {
  // b000: {
  //   hideCarousel: false,//是否隐藏首页轮播图
  //   protocol: true, //是否显示开户协议
  //   indexFirstDpc: true, //首页低频彩在前，传统彩在后
  //   bettingRuleText: '竟猜规则', //购彩大厅彩票规则文字修改
  //   hideWithDrawRecordTotle: true, //是否隐藏取款记录头部总提现
  // },
  bx101: {
    //bx101 前台绑定银行卡管理需要修改成PIX/CNPJ管理
    en: {
      changeBankIndex: "PIX / CNPJ management",
      bankRealName: "Name and Nickname",
      bankCardNo: "PIX / CNPJ",
      sureBank: "Confirm PIX / CNPJ",
      changeRePutBankNo: "Please enter PIX / CNPJ again",
      pixTip: "PIX supports the following recharge methods:",
      pixTip1:
        "Method 1: CPF (personal tax number), a total of 11 digits. Format: [XXXXXXXXXXX]",
      pixTip2:
        "Method 2: CNPJ (Federal tax identification number registered by the national legal person), a total of 14 digits. Format: [XX.XXX.XXX/XXXX.XX]",
      pixTip3:
        "Method 3: Commonly used mobile phone number, total [10 or 11] digits. Format: [+55XXXXXXXXX]",
      pixTip4: "Method 4: Commonly used email accounts",

    },
    br: {
      changeBankIndex: "Gerecimento de Pix / CNPJ",
      bankRealName: "Nome e Apelido",
      bankCardNo: "PIX / CNPJ",
      sureBank: "Confirme PIX / CNPJ",
      changeRePutBankNo: "Insira novamente o PIX / CNPJ",
      pixTip: "PIX suporta os seguintes métodos de recarga:",
      pixTip1:
        "Método 1: CPF, com um total de 11 dígitos. Formato: [XXXXXXXXXXXXX]",
      pixTip2:
        "Método 2: CNPJ (número de identificação fiscal federal registrado pela pessoa jurídica nacional), um total de 14 dígitos. Formato: [XX.XXX.XXX/XXXX.XX]",
      pixTip3:
        "Método 3: Número de telefone celular comumente usado, total de [10 ou 11] dígitos. Formato: [+55XXXXXXXXXXX]",
      pixTip4: "Método 4: Contas de e-mail comumente usadas",
      nowLotRebateAmount: "Jogos",
      nowLiveRebateAmount: "Ao Vivo",
      nowEgameRebateAmount: "Slots",
      nowSportRebateAmount: "Esportes",
      nowChessRebateAmount: "Jogos de Cartas",
      nowEsportRebateAmount: "Pescaria",
      nowFishingRebateAmount: "Bingo",
      realBettingMoney:'Rollover',
    },
  },
  bx102: {
    en: {
      pixTip: "PIX supports the following recharge methods:",
      pixTip1:
        "Method 1: CPF (personal tax number), a total of 11 digits. Format: [XXXXXXXXXXX]",
      pixTip2:
        "Method 2: CNPJ (Federal tax identification number registered by the national legal person), a total of 14 digits. Format: [XX.XXX.XXX/XXXX.XX]",
      pixTip3:
        "Method 3: Commonly used mobile phone number, total [10 or 11] digits. Format: [+55XXXXXXXXX]",
      pixTip4: "Method 4: Commonly used email accounts",
    },
    br: {
      pixTip: "PIX suporta os seguintes métodos de recarga:",
      pixTip1:
        "Método 1: CPF, com um total de 11 dígitos. Formato: [XXXXXXXXXXXXX]",
      pixTip2:
        "Método 2: CNPJ (número de identificação fiscal federal registrado pela pessoa jurídica nacional), um total de 14 dígitos. Formato: [XX.XXX.XXX/XXXX.XX]",
      pixTip3:
        "Método 3: Número de telefone celular comumente usado, total de [10 ou 11] dígitos. Formato: [+55XXXXXXXXXXX]",
      pixTip4: "Método 4: Contas de e-mail comumente usadas",
    },
  },
  yd101: {

  },
  yd102: {

  },
  yn103: {
    vi: {
      loginPwd: "Mật khẩu",
      nowBetNum: "Tổng cược hợp lệ hiện tại",
      drawNeedBet: "Tổng cược hợp lệ cần cho rút tiền",
      typeText:"Ngân Hàng"
    }
  },
  // t308:{
  //   br:{
  //     realBettingMoney:'Rollover'
  //   }
  // }
};

let config = {
  // upPrompt: {
  //   other: {
  //     station: 'w167, w147, h029',
  //     val: '充值建议:使用银行卡转账成功率100%，赠送0.2%返利',
  //   },
  //   h018: '充值建议:使用银行卡转账成功率100%，赠送0.3%返利',
  // },
};

function configFn(stationCode, pam) {
  for (let key in config) {
    let json1 = config[key];
    if (key === pam) {
      for (let key1 in json1) {
        let json2 = json1[key1];
        if (json2[stationCode]) {
          return json2[stationCode];
        } else {
          if (json2.station && json2.station.includes(stationCode)) {
            return json2.val;
          }
        }
      }
    }
  }

  return null;
}

function configStation(stationCode) {
  if (configJs[stationCode]) {
    return configJs[stationCode];
  } else return {};
}

export default { configFn, configStation };
