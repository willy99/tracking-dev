// copied from https://developer.mozilla.org/ru/docs/Web/API/Element/closest
(function(ELEMENT) {
    ELEMENT.matches = ELEMENT.matches || ELEMENT.mozMatchesSelector || ELEMENT.msMatchesSelector || ELEMENT.oMatchesSelector || ELEMENT.webkitMatchesSelector;
    ELEMENT.closest = ELEMENT.closest || function closest(selector) {
        if (!this) return null;
        if (this.matches(selector)) return this;
        if (!this.parentElement) {return null}
        else return this.parentElement.closest(selector)
    };
}(Element.prototype));

document.addEventListener('DOMContentLoaded', function () {

    /* ПРОВЕРКА ПОЛЕЙ ВВОДА: START */
    var $inputTypeTel = $('input[type="tel"]');
    $inputTypeTel.mask('+99 (999) 999-99-99');// форматирование ввода
    $inputTypeTel.on('blur', function (e) {
        if(this.value.length === 0){
            this.classList.add('invalid');
        }else{
            this.classList.remove('invalid');
        }
    });

    $('input[type="email"]').on('blur', function (e) {
        if (!this.validity.valid || this.value === ''){
            this.classList.add('invalid');
        }else{
            this.classList.remove('invalid');
        }
    });
    /* ПРОВЕРКА ПОЛЕЙ ВВОДА: END */

    // попапы
    if(document.querySelector('.pop-up')){
        (function () {
            var popUps = document.querySelectorAll('.pop-up');

            // открытие попапа
            Array.prototype.forEach.call(document.querySelectorAll('.pop-up-opener'), function (button) {
                button.addEventListener('click', function () {
                    document.getElementById(button.dataset.popUpId).classList.add('active');
                    document.body.classList.add('fixed');
                });
            });

            // закрытие попапа
            function closePopUp() {
                this.classList.remove('active');
                document.body.classList.remove('fixed');
            }
            Array.prototype.forEach.call(popUps, function (popUp) {
                popUp.addEventListener('click', closePopUp);
                popUp.querySelector('.close-btn').addEventListener('click', function () {
                    closePopUp.call(this.closest('.pop-up'));
                });
                var cancelBtn = popUp.querySelector('.cancel-btn');
                if(cancelBtn){
                    cancelBtn.addEventListener('click', function () {
                        closePopUp.call(this.closest('.pop-up'));
                    });
                }
                popUp.querySelector('.window').addEventListener('click', function (e) {
                    e.stopPropagation();
                });
            });

            // сворачивание fieldset
            Array.prototype.forEach.call(document.querySelectorAll('.pop-up .fieldset'), function (fieldset) {
                var subtitle = fieldset.querySelector('.sub-title');
                if(subtitle){
                    subtitle.addEventListener('click', function () {
                        fieldset.classList.toggle('hidden');
                    })
                }
            });


            // сообщение в попапе
            function showPopUpMessage(popUp, messageText, status, timeout) {/* status = success || error */
                if(typeof popUp === 'string'){
                    popUp = document.querySelector(popUp);
                }
                timeout = timeout ? timeout : 2000;
                var messageElement = popUp.querySelector('.pop-up-message');
                messageElement.textContent = messageText;
                messageElement.classList.add(status);
                messageElement.classList.add('active');
                messageElement.style.height = messageElement.scrollHeight + 'px';
                setTimeout(function () {
                    messageElement.classList.remove('active');
                    messageElement.style = '';
                    setTimeout(function () {
                        messageElement.classList.remove(status);
                        messageElement.textContent = '';
                    }, 300);
                }, timeout);
            }
            //showPopUpMessage('#edit-company-pop-up', 'Houston, we got a problem...', 'error');
            //showPopUpMessage('#add-company-pop-up', 'Поздравляем, можете спать спокойно!', 'success');
        }());
    }
});