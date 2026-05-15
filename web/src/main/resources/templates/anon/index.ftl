<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agrolog | Site under construction</title>
    <link rel="icon" type="image/png" href="image/favicon/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="image/favicon/favicon-16x16.png" sizes="16x16" />
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">

    <style>
        body,html {
            height: 100%;
            margin: 0;
            padding: 0;
            font-family: 'Open Sans', sans-serif;
            overflow: hidden;
        }
        main {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            height: 100%;
            background: url("${contextPath}/images/page-index/bg.jpg") repeat;

            /* Ховаємо на старті та запускаємо анімацію на 4 секунди */
            opacity: 0;
            animation: fullScreenFade 4s forwards;
        }
        .logo-bg {
            position: absolute;
            top: 60px;
            left:0;
            width: 1058px;
            height: 683px;
            background: url("${contextPath}/images/page-index//logo-bg.svg") no-repeat;
            background-size: contain;
        }
        .logo {
            width: 507px;
            height: 135px;
            margin-top: 250px;
            background: url("${contextPath}/images/page-index//logo.svg") no-repeat;
            background-size: contain;
        }
        .motto {
            margin: 30px 0 250px;
            font-size: 43px;
            text-align: right;
        }
        .copyright {
            font-size: 14px;
        }

        @media (max-width: 1700px) {
            .logo-bg {
                position: absolute;
                top: calc(60px + (60 - 100) * (100vw - 1700px) / (1700 - 320));
                width: calc(1058px + (1058 - 386) * (100vw - 1700px) / (1700 - 320));
                height: calc(683px + (683 - 249) * (100vw - 1700px) / (1700 - 320));
            }
            .logo {
                width: calc(507px + (507 - 204) * (100vw - 1700px) / (1700 - 320));
                height: calc(135px + (135 - 55) * (100vw - 1700px) / (1700 - 320));
                margin-top: calc(250px + (250 - 100) * (100vw - 1700px) / (1700 - 320));
            }
            .motto {
                margin: 30px 0 calc(250px + (250 - 100) * (100vw - 1700px) / (1700 - 320));
                font-size: calc(43px + (43 - 18) * (100vw - 1700px) / (1700 - 320));
                text-align: right;
            }
            .copyright {
                font-size: calc(14px + (14 - 12) * (100vw - 1700px) / (1700 - 320));
            }
        }

        @media (max-width: 720px) {
            main {
                background-size: contain;
            }
        }

        .content {
            margin: auto;
            color: #ffffff;
            text-align: center;
            /* Додаємо анімацію: триває 3 секунди і зупиняється в кінці (forwards) */
            animation: splashFade 3s forwards;
        }

        /* Анімуємо і фоновий логотип, щоб він теж згасав разом з текстом */
        .logo-bg {
            /* ... ваші існуючі стилі ... */
            animation: splashFade 3s forwards;
        }

        /* Сама логіка анімації */
        @keyframes fullScreenFade {
            0%   { opacity: 0; }
            12%  { opacity: 1; } /* Пройшла 1 сек: екран повністю з'явився */
            55%  { opacity: 1; } /* Пройшло ще 1 сек: тримаємо контент видимим */
            100% { opacity: 0; } /* Останні 2 сек: плавне розчинення до нуля */
        }
        @keyframes splashFade {
            0%   { opacity: 1; }
            33%  { opacity: 1; } /* 1 секунду тримаємо повністю видимим (33% від 3 секунд) */
            100% { opacity: 0; } /* Наступні 2 секунди плавно розчиняємо до 0 */
        }

    </style>
</head>
<body>

<main class="bg">
    <div class="content">
        <div class="logo"></div>
        <div class="motto">ADVANCED MATHEMATICS OF LOGISTICS</div>
        <div class="copyright">(С) 2026 Artinlog</div>
    </div>
    <div class="logo-bg"></div>
    <a href="menu">&gt;</a>
</main>
<script>
    // Чекаємо 4 секунди (1с поява + 1с пауза + 2с зникання) і переходимо
    setTimeout(function() {
        window.location.href = 'menu';
    }, 4000);
</script>
</body>
</html>
