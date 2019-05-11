app.controller("itemCatController",function($scope,itemCatService){

    // 根据父ID查询分类
    $scope.findItemCatList =function(parentId){
    	//alert(parentId);
        itemCatService.findItemCatList(parentId).success(
        	function(response){
            $scope.itemCatlist=response;
        });
    }

    // 根据父ID查询分类
    $scope.findItemCatList2 =function(parentId){
        //alert(parentId);
        itemCatService.findItemCatList2(parentId).success(
            function(response){
                $scope.itemCatlist2=response;
            });
    }
});