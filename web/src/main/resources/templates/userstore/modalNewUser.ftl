<div class="pop-up" id="add-user-pop-up">
    <div class="window">
        <div class="close-btn"></div>
        <div class="pop-up-title">New User Creation</div>
        <form name="userForm" class="css-form">
            <div class="fieldset visible">
                <div class="row">
                    <div class="name">Activity:</div>
                    <label class="checkbox">
                        <input type="checkbox" name="activity" ng-model="user.active">
                        <div class="wrap"><div class="yes">Yes</div> <div class="switcher"></div> <div class="no">No</div></div>
                    </label>
                </div>

                <div class="row">
                    <div class="name">First Name:</div>
                    <input class="input" type="text" ng-model="user.firstName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Last Name:</div>
                    <input class="input" type="text" ng-model="user.lastName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Phone Num:</div>
                    <input class="input" type="tel" name="phone" ng-model="user.phone" ng-required="true" placeholder="+__ (___) ___-__-__">
                    <div class="hint" ng-show="userForm.phone.$error.pattern">
                        Not a valid number, Please enter the phone in format +__ (___) ___-__-__</div>
                </div>
                <div class="row">
                    <div class="name">Email:</div>
                    <input class="input" type="email" ng-model="user.email" ng-required="true">
                </div>

                <div class="row">
                    <div class="name">Role</div>
                    <div >
                        <select id="mainselection" name="role" ng-model="user.role" ng-options="role.roleName for role in roleList track by role.id"></select>
                    </div>
                </div>
                <div class="pop-up-message" id="pop-up-message"></div>

                <div class="buttons-wrap">
                    <div class="button button-gray button-left cancel-btn">Exit without save</div>
                    <button ng-click="saveUser()" class="button button-green button-right">Create a User</button>
                </div>

            </div>
        </form>
    </div>
</div>

<div class="pop-up" id="edit-user-pop-up">
    <div class="window">
        <div class="close-btn"></div>
        <div class="pop-up-title">Edit User</div>
        <form name="userForm" class="css-form">
            <div class="sub-title">User Info</div>
            <div class="fieldset visible">
                <div class="row">
                    <div class="name">Activity:</div>
                    <label class="checkbox">
                        <input type="checkbox" name="activity" ng-model="user.active">
                        <div class="wrap"><div class="yes">Yes</div> <div class="switcher"></div> <div class="no">No</div></div>
                    </label>
                </div>

                <div class="row">
                    <div class="name">First Name:</div>
                    <input class="input" type="text" ng-model="user.firstName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Last Name:</div>
                    <input class="input" type="text" ng-model="user.lastName" ng-required="true">
                </div>
                <div class="row">
                    <div class="name">Phone Num:</div>
                    <input class="input" type="tel" name="phone" ng-model="user.phone" ng-required="true" placeholder="+__ (___) ___-__-__">
                    <div class="hint" ng-show="userForm.phone.$error.pattern">
                        Not a valid number, Please enter the phone in format +__ (___) ___-__-__</div>
                </div>
                <div class="row">
                    <div class="name">Email:</div>
                    <input class="input" type="email" ng-model="user.email" ng-required="true">
                </div>

                <div class="row">
                    <div class="name">Role</div>
                    <div>
                        <select id="mainselection" name="role" ng-model="user.role" ng-options="role.roleName for role in roleList track by role.id"></select>
                    </div>
                </div>

                <div class="row">
                    <div class="name">Password:</div>
                    <input class="input" type="password" ng-model="user.password">
                </div>

                <div class="pop-up-message" id="pop-up-message"></div>

                <div class="buttons-wrap">
                    <!--div class="button button-red button-left delete-btn" ng-click="deleteUser()">Delete user</div-->
                    <button ng-click="saveUser()" class="button button-green button-right">Save Changes</button>
                </div>

            </div>

        </form>
    </div>

</div>

