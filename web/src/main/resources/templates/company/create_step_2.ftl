<div class="span3">

    <div class="row">
        <div class="span3"><label>Create a Company Admin User</label></div>
    </div>

    <div class="row span3 controls" style="margin-left:0;margin-top:20px;">
        <div class="row">
            <label class="control-label span1 inline" for="firstName">First Name</label>
            <input id="firstName" type="text" class="span2 inline" placeholder="first name"
                   ng-model="company.user.firstName" required>
            <label class="control-label span1 inline" for="firstName">Last Name</label>
            <input id="lastName" type="text" class="span2 inline" placeholder="last name"
                   ng-model="company.user.lastName" required>
            <label class="control-label span1 inline" for="shortName">Email</label>
            <input id="shortName" type="text" class="span2 inline" placeholder="Email"
                   ng-model="company.user.email" required>
            <label class="control-label span1 inline" for="shortName">Phone</label>
            <input id="shortName" type="text" class="span2 inline" placeholder="Phone"
                   ng-model="company.user.phone" required value="+380">
            <label class="control-label span1 inline" for="shortName">Password</label>
            <input id="shortName" type="text" class="span2 inline" placeholder="Password"
                   ng-model="company.user.password" required>
        </div>
    </div>
</div>
