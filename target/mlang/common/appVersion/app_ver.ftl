<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport"
          content="width=device-width; initial-scale=1.0;minimum-scale=1.0; maximum-scale=1.0; user-scalable=yes;">
    <#assign res="${base}/common/appVersion">
    <script src="${res}/js/jquery-1.8.2.js"></script>
    <link href="${res}/css/mui.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${res}/css/mobile.css?v=1.0.1">
    <title><@spring.message "admin.version.update"/></title>

    <script type="application/javascript">

        function addVersion() {
            var id = $('#id').val();
            var version = $('#version').val();
            if (version == ""){
                alert('<@spring.message "admin.write.version.new"/>');
                return;
            }
            var content = $('#content').val();
            if (content == ""){
                alert('<@spring.message "admin.write.update.content"/>');
                return;
            }
            console.log("verison = " + version + ",content = " + content);

            $('#sql').text("INSERT INTO \"public\".\"app_update\" (\"id\",\"version\", \"content\", \"status\", \"station_ids\", \"platform\")\n" +
                "VALUES('" + id + "','" + version + "','" + content + "','2','','');");
        }

    </script>


</head>
<body>
<div style="margin-left: 40px;margin-top: 40px">

    <h3><span style="color: red"><@spring.message "admin.notice.version"/>;
        <@spring.message "admin.notice.version.two"/></span></h3>
    <h3 style="margin-top: 20px"><@spring.message "admin.history.version"/>：</h3>
    <div style="margin-top: 20px;">
        <ul style="overflow:auto;max-height: 400px">
            <#list versions as version>
                <li><span>${version.id}</span>---<span>${version.version}</span>--------------------<span>${version.content}</span></li>
            </#list>
        </ul>
    </div>
    <p style="margin-top: 20px"><input type="hidden" id="id" value="${lastVersionId}"></p>
    <div style="margin-top: 20px"><span><@spring.message "admin.new.version"/>：</span><input style="width: 200px;height: 30px" name="version" id="version" value="${lastVersion}">
        <span style="color: red;font-weight: bold"><@spring.message "admin.notice.version.more.last"/></span></div>
    <div style="margin-top: 20px"><span><@spring.message "admin.modify.content.info"/>：</span><textarea id="content" wrap="virtual" rows="15" cols="80" name="textfield10"><@spring.message "admin.update.model.que"/>\n</textarea>
        <span style="color: red;font-weight: bold"><@spring.message "admin.one.update.add"/>\n</span></div>
    <button style="margin-top: 20px;width: 150px;height: 40px" onclick="addVersion();"><@spring.message "admin.submit.up"/></button>
    <div id="sqlDiv" style="margin-top: 20px"><span style="color: red;"><@spring.message "admin.version.sql"/>：</span></div>
    <div><textarea style="color: dodgerblue;" id="sql" rows="10" cols="80" name="textfield10"></textarea></div>

</div>
</body>
</html>