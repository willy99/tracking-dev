<#assign page_title = "Login">
<#assign top_nav_selected = "login">
<#include "header.ftl"/>

<div class="login-header-wrap">
    <header>
        <div class="left">
            <a href="#" class="logo"></a>
            <div class="slogan">ADVANCED MATHEMATICS OF LOGISTICS</div>
        </div>
        <#if help_button><a class="help" href="#">Help?</a></#if>
    </header>
</div>

<main id="page-login">
    <h1 class="page-title">
        Tracking<br>
        Maintenance<br>
        Tools
    </h1>
    <form action="${contextPath}/tmw/login" class="login-form" method="post">
        <div class="title">Enter</div>
        <div class="description">
            Leave questions to us, we will answer and consult in a best possible way
        </div>
        <input type="text" name="username" id="username"  required placeholder="Phone/Email" value="${username!"+380"}">
        <input type="password" name="password" required placeholder="Password">
        <#if error??><span id="loginErrorMessage" style="color: #d14;">Username or password is invalid</span></#if>
        <input class="login-btn" type="submit" value="LOGIN">
        <a class="btn-link" href="${contextPath}/tmw/recover">Forgot password</a>

    </form>
</main>


<!--fieldset>
    <legend>Login:</legend>
    <form id="showOrderInfo" action="${contextPath}/tmw/login" method="post">
        <div class="input-prepend input-append" style="position: relative;">
            <span class="add-on" style="height: 20px;width: 120px;">Email/Phone:</span>
            <input id="username" type="text" style="height: 30px;" name="username" value="${username!"+380"}"/>
        </div><br/>
        <div class="input-prepend input-append" style="position: relative;">
            <span class="add-on" style="height: 20px;width: 120px;">Password:</span>
            <input type="password" style="height: 30px;" name="password"/>
        </div><br/>
        <button type="submit" class="btn btn-info">Login</button>
        <#if error??><span id="loginErrorMessage" style="color: #d14;">Username or password is invalid</span></#if>
        <a class="btn-link" href="${contextPath}/tmw/recover">Forgot password</a>
    </form>
</fieldset-->
<script language="javascript">
    document.getElementById("username").focus();
</script>

<#include "footer.ftl"/>
