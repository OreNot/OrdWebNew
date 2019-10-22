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
                document.getElementById("executor").disabled = true;
                break;

            case 'urgencyfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = false;
                document.getElementById("workgroup").disabled = true;
                document.getElementById("executor").disabled = true;
                break;

            case 'workgroupfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = false;
                document.getElementById("executor").disabled = true;
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
</script>

<div>
    <#if error?has_content>
        ${error}
    </#if>
</div>

<div class="form-row">
    <div class="form-group col-md-10">
    <form method="post" enctype="multipart/form-data" id="js-upload-form">
<!--
        <div class="form-check form-check-inline">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="statusfilter" checked="true">


        <select class="form-control" name="statusname" id="status" placeholder="Статус" onchange="toggleSelect(this)">
            <option value="Статус">Статус</option>
            <option value="Все">Все</option>
            <#list statuses as status>
                <option value="${status.name}">${status.name}</option>
            </#list>

        </select>

        </div>
        <br>

        <div class="form-check form-check-inline mt-3">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="urgencyfilter">


            <select class="form-control" name="urgencyname" disabled id="urgency" placeholder="Срочность" onchange="toggleSelect(this)">
                <option value="Срочность">Важность</option>
                <option value="Все">Все</option>
                  <#list urgencys as urgency>
                    <option value="${urgency.name}">${urgency.name}</option>
                </#list>

            </select>

        </div>
        <br>
        <div class="form-check form-check-inline mt-3">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="workgroupfilter">


            <select class="form-control" name="workgroupname" disabled id="workgroup" placeholder="РГ" onchange="toggleSelect(this)">
                <option value="РГ">РГ</option>
                <option value="Все">Все</option>
                <#list workgroups as workgroup>
                    <option value="${workgroup.name}">${workgroup.name}</option>
                </#list>

            </select>

        </div>
        <br>
        <div class="form-check form-check-inline mt-3">

            <input type="radio" class="form-check-input" name="radiofilter" onclick="radioClick(this)" value="executorfilter">


            <select class="form-control" name="executorname" disabled id="executor" placeholder="Исполнитель" onchange="toggleSelect(this)">
                <option value="Исполнитель">Исполнитель</option>
                <option value="Все">Все</option>
                <#list executors as executor>
                    <option value="${executor.username}">${executor.username}</option>
                </#list>

            </select>

        </div>
        -->
        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <button class="btn btn-primary mb-2"  onclick="window.open('${urlprefixPath}/taskfounder')"
                    >К поиску заданий</button>

            </div>
        </div>
        <br>
        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <button type="submit" id="submit" hidden class="btn btn-primary mb-2">Найти задачу</button>

            </div>
        </div>
        <div><h4>Задачи по статусам </h4></div>
        <div>
            <table class="table" id="rgtable">
                <thead>
                <tr>
                    <th scope="col">РГ</th>
                    <th scope="col">Зарегистрировано</th>
                    <th scope="col">Назначена РГ</th>
                    <th scope="col">Назначен исполнитель</th>
                    <th scope="col">Возвращено на распределение</th>
                    <th scope="col">В работе РГ</th>
                    <th scope="col">В работе у исполнителя</th>
                    <th scope="col">Выполнено</th>
                </tr>
                </thead>
                <tbody>

                    <#list statusByGroups as statusByGroup>
                            <tr>
                    <td>${statusByGroup.workGroupName}</td>
                    <td class=<#if (statusByGroup.regStatus?number > 50)>"table-danger"<#elseif (statusByGroup.regStatus?number > 20 && statusByGroup.regStatus?number < 50)>"table-warning"<#elseif (statusByGroup.regStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                                ${statusByGroup.regStatus}</td>
                    <td class=<#if (statusByGroup.setRGStatus?number > 50)>"table-danger"<#elseif (statusByGroup.setRGStatus?number > 20 && statusByGroup.setRGStatus?number < 50)>"table-warning"<#elseif (statusByGroup.setRGStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                    ${statusByGroup.setRGStatus}</td>
                    <td class=<#if (statusByGroup.setExecStatus?number > 50)>"table-danger"<#elseif (statusByGroup.setExecStatus?number > 20 && statusByGroup.setExecStatus?number < 50)>"table-warning"<#elseif (statusByGroup.setExecStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                    ${statusByGroup.setExecStatus}</td>
                    <td class=<#if (statusByGroup.returnStatus?number > 50)>"table-danger"<#elseif (statusByGroup.returnStatus?number > 20 && statusByGroup.returnStatus?number < 50)>"table-warning"<#elseif (statusByGroup.returnStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                    ${statusByGroup.returnStatus}</td>
                    <td class=<#if (statusByGroup.inRGWorkStatus?number > 50)>"table-danger"<#elseif (statusByGroup.inRGWorkStatus?number > 20 && statusByGroup.inRGWorkStatus?number < 50)>"table-warning"<#elseif (statusByGroup.returnStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                    ${statusByGroup.inRGWorkStatus}</td>
                    <td class=<#if (statusByGroup.inExecWorkStatus?number > 50)>"table-danger"<#elseif (statusByGroup.inExecWorkStatus?number > 20 && statusByGroup.inExecWorkStatus?number < 50)>"table-warning"<#elseif (statusByGroup.inExecWorkStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                    ${statusByGroup.inExecWorkStatus}</td>
                    <td class=<#if (statusByGroup.compliteStatus?number > 50)>"table-danger"<#elseif (statusByGroup.compliteStatus?number > 20 && statusByGroup.compliteStatus?number < 50)>"table-warning"<#elseif (statusByGroup.compliteStatus?number < 20)>"table-primary"<#else>"table-light"</#if>>
                    ${statusByGroup.compliteStatus}</td>


                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>
        </div>
        <div><h4>Задачи по важности (за исключением выполненых)</h4></div>
        <div>
            <table class="table" id="rgtable">
                <thead>
                <tr>
                    <th scope="col">РГ</th>
                    <th scope="col">Очень важно</th>
                    <th scope="col">Важно</th>
                    <th scope="col">Стандартно</th>
                    <th scope="col">Менее важно</th>
                </tr>
                </thead>
                <tbody>

                    <#list urgencyByGroups as urgencyByGroup>
                    <tr>
                        <td>${urgencyByGroup.workGroupName}</td>
                            <td class=<#if (urgencyByGroup.mostUrgency?number > 50)>"table-danger"<#elseif (urgencyByGroup.mostUrgency?number > 20 && urgencyByGroup.mostUrgency?number < 50)>"table-warning"<#elseif (urgencyByGroup.mostUrgency?number < 20)>"table-primary"<#else>"table-light"</#if>>
                        ${urgencyByGroup.mostUrgency}</td>
                            <td class=<#if (urgencyByGroup.urgency?number > 50)>"table-danger"<#elseif (urgencyByGroup.urgency?number > 20 && urgencyByGroup.urgency?number < 50)>"table-warning"<#elseif (urgencyByGroup.urgency?number < 20)>"table-primary"<#else>"table-light"</#if>>
                        ${urgencyByGroup.urgency}</td>
                            <td class=<#if (urgencyByGroup.standart?number > 50)>"table-danger"<#elseif (urgencyByGroup.standart?number > 20 && urgencyByGroup.standart?number < 50)>"table-warning"<#elseif (urgencyByGroup.standart?number < 20)>"table-primary"<#else>"table-light"</#if>>
                        ${urgencyByGroup.standart}</td>
                            <td class=<#if (urgencyByGroup.minUrgency?number > 50)>"table-danger"<#elseif (urgencyByGroup.minUrgency?number > 20 && urgencyByGroup.minUrgency?number < 50)>"table-warning"<#elseif (urgencyByGroup.minUrgency?number < 20)>"table-primary"<#else>"table-light"</#if>>
                        ${urgencyByGroup.minUrgency}</td>

                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>
        </div>
        <div><h4>Нагрузка по РГ</h4></div>
        <div>
        <table class="table" id="rgtable">
            <thead>
            <tr>
                <th scope="col">РГ</th>
                <th scope="col">Количество задач</th>
            </tr>
            </thead>
            <tbody>

                <#list countbyworkgroups?keys as key>
                <tr class=<#if (countbyworkgroups[key]?number > 50)>"table-danger"<#elseif (countbyworkgroups[key]?number > 20 && countbyworkgroups[key]?number < 50)>"table-warning"<#elseif (countbyworkgroups[key]?number < 20)>"table-primary"<#else>"table-light"</#if>>
                <td>${key}</td>
                <td>${countbyworkgroups[key]}</td>

                </tr>
                <#else>
                Пусто
                </#list>
            </tbody>
        </table>
        </div>
        <div>
        <div><h4>Нагрузка по сотрудникам</h4></div>
        <table class="table" id="exectable">
            <thead>
            <tr>
                <th scope="col">Исполнитель</th>
                <th scope="col">Количество задач</th>
            </tr>
            </thead>
            <tbody>
                <#list countbyexecutors?keys as key>
                <tr class=<#if (countbyexecutors[key]?number > 0)>"table-danger"<#else>"table-primary"</#if>>
                    <td>${key}</td>
                    <td>${countbyexecutors[key]}</td>

                </tr>
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
<br>


</@c.page>