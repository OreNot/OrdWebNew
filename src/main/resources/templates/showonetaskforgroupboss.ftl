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
    function toggleCheckbox(elem) {

        if(elem.checked)
        {
            document.getElementById('comment').hidden = false;
            document.getElementById('commentlabel').hidden = false;
        }
        else
        {
            document.getElementById('comment').hidden = true;
            document.getElementById('commentlabel').hidden = true;
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
<div class="form-row">
    <div class="form-group col-md-10">
    <form method="post" enctype="multipart/form-data" id="js-upload-form">

        <div><h4>Задача</h4></div>
        <div class="form-group row mt-3">
        <div class="col-auto">
        <select class="form-control" name="editableurgency"  placeholder="Важность">

            <#list urgencys as urgency>
                <option value="${urgency.name}" <#if urgency.name == "${selectedurgency}">selected="selected"</#if>>${urgency.name}</option>
            </#list>

        </select>
        </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-md-10">
                <label for="exampleFormControlTextarea1">Описание</label>
                <textarea class="form-control"  disabled name="editabledescription" rows="5" style="min-width: 100%">${selecteddescription}</textarea>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">

             <input type="text" class="form-control" name="editableexecdate" placeholder="Дата выполнения" value="${selectedexecdate}"/>

            </div>
        </div>


        <#if taskfilepath??>
        Вложение : ${taskFileName}
            <a class="btn btn-success mb-2" role="button" href="window['Ionic']['WebView'].convertFileSrc(${taskfilepath})"
               download="${taskFileName}">
                Посмотреть вложение
            </a>

            <div class="file-upload-wrapper mb-2">
                <input type="file"  name="file" id="input-file-now" class="file-upload"/>
                <label class="input-file-label" for="input-file-now">Изменить вложение</label>
            </div>
        <#else>
            <div class="file-upload-wrapper mb-2">
                <input type="file"  name="file" id="input-file-now" class="file-upload"/>
                <label class="input-file-label" for="input-file-now">Добавить вложение</label>
            </div>

        </#if>

        <div class="form-group row mt-3">

            <div class="col-auto">

                Назначить исполнителя<select class="form-control" name="executor"  placeholder="Исполнители">
                         <option value="Не назначен">Не назначен</option>
                    <#list executorlist as executor>
                        <option value="${executor.username}" <#if selectedexecutor??><#if executor.username == "${selectedexecutor}">selected="selected"</#if></#if>>${executor.username}</option>
                    </#list>

                </select>
            </div>
        </div>

        <input type="hidden" name="editabletid" value="${tid}">

        <div class="custom-control custom-checkbox">
            <input type="checkbox" class="custom-control-input" id="resend" name="resend" onchange="toggleCheckbox(this)">
            <label class="custom-control-label" for="resend">Вернуть на распределение</label>
        </div>

        <div class="form-group row mt-3">
            <div class="col-md-10">
                <label for="comment" hidden="true" id="commentlabel">Комментарий</label>
                <textarea class="form-control" hidden="true" name="comment" id="comment" rows="5" style="min-width: 100%" ></textarea>
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