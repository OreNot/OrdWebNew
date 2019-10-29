<#macro login path isRegisterForm>
    <#include "security.ftl">

  <form action="${urlprefixPath}${path}" method="post">
       <!--<form action="${path}" method="post">-->
    <div class="form-group row mt-3">
        <label class="col-sm-2 col-form-label"> Логин : </label>
        <div class="col-sm-3">
        <input type="text" name="username" class="form-control" placeholder="логин"/>
        </div>
    </div>
    <div class="form-group row">
        <label  class="col-sm-2 col-form-label"> Пароль:  </label>
        <div class="col-sm-3">
        <input type="password" name="password" class="form-control" placeholder="пароль"/>
        </div>
    </div>
    <#if isRegisterForm>
        <div class="form-group row mt-3">
            <label class="col-sm-2 col-form-label"> Ф.И.О : </label>
            <div class="col-sm-3">
                <input type="text" name="fio" class="form-control" placeholder="Ф.И.О"/>
            </div>
        </div>
        <div class="form-group row">
            <label  class="col-sm-2 col-form-label"> E-mail:  </label>
            <div class="col-sm-3">
                <input type="text" name="email" class="form-control" placeholder="E-mail"/>
            </div>
        </div>
    </#if>

     <div class="form-group row">
         <div class="col-sm-3">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
             <!--<#if !isRegisterForm><a href="$prefix$/registration">Регистрация</a> </#if> -->
    <#if !isRegisterForm><a href="${urlprefixPath}/registration">Регистрация</a> </#if>


    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Зарегистрироваться<#else>Войти</#if></button>
         </div>
     </div>
</form>
</#macro>

<#macro logout>
<form action="${urlprefixPath}/logout" method="post">
    <button  class="btn btn-primary" type="submit">Выход</button>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
</form>
</#macro>