# 工程简介
[循环依赖](https://mrbird.cc/%E6%B7%B1%E5%85%A5%E7%90%86%E8%A7%A3Spring%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%96.html)

所谓循环依赖指的是：BeanA对象的创建依赖于BeanB，BeanB对象的创建也依赖于BeanA，这就造成了死循环，如果不做处理的话势必会造成栈溢出。Spring通过提前曝光机制，利用三级缓存解决循环依赖问题。本节将记录单实例Bean的创建过程，并且仅记录两种常见的循环依赖情况：普通Bean与普通Bean之间的循环依赖，普通Bean与代理Bean之间的循环依赖。
# 延伸阅读

