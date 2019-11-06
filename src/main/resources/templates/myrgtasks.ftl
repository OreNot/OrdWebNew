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
<script>
    function radioClick(elem) {

        switch(elem.value) {
            case 'statusfilter':
                document.getElementById("status").disabled = false;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = true;
                break;

            case 'urgencyfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = false;
                document.getElementById("workgroup").disabled = true;
                break;
        }
    }
</script>
<script>
    function toggleSelect(elem) {

        document.getElementById("submit").click();
    }

    function toggleCheckbox(elem) {
        document.getElementById("submit").click();
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

        <div class="form-check form-check-inline">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="statusfilter" <#if radiofilterset  == "statusfilter">checked="true"</#if>>


            <select class="form-control" name="status" <#if radiofilterset  != "statusfilter">disabled</#if> id="status" placeholder="Статус" onchange="toggleSelect(this)">
                <option value="Статус">Статус</option>
                <option value="Все">Все</option>
                <#list statuses as status>
                    <option <#if statusset?has_content && statusset  == "${status.name}">selected</#if> value="${status.name}">${status.name}</option>
                </#list>

            </select>

        </div>
        <br>

        <div class="form-check form-check-inline mt-3">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="urgencyfilter" <#if radiofilterset  == "urgencyfilter">checked="true"</#if>>


            <select class="form-control" name="urgency" <#if radiofilterset  != "urgencyfilter">disabled</#if> id="urgency" placeholder="Срочность" onchange="toggleSelect(this)">
                <option value="Важность">Важность</option>
                <option value="Все">Все</option>
                <#list urgencys as urgency>
                    <option <#if urgencyset?has_content && urgencyset  == "${urgency.name}">selected</#if> value="${urgency.name}">${urgency.name}</option>
                </#list>

            </select>

        </div>


        <br>
        <div class="custom-control custom-checkbox">
            <input type="checkbox" class="custom-control-input" id="finished" name="finished" <#if finished == "on">checked</#if> onchange="toggleCheckbox(this)">
            <label class="custom-control-label"  for="finished">Исключая завершенные</label>
        </div>
        <br>
        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <button type="submit" id="submit" hidden class="btn btn-primary mb-2">Фильтр</button>

            </div>
        </div>

        <div><h4>Задачи</h4></div>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Номер</th>
                <th scope="col">Дата регистрации</th>
                <th scope="col">Дата исполнения</th>
                <th scope="col">РГ</th>
                <th scope="col">Описание</th>
                <th scope="col">Срочность</th>
                <th scope="col">Статус</th>
                <th scope="col">Автор</th>
                <th scope="col">Исполнитель</th>
                <th scope="col">Отчет</th>
            </tr>
            </thead>
            <tbody>
                <#list myrgtasks as task>

                        <tr class=<#if task.urgency.name =="Очень важно">"table-danger"<#elseif task.urgency.name =="Важно">"table-warning"<#elseif task.urgency.name =="Стандартно">"table-primary"<#else>"table-light"</#if>
                    onclick="window.open('${urlprefixPath}/showonetaskforgroupboss?tid=${task.id}&regdata=${task.regDate}&execdate=${task.execDate}&workgroup=${task.workGroup.name}&description=${task.description}&urgency=${task.urgency.name}')">


                    <td>${task.id}</td>
                    <td>${task.regDate}</td>
                    <td>${task.execDate}</td>
                    <td>${task.workGroup.name}</td>
                    <td>${task.description}</td>
                    <td>${task.urgency.name}</td>
                    <td><span class="badge badge-pill <#if task.status.name  == "Выполнено">badge-success<#elseif task.status.name  == "В работе у исполнителя">badge-secondary<#elseif task.status.name  =="Назначен исполнитель">badge-warning<#else>badge-danger</#if>">${task.status.name}</span></td>
                    <td>${task.autor.fio}</td>
                    <td><#if task.executor??>
                    ${task.executor.fio}
                    <#else>Исполнитель не назначен</#if></td>
                    <td>
                    <#if task.report??>
                    ${task.report}
                    <#else>Пусто</#if>
                     </td>
                </tr>
      <#else>
                Пусто
                </#list>
            </tbody>
        </table>


        <input type="hidden" name="_csrf" value="${_csrf.token}">

    </form>
    </div>
</div>
<br>


</@c.page>