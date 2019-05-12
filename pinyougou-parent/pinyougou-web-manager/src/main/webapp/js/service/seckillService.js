//服务层
app.service('seckillService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findGoodsByGoodsId=function (id) {
        return $http.post('../seckill/findOne.do?goodsId='+id);
    }

    //联动查询价格,库存
	this.findByitemId=function (id) {
        return $http.post('../seckill/findByitemId.do?itemId='+id);
    }

    //保存秒杀
	this.save=function (seckillVO, item) {
		return $http.post('../seckill/save.do?',seckillVO);
    }

    //回显秒杀商品
    this.findSeckillGoodsList=function () {
        return $http.get('../seckill/findSeckillGoodsList.do');
    }

    //回显全部秒杀商品
    this.findAllSeckillGoodsList=function () {
        return $http.get('../seckill/findAllSeckillGoodsList.do');
    }

    //开始审核  审核通过 或是驳回
    this.updateStatus = function(ids,status){
        return $http.get('../seckill/updateStatus.do?ids='+ids+"&status="+status);
    }

});
