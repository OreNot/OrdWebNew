<#import "parts/common.ftl" as c>

<@c.page>
<script language="JavaScript">

    function openNewWin(url) {

        myWin= open(url);
    }

        function radioClick(elem) {

        switch(elem.value) {

        case 'orgfilter':
        document.getElementById("organization").disabled = false;
        document.getElementById("fio").disabled = true;
        break;

        case 'fiofilter':
        document.getElementById("organization").disabled = true;
        document.getElementById("fio").disabled = false;
        break;

    }
    }


</script>
<img src="${urlprefixPath}/img/greenatom.png" class="rounded float-left" width="145" height="159">
<br>


<div>
    <#if count?has_content>
        Всего документов в архиве: ${count}
    </#if>
</div>



<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="showarchive">

            <div class="form-check form-check-inline mb-1">
                <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="fiofilter" checked="true">
                <input type="text" id="fio"  class="form-control" name="filterByFio"  placeholder="Ф.И.О">
            </div>
            <br>
            <div class="form-check form-check-inline mb-1">
                <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="orgfilter">
                <input type="text" id="organization" class="form-control"  name="filterByOrg"  placeholder="Организация" disabled>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <button type="submit" class="btn btn-primary mb-2">Найти</button>
                </div>
            </div>

            <div>

<table  class="table mt-2">
    <thead>
    <tr>
        <th scope="col">ФИО</th>
        <th scope="col">Каталог</th>

    </tr>
    </thead>
    <tbody>
    <#list userlist as user>
    <!-- <div>-->
    <tr>
        <td>${user.fio}</td>
        <td>
        <#list user.userFiles as file>
            <input type="button" value="${file.orgDate}" onclick="openNewWin('${urlprefixPath}/orders/${file.filePath}')">
            <!--<a href="file:///${file.filePath}" target="_blank">${file.orgDate}</a> -->
        <#else>
            Пусто
        </#list>
        </td>

    </tr>
    <!--</div>-->
    <#else>
    Пусто
    </#list>
    </tbody>
</table>

            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

</form>
    </div>
</div>

</@c.page>