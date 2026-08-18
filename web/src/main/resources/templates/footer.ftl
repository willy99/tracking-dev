<div class="footer-wrap-app">
    <footer class="app-footer">
        <div class="app-footer__logo">
            <a href="#" class="logo app-footer__logo-img"></a>
        </div>
        <div class="app-footer__col">
            <div class="app-footer__label">Address</div>
            <div class="app-footer__text">65003, Ukraine, Odessa,<br>Chernomorskogo Kazachestva st, 115</div>
        </div>
        <div class="app-footer__col">
            <div class="app-footer__label">Contacts</div>
            <div class="app-footer__text">
                <a href="tel:+380487282588" class="app-footer__link">+38 (048) 728 25 88</a><br>
                <a href="tel:+380487289394" class="app-footer__link">+38 (048) 728 93 94</a><br>
                <a href="mailto:info@stoles.com.ua" class="app-footer__link">info@stoles.com.ua</a>
            </div>
        </div>
        <div class="app-footer__copy">
            &copy; 2005&ndash;2025 Stoles Logistic.<br>All rights reserved.
        </div>
    </footer>
</div>

<style>
    .footer-wrap-app {
        background-color: #384352;
        margin-top: 40px;
    }
    .app-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-wrap: wrap;
        gap: 24px;
        max-width: 1600px;
        margin: 0 auto;
        padding: 22px 50px;
    }
    .app-footer__logo-img {
        display: block;
        width: 160px;
        height: 36px;
        background: url("${contextPath}/images/page-index/logo.svg") no-repeat left center;
        background-size: contain;
        filter: brightness(0) invert(1);
        opacity: 0.85;
    }
    .app-footer__col {
        display: flex;
        flex-direction: column;
        gap: 4px;
    }
    .app-footer__label {
        font-size: 11px;
        font-weight: 700;
        letter-spacing: 1.2px;
        text-transform: uppercase;
        color: #00c0cd;
        margin-bottom: 2px;
    }
    .app-footer__text {
        font-size: 12px;
        color: #c5cad2;
        line-height: 1.7;
    }
    .app-footer__link {
        color: #c5cad2;
        text-decoration: none;
        transition: color .2s;
    }
    .app-footer__link:hover {
        color: #fff;
    }
    .app-footer__copy {
        font-size: 11px;
        color: #7c8798;
        line-height: 1.6;
        text-align: right;
    }
    @media (max-width: 768px) {
        .app-footer {
            flex-direction: column;
            align-items: flex-start;
            padding: 20px 24px;
        }
        .app-footer__copy { text-align: left; }
    }
</style>

</body>
</html>