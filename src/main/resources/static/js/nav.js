/* global $*/
document.getElementById("header").innerHTML = `<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery-3.4.1.min.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="#">合同管理系统</a>
    <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler" id="navbar_toggler" data-target="#navbarSupportedContent" data-toggle="collapse" type="button"><span class="navbar-toggler-icon"></span></button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown active">
                <a aria-expanded="false" aria-haspopup="true" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" id="contractDropdown" role="button">合同操作</a>
                <div aria-labelledby="contractDropdown" class="dropdown-menu">
                    <a class="dropdown-item" href="draft.html" id="draft_nav">起草合同 </a>
                    <a class="dropdown-item" href="#" id="finalize_nav">定稿合同 </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="#" id="counter_sign_nav">会签合同 </a>
                    <a class="dropdown-item" href="#" id="review_nav">审批合同 </a>
                    <a class="dropdown-item" href="#" id="sign_nav">签订合同 </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="#" id="assign_nav">分配合同 </a>
                </div>
            </li>
            <li class="nav-item"><a class="nav-link" href="contract.html">合同查询</a></li>
            <li class="nav-item dropdown">
                <a aria-expanded="false" aria-haspopup="true" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" id="navbarDropdown" role="button">权限管理</a>
                <div aria-labelledby="navbarDropdown" class="dropdown-menu">
                    <a class="dropdown-item" href="#">用户管理</a>
                    <a class="dropdown-item" href="#">角色管理</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a aria-expanded="false" aria-haspopup="true" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" id="userDropdown" role="button">个人资料<span class="sr-only">(current)</span></a>
                <div aria-labelledby="userDropdown" class="dropdown-menu">
                    <a class="dropdown-item" href="draft.html">重置密码</a>
                    <a class="dropdown-item" href="#">创建账户</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="login.html">更换账户</a>
                    <a class="dropdown-item" href="#">注销登录</a>
                </div>
            </li>
        </ul>
    </div>
</nav>

`;
// $("#header").load("header.html");

$(function () {
    let contractDropdown = $("#contractDropdown");
    let draftNav = $("#draft_nav");
    let finalizeNav = $("#finalize_nav");
    let counterSignNav = $("#counter_sign_nav");
    let reviewNav = $("#review_nav");
    let signNav = $("#sign_nav");
    let assignNav = $("#assign_nav");
    function addBadges(resultList) {
        console.log(resultList);
        if (resultList[0])addBadge(contractDropdown);
        if (resultList[1])addBadge(finalizeNav);
        if (resultList[2])addBadge(counterSignNav);
        if (resultList[3])addBadge(reviewNav);
        if (resultList[4])addBadge(signNav);
        if (resultList[5])addBadge(assignNav);
    }
    $.ajax({
        type: "POST",
        url: "/api/contract/UnfinishedJobs/select",
        headers: {'Content-Type': 'application/json;charset=utf8', 'token': getToken()},
        statusCode: {
            200: (resultList) => addBadges(resultList)
        },
        error: () => alert("未连接到网络，或者无权限")
    })
});

function addBadge(nav) {
    let badge = document.createElement("span");
    badge.className = "badge badge-pill badge-danger";
    badge.textContent = "!";
    nav.append(badge);
}

