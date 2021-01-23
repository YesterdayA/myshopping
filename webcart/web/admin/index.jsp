<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/13
  Time: 10:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="commons/header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12">
            <form class="form-inline" onclick="javascript:return false">
                <div class="form-group">
                    <label for="name">姓名</label>
                    <input type="text" id="name" class="form-control">
                </div>
                <div class="form-group">
                    <label for="sex">性别</label>
                    <select id="sex" class="form-group">
                        <option value="-1">全部</option>
                        <option value="M">男</option>
                        <option value="F">女</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">状态</label>
                    <select id="status" class="form-group">
                        <option value="-1">全部</option>
                        <option value="1">已激活</option>
                        <option value="0">未激活</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="email">邮箱</label>
                    <input type="text" id="email" class="form-control">
                </div>
                <div class="form-group">
                    <label>注册日期</label>
                    <input type="text" id="beginRegisterDate" class="form-control form-datetimepicker">-
                    <input type="text" id="endRegisterDate" class="form-control form-datetimepicker">
                </div>
                <div class="form-group">
                    <button class="btn btn-primary" onclick="ResetData()"><span class="fa fa-search"></span> 重置</button>
                    <button class="btn btn-primary" onclick="searchData()"><span class="fa fa-search"></span> 查询
                    </button>

                </div>
            </form>
        </div>
    </div>
</div>


<table id="userTableData" class="table table-hover table-bordered">

</table>

<%@ include file="commons/footer.jsp" %>
<div class="modal fade" data-backdrop="static" tabindex="-1" id="showAddress">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <button class="close" data-dismiss="modal">&times;</button>
                <h3 class="modal-title">收货地址</h3>
            </div>
            <div class="modal-body" id="addressList">

            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>

</div>
<script>
    $('#userTableData').bootstrapTable({
        url: '${pageContext.servletContext.contextPath}/user',  //请求地址
        columns: [
            /**
             * "email": "78@qq.com",
             "id": 2,
             "name": "李四",
             "phone": "15021100000",
             "registerDate": 1589358044000,
             "sex": "M",
             "status": 0
             *
             */
            {field: 'id', title: 'ID'},
            {field: 'name', title: '姓名'},
            {field: 'sex', title: '性别', formatter: formatSex},
            {field: 'email', title: '邮箱'},
            {field: 'phone', title: '电话'},
            {field: 'status', title: '状态', formatter: formatStatus},
            {field: 'registerDate', title: '注册日期', formatter: formatRegisterDate},
            {title: '操作', formatter: operationBtn}
        ],
        pagination: true,
        sidePagination: 'server',
        queryParams: function (params) {
            params.name = $('#name').val();
            params.sex = $('#sex').val();
            params.email = $('#email').val();
            params.status = $('#status').val();
            params.beginRegisterDate = $('#beginRegisterDate').val();
            params.endRegisterDate = $('#endRegisterDate').val();
            params.method = 'getUser'
            return params;
        }
    })

    function formatSex(value, row, index) {
        return value == 'F' ? '女' : '男';
    }

    function formatStatus(value, row, index) {
        if (value == 1) {
            return '<span class="label label-primary">已激活</span>'
        } else {
            return '<span class="label label-warning">未激活</span>'
        }
    }

    function formatRegisterDate(value, row, index) {
        return moment(value).format('YYYY-MM-DD HH:mm:ss')
    }

    function operationBtn(value, row) {
        //console.log(row)
        if (row.status == 1) {
            return '<button class="btn btn-info btn-xs" data-toggle="modal" data-target="#showAddress" onclick="showReceiptAddress(' + row.id + ')">查看收货地址</button>'
        } else {
            return '<button class="btn btn-info btn-xs" data-toggle="modal" data-target="#showAddress" onclick="showReceiptAddress(' + row.id + ')">查看收货地址</button> ' +
                '<button class="btn btn-success btn-xs" onclick="activeStatus(' + row.id + ')">激活</button>'
        }
    }

    function activeStatus(userId) {
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/user',
            data: {method: 'activeStatus', userId: userId},
            dataType: 'json',
            type: 'get',
            success: function (_data) {
                if (_data.code < 0) {
                    Lobibox.notify('info', {
                        title: '提示',
                        msg: '激活失败，请联系管理员！',
                        size: 'mini'
                    })
                } else {
                    $('#userTableData').bootstrapTable('refresh');
                }
            }
        })
    }

    //当点击按钮的时候，到数据库中拿数据
    function showReceiptAddress(userId) {
        //解决只能获取地址只能获取一次的问题，先销毁，再创建
        $('#addressList').bootstrapTable('destroy');
        $('#addressList').bootstrapTable({
            url: '${pageContext.servletContext.contextPath}/user',
            columns: [
                {field: 'name', title: '收件人姓名'},
                {field: 'phone', title: '收件人电话'},
                {field: 'detail', title: '详细地址'},
                {field: 'status', title: '默认地址'}
            ],
            pagination: true,
            sidePagination: 'server',
            queryParams: function (params) {
                //console.log(params);
                params.userId = userId;
                params.method = 'getAddress'
                return params;
            }
        });
    }

    //根据条件来查询数据 status email beginRegisterDate  endRegisterDate
    function searchData() {
        /*        $('#name').val();
                $('#sex').val();
                $('#email').val();
                $('#status').val();
                $('#beginRegisterDate').val();
                $('#endRegisterDate').val();*/
        $('#userTableData').bootstrapTable('refresh')
    }

    function ResetData() {
        $('#name').val('');
        $('#sex').val('-1');
        $('#email').val('');
        $('#status').val('-1');
        $('#beginRegisterDate').val('');
        $('#endRegisterDate').val('');
        $('#userTableData').bootstrapTable('refresh')
    }

    //DateTimePicker时间选择器
    $('.form-datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd hh:ii:ss',
        language: 'zh-CN',
        defaultDate: new Date(),
    })

</script>


