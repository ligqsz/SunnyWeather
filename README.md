# SunnyWeather实现流程  
## 网络模块编写  
1.retrofit的创建方式，详见ServiceCreator  
2.全球城市搜索：详见接口PlaceService  
3.数据获取详见SunnyWeatherNetwork，重点：扩展函数的写法和协程的使用  
4.数据处理详见仓库类Repository，重点LiveData协程的使用  
5.调用逻辑：输入地址（ui）-->改变viewModel的搜索城市liveData值--switchmap-->调用仓库网络请求接口Repository.searchPlaces-->改变城市liveData
-->ui界面数据刷新
