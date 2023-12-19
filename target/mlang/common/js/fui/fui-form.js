define(['jquery_validation'],function(){
	/**
	 * dialog上的表单提交回调函数
	 */
	function dialogAjaxDone(json,$form){
		if(json.success ==true){
			if(!json.unReloadTable){
				var $table=$(".fui-box.active").data("bootstrapTable");
				if($table && $table.length){
					$table.bootstrapTable('refresh');
				}
			}
			if(!json.unclose){
				var closeBtn=$form.find(".fui-close");
				if(closeBtn.length){
					closeBtn.eq(0).click();
				}else{
					layer.closeAll();
				}
			}
			if(json.closeTabId){
				Fui.tab.close(json.closeTabId);
			}
			if(json.msg)layer.msg(json.msg||"操作成功");
		}else{
			layer.msg(json.msg);
		}
		if(json.resultCallback)json.resultCallback(json);
	}
	var initJQueryValidatorIsDone=false;
	function initJQueryValidator(){
		if(initJQueryValidatorIsDone)
			return;
		if ($.validator) {
			initJQueryValidatorIsDone=true;
			$.validator.addMethod("alphanumeric", function(value, element) {
				return this.optional(element) || /^\w+$/i.test(value);
			}, "Letters, numbers or underscores only please");
			
			$.validator.addMethod("lettersonly", function(value, element) {
				return this.optional(element) || /^[a-z]+$/i.test(value);
			}, "Letters only please"); 
			
			$.validator.addMethod("phone", function(value, element) {
				return this.optional(element) || /^[0-9 \(\)]{7,30}$/.test(value);
			}, "Please specify a valid phone number");
			
			$.validator.addMethod("postcode", function(value, element) {
				return this.optional(element) || /^[0-9 A-Za-z]{5,20}$/.test(value);
			}, "Please specify a valid postcode");
			
			$.validator.addMethod("date", function(value, element) {
				value = value.replace(/\s+/g, "");
				if (String.prototype.parseDate){
					var $input = $(element);
					var pattern = $input.attr('dateFmt') || 'yyyy-MM-dd';
		
					return !$input.val() || $input.val().parseDate(pattern);
				} else {
					return this.optional(element) || value.match(/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/);
				}
			}, "Please enter a valid date.");
			
			$.validator.addMethod("money", function(value, element) {
				return this.optional(element) || /^(([\d]{1,3}(,[\d]{3})*)|[\d]+)(\.[\d]{1,6})?$/.test(value);
			}, "Please specify a valid money");
			$.validator.addMethod("ip", function(value, element) { 
				 var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/; 
				 return this.optional(element) || (ip.test(value) && (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256)); 
			}, "Please enter a valid ip.");
			$.validator.addMethod("regexp", function(value, element) {
				var $input = $(element),reg=new RegExp("^"+$input.attr("regexp")+"$"),tip=$input.attr("tip")||"请输入正确格式的数据";
				$.validator.messages.regexp=tip;
				return this.optional(element) || reg.test(value);
			}, "Please enter a valid data.");
			$.validator.addClassRules({
				date: {date: true},
				alphanumeric: { alphanumeric: true },
				lettersonly: { lettersonly: true },
				phone: { phone: true },
				postcode: {postcode: true}
			});
			$.validator.setDefaults({errorElement:"span"});
			$.validator.autoCreateRanges = true;
			if((typeof Admin=='undefined')){
				$.extend($.validator.messages, {
					required: "这是必填字段",
					remote: "请修正此字段",
					email: "请输入有效的电子邮件地址",
					url: "请输入有效的网址",
					date: "请输入有效的日期",
					dateISO: "请输入有效的日期 (YYYY-MM-DD)",
					number: "请输入有效的数字",
					digits: "只能输入数字",
					creditcard: "请输入有效的信用卡号码",
					equalTo: "你的输入不相同",
					extension: "请输入有效的后缀",
					maxlength: $.validator.format("最多可以输入 {0} 个字符"),
					minlength: $.validator.format("最少要输入 {0} 个字符"),
					rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
					range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
					max: $.validator.format("请输入不大于 {0} 的数值"),
					min: $.validator.format("请输入不小于 {0} 的数值"),
					ip: "Ip地址格式错误",
					alphanumeric: "字母、数字、下划线",
					lettersonly: "必须是字母",
					phone: "请输入有效的电话号码，包含数字、空格、括号",
					money:"金额格式如：12332、21,032.12或10032.12"
				});
			}else{
				$.extend($.validator.messages, {
					required: Admin.validRequired,
					remote: Admin.validRemote,
					email: Admin.validEmail,
					url: Admin.validUrl,
					date: Admin.validDate,
					dateISO: Admin.validDateISO,
					number: Admin.validNumber,
					digits: Admin.validDigits,
					creditcard: Admin.validCreditcard,
					equalTo: Admin.validEqualTo,
					extension: Admin.validExtension,
					maxlength: $.validator.format(Admin.validMaxlength),
					minlength: $.validator.format(Admin.validMinlength),
					rangelength: $.validator.format(Admin.validRangelength),
					range: $.validator.format(Admin.validRange),
					max: $.validator.format(Admin.validMax),
					min: $.validator.format(Admin.validMin),
					ip: Admin.validIp,
					alphanumeric: Admin.validAlphanumeric,
					lettersonly: Admin.validLettersonly,
					phone: Admin.validPhone,
					money:Admin.validMoney
				});
			}
		}
	}
	return {
		initFormValidator:function(formSubmit,$p){
			initJQueryValidator();
			formSubmit.each(function(){
				var $form=$(this);
				$form.validate({
					onsubmit: false,
					focusInvalid: false,
					focusCleanup: true,
					onkeyup:false,
					unhighlight: function( element ) {
						if(element.layerIndex)layer.close(element.layerIndex);
					},
					ignore:".ignore",
					showErrors:function(errorMap,errorList){
						var i, elements;
						for ( i = 0; this.errorList[i]; i++ ) {
							var error = this.errorList[i];
							if ( this.settings.highlight ) {
								this.settings.highlight.call( this, error.element, this.settings.errorClass, this.settings.validClass );
							}
							error.element.layerIndex=layer.tips(error.message, error.element, {tips:1,style: ['background-color:#c00; color:#fff', '#c00'],time:3000,tipsMore: true});
						}
						if ( this.settings.unhighlight ) {
							for ( i = 0, elements = this.validElements(); elements[i]; i++ ) {
								this.settings.unhighlight.call( this, elements[i], this.settings.errorClass, this.settings.validClass );
							}
						}
					}
				});
				$form.submit(function(event){
					if($form.data("submit1")==1){
						layer.msg((typeof Admin=='undefined')?'不能重复提交表单':Admin.canntSubmit2);
						return false;
					}
					$form.data("submit1",1);
					var layerLoadingIndex=layer.load(2);
					if (!$form.valid()) {
						layer.closeAll('loading');
						$form.data("submit1",0);
						return false;
					}
					var callback=$form.data("callback")||dialogAjaxDone
						,errorFn=$form.data("errorcallback")
						,paramFn=$form.data("paramFn")
						,confirmMsg=$form.attr("confirmmsg")
						,unReloadTable=$form.attr("unReloadTable")
						,unclose=$form.attr("unclose")
						,closeTabId=$form.attr("closeTabId")
						,resultCallback=$form.data("resultCallback")
						,params=null, uploadFlag = false;
					if (paramFn){
						if(! $.isFunction(paramFn)){
							paramFn = eval('(' + paramFn + ')');
						}
						params=paramFn($form);
						if(params==false){
							layer.closeAll('loading');
							$form.data("submit1",0);
							return false;
						}
					}
					if("multipart/form-data" == $form.attr("enctype")){
						uploadFlag = true;
						params = new FormData($form[0]);  
					}else if(!params){
						params=$form.serialize();
					}
					if (! $.isFunction(callback)) callback = eval('(' + callback + ')');
					if(errorFn && ! $.isFunction(errorFn)){
						errorFn=eval('(' + errorFn + ')');
					}
					var _submitFn = function(){
						var options = {
							type: $form.method || 'POST',
							url:$form.attr("action"),
							data:params,
							dataType:"json",
							cache: false,
							success: function(json){
								json.unReloadTable=unReloadTable;
								json.unclose=unclose;
								json.closeTabId=closeTabId;
								json.resultCallback=resultCallback;
								if(callback)callback(json,$form);
								$form.data("submit1",0);
							},
							error: function(){
								layer.msg((typeof Admin=='undefined')?'网络异常':Admin.networkError);
								$form.data("submit1",0);
							},
							errorFn:function(json,status){
								if(errorFn){
									errorFn(json,status);
								}
								$form.data("submit1",0);
							},
							complete:function(){
								layer.closeAll('loading');
								$form.data("submit1",0);
							}
						};
						if(uploadFlag){
							options.contentType= false;
							options.processData=false;
						}
						$.ajax(options);

					}
					
					if (confirmMsg) {
						var layerIndex=layer.confirm(confirmMsg, function(){
							_submitFn();
							layer.close(layerIndex);
						});
					} else {
						_submitFn();
					}
					
					return false;
				});
			});
		}
	}
});