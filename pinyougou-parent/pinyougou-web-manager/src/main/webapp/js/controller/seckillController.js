 //控制层 
app.controller('seckillController' ,function($scope,$controller,$location,seckillService ){
	
	$controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findGoodsByGoodsId=function(){
    	 var id = $location.search()['id'];
        seckillService.findGoodsByGoodsId(id).success(
            function(response){
				$scope.entity = response;
            }
        );
    }

    //联动查询价格,库存
	$scope.findByitemId=function(){
    	seckillService.findByitemId($scope.entity.seckillGoods.itemId).success(
    		function (response) {
				$scope.item = response;
            }
		)
	}

    //提交秒杀商品
	$scope.save=function () {

        $scope.entity.item = $scope.item;
		seckillService.save($scope.entity).success(
			function (response) {
				alert(response.message)
                location.href="seckillfind.html";
            }
		)
    }

    //申请秒杀回显
	$scope.findSeckillGoodsList=function () {
		seckillService.findSeckillGoodsList().success(
			function (response) {
				$scope.list = response;
            }
		)
    }

    //查询全部秒杀商品
	$scope.findAllSeckillGoodsList=function () {
		seckillService.findAllSeckillGoodsList().success(
			function (response) {
				$scope.list = response;
            }
		)
    }


    // 审核的方法:
    $scope.updateStatus = function(status){
        seckillService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.findAllSeckillGoodsList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }

});	
