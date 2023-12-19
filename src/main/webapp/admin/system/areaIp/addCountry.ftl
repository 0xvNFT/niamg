<form action="${adminBase}/stationWhiteArea/add.do" class="form-submit" id="agent_country_add_formId">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><@spring.message "admin.add"/></h4>
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-striped" id="check_all_country_tb_id">
					<tbody>
					<tr>
						<td class="text-right media-middle" style="width: 30%;"><@spring.message "admin.CountryCityname"/>：<br>
							<label class="checkbox-inline">
								<input type="checkbox" class="check_all_country"><font color="red" style="font-weight: bold;"><@spring.message "admin.select.all"/></font>
							</label>
						</td>
						<td class="text-left">
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CN"><@spring.message "admin.Chinesemainland"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="HK"><@spring.message "admin.HongKong,China"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="TW"><@spring.message "admin.Taiwan,China"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="PH"><@spring.message "admin.Philippines"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="KH"><@spring.message "admin.Cambodia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MY"><@spring.message "admin.Malaysia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="TH"><@spring.message "admin.Thailand"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MM"><@spring.message "admin.Burma"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="LA"><@spring.message "admin.Laos"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="BD"><@spring.message "admin.Bangladesh"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="IR"><@spring.message "admin.Iran"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MX"><@spring.message "admin.Mexico"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="FR"><@spring.message "admin.France"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="BY"><@spring.message "admin.Belarus"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="SG"><@spring.message "admin.Singapore"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="VN"><@spring.message "admin.VietNam"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="IE"><@spring.message "admin.Ireland"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="KR"><@spring.message "admin.Korea"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="DE"><@spring.message "admin.Germany"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="GN"><@spring.message "admin.Guinea"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="LB"><@spring.message "admin.Lebanon"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="UA"><@spring.message "admin.Ukraine"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="LV"><@spring.message "admin.Latvia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="RU"><@spring.message "admin.Russia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="US"><@spring.message "admin.UnitedStates"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="SO"><@spring.message "admin.Somalia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="BR"><@spring.message "admin.Brazil"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="ID"><@spring.message "admin.Indonesia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="JP"><@spring.message "admin.Japan"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="IN"><@spring.message "admin.India"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="SE"><@spring.message "admin.Sweden"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="NL"><@spring.message "admin.Netherlands"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="TR"><@spring.message "admin.Turkey"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="EG"><@spring.message "admin.Egypt"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="VE"><@spring.message "admin.Venezuela"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="KZ"><@spring.message "admin.Kazakhstan"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CO"><@spring.message "admin.Colombia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="UY"><@spring.message "admin.Uruguay"/>
							</label>

							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="BW"><@spring.message "admin.Botswana"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="ZA"><@spring.message "admin.SouthAfrica"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="DO"><@spring.message "admin.Dominican"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="NG"><@spring.message "admin.Nigeria"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="PE"><@spring.message "admin.Peru"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="LY"><@spring.message "admin.Libya"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="BA"><@spring.message "admin.Bosnia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CZ"><@spring.message "admin.Czech"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="IT"><@spring.message "admin.Italy"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="AE"><@spring.message "admin.UAE"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="SL"><@spring.message "admin.Sierraleone"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="EC"><@spring.message "admin.Ecuador"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CM"><@spring.message "admin.Cameroon"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="DJ"><@spring.message "admin.Djibouti"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="PK"><@spring.message "admin.Pakistan"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CR"><@spring.message "admin.CostaRica"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="UG"><@spring.message "admin.Uganda"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="GB"><@spring.message "admin.UnitedKingdom"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="PL"><@spring.message "admin.Poland"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="AL"><@spring.message "admin.Albania"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="PA"><@spring.message "admin.Panama"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="AM"><@spring.message "admin.Armenia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CA"><@spring.message "admin.Canada"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="GE"><@spring.message "admin.Georgia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="IQ"><@spring.message "admin.Iraq"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="FI"><@spring.message "admin.Finland"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="SK"><@spring.message "admin.Slovakia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="GR"><@spring.message "admin.Greece"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="AR"><@spring.message "admin.Argentina"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="CL"><@spring.message "admin.Chile"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="PS"><@spring.message "admin.Palestinian"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="ES"><@spring.message "admin.Spain"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="NI"><@spring.message "admin.Nicaragua"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="GT"><@spring.message "admin.Guatemala"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="KE"><@spring.message "admin.Kenya"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="TT"><@spring.message "admin.TrinidadandTobago"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="RO"><@spring.message "admin.Romanian"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="ZW"><@spring.message "admin.Zimbabwe"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="HN"><@spring.message "admin.Honduras"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="RS"><@spring.message "admin.Serbia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="YE"><@spring.message "admin.Yemen"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="NP"><@spring.message "admin.Nepal"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="IL"><@spring.message "admin.Israel"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MN"><@spring.message "admin.Mongolia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MG"><@spring.message "admin.Madagascar"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="BO"><@spring.message "admin.Bolivia"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MW"><@spring.message "admin.Malawi"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="OM"><@spring.message "admin.Oman"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="HU"><@spring.message "admin.Hungary"/>
							</label>
							<label class="checkbox-inline">
								<input type="checkbox" name="countryCodes" value="MZ"><@spring.message "admin.Mozambique"/>
							</label>
						</td>
					</tr>

					<tr>
						<td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
						<td class="text-left"><select name="status" class="form-control">
								<option value="2" selected><@spring.message "admin.enable"/></option>
								<option value="1"><@spring.message "admin.disabled"/></option>
							</select></td>
					</tr>
					<#--					<tr>-->
					<#--						<td class="text-right media-middle">限制类型：</td>-->
					<#--						<td class="text-left">-->
					<#--							<select class="form-control" name="limitType">-->
					<#--			                    <option value="1">会员前台</option>-->
					<#--			                    <option value="2">代理后台</option>-->
					<#--			                </select>-->
					<#--						</td>-->
					<#--					</tr>-->
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
				<button class="btn btn-primary"><@spring.message "admin.save"/></button>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
	var $countrytable = $("#check_all_country_tb_id");
	$countrytable.on("click", "input.check_all_country", function () {
		var $this = $(this), isChecked = $this.prop('checked');
		$countrytable.find('tbody input:checkbox:enabled').prop('checked', isChecked);

	});
</script>