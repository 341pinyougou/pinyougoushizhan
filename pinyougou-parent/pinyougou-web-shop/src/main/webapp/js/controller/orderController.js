 //控制层 
app.controller('orderController' ,function($scope,$controller,$location,orderService){
	
	$controller('baseController',{$scope:$scope});//继承

    $scope.searchEntity={};//定义搜索对象
	//搜索
	$scope.search=function(page,rows){			
		orderService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
     $scope.orderItemList={};
	//订单查询
    $scope.findOrderAndOrderItem=function(){
        orderService.findOrderAndOrderItem().success(
            function(response){
                $scope.list=response;

            }
        );
    }
    $scope.aaa=function (orderId) {
	orderService.aaa(orderId).success(
            function(response){
                if(response.flag){
                	alert(response.message)
				}else {
                	alert(response.message)
				}

            }
        );
    }
    
});	
