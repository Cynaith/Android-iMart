# iMart
---
- 帖子数据 https://www.rouding.com/shejiyushoucang/
- 帖子数据 https://www.topys.cn/
- 首页数据 http://search.sohu.com/?keyword=%E5%88%9B%E6%84%8F%E5%B8%82%E9%9B%86&spm=smpc.csrpage.0.0.1581866449821X7uHqPs&queryType=edit
- 图标 https://www.iconfont.cn/collections/detail?spm=a313x.7781069.1998910419.d9df05512&cid=20933
 
- fourth 通过登陆的用户名获取Info
  - 未完成 注册时将用户名写入数据库

- 全局变量 SharePreferenceUtils.getInstance().getUserName()
- 点赞https://github.com/jd-alexander/LikeButton
- 收藏 https://github.com/varunest/SparkButton

---

- 推荐功能

  - 后台根据定位获取相关 Shop
    - 前台获取定位，包含Location 和City
    - 后台根据Location 获取 region(完成)
    - 后台根据Location 获取 Shop (完成)
  - 后台根据 当前 Location和目标 Location 实现 WalkingWay (完成)
    - 返回List<String>
  - 后台根据 当前 Location和目标 Location 实现 BusWay
  - 后台根据 当前 Location和目标 Location 实现 CarWay
  - 集成百度地图实现POI检索 (完成)
  - 集成高德地图实现POI检索 (今日)
  - 界面
    - RecycleView 展示 List (今日)
    - List -> TabLayout Walk、Bus、Car (今日)
    - List展示、百度Map

