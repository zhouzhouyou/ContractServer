$(function () {
    let defaultContractTable = document.getElementById('default-contract-table');
    console.log(defaultContractTable);
    if (defaultContractTable !== null) {
        console.log('loading contract table');
        document.getElementById('default-contract-table').innerHTML = `<table class="table">
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
        document.getElementById("default-contract-modal-form").innerHTML =
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

});

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