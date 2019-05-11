//首页控制器
app.controller('indexController',function($scope,loginService){
	//显示当前登陆人的名称
	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;//Map
					}
			);
	}

    //查询购物车列表
    $scope.findAllOrder=function(){
        loginService.findAllOrder().success(
            function(response){
            	alert("111");
                $scope.orderItemList=response;
            }
        );
    }

});