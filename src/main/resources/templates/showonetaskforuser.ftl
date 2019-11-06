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
    function toggleSelect(elem) {

        if(elem.value == "Выполнено")
        {
            document.getElementById('report').hidden = false;
            document.getElementById('reportlabel').hidden = false;
        }
        else
        {
            document.getElementById('report').hidden = true;
            document.getElementById('reportlabel').hidden = true;
        }
    }
</script>


<br>
<div>
    <#if error?has_content>
        ${error}
    </#if>
</div>
<br>

<div class="form-group row mt-3">
    <div class="col-sm-3">
        <button class="btn btn-primary mb-2" onclick="window.close()">Закрыть</button>

    </div>
</div>


<div class="form-row">
    <div class="form-group col-md-10">
    <form method="post" enctype="multipart/form-data" id="js-upload-form">

        <div><h4>Задача #<#if tid?has_content>${tid}</#if></h4></div>
        <div class="form-group row mt-3">
        <div class="col-auto">
            <span class="badge badge-pill <#if selectedurgency  =="Очень важно">badge-danger<#elseif selectedurgency  =="Важно">badge-warning<#elseif selectedurgency  =="Стандартно">badge-primary<#else>badge-light</#if>">${selectedurgency}</span>
        </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-md-10">
                <label for="exampleFormControlTextarea1">Описание</label>
                <textarea class="form-control"  disabled name="description" rows="5" style="min-width: 100%">${description}</textarea>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">

             <input type="text" class="form-control" name="execdate" placeholder="Дата выполнения" value="${execdate}"/>

            </div>
        </div>


        <#if taskfilepath??>
        Вложение : ${taskFileName}
            <a class="btn btn-success mb-2" role="button" href="window['Ionic']['WebView'].convertFileSrc(${taskfilepath})"
               download="${taskFileName}">
                Посмотреть вложение
            </a>

        </#if>

        <div class="form-group row mt-3">
            <div class="col-auto">
                <select class="form-control" name="status"  placeholder="Статус" onchange="toggleSelect(this)">

                 <option value="В работе у исполнителя">В работе у исполнителя</option>
                 <option value="Выполнено">Выполнено</option>

                </select>
            </div>
        </div>

        <input type="hidden" name="editabletid" value="${tid}">


        <div class="form-group row mt-3">
            <div class="col-md-10">
                <label for="reportlabel" hidden="true" id="reportlabel">Решение</label>
                <textarea class="form-control" hidden="true" name="report" id="report" rows="5" style="min-width: 100%" ></textarea>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-3">
    <button type="submit" class="btn btn-primary mb-2">Изменить задачу</button>

            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <button class="btn btn-primary mb-2" onclick="window.close()">Выйти без изменения</button>

            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}">

        <div class="form-group row mt-3">
            <div class="col-md-10">
                <label for="exampleFormControlTextarea1">Хронология</label>
                <textarea readonly class="form-control" name="сchronos" rows="5" style="min-width: 100%">${chronos}</textarea>
            </div>
        </div>

    </form>
    </div>
</div>

<br>


</@c.page>