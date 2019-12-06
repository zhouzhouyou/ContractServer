# 后端须知

1.对于Controller层，为了使Swagger能识别你要写进Api文档的方法，类名，甚至属性，你需要：

在你创建的Controller类前加上@Api注解，如图：

![image-20191206183535554](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20191206183535554.png)

在你的方法上添加@ApiOperation注解，如图：

![image-20191206183651620](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20191206183651620.png)

2.对于实体层，你需要在实体类前加@ApiModel注解，

![image-20191206183841761](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20191206183841761.png)

可以在属性前加@ApiModelProperty注解，

![image-20191206183943092](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20191206183943092.png)

如果需要更多Api相关注解，可查阅

https://www.jianshu.com/p/296d77fac00d

# 前端须知

你所需的就是查阅Api文档的方法，运行我们的服务器，在浏览器中访问

http://localhost:8848/swagger-ui.html

效果图：

![image-20191206184420557](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20191206184420557.png)

