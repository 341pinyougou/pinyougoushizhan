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
    
});	
