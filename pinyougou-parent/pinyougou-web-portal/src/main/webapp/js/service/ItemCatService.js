app.service("itemCatService",function($http){

	this.findItemCatList = function(parentId){
		//alert(parentId);
		return $http.get("itemCat/findItemCatList.do?parentId="+parentId);
	}
});