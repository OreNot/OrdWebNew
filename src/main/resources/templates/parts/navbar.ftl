<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">UC</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">


    <#if isOperator || isManager>
            <li class="nav-item">
                <a class="nav-link" href="/addtoarchive">Добавление в Архив</a>
            </li>
                <li class="nav-item">
                    <a class="nav-link" href="/showarchive">Просмотр Архива</a>
                </li>

                </#if>

            <#if isManager>
                <li class="nav-item">
                    <a class="nav-link" href="/addtask">Добавить задачу</a>
                </li>

                </#if>
<#if isManager || isSuperBoss>
                <li class="nav-item">
                    <a class="nav-link" href="/showtask">Задачи</a>
                </li>

        <#if isGroupBoss>


                <li class="nav-item">
                    <a class="nav-link" href="/myrgtasks">Задачи на мою РГ</a>
                </li>
        </#if>
</#if>

 <#if isAdmin>
    <li class="nav-item">
        <a class="nav-link" href="/usersettings">Настройки пользователей</a>
    </li>
 </#if>


        </ul>

        <div class="navbar-text mr-3">${name}</div>
    <@l.logout/>
    </div>
</nav>