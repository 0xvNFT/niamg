define(['moment', 'moment_zh', 'datetimepicker'], function (moment) {
    window.moment = moment;
	var locale='zh-cn';
	if(baseInfo.language){
		switch(baseInfo.language){
			case "en":locale='en-gb';break;
			case "id":locale='id';break;
			case "ms":locale='ms';break;
			case "th":locale='th';break;
			case "vi":locale='vi';break;
			case "br":locale='br';break;
            case "in":locale='in';break;
            case "es":locale='es';break;
            case "ja":locale='ja';break;
			default:locale='zh-cn';
		}
	}
	moment.locale(locale);
    return {
        initDatetime: function (dateEle, $p) {
            dateEle.each(function () {
                var it = $(this)
                    , format = it.attr("format") || 'YYYY-MM-DD'//' HH:mm:ss'
                    , options = {
                    locale: locale,
                    format: format,
                    minDate: it.attr("minDate") || false,
                    maxDate: it.attr("maxDate") || false,
                    sideBySide: true,
                    viewMode: it.attr("viewMode") || 'days',
                    showTodayButton: true,
                    showClear: true
                };
                it.attr("autocomplete","off");
                it.datetimepicker(options);
            });

            $(".fui-date-btn", $p).each(function () {
                var it = $(this)
                    , target = it.data("target") || 'today'
                    , parent = it.parents(".fui-data-wrap")
                    , dates = parent.find(".fui-date")
                    , $searchBtn = parent.find(".fui-date-search");
                if (!parent.length) {
                    layer.msg("日期按钮没有class='fui-data-wrap'的父级");
                    return;
                }
                if (dates.length != 2) {
                    layer.msg("日期按钮必须对应的2个日期控件");
                    return;
                }
                it.click(function () {
                    switch (target) {
                        case 'today':
                            var m = moment(), $d, format;
                            $d = dates.eq(0);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "00:00:00");
                            }
                            $d.val(m.format(format));

                            $d = dates.eq(1);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'yesterday':
                            var m = moment().subtract(1, 'days'), $d, format;
                            $d = dates.eq(0);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "00:00:00");
                            }
                            $d.val(m.format(format));

                            $d = dates.eq(1);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'thisWeek':
                            var m = moment(), w = m.week(), $d, format;
                            if (m.weekday() == 6) {
                                w = w - 1;
                            }
                            m.week(w);
                            m.day(1);
                            $d = dates.eq(0);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "00:00:00");
                            }
                            $d.val(m.format(format));
                            m.add(6, 'days');
                            $d = dates.eq(1);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'lastWeek':
                            var m = moment(), w = m.week(), $d, format;
                            if (m.weekday() == 6) {
                                w = w - 1;
                            }
                            m.week(w - 1);
                            m.day(1);
                            $d = dates.eq(0);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "00:00:00");
                            }
                            $d.val(m.format(format));
                            m.add(6, 'days');
                            $d = dates.eq(1);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'thisMonth':
                            var m = moment(), $d, format;
                            m.date(1);
                            $d = dates.eq(0);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "00:00:00");
                            }
                            $d.val(m.format(format));
                            m.add(1, 'months').subtract(1, 'days');
                            $d = dates.eq(1);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'lastMonth':
                            var m = moment(), $d, format;
                            m.date(1).subtract(1, 'months');
                            $d = dates.eq(0);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "00:00:00");
                            }
                            $d.val(m.format(format));
                            m.add(1, 'months').subtract(1, 'days');
                            $d = dates.eq(1);
                            format = $d.attr("format") || 'YYYY-MM-DD';
                            if (format.indexOf("HH:mm:ss") > -1) {
                                format = format.replace("HH:mm:ss", "23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'dayAfter' :
                            var m = moment(), beginFormat,endFormat, $begin, $end;
                            $begin = dates.eq(0);$end = dates.eq(1);
                            beginFormat = $begin.attr("format") || 'YYYY-MM-DD';
                            if (beginFormat.indexOf("HH:mm:ss") > -1) {
                                beginFormat = beginFormat.replace("HH:mm:ss", "00:00:00");
                            }
                            endFormat = $end.attr("format") || 'YYYY-MM-DD';
                            if (endFormat.indexOf("HH:mm:ss") > -1) {
                                endFormat = endFormat.replace("HH:mm:ss", "23:59:59");
                            }
                            if (!$begin.val() || !$end.val()) {
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }else if ($begin.val() !== $end.val().replace("23:59:59","00:00:00")) {
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }else if ($begin.val() === m.format(beginFormat)) {
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }else {
                                m = moment($begin.val()).add(1, 'days');
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }
                            break;
                        case 'dayBefore':
                            var m = moment(), beginFormat,endFormat, $begin, $end;
                            $begin = dates.eq(0);$end = dates.eq(1);
                            beginFormat = $begin.attr("format") || 'YYYY-MM-DD';
                            if (beginFormat.indexOf("HH:mm:ss") > -1) {
                                beginFormat = beginFormat.replace("HH:mm:ss", "00:00:00");
                            }
                            endFormat = $end.attr("format") || 'YYYY-MM-DD';
                            if (endFormat.indexOf("HH:mm:ss") > -1) {
                                endFormat = endFormat.replace("HH:mm:ss", "23:59:59");
                            }
                            if (!$begin.val() || !$end.val()) {
                                m = m.subtract(1, 'days');
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }else if ($begin.val() !== $end.val().replace("23:59:59","00:00:00")) {
                                m = m.subtract(1, 'days');
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }else {
                                m = moment($begin.val()).subtract(1, 'days');
                                $begin.val(m.format(beginFormat));
                                $end.val(m.format(endFormat));
                            }
                            break;
						case 'monthBefore':
                            var format,$d=dates.eq(0),m = moment($d.val());
                            m.date(1).subtract(1, 'months');
                            format=$d.attr("format")||'YYYY-MM-DD';
                            if(format.indexOf("HH:mm:ss")>-1){
                                format=format.replace("HH:mm:ss","00:00:00");
                            }
                            $d.val(m.format(format));
                            m.add(1,'months').subtract(1, 'days');
                            $d=dates.eq(1);
                            format=$d.attr("format")||'YYYY-MM-DD';
                            if(format.indexOf("HH:mm:ss")>-1){
                                format=format.replace("HH:mm:ss","23:59:59");
                            }
                            $d.val(m.format(format));
                            break;
                        case 'monthAfter':
                            var format,$d=dates.eq(0),m = moment($d.val());
                            m.date(1).add(1, 'months');
                            format=$d.attr("format")||'YYYY-MM-DD';
                            if(format.indexOf("HH:mm:ss")>-1){
                                format=format.replace("HH:mm:ss","00:00:00");
                            }
                            if(m.format(format) > moment().format(format)){
                                $d.val(moment().date(1).format(format));
                            }else {
                                $d.val(m.format(format));
                            }
                            m.add(1,'months').subtract(1, 'days');
                            $d=dates.eq(1);
                            format=$d.attr("format")||'YYYY-MM-DD';
                            if(format.indexOf("HH:mm:ss")>-1){
                                format=format.replace("HH:mm:ss","23:59:59");
                            }
                            if(m.format(format) > moment().format(format)){
                                $d.val(moment().format(format));
                            }else {
                                $d.val(m.format(format));
                            }
                            break;
                    }
                    $searchBtn.click();
                    return false;
                });
            });
        }
    }
});
