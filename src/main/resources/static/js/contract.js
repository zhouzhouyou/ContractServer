$(function () {
    let defaultContractTable = document.getElementById('default-contract-table');
    console.log(defaultContractTable);

    if (defaultContractTable !== null) {
        console.log('loading contract table');
        defaultContractTable.innerHTML = `<table class="table">
        <thead>
        <tr>
            <th scope="col">合同编号</th>
            <th scope="col">合同名称</th>
            <th scope="col">客户编号</th>
            <th scope="col">开始时间</th>
            <th scope="col">结束时间</th>
            <th scope="col">起草人</th>
        </tr>
        </thead>
        <tbody id="contractTableBody">
        </tbody>
    </table>`;
    }

    let defaultContractModalForm = document.getElementById("default-contract-modal-form");
    console.log(defaultContractModalForm);
    if (defaultContractModalForm !== null) {
        console.log('loading contract modal form');
        defaultContractModalForm.innerHTML =
            `<div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-num" style="display: inline">合同编号</label>
                <div class="col-sm-8">
                    <input class="form-control" id="contract-num" readonly/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-name" style="display: inline">合同名称</label>
                <div class="col-sm-8">
                    <input class="form-control" id="contract-name" readonly/>
                 </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-customerNum" style="display: inline">客户编号</label>
                <div class="col-sm-8">
                    <input class="form-control" id="contract-customerNum" readonly/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-begin" style="display: inline">开始时间</label>
                <div class="col-sm-8"><input class="form-control" id="contract-begin" readonly/></div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-end" style="display: inline">结束时间</label>
                <div class="col-sm-8"><input class="form-control" id="contract-end" readonly/></div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-content" style="display: inline">合同内容</label>
                <div class="col-sm-8"><textarea class="form-control" id="contract-content" readonly rows="5"></textarea></div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label" for="contract-username" style="display: inline">操作员</label>
                <div class="col-sm-8"><input class="form-control" id="contract-username" readonly/></div>
            </div>`;
    }

    let accordionContainer = document.getElementById("accordionContainer");
    if (accordionContainer !== null) {
        accordionContainer.innerHTML = `<div class="accordion" id="accordion"></div>`;
    }

});

function loadNewTable(url, trClick) {
    $.ajax({
        type: 'POST',
        url: url,
        headers: {'Content-Type': 'application/json;charset=utf8', 'token': getToken()},
        statusCode: {
            200: (result) => refreshContractTable(result, trClick)
        },
        error: () => alert("未连接到网络，或者无权限")
    });
}

function refreshContractTable(resultList, trClick) {
    contractTableBody.empty();
    resultList.forEach((contract) => {
        let tr = $(`<tr></tr>`);
        loadContractTableWithoutContent(contract, tr);
        tr.click(function () {
            trClick(contract['num']);
        });
        contractTableBody.append(tr);
    })
}

function loadContractTableWithoutContent(contract, tr) {
    let keys = Object.keys(contract);
    for (let i = 0; i < keys.length; i++) {
        let key = keys[i];
        if (key === 'content') continue;
        tr.append($(`<td>${contract[key]}</td>`));
    }
}

function loadDetailContractInfo(contract) {
    $("#contract-num").val(contract['num']);
    $("#contract-name").val(contract['name']);
    $("#contract-customerNum").val(contract['customerNum']);
    $("#contract-begin").val(contract['begin']);
    $("#contract-end").val(contract['end']);
    $("#contract-content").val(contract['content']);
    $("#contract-username").val(contract['userName']);
}

function createCard(operator, message) {
    let cardHeaderName = `${'card_' + operator}`;
    let cardInfoName = `${'card_info_' + operator}`;
    return $(`<div class="card form-group">
                            <div class="card-header" id=${cardHeaderName}>
                                <h2 class="mb-0">
                                    <button aria-controls=${cardInfoName} aria-expanded="true"
                                            class="btn btn-link"
                                            data-target=${'#card_info_' + operator} data-toggle="collapse"
                                            type="button">
                                        ${operator}
                                    </button>
                                </h2>
                            </div>
                            <div aria-labelledby=${cardHeaderName} class="collapse show"
                                 data-parent="#accordion"
                                 id=${cardInfoName}
                                <div class="card-body">
                                    ${message}
                                </div>
                            </div>
                        </div>`);
}