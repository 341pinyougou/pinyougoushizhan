app.service("itemCatService",function($http){

	this.findItemCatList = function(parentId){
		//alert(parentId);
		return $http.get("itemCat/findItemCatList.do?parentId="+parentId);
	}

    this.findItemCatList2 = function(parentId){
        //alert(parentId);
        return $http.get("itemCat/findItemCatList2.do?parentId="+parentId);
    }
});