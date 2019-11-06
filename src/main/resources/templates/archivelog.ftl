<#import "parts/common.ftl" as c>

<@c.page>
<img src="${urlprefixPath}/img/greenatom.png" class="rounded float-left" width="145" height="159">



<script language="JavaScript">
    <!-- hide
    function openNewWin(url) {
        myWin= open(url);
    }

</script>

<div><h4>Лог Архива</h4></div>
<br>
    <#if countToday?has_content>
        Добавлено сегодня: ${countToday}
    </#if>
<br>
    <#if countYersterday?has_content>
    Добавлено вчера: ${countYersterday}
    </#if>
<br>
    <#if monthCounter?has_content>
    Добавлено за месяц: ${monthCounter}
    </#if>

<br>
<br>
<br>
<div>
    <#if error?has_content>
        <#list error as err>
            ${err}
        </#list>
    </#if>
</div>
<div>
    <#if log?has_content>
        <#list log as logstr>
            ${logstr}<br>
        </#list>
    </#if>
</div>


</@c.page>