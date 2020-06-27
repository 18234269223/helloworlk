//模块化的JS开发，是一种面向对象的思想\
//secKillObj是一个专门用于实现秒杀业务的js对象，里面可以拥有若干的属性
//例如fun和url，fun和url也是对象类型分别表示为fun表示函数，用于完成及具体的秒杀业务逻辑，url表示请求路径url拥有若干个方法用于返回
//请求路径实现请求路径重复使用
var secKillObj={
   url:{
        getSystemTime:function () {
            return "/getSystemTime";
        },
        getRandomName:function(goodsId){
            return "/getRandomName/"+goodsId
        },
        secKill:function(goodsId,randomName){
            return "/secKill/"+goodsId+"/"+randomName
        },
       getOrderResult:function(goodsId){
            return "/getOrderResult/"+goodsId
       }
   },
   fun:{
       //初始化秒杀的方法，用于控制按钮逻辑
       initSecKill:function (goodsId,startTime,endTime) {
          $.ajax({
              url:secKillObj.url.getSystemTime(),
              type:"get",
              dataType:"json",
              success:function(data){
                  if(data.code!="0"){
                      alert(data.message)
                      return false
                  }
                  if(data.result<startTime){
                      secKillObj.fun.secKillCountdown(startTime*1,goodsId)
                      return false
                  }
                  if(data.result>endTime){
                      $("#secKillSpan").html("<span style='color:red;'>秒杀已经结束</span>")
                      return false
                  }

                 secKillObj.fun.doSecKill(goodsId)
              },
              error:function(){
                  alert("对不起！网络异常请求稍后再试！")
              }
          })
       },
       secKillCountdown:function(startTime,goodsId){
           var killTime = new Date(startTime + 1000);
           // var killTime=new Date(new Date().getTime()+5000)
           //使用任意的JQuery的对象，来启动倒计时，
           // 参数1为目标时间
           // 参数2为回调方法，JQuery回启动线程每秒钟代用一次这个回调方法用于修改页面的显示效果
           $("#secKillSpan").countdown(killTime, function (event) {
               //时间格式
               var format = event.strftime('距秒杀开始还有: %D天 %H时 %M分 %S秒');
               $("#secKillSpan").html("<span style='color:red;'>"+format+"</span>");
           }).on('finish.countdown', function () {
               //倒计时结束后回调事件，已经开始秒杀，用户可以进行秒杀了，有两种方式：
               //1、刷新当前页面
               // location.reload();
               //或者2、调用秒杀开始的函数
               secKillObj.fun.doSecKill(goodsId)
           });
       },
       doSecKill:function(goodsId){
           $("#secKillSpan").html('<input type="button" value="立即抢购" id="secKillBut">')
           $("#secKillBut").bind("click",function(){
               //点击秒杀按钮以后设置按钮不可用，用于第一个拦截用户重复购买
               //不是能100%拦截住所有重复请求，只能拦截住一部分
               $(this).attr("disabled",true)
               $.ajax({
                   url:secKillObj.url.getRandomName(goodsId),
                   dataType:"json",
                   type:"get",
                   success:function(data){
                        if(data.code!="0"){
                            alert(data.message)
                            return false
                        }
                        secKillObj.fun.secKill(goodsId,data.result);
                        //alert(data.result);
                   },
                   error:function(){
                       alert("对不起！网络异常请求稍后再试！")
                   }
               })
           })
       },
       secKill:function(goodsId,randomName){
           $.ajax({
               url:secKillObj.url.secKill(goodsId,randomName),
               dataType:"json",
               type:"get",
               success:function(data){
                   if(data.code!="0"){
                       alert(data.message)
                       return false
                   }
                  secKillObj.fun.getOrderResult(goodsId)
               },
               error:function(){
                   alert("对不起！网络异常请求稍后再试！22222222222")
               }
           })
       },
       getOrderResult:function(goodsId){
           $.ajax({
               url:secKillObj.url.getOrderResult(goodsId),
               dataType:"json",
               type:"get",
               success:function(data){
                   if(data.code!="0"){
                       //延时3钟执行一次递归调用获取订单
                       window.setTimeout("secKillObj.fun.getOrderResult("+goodsId+")",3000)
                       return false
                   }
                  $("#secKillSpan").html('<span style="color: red">下单成功 共'+data.result.ordermoney+'元 ，<a href="zhifu?id='+data.result.id+'">立即支付</a></span>')
               },
               error:function(){
                   alert("对不起！网络异常请求稍后再试！3333333333333333333")
               }
           })
       }
   }


}