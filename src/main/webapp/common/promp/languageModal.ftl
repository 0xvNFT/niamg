<div class='modal-dialog'>
  <div class="modal-content">
    <div class="modal-header">
      <div class="modal-icon-2 pull-left ">
        <img src="/common/promp/images/modal.png" alt="" class="img-responsive d-in">
        <span class="modal-title region">
          Region
        </span>
      </div>
      <div class="modal-close">
        <img src="/common/promp/images/close.png" alt="" class="img-responsive pointer">
      </div>
    </div>
    <div class="modal-body">
      <div class="lang-box">
        <div class="lang-country country">中国</div>
        <div class="lang-box-container mgt10">
          <img src="/common/promp/images/cn.png" alt="" class='langImg'>
          <!--<span class="sel-lang active" name="cn">中文</span>-->
          <span class="sel-lang " name="en">English</span>
          <span class="sel-lang" name="pr">Português</span>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  $(function () {
    $(".mgt10 span").click(function () {
      $('.mgt10 span').removeClass('active')
      $(this).addClass('active')
      let lang = $(this).attr('name')
      var langArr = {}
      for (const key in params) {
        if (Object.hasOwnProperty.call(params, key)) {
          const element = params[key];
          // console.log(111, key);
          if (lang == key) {
            langArr = element
            console.log(langArr)
            $('.langImg').attr('src', langArr.img)
            $('.imgIndex').attr('src', langArr.imgIndex)
            for (const key1 in element) {
              if (Object.hasOwnProperty.call(element, key1)) {
                const element1 = element[key1];
                $('.' + key1).html(element1)
                $('.' + key1).attr('placeholder', element1)
              }
            }
          }
        }
      }
    });
    $('.dd-languge').click(function () {
      $('#languageModal').show();
    })
    $('.img-responsive').click(function () {
      $('#languageModal').hide();
    })
  })
  selectL('pr')
  function selectL (lan) {
    for (const key in params) {
      if (Object.hasOwnProperty.call(params, key)) {
        const element = params[key];
        // console.log(111, key);
        if (lan == key) {
          $('.langImg').attr('src', element.img)
          $('.imgLunbo').attr('src', element.imgIndex)
          $('.imgIndex').attr('src', element.imgIndex)
          for (const key1 in element) {
            if (Object.hasOwnProperty.call(element, key1)) {
              const element1 = element[key1];
              $('.' + key1).html(element1)
              $('.' + key1).attr('placeholder', element1)
            }
          }
        }
      }
    }
  }

</script>