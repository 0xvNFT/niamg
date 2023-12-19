<div class="no-have-google-auth" <#if !canBindAuth>style="display: none"</#if>>
    <h3>
        <label class="label-danger label"><@spring.message "admin.googleValid5"/></label>
    </h3>
    <div class="form-inline">
        <a class="btn btn-primary add-btn" href="#"><@spring.message "admin.googleValid6"/></a>
    </div>
</div>
<div class="have-google-auth" <#if canBindAuth>style="display: none"</#if>>
    <h3>
        <label class="label-success label"><@spring.message "admin.googleValid7"/></label>
    </h3>
</div>
<div class="have-google-auth" style="display: none">
    <div class="text-center">
        <img style="width: 400px;height: 400px" src="" class="img-thumbnail">
    </div>
    <h3 style="color:red;font-size: 20px">
        <@spring.message "admin.googleValid8"/><br>
        <@spring.message "admin.googleValid8"/><br>
        <@spring.message "admin.googleValid8"/><br>
    </h3>
</div>
<script type="text/javascript">
    requirejs(['jquery'], function () {
        $('.no-have-google-auth .add-btn').click(function () {
            $.ajax({
                type: "POST",
                url: "${adminBase}/googleAuth/save.do",
                success: function (data, textStatus, xhr) {
                    if (data.success) {
                        layer.msg(Admin.successfulOperation);
                        $(".have-google-auth").find('img').attr('src', '${adminBase}/googleAuth/getImage.do?v=${nowTime}');
                        $(".no-have-google-auth").hide();
                        $(".have-google-auth").show();
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        })
    })
</script>
