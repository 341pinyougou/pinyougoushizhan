//服务层
app.service('orderService',function($http){

	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../order/search.do?page='+page+"&rows="+rows, searchEntity);
	}
	//findOrderAndOrderItem
    this.findOrderAndOrderItem=function(){
        return $http.post('../order/findOrderAndOrderItem.do');
    }
    this.aaa=function(orderId){
        return $http.post('../order/fahuo.do?orderId='+orderId);
    }
});
