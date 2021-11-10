#### 更新历史

---
- 1.0.5
兼容`ActivityResultLauncher.launch(I input, ActivityOptionsCompat options)`, 支持Activity启动时配置`ActivityOptionsCompat`

- 1.0.4
修复系统配置变更(如屏幕旋转)导致`Activity重建`后回调被清空的问题

- 1.0.3
移除引用`androidx.activity:activity-ktx:1.3.1`后导致需要`minCompileSdk (30)`的问题

- 1.0.2
`IManageStartActivity`的lifecycleHost从param改为receiver

- 1.0.1
修复`META-INF/library_release.kotlin_module`错误

- 1.0.0
发布`ManageStartActivity`