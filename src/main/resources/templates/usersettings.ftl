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
        <select class="form-control" name="username"  placeholder="Имя пользователя">

            <#list userlist as user>
                <option value="${user.username}">${user.username}</option>
            </#list>

        </select>
        </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-9">
                    <input type="text" class="form-control" name="fio" placeholder="ФИО"/>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">
                <select class="form-control" name="userrole"  placeholder="Роль пользователя">

                    <option value="USER">USER</option>
                    <option value="OPERATOR">OPERATOR</option>
                    <option value="GROUPBOSS">GROUPBOSS</option>
                    <option value="MANAGER">MANAGER</option>
                    <option value="SUPERBOSS">SUPERBOSS</option>


                </select>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-auto">
                <select class="form-control" name="workgroup"  placeholder="РГ">
                    <option value="NONE">NONE</option>
                    <#list workgroupslist as workgroup>
                        <option value="${workgroup.name}">${workgroup.name}</option>
                    </#list>

                </select>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-3">
    <button type="submit" class="btn btn-primary mb-2">Назначить</button>

            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}">

    </form>
    </div>
</div>
<br>


</@c.page>