/* global $*/
document.getElementById("header").innerHTML = `<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery-3.4.1.min.js"></script>
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/js.cookie-2.2.1.min.js"></script>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="#">合同管理系统</a>
    <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler" id="navbar_toggler" data-target="#navbarSupportedContent" data-toggle="collapse" type="button"><span class="navbar-toggler-icon"></span></button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown" id="contractManagementNav">
                <a aria-expanded="false" aria-haspopup="true" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" id="contractDropdown" role="button">合同操作</a>
                <div aria-labelledby="contractDropdown" class="dropdown-menu">
                    <a class="dropdown-item" href="draft.html" id="draft_nav">起草合同 </a>
                    <a class="dropdown-item" href="finalize.html" id="finalize_nav">定稿合同 </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="countersign.html" id="counter_sign_nav">会签合同 </a>
                    <a class="dropdown-item" href="review.html" id="review_nav">审批合同 </a>
                    <a class="dropdown-item" href="sign.html" id="sign_nav">签订合同 </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="assign.html" id="assign_nav">分配合同 </a>
                </div>
            </li>
            <li class="nav-item" id="contractQueryNav"><a class="nav-link" href="contract.html">合同查询</a></li>
            <li class="nav-item dropdown" id="functionManagementNav">
                <a aria-expanded="false" aria-haspopup="true" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button">权限管理</a>
                <div aria-labelledby="navbarDropdown" class="dropdown-menu">
                    <a class="dropdown-item" href="#">用户管理</a>
                    <a class="dropdown-item" href="#">角色管理</a>
                </div>
            </li>
            <li class="nav-item" id="customerNav"><a class="nav-link" href="customer.html">客户管理</a></li>
            <li class="nav-item" id="logQueryNav"><a class="nav-link" href="log.html">日志查询</a></li>
            <li class="nav-item dropdown" id="functionManagementNav">
                <a aria-expanded="false" aria-haspopup="true" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button">个人信息</a>
                <div aria-labelledby="navbarDropdown" class="dropdown-menu">
                    <a class="dropdown-item" href="welcome.html">欢迎页面</a>
                    <a class="dropdown-item" href="resetPassword.html">重置密码</a>
                    <a class="dropdown-item" href="login.html">登录页面</a>
                    <a class="dropdown-item" href="signUp.html">注册页面</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
`;
// $("#header").load("header.html");

let contractDropdown;
let finalizeNav;
let counterSignNav;
let reviewNav;
let signNav;
let assignNav;

let contractManagementNav;
let contractQueryNav;
let customerNav;
let logQueryNav;
let functionManagementNav;


function addBadges(resultList) {
    console.log(resultList);

    if (resultList[0])addBadge(contractDropdown);
    if (resultList[1])addBadge(finalizeNav);
    if (resultList[2])addBadge(counterSignNav);
    if (resultList[3])addBadge(reviewNav);
    if (resultList[4])addBadge(signNav);
    if (resultList[5])addBadge(assignNav);
}

function checkMyJob() {
    $.ajax({
        type: "POST",
        url: "/api/contract/UnfinishedJobs/select",
        headers: {'Content-Type': 'application/json;charset=utf8', 'token': Cookies.get('token')},
        statusCode: {
            200: (resultList) => addBadges(resultList)
        },
        error: () => alert("未连接到网络，或者无权限")
    })
}

$(function () {
    contractDropdown = $("#contractDropdown");

    finalizeNav = $("#finalize_nav");
    counterSignNav = $("#counter_sign_nav");
    reviewNav = $("#review_nav");
    signNav = $("#sign_nav");
    assignNav = $("#assign_nav");

    contractManagementNav = $("#contractManagementNav");
    contractQueryNav = $("#contractQueryNav");
    customerNav = $("#customerNav");
    logQueryNav = $("#logQueryNav");
    functionManagementNav = $("#functionManagementNav");

    checkMyJob();
});

function addBadge(nav) {
    nav.children().remove("span");
    let badge = document.createElement("span");
    badge.className = "badge badge-pill badge-danger";
    badge.textContent = "!";
    nav.append(badge);
}

function changeActive(current) {
    current.addClass("active");
}
