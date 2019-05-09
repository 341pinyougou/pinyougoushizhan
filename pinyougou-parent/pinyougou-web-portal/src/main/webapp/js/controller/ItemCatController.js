app.controller("itemCatController",function($scope,itemCatService){

    // 根据父ID查询分类
    $scope.findItemCatList =function(parentId){
    	//alert(parentId);
        itemCatService.findItemCatList(parentId).success(
        	function(response){
            $scope.itemCatlist=response;
        });
    }
});