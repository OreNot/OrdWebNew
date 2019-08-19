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
                <textarea class="form-control" name="editabledescription" rows="5" style="min-width: 100%">${selecteddescription}</textarea>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">

             <input type="text" class="form-control" name="editableexecdate" placeholder="Дата выполнения" value="${selectedexecdate}"/>

            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">
                <select class="form-control" name="editableworkgroup"  placeholder="РГ">

                    <#list workgroups as workgroup>
                        <option value="${workgroup.name}" <#if workgroup.name == "${selectedworkgroup}">selected="selected"</#if>>${workgroup.name}</option>
                    </#list>

                </select>
            </div>
        </div>
        <input type="hidden" name="editabletid" value="${tid}">

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

    </form>
    </div>
</div>
<br>


</@c.page>