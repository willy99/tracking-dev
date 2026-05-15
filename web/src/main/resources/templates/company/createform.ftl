<div id="company-container" class="span6">

    <div class="page-header text-center">
        <h2>Create a new Company</h2>
    </div>

    <!-- use ng-submit to catch the form submission and use our Angular function -->
    <form id="signup-form" ng-submit="processForm()">
        <!-- our nested state views will be injected here -->
        <div id="company-views" class="span6" ui-view></div>
    </form>

</div>

<!-- show our formData as it is being typed -->
<pre>
    {{ company }}
</pre>
<div class="page-header text-center">
    <div class="row">
        <div class="span1">
           <button ng-show="state>1" class="btn btn-info" ng-click="previousState()">Previous</button>
        </div>
        <div class="span1">
            <button ng-show="state<2" class="btn btn-info" ng-click="nextState()">Next</button>
        </div>
        <div class="span1">
            <button ng-show="state==2" class="btn btn-info" ng-click="save()">Save</button>
        </div>
        <div class="span1">
            <button class="btn btn-danger" ng-click="dismiss()">Cancel</button>
        </div>
    </div>
</div>
