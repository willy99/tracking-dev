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


        }());
    }



});