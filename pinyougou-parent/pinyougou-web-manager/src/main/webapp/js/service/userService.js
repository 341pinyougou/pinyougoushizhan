//服务层
app.service('userService',function($http){

    //读取用户列表
    this.findAll = function(){
        return $http.get('../user/findAll.do');
    }


    //分页
    this.findPage=function(page,rows){
        return $http.get('../user/findPage.do?page='+page+'&rows='+rows);
    }

    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../user/search.do?page='+page+"&rows="+rows, searchEntity);
    }

    //冻结
    this.updateStatus = function (ids,status) {
        return $http.get('../user/updateStatus.do?ids='+ids+'&status='+status);
    }
})