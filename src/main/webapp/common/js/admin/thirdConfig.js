//AG盘口
var ag_oddtype = [
    {'value': 'A', 'label': '20-50000'},
    {'value': 'B', 'label': '50-5000'},
    {'value': 'C', 'label': '20-10000'},
    {'value': 'D', 'label': '200-20000'},
    {'value': 'E', 'label': '300-30000'},
    {'value': 'F', 'label': '400-40000'},
    {'value': 'G', 'label': '500-50000'},
    {'value': 'H', 'label': '1000-100000'},
    {'value': 'I', 'label': '2000-200000'}
];
var config = {
    "ag_oddtype": ag_oddtype
};


function configValueFormatter(value,row,index) {
    if (row.configName) {
        if (config[row.configName]) {
            var htm = value;
            $.each(config[row.configName], function (n, configValue) {
                if (configValue.value === value) {
                    htm = configValue.label;
                }
            });
            return htm;
        } else {
            return value;
        }
    } else {
        return value;
    }
}
function configSelect() {
    var $select = $("select[name='configName']");
    var type = $select.find("option:selected").data("type");
    var configName = $select.val();
    var htm = '';
    if (configName) {
        if (type === 'select') {
            htm += '<select name="configValue" class="form-control">';
            if (config[configName]) {
                $.each(config[configName], function (n, value) {
                    htm += '<option value="' + value.value + '"';
                    if (defConfigValue && defConfigValue === value.value) {
                        htm += ' selected';
                    }
                    htm += '>' + value.label + '</option>';
                });
            }
            htm += '</select>';
        } else if (type === 'text') {
            htm += ' <input name="configValue" class="form-control required" type="text">'
        }
        $(".configValue").empty().html(htm);
    }
}
