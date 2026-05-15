<?php require 'header.php'; ?>

<main class="page">
    <div class="block main-block">
        <div class="content">
            <div class="aside-help-btn">help?</div>
            <h1>Companies</h1>

            <div class="top-menu">
                <div class="button button-green pop-up-opener" data-pop-up-id="add-company">Create a New Company</div>
                <form class="search-form">
                    <input name="search" placeholder="Search" type="search"><input type="submit" class="button button-gray" value="Go!">
                </form>
            </div>
            <table class="common-table">
                <thead>
                <tr>
                    <th><span class="title">COMPANY NAME</span><div class="arrow"></div></th>
                    <th><span class="title">ACTIVE</span><div class="arrow"></div></th>
                    <th><span class="title">Users</span> <span class="description">(Active / Nonactive)</span><span class="arrow"></span></th>
                    <th><span class="title">Orders</span><div class="arrow"></div></th>
                    <th><span class="title">Created</span><div class="arrow"></div></th>
                    <th><span class="title">Action</span><div class="arrow"></div></th>
                </tr>
                </thead>
                <tbody>
                <tr class="active">
                    <td>So-Tracking</td>
                    <td>Yes</td>
                    <td>128 / 12</td>
                    <td>1203</td>
                    <td>12 / 11 / 2017</td>
                    <td class="edit"><img src="images/pencil.svg" alt="" class="pop-up-opener" data-pop-up-id="edit-company"></td>
                </tr>
                <tr class="active">
                    <td>Treasure Intellegens</td>
                    <td>Yes</td>
                    <td>8 / 15</td>
                    <td>1203</td>
                    <td>12 / 11 / 2017</td>
                    <td class="edit"><img src="images/pencil.svg" alt="" class="pop-up-opener" data-pop-up-id="edit-company"></td>
                </tr>
                <tr>
                    <td>So-Tracking</td>
                    <td>No</td>
                    <td>128 / 12</td>
                    <td>1203</td>
                    <td>12 / 11 / 2017</td>
                    <td class="edit"><img src="images/pencil.svg" alt="" class="pop-up-opener" data-pop-up-id="edit-company"></td>
                </tr>
                <tr>
                    <td>So-Tracking</td>
                    <td>No</td>
                    <td>128 / 12</td>
                    <td>1203</td>
                    <td>12 / 11 / 2017</td>
                    <td class="edit"><img src="images/pencil.svg" alt="" class="pop-up-opener" data-pop-up-id="edit-company"></td>
                </tr>
                <tr>
                    <td>So-Tracking</td>
                    <td>No</td>
                    <td>128 / 12</td>
                    <td>1203</td>
                    <td>12 / 11 / 2017</td>
                    <td class="edit"><img src="images/pencil.svg" alt="" class="pop-up-opener" data-pop-up-id="edit-company"></td>
                </tr>

                </tbody>
            </table>
            <div class="bottom-menu">
                <ul class="pagination">
                    <li class="current-page"><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                </ul>
                <div class="items-count-select-wrap">
                    <div class="title">Отображать на странице по:</div>
                    <select name="items-on-page" class="items-on-page">
                        <option value="5">5</option>
                        <option selected value="10">10</option>
                        <option value="15">15</option>
                        <option value="20">20</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
</main>

<?php require 'footer.php'; ?>