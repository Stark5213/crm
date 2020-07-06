layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 计划项数据展示
     */
    var  tableIns = table.render({
        elem: '#cusDevPlanList',
        url : ctx+'/cus_dev_plan/list?sid='+$("input[name='id']").val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    });


    /**
     * 工具栏监听事件
     */
    table.on('toolbar(cusDevPlans)', function (data) {
        var event = data.event;
        // 判断用户行为
        switch (event) {
            case "add":
                //  打开对话框
                openAddOrUpdateCusDevPlanDialog();
                break;
        }
    });


    /**
     * 行监听事件
     */
    table.on('tool(cusDevPlans)',function (data) {
        // 得到行事件
        var event = data.event;
        // 获取当前行的数据
        var rowData = data.data;

        if (event == "edit") {
            //  打开对话框
            openAddOrUpdateCusDevPlanDialog(rowData.id);
        }

    });


    function openAddOrUpdateCusDevPlanDialog(cusDevPlanId) {
        var title = "<h2>计划项管理 - 添加数据</h2>";
        var url = ctx + "/cus_dev_plan/addOrUpdateCusDevPlanPage?sid=" + $("input[name='id']").val();

        // 判断Id是否为空 不为空则为修改
        if (cusDevPlanId != null) {
            title = "<h2>计划项管理 - 更新数据</h2>";
            url += "&cId=" + cusDevPlanId;
        }

        layui.layer.open({
            title:title, // 标题
            type:2, // ifream层
            content: url, // 加载路径
            area:["500px","300px"], // 弹出层的大小
            maxmin:true // 是否可以最大化
        });
    }


});
