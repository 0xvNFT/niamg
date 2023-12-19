//! moment.jsのロケール設定
//! ロケール: 英語 (イギリス) [en-gb]
//! 作者: クリス・ゲドリム : https://github.com/chrisgedrim

;(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined'
    && typeof require === 'function' ? factory(require('moment')) :
        typeof define === 'function' && define.amd ? define(['moment'], factory) :
            factory(global.moment)
}(this, (function (moment) { 'use strict';

    var ja = moment.defineLocale('ja', {
        months : '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
        monthsShort : '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
        weekdays : '日曜日_月曜日_火曜日_水曜日_木曜日_金曜日_土曜日'.split('_'),
        weekdaysShort : '日_月_火_水_木_金_土'.split('_'),
        weekdaysMin : '日_月_火_水_木_金_土'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'HH:mm:ss',
            L : 'YYYY/MM/DD',
            LL : 'YYYY年M月D日',
            LLL : 'YYYY年M月D日 HH:mm',
            LLLL : 'YYYY年M月D日(ddd) HH:mm'
        },
        calendar : {
            sameDay : '[今日] LTに',
            nextDay : '[明日] LTに',
            nextWeek : 'dddd LTに',
            lastDay : '[昨日] LTに',
            lastWeek : '[前]dddd LTに',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s後',
            past : '%s前',
            s : '数秒',
            m : '1分',
            mm : '%d分',
            h : '1時間',
            hh : '%d時間',
            d : '1日',
            dd : '%d日',
            M : '1ヶ月',
            MM : '%dヶ月',
            y : '1年',
            yy : '%d年'
        },
        ordinalParse: /\d{1,2}/,
        ordinal : function (number) {
            return number;
        },
        week : {
            dow : 1, // Monday is the first day of the week.
            doy : 4  // The week that contains Jan 4th is the first week of the year.
        }
    });

    return ja;

})));