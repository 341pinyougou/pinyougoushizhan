 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService,uploadService){
	$scope.entity = {};
	//注册用户
	$scope.reg=function(){
		
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);
			}
		);		
	}
    //$scope.entity.profession = "";


    //完善用户个人信息
    $scope.perfectionMessage=function(){
        $scope.entity.birthday=$scope.year+'年'+$scope.mouth+'月'+$scope.day+'日';
        $scope.entity.address=$scope.province+' '+$scope.city+' '+$scope.district;
        userService.updateMSG($scope.entity).success(
        	function (response) {
				alert(response.message);
            }
		);
    }

    //上传图片
    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                // 获得url

                $scope.url =  response.message;
                alert($scope.url);

            }else{
                alert(response.message); //<img src={{image_entity.url}} 再次发出请求
            }
        });
        // 准备下面的image_entity 有什么用呢？ 暂时当不知道
        //$scope.image_entity = {color:红色,url:路径};
    }



    //程序员
    $scope.cxy=function(){
        $scope.entity.profession='cxy';
    }
    //产品经理
    $scope.cpjl=function(){
        $scope.entity.profession='cpjl';
    }
    //UI
    $scope.ui=function(){
        $scope.entity.profession='ui';
    }

    //回显用户详情信息
	$scope.findVO=function () {{
		userService.findVO().success(
			function (response) {
				$scope.entity=response;
            }
		)
	}

    }
	
});	
