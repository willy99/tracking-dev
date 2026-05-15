<div class="pop-up" id="add-company-pop-up">
    <div class="window">
        <div class="close-btn"></div>
        <div class="pop-up-title">New Company Creation</div>
        <form name="companyForm" class="css-form">
            <div class="row">
                <div class="name">Name:</div>
                <input class="input" type="text" placeholder="New Company Name" ng-model="company.name" ng-required="true">
            </div>
            <div class="row">
                <div class="name">Activity:</div>
                <label class="checkbox">
                    <input type="checkbox" ng-model="company.active">
                    <div class="wrap"><div class="yes">Yes</div> <div class="switcher"></div> <div class="no">No</div></div>
                </label>
            </div>

            <div class="fieldset visible">
                <div class="sub-title">Company Admin User Info</div>
                <div class="row">
                    <div class="name">First Name:</div>
                    <input class="input" type="text" ng-model="company.admin.firstName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Last Name:</div>
                    <input class="input" type="text" ng-model="company.admin.lastName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Phone Num:</div>
                    <input class="input" type="tel" name="phone" ng-model="company.admin.phone" ng-required="true" placeholder="+__ (___) ___-__-__">
                    <div class="hint" ng-show="companyForm.phone.$error.pattern">
                        Not a valid number, Please enter the phone in format +__ (___) ___-__-__</div>
                </div>
                <div class="row">
                    <div class="name">Email:</div>
                    <input class="input" type="email" ng-model="company.admin.email" ng-required="true">
                </div>
            </div>
            <div class="pop-up-message" id="pop-up-message"></div>

            <div class="buttons-wrap">
                <div class="button button-gray button-left cancel-btn">Exit without save</div>
                <button ng-click="saveCompany()" class="button button-green button-right">Create a Company</button>
            </div>

        </form>
    </div>
</div>

<div class="pop-up" id="edit-company-pop-up">
    <div class="window">
        <div class="close-btn"></div>
        <div class="pop-up-title">Edit Company</div>
        <form name="companyForm" class="css-form">
            <div class="row">
                <div class="name">Name:</div>
                <input class="input" type="text" name="company-name" placeholder="Company Name" ng-model="company.name" ng-required="true">
            </div>
            <div class="row">
                <div class="name">Activity:</div>
                <label class="checkbox">
                    <input type="checkbox" name="activity" ng-model="company.active">
                    <div class="wrap"><div class="yes">Yes</div> <div class="switcher"></div> <div class="no">No</div></div>
                </label>
            </div>

            <div class="fieldset hidden">
                <div class="sub-title">Company Admin User Info</div>
                <div class="row">
                    <div class="name">First Name:</div>
                    <input class="input" type="text" ng-model="company.admin.firstName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Last Name:</div>
                    <input class="input" type="text" ng-model="company.admin.lastName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Phone Num:</div>
                    <input class="input" type="tel" name="phone" ng-model="company.admin.phone" ng-required="true" placeholder="+__ (___) ___-__-__">
                    <div class="hint" ng-show="companyForm.phone.$error.pattern">
                        Not a valid number, Please enter the phone in format +__ (___) ___-__-__</div>
                </div>
                <div class="row">
                    <div class="name">Email:</div>
                    <input class="input" type="email" ng-model="company.admin.email" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Password:</div>
                    <input class="input" type="password" ng-model="company.admin.password">
                </div>

            </div>
            <div class="pop-up-message" id="pop-up-message"></div>

            <div class="buttons-wrap">
                <!--div class="button button-red button-left delete-btn" ng-click="deleteCompany()">Delete company</div-->
                <button ng-click="saveCompany()" class="button button-green button-right">Save Changes</button>
            </div>


        </form>
    </div>

</div>

