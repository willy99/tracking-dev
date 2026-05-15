<div class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" ng-click="close(false)" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Please select new user for {{roleInfo.role.roleName}} role  </h4>
            </div>

            <div class="modal-body">

                <div class="controls form-search well">
                    <input id="searchtext" type="text" class="span2 offset2" placeholder="Search for" ng-model="searchtext">
                    <button id="search" class="btn" type="button" ng-click="search()">Search</button>
                    <img id="loading" src="${contextPath}/img/ajax-loader.gif" alt="Loading..." ng-show="loading"/>
                </div>

                <table class="table table-bordered table-striped" ng-table="usersTableModal">
                    <tr ng-repeat="u in dataModal" ng-click="editUser(u)" ng-class="{active: u.id === idSelectedUser, deleted: u.active === false}">
                        <td data-title="'First Name'" sortable="'firstName'">
                            <span ng-bind-html="u.firstName | highlight:searchtext | transliterate | trusted"></span>
                        </td>
                        <td data-title="'Last Name'" sortable="'lastName'">
                            <span ng-bind-html="u.lastName | highlight:searchtext | transliterate | trusted"></span>
                        </td>
                        <td data-title="'Email'" sortable="'email'">
                            <span ng-bind-html="u.email | highlight:searchtext | trusted"></span>
                        </td>
                    </tr>
                </table>


            </div>
            <div class="modal-footer">
                <button type="button" ng-click="close()" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" ng-click="close(idSelectedUser)" class="btn btn-primary" data-dismiss="modal" >Add</button>
            </div>
        </div>
    </div>
</div>
