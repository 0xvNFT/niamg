# transcan
- 通过波场自动上分

## 注册流程
1. 玩家申请绑定Tron链钱包（必须使用去中心化钱包地址，不可以使用交易所地址）
2. 提示玩家转账指定金额（请设置在 0.1001 - 0.1999之间）的TRX，到指定地址（平台的收款地址）
3. 如果15分钟内收到该笔转账，绑定成功，否则绑定失败

## 本业务模块会丢弃金额过小的交易
1. TRX  < 【0.0001】会丢弃（用来做账号绑定验证）
2. USDT < 【1】会丢弃