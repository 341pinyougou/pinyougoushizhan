//服务层
app.service('orderService',function($http){

	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../order/search.do?page='+page+"&rows="+rows, searchEntity);
	}    	
});
