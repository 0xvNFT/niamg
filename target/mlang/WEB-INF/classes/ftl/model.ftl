package com.play.model;
<#assign hasDate=false>
<#assign hasBigDecimal=false>
<#list colList as col>
<#if col.javaType=="Date"><#assign hasDate=true></#if>
<#if col.javaType=="BigDecimal"><#assign hasBigDecimal=true></#if>
</#list>
<#if hasBigDecimal || hasDate>

<#if hasBigDecimal>import java.math.BigDecimal;</#if>
<#if hasDate>import java.util.Date;</#if>
</#if>

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
<#if common?has_content>/**
 * ${common} 
 *
 * @author admin
 *
 */</#if>
@Table(name = "${tableName}")
public class ${javaName} {
	<#list colList as col>
	<#if col.desc?has_content>/**
	 * ${col.desc}
	 */</#if>
	@Column(name = "${col.name}"<#if col.name==pk>, primarykey = true</#if>)
	private ${col.javaType} ${col.camelName};
	</#list>
<#list colList as col>

	public ${col.javaType} get${col.camelName?cap_first}() {
		return ${col.camelName};
	}

	public void set${col.camelName?cap_first}(${col.javaType} ${col.camelName}) {
		this.${col.camelName} = ${col.camelName};
	}
</#list>

}
