<#import "parts/common.ftl" as c>

<@c.page>
<img src="${urlprefixPath}/img/greenatom.png" class="rounded float-left" width="145" height="159">
<br>


<script language="JavaScript">
    <!-- hide
    function openNewWin(url) {
        myWin= open(url);
    }

</script>


<br>
<div>
    <#if error?has_content>
        ${error}
    </#if>
</div>
<br>
<div class="form-row">
    <div class="form-group col-md-10">
    <form method="post" enctype="multipart/form-data" id="js-upload-form">

        <div class="form-group row mt-3">
        <div class="col-auto">
        <select class="form-control" name="urgency"  placeholder="Важность">

            <#list urgencys as urgency>
                <option value="${urgency.name}">${urgency.name}</option>
            </#list>

        </select>
        </div>
        </div>

        <div><h4>Мои задачи</h4></div>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Дата регистрации</th>
                <th scope="col">Дата исполнения</th>
                <th scope="col">РГ</th>
                <th scope="col">Описание</th>
                <th scope="col">Срочность</th>
                <th scope="col">Статус</th>
                <th scope="col">Автор</th>

            </tr>
            </thead>
            <tbody>
                <#list myrgtasks as task>

                   <tr class=<#if task.urgency.name =="Очень важно">"table-danger"<#elseif task.urgency.name =="Важно">"table-warning"<#elseif task.urgency.name =="Стандартно">"table-primary"<#else>"table-light"</#if>
                    onclick="window.open('${urlprefixPath}/showonetaskforuser?tid=${task.id}&regdata=${task.regDate}&execdate=${task.execDate}&workgroup=${task.workGroup.name}&description=${task.description}&urgency=${task.urgency.name}')">


                    <td>${task.regDate}</td>
                    <td>${task.execDate}</td>
                    <td>${task.workGroup.name}</td>
                    <td>${task.description}</td>
                    <td>${task.urgency.name}</td>
                    <td>${task.status.name}</td>
                    <td>${task.autor.username}</td>

                </tr>
      <#else>
                Пусто
                </#list>
            </tbody>
        </table>


        <div class="form-group row mt-3">
            <div class="col-sm-3">
    <button type="submit" class="btn btn-primary mb-2">Создать задачу</button>

            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}">

    </form>
    </div>
</div>
<br>


</@c.page>