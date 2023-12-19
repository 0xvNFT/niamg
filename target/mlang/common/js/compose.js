/**
 * @param tag       标签
 * @param classes   类
 * @param styles    样式
 * @param props     属性值
 * @param text      文本内容
 * @param subDom    子元件
 * @returns {*|jQuery|HTMLElement}
 */
function createDynamicsDom([tag, {classes = [], styles = {}, props = {}, text = "", subDom = []}]) {
    const node = $(`<${tag}>`)
    node.addClass(classes.join(" ")).css(styles).attr(props).text(text)
    subDom.forEach(subNode => node.append(createDynamicsDom(subNode)))
    return node
}


function createDynamicsDomOuterHTML([tag, {classes = [], styles = {}, props = {}, text = "", subDom = []}]) {
    return createDynamicsDom([tag, {
        classes: classes,
        styles: styles,
        props: props,
        text: text,
        subDom: subDom
    }]).prop("outerHTML")
}


function createDynamicsDomOuterHTMLList(domList = []) {
    return Array.isArray(domList) && domList.length > 0 ? domList.map(dom => createDynamicsDomOuterHTML(dom)).join("") : ""
}


/**
 *
 * 用于后台提示
 *
 * @param contenttext       纯文字内容
 * @param customizeddom     自定义 html 语法
 * @param direction         1 上；2 右；3 下；4 左，预设为 1
 * @param backgroundcolor   背景颜色，预设为红色
 * @param width             设定宽，预设为 auto，表示自动
 * @param height            设定高，预设为 auto，表示自动
 * @param classes           自定义类，绑定在 layui-layer-content 外层
 */
function showTips($this) {
    $this = $($this);
    let contenttext = $this.data("tips-content-text") ? $this.data("tips-content-text") : ""
    let customizeddom = $this.data("tips-customized-dom")
    let direction = $this.data("tips-direction") ? $this.data("tips-direction") : 1
    let backgroundcolor = $this.data("tips-backgroundcolor") ? $this.data("tips-backgroundcolor") : "#d9534f"
    let width = $this.data("tips-width") ? $this.data("tips-width") : 'auto'
    let height = $this.data("tips-height") ? $this.data("tips-height") : 'auto'
    let classes = $this.data("tips-classes") ? $this.data("tips-classes") : ''

    let content = ""
    if(typeof customizeddom != 'undefined'){ // 如果有自定义 html 语法，采用自定义内容
        content = customizeddom
    } else {
        content = createDynamicsDomOuterHTML([
            'span', {
                styles: {'font-size': '14px'},
                text: contenttext
            }
        ])
    }
    layer.tips(
        content, // 可接受 html
        $this,
        {
            tips: [direction, backgroundcolor],
            time: 0, // tips 一次只能显示一个，就预设为 0，不做调整
            area: [width, height],
            skin: classes,
        }
    );
}

/**
 * 关闭所有的 tips 层
 */
function closeTips() {
    layer.closeAll('tips');
}

/**
 * 滑入显示提示、滑出关闭提示
 */
require(['jquery'], function ($) {
    $(document).ready(function () {
        $(document).on("mouseenter", ".show-tips", function () {
            showTips($(this))
        }).on("mouseleave", ".show-tips", function () {
            closeTips()
        });
    });
});