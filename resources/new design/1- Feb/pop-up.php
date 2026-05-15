<div class="pop-up" id="add-company">
    <div class="window">
        <div class="close-btn"></div>
        <div class="pop-up-title">New Company Creation</div>
        <form action="" method="post">
            <div class="row">
                <div class="name">Name:</div>
                <input class="input" type="text" name="company-name" placeholder="New Company Name">
            </div>
            <div class="row">
                <div class="name">Activity:</div>
                <label class="checkbox">
                    <input type="checkbox" name="activity">
                    <div class="wrap"><div class="yes">Yes</div> <div class="switcher"></div> <div class="no">No</div></div>
                </label>
            </div>

            <div class="fieldset">
                <div class="sub-title">Company Admin User Info</div>
                <div class="row">
                    <div class="name">First Name:</div>
                    <input class="input" type="text" name="user-first-name">
                </div>
                <div class="row">
                    <div class="name">Last Name:</div>
                    <input class="input" type="text" name="user-last-name">
                </div>
                <div class="row">
                    <div class="name">Phone Num:</div>
                    <input class="input" type="tel" name="tel">
                </div>
                <div class="row">
                    <div class="name">Email:</div>
                    <input class="input" type="email" name="email">
                </div>
                <div class="row">
                    <div class="name">Password:</div>
                    <input class="input" type="password" name="password">
                </div>
            </div>

            <div class="buttons-wrap">
                <div class="button button-gray button-left cancel-btn">Exit without save</div>
                <button type="submit" class="button button-green button-right">Create a Company</button>
            </div>

        </form>
    </div>
</div>

<div class="pop-up" id="edit-company">
    <div class="window">
        <div class="close-btn"></div>
        <div class="pop-up-title">Edit Company</div>
        <form action="" method="post">
            <div class="row">
                <div class="name">Name:</div>
                <input class="input" type="text" name="company-name" placeholder="Company Name">
            </div>
            <div class="row">
                <div class="name">Activity:</div>
                <label class="checkbox">
                    <input type="checkbox" name="activity">
                    <div class="wrap"><div class="yes">Yes</div> <div class="switcher"></div> <div class="no">No</div></div>
                </label>
            </div>

            <div class="fieldset hidden">
                <div class="sub-title">Company Admin User Info</div>
                <div class="row">
                    <div class="name">First Name:</div>
                    <input class="input" type="text" name="user-first-name">
                </div>
                <div class="row">
                    <div class="name">Last Name:</div>
                    <input class="input" type="text" name="user-last-name">
                </div>
                <div class="row">
                    <div class="name">Phone Num:</div>
                    <input class="input" type="tel" name="tel">
                </div>
                <div class="row">
                    <div class="name">Email:</div>
                    <input class="input" type="email" name="email">
                </div>
                <div class="row">
                    <div class="name">Password:</div>
                    <input class="input" type="password" name="password">
                </div>
            </div>

            <div class="buttons-wrap">
                <div class="button button-red button-left delete-btn">Delete company</div>
                <button type="submit" class="button button-green button-right">Save Change</button>
            </div>

        </form>
    </div>
</div>