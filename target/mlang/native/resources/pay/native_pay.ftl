<!DOCTYPE html>
<html>
<head>
    <title>在线支付</title>
</head>
<body>
${error}
<form action=${nativeFormActionStr} method="post" id="redirectUrlFormId">
    <#list data?keys as k>
        <input name="${k}" value="${data[k]}" type="hidden"/></br>
    </#list>
</form>
<script>
    document.getElementById("redirectUrlFormId").submit()
</script>
</body>
</html>