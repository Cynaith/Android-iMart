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

- 推荐功能(完成)

  - 后台根据定位获取相关 Shop(完成)
    - 前台获取定位，包含Location 和City(完成)
    - 后台根据Location 获取 region(完成)
    - 后台根据Location 获取 Shop (完成)
  - 后台根据 当前 Location和目标 Location 实现 WalkingWay (完成)
    - 返回List<String>(完成)
  - 后台根据 当前 Location和目标 Location 实现 BusWay(完成)
  - 后台根据 当前 Location和目标 Location 实现 CarWay(完成)
  - 集成百度地图实现POI检索 (完成)
  - 集成高德地图实现POI检索 (完成)
  - 界面
    - RecycleView 展示 List (完成)
    - List -> TabLayout Walk、Bus、Car (完成)
    - List展示、百度Map(完成)
- android切换定位
 - https://blog.csdn.net/ppdouble/article/details/24656879?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task
 - adb -s emulator-5554 emu geo fix 121.49612 31.24010
- 视频功能(完成)
  - 上传视频
  - 点赞
  - 视频信息显示
  - Button跳转
- 邮件功能后台优化 (2020-04-02)
  - 邮箱验证 (完成)
  - 邮件发送 (完成)
  - 集成APP
  - 后台管理 (部分)
- 聊天功能优化 (2020-04-03)
  - 集成简单IM 目标: 点击私信->聊天界面 聊天列表中有聊天数据
    - 将源代码提取 实现点击私信自动加好友 (实现)
    - 提取聊天界面 (实现)
    - 获取聊天id，聊天界面绑定到私信按钮 (实现)
    - 实现列表的刷新 
    - 删除聊天列表

