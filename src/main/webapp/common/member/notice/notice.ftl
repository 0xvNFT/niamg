<link rel="stylesheet" href="${base}/common/member/notice/css/ui.min.css?v=11"/>
<script>
    function getCookieValue(cookieName) {
        const strCookie = document.cookie
        const cookieList = strCookie.split(';')

        for(let i = 0; i < cookieList.length; i++) {
            const arr = cookieList[i].split('=')
            if (cookieName === arr[0].trim()) {
                return arr[1]
            }
        }
        return ''
    }

    var cookieLanguage = getCookieValue("userLanguage");
    //console.log("cookieLanguage-------------:" + cookieLanguage);
    var currentLanguage = cookieLanguage !== null &&  cookieLanguage !== ''? cookieLanguage : '${language}' ;
    //console.log("currentLanguage-------------:" + currentLanguage);

	var popShowTime = '${popShowTime}';
	var languagePop = currentLanguage;
</script>
<script type="text/html" id="tpl-message">
    <div>
        <div>
            <div class="side_left" data-bind="foreach:dialogNotice">
                <div class="side_item">
                    <a href="javascript:;"
                       data-bind="click:$parent.active.bind($data,$index()),css:$parent.activeIndex() == $index() ? 'active' : ''">
                        <span data-bind="text:title,style: { color: $data.messageFlag ? 'green' : '' }"></span>
                    </a>
                </div>
            </div>
            <div class="notice_main" data-bind="with:activeMessage">
                <div class="notice_title"><h1 data-bind="text:title"></h1></div>
                <div class="notice_text">
                    <div class="fleft notice_item" style="text-align:left;word-break:break-all;"
                         data-bind="html:content"></div>
                    <div class="fright"></div>
                </div>
            </div>
        </div>
    </div>
</script>
<script src="${base}/common/js/knockout-3.4.2.js"></script>
<script src="${base}/common/member/notice/js/dialog-plus-min.js"></script>
<script src="${base}/common/member/notice/js/jquery.cookie.js"></script>
<script src="${base}/common/member/notice/js/notice.js?v=12"></script>
<script src="${base}/common/lang/${language}.js?v=5"></script>
<#-- <script src="${base}/common/member/notice/js/knojs.notice.min.js"></script> -->
<#include "/common/member/floatFrame/index.ftl">