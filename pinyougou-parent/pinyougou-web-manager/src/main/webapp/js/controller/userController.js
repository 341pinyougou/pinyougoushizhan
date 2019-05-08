//用户控制层
app.controller('userController',function($scope,$controller,userService){

    $controller('baseController',{$scope:$scope});//继承

    //findAll
    $scope.findAll = function () {
        userService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }


    //分页
    $scope.findPage=function(page,rows){
        userService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }


    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(page,rows){
        userService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    // 会员来源
    $scope.sourceType = ["","PC","H5","Android","IOS","WeChat"];

    // 显示状态
    $scope.status = ["冻结","正常"];

    //修改用户状态(冻结解冻)
    $scope.updateStatus = function(status){
        userService.updateStatus($scope.selectIds,status).success(function(response){
           if(response.flag){
               $scope.reloadList();//刷新
               $scope.selectIds = [];
           }else {
               alert(response.message);
           }
        });
    }


})
