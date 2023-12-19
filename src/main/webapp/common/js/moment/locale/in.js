//! moment.js locale configuration
//! locale : English (United Kingdom) [en-gb]
//! author : Chris Gedrim : https://github.com/chrisgedrim

;(function (global, factory) {
   typeof exports === 'object' && typeof module !== 'undefined'
       && typeof require === 'function' ? factory(require('moment')) :
   typeof define === 'function' && define.amd ? define(['moment'], factory) :
   factory(global.moment)
}(this, (function (moment) { 'use strict';


    var ind = moment.defineLocale('in', {
        months : 'जनवरी_फरवरी_मार्च_अप्रैल_मई_जून_जुलाई_अगस्त_सितंबर_अक्टूबर_नवंबर_दिसंबर'.split('_'),
        monthsShort : 'जन_फर_मार्च_अप्रैल_मई_जून_जुलाई_अगस्त_सितंबर_अक्टूबर_नवंबर_दिसंबर'.split('_'),
        weekdays : 'रविवार_सोमवार_मंगलवार_बुधवार_गुरुवार_शुक्रवार_शनिवार'.split('_'),
        weekdaysShort : 'रवि_सोम_मंगल_बुध_गुरु_शुक्र_शनि'.split('_'),
        weekdaysMin : 'र_स_मं_ब_गु_शु_श'.split('_'),
        longDateFormat : {
            LT : 'HH:mm',
            LTS : 'HH:mm:ss',
            L : 'DD/MM/YYYY',
            LL : 'D MMMM YYYY',
            LLL : 'D MMMM YYYY HH:mm',
            LLLL : 'दिनवार, D MMMM YYYY HH:mm'
        },
        calendar : {
            sameDay : '[आज] LT को',
            nextDay : '[कल] LT को',
            nextWeek : 'dddd [को] LT',
            lastDay : '[कल] LT को',
            lastWeek : '[पिछले] dddd [को] LT',
            sameElse : 'L'
        },
        relativeTime : {
            future : '%s में',
            past : '%s पहले',
            s : 'कुछ सेकंड्स',
            m : 'एक मिनट',
            mm : '%d मिनट',
            h : 'एक घंटा',
            hh : '%d घंटे',
            d : 'एक दिन',
            dd : '%d दिन',
            M : 'एक महीना',
            MM : '%d महीने',
            y : 'एक साल',
            yy : '%d साल'
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

return ind;

})));
