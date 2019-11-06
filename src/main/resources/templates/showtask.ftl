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

            case 'workgroupfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = false;
                break;

            case 'executorfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = true;
                document.getElementById("executor").disabled = false;
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

<div>
    <#if error?has_content>
        ${error}
    </#if>
</div>

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
                <option value="Срочность">Важность</option>
                <option value="Все">Все</option>
                  <#list urgencys as urgency>
                    <option <#if urgencyset?has_content && urgencyset  == "${urgency.name}">selected</#if> value="${urgency.name}">${urgency.name}</option>
                </#list>

            </select>

        </div>
        <br>

        <div class="form-check form-check-inline mt-3">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="workgroupfilter" <#if radiofilterset  == "workgroupfilter">checked="true"</#if>>


            <select class="form-control" name="workgroup" <#if radiofilterset  != "workgroupfilter">disabled</#if> id="workgroup" placeholder="РГ" onchange="toggleSelect(this)">
                <option value="РГ">РГ</option>
                <option value="Все">Все</option>
                <#list workgroups as workgroup>
                    <option <#if workgroupset?has_content && workgroupset  == "${workgroup.name}">selected</#if> value="${workgroup.name}">${workgroup.name}</option>
                </#list>

            </select>

        </div>
        <br>
        <div class="form-check form-check-inline mt-3">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="executorfilter" <#if radiofilterset  == "executorfilter">checked="true"</#if>>


            <select class="form-control" name="executor" <#if radiofilterset  != "executorfilter">disabled</#if> id="executor" placeholder="Исполнитель" onchange="toggleSelect(this)">
                <option value="Исполнитель">Исполнитель</option>
                <option value="Все">Все</option>
                <#list executors as executor>
                    <option <#if executorset?has_content && executorset  == "${executor.fio}">selected</#if> value="${executor.fio}">${executor.fio}</option>
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
                <th scope="col">Комментарий</th>
                <th scope="col">Исполнитель</th>
                <th scope="col">Отчет</th>
            </tr>
            </thead>
            <tbody>
                <#list tasks as task>

                        <tr class=<#if task.urgency.name =="Очень важно">"table-danger"<#elseif task.urgency.name =="Важно">"table-warning"<#elseif task.urgency.name =="Стандартно">"table-primary"<#else>"table-light"</#if>
                    onclick="window.open('${urlprefixPath}/showonetaskformanager?tid=${task.id}&regdata=${task.regDate}&execdate=${task.execDate}&workgroup=${task.workGroup.name}&description=${task.description}&urgency=${task.urgency.name}')">

                    <td>${task.id}</td>
                    <td>${task.regDate}</td>
                    <td>${task.execDate}</td>
                    <td>${task.workGroup.name}</td>
                    <td>${task.description}</td>
                    <td>${task.urgency.name}</td>
                <td><span class="badge badge-pill <#if task.status.name  == "Выполнено">badge-success<#elseif task.status.name  == "В работе у исполнителя">badge-secondary<#elseif task.status.name  =="Назначен исполнитель">badge-warning<#else>badge-danger</#if>">${task.status.name}</span></td>

                <td>${task.autor.fio}</td>
                <td><#if task.comment??>
                    ${task.comment}
                    <#else>Нет комментариев</#if></td>

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

        <!--
        <div class="form-group row mt-3">
            <div class="col-md-10">
                <label for="exampleFormControlTextarea1">Описание</label>
                <textarea class="form-control" name="description" rows="5" style="min-width: 100%"></textarea>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">
            <label for="inputDate">Финальная дата выполнения:</label>
            <input type="date" class="form-control" name="execdate">
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">
                <select class="form-control" name="workgroup"  placeholder="РГ">

                    <#list workgroups as workgroup>
                        <option value="${workgroup.name}">${workgroup.name}</option>
                    </#list>

                </select>
            </div>
        </div>

    <!--
        <div class="form-group row mt-3">
            <div class="col-sm-9">
                <#if fio??>
                <input type="text" class="form-control" name="fio" placeholder="ФИО" value="${fio}"/>
                <#else>
                    <input type="text" class="form-control" name="fio" placeholder="ФИО"/>
                </#if>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-9">
                <#if organization??>
                <input type="text" class="form-control" name="organization" placeholder="Организация" value="${organization}"/>
                    <#else>
                        <input type="text" class="form-control" name="organization" placeholder="Организация"/>
                </#if>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <#if catnum??>
                <input type="text" class="form-control" name="catnum" placeholder="Номер папки" value="${catnum}">
                <#else>
                    <input type="text" class="form-control" name="catnum" placeholder="Номер папки">
                </#if>
            </div>
        </div>

        <div class="file-upload-wrapper mb-2">
            <input type="file"  name="file" id="input-file-now" class="file-upload"/>
            <label class="input-file-label" for="input-file-now">Документы</label>
        </div>
-->


        <input type="hidden" name="_csrf" value="${_csrf.token}">

    </form>
    </div>
</div>
<br>


</@c.page>