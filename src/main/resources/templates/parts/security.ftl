<#assign
know = Session.SPRING_SECURITY_CONTEXT??
>

<#if know>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    isOperator = user.isOperator()
    isManager = user.isManager()
    isGroupBoss = user.isGroupBoss()
    isSuperBoss = user.isSuperBoss()
    isUser = user.isUser()

    prefix="/OrdWebNew"
    >
<#else>
<#assign
name = "Пользователь"
isAdmin = false
isOperator = false
isManager = false
isGroupBoss = false
isSuperBoss = false
isUser = false
prefix="/OrdWebNew"
>
</#if>