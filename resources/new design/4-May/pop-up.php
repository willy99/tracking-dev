<div class="pop-up" id="add-company-pop-up">
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
                    <div class="name">Custom select</div>
                    <select name="somenthig">
                        <option value="value-1">Value 1</option>
                        <option value="value-2">Value 2</option>
                        <option value="value-3">Value 3</option>
                    </select>
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
                <div class="row tree-row">
                    <div class="name">Permission:</div>
                    <ul class="tree">
                        <li class="tree-item">
                            <div class="sub-tree-title">Order Permissions</div>
                            <ul class="sub-tree">
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Show Orders</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Update Orders</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Delete Orders</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <div class="sub-tree-title">Details</div>
                                    <ul class="sub-tree">
                                        <li class="tree-item">
                                            <label>
                                                <input type="checkbox">
                                                <span class="checkbox"></span>
                                                <span class="param-name">Show Orders</span>
                                            </label>
                                        </li>
                                        <li class="tree-item">
                                            <label>
                                                <input type="checkbox">
                                                <span class="checkbox"></span>
                                                <span class="param-name">Update Orders</span>
                                            </label>
                                        </li>
                                    </ul>
                                </li>
                                <li class="tree-item">
                                    <div class="sub-tree-title">Comments</div>
                                    <ul class="sub-tree">
                                        <li class="tree-item">
                                            <label>
                                                <input type="checkbox">
                                                <span class="checkbox"></span>
                                                <span class="param-name">Create Comments</span>
                                            </label>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li class="tree-item">
                            <div class="sub-tree-title">Dictionaries</div>
                            <ul class="sub-tree">
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Show Dictionaries</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Update Dictionaries</span>
                                    </label>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>


            </div>
            <div class="pop-up-message"></div>
            <div class="buttons-wrap">
                <div class="button button-gray button-left cancel-btn">Exit without save</div>
                <button type="submit" class="button button-green button-right">Create a Company</button>
            </div>

        </form>
    </div>
</div>

<div class="pop-up" id="edit-company-pop-up">
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
                    <div class="name">Custom select</div>
                    <select name="somenthig">
                        <option value="value-1">Value 1</option>
                        <option value="value-2">Value 2</option>
                        <option value="value-3">Value 3</option>
                    </select>
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
                <div class="row tree-row">
                    <div class="name">Permission:</div>
                    <ul class="tree">
                        <li class="tree-item">
                            <div class="sub-tree-title">Order Permissions</div>
                            <ul class="sub-tree">
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Show Orders</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Update Orders</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Delete Orders</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <div class="sub-tree-title">Details</div>
                                    <ul class="sub-tree">
                                        <li class="tree-item">
                                            <label>
                                                <input type="checkbox">
                                                <span class="checkbox"></span>
                                                <span class="param-name">Show Orders</span>
                                            </label>
                                        </li>
                                        <li class="tree-item">
                                            <label>
                                                <input type="checkbox">
                                                <span class="checkbox"></span>
                                                <span class="param-name">Update Orders</span>
                                            </label>
                                        </li>
                                    </ul>
                                </li>
                                <li class="tree-item">
                                    <div class="sub-tree-title">Comments</div>
                                    <ul class="sub-tree">
                                        <li class="tree-item">
                                            <label>
                                                <input type="checkbox">
                                                <span class="checkbox"></span>
                                                <span class="param-name">Create Comments</span>
                                            </label>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li class="tree-item">
                            <div class="sub-tree-title">Dictionaries</div>
                            <ul class="sub-tree">
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Show Dictionaries</span>
                                    </label>
                                </li>
                                <li class="tree-item">
                                    <label>
                                        <input type="checkbox">
                                        <span class="checkbox"></span>
                                        <span class="param-name">Update Dictionaries</span>
                                    </label>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>

            </div>

            <div class="pop-up-message"></div>
            <div class="buttons-wrap">
                <div class="button button-red button-left delete-btn">Delete company</div>
                <button type="submit" class="button button-green button-right">Save Change</button>
            </div>

        </form>
    </div>
</div>