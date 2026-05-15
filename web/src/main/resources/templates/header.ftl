<html <#if angular??>ng-app="app"</#if>>
<head>
    <title>${page_title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta charset="UTF-8">

    <!-- ================================================================== -->
    <!-- Bootstrap -->
    <link href="${contextPath}/css/bundle.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/ng-table.min.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/dt_bootstrap.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/app.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/angular-ui-tree.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/paging.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/web-style.css" rel="stylesheet" media="screen">
    <!--link href="${contextPath}/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/datepicker.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/dt_bootstrap.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/fullcalendar.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/google-maps.css" rel="stylesheet" media="screen">
    <link href="${contextPath}/css/app.css" rel="stylesheet" media="screen"-->


    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" />
    <style>
        .header-wrap header .logo {
            display: block;
            width: 220px;  /* Ширина логотипу для хедера (можете підрегулювати) */
            height: 50px;  /* Висота, щоб акуратно стати у хедер */
            margin: 0;
            /* Підтягуємо новий SVG замість старого PNG */
            background: url("${contextPath}/images/page-index/logo.svg") no-repeat left center;
            background-size: contain; /* Важливо: масштабує логотип пропорційно, щоб він не обрізався */
            transition: opacity 0.3s ease;
        }

        .header-wrap header .logo:hover {
            opacity: 0.8; /* Легкий ефект при наведенні */
        }

        /* Адаптивність: зменшуємо логотип на невеликих екранах */
        @media (max-width: 1550px) {
            .header-wrap header .logo {
                width: 160px;
                height: 40px;
                margin-bottom: 0;
                background-image: url("${contextPath}/images/page-index/logo.svg");
            }
        }
    </style>
    <!-- ================================================================== -->
    <!-- jquery -->
    <script src="${contextPath}/js/jquery/jquery-3.3.1.min.js"></script>
    <script src="${contextPath}/js/jquery/jquery-ui.min.js"></script>
    <script src="${contextPath}/js/jquery/jquery.printElement.min.js"></script>
    <script src="${contextPath}/js/jquery/jquery.maskedinput.min.js"></script>

    <!-- data table -->
    <script src="${contextPath}/js/dataTable/jquery.dataTables.min.js"></script>
    <script src="${contextPath}/js/dataTable/dt_bootstrap.js"></script>
    <!-- bootstrap -->
    <script src="${contextPath}/js/bootstrap/bootstrap.min.js"></script>
    <script src="${contextPath}/js/bootstrap/bootstrap-datepicker.js"></script>
    <!-- other -->
    <script src="${contextPath}/js/support.js"></script>
    <script src="${contextPath}/js/app.js"></script>
    <script src="${contextPath}/js/3d/three.js"></script>
    <script src="${contextPath}/js/3d/ThreeCSG.js"></script>
    <script src="${contextPath}/js/3d/controls/OrbitControls.js"></script>
    <script src="${contextPath}/js/3d/ColladaLoader.js"></script>

    <!-- Firebase App (the core Firebase SDK) is always required and must be listed first -->
    <script src="https://www.gstatic.com/firebasejs/6.0.2/firebase-app.js"></script>

    <!-- Add Firebase products that you want to use -->
    <script src="https://www.gstatic.com/firebasejs/6.0.2/firebase-auth.js"></script>
    <script src="https://www.gstatic.com/firebasejs/6.0.2/firebase-firestore.js"></script>
    <script src="https://www.gstatic.com/firebasejs/6.0.2/firebase-database.js"></script>



    <!-- angular -->
<#if angular??>
    <script src="${contextPath}/js/angular/angular-ui/lodash.js"></script>
    <script src="${contextPath}/js/angular/angular.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-strap.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-strap.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-strap.tpl.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/ui-bootstrap-tpls-0.11.0.min.js"></script>
    <script src="${contextPath}/js/fullcalendar.min.js"></script>
    <script src="${contextPath}/js/gcal.js"></script>
    <script src="${contextPath}/js/moment.js"></script>
    <script src="${contextPath}/js/moment-timezone.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/calendar.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/ng-table.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-simple-logger.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-google-maps.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-google-maps-tools.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angularLocalStorage.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-modal-service.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-ui-router.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/angular-animate.min.js"></script>

     <!-- AngularFire -->
    <script src="https://cdn.firebase.com/libs/angularfire/2.3.0/angularfire.min.js"></script>
    <script src="${contextPath}/js/angular/angular-ui/directives.js"></script>


</#if>

    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-153753645-1"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'UA-153753645-1');
    </script>


<#assign help_button = false>
</head>
<body>

<#if locale??>
    <div data-ng-controller="mainController" data-ng-init="setLocale('${locale}')"></div>
<#else>
    <div ng-controller="mainController"></div>
</#if>

<div class="header-wrap">
    <header>
        <a href="#" class="logo"></a>
        <ul class="menu">

            <#if shiro.principal??>


                <#if shiro.isPermitted("COMPANIES_MAINTAIN_CRUD")>
                    <li <#if top_nav_selected == 'companyManagement'>class="current-page"</#if>><a href="${contextPath}/tmw/company/companyManagement">Company Management</a></li>
                </#if>

                <li class="has-sub-menu">
                    <a data-toggle="dropdown" class="dropdown-toggle">Dictionary</a>
                    <#if shiro.isPermitted("DICTIONARY")>
                        <ul class="sub-menu">
                            <#if shiro.isPermitted("DICT_CONT_TYPES_UPDATE")><li><a href="${contextPath}/tmw/dict/containerTypes">Container Types</a></li></#if>
                            <#if shiro.isPermitted("DICT_DRIVERS_UPDATE")><li><a href="${contextPath}/tmw/dict/drivers">Drivers</a></li></#if>
                            <#if shiro.isPermitted("DICT_CONT_LINES_SHOW")><li><a href="${contextPath}/tmw/dict/lines">Tracking Lines</a></li></#if>
                            <#if shiro.isPermitted("DICT_STATION_CODE_UPDATE")><li><a href="${contextPath}/tmw/dict/lines">Station Codes</a></li></#if>
                            <#if shiro.isPermitted("DICT_GOODS_UPDATE")><li><a href="${contextPath}/tmw/dict/lines">Goods Codes</a></li></#if>
                        </ul>

                    </#if>
                </li>
                <li class="has-sub-menu">
                    <a data-toggle="dropdown" class="dropdown-toggle">Service</a>
                    <ul class="sub-menu">
                        <li><a href="${contextPath}/tmw/calculator/containercalc">Container Calculator</a></li>
                        <#if shiro.isPermitted("ORDER_SHOW")>
                            <li <#if top_nav_selected == 'trackingOrdersManagement'>class="current-page"</#if>><a href="${contextPath}/tmw/order/">Order</a></li>
                        </#if>
                        <#if shiro.isPermitted("LOGISTIC")>
                            <li <#if top_nav_selected == 'trackingOrdersManagement'>class="current-page"</#if>><a href="${contextPath}/tmw/flex/batchImportFlex/">Batch Import Flex</a></li>
                            <li <#if top_nav_selected == 'trackingOrdersManagement'>class="current-page"</#if>><a href="${contextPath}/tmw/flex/batchExportFlex/">Batch Export Flex</a></li>
                            <li <#if top_nav_selected == 'trackingOrdersManagement'>class="current-page"</#if>><a href="${contextPath}/tmw/flex/batchMountFlex/">Batch Mount Flex</a></li>
                        </#if>

                    </ul>
                </li>

                <#if shiro.isPermitted("COMPANY_MANAGEMENT")>
                    <li class="has-sub-menu settings">
                        <a data-toggle="dropdown" class="dropdown-toggle">System Tools</a>

                        <ul class="sub-menu">
                            <#if shiro.isPermitted("COMPANY_MANAGE_USERS")>
                                <li><a href="${contextPath}/tmw/userstore/userManagement">User management</a></li>
                            </#if>
                            <#if shiro.isPermitted("COMPANY_MANAGE_ROLES")>
                                <li><a href="${contextPath}/tmw/userstore/roleManagement">Role management</a></li>
                            </#if>
                        </ul>
                    </li>
                </#if>
                <li class="has-sub-menu user"><a href="#"><img src="${contextPath}/images/user-icon.png" alt="" class="icon">
                    Hi, <span class="name">${shiro.principal.firstName}</span></a>
                    <ul class="sub-menu">
                        <li><a href="${contextPath}/tmw/userstore/profile">Edit Profile</a></li>
                    </ul>
                </li>

                <li><a href="${contextPath}/tmw/logout">log out</a></li>
            <#else>
                <li><a href="${contextPath}/tmw/calculator/containercalc">Container Calculator</a></li>
                <li><a href="${contextPath}/tmw/tracking/trackContainer">Track Container</a></li>
                <li><a href="${contextPath}/tmw/anon/showpage?page=contact">Contact</a></li>
                <li><a href="${contextPath}/tmw/login">Login</a></li>
            </#if>

        </ul>
    </header>
</div>
