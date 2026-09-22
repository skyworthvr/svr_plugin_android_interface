# SVR Android API 文档中心

<!-- 目录 -->
- [概览](#概览)
    - [功能模块一览](#功能模块一览)
- [1. 快速开始](#1-快速开始)
    - [1.1 环境要求](#11-环境要求)
    - [1.2 集成](#12-集成)
    - [1.3 初始化与释放](#13-初始化与释放)
    - [1.4 线程与初始化时序](#14-线程与初始化时序)
- [2. API 参考](#2-api-参考)
    - [2.1 AndroidInterface（入口）](#21-androidinterface入口)
    - [2.2 ApkUtils（安装/卸载）](#22-apkutils安装卸载)
    - [2.3 AppUtils（应用管理）](#23-apputils应用管理)
    - [2.4 JAppInfo（应用信息）](#24-jappinfo应用信息)
    - [2.5 MediaInfoUtils（媒体信息）](#25-mediainfoutils媒体信息)
    - [2.6 BatteryUtils（电量）](#26-batteryutils电量)
    - [2.7 BluetoothUtils（蓝牙）](#27-bluetoothutils蓝牙)
    - [2.8 BrightnessUtils（亮度）](#28-brightnessutils亮度)
    - [2.9 BusinessUtils（行业助手）](#29-businessutils行业助手)
    - [2.10 DeviceUtils（设备信息）](#210-deviceutils设备信息)
    - [2.11 DownloadUtils（下载）](#211-downloadutils下载)
    - [2.12 FotaUtils（系统升级）](#212-fotautils系统升级)
    - [2.13 UpgradeInfo（升级包信息）](#213-upgradeinfo升级包信息)
    - [2.14 HandshankUtils（手柄）](#214-handshankutils手柄)
    - [2.15 IntentUtils（Intent）](#215-intentutilsintent)
    - [2.16 ProximitySensorUtils（距离传感器）](#216-proximitysensorutils距离传感器)
    - [2.17 StorageUtils（存储）](#217-storageutils存储)
    - [2.18 SpeedTestUtils（网络测速）](#218-speedtestutils网络测速)
    - [2.19 TimeUtils（时间）](#219-timeutils时间)
    - [2.20 VolumeUtils（音量）](#220-volumeutils音量)
    - [2.21 WifiUtils（Wi-Fi）](#221-wifiutilswi-fi)
    - [2.22 WifiInfo（Wi-Fi 信息）](#222-wifiinfowi-fi-信息)
    - [2.23 GlobalWindowUtils（全局窗口）](#223-globalwindowutils全局窗口)
    - [2.24 CustomGlobalWindow（自定义窗口）](#224-customglobalwindow自定义窗口)
    - [2.25 GlobalWindowInfo（窗口参数）](#225-globalwindowinfo窗口参数)
    - [2.26 GlobalWindowType（窗口类型）](#226-globalwindowtype窗口类型)
    - [2.27 GlobalActionType（操作类型）](#227-globalactiontype操作类型)
    - [2.28 全局窗口监听接口](#228-全局窗口监听接口)
    - [2.29 ControllerManager（手柄数据）](#229-controllermanager手柄数据)
    - [2.30 PairManager（手柄配对）](#230-pairmanager手柄配对)
    - [2.31 AIDL 辅助类](#231-aidl-辅助类)
- [3. 附录](#3-附录)
    - [3.1 版本](#31-版本)
    - [3.2 混淆](#32-混淆)

## 概览

`svr_plugin_android_api.aar` 是创维 VR 一体机系统服务的 Android 客户端 SDK（包名 `com.ssnwt.vr.*`）。
SDK 通过 Binder 与设备端系统服务 `SvrService`（`com.ssnwt.vr.server`）通信，
为应用提供 APK 安装卸载、应用管理、蓝牙、Wi-Fi、电量/亮度/音量、设备信息、系统升级（FOTA）、手柄、全局窗口等系统能力。

> ⚠️ 本 SDK 依赖设备端预置的 `SvrService`，**仅能在创维 VR 设备上运行**（VQ910 / VQ920 / VQ930 等），在普通手机或其他设备上无法初始化。

### 功能模块一览

| 模块 | 入口类 | 说明 |
| --- | --- | --- |
| 入口 | `AndroidInterface` | 单例入口，init / release，获取各功能 Utils |
| 安装/卸载 | `ApkUtils` | apk/xapk 安装、卸载、包信息解析 |
| 应用管理 | `AppUtils` / `JAppInfo` | 应用列表、启动应用、后台管理、应用信息 |
| 系统状态 | `BatteryUtils` / `BrightnessUtils` / `VolumeUtils` / `TimeUtils` | 电量、亮度、音量、时间 |
| 设备 | `DeviceUtils` / `StorageUtils` / `ProximitySensorUtils` | 设备信息、息屏/亮屏、护眼模式、关机重启、存储、距离传感器 |
| 网络 | `WifiUtils` / `BluetoothUtils` | Wi-Fi 连接/热点、蓝牙扫描/配对/连接 |
| 升级 | `FotaUtils` / `UpgradeInfo` | 系统 FOTA 检查、下载、安装 |
| 下载 | `DownloadUtils` | 文件下载任务管理 |
| 手柄 | `HandshankUtils` / `ControllerManager` / `PairManager` | 手柄配对、电量、版本、DFU 升级、按键/摇杆事件、低层数据 |
| 全局窗口 | `GlobalWindowUtils` / `CustomGlobalWindow` | 系统全局菜单/对话框、第三方自绘全局窗口（仅 VQ920 / VQ930） |
| 行业助手 | `BusinessUtils` | 目录文件变化监听 |

## 1. 快速开始

### 1.1 环境要求

- minSdkVersion 26，目标平台 Android（ABI：`armeabi-v7a` / `arm64-v8a`）
- 运行设备：创维 VR 一体机（系统预置 `com.ssnwt.vr.server` 服务）
- 开发环境：Android Studio（Gradle）

### 1.2 集成

将 `svr_plugin_android_api.aar` 拷贝到 app 模块的 `libs` 目录，在 `build.gradle` 中添加依赖：

```
dependencies {
    implementation files('libs/svr_plugin_android_api.aar')
}
```

### 1.3 初始化与释放

通过单例 `AndroidInterface` 初始化；Unity 工程 `init` 的 `app` 参数可传 `null`（内部取 `UnityPlayer.currentActivity`）。

```
public class MainActivity extends AppCompatActivity {
    AndroidInterface androidInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidInterface = AndroidInterface.getInstance();
        androidInterface.init(getApplication(), new AndroidInterface.InitListener() {
            @Override public void onInitialized() {
                // 所有接口必须在初始化成功后调用
                androidInterface.getWifiUtils().setListener2(new WifiUtils.WifiListener2() {
                    @Override public void onOpened(boolean b) { }
                    @Override public void onConnecting(int i, String s) { }
                    @Override public void onSearchResult(ArrayList<WifiInfo> arrayList) { }
                    @Override public void onRssiLevelChanegd(int i) { }
                });
            }

            @Override public void onReleased() { }

            @Override public void onInitError() { }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        androidInterface.release();
    }
}
```

### 1.4 线程与初始化时序

> ⚠️ 所有 `getXxxUtils()` 及业务接口**必须在 `InitListener.onInitialized()` 回调之后调用**。
> 在非主线程且尚未初始化完成时调用 getter，会阻塞等待最多 5000ms 等待服务连接。建议在主线程调用 `init`。

> ⚠️ 不再使用时调用 `release()` 断开与系统服务的连接（通常在 `onDestroy()` 中）。

## 2. API 参考

### 2.1 AndroidInterface（入口）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance()`

AndroidInterface 是所有 Android 能力的统一入口单例。通过 `getInstance()` 获取实例，先调用 `init(Application)` 完成初始化，再通过各 `getXxxUtils()` 方法取得对应的能力对象。初始化依赖设备端 SvrService 连接成功。

> ⚠️ 线程与调用时序：所有 `getXxxUtils()` 内部会调用 `checkThread()`，在非主线程且尚未初始化完成时会阻塞最多 5000ms 等待初始化完成。建议在主线程调用 `init()`，并在 `InitListener.onInitialized()` 回调之后再调用各 `getXxxUtils()`。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `AndroidInterface` | 获取单例实例 |  |
| `init` | `(Application app)` | `void` | 初始化 SDK 接口；使用 Unity 时 app 可为 null | 必须在调用各 `getXxxUtils()` 之前调用 |
| `init` | `(Application app, InitListener listener)` | `void` | 初始化 SDK 接口并注册初始化状态回调；使用 Unity 时 app 可为 null | 重复调用时若已初始化会立即回调 `onInitialized()`；若正在初始化中，listener 加入等待列表，初始化完成后统一回调 |
| `release` | `()` | `void` | 释放接口，解除监听并释放 SvrManager | 释放后需重新 `init()` 才能继续使用 |
| `isInitialized` | `()` | `boolean` | 是否已完成初始化（与 SvrService 连接成功） |  |
| `getApkUtils` | `()` | `ApkUtils` | apk 安装卸载 |  |
| `getAppUtils` | `()` | `AppUtils` | 应用列表获取、打开应用等 |  |
| `getBatteryUtils` | `()` | `BatteryUtils` | 电量相关接口 |  |
| `getWifiUtils` | `()` | `WifiUtils` | wifi 相关接口 |  |
| `getBrightnessUtils` | `()` | `BrightnessUtils` | 亮度相关接口 |  |
| `getVolumeUtils` | `()` | `VolumeUtils` | 音量相关接口（仅能控制 STREAM_MUSIC，其他需自行实现） |  |
| `getTimeUtils` | `()` | `TimeUtils` | 时间相关接口 |  |
| `getFotaUtils` | `()` | `FotaUtils` | 系统升级相关接口 |  |
| `getDeviceUtils` | `()` | `DeviceUtils` | 设备信息相关接口（包含息亮屏、护眼模式、关机重启等） |  |
| `getStorageUtils` | `()` | `StorageUtils` | 存储大小相关接口 |  |
| `getBluetoothUtils` | `()` | `BluetoothUtils` | 蓝牙相关接口 |  |
| `getProximitySensorUtils` | `()` | `ProximitySensorUtils` | 距离传感器相关接口 |  |
| `getHandshankUtils` | `()` | `HandshankUtils` | 获取手柄工具实例 |  |
| `getDownloadUtils` | `()` | `DownloadUtils` | 获取下载工具实例 |  |
| `getSpeedTestUtils` | `()` | `SpeedTestUtils` | 获取测速工具实例 |  |
| `getBusinessUtils` | `()` | `BusinessUtils` | 获取行业助手工具实例 |  |
| `getMediaInfoUtils` | `()` | `MediaInfoUtils` | 获取媒体信息工具实例 |  |
| `getIntentUtils` | `()` | `IntentUtils` | 获取 Intent 工具实例 |  |
| `getGlobalWindowUtils` | `()` | `GlobalWindowUtils` | 获取全局窗口工具实例 |  |

##### InitListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onInitialized` | 无 | 初始化完成（与 SvrService 连接成功），此时可安全调用各 getXxxUtils() |
| `onReleased` | 无 | 已释放 / 连接断开 |
| `onInitError` | 无 | 初始化出错（连接失败） |

另提供公开嵌套类 `AndroidInterface.InitListenerNative`（构造方法 `InitListenerNative(long handle)`），用于将 InitListener 回调转发到 Native 层，供 JNI 使用。

### 2.2 ApkUtils（安装/卸载）

包名：`com.ssnwt.vr.androidmanager.apk`

获取方式：`AndroidInterface.getInstance().getApkUtils()`

Apk 安装/卸载接口。支持从 apk / xapk 文件解析包名、应用名、版本号与完整应用信息，并提供 apk、xapk 的安装与应用卸载能力，安装与卸载结果为异步回调。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(ApkListener listener)` | `void` | 设置安装/卸载回调监听器 |  |
| `getPackageName` | `(String path)` | `String` | 从 apk 文件路径获取包名 |  |
| `getPackageNameFromXAPK` | `(String path)` | `String` | 从 xapk 文件路径获取包名 |  |
| `getAppName` | `(String path)` | `String` | 从 apk 文件路径获取应用名 |  |
| `getAppNameFromXAPK` | `(String path)` | `String` | 从 xapk 文件路径获取应用名 |  |
| `getVersionCode` | `(String path)` | `int` | 获取 apk 文件中的版本号 | 异常时返回 -1 |
| `getAppInfo` | `(String path)` | `JAppInfo` | 获取 apk 文件中解析出的应用全部信息 | 异常时返回 null |
| `installApk` | `(String path)` | `void` | 安装 apk，异步，结果经 `ApkListener` 回调 | path 形如 `/storage/emulated/0/APK/test.apk` |
| `installXApk` | `(String path)` | `void` | 安装 xapk，异步，结果经 `ApkListener` 回调 | path 形如 `/storage/emulated/0/APK/test.xapk` |
| `uninstallApk` | `(String packageName)` | `void` | 卸载应用，异步，结果经 `ApkListener` 回调 | 参数为包名 |

##### 接口：ApkListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onPackageEvent` | `(int eventCode, String msg, JAppInfo info)` | 安装/卸载结果回调。`eventCode` 为结果事件码（见下表）；`msg` 在安装/卸载失败时为 apk 路径或应用包名；`info` 在安装/卸载成功时为应用信息 |

##### 安装/卸载事件码

| 事件码 | 值 | 说明 |
| --- | --- | --- |
| `EVENT_INSTALL_SUCCESS` | `0` | 安装成功 |
| `EVENT_INSTALL_FAIL_PACKAGE_ERROR` | `1` | 安装包有问题 |
| `EVENT_INSTALL_FAIL_VERSION_LOW` | `2` | 安装版本低于当前版本 |
| `EVENT_INSTALL_FAIL_PATH_ERROR` | `3` | 安装包路径有问题 |
| `EVENT_INSTALL_FAIL_SYSTEM_APP` | `4` | 安装失败：系统应用 |
| `EVENT_INSTALL_FAIL_MEMORY_NOT_ENOUGH` | `5` | 安装失败：内存不足 |
| `EVENT_UNINSTALL_SUCCESS` | `10` | 卸载成功 |
| `EVENT_UNINSTALL_FAIL_SYSTEM_APP` | `11` | 卸载失败：系统应用 |
| `EVENT_UNINSTALL_FAIL_OTHER` | `12` | 卸载的其他问题 |
| `EVENT_UNINSTALL_NO_EXIST` | `13` | 当前应用不存在 |
| `EVENT_INSTALL_ING` | `14` | 安装中 |

表注：以上事件码常量定义于 `JAppState`，随 `onPackageEvent` 回调下发，可直接引用。

### 2.3 AppUtils（应用管理）

包名：`com.ssnwt.vr.androidmanager.app`

获取方式：`AndroidInterface.getInstance().getAppUtils()`

应用管理接口。提供应用启动、应用列表查询、最近应用列表、应用图标与名称查询、内存清理与后台进程管理、应用类别查询等能力。

> ⚠️ `isVrApp` 仅支持 VQ920 / VQ930 设备，其他机型恒返回 false。

公开常量：`TAG`（`String`），值为 `"AppUtils"`。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `AppUtils` | `(Context context)` | `AppUtils` | 构造方法，使用传入的 Context 获取 PackageManager | 构造方法；内部持有 Context 引用，注意生命周期 |
| `setListener` | `(AppListener listener)` | `void` | 设置应用数量变化回调监听器 |  |
| `startApp` | `(String pkg)` | `void` | 按包名启动应用 |  |
| `startApp` | `(String pkg, String clazz)` | `void` | 按包名与 Activity 类名启动应用 |  |
| `startApp` | `(String pkg, String clazz, String type, String title, String uri, String info)` | `void` | 启动应用并携带媒体信息。type 为 `video/*`、`image/*`；title 为视频/图片名称；uri 为本地（`/storage/emulated/0/Movies/test.mp4`）或网络（`http://192.168.1.1/test.mp4`）地址；info 为 `JAppInfo` 的 JSON 数据 |  |
| `startApp` | `(String pkg, String clazz, String key, boolean value)` | `void` | 启动应用并携带单个键值参数 |  |
| `startApp` | `(String pkg, String[] keys, String[] values)` | `void` | 启动应用并携带多组键值参数 | keys/values 为空或长度不一致时不执行；内部以逗号拼接后下发 |
| `getFirstLauncherActivity` | `(String pkg)` | `String` | 获取首个 launcher Activity（CATEGORY_LAUNCHER）类名 |  |
| `getVersionCode` | `(String packageName)` | `int` | 获取应用版本号 |  |
| `openBrowser` | `(String url)` | `void` | 打开浏览器 | url 必须包含 HTTP/HTTPS |
| `openBrowser` | `(String packageName, String url)` | `void` | 打开浏览器；若指定包名存在会弹出选择框。包名示例：`com.android.chrome`、`org.mozilla.vrbrowser` | url 必须包含 HTTP/HTTPS |
| `openBrowser2` | `(String url)` | `String` | 打开浏览器，带返回值 | url 必须包含 HTTP/HTTPS |
| `openBrowser2` | `(String packageName, String url)` | `String` | 打开浏览器，带返回值；若指定包名存在会弹出选择框 | url 必须包含 HTTP/HTTPS |
| `getIcon` | `(String pkg, String clazz)` | `byte[]` | 获取应用图标的二进制数据 | 服务端无返回时为 null |
| `getAppName` | `(String pkg, String clazz)` | `String` | 按包名与 Activity 类名获取应用名 |  |
| `getAppInfos` | `(int index, int count)` | `String` | 获取应用列表 | 返回 `JAppInfo` 数组的 JSON 字符串，index 为起始位置，count 为数量 |
| `getAppInfos2` | `(int index, int count)` | `ArrayList<JAppInfo>` | 获取应用列表（已反序列化） | 服务端 parcel 为 null 时返回空列表 |
| `scanApps` | `()` | `void` | 扫描应用 |  |
| `cleanMemory` | `()` | `void` | 清理后台应用，释放内存 |  |
| `getResolveInfo` | `(String packageName)` | `List<ResolveInfo>` | 根据包名获取应用，一个应用可能有多个 launcher 属性（多个图标显示） |  |
| `checkAppExist` | `(String packageName)` | `boolean` | 检查应用是否存在 | true 存在，false 不存在 |
| `isSystemApp` | `(String packageName)` | `boolean` | 检查是否为系统应用 | true 系统应用，false 普通应用 |
| `isPackageStartable` | `(String packageName)` | `boolean` | 检查应用是否可启动 |  |
| `getAppCategory` | `(String packageName)` | `JAppInfo.AppCategory` | 通过包名查询应用类别 |  |
| `getTaskSize` | `()` | `int` | 获取当前运行的任务数量 |  |
| `killProcess` | `(String pkg)` | `void` | 杀死指定包名的应用 |  |
| `getRunningPackages` | `()` | `ArrayList<ComponentName>` | 获取后台运行程序的包名等信息 |  |
| `getRunningPackages2` | `()` | `ArrayList<JAppInfo>` | 获取后台运行的 apk 的信息 |  |
| `getAppInfos` | `(String pkg)` | `ArrayList<JAppInfo>` | 根据包名获取 apk 的信息 | 与 `getAppInfos(int, int)` 为重载，勿混淆 |
| `isVrApp` | `(String packageName)` | `boolean` | 判断指定包名是否为 VR 应用 | **设备限制**：仅支持 VQ930（反射调用 `android.util.SxrVrUtils.isVRApp`）与 VQ920（反射调用 `android.util.SSNWTVRUtils.isVRApp`）；其他机型打印日志 `isVrApp ： The <device> is not support!` 并返回 false。反射失败时抛出 `RuntimeException` |
| `isXRVDEnabled` | `()` | `boolean` | 判断 XRVD 是否启用 | 静态方法；依赖系统属性：`persist.sys.xrvd.enable` 为 true 且 `persist.ssnwtvr.display.enable` 为 false（默认 true） |

##### 接口：AppListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onAppCountChanged` | `(int count)` | 应用数量变化回调，`count` 为当前应用数量 |

### 2.4 JAppInfo（应用信息）

包名：`com.ssnwt.vr.androidmanager.app`

来源：由 `ApkUtils` / `AppUtils` 接口返回（例如 `ApkUtils.getAppInfo`、`AppUtils.getAppInfos2`）

应用信息数据类，实现 `Serializable`。承载应用名、包名、Activity 类名、版本、存储与内存占用、使用与安装时间、应用类型与类别、状态、图标与网络扩展信息等。

> 实例由 SDK 接口返回或通过 `parseFromJson` 创建，无需直接构造。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `parseFromJson` | `(String json)` | `JAppInfo` | 由 JSON 字符串反序列化为 JAppInfo | 静态方法 |
| `getAppName` | `()` | `String` | 获取应用名 |  |
| `getPackageName` | `()` | `String` | 获取包名 |  |
| `getClassName` | `()` | `String` | 获取 Activity 类名 |  |
| `getVersion` | `()` | `String` | 获取应用版本名 |  |
| `getVersionCode` | `()` | `int` | 获取应用版本号 |  |
| `setVersionCode` | `(int versionCode)` | `void` | 设置应用版本号 |  |
| `getStorageSize` | `()` | `int` | 获取应用存储占用（data、cache 等） |  |
| `getAppType` | `()` | `int` | 获取应用类型，取值见 `AppType` |  |
| `getLastTimeUsed` | `()` | `long` | 获取最后一次使用时间 |  |
| `setKiosk` | `(boolean kiosk)` | `void` | 设置是否开启 kiosk 模式 |  |
| `getLaunchCount` | `()` | `int` | 获取使用次数 |  |
| `getInstallTime` | `()` | `long` | 获取应用安装时间 |  |
| `getMemorySize` | `()` | `int` | 获取内存占用 | 单位 kb |
| `setMemorySize` | `(int size)` | `void` | 设置内存占用 | 单位 kb |
| `isNeedUserLogined` | `()` | `boolean` | 是否需要用户登录 |  |
| `setNeedUserLogined` | `(boolean needUserLogined)` | `void` | 设置是否需要用户登录 |  |
| `isNeedHandleConnected` | `()` | `boolean` | 是否需要连接手柄 |  |
| `setNeedHandleConnected` | `(boolean needHandleConnected)` | `void` | 设置是否需要连接手柄 |  |
| `getCategory` | `()` | `int` | 获取应用类别，取值见 `AppCategory` |  |
| `setCategory` | `(int category)` | `void` | 设置应用类别 |  |
| `toJsonStr` | `()` | `String` | 序列化为 JSON 字符串 |  |

##### 公开字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `DESC` | `Comparator` | 静态比较器，按 `lastTimeUsed` 降序 |
| `TIMEUSED` | `Comparator` | 静态比较器，按 `installTime` 降序 |
| `NAME_ASC` | `Comparator` | 静态比较器，按 `appName` 忽略大小写升序 |
| `NAME_DESC` | `Comparator` | 静态比较器，按 `appName` 忽略大小写降序 |
| `ASC` | `Comparator` | 静态比较器，按 `lastTimeUsed` 升序 |
| `packageName` | `String` | 包名，默认空字符串 |
| `lastTimeUsed` | `long` | 最后一次使用时间 |
| `appName` | `String` | 应用名，默认空字符串 |
| `className` | `String` | Activity 类名，默认空字符串 |
| `version` | `String` | 版本名，默认空字符串 |
| `versionCode` | `int` | 版本号 |
| `storageSize` | `int` | 应用存储占用（data、cache 等），默认 -1 |
| `appType` | `int` | 应用类型，取值为 `AppType` 的序数，默认 `AppType.Normal` |
| `launchCount` | `int` | 启动次数 |
| `installTime` | `long` | 安装时间 |
| `memorySize` | `int` | 内存占用，单位 kb，默认 -1 |
| `needUserLogined` | `boolean` | 是否需要用户登录 |
| `needHandleConnected` | `boolean` | 是否需要连接手柄 |
| `category` | `int` | 类别：1 应用、2 游戏、0 未知应用，默认 `AppCategory.UNKNOWN` |
| `kiosk` | `boolean` | 是否开启 kiosk 模式 |
| `icon` | `String` | 网络数据：图标 |
| `url` | `String` | 网络数据：地址 |
| `categoryNet` | `String` | 网络数据：分类，例如「影音娱乐」 |
| `cateName` | `String` | 网络数据：分类名，例如「应用」 |
| `appId` | `int` | 网络数据：应用 ID |
| `isConnection` | `boolean` | 是否被收藏 |
| `forceUp` | `boolean` | 是否强制更新 |
| `appStatus` | `AppStatus` | 应用状态，默认为可使用状态（`AppStatus.Normal`） |

##### 枚举 AppType

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `System` | `0` | 系统应用 |
| `Normal` | `1` | 普通安装应用 |
| `Vive` | `2` | Vive 应用 |
| `Svr` | `3` | Svr 应用 |
| `Service` | `4` | 服务 |

静态方法：`AppType.fromInt(int i)` 按序数反查枚举，未命中返回 null。

##### 枚举 AppCategory

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `APP` | `1` | 应用 |
| `GAME` | `2` | 游戏 |
| `UNKNOWN` | `0` | 未知应用 |

公开字段：`type`（`int`），类别数值。静态方法：`AppCategory.fromType(int type)`、`AppCategory.fromType(String typeName)`，均未命中时返回 `UNKNOWN`。

##### 枚举 AppStatus

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `Pending` | `0` | 待处理 |
| `Downloading` | `1` | 下载中 |
| `Pause` | `2` | 暂停 |
| `Downloaded` | `3` | 已下载 |
| `Install` | `4` | 安装中 |
| `Update` | `5` | 更新 |
| `Normal` | `6` | 已安装可使用状态 |
| `Error` | `7` | 错误 |
| `Uninstalling` | `8` | 卸载中 |

公开字段：`state`（`int`），状态数值。静态方法：`AppStatus.fromType(int state)`、`AppStatus.fromType(String stateName)`，均未命中时返回 `Normal`。

### 2.5 MediaInfoUtils（媒体信息）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getMediaInfoUtils()`

媒体信息接口。当前提供查询指定视频字幕列表的能力，结果通过 `SubtitleListListener` 回调返回。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `querySubtitleList` | `(String uri, SubtitleListListener listener)` | `void` | 查询字幕列表 | `uri` 支持：本地视频 `/storage/emulated/0/xxx` 或 `file://xxx`；NAS 视频 `smb://xxx`；飞屏视频 `http://xxx/media_file`。listener 请务必创建新的对象 |

##### SubtitleListListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onResult` | `List<String> list` | 字幕列表查询结果回调，参数为查询到的字幕列表 |

### 2.6 BatteryUtils（电量）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getBatteryUtils()`

电量相关接口：查询当前电量、最大电量、充电状态与充电开关，并可通过 `setListener` 监听电量与充电状态变化。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(Listener listener)` | `void` | 设置电量/充电状态变化监听 |  |
| `getCurrentBattery` | `()` | `int` | 获取当前电量，取值 0 - getMaxBattery() |  |
| `getMaxBattery` | `()` | `int` | 获取最大电量，默认最大值 100 |  |
| `getBatteryStatus` | `()` | `int` | 充电状态：0 未充电，1 充电器充电，2 USB 充电 |  |
| `getChargeState` | `()` | `int` | 查询充电开关状态，返回 1 为 enable，0 为 disable |  |
| `disableCharge` | `()` | `void` | 关闭充电 |  |
| `enableCharge` | `()` | `void` | 开启充电 |  |

##### Listener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onBatteryChanged` | `int battery` | 电量变化，取值 0 - getMaxBattery() |
| `onBatteryStatusChanged` | `int status` | 充电状态变化：0 未充电，1 充电器充电，2 USB 充电 |

### 2.7 BluetoothUtils（蓝牙）

包名：`com.ssnwt.vr.androidmanager.bluetooth`

获取方式：`AndroidInterface.getInstance().getBluetoothUtils()`

蓝牙工具类。提供蓝牙开关与状态查询、本机/远端设备名称设置、设备搜索、配对与解绑、连接与断开、媒体音频开关，以及设备发现与连接状态回调等能力。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(BluetoothListener listener)` | `void` | 设置蓝牙行为回调监听器 |  |
| `setConnectionStateListener` | `(BluetoothConnectionStateListener listener)` | `void` | 设置蓝牙连接状态回调监听器 |  |
| `isOpen` | `()` | `boolean` | 蓝牙是否已打开 | true-已打开，false-已关闭 |
| `open` | `()` | `boolean` | 打开蓝牙 | true-成功，false-失败 |
| `getName` | `()` | `String` | 获取设备名称 |  |
| `setNewName` | `(BluetoothDevice device, String name)` | `boolean` | 设置远端蓝牙设备名称 | device 为蓝牙设备，name 为设备名称 |
| `hasMediaAudio` | `(BluetoothDevice device)` | `boolean` | 判断该蓝牙设备是否支持媒体音频（MediaAudio） |  |
| `isMediaAudioOn` | `(BluetoothDevice device)` | `boolean` | 判断该蓝牙设备的媒体音频是否已开启 |  |
| `setMediaAudio` | `(BluetoothDevice device, boolean on)` | `boolean` | 开关该蓝牙设备的媒体音频 | on 为 true 表示开启 |
| `setName` | `(String name)` | `void` | 修改本机蓝牙名称 |  |
| `close` | `()` | `boolean` | 关闭蓝牙 | true-成功，false-失败 |
| `search` | `()` | `boolean` | 搜索蓝牙设备 | true-成功，false-失败 |
| `cancelSearch` | `()` | `boolean` | 取消搜索蓝牙设备 | true-成功，false-失败 |
| `isSearching` | `()` | `boolean` | 是否正在搜索蓝牙设备 | true-搜索中，false-未搜索 |
| `isConnected` | `()` | `boolean` | 蓝牙是否已连接 | true-已连接，false-已断开 |
| `getBondedDevices` | `()` | `List<BluetoothDevice>` | 获取已配对设备列表 | 无结果时返回空列表 |
| `bond` | `(BluetoothDevice device)` | `void` | 配对远端蓝牙设备 |  |
| `unbond` | `(BluetoothDevice device)` | `void` | 取消配对远端蓝牙设备 |  |
| `isDeviceConnected` | `(BluetoothDevice device)` | `boolean` | 判断指定远端蓝牙设备是否已连接 | true-已连接，false-未连接 |
| `connectDevice` | `(BluetoothDevice device)` | `void` | 连接远端蓝牙设备 |  |
| `disconnectDevice` | `(BluetoothDevice device)` | `void` | 断开与远端蓝牙设备的连接 |  |

##### 接口：BluetoothListener

蓝牙行为回调接口，通过 `setListener` 注册。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onOpened` | `(boolean isOpen)` | 蓝牙开关状态变化；true-已打开，false-已关闭 |
| `onConnected` | `(boolean isConnect)` | 蓝牙连接状态变化；true-已连接，false-已断开 |
| `onDeviceFound` | `(BluetoothDevice device)` | 搜索到新设备 |
| `onBondChanged` | `(BluetoothDevice device)` | 设备配对状态发生变化 |
| `onScanStart` | `()` | 开始搜索 |
| `onScanFinish` | `()` | 搜索结束 |
| `onNoSupportBluetooth` | `()` | 本机不支持蓝牙 |
| `onBondError` | `(int code)` | 配对出错；code 为 0 表示配对错误，1 表示需要系统权限 |
| `onBluetoothUtilsActive` | `(boolean active)` | BluetoothUtils 是否处于活跃状态 |

##### 接口：BluetoothConnectionStateListener

蓝牙连接状态回调接口，通过 `setConnectionStateListener` 注册。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onConnectionStateChanged` | `(String device, int state)` | 连接状态发生变化；device 为状态变化的设备，state 取值：`STATE_DISCONNECTED`(0)、`STATE_CONNECTING`(1)、`STATE_CONNECTED`(2)、`STATE_DISCONNECTING`(3) |

### 2.8 BrightnessUtils（亮度）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getBrightnessUtils()`

亮度相关接口：查询最大/当前亮度、设置亮度，并可通过 `setListener` 监听亮度变化。提供 v1（int）与 v2（String/float）两套读写接口。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(Listener listener)` | `void` | 设置亮度变化监听 |  |
| `getMaxBrightness` | `()` | `int` | 获取最大亮度，默认值 255 |  |
| `getCurrentBrightness` | `()` | `int` | 获取当前亮度，取值 0 - getMaxBrightness() |  |
| `setBrightness` | `(int brightness)` | `void` | 设置系统亮度，取值 0 - getMaxBrightness() |  |
| `getCurrentBrightnessS` | `()` | `String` | 获取当前亮度（v2），取值 0 - getMaxBrightness() | 返回字符串形式 |
| `setBrightnessF` | `(float brightness)` | `void` | 设置系统亮度（v2），取值 0 - getMaxBrightness() | 浮点形式设置 |

##### Listener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onBrightnessChanged` | `int brightness` | 亮度变化 |

### 2.9 BusinessUtils（行业助手）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getBusinessUtils()`

行业助手工具类：用于在 Unity 中监听指定目录（可选指定文件）的文件变化事件。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setFilePathListener` | `(String dir, String path, FileObserverListener listener)` | `void` | Unity 中监听文件变化 | `dir` 为必填的监控目录（@NonNull），`path` 可为 null（@Nullable） |

##### FileObserverListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `create` | 无 | 文件或文件夹被创建 |
| `modify` | 无 | 文件或文件夹被修改，copy 或覆盖时发生 |
| `delete` | 无 | 文件或文件夹被删除 |
| `attrib` | 无 | 权限、所有者、时间戳被修改 |
| `deleteSelf` | 无 | 被监控的文件或文件夹被删除，监控停止 |
| `open` | 无 | 文件或文件夹被打开 |

##### 公开字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `TAG` | `String` | 日志 TAG，值为类简单名（当前为 `BusinessUtils`） |
| `CREATE` | `int` | 值 1，文件/文件夹被创建事件标识 |
| `MODIFY` | `int` | 值 2，文件/文件夹被修改事件标识 |
| `DELETE` | `int` | 值 3，文件/文件夹被删除事件标识 |
| `ATTRIB` | `int` | 值 4，属性（权限/所有者/时间戳）被修改事件标识 |
| `DELETE_SELF` | `int` | 值 5，被监控对象被删除、监控停止事件标识 |
| `OPEN` | `int` | 值 6，文件/文件夹被打开事件标识 |

另提供公开嵌套类 `BusinessUtils.FileObserverListenerDelegate`（SvrCallback 子类），用于将服务端回调按上述事件标识分派到 `FileObserverListener`，通常无需直接使用。

### 2.10 DeviceUtils（设备信息）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getDeviceUtils()`

设备信息相关接口：获取设备名称、品牌、型号、CPU/GPU/主板信息、版本号、MAC/IP、分辨率、内存等，并提供息亮屏、护眼模式、色温、重启关机、adb 开关、LED 控制及多种状态监听能力。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(Listener listener)` | `void` | 设置护眼模式状态监听 |  |
| `getDeviceName` | `()` | `String` | 获取产品名称 |  |
| `getBrand` | `()` | `String` | 获取品牌名称 |  |
| `getProductModel` | `()` | `String` | 获取产品型号 |  |
| `getCPUModel` | `()` | `String` | 获取 CPU 型号 |  |
| `getCPUId` | `()` | `String` | 获取 CPU Id |  |
| `getGPUModel` | `()` | `String` | 获取 GPU 型号 |  |
| `getBoardModel` | `()` | `String` | 获取主板型号 |  |
| `getManufacturer` | `()` | `String` | 获取制造商 |  |
| `getAndroidVersion` | `()` | `String` | 获取安卓版本 |  |
| `getResolution` | `()` | `String` | 获取分辨率，格式为 宽x高 |  |
| `getSerialNumber` | `()` | `String` | 获取序列号（SN） |  |
| `getSoftwareVersion` | `()` | `String` | 获取软件版本号 |  |
| `getSoftwareVersionCode` | `()` | `String` | 获取软件版本编码 |  |
| `getHardwareVersion` | `()` | `String` | 获取硬件版本 |  |
| `getWifiMac` | `()` | `String` | 获取 WIFI MAC 地址 |  |
| `getWifiIpAddresses` | `()` | `String` | 获取 WIFI IP 地址 |  |
| `getTotalRamStorageSize` | `()` | `int` | 获取总内存大小 | 单位 MB |
| `getAvailableRamStorageSize` | `()` | `int` | 获取可用内存大小 | 单位 MB |
| `getDisplayWidthPixels` | `()` | `int` | 获取当前屏幕宽度像素 |  |
| `getDisplayHeightPixels` | `()` | `int` | 获取当前屏幕高度像素 |  |
| `getDisplayDensity` | `()` | `int` | 获取当前屏幕密度 |  |
| `doFactoryReset` | `(Context context, boolean eraseSdCard)` | `void` | 恢复出厂设置 | `eraseSdCard` 为是否擦除 SD 卡；`context` 参数未参与实际下发 |
| `openEyeProtectionMode` | `(boolean open)` | `void` | 开关护眼模式 | true 打开，false 关闭 |
| `isOpenEyeProtectionMode` | `()` | `boolean` | 护眼模式是否开启 | true 打开，false 关闭 |
| `setColorTemperature` | `(int value)` | `void` | 设置色温 | 取值范围 0-27 |
| `reboot` | `()` | `void` | 重启设备 |  |
| `shutdown` | `()` | `void` | 关机 |  |
| `setScreenOffTimeout` | `(int timeoutValue)` | `void` | 设置息屏时间 | 单位 ms |
| `setScreenStatusListener` | `(ScreenStatusListener listener)` | `void` | 设置息亮屏状态监听 |  |
| `setNativeScreenStatusListener` | `(long handle)` | `void` | 同 setScreenStatusListener，用于 Native 函数设置监听器 | 供 JNI 使用 |
| `isMouseAttached` | `()` | `boolean` | 鼠标是否已插入 |  |
| `setMouseListener` | `(MouseListener listener)` | `void` | 设置鼠标插拔状态监听 |  |
| `isScreenOn` | `()` | `boolean` | 屏幕是否点亮 | true 亮屏，false 息屏 |
| `setAdbEnabled` | `(boolean enable)` | `boolean` | 打开或关闭 adb，本次开机有效 | 重启后失效 |
| `setScreenOffEnabled` | `(boolean enable)` | `void` | 打开或关闭「设备不熄屏」 |  |
| `setPersistAdbEnabled` | `(boolean enable)` | `boolean` | 打开或关闭 adb，永久有效 | ⚠️ 如果是定制设备（`ro.ssnwt.adb.lock` 为 1），一旦关闭，只能刷机或重置才能重新打开 |
| `isAdbEnabled` | `()` | `boolean` | adb 是否开启 |  |
| `isScreenOffEnabled` | `()` | `boolean` | 「设备不熄屏」是否开启 |  |
| `hasSSNWTAdbLock` | `()` | `boolean` | 当前 ROM 是否添加了定制的 adb 锁；若添加，一旦关闭则不能打开 |  |
| `isLargeSpaceEnable` | `()` | `boolean` | 判断大空间模式是否开启，读取系统属性 `persist.sxr.large_space.enable` 是否为 1 | 静态方法，直接读系统属性，不经过 SvrService |
| `setIpdListener` | `(IpdListener listener)` | `void` | 设置瞳距变化监听 |  |
| `setDeviceShutdownListener` | `(DeviceShutdownListener listener)` | `void` | 设置设备关机、重启回调 |  |
| `setDeviceShutdownNativeListener` | `(long handle)` | `void` | 同 setDeviceShutdownListener，用于 Native 设置监听器 | 供 JNI 使用 |
| `setVSTRangeListener` | `(VSTRangeListener listener)` | `void` | 设置 VST 范围变化监听 |  |
| `onInterceptKeyBeforeDispatching` | `(KeyEvent event)` | `boolean` | 按键事件分发前拦截 | 返回 true 表示拦截该 key 事件 |
| `recenter` | `()` | `void` | 画面重新居中（recenter） |  |
| `execCommand` | `(String command, CommandListener listener)` | `void` | 在设备端执行命令并异步返回执行结果 | `listener` 可为 null，此时不返回结果 |
| `setProp` | `(String key, String value)` | `void` | 设置系统属性 |  |
| `getProp` | `(String key, String defaultValue)` | `String` | 读取系统属性，不存在时返回默认值 |  |
| `readFileToInt` | `(String path)` | `int` | 读取文件内容并转为 int 返回 |  |
| `writeStringToFile` | `(String path, String value)` | `void` | 将字符串写入指定文件 |  |
| `flashLed` | `(int type)` | `void` | 点亮 LED | `type` 取值由设备端约定 |
| `blinkLed` | `(int type, int delayOn, int delayOff)` | `void` | LED 闪烁 | `delayOn` 亮时长，`delayOff` 灭时长 |

##### Listener（护眼模式状态回调）

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onEyeProtection` | `boolean open` | 护眼模式状态变化，true 开启，false 关闭 |

##### ScreenStatusListener（息亮屏状态回调）

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onSwitch` | `String status` | 息亮屏状态变化，状态字符串由设备端透传 |

##### MouseListener（鼠标插拔状态回调）

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onState` | `boolean isAttached` | 鼠标插拔状态变化，true 已插入 |

##### IpdListener（瞳距回调）

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onIpdChange` | `int ipd` | 瞳距变化 |

##### DeviceShutdownListener（设备关机、重启回调）

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onShutdown` | `boolean isReboot` | 设备关机/重启，true 表示重启，false 表示关机 |

##### VSTRangeListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onVSTChange` | `int range` | VST 范围变化 |

##### CommandListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onResult` | `int exitCode, String stdout, String stderr` | 命令执行完成，返回退出码与标准输入输出内容 |

### 2.11 DownloadUtils（下载）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getDownloadUtils()`

下载管理工具类：支持按 url / key 发起下载、指定存储路径、重新下载、暂停、停止、删除任务，以及查询文件是否存在、是否下载完成和下载信息。下载过程通过 `DownloadListener` 回调。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `downloadFile` | `(String url, DownloadListener listener)` | `void` | 下载文件 | 不传 key，由设备端根据 url 推导 |
| `downloadFile` | `(String key, String url, DownloadListener listener)` | `void` | 指定 key 下载文件 |  |
| `downloadFile` | `(String key, String url, String path, DownloadListener listener)` | `void` | 指定 key 与存储路径下载文件 | `path` 为文件存储路径 |
| `reDownloadFile` | `(String key, DownloadListener listener)` | `void` | 按 key 重新开始下载 |  |
| `resetDownloadListener` | `(String key, DownloadListener listener)` | `void` | 重设下载监听器，用于重启或者 launcher 退出等情况 |  |
| `pause` | `(String key)` | `void` | 暂停下载任务 |  |
| `stop` | `(String key)` | `void` | 停止下载任务 |  |
| `delete` | `(String key)` | `void` | 删除下载任务 |  |
| `checkFileExist` | `(String key)` | `boolean` | 检查文件是否存在 | true 存在，false 不存在 |
| `checkFileComplete` | `(String key)` | `boolean` | 检查文件是否下载完成 | true 已完成，false 未完成 |
| `getDownloadInfo` | `(String key)` | `String` | 获取下载文件信息 |  |

##### DownloadListener（com.ssnwt.vr.download.DownloadListener）

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onTotalSize` | `String appKey, long bytes` | 获取文件总大小，单位 Byte |
| `onDownloadStarted` | `String appKey` | 下载开始 |
| `onDownloadProgress` | `String appKey, int progress` | 下载进度，progress 取值 0-100 |
| `onDownloadProgress` | `String appKey, int progress, long speed` | 带下载速度的进度回调，progress 取值 0-100 |
| `onDownloadCompleted` | `String appKey, String apkPath` | 下载完成，返回文件路径 |
| `onStateChanged` | `String appKey, int state` | 任务状态变化：-1 unknown，0 pending，1 downloading，2 paused，3 downloaded，4 error |
| `onError` | `String appKey` | 下载出错 |

### 2.12 FotaUtils（系统升级）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getFotaUtils()`

系统升级（FOTA）接口：检查新版本、下载升级包、安装升级包，并查询升级状态、包大小与已下载大小。升级过程与结果通过 `FotaListener` 回调。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(FotaListener listener)` | `void` | 设置升级监听器，回调 FOTA 包版本与下载进度 |  |
| `checkUpdate` | `()` | `void` | 检查 FOTA 更新 |  |
| `goToDownload` | `()` | `void` | 检测到新版本后开始下载升级包 |  |
| `isDownloading` | `()` | `boolean` | 是否正在下载 |  |
| `isDownloadingFinished` | `()` | `boolean` | 下载是否已完成 |  |
| `hasNewVersion` | `()` | `boolean` | 是否有新版本 |  |
| `getUpgradeInfo` | `()` | `UpgradeInfo` | 获取升级信息 | 返回 `com.ssnwt.vr.androidmanager.fota.UpgradeInfo` |
| `getState` | `()` | `int` | 获取升级状态 | 0 Idle，1 Downloading，2 Installing，3 Installed |
| `getPackageSize` | `()` | `int` | 获取升级包大小 | 单位 KB |
| `getDownloadedSize` | `()` | `int` | 获取已下载大小 | 单位 B |
| `installPackage` | `()` | `void` | 安装 FOTA 升级包 |  |
| `goToFotaForceUpdate` | `(String filePath)` | `void` | 将本地文件复制到安装目录，进行强制升级 |  |

##### FotaListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onVersion` | `boolean hasNewVersion, boolean forceUpdate, String pkgVersion, String description` | 是否检测到新版本，是否强制更新，以及 FOTA 包版本与描述 |
| `onVersionError` | `boolean hasNewVersion, int errorCode` | 版本检查出错，errorCode 为 8 表示空间不足 |
| `onDownloadProgress` | `int progress` | 下载进度，取值 0-100 |
| `onDownloadError` | `int errCode` | 下载失败，errCode：0 存储空间不足，1 网络错误 |
| `onDownloadFinish` | 无 | 新 FOTA 包下载完成 |
| `onInstallProgress` | `int progress` | 安装进度，取值 0-100 |
| `onInstallError` | `int errCode` | 安装失败，errCode：0 存储空间不足，1 网络错误 |
| `onInstallError` | `int errCode, boolean isUserLocalUpgrade` | 安装失败，isUserLocalUpgrade 为 true 表示用户手动拷贝的包，false 表示远程下载的包 |
| `onInstallFinish` | 无 | FOTA 包安装完成 |
| `onInstallFinish` | `boolean isUserLocalUpgrade` | FOTA 包安装完成，isUserLocalUpgrade 为 true 表示用户手动拷贝的包，false 表示远程下载的包 |

### 2.13 UpgradeInfo（升级包信息）

包名：`com.ssnwt.vr.androidmanager.fota`

由 `FotaUtils.getUpgradeInfo()` 返回；实现 `Parcelable`，经 AIDL 跨进程传输。

FOTA 升级包信息数据类，承载服务端下发的升级包元数据（版本、下载地址、MD5、大小、强制升级标志等），字段通过 getter/setter 读写。

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `int` | 升级包记录唯一标识 |
| `devicetype` | `String` | 设备类型 |
| `customerid` | `String` | 客户 ID |
| `hardversion` | `String` | 硬件版本 |
| `allsoft` | `int` | 是否整机（全量）软件升级标志 |
| `isPublish` | `int` | 是否已发布标志 |
| `currnumber` | `int` | 当前已升级数量 |
| `maxnumber` | `int` | 可升级数量上限 |
| `releasetime` | `long` | 升级包发布时间戳 |
| `storeFileName` | `String` | 本地存储文件名 |
| `pkgurl` | `String` | 升级包下载地址 |
| `pkgmd5` | `String` | 升级包 MD5 校验值 |
| `pkgversion` | `String` | 升级包版本号 |
| `pkgcndesc` | `String` | 升级包中文描述 |
| `pkgendesc` | `String` | 升级包英文描述 |
| `pkgdesc` | `String` | 升级包描述 |
| `forceupgrade` | `int` | 是否强制升级标志 |
| `pkgtype` | `int` | 升级包类型 |
| `pkgsize` | `long` | 升级包大小，单位字节 |
| `committime` | `long` | 提交时间戳 |
| `modifytime` | `long` | 最后修改时间戳 |

> ⚠️ 跨进程（Parcel）传输不保留 `pkgendesc`（英文字段描述），仅同进程直传对象时有效。

##### 辅助方法

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `equals` | `(Object o)` | `boolean` | 业务比对：`pkgmd5`、`pkgurl`（均忽略大小写）与 `forceupgrade` 三者均相同则返回 true | 未标注 `@Override`，且未重写 `hashCode`，用作 `HashSet`/`HashMap` 元素时行为与常规对象不同；注意其语义为业务比对，并非全字段比对 |
| `isIDEqual` | `(Object o)` | `boolean` | 判断传入对象的 `id` 是否与本对象相同 | 非 `UpgradeInfo` 类型返回 false |
| `isJustFroceNotEqual` | `(Object o)` | `boolean` | 判断 md5 与 url 均相同、但强制升级标志不同（仅强制升级标记发生变化） | 方法名拼写为 `Froce`（应为 Force），历史拼写保留 |
| `isEqual` | `(String str1, String str2)` | `boolean` | 静态工具方法，忽略大小写比较两个字符串 | `static`；`str1` 为 null 时返回 false，两者均为 null 亦返回 false |

### 2.14 HandshankUtils（手柄）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getHandshankUtils()`

手柄（控制器）管理接口：支持左右手柄的配对/解绑、连接状态查询、电量获取、MAC 地址与版本号读取、进入 DFU 模式与固件升级，并提供配对、电量、按键/摇杆/触摸/霍尔、连接状态等事件监听。除特别说明外，`lr` 参数取值为 0 表示左手柄、1 表示右手柄。

> ⚠️ `getExtra` 与 `setExtra` 仅支持 ES202(腕部相机) 设备；`getControllerVersion`、`getControllerMac` 的 `lr` 另支持 -1 表示主机。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(HandshankListener listener)` | `void` | 设置手柄配对/解绑事件监听 |  |
| `isBond` | `(int lr)` | `boolean` | 手柄是否已配对 | true 已配对，false 未配对 |
| `bond` | `(int lr)` | `void` | 配对（绑定）手柄 |  |
| `unbond` | `(int lr)` | `void` | 解绑手柄 |  |
| `isConnect` | `(int lr)` | `boolean` | 手柄是否已连接 | true 已连接，false 未连接 |
| `getControllerVersionAsync` | `(int lr, HandshankVersionListener listener)` | `void` | 异步获取控制器版本号 | `lr` 支持 -1 表示主机 |
| `getBatteryLevelAsync` | `(HandshankBatteryListener listener)` | `void` | 异步获取手柄电量，会轮询最多 10 次，直到获取到的不为 0 | 请务必为每次调用创建新的 listener 对象 |
| `getControllerMac` | `(int lr)` | `String` | 获取控制器 MAC 地址 | `lr` 支持 -1 表示主机；同步调用 |
| `getControllerMacAsync` | `(int lr, HandshankMacListener listener)` | `void` | 异步获取 MAC 地址，会轮询最多 3 次，直到获取到的不为空 | `lr`：0 左手柄，1 右手柄，-1 主机 |
| `getExtra` | `(int code, int lr)` | `String` | 查询 ES202(腕部相机) 扩展信息 | ⚠️ 仅支持 ES202(腕部相机) 设备；`code` 取 `C.EXTRA_GET_MAC` 或 `C.EXTRA_GET_STATUS`；返回 MAC 字符串或十进制状态值，失败/不支持返回 "" |
| `setExtra` | `(int code)` | `void` | 控制 ES202(腕部相机) 设备 | ⚠️ 仅支持 ES202(腕部相机) 设备；`code` 掩码见 `C.EXTRA_SET_*`，无返回 |
| `setExtra` | `(int code, String value)` | `void` | 控制 ES202(腕部相机) 扩展节点（带 value 形式，与 `setExtra(int)` 共用命令码、服务端按参数个数区分） | ⚠️ 仅支持 ES202(腕部相机) 设备；`code` 取 `C.EXTRA_SET_WIFI_NODE`（`value` "1" 开配网窗口 / "0" 关）或 `C.EXTRA_SET_TRANSFER_NODE`（`value` 为 4 字节空格分隔 hex，如 "DE 35 00 0B"）；掩码（`C.EXTRA_SET_*` 低字节）亦可传但应走 `setExtra(int)`；`value` 传 null 退化为单参数旧形式（线上传输与旧版逐字节一致） |
| `setBatteryListener` | `(HandshankBatteryListener listener)` | `void` | 监听手柄电量变化 |  |
| `initBatteryListenerNative` | `(long handle)` | `void` | 辅助 Native 设置 HandshankBatteryListener | 供 JNI 使用；内部同时调用 `getBatteryLevelAsync` 与 `setBatteryListener` |
| `enterDFUMode` | `(int lr)` | `void` | 进入 DFU 模式 |  |
| `checkNewVersion` | `(HandShankCheckListener listener)` | `void` | 检查手柄是否有新版本固件 | 检查结果取值见 `onCheckInfoResult` 说明 |
| `requestUpgrade` | `(HandshankUpgradeListener listener)` | `void` | 开始升级手柄 |  |
| `startUpgrade` | `(String deviceVersion, String deviceDfuPath, String hostVersion, String hostDfuPath, HandshankUpgradeListener listener)` | `void` | 开始固件升级 | `deviceVersion` 手柄版本，`deviceDfuPath` 手柄固件文件路径，`hostVersion` 手柄主机版本，`hostDfuPath` 手柄主机固件文件路径 |
| `cancelFotaThenCheck` | `()` | `void` | 取消 FOTA 升级后重新检查手柄 | 手柄检测弹窗优先级低于 FOTA 升级，在 FOTA 升级取消时调用 |
| `setControllerListener` | `(HandshankControllerListener listener)` | `void` | 设置手柄控制器事件监听 | 传 null 可清除监听 |
| `setConnectListener` | `(HandshankConnectListener listener)` | `void` | 注册手柄连接状态监听（连接/断开事件，回调 ON_CONNECT_CHANGED=605） | ⚠️ 需设备端 svrservice 同步支持 `HANDSHANK_SET_CONNECT_LISTENER(2318)`，否则该监听不生效；传 null 反注册 |

##### HandshankListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onBond` | `int lr` | 配对成功 |
| `onUnbond` | `int lr` | 解绑成功 |
| `onBondError` | `int lr` | 配对失败 |
| `onUnbondError` | `int lr` | 解绑失败 |

##### HandshankBatteryListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onBatteryChanged` | `int l, int r` | l 左手柄电量，r 右手柄电量，-1 表示未连接状态 |

##### HandshankMacListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onReceiveMac` | `String mac` | 异步获取到 MAC 地址 |

##### HandshankVersionListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onReceiveVersion` | `String version` | 异步获取到控制器版本号 |

##### HandshankUpgradeListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onSuccess` | 无 | 升级成功 |
| `onError` | `int code` | 升级失败，返回错误码 |

##### HandShankCheckListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onCheckInfoResult` | `int result, String updateInfo` | 版本检查结果：0 没有更新，1 有更新非强制，2 有更新且强制，-1 检查失败；updateInfo 为更新描述信息 |

##### HandshankConnectListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onConnectChanged` | `int lr, boolean connect` | 手柄连接状态变化，connect 为 true 表示已连接，false 表示已断开 |

##### HandshankControllerListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onControllerKeyEvent` | `int controllerId, int keyCode, KeyEvent event` | 按键事件，controllerId 0 左手 / 1 右手，event 为 ACTION_DOWN 或 ACTION_UP |
| `onControllerTouchEvent` | `int controllerId, int keyCode, boolean touched` | 触摸事件，keyCode 为被触摸按钮的 keyCode，touched 为 true 表示触摸中 |
| `onControllerThumbstickEvent` | `int controllerId, float x, float y` | 摇杆事件，x/y 为摇杆坐标 |
| `onControllerHallEvent` | `int controllerId, float trigger, float grip` | 霍尔事件，trigger 为扳机值，grip 为握持值 |

另提供公开嵌套类 `HandshankUtils.HandshankBatteryListenerNative`（构造方法 `HandshankBatteryListenerNative(long handle)`），用于将电量回调转发到 Native 层；以及公开的回调委托类 `HandshankMacListenerDelegate`、`HandshankVersionListenerDelegate`，通常无需直接使用。

### 2.15 IntentUtils（Intent）

包名：`com.ssnwt.vr.androidmanager.intent`

获取方式：`AndroidInterface.getInstance().getIntentUtils()`

Intent 相关接口。通过 `setListener` 注册回调，用于接收 Intent 启动的结果通知。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(IntentListener listener)` | `void` | 设置 Intent 启动回调监听器 |  |

##### IntentListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onStartIntent` | `(int result, String paramInfo)` | Intent 启动回调；`result` 为调用结果，`paramInfo` 为 Intent 参数信息 |

### 2.16 ProximitySensorUtils（距离传感器）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getProximitySensorUtils()`

距离传感器相关接口。用于添加/移除距离传感器监听器，在遮挡物远离或靠近传感器时收到回调。支持多监听器，内部使用线程安全列表管理。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `addSensorListener` | `(SensorListener sensorListener)` | `void` | 添加距离传感器监听器 | 重复添加同一监听器不会生效；首个监听器加入时自动向 SvrService 注册传感器 |
| `removeSensorListener` | `(SensorListener sensorListener)` | `void` | 移除距离传感器监听器 | 监听器列表为空时自动反注册传感器，以节省功耗 |

##### SensorListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onDistanceFar` | `boolean far` | true 5cm以外; false 5cm以内 |

### 2.17 StorageUtils（存储）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getStorageUtils()`

存储相关接口。用于查询数据目录、指定路径目录及外置 SD 卡的容量与路径，并可监听 SD 卡插入/移除事件。所有容量接口返回值单位为 Mb。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(StorageListener listener)` | `void` | 设置监听器，监听 SD 卡插入/移除变化 |  |
| `getTotalStorageSize` | `()` | `int` | 获取数据目录的总存储容量 | 单位 Mb |
| `getUsedStorageSize` | `()` | `int` | 获取数据目录的已用存储容量 | 单位 Mb |
| `getTotalStorageSize` | `(String path)` | `int` | 获取指定目录的总存储容量 | 单位 Mb；path 为空或目录不存在时返回 -1 |
| `getUsedStorageSize` | `(String path)` | `int` | 获取指定目录的已用存储容量 | 单位 Mb；path 为空或目录不存在时返回 -1 |
| `getSDCardPath` | `()` | `String` | 获取外置 SD 卡路径 |  |
| `getAllSDCardPath` | `()` | `String[]` | 获取所有外置 SD 卡路径 |  |

##### StorageListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onSDCardInsert` | `String path` | SD 卡插入，返回插入卡的路径 |
| `onSDCardRemove` | `()` | SD 卡移除 |

### 2.18 SpeedTestUtils（网络测速）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getSpeedTestUtils()`

网络测速接口。提供默认参数测速、自定义测试时间测速以及完整自定义参数测速三种方式，测速过程中的实时速度、最终速度与错误信息通过 `SpeedListener` 回调返回。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `test` | `(String ip, SpeedListener listener)` | `void` | 默认测速 | 默认回显时间 1s、测试 10s、不限速 |
| `test` | `(int t, String ip, SpeedListener listener)` | `void` | 自定义测试时间测速 | `t` 为测试时间，单位 s |
| `test` | `(int i, int t, String b, boolean R, String ip, SpeedListener listener)` | `void` | 自定义测速参数测速 | `i` 回显间隔（单位 s）；`t` 测试时间（单位 s）；`b` 限速，比如 7M（单位 mb/s）；`R` 反向测速 |

##### SpeedListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onSpeed` | `String speed` | 实时速度，单位 M/B |
| `onResult` | `String speed` | 最终速度，单位 M/B |
| `onError` | `String error` | 测速错误回调，参数为错误信息 |

接口内另定义回调 ID 常量 `CALLBACK_ID_ON_SPEED`（值 1）、`CALLBACK_ID_ON_ERROR`（值 2）、`CALLBACK_ID_ON_RESULT`（值 3），供 SDK 内部回调分派使用，一般无需关注。

### 2.19 TimeUtils（时间）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getTimeUtils()`

时间相关接口。用于获取当前时间及时间格式，设置时间制（12/24 小时）、系统时间、时区以及是否自动获取网络时间。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `getCurrTime` | `()` | `String` | 获取当前时间，格式 yyyy-MM-dd HH:mm:ss |  |
| `getCurrTimeWithoutYear` | `()` | `String` | 获取当前时间，格式 HH:mm，不含年月日 | 受 24 小时/12 小时制影响 |
| `is24Hour` | `()` | `boolean` | 获取当前是否是 24 小时时间制格式 | true：是24小时；false：12小时 |
| `set24Hour` | `(boolean is24Hour)` | `void` | 设置时间制格式 |  |
| `setCurrentTime` | `(String time)` | `void` | 设置当前时间 |  |
| `setCurrentTimezone` | `(String timezone)` | `void` | 设置当前时区 |  |
| `setAutoTime` | `(boolean enable)` | `void` | 设置是否自动获取网络时间 |  |
| `isAutoTime` | `()` | `boolean` | 获取是否自动获取网络时间 |  |

### 2.20 VolumeUtils（音量）

包名：`com.ssnwt.vr.androidmanager`

获取方式：`AndroidInterface.getInstance().getVolumeUtils()`

音量相关接口。提供媒体音量、通话音量与系统音量的获取与设置，以及音量变化监听。媒体音量默认最大值为 15。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener` | `(VolumeListener listener)` | `void` | 设置音量变化监听器 |  |
| `getMaxVolume` | `()` | `int` | 获取媒体音量最大值 | 默认 15 |
| `getCurrentVolume` | `()` | `int` | 获取当前媒体音量 | 取值范围 0 ~ getMaxVolume() |
| `setVolume` | `(int volume)` | `void` | 设置媒体音量 | 取值范围 0 ~ getMaxVolume() |
| `getCurrentVolumeS` | `()` | `String` | 以字符串形式获取当前音量值 |  |
| `setVolumeF` | `(float volume)` | `void` | 以浮点值设置音量 |  |
| `setVolumeWithUI` | `(int volume)` | `void` | 设置媒体音量并弹出系统音量 UI | 取值范围 0 ~ getMaxVolume() |
| `getMaxVoiceCallVolume` | `()` | `int` | 获取通话音量最大值 |  |
| `getCurrentVoiceCallVolume` | `()` | `int` | 获取当前通话音量值 |  |
| `setVoiceCallVolume` | `(int volume)` | `void` | 设置通话音量值 |  |
| `getMaxSystemVolume` | `()` | `int` | 获取系统音量最大值 |  |
| `getCurrentSystemVolume` | `()` | `int` | 获取当前系统音量值 |  |
| `setSystemVolume` | `(int volume)` | `void` | 设置系统音量值 |  |

##### VolumeListener

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onVolumeChanged` | `()` | 音量发生变化时回调 |

### 2.21 WifiUtils（Wi-Fi）

包名：`com.ssnwt.vr.androidmanager.wifi`

获取方式：`AndroidInterface.getInstance().getWifiUtils()`

Wifi 管理接口。提供 Wifi 开关与搜索、网络的添加/连接/断开/忘记、已保存密码查询、信号等级查询、默认国家码设置、Wi-Fi 热点开关，以及连接状态与搜索结果回调等能力。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setListener2` | `(WifiListener2 listener)` | `void` | 设置 wifi 连接断开等回调监听器 | 推荐使用的回调接口 |
| `isOpenWifi` | `()` | `boolean` | Wifi 当前是否已打开 | true-已打开，false-已关闭 |
| `openWifi` | `()` | `void` | 请求打开 Wifi |  |
| `closeWifi` | `()` | `void` | 请求关闭 Wifi |  |
| `searchWifi` | `()` | `boolean` | 请求搜索 Wifi | 搜索结果通过 `WifiListener2.onSearchResult` 回调 |
| `addNetwork` | `(String ssid, int security, String password)` | `int` | 添加 Wi-Fi 网络，仅支持 OPEN 或 WPA PSK 网络 | ssid 为网络名称；security 为加密方式：0-NONE，1-WEP，2-PSK；password 为密码 |
| `connectWifi` | `(String ssid, String bssid, String capabilities, String password)` | `int` | 使用密码连接 Wifi |  |
| `connectWifi` | `(int nid)` | `void` | 连接已保存的 Wifi | nid 为 networkId |
| `disconnectWifi` | `()` | `void` | 断开当前已连接的 Wifi |  |
| `disconnectWifi` | `(int nid)` | `void` | 按 networkId 断开已连接的 Wifi |  |
| `forget` | `(int nid)` | `void` | 忘记 Wifi 密码 | nid 为 networkId |
| `saveWifi` | `(WifiConfiguration info, SaveWifiListener listener)` | `void` | 保存 Wifi 配置 | 结果通过 `SaveWifiListener` 异步回调 |
| `forget` | `()` | `void` | 忘记当前已连接 Wifi 的密码 |  |
| `getCurrentNetworkID` | `()` | `int` | 获取当前已连接网络的 networkId |  |
| `getConnectedWifi2` | `()` | `WifiInfo` | 获取已连接 Wifi 的信息对象 | 服务端返回 JSON 字符串后解析为 `WifiInfo` |
| `getWifiRssiLevel` | `()` | `int` | 获取当前 Wifi 信号等级 | 0-3，0 无信号，3 信号最强 |
| `getWifiPassword` | `(String ssid)` | `String` | 获取已保存的 wifi 密码 |  |
| `setDefaultCountryCode` | `(String country)` | `void` | 设置默认 wifi 国家码 |  |
| `startHotspot` | `(String ssid, String password, int security)` | `boolean` | 开启 Wi-Fi 热点 | ssid 为热点名称；password 加密时需要且至少 8 位，开放模式可传 null；security 取值见公开常量；true 表示调用成功 |
| `stopHotspot` | `()` | `boolean` | 关闭 Wi-Fi 热点 | true 表示调用成功 |
| `isHotspotEnabled` | `()` | `boolean` | 查询 Wi-Fi 热点是否已开启 | true 表示热点已开启 |
| `connectWifi` | `(Context context, String ssid, String password)` | `void` | 直接以 Intent（`svr.intent.action.CONNECT_WIFI`）方式请求连接 Wifi | 静态方法，不经过 SvrManager；通过 `ssid` / `password` 两个 extra 传给 `com.ssnwt.vr.server/com.ssnwt.vr.svrservice.SvrService` |

##### 公开常量

| 常量 | 值 | 说明 |
| --- | --- | --- |
| `SECURITY_OPEN` | `0` | 开放热点（无密码） |
| `SECURITY_WPA2` | `2` | WPA2-PSK 加密热点 |

##### 枚举：WifiConnectionState

Wifi 连接状态。用于 `WifiListener2.onConnecting` 的 status 参数。

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `IDLE` | `0` | 空闲状态 |
| `DISCONNECTED` | `1` | 已断开 |
| `CONNECTING` | `2` | 连接中 |
| `CONNECTED` | `3` | 已连接 |
| `PASSWORD_ERROR` | `4` | 密码错误 |
| `NOT_SUPPORT` | `5` | 不支持 |
| `FORGET` | `6` | 已忘记网络 |
| `START_CONNECT` | `7` | 开始连接 |
| `FORGET_FAIL` | `8` | 忘记网络失败 |
| `CONNECT_FAIL` | `9` | 连接失败 |
| `ADD_NETWORK_FAIL` | `10` | 添加网络失败 |

##### 接口：WifiListener2

Wifi 状态回调接口，通过 `setListener2` 注册。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onOpened` | `(boolean open)` | wifi 开关状态变化；true-已打开，false-已关闭 |
| `onConnecting` | `(int status, String ssid)` | 连接状态变化；status 为 `WifiConnectionState` 的序号，ssid 为 wifi 名称 |
| `onSearchResult` | `(ArrayList<WifiInfo> wifiList)` | 搜索结果回调，需先调用 `searchWifi()`；wifiList 为 `WifiInfo` 列表 |
| `onRssiLevelChanegd` | `(int level)` | 当前 wifi 信号强度等级变化（方法名拼写保持原样） |

##### 接口：SaveWifiListener

保存 Wifi 配置的回调接口，用于 `saveWifi`。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onSuccess` | `()` | 保存成功 |
| `onFailed` | `()` | 保存失败 |

### 2.22 WifiInfo（Wi-Fi 信息）

包名：`com.ssnwt.vr.androidmanager.wifi`

获取方式：由 `WifiUtils` 接口返回，例如 `WifiUtils.getConnectedWifi2()`，或 `WifiListener2.onSearchResult(ArrayList<WifiInfo>)` 回调

Wifi 信息数据类。描述一个 Wi-Fi 网络（含名称、MAC、频段、信号强度、加密方式与状态），支持 JSON 序列化与反序列化。所有字段均通过 getter 访问。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `parseFromJson` | `(String json)` | `WifiInfo` | 将 JSON 字符串解析为 WifiInfo 对象 | 静态方法 |
| `getSSID` | `()` | `String` | 获取 wifi 名称 |  |
| `getBSSID` | `()` | `String` | 获取 wifi mac 地址 |  |
| `getNetworkID` | `()` | `int` | 获取 wifi id | 仅已连接的网络有该值 |
| `get5G` | `()` | `boolean` | 是否为 5G 频段 |  |
| `getFrequency` | `()` | `int` | 获取频率 | 2.4G(2400) / 5G(5000+) / 6G(5925+) |
| `getRssi` | `()` | `int` | 获取当前 wifi 信号强度 | 取值区间 (-128, 0) |
| `getRssiLevel` | `()` | `int` | 获取当前 wifi 信号等级 | 默认 0-3 |
| `getWifiStatus` | `()` | `int` | 获取 wifi 状态 | 取值为 `WifiStatus` 的序号 |
| `getCapabilities` | `()` | `String` | 获取 wifi 加密模式（原始 capabilities 字符串） |  |
| `getSecurity` | `()` | `String` | 获取加密类型 | 取值见公开常量 `TYPE_SECURITY_*` |
| `setSecurity` | `(WifiConfiguration config)` | `void` | 通过 WifiConfiguration 类型的对象设置密码类型 | 依次判断 SAE / WPA_PSK / WPA_EAP 或 IEEE8021X / WEP，均不匹配则为 NONE |
| `isNeedPassword` | `()` | `boolean` | 是否需要密码 |  |
| `toString` | `()` | `String` | 输出频段、rssi、等级、networkId、状态、加密方式与 SSID(BSSID) 的拼接文本 | 重写 `Object.toString` |
| `equals` | `(Object obj)` | `boolean` | 与另一个 WifiInfo 按 SSID 判等 | 重写 `Object.equals` |
| `toJsonString` | `()` | `String` | 将当前对象序列化为 JSON 字符串 |  |

##### 公开常量

| 常量 | 值 | 说明 |
| --- | --- | --- |
| `TYPE_SECURITY_WEP` | `"WEP"` | WEP 加密标识 |
| `TYPE_SECURITY_PSK` | `"PSK"` | WPA/WPA2-PSK 加密标识 |
| `TYPE_SECURITY_EAP` | `"EAP"` | EAP / 802.1X 加密标识 |
| `TYPE_SECURITY_SAE` | `"SAE"` | WPA3-SAE 加密标识 |
| `TYPE_SECURITY_NONE` | `"NONE"` | 无加密标识，也是默认值 |
| `MIN_FREQ_5G` | `5000` | 5G 频段起始频率（MHz） |
| `MIN_FREQ_6G` | `5925` | 6G 频段起始频率（MHz） |

##### 枚举：WifiStatus

Wifi 状态。用于 `getWifiStatus()` 的返回值。

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `Enabled` | `0` | 已启用（默认值） |
| `Saved` | `1` | 已保存 |
| `Using` | `2` | 正在使用 |

### 2.23 GlobalWindowUtils（全局窗口）

包名：`com.ssnwt.vr.globalwindow`

获取方式：`AndroidInterface.getInstance().getGlobalWindowUtils()`（类提供公开无参构造，但请统一通过该 getter 获取实例）

全局菜单。提供系统内置全局窗口的显示与隐藏、各类对话框、Toast、KIOSK 模式以及触摸与焦点事件注入等能力。

> ⚠️ 全局菜单仅支持 VQ920 / VQ930 设备。

> ⚠️ 必须先通过 `setGlobalWindowListener` / `setCustomWindowListener` 注册监听，才能进行全局窗口调用。

**窗口显示流程：**`requestShow` 返回窗口的唯一标志 id；系统准备好渲染目标后回调监听器 —— 内置窗口走 `GlobalWindowListener.onRequestShow(GlobalWindowInfo)`，自定义窗口走 `CustomGlobalWindowListener.onShow(int id, Surface surface)`，自定义窗口需在回调中调用 `CustomGlobalWindow.show(surface)` 把内容渲染到该 `Surface`。也可直接调用 `show(int id, Surface surface)` 手动指定目标 Surface 完成显示。

内部回调分发类 `GlobalWindowListenerDelegate` / `CustomWindowListenerDelegate` / `KioskListenerDelegate` 及其回调常量（`ON_REQUEST_SHOW=1`、`ON_HIDE=2`、`HOME_EVENT=3`、`ACTION_EVENT=4` 等）仅用于框架内部消息路由，外部无需掌握，注册监听器即可收到对应回调。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `setCustomWindowListener` | `(CustomGlobalWindowListener listener)` | `void` | 第三方应用必须先设置 CustomWindowListener 才能进行全局窗口调用 |  |
| `setGlobalWindowListener` | `(GlobalWindowListener listener)` | `void` | 当前应用必须先设置 GlobalWindowListener 才能进行全局窗口调用 |  |
| `requestHomeEvent` | `(int type)` | `void` | Home 事件，type 取值：0 hide、1 show |  |
| `requestKioskEvent` | `()` | `void` | 组合键，开启 KIOSK 模式 |  |
| `requestAllMenu` | `()` | `void` | 组合键，显示全局菜单所有功能 |  |
| `requestActionEvent` | `(int type, int action)` | `void` | type 为界面类型，action 为操作类型 | 取值分别见 `GlobalWindowType` 与 `GlobalActionType` |
| `initGlobalWindowNative` | `(long handle)` | `void` | 辅助 Native 设置 GlobalWindowListener，handle 为 native 中上下文标识 | 仅供 JNI 使用 |
| `requestShow` | `(@GlobalWindowType int type)` | `int` | 请求显示 GlobalWindowType 中的某个窗口，返回生成的窗口唯一标志 id | 部分需要特殊参数的窗口有单独接口，如 `toast(String, int)` |
| `requestShow` | `(CustomGlobalWindow globalWindow)` | `int` | 请求显示自定义窗口，返回窗口唯一标志 id |  |
| `addOverrideCustomView` | `(CustomGlobalWindow globalWindow)` | `int` | 设置相同 type 的全局弹窗，即以自定义视图覆盖同类型窗口 |  |
| `show` | `(int id, Surface surface)` | `void` | 把指定窗口显示到给定的 Surface 上 |  |
| `requestHide` | `(int id)` | `void` | 请求隐藏某个当前正在显示的窗口 |  |
| `setVisibility` | `(int id)` | `void` | 请求某个当前可见窗口 | 语义为使窗口可见 |
| `setInVisibility` | `(int id)` | `void` | 请求某个当前不可见窗口 | 语义为使窗口不可见 |
| `injectTouchEvent` | `(int id, MotionEvent event)` | `void` | 为窗口注入 MotionEvent |  |
| `updateFocusPosition` | `(int id, int x, int y)` | `void` | 设置 hover 效果位置 |  |
| `performClickDown` | `(int id, int x, int y)` | `void` | 注入 motion down 事件 |  |
| `performActionMove` | `(int id, int x, int y)` | `void` | 注入 motion ACTION_MOVE 事件 |  |
| `performClickUp` | `(int id, int x, int y)` | `void` | 注入 motion up 事件 |  |
| `toast` | `(String content)` | `void` | 显示 toast，默认显示 3s |  |
| `toast` | `(String content, int millisecond)` | `void` | 显示 toast，自定义显示时长，单位 ms |  |
| `showRelocateSuccessSafeArea` | `(OnConfirmListener listener)` | `int` | 显示重定位安全区成功全局窗口 |  |
| `showResetSuccessSafeArea` | `(OnConfirmListener listener)` | `int` | 显示重置安全区成功全局窗口 |  |
| `showConfirmDialog` | `(ActionListener listener)` | `int` | 显示确认对话框 |  |
| `showConfirmDialog` | `(ActionListener listener, List<String> strings)` | `int` | strings 依次为 [title，tips，buttonText]，必须顺序设置；中间项不需要时可传空串，如 ["title","","buttonText"]；第四个参数为 "-" 则不退出当前应用 | 参数顺序为强约定 |
| `showUninstallDialog` | `(ActionListener listener, String pkg)` | `int` | 显示卸载对话框，pkg 为目标包名 |  |
| `showDeleteDialog` | `(ActionListener listener)` | `int` | 显示删除对话框 |  |
| `showKioskDialog` | `(ActionListener listener)` | `int` | 显示 KIOSK 模式对话框 |  |
| `showSpaceOrientationDialog` | `(ActionListener listener)` | `int` | 显示空间朝向对话框 |  |
| `showNoteNoSlamDialog` | `(ActionListener listener)` | `int` | 显示无 SLAM 定位提示对话框 |  |
| `showHandShakeDialog` | `(ActionListener listener, String type, String updateStr)` | `int` | 显示手柄震动与升级相关对话框 |  |
| `showSystemUpdateDialog` | `(String params)` | `void` | 显示系统升级对话框，params 参数透传 |  |
| `nativeShowRelocateSuccessSafeArea` | `(long handle)` | `int` | 用于 native 调用显示重定位安全区成功全局窗口 | 仅供 JNI 使用 |
| `nativeShowResetSuccessSafeArea` | `(long handle)` | `int` | 用于 native 调用显示重置安全区成功全局窗口 | 仅供 JNI 使用 |
| `setKioskListener` | `(KioskListener listener)` | `void` | 设置 KIOSK 请求回调监听 |  |
| `setKioskApp` | `(String pkg, boolean value)` | `void` | 设置指定应用是否允许在 KIOSK 模式下使用 |  |
| `setKioskSettings` | `(int settingsType, boolean value)` | `void` | 设置 KIOSK 某项开关配置 |  |
| `getKioskSettings` | `(int settingsType)` | `boolean` | 查询 KIOSK 某项开关配置 |  |
| `getKioskApp` | `(String pkg)` | `boolean` | 查询指定应用是否在 KIOSK 白名单中 |  |
| `inputKioskPwdSuccess` | `()` | `void` | 通知 KIOSK 密码校验成功 |  |
| `setKioskPwdFail` | `()` | `boolean` | 通知 KIOSK 密码校验失败，返回操作结果 |  |
| `canInputKioskPwd` | `()` | `boolean` | 查询当前是否允许输入 KIOSK 密码 |  |

##### 公开字段

| 类型 | 字段 | 说明 |
| --- | --- | --- |
| `boolean` | `isUpdateDialogVisible` | 系统升级对话框当前是否可见，由 SDK 内部更新 |

##### 接口：KioskListener

KIOSK 模式请求回调接口，实现后通过 `setKioskListener` 注册。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `requestKiosk` | `()` | 当系统请求进入 KIOSK 模式时回调 |

### 2.24 CustomGlobalWindow（自定义窗口）

包名：`com.ssnwt.vr.globalwindow`

获取方式：`CustomGlobalUtils.getInstance()`（自定义窗口管理单例）；`CustomGlobalWindow` 为抽象基类，由业务方继承实现

自定义全局窗口方案：业务方继承 `CustomGlobalWindow` 并实现 `initType()` / `initContentView()` / `initInfo()`，内部通过 `VirtualDisplay` 与 `Presentation` 把自定义 View 渲染进全局窗口系统（虚拟屏名称前缀 `CustomGlobalWindow` 供 SurfaceFlinger 判断，不做反畸变等操作）。`CustomGlobalUtils` 维护待显示、正在显示与同类型覆盖三张窗口表，收到系统回调后切到主线程 Handler 驱动对应窗口。

##### CustomGlobalWindow（抽象基类）

> ⚠️ 全局菜单仅支持 VQ920 / VQ930 设备。

> ⚠️ 子类必须实现受保护的抽象方法 `initType()`、`initContentView()`、`initInfo()`，并在构造后调用 `init()` 完成初始化。

**显示流程：**`CustomGlobalUtils.requestShow(window)` 返回窗口 id → 系统回调 `CustomGlobalWindowListener.onShow(int id, Surface surface)` → 框架自动调用 `CustomGlobalWindow.show(surface)` 在该 Surface 上创建虚拟屏并显示内容；`setVisibility()` / `setInVisibility()` 基类为空实现，由 Unity 端控制显隐，子类可覆写以刷新界面数据。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `CustomGlobalWindow` | `(Context context)` | `CustomGlobalWindow` | 构造方法，仅保存 Context，需再调用 `init()` 完成初始化 | 构造方法 |
| `init` | `()` | `CustomGlobalWindow` | 依次执行 `initType()`、`initContentView()` 并测量尺寸、`initInfo()`，返回 this 便于链式调用 |  |
| `show` | `(Surface surface)` | `void` | 在给定 Surface 上创建 VirtualDisplay 并显示内容视图 | display 无效时跳过显示并打日志 |
| `setVisibility` | `()` | `void` | 空实现；因 Unity 端控制显示隐藏，此处需要状态用于刷新界面数据 | 设计为子类覆写 |
| `setInVisibility` | `()` | `void` | 空实现 | 设计为子类覆写 |
| `hide` | `()` | `void` | 销毁 Presentation 并释放虚拟屏 |  |
| `getGlobalWindowInfo` | `()` | `GlobalWindowInfo` | 返回该窗口的窗口描述数据 |  |
| `injectTouchEvent` | `(MotionEvent event)` | `void` | 把触摸事件派发到承载视图 | Presentation 为空时报错返回 |
| `updateFocusPosition` | `(int x, int y)` | `void` | 生成 ACTION_HOVER_MOVE 事件派发，实现 hover 效果 |  |
| `performClickDown` | `(int x, int y)` | `void` | 生成 ACTION_DOWN 事件并派发 |  |
| `performActionMove` | `(int x, int y)` | `void` | 生成 ACTION_MOVE 事件并派发 |  |
| `performClickUp` | `(int x, int y)` | `void` | 生成 ACTION_UP 事件并派发 |  |

##### CustomGlobalUtils（单例）

私有构造，仅能通过 `getInstance()` 获取；内部依赖 `AndroidInterface.getInstance().getGlobalWindowUtils()`，需先完成 AndroidInterface 初始化。该单例在构造时已自动注册 `CustomGlobalWindowListener`，业务方无需重复注册。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `CustomGlobalUtils` | 获取单例，懒加载 | public static synchronized |
| `requestShow` | `(CustomGlobalWindow customGlobalWindow)` | `int` | 请求显示自定义窗口，返回窗口唯一标志 id，并把该窗口存入待显示表 |  |
| `requestHide` | `(int infoId)` | `void` | 请求隐藏指定 id 的自定义窗口 |  |
| `addOverrideCustomView` | `(CustomGlobalWindow globalWindow)` | `void` | 以自定义窗口覆盖同 type 的全局弹窗，并记录到覆盖表 | 返回类型为 void，与 GlobalWindowUtils 同名方法返回 int 不同 |

##### 公开字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `mHandler` | `android.os.Handler` | public final，绑定主线程 Looper。窗口的 show / hide / 事件注入均通过它 post 到主线程执行；调用方也可用于自行 post 主线程任务 |

##### CustomOffscreenPresentation

继承 `android.app.Presentation`，作为虚拟屏上的离屏承载容器，由 `CustomGlobalWindow.show(surface)` 内部创建，业务方通常无需直接使用。其 `onCreate` 中设置透明背景与沉浸式全屏，并把窗口类型设为 `TYPE_PRIVATE_PRESENTATION`（需系统签名或平台权限，失败时仅打印堆栈不抛出）；另提供 `injectTouchEvent(MotionEvent)` 与 `injectGenericMotionEvent(MotionEvent)` 两个方法，分别把事件直接派发到 decorView 的 touch 与 generic motion 通道。构造方法为 `CustomOffscreenPresentation(Context context, Display display)`。

### 2.25 GlobalWindowInfo（窗口参数）

包名：`com.ssnwt.vr.globalwindow`

获取方式：直接使用全参构造方法创建，或由系统回调 `GlobalWindowListener.onRequestShow(GlobalWindowInfo)` 传入

`GlobalWindowInfo` 实现 `android.os.Parcelable`，描述一个全局窗口的完整渲染参数（类型、尺寸、层级、位置、朝向、弧面参数等），是跨进程传递窗口配置的数据类。构造时自动分配唯一自增 id；`isDialog` 没有对应的构造参数，如需使用请在构造后直接赋值。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `GlobalWindowInfo` | `(@GlobalWindowType int type, int width, int height, int scale, String name, int layer, int layerIndex, int faceType, int resolution, float curvatureAngle, int focusType, int anchorType, float[] center, float[] position, float[] rotation)` | `GlobalWindowInfo` | 全参构造方法，逐项写入各字段并自动分配 id（`sID++`） | 参数较多且同型，需严格按序传参；构造方法 |
| `describeContents` | `()` | `int` | 固定返回 0，无特殊文件描述符 | 覆写 Parcelable |
| `writeToParcel` | `(Parcel dest, int flags)` | `void` | 按固定顺序写入全部字段 | 覆写 Parcelable |
| `toString` | `()` | `String` | 输出包含全部字段的可读字符串 | 覆写 Object |

##### 公开字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `sID` | `static int` | 全局自增 id 计数器，初值 0；每个实例构造时取 `sID++` 作为 id。public static，可被外部改写 |
| `type` | `int`（`@GlobalWindowType`） | 窗口类型，取值见 `GlobalWindowType` |
| `id` | `int` | 窗口唯一标识，构造时由 `sID++` 自动分配 |
| `width` | `int` | 窗口宽度 |
| `height` | `int` | 窗口高度 |
| `scale` | `int` | 缩放比例。约定值：TabBar 与 Dialog 为 1400，ShortCut 与 ShutDown 为 1720，toast 为 200，安全区为 600 |
| `name` | `String` | 窗口名称，参与虚拟屏名称拼接 |
| `layer` | `int` | UI 层级，数值越大越在上层 |
| `focusType` | `int` | 是否需要焦点，1 需要焦点，0 不需要 |
| `anchorType` | `int` | 窗口锚定类型：0 固定位置（如 Toast）；1 跟随视角（如安全区提示）；2 近大远小（如主菜单） |
| `layerIndex` | `int` | 同层级下的显示顺序，数值越大越在上层；默认值 0 |
| `faceType` | `int` | 显示 UI 面的类型，暂定平面 0、弧面 1；默认值 0 |
| `isDialog` | `int` | 是否作为对话框，对话框弹出时会隐藏后面所有窗口，1 为是；默认值 0 |
| `center` | `float[]` | UI 坐标的中心位置，归一化坐标，默认以中心为原点。左下角坐标为 (0,0)，右上角为 (1,1)；默认 `{0.5f, 0.5f, 0}` |
| `position` | `float[]` | UI 显示的位置，按 xyz 顺序、相对于系统原点坐标；X 轴左右、Y 轴上下、Z 轴前后；默认 `{0, 0, -2}` |
| `rotation` | `float[]` | UI 的旋转，按 xyz 顺序，对应各轴上的旋转欧拉角；默认 `{0}` |
| `resolution` | `int` | 弧面分段数，弧面显示区域大小与平面共用 width / height 定义；常用 16 段，越多越平滑、性能消耗越高；默认值 0 |
| `curvatureAngle` | `float` | 圆弧突出最高点和平面的夹角，越大越弯曲，0 即为平面；默认值 0 |
| `CREATOR` | `Parcelable.Creator<GlobalWindowInfo>` | public static final，Parcelable 反序列化入口，内部实现 `createFromParcel(Parcel)` 与 `newArray(int)` |

##### GlobalWindowCommon（工具类）

全局窗口公共工具类，提供尺寸测量与 MotionEvent 构造，通常由框架内部调用，业务方一般无需直接使用。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `measureSize` | `(View view)` | `android.util.Pair<Integer, Integer>` | 以 UNSPECIFIED 模式对 View 调 measure，返回测量后的宽高 | public static |
| `generateEvent` | `(int action, float x, float y)` | `MotionEvent` | 以 uptimeMillis 构造 MotionEvent，source 设为 SOURCE_TOUCHSCREEN | public static |

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `TAG` | `static final String` | 日志 TAG，值为类简单名 GlobalWindowCommon |

### 2.26 GlobalWindowType（窗口类型）

包名：`com.ssnwt.vr.globalwindow`

使用方式：作为 `@GlobalWindowType` 注解的取值传入 `GlobalWindowUtils.requestShow(int type)`、`requestActionEvent(int type, int action)` 等接口

`GlobalWindowType` 是全局窗口类型常量注解（`@interface` 配合 `@IntDef`），定义系统内置全局窗口与安全区提示窗口的类型编号。值域分三段：普通窗口 0~25、安全区 101~107、自定义视图 1000。

| 常量 | 值 | 说明 |
| --- | --- | --- |
| `ALL` | 0 | 主要用于一次性操作所有的窗口 |
| `TOAST` | 1 | Toast 提示窗口，scale 为 200 |
| `DIALOG` | 2 | 对话框窗口，scale 为 1400 |
| `TAB_BARS` | 3 | TabBar 窗口，scale 为 1400 |
| `SHORT_CUT` | 4 | 快捷菜单窗口，scale 为 1720 |
| `SHUT_DOWN` | 5 | 关机菜单窗口，scale 为 1720 |
| `MY_APP` | 6 | 我的应用窗口 |
| `UNINSTALL_DIALOG` | 7 | 卸载对话框 |
| `KEY_BOARD` | 8 | 键盘窗口 |
| `LOGIN` | 9 | 登录页窗口 |
| `POLICY` | 10 | 隐私政策窗口 |
| `VOLUME` | 11 | 音量窗口 |
| `PUPIL_DISTANCE` | 12 | 瞳距调节窗口 |
| `SETTINGS` | 13 | 设置窗口 |
| `HAND_SHAKE_DIALOG` | 14 | 手柄震动与升级相关对话框 |
| `UPDATE_DIALOG` | 15 | 升级对话框 |
| `RESET_DIALOG` | 16 | 重置对话框 |
| `RESOURCE_CENTER` | 17 | 资源中心窗口 |
| `EXIT_APP_DIALOG` | 18 | 退出应用对话框 |
| `KIOSK_DIALOG` | 19 | KIOSK 对话框 |
| `SAMBA_DIALOG` | 20 | Samba 对话框 |
| `SPACE_ORIENTATION_DIALOG` | 21 | 空间朝向对话框 |
| `NOTE_NO_SLAM_DIALOG` | 22 | 无 SLAM 定位提示对话框 |
| `TRACKING_LOST` | 23 | 追踪丢失提示窗口 |
| `CREATE_MAP_DIALOG` | 24 | 创建地图对话框 |
| `DISABLE_LARGE_SPACE_DIALOG` | 25 | 禁用大空间对话框 |
| `CUSTOM_VIEW` | 1000 | 自定义视图类型，供第三方自定义窗口使用 |
| `SAFE_AREA_RELOCATE` | 103 | 安全区：正在重新定位，找回安全区 |
| `LEAVE_SAFE_AREA` | 101 | 安全区：找回安全区后，但不在设置的区域内 |
| `SAFE_AREA_RELOCATE_FAIL` | 102 | 安全区：识别失败 |
| `SAFE_AREA_RELOCATE_SUCCESS` | 104 | 安全区：识别成功 |
| `SAFE_AREA_RESET_SUCCESS` | 105 | 安全区：设置成功 |
| `FIND_SAFE_AREA` | 106 | 安全区：寻找安全区 |
| `RESET_BOUNDARY_DIALOG` | 107 | 安全区：二次确认是否要设置安全区 |

### 2.27 GlobalActionType（操作类型）

包名：`com.ssnwt.vr.globalwindow`

使用方式：作为 `@GlobalActionType` 注解的取值传入 `GlobalWindowUtils.requestActionEvent(int type, int action)` 的 action 参数，或从 `GlobalWindowListener.onActionEvent(int type, int action)` 的 action 参数中读取

`GlobalActionType` 是操作触发事件常量注解（`@interface` 配合 `@IntDef`），描述全局菜单、快捷键、TabBar、对话框、键盘、登录页及安全区等各组件的交互事件编号。

> ⚠️ `@IntDef` 白名单只包含 13 个值（ALL_HIDE、EXPAND_OR_HIDE、TAB_BAR_ITEM_CLICK、TAB_BAR_EXIT_APP_CLICK、SHORTCUT_CLOSE、SHORTCUT_ITEM_CLICK、SHORTCUT_ENABLE_VST、APP_EXIT、DIALOG_DISMISS、SHUT_DOWN、REBOOT、CANCEL、MY_APP_EXPAND_OR_HIDE）。其余 16 个已声明常量不在白名单内：运行期仍然可用，但作为该注解参数传入时会触发 lint 告警。

| 常量 | 值 | 说明 |
| --- | --- | --- |
| `ALL_HIDE` | 0 | 隐藏所有窗口 |
| `EXPAND_OR_HIDE` | 10 | TabBar 快捷展开或关闭 |
| `TAB_BAR_ITEM_CLICK` | 11 | TabBar 选项点击后唤醒 dialog |
| `TAB_BAR_EXIT_APP_CLICK` | 12 | 点击关闭当前应用 |
| `MY_APP_EXPAND_OR_HIDE` | 13 | MyApp 快捷展开或关闭 |
| `RESOURCE_EXPAND_OR_HIDE` | 14 | resource_center 展开或关闭 |
| `SETTINGS_EXPAND_OR_HIDE` | 15 | settings 快捷展开或关闭 |
| `SHORTCUT_CLOSE` | 20 | 快捷菜单关闭（隐藏） |
| `SHORTCUT_ITEM_CLICK` | 21 | ShortCut 选项点击后唤醒 dialog |
| `SHORTCUT_ENABLE_VST` | 22 | ShortCut 开启 VST |
| `APP_EXIT` | 30 | 确认退出应用 |
| `DIALOG_DISMISS` | 31 | 对话框消失 |
| `SHUT_DOWN` | 40 | 关机 |
| `REBOOT` | 41 | 重启 |
| `CANCEL` | 42 | 取消 |
| `UNINSTALL_DIALOG_SHOW` | 71 | 显示卸载对话框 |
| `UNINSTALL_DIALOG_DISMISS` | 72 | 卸载对话框消失 |
| `SHOW_KEY_BOARD` | 80 | 键盘显示 |
| `HIDE_KEY_BOARD` | 81 | 隐藏键盘显示 |
| `HIDE_TAB_BAR` | 82 | 隐藏 TabBar |
| `SHOW_TAB_BAR` | 83 | 显示 TabBar |
| `HIDE_SYSTEM_UPDATE` | 84 | 隐藏升级弹框 |
| `HIDE_OR_SHOW_LOGIN` | 91 | 登录页面展开或关闭 |
| `HIDE_LOGIN` | 92 | 登录页面关闭 |
| `FIND_SAFE_AREA_1` | 1 | 以当前边界进入 |
| `HAND_SHAKE` | 131 | 手柄震动 |
| `Boundary_Enable` | 132 | 打开安全区设置 |
| `Boundary_Unable` | 133 | 关闭安全区设置 |
| `START_LAUNCHER` | 141 | 强制手柄升级跳转 |

**使用注意：**本枚举与 `GlobalWindowType` 常作为相邻参数同时出现在 `requestActionEvent(int type, int action)` 中，两者值域有重叠（如都为 1），请勿混用；`Boundary_Enable` / `Boundary_Unable` 采用下划线加首字母大写的命名风格，与其他常量不一致。

### 2.28 全局窗口监听接口

包名：`com.ssnwt.vr.globalwindow`（含子包 `com.ssnwt.vr.globalwindow.listener`）

获取方式：实现下列接口后，分别通过 `GlobalWindowUtils.setGlobalWindowListener` / `setCustomWindowListener` / `setKioskListener` 注册；`ActionListener` 与 `OnConfirmListener` 作为参数直接传入对应的 show 方法

全局窗口对外提供的四个回调接口：`GlobalWindowListener` 接收内置窗口事件，`CustomGlobalWindowListener` 接收自定义窗口事件，`ActionListener` 接收对话框的确认与消失，`OnConfirmListener` 接收安全区窗口的确认。

> ⚠️ 必须先注册监听（`setGlobalWindowListener` / `setCustomWindowListener`），否则全局窗口调用不会收到任何回调。

##### GlobalWindowListener（内置窗口事件）

当前应用侧全局窗口回调接口，通过 `GlobalWindowUtils.setGlobalWindowListener` 注册。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onRequestShow` | `(GlobalWindowInfo info)` | 当请求显示某个全局菜单，info 为请求显示的窗口描述数据 |
| `onHide` | `(int id)` | 当隐藏某个全局菜单，id 为窗口唯一标识 |
| `onHomeEvent` | `(int type)` | 当监听到系统的 home 按键后由 Unity 控制显示与隐藏，防止闪烁；type：0 hide、1 show、2 长按 home（菜单回正） |
| `onActionEvent` | `(int type, int action)` | type 为 UI 类型（见 `GlobalWindowType`），action 为各种类型的点击事件（见 `GlobalActionType`） |

##### CustomGlobalWindowListener（自定义窗口事件）

第三方自定义窗口回调接口，通过 `GlobalWindowUtils.setCustomWindowListener` 注册；`CustomGlobalUtils` 内部已注册一份实现并完成事件分发，通常无需重复注册。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onShow` | `(int id, Surface surface)` | 当请求显示某个全局菜单；surface 为系统提供的渲染目标，自定义窗口需在回调中调用 `CustomGlobalWindow.show(surface)` |
| `onHide` | `(int id)` | 当隐藏某个全局菜单 |
| `setVisibility` | `(int id)` | 请求使该窗口可见 |
| `setInVisibility` | `(int id)` | 请求使该窗口不可见 |
| `injectTouchEvent` | `(int id, MotionEvent event)` | 向该窗口注入触摸事件 |
| `updateFocusPosition` | `(int id, int x, int y)` | 更新 hover 焦点位置 |
| `performClickDown` | `(int id, int x, int y)` | 注入按下事件 |
| `performActionMove` | `(int id, int x, int y)` | 注入移动事件 |
| `performClickUp` | `(int id, int x, int y)` | 注入抬起事件 |
| `requestTypeView` | `(int id)` | 请求指定 type 的自定义 View（框架会将其放入待显示表） |

##### ActionListener（对话框）

监听全局对话框的操作结果，作为 `showConfirmDialog` / `showUninstallDialog` / `showDeleteDialog` / `showKioskDialog` / `showSpaceOrientationDialog` / `showNoteNoSlamDialog` / `showHandShakeDialog` 的参数传入。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `dismiss` | `()` | 对话框消失时回调。默认方法且为空实现，实现方可不覆写 |
| `exit` | `()` | 用户确认退出时回调。抽象方法，实现方必须实现 |

##### OnConfirmListener（安全区确认）

安全区成功窗口的确认回调，作为 `showRelocateSuccessSafeArea` / `showResetSuccessSafeArea` 的参数传入。

| 回调方法 | 参数 | 说明 |
| --- | --- | --- |
| `onConfirm` | `()` | 用户点击确认时回调 |

**Native 桥接类：**`com.ssnwt.vr.globalwindow.GlobalWindowNative` 与 `com.ssnwt.vr.globalwindow.listener.GlobalWindowConfirmListenerNative` 分别是 `GlobalWindowListener` 与 `OnConfirmListener` 的 JNI 桥接实现，把回调转发为 native 方法（`nativeOnRequestShow` / `nativeOnHide` / `nativeOnHomeEvent` / `nativeOnActionEvent` / `nativeOnConfirm`）。它们仅供 JNI 使用，一般由 `GlobalWindowUtils.initGlobalWindowNative(long handle)` 及 `nativeShowRelocateSuccessSafeArea` / `nativeShowResetSuccessSafeArea` 内部创建，Java 层无需直接实例化。

### 2.29 ControllerManager（手柄数据）

包名：`com.ssnwt.vr.svrcontroller`

获取方式：`ControllerManager.getInstance()`

手柄数据索引（当前功能对应手柄数据的偏移），`INDEX_xxx` 为手柄数组的数据定义。单个手柄的数据分组长度为 `GROUP_DATA_SIZE = 30`，通过 `getData(float[])` 取回原始数据后按下列偏移逐项解析。

> ⚠️ 本类加载时会 `System.loadLibrary("svr_controller_v2")`，需设备预置该 native 库与手柄服务（`com.ssnwt.vr.server`）；手柄状态通过 `getData(float[])` 轮询获取，无 Listener 回调。

> ⚠️ 仅支持 3dof 版本设备（V901 / S802 / BQ810 / S801）；6dof 设备（VQ910 / VQ920 / VQ930）请使用 `HandshankUtils`。

##### 数据布局常量

| 常量 | 值 | 说明 |
| --- | --- | --- |
| `GROUP_DATA_SIZE` | `30` | 单个手柄数据分组的数组长度 |
| `INDEX_CONNECT_STATUS` | `0` | 连接状态（`ConnectStatus`），长度 1 |
| `INDEX_TYPE` | `1` | 手柄类型（`Type`），长度 1 |
| `INDEX_HANDNESS` | `2` | 手柄左右手状态（`Handness`），长度 1 |
| `INDEX_RECENTERED` | `3` | 重置视角，1 表示需要重置视角，长度 1 |
| `INDEX_BATTERY` | `4` | 电量 (0-100)，长度 1 |
| `INDEX_ROTATION` | `5` | 方向信息 (x, y, z, w)，长度 4 |
| `INDEX_POSITION` | `9` | 位置信息 (x, y, z)，长度 3 |
| `INDEX_GATEWAY_POS` | `12` | 网关位置 (x, y)，长度 3 |
| `INDEX_BUTTON_STATE` | `15` | 按键信息，按下时为对应键值，允许同时按住多个按键（`KeyCode` 位掩码），长度 2 |
| `INDEX_TOUCH_STATE` | `17` | 触摸板的触摸事件，1 为正在触摸，长度 1 |
| `INDEX_TOUCH_POS` | `18` | 触摸板触摸坐标 (x, y)，取值范围 (0-1)，长度 2 |
| `INDEX_TRIGGER_PROCESS` | `20` | 扳机按下过程，取值范围 (0-1)，长度 1 |
| `INDEX_GRIP_PROCESS` | `21` | GRIP 按下过程，取值范围 (0-1)，长度 1 |
| `INDEX_DEVICE_NAME` | `22` | 设备名称，最多 16 字节，长度 4（可用 `getDeviceName(float[])` 解析） |
| `INDEX_RESERVED2` | `26` | 预留数据，长度 4 |

##### 枚举：ConnectStatus（连接状态）

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `Disconnected` | `0` | 已断开 |
| `Scanning` | `1` | 扫描中 |
| `Connecting` | `2` | 连接中 |
| `Connected` | `3` | 已连接 |
| `NoRecenter` | `4` | 未重置视角 |

##### 枚举：Type（手柄类型）

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `I3VR` | `0` | I3VR 手柄 |
| `Nolo_6dof` | `1` | Nolo 6DoF 手柄 |
| `Nolo_3dof` | `2` | Nolo 3DoF 手柄 |

##### 枚举：Handness（左右手状态）

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `Right` | `0` | 右手 |
| `Left` | `1` | 左手 |
| `Head` | `2` | 头显 |
| `Max` | `3` | 枚举上限哨兵值 |

##### 枚举：KeyCode（按键位掩码，可同时按住多个按键）

| 枚举值 | 值 | 说明 |
| --- | --- | --- |
| `Button_Up` | `0x00000001` | 触摸板 上 |
| `Button_Down` | `0x00000002` | 触摸板 下 |
| `Button_Left` | `0x00000004` | 触摸板 左 |
| `Button_Right` | `0x00000008` | 触摸板 右 |
| `Button_Enter` | `0x00000010` | 系统键 确认 |
| `Button_Home` | `0x00000020` | 系统键 Home |
| `Button_Menu` | `0x00000040` | 系统键 Menu |
| `Button_Back` | `0x00000080` | 系统键 返回 |
| `Button_Volume_Up` | `0x00000100` | 音量 + |
| `Button_Volume_Down` | `0x00000200` | 音量 − |
| `Button_Grip` | `0x00000400` | Grip 键 |
| `Button_Trigger` | `0x00000800` | Trigger 键 |
| `Button_EnumSize` | `0x7fffffff` | 枚举上限哨兵值 |

##### 公开方法

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `ControllerManager` | 获取单例 | 静态方法 |
| `getDeviceName` | `(float[] data)` | `String` | 从手柄数据数组中解析设备名称（等价于 offset=0、size=data.length） | 静态方法 |
| `getDeviceName` | `(float[] data, int offset, int size)` | `String` | 从指定偏移的数据中解析设备名称 | 静态方法 |
| `float2string` | `(float[] in, int offset, int size)` | `String` | 将最多 4 个 float（16 字节）按小端还原为字节、移除末尾 0 后按 GBK 解码为字符串 | 静态方法；`size <= 0` 或 `size > 4` 或数组越界时返回 `"UNKNOWN"` |
| `string2float` | `(String in)` | `float[]` | 将字符串按 GBK 编码拆分为 float 数组（最多 4 个 float），不足补 0 | 静态方法；入参为空返回 `null` |
| `getGBKBytes` | `(String in)` | `byte[]` | 按 GBK 获取字节，不支持 GBK 时回退 UTF-8 | 静态方法 |
| `getGBKString` | `(byte[] in, int offset, int length)` | `String` | 按 GBK 解码字节，不支持 GBK 时回退 UTF-8 | 静态方法 |
| `startService` | `(Context context)` | `void` | 启动手柄服务，可重复调用；action 为 `com.ssnwt.vr.svrapi.ISvrController`，package 为 `com.ssnwt.vr.server`，也可自行实现启动服务 | 需传入 `Context` |
| `connect` | `()` | `int` | 连接手柄服务；返回 0 表示成功 | native 方法 |
| `disconnect` | `()` | `int` | 断开车柄服务；返回 0 表示成功 | native 方法 |
| `getData` | `(float[] data)` | `int` | 从服务读取手柄数据，按 `INDEX_*` 布局解析；返回读取到的数据长度 | native 方法 |

### 2.30 PairManager（手柄配对）

包名：`com.ssnwt.vr.svrcontroller`

获取方式：`PairManager.getInstance()`

手柄配对管理类，负责发起/停止手柄扫描、取消配对、断开连接以及配对齐开关。所有操作均通过向手柄服务（`com.ssnwt.vr.server`）发送带 `action` 参数的 Intent 完成，配对与连接结果需通过 `ControllerManager.getData(float[])` 判定，本类不提供 Listener 回调。

> ⚠️ 仅支持 3dof 版本设备（V901 / S802 / BQ810 / S801）；6dof 设备（VQ910 / VQ920 / VQ930）请使用 `HandshankUtils`。

##### 配对流程

标准手柄：

1. VR 端执行手柄搜索（`search(Context)`）。
2. 打开手柄电源（需要保证该手柄没有被其他 VR 设备连接）。
3. 同时按住手柄 Home 键和 App 键（手柄正面上的两个小圆键，按键上一个是小圆圈，一个是三个小点），指示灯会常亮。
4. 等待 VR 设备扫描并绑定；如果长时间失败，请按照 1、2、3 重试。

Nolo 手柄：打开网关设备及需要使用的手柄，将手柄 VR 头戴部分接入 VR 设备的 USB 接口，完成之后手柄会自动连接。

##### 公开方法

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `PairManager` | 获取单例 | 静态方法 |
| `search` | `(Context context)` | `void` | 扫描手柄，当找到第一个手柄后会自动连接并停止扫描；是否配对并连接可通过 `ControllerManager.getData(float[])` 判定。如果已经配对，将不会再继续配对新的设备，需要先取消配对好的设备 | 建议传入 `Activity#getApplicationContext()` |
| `stopSearch` | `(Context context)` | `void` | 停止扫描 | 建议传入 `Activity#getApplicationContext()` |
| `cancelPaired` | `(Context context)` | `void` | 取消配对 | 建议传入 `Activity#getApplicationContext()` |
| `disconnect` | `(Context context)` | `void` | 断开连接；如果需要重新连接，只需长按手柄 Home 键 1S 左右 | 建议传入 `Activity#getApplicationContext()` |
| `isPaired` | `()` | `boolean` | 当前是否已经有配对手柄，跟是否已经连接上手柄没有关系；返回 true 表示已配对，false 表示未配对 | 读取外部存储文件 `/Controller/Runtime/CtrlMacAddr.txt`（需外部存储读权限）；文件不存在、内容为空或 MAC 为 `ff:ff:ff:ff:ff:ff` 时视为未配对 |
| `enablePair` | `(Context context)` | `void` | 设置允许配对（默认为不允许配对，需要手动打开） | 建议传入 `Activity#getApplicationContext()` |
| `disablePair` | `(Context context)` | `void` | 不允许配对（配对成功后建议禁用手柄配对，防止手柄串联）。 | 建议传入 `Activity#getApplicationContext()` |

### 2.31 AIDL 辅助类

包名：`com.ssnwt.vr.svrapi`

获取方式：由 AIDL 回调参数直接获得（`SvrCallback.callback` 的 `parcelParams`），或通过各包装类的构造方法与 `wrap(...)` 静态工厂创建。

本组为一套跨进程传输用的 AIDL 辅助类：`SvrCallback` 是业务侧继承的回调基类，其余四个类分别为 Parcelable 值、字节数组、Parcelable 列表与字符串列表的包装容器，均实现 `Parcelable`。

##### `SvrCallback` — AIDL 回调基类

`public abstract class SvrCallback extends ISvrCallback.Stub`，供业务继承后作为回调传入各命令接口，避免直接依赖 AIDL 生成的 `Stub`。本类自身未声明方法，需实现下列来自父类的回调方法。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `callback` | `(int callbackId, List<String> params, List<SvrParcel> parcelParams)` | `void` | 命令结果回调。`callbackId` 为命令 ID；`params` 为该命令回传的字符串参数列表（长度视命令而定，通常 `params.get(0)` 承载结果）；`parcelParams` 为该命令回传的 Parcelable 包装列表 | 声明 `throws RemoteException`；需在子类中实现 |

##### `SvrParcel` — Parcelable 值包装

`public class SvrParcel<T extends Parcelable> implements Parcelable`，包装单个任意 Parcelable 值，用于 AIDL 传参与回调。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `SvrParcel` | `(T value)` | — | 包装指定的 Parcelable 值 | 构造方法 |
| `wrap` | `(Parcelable value)` | `SvrParcel` | 静态工厂，等价于构造方法 | 静态方法 |
| `getValue` | `()` | `T` | 取出被包装的值 | — |
| `equals` | `(Object obj)` | `boolean` | 类型相同且被包装值相等时返回 true | — |
| `hashCode` | `()` | `int` | 基于被包装值的哈希值 | — |
| `toString` | `()` | `String` | 返回被包装值的字符串形式 | — |

##### `SvrByteArray` — 字节数组包装

`public class SvrByteArray implements Parcelable`，包装 `byte[]`，便于通过 AIDL 传输字节数组。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `SvrByteArray` | `(byte[] bytes)` | — | 包装指定的字节数组 | 构造方法；参数不可为 null |
| `wrap` | `(byte[] bytes)` | `SvrByteArray` | 静态工厂，等价于构造方法 | 静态方法 |
| `getValue` | `()` | `byte[]` | 取出原始字节数组 | — |
| `equals` | `(Object thatObject)` | `boolean` | 字节内容逐字节相等时返回 true | 基于 `Arrays.equals` |
| `hashCode` | `()` | `int` | 基于字节数组内容的哈希值 | 基于 `Arrays.hashCode` |

##### `SvrParcelArray` — Parcelable 列表包装

`public class SvrParcelArray<T extends Parcelable> implements Parcelable`，包装 `ArrayList<T>`，用于 AIDL 批量传递 Parcelable。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `SvrParcelArray` | `(ArrayList<T> list)` | — | 包装指定的 Parcelable 列表 | 构造方法；参数不可为 null |
| `wrap` | `(ArrayList<T> list)` | `SvrParcelArray` | 静态工厂，等价于构造方法 | 静态方法 |
| `getValue` | `()` | `ArrayList<T>` | 取出列表内容 | — |
| `equals` | `(Object obj)` | `boolean` | 类型相同且列表内容相等时返回 true | — |
| `hashCode` | `()` | `int` | 基于列表内容的哈希值 | — |
| `toString` | `()` | `String` | 返回列表的字符串形式 | — |

##### `SvrStringArray` — 字符串列表包装

`public class SvrStringArray implements Parcelable`，包装 `ArrayList<String>`。

| 方法 | 签名 | 返回类型 | 说明 | 备注 |
| --- | --- | --- | --- | --- |
| `SvrStringArray` | `(ArrayList<String> list)` | — | 包装指定的字符串列表 | 构造方法；参数不可为 null |
| `readStringList` | `(Parcel in)` | `ArrayList<String>` | 从 Parcel 解析字符串列表：先读取 int 长度，再逐个读取字符串；长度为负时返回 null | 反序列化时内部调用 |
| `wrap` | `(ArrayList<String> list)` | `SvrStringArray` | 静态工厂，等价于构造方法 | 静态方法 |
| `getValue` | `()` | `ArrayList<String>` | 取出列表内容 | — |
| `equals` | `(Object obj)` | `boolean` | 类型相同且列表内容相等时返回 true | — |
| `hashCode` | `()` | `int` | 基于列表内容的哈希值 | — |
| `toString` | `()` | `String` | 返回列表的字符串形式 | — |

以上各类均实现 `Parcelable`，各自暴露 `CREATOR` 常量供框架反序列化使用；`describeContents()` 恒返回 0，`writeToParcel(Parcel dest, int flags)` 负责写入内部数据，属 Parcelable 标准实现，调用方无需直接使用。

## 3. 附录

### 3.1 版本

| 项 | 值 |
| --- | --- |
| 产物 | `svr_plugin_android_api.aar` |
| versionCode | 20 |
| versionName | 1.0.20-<git commit>（构建时自动追加提交号） |
| ABI | armeabi-v7a, arm64-v8a |
| minSdkVersion | 26 |

### 3.2 混淆

AAR 自带的 `consumer-rules.pro` 为空，未内置混淆保留规则。若应用开启代码混淆，建议在应用的 `proguard-rules.pro` 中添加：

```
-keep class com.ssnwt.vr.** { *; }
```

---

# SVR Android API Docs

<!-- 目录 -->
- [Overview](#overview)
    - [Feature Modules](#feature-modules)
- [1. Quick Start](#1-quick-start)
    - [1.1 Requirements](#11-requirements)
    - [1.2 Integration](#12-integration)
    - [1.3 Init & Release](#13-init--release)
    - [1.4 Threading & Init Timing](#14-threading--init-timing)
- [2. API Reference](#2-api-reference)
    - [2.1 AndroidInterface (Entry)](#21-androidinterface-entry)
    - [2.2 ApkUtils (Install/Uninstall)](#22-apkutils-installuninstall)
    - [2.3 AppUtils (App Management)](#23-apputils-app-management)
    - [2.4 JAppInfo (App Info)](#24-jappinfo-app-info)
    - [2.5 MediaInfoUtils (Media Info)](#25-mediainfoutils-media-info)
    - [2.6 BatteryUtils (Battery)](#26-batteryutils-battery)
    - [2.7 BluetoothUtils (Bluetooth)](#27-bluetoothutils-bluetooth)
    - [2.8 BrightnessUtils (Brightness)](#28-brightnessutils-brightness)
    - [2.9 BusinessUtils (Business)](#29-businessutils-business)
    - [2.10 DeviceUtils (Device Info)](#210-deviceutils-device-info)
    - [2.11 DownloadUtils (Download)](#211-downloadutils-download)
    - [2.12 FotaUtils (System Update)](#212-fotautils-system-update)
    - [2.13 UpgradeInfo (Package Info)](#213-upgradeinfo-package-info)
    - [2.14 HandshankUtils (Controller)](#214-handshankutils-controller)
    - [2.15 IntentUtils (Intent)](#215-intentutils-intent)
    - [2.16 ProximitySensorUtils (Proximity)](#216-proximitysensorutils-proximity)
    - [2.17 StorageUtils (Storage)](#217-storageutils-storage)
    - [2.18 SpeedTestUtils (Speed Test)](#218-speedtestutils-speed-test)
    - [2.19 TimeUtils (Time)](#219-timeutils-time)
    - [2.20 VolumeUtils (Volume)](#220-volumeutils-volume)
    - [2.21 WifiUtils (Wi-Fi)](#221-wifiutils-wi-fi)
    - [2.22 WifiInfo (Wi-Fi Info)](#222-wifiinfo-wi-fi-info)
    - [2.23 GlobalWindowUtils (Global Window)](#223-globalwindowutils-global-window)
    - [2.24 CustomGlobalWindow (Custom Window)](#224-customglobalwindow-custom-window)
    - [2.25 GlobalWindowInfo (Window Info)](#225-globalwindowinfo-window-info)
    - [2.26 GlobalWindowType (Window Types)](#226-globalwindowtype-window-types)
    - [2.27 GlobalActionType (Action Types)](#227-globalactiontype-action-types)
    - [2.28 Global Window Listeners](#228-global-window-listeners)
    - [2.29 ControllerManager (Controller Data)](#229-controllermanager-controller-data)
    - [2.30 PairManager (Controller Pairing)](#230-pairmanager-controller-pairing)
    - [2.31 AIDL Helper Classes](#231-aidl-helper-classes)
- [3. Appendix](#3-appendix)
    - [3.1 Version](#31-version)
    - [3.2 ProGuard](#32-proguard)

## Overview

`svr_plugin_android_api.aar` is the Android client SDK (package `com.ssnwt.vr.*`) for Skyworth VR all-in-one system services.
It communicates over Binder with the on-device system service `SvrService` (`com.ssnwt.vr.server`),
giving apps access to APK install/uninstall, app management, Bluetooth, Wi-Fi, battery/brightness/volume, device info, FOTA system update, controllers, global windows and more.

> ⚠️ The SDK depends on the preinstalled `SvrService` and **only works on Skyworth VR devices** (VQ910 / VQ920 / VQ930, etc.). It cannot initialize on phones or other devices.

### Feature Modules

| Module | Entry Class | Description |
| --- | --- | --- |
| Entry | `AndroidInterface` | Singleton entry: init / release, accessors of all Utils |
| Install | `ApkUtils` | apk/xapk install, uninstall, package parsing |
| Apps | `AppUtils` / `JAppInfo` | App list, launch apps, background management, app info |
| System state | `BatteryUtils` / `BrightnessUtils` / `VolumeUtils` / `TimeUtils` | Battery, brightness, volume, time |
| Device | `DeviceUtils` / `StorageUtils` / `ProximitySensorUtils` | Device info, screen on/off, eye protection, reboot/shutdown, storage, proximity |
| Network | `WifiUtils` / `BluetoothUtils` | Wi-Fi connect/hotspot, Bluetooth scan/bond/connect |
| Update | `FotaUtils` / `UpgradeInfo` | FOTA check, download, install |
| Download | `DownloadUtils` | File download task management |
| Controller | `HandshankUtils` / `ControllerManager` / `PairManager` | Pairing, battery, version, DFU upgrade, key/thumbstick events, low-level data |
| Global window | `GlobalWindowUtils` / `CustomGlobalWindow` | System global menus/dialogs, custom global windows (VQ920 / VQ930 only) |
| Business | `BusinessUtils` | Directory file-change monitoring |

## 1. Quick Start

### 1.1 Requirements

- minSdkVersion 26, Android (ABIs: `armeabi-v7a` / `arm64-v8a`)
- Device: Skyworth VR all-in-one with the preinstalled `com.ssnwt.vr.server` service
- Toolchain: Android Studio (Gradle)

### 1.2 Integration

Copy `svr_plugin_android_api.aar` into the app module's `libs` directory and add the dependency in `build.gradle`:

```
dependencies {
    implementation files('libs/svr_plugin_android_api.aar')
}
```

### 1.3 Init & Release

Initialize via the `AndroidInterface` singleton. In Unity projects the `app` argument of `init` may be `null` (`UnityPlayer.currentActivity` is used internally).

```
public class MainActivity extends AppCompatActivity {
    AndroidInterface androidInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidInterface = AndroidInterface.getInstance();
        androidInterface.init(getApplication(), new AndroidInterface.InitListener() {
            @Override public void onInitialized() {
                // 所有接口必须在初始化成功后调用
                androidInterface.getWifiUtils().setListener2(new WifiUtils.WifiListener2() {
                    @Override public void onOpened(boolean b) { }
                    @Override public void onConnecting(int i, String s) { }
                    @Override public void onSearchResult(ArrayList<WifiInfo> arrayList) { }
                    @Override public void onRssiLevelChanegd(int i) { }
                });
            }

            @Override public void onReleased() { }

            @Override public void onInitError() { }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        androidInterface.release();
    }
}
```

### 1.4 Threading & Init Timing

> ⚠️ All `getXxxUtils()` accessors and business APIs **must be called after `InitListener.onInitialized()`**.
> Calling a getter from a non-main thread before initialization completes blocks up to 5000ms waiting for the service connection. Call `init` on the main thread.

> ⚠️ Call `release()` (typically in `onDestroy()`) to disconnect from the system service when done.

## 2. API Reference

### 2.1 AndroidInterface (Entry)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance()`

AndroidInterface is the single entry point for all Android capabilities. Get the instance with `getInstance()`, call `init(Application)` to initialize, then obtain each capability object through the corresponding `getXxxUtils()` method. Initialization depends on a successful connection to the device-side SvrService.

> ⚠️ Threading and call order: every `getXxxUtils()` calls `checkThread()` internally, which blocks for up to 5000 ms waiting for initialization when called from a non-main thread before initialization completes. Call `init()` on the main thread, and only call the `getXxxUtils()` methods after the `InitListener.onInitialized()` callback.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `AndroidInterface` | Get the singleton instance |  |
| `init` | `(Application app)` | `void` | Initialize the SDK; when using Unity, app may be null | Must be called before any `getXxxUtils()` |
| `init` | `(Application app, InitListener listener)` | `void` | Initialize the SDK and register an initialization callback; when using Unity, app may be null | If already initialized, `onInitialized()` is invoked immediately; if initialization is already in progress, the listener is added to the waiting list and invoked together once initialization completes |
| `release` | `()` | `void` | Release the API, remove listeners and release SvrManager | Call `init()` again before further use |
| `isInitialized` | `()` | `boolean` | Whether initialization is complete (SvrService connected) |  |
| `getApkUtils` | `()` | `ApkUtils` | Install and uninstall APKs |  |
| `getAppUtils` | `()` | `AppUtils` | List installed apps, launch apps, etc. |  |
| `getBatteryUtils` | `()` | `BatteryUtils` | Battery-related APIs |  |
| `getWifiUtils` | `()` | `WifiUtils` | Wi-Fi related APIs |  |
| `getBrightnessUtils` | `()` | `BrightnessUtils` | Brightness-related APIs |  |
| `getVolumeUtils` | `()` | `VolumeUtils` | Volume-related APIs (STREAM_MUSIC only; implement others yourself) |  |
| `getTimeUtils` | `()` | `TimeUtils` | Time-related APIs |  |
| `getFotaUtils` | `()` | `FotaUtils` | System update (FOTA) APIs |  |
| `getDeviceUtils` | `()` | `DeviceUtils` | Device information APIs (screen on/off, eye protection, reboot/shutdown, etc.) |  |
| `getStorageUtils` | `()` | `StorageUtils` | Storage size APIs |  |
| `getBluetoothUtils` | `()` | `BluetoothUtils` | Bluetooth-related APIs |  |
| `getProximitySensorUtils` | `()` | `ProximitySensorUtils` | Proximity sensor APIs |  |
| `getHandshankUtils` | `()` | `HandshankUtils` | Controller (handshank) utility instance |  |
| `getDownloadUtils` | `()` | `DownloadUtils` | Download utility instance |  |
| `getSpeedTestUtils` | `()` | `SpeedTestUtils` | Network speed test utility instance |  |
| `getBusinessUtils` | `()` | `BusinessUtils` | Industry assistant utility instance |  |
| `getMediaInfoUtils` | `()` | `MediaInfoUtils` | Media information utility instance |  |
| `getIntentUtils` | `()` | `IntentUtils` | Intent utility instance |  |
| `getGlobalWindowUtils` | `()` | `GlobalWindowUtils` | Global window utility instance |  |

##### InitListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onInitialized` | none | Initialization finished (SvrService connected); the getXxxUtils() methods can now be called safely |
| `onReleased` | none | Released / connection lost |
| `onInitError` | none | Initialization failed (connection error) |

A public nested class `AndroidInterface.InitListenerNative` (constructor `InitListenerNative(long handle)`) is also provided to forward InitListener callbacks to native code for JNI usage.

### 2.2 ApkUtils (Install/Uninstall)

Package: `com.ssnwt.vr.androidmanager.apk`

Obtain via: `AndroidInterface.getInstance().getApkUtils()`

APK install/uninstall interface. It parses package name, app name, version code and full app information from apk / xapk files, and provides apk / xapk installation and app uninstallation. Install and uninstall results are delivered asynchronously via callback.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(ApkListener listener)` | `void` | Sets the install/uninstall callback listener |  |
| `getPackageName` | `(String path)` | `String` | Gets the package name from an apk file path |  |
| `getPackageNameFromXAPK` | `(String path)` | `String` | Gets the package name from an xapk file path |  |
| `getAppName` | `(String path)` | `String` | Gets the app name from an apk file path |  |
| `getAppNameFromXAPK` | `(String path)` | `String` | Gets the app name from an xapk file path |  |
| `getVersionCode` | `(String path)` | `int` | Gets the version code from an apk file | Returns -1 on error |
| `getAppInfo` | `(String path)` | `JAppInfo` | Gets all parsed app information from an apk file | Returns null on error |
| `installApk` | `(String path)` | `void` | Installs an apk; asynchronous, result is delivered via `ApkListener` | path e.g. `/storage/emulated/0/APK/test.apk` |
| `installXApk` | `(String path)` | `void` | Installs an xapk; asynchronous, result is delivered via `ApkListener` | path e.g. `/storage/emulated/0/APK/test.xapk` |
| `uninstallApk` | `(String packageName)` | `void` | Uninstalls an app; asynchronous, result is delivered via `ApkListener` | Parameter is the package name |

##### Interface: ApkListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onPackageEvent` | `(int eventCode, String msg, JAppInfo info)` | Install/uninstall result callback. `eventCode` is the result code (see the table below); `msg` is the apk path or app package name when install/uninstall fails; `info` is the app information when install/uninstall succeeds |

##### Install/Uninstall Event Codes

| Event Code | Value | Description |
| --- | --- | --- |
| `EVENT_INSTALL_SUCCESS` | `0` | Install succeeded |
| `EVENT_INSTALL_FAIL_PACKAGE_ERROR` | `1` | Install failed: broken package |
| `EVENT_INSTALL_FAIL_VERSION_LOW` | `2` | Install failed: version lower than the installed one |
| `EVENT_INSTALL_FAIL_PATH_ERROR` | `3` | Install failed: invalid package path |
| `EVENT_INSTALL_FAIL_SYSTEM_APP` | `4` | Install failed: system app |
| `EVENT_INSTALL_FAIL_MEMORY_NOT_ENOUGH` | `5` | Install failed: not enough memory |
| `EVENT_UNINSTALL_SUCCESS` | `10` | Uninstall succeeded |
| `EVENT_UNINSTALL_FAIL_SYSTEM_APP` | `11` | Uninstall failed: system app |
| `EVENT_UNINSTALL_FAIL_OTHER` | `12` | Uninstall failed: other reasons |
| `EVENT_UNINSTALL_NO_EXIST` | `13` | The app does not exist |
| `EVENT_INSTALL_ING` | `14` | Installing |

Table note: the event codes above are defined in `JAppState` and delivered via the `onPackageEvent` callback; they can be referenced directly.

### 2.3 AppUtils (App Management)

Package: `com.ssnwt.vr.androidmanager.app`

Obtain via: `AndroidInterface.getInstance().getAppUtils()`

App management interface. Provides app launching, app list queries, recent app list, app icon and name queries, memory cleanup and background process management, and app category queries.

> ⚠️ `isVrApp` is only supported on VQ920 / VQ930; other models always return false.

Public constant: `TAG` (`String`), value `"AppUtils"`.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `AppUtils` | `(Context context)` | `AppUtils` | Constructor; uses the given Context to obtain the PackageManager | Constructor; the instance holds a Context reference, mind the lifecycle |
| `setListener` | `(AppListener listener)` | `void` | Sets the app count change callback listener |  |
| `startApp` | `(String pkg)` | `void` | Starts an app by package name |  |
| `startApp` | `(String pkg, String clazz)` | `void` | Starts an app by package name and activity class name |  |
| `startApp` | `(String pkg, String clazz, String type, String title, String uri, String info)` | `void` | Starts an app with media information. type is `video/*` or `image/*`; title is the video/image name; uri is a local (`/storage/emulated/0/Movies/test.mp4`) or network (`http://192.168.1.1/test.mp4`) address; info is JSON data of `JAppInfo` |  |
| `startApp` | `(String pkg, String clazz, String key, boolean value)` | `void` | Starts an app with a single key/value parameter |  |
| `startApp` | `(String pkg, String[] keys, String[] values)` | `void` | Starts an app with multiple key/value parameters | Does nothing when keys/values are empty or of different length; values are joined with commas internally |
| `getFirstLauncherActivity` | `(String pkg)` | `String` | Gets the class name of the first launcher activity (CATEGORY_LAUNCHER) |  |
| `getVersionCode` | `(String packageName)` | `int` | Gets the app version code |  |
| `openBrowser` | `(String url)` | `void` | Opens the browser | url must include HTTP/HTTPS |
| `openBrowser` | `(String packageName, String url)` | `void` | Opens the browser; if the package name exists a selection dialog is shown. Package examples: `com.android.chrome`, `org.mozilla.vrbrowser` | url must include HTTP/HTTPS |
| `openBrowser2` | `(String url)` | `String` | Opens the browser and returns a result | url must include HTTP/HTTPS |
| `openBrowser2` | `(String packageName, String url)` | `String` | Opens the browser and returns a result; if the package name exists a selection dialog is shown | url must include HTTP/HTTPS |
| `getIcon` | `(String pkg, String clazz)` | `byte[]` | Gets the app icon as binary data | Returns null when the service has no result |
| `getAppName` | `(String pkg, String clazz)` | `String` | Gets the app name by package name and activity class name |  |
| `getAppInfos` | `(int index, int count)` | `String` | Gets the app list | Returns a JSON string of a `JAppInfo` array; index is the start position and count is the number of apps |
| `getAppInfos2` | `(int index, int count)` | `ArrayList<JAppInfo>` | Gets the app list (already deserialized) | Returns an empty list when the service parcel is null |
| `scanApps` | `()` | `void` | Scans apps |  |
| `cleanMemory` | `()` | `void` | Kills background apps to free memory |  |
| `getResolveInfo` | `(String packageName)` | `List<ResolveInfo>` | Gets the app by package name; one app may have several launcher entries (multiple icons) |  |
| `checkAppExist` | `(String packageName)` | `boolean` | Checks whether the app exists | true = exists, false = not exists |
| `isSystemApp` | `(String packageName)` | `boolean` | Checks whether the app is a system app | true = system app, false = normal app |
| `isPackageStartable` | `(String packageName)` | `boolean` | Checks whether the package is startable |  |
| `getAppCategory` | `(String packageName)` | `JAppInfo.AppCategory` | Queries the app category by package name |  |
| `getTaskSize` | `()` | `int` | Gets the number of currently running tasks |  |
| `killProcess` | `(String pkg)` | `void` | Kills the app with the given package name |  |
| `getRunningPackages` | `()` | `ArrayList<ComponentName>` | Gets package name and other information of background running programs |  |
| `getRunningPackages2` | `()` | `ArrayList<JAppInfo>` | Gets information of the apks running in the background |  |
| `getAppInfos` | `(String pkg)` | `ArrayList<JAppInfo>` | Gets apk information by package name | Overload of `getAppInfos(int, int)`; do not confuse the two |
| `isVrApp` | `(String packageName)` | `boolean` | Checks whether the given package is a VR app | **Device restriction**: only supported on VQ930 (reflectively calls `android.util.SxrVrUtils.isVRApp`) and VQ920 (reflectively calls `android.util.SSNWTVRUtils.isVRApp`); on other models it logs `isVrApp ： The <device> is not support!` and returns false. Throws `RuntimeException` if the reflective call fails |
| `isXRVDEnabled` | `()` | `boolean` | Checks whether XRVD is enabled | Static method; depends on system properties: `persist.sys.xrvd.enable` must be true and `persist.ssnwtvr.display.enable` must be false (default true) |

##### Interface: AppListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onAppCountChanged` | `(int count)` | App count change callback; `count` is the current app count |

### 2.4 JAppInfo (App Info)

Package: `com.ssnwt.vr.androidmanager.app`

Provided by: returned from `ApkUtils` / `AppUtils` APIs (for example `ApkUtils.getAppInfo`, `AppUtils.getAppInfos2`)

App information data class implementing `Serializable`. Carries app name, package name, activity class name, version, storage and memory usage, usage and install time, app type and category, status, icon and extended network data.

> Instances are returned by SDK APIs or created via `parseFromJson`; direct construction is not needed.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `parseFromJson` | `(String json)` | `JAppInfo` | Deserializes a JSON string into a JAppInfo | Static method |
| `getAppName` | `()` | `String` | Gets the app name |  |
| `getPackageName` | `()` | `String` | Gets the package name |  |
| `getClassName` | `()` | `String` | Gets the activity class name |  |
| `getVersion` | `()` | `String` | Gets the app version name |  |
| `getVersionCode` | `()` | `int` | Gets the app version code |  |
| `setVersionCode` | `(int versionCode)` | `void` | Sets the app version code |  |
| `getStorageSize` | `()` | `int` | Gets the app storage size (data, cache, etc.) |  |
| `getAppType` | `()` | `int` | Gets the app type; see `AppType` |  |
| `getLastTimeUsed` | `()` | `long` | Gets the last time the app was used |  |
| `setKiosk` | `(boolean kiosk)` | `void` | Sets whether kiosk mode is enabled |  |
| `getLaunchCount` | `()` | `int` | Gets the launch count |  |
| `getInstallTime` | `()` | `long` | Gets the app install time |  |
| `getMemorySize` | `()` | `int` | Gets the memory usage | Unit: kb |
| `setMemorySize` | `(int size)` | `void` | Sets the memory usage | Unit: kb |
| `isNeedUserLogined` | `()` | `boolean` | Whether user login is required |  |
| `setNeedUserLogined` | `(boolean needUserLogined)` | `void` | Sets whether user login is required |  |
| `isNeedHandleConnected` | `()` | `boolean` | Whether a controller connection is required |  |
| `setNeedHandleConnected` | `(boolean needHandleConnected)` | `void` | Sets whether a controller connection is required |  |
| `getCategory` | `()` | `int` | Gets the app category; see `AppCategory` |  |
| `setCategory` | `(int category)` | `void` | Sets the app category |  |
| `toJsonStr` | `()` | `String` | Serializes this object into a JSON string |  |

##### Public Fields

| Field | Type | Description |
| --- | --- | --- |
| `DESC` | `Comparator` | Static comparator sorting by `lastTimeUsed` descending |
| `TIMEUSED` | `Comparator` | Static comparator sorting by `installTime` descending |
| `NAME_ASC` | `Comparator` | Static comparator sorting by `appName` ascending, case-insensitive |
| `NAME_DESC` | `Comparator` | Static comparator sorting by `appName` descending, case-insensitive |
| `ASC` | `Comparator` | Static comparator sorting by `lastTimeUsed` ascending |
| `packageName` | `String` | Package name, default empty string |
| `lastTimeUsed` | `long` | Last time the app was used |
| `appName` | `String` | App name, default empty string |
| `className` | `String` | Activity class name, default empty string |
| `version` | `String` | Version name, default empty string |
| `versionCode` | `int` | Version code |
| `storageSize` | `int` | App storage size (data, cache, etc.), default -1 |
| `appType` | `int` | App type, the ordinal of `AppType`, default `AppType.Normal` |
| `launchCount` | `int` | Launch count |
| `installTime` | `long` | Install time |
| `memorySize` | `int` | Memory usage in kb, default -1 |
| `needUserLogined` | `boolean` | Whether user login is required |
| `needHandleConnected` | `boolean` | Whether a controller connection is required |
| `category` | `int` | Category: 1 = app, 2 = game, 0 = unknown, default `AppCategory.UNKNOWN` |
| `kiosk` | `boolean` | Whether kiosk mode is enabled |
| `icon` | `String` | Network data: icon |
| `url` | `String` | Network data: URL |
| `categoryNet` | `String` | Network data: category, e.g. "video & entertainment" |
| `cateName` | `String` | Network data: category name, e.g. "app" |
| `appId` | `int` | Network data: app ID |
| `isConnection` | `boolean` | Whether the app is favorited |
| `forceUp` | `boolean` | Whether a forced update is required |
| `appStatus` | `AppStatus` | App status, default is the usable state (`AppStatus.Normal`) |

##### Enum: AppType

| Value | Numeric | Description |
| --- | --- | --- |
| `System` | `0` | System app |
| `Normal` | `1` | Normally installed app |
| `Vive` | `2` | Vive app |
| `Svr` | `3` | Svr app |
| `Service` | `4` | Service |

Static method: `AppType.fromInt(int i)` looks up the enum by ordinal; returns null when not found.

##### Enum: AppCategory

| Value | Numeric | Description |
| --- | --- | --- |
| `APP` | `1` | App |
| `GAME` | `2` | Game |
| `UNKNOWN` | `0` | Unknown app |

Public field: `type` (`int`), the category value. Static methods: `AppCategory.fromType(int type)` and `AppCategory.fromType(String typeName)`; both return `UNKNOWN` when not found.

##### Enum: AppStatus

| Value | Numeric | Description |
| --- | --- | --- |
| `Pending` | `0` | Pending |
| `Downloading` | `1` | Downloading |
| `Pause` | `2` | Paused |
| `Downloaded` | `3` | Downloaded |
| `Install` | `4` | Installing |
| `Update` | `5` | Updating |
| `Normal` | `6` | Installed and usable |
| `Error` | `7` | Error |
| `Uninstalling` | `8` | Uninstalling |

Public field: `state` (`int`), the status value. Static methods: `AppStatus.fromType(int state)` and `AppStatus.fromType(String stateName)`; both return `Normal` when not found.

### 2.5 MediaInfoUtils (Media Info)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getMediaInfoUtils()`

Media information APIs. Currently it provides the ability to query the subtitle list of a given video; the result is delivered through the `SubtitleListListener` callback.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `querySubtitleList` | `(String uri, SubtitleListListener listener)` | `void` | Queries the subtitle list | `uri` may be: local video `/storage/emulated/0/xxx` or `file://xxx`; NAS video `smb://xxx`; screen-casting video `http://xxx/media_file`. A new listener object must be created for every call |

##### SubtitleListListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onResult` | `List<String> list` | Subtitle list query result callback; the parameter is the queried subtitle list |

### 2.6 BatteryUtils (Battery)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getBatteryUtils()`

Battery APIs: query the current and maximum battery level, the charging status and the charging switch, and observe battery and charging status changes through `setListener`.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(Listener listener)` | `void` | Register a battery / charging status listener |  |
| `getCurrentBattery` | `()` | `int` | Current battery level, range 0 - getMaxBattery() |  |
| `getMaxBattery` | `()` | `int` | Maximum battery level, default is 100 |  |
| `getBatteryStatus` | `()` | `int` | Charging status: 0 not charging, 1 charging via charger, 2 charging via USB |  |
| `getChargeState` | `()` | `int` | Charging switch state: 1 enabled, 0 disabled |  |
| `disableCharge` | `()` | `void` | Disable charging |  |
| `enableCharge` | `()` | `void` | Enable charging |  |

##### Listener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onBatteryChanged` | `int battery` | Battery level changed, value range 0 - getMaxBattery() |
| `onBatteryStatusChanged` | `int status` | Charging status changed: 0 not charging, 1 charging via charger, 2 charging via USB |

### 2.7 BluetoothUtils (Bluetooth)

Package: `com.ssnwt.vr.androidmanager.bluetooth`

Obtain via: `AndroidInterface.getInstance().getBluetoothUtils()`

Bluetooth utility class. Provides Bluetooth on/off and state queries, local/remote device name settings, device discovery, bonding and unbonding, connect and disconnect, media audio control, and device discovery / connection state callbacks.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(BluetoothListener listener)` | `void` | Sets the Bluetooth behavior callback listener |  |
| `setConnectionStateListener` | `(BluetoothConnectionStateListener listener)` | `void` | Sets the Bluetooth connection state callback listener |  |
| `isOpen` | `()` | `boolean` | Whether Bluetooth is open | true = open, false = closed |
| `open` | `()` | `boolean` | Opens Bluetooth | true = success, false = fail |
| `getName` | `()` | `String` | Gets the device name |  |
| `setNewName` | `(BluetoothDevice device, String name)` | `boolean` | Sets the name of a remote Bluetooth device | device is the Bluetooth device, name is the new device name |
| `hasMediaAudio` | `(BluetoothDevice device)` | `boolean` | Whether the Bluetooth device supports MediaAudio |  |
| `isMediaAudioOn` | `(BluetoothDevice device)` | `boolean` | Whether MediaAudio is enabled on the Bluetooth device |  |
| `setMediaAudio` | `(BluetoothDevice device, boolean on)` | `boolean` | Turns MediaAudio on or off for the Bluetooth device | on = true to enable |
| `setName` | `(String name)` | `void` | Modifies the local Bluetooth name |  |
| `close` | `()` | `boolean` | Closes Bluetooth | true = success, false = fail |
| `search` | `()` | `boolean` | Searches for Bluetooth devices | true = success, false = fail |
| `cancelSearch` | `()` | `boolean` | Cancels the Bluetooth device search | true = success, false = fail |
| `isSearching` | `()` | `boolean` | Whether a Bluetooth device search is in progress | true = searching, false = not searching |
| `isConnected` | `()` | `boolean` | Whether Bluetooth is connected | true = connected, false = disconnected |
| `getBondedDevices` | `()` | `List<BluetoothDevice>` | Gets the list of bonded devices | Returns an empty list when there is no result |
| `bond` | `(BluetoothDevice device)` | `void` | Bonds a remote Bluetooth device |  |
| `unbond` | `(BluetoothDevice device)` | `void` | Removes the bond of a remote Bluetooth device |  |
| `isDeviceConnected` | `(BluetoothDevice device)` | `boolean` | Checks whether the given remote Bluetooth device is connected | true = connected, false = not connected |
| `connectDevice` | `(BluetoothDevice device)` | `void` | Connects to a remote Bluetooth device |  |
| `disconnectDevice` | `(BluetoothDevice device)` | `void` | Disconnects from a remote Bluetooth device |  |

##### Interface: BluetoothListener

Bluetooth behavior callback interface, registered via `setListener`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onOpened` | `(boolean isOpen)` | Bluetooth open state changed; true = opened, false = closed |
| `onConnected` | `(boolean isConnect)` | Bluetooth connection state changed; true = connected, false = disconnected |
| `onDeviceFound` | `(BluetoothDevice device)` | A new device was found |
| `onBondChanged` | `(BluetoothDevice device)` | The bond state of a device changed |
| `onScanStart` | `()` | The search started |
| `onScanFinish` | `()` | The search finished |
| `onNoSupportBluetooth` | `()` | The local device has no Bluetooth support |
| `onBondError` | `(int code)` | Bonding failed; code 0 = pairing error, 1 = system permission required |
| `onBluetoothUtilsActive` | `(boolean active)` | Whether BluetoothUtils is active |

##### Interface: BluetoothConnectionStateListener

Bluetooth connection state callback interface, registered via `setConnectionStateListener`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onConnectionStateChanged` | `(String device, int state)` | Connection state changed; device is the device whose state changed, state is one of `STATE_DISCONNECTED` (0), `STATE_CONNECTING` (1), `STATE_CONNECTED` (2), `STATE_DISCONNECTING` (3) |

### 2.8 BrightnessUtils (Brightness)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getBrightnessUtils()`

Brightness APIs: read the maximum and current brightness, set the brightness, and observe brightness changes through `setListener`. Both a v1 (int) and a v2 (String/float) read/write interface are provided.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(Listener listener)` | `void` | Register a brightness change listener |  |
| `getMaxBrightness` | `()` | `int` | Maximum brightness, default value is 255 |  |
| `getCurrentBrightness` | `()` | `int` | Current brightness, range 0 - getMaxBrightness() |  |
| `setBrightness` | `(int brightness)` | `void` | Write the brightness into the system, range 0 - getMaxBrightness() |  |
| `getCurrentBrightnessS` | `()` | `String` | Current brightness (v2), range 0 - getMaxBrightness() | Returned as a string |
| `setBrightnessF` | `(float brightness)` | `void` | Write the brightness into the system (v2), range 0 - getMaxBrightness() | Floating point value |

##### Listener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onBrightnessChanged` | `int brightness` | Brightness changed |

### 2.9 BusinessUtils (Business)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getBusinessUtils()`

Industry assistant utility: observes file change events for a given directory, or for a single file inside it, from Unity.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `setFilePathListener` | `(String dir, String path, FileObserverListener listener)` | `void` | Observe file changes from Unity | `dir` is the required directory to watch (@NonNull); `path` may be null (@Nullable) |

##### FileObserverListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `create` | none | A file or directory was created |
| `modify` | none | A file or directory was modified; fired on copy or overwrite |
| `delete` | none | A file or directory was deleted |
| `attrib` | none | Permissions, owner or timestamp were modified |
| `deleteSelf` | none | The watched file or directory was deleted and watching stopped |
| `open` | none | A file or directory was opened |

##### Public fields

| Field | Type | Description |
| --- | --- | --- |
| `TAG` | `String` | Log tag, value is the class simple name (currently `BusinessUtils`) |
| `CREATE` | `int` | Value 1, identifier of the file/directory created event |
| `MODIFY` | `int` | Value 2, identifier of the file/directory modified event |
| `DELETE` | `int` | Value 3, identifier of the file/directory deleted event |
| `ATTRIB` | `int` | Value 4, identifier of the attributes (permissions/owner/timestamp) changed event |
| `DELETE_SELF` | `int` | Value 5, identifier of the watched target deleted / watching stopped event |
| `OPEN` | `int` | Value 6, identifier of the file/directory opened event |

A public nested class `BusinessUtils.FileObserverListenerDelegate` (a SvrCallback subclass) is also provided to dispatch server callbacks to `FileObserverListener` using the event identifiers above; it normally does not need to be used directly.

### 2.10 DeviceUtils (Device Info)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getDeviceUtils()`

Device information APIs: read the device name, brand, model, CPU/GPU/board details, version numbers, MAC/IP addresses, resolution and memory, and control screen on/off, eye protection mode, colour temperature, reboot/shutdown, adb switches, LED behaviour and several status listeners.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(Listener listener)` | `void` | Register an eye protection mode listener |  |
| `getDeviceName` | `()` | `String` | Product name |  |
| `getBrand` | `()` | `String` | Brand name |  |
| `getProductModel` | `()` | `String` | Product model |  |
| `getCPUModel` | `()` | `String` | CPU model |  |
| `getCPUId` | `()` | `String` | CPU id |  |
| `getGPUModel` | `()` | `String` | GPU model |  |
| `getBoardModel` | `()` | `String` | Board model |  |
| `getManufacturer` | `()` | `String` | Manufacturer |  |
| `getAndroidVersion` | `()` | `String` | Android version |  |
| `getResolution` | `()` | `String` | Resolution, in the form width x height |  |
| `getSerialNumber` | `()` | `String` | Serial number (SN) |  |
| `getSoftwareVersion` | `()` | `String` | Software version |  |
| `getSoftwareVersionCode` | `()` | `String` | Software version code |  |
| `getHardwareVersion` | `()` | `String` | Hardware version |  |
| `getWifiMac` | `()` | `String` | Wi-Fi MAC address |  |
| `getWifiIpAddresses` | `()` | `String` | Wi-Fi IP address |  |
| `getTotalRamStorageSize` | `()` | `int` | Total RAM size | Unit: MB |
| `getAvailableRamStorageSize` | `()` | `int` | Available RAM size | Unit: MB |
| `getDisplayWidthPixels` | `()` | `int` | Width in pixels of the current display |  |
| `getDisplayHeightPixels` | `()` | `int` | Height in pixels of the current display |  |
| `getDisplayDensity` | `()` | `int` | Density of the current display |  |
| `doFactoryReset` | `(Context context, boolean eraseSdCard)` | `void` | Perform a factory reset | `eraseSdCard` whether to erase the SD card; the `context` parameter is not used in the actual request |
| `openEyeProtectionMode` | `(boolean open)` | `void` | Turn eye protection mode on or off | true open, false close |
| `isOpenEyeProtectionMode` | `()` | `boolean` | Whether eye protection mode is enabled | true open, false close |
| `setColorTemperature` | `(int value)` | `void` | Set the colour temperature | Valid range 0-27 |
| `reboot` | `()` | `void` | Reboot the device |  |
| `shutdown` | `()` | `void` | Shut the device down |  |
| `setScreenOffTimeout` | `(int timeoutValue)` | `void` | Set the screen-off timeout | Unit: ms |
| `setScreenStatusListener` | `(ScreenStatusListener listener)` | `void` | Register a screen on/off status listener |  |
| `setNativeScreenStatusListener` | `(long handle)` | `void` | Same as setScreenStatusListener, for native code to register a listener | Intended for JNI usage |
| `isMouseAttached` | `()` | `boolean` | Whether a mouse is attached |  |
| `setMouseListener` | `(MouseListener listener)` | `void` | Register a mouse attach/detach listener |  |
| `isScreenOn` | `()` | `boolean` | Whether the screen is on | true on, false off |
| `setAdbEnabled` | `(boolean enable)` | `boolean` | Enable or disable adb for the current boot only | Reset after reboot |
| `setScreenOffEnabled` | `(boolean enable)` | `void` | Enable or disable keeping the screen always on |  |
| `setPersistAdbEnabled` | `(boolean enable)` | `boolean` | Enable or disable adb permanently | ⚠️ On customized devices (`ro.ssnwt.adb.lock` is 1), once disabled it can only be re-enabled by flashing or resetting |
| `isAdbEnabled` | `()` | `boolean` | Whether adb is enabled |  |
| `isScreenOffEnabled` | `()` | `boolean` | Whether the screen always-on feature is enabled |  |
| `hasSSNWTAdbLock` | `()` | `boolean` | Whether the current ROM carries the custom adb lock; if so, once disabled it cannot be re-enabled |  |
| `isLargeSpaceEnable` | `()` | `boolean` | Whether large space mode is enabled, read from the system property `persist.sxr.large_space.enable` equal to 1 | Static method; reads the system property directly without going through SvrService |
| `setIpdListener` | `(IpdListener listener)` | `void` | Register an IPD (interpupillary distance) listener |  |
| `setDeviceShutdownListener` | `(DeviceShutdownListener listener)` | `void` | Register a device shutdown/reboot callback |  |
| `setDeviceShutdownNativeListener` | `(long handle)` | `void` | Same as setDeviceShutdownListener, for native code | Intended for JNI usage |
| `setVSTRangeListener` | `(VSTRangeListener listener)` | `void` | Register a VST range change listener |  |
| `onInterceptKeyBeforeDispatching` | `(KeyEvent event)` | `boolean` | Intercept a key event before dispatch | Returning true intercepts the key event |
| `recenter` | `()` | `void` | Recenter the view |  |
| `execCommand` | `(String command, CommandListener listener)` | `void` | Execute a command on the device and deliver the result asynchronously | `listener` may be null, in which case no result is delivered |
| `setProp` | `(String key, String value)` | `void` | Set a system property |  |
| `getProp` | `(String key, String defaultValue)` | `String` | Read a system property, returning the default value when absent |  |
| `readFileToInt` | `(String path)` | `int` | Read a file and return its content as an int |  |
| `writeStringToFile` | `(String path, String value)` | `void` | Write a string into the given file |  |
| `flashLed` | `(int type)` | `void` | Turn the LED on | `type` is defined by the device side |
| `blinkLed` | `(int type, int delayOn, int delayOff)` | `void` | Blink the LED | `delayOn` on duration, `delayOff` off duration |

##### Listener (eye protection mode callback)

| Callback | Parameters | Description |
| --- | --- | --- |
| `onEyeProtection` | `boolean open` | Eye protection mode changed, true open, false close |

##### ScreenStatusListener (screen on/off callback)

| Callback | Parameters | Description |
| --- | --- | --- |
| `onSwitch` | `String status` | Screen status changed; the status string is passed through from the device side |

##### MouseListener (mouse attach/detach callback)

| Callback | Parameters | Description |
| --- | --- | --- |
| `onState` | `boolean isAttached` | Mouse attach state changed, true attached |

##### IpdListener (IPD callback)

| Callback | Parameters | Description |
| --- | --- | --- |
| `onIpdChange` | `int ipd` | IPD changed |

##### DeviceShutdownListener (shutdown / reboot callback)

| Callback | Parameters | Description |
| --- | --- | --- |
| `onShutdown` | `boolean isReboot` | Device shutdown or reboot; true means reboot, false means shutdown |

##### VSTRangeListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onVSTChange` | `int range` | VST range changed |

##### CommandListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onResult` | `int exitCode, String stdout, String stderr` | Command finished, returning the exit code and the standard output/error |

### 2.11 DownloadUtils (Download)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getDownloadUtils()`

Download management utility: start a download by url or key, specify a storage path, restart, pause, stop and delete download tasks, and query whether a file exists, whether it is complete, and its download information. Progress is reported through `DownloadListener`.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `downloadFile` | `(String url, DownloadListener listener)` | `void` | Download a file | No key is passed; the device derives it from the url |
| `downloadFile` | `(String key, String url, DownloadListener listener)` | `void` | Download a file with a key |  |
| `downloadFile` | `(String key, String url, String path, DownloadListener listener)` | `void` | Download a file with a key and a storage path | `path` is the file storage path |
| `reDownloadFile` | `(String key, DownloadListener listener)` | `void` | Restart the download for the given key |  |
| `resetDownloadListener` | `(String key, DownloadListener listener)` | `void` | Re-register the download listener, for example after a reboot or when the launcher exits |  |
| `pause` | `(String key)` | `void` | Pause a download task |  |
| `stop` | `(String key)` | `void` | Stop a download task |  |
| `delete` | `(String key)` | `void` | Delete a download task |  |
| `checkFileExist` | `(String key)` | `boolean` | Check whether the file exists | true exists, false does not exist |
| `checkFileComplete` | `(String key)` | `boolean` | Check whether the file has been fully downloaded | true complete, false not complete |
| `getDownloadInfo` | `(String key)` | `String` | Get information about the downloaded file |  |

##### DownloadListener (com.ssnwt.vr.download.DownloadListener)

| Callback | Parameters | Description |
| --- | --- | --- |
| `onTotalSize` | `String appKey, long bytes` | Total file size, in bytes |
| `onDownloadStarted` | `String appKey` | Download started |
| `onDownloadProgress` | `String appKey, int progress` | Download progress, 0-100 |
| `onDownloadProgress` | `String appKey, int progress, long speed` | Download progress with speed, progress is 0-100 |
| `onDownloadCompleted` | `String appKey, String apkPath` | Download completed, returning the file path |
| `onStateChanged` | `String appKey, int state` | Task state changed: -1 unknown, 0 pending, 1 downloading, 2 paused, 3 downloaded, 4 error |
| `onError` | `String appKey` | Download error |

### 2.12 FotaUtils (System Update)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getFotaUtils()`

System update (FOTA) APIs: check for a new version, download the update package and install it, and query the update state, package size and downloaded size. Progress and results are reported through `FotaListener`.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(FotaListener listener)` | `void` | Register an update listener for FOTA package version and download progress |  |
| `checkUpdate` | `()` | `void` | Check for a FOTA update |  |
| `goToDownload` | `()` | `void` | Start downloading the FOTA package when a new version is available |  |
| `isDownloading` | `()` | `boolean` | Whether a download is in progress |  |
| `isDownloadingFinished` | `()` | `boolean` | Whether the download has finished |  |
| `hasNewVersion` | `()` | `boolean` | Whether a new version is available |  |
| `getUpgradeInfo` | `()` | `UpgradeInfo` | Get the upgrade information | Returns `com.ssnwt.vr.androidmanager.fota.UpgradeInfo` |
| `getState` | `()` | `int` | Get the update state | 0 Idle, 1 Downloading, 2 Installing, 3 Installed |
| `getPackageSize` | `()` | `int` | Get the package size | Unit: KB |
| `getDownloadedSize` | `()` | `int` | Get the downloaded size | Unit: byte |
| `installPackage` | `()` | `void` | Install the FOTA package |  |
| `goToFotaForceUpdate` | `(String filePath)` | `void` | Copy a local file into the installation directory for a forced update |  |

##### FotaListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onVersion` | `boolean hasNewVersion, boolean forceUpdate, String pkgVersion, String description` | Whether a new version exists, whether the update is forced, plus the FOTA package version and description |
| `onVersionError` | `boolean hasNewVersion, int errorCode` | Version check failed; errorCode 8 means no space left |
| `onDownloadProgress` | `int progress` | Download progress, 0-100 |
| `onDownloadError` | `int errCode` | Download failed; errCode: 0 not enough storage space, 1 network error |
| `onDownloadFinish` | none | The new FOTA package has been downloaded |
| `onInstallProgress` | `int progress` | Install progress, 0-100 |
| `onInstallError` | `int errCode` | Install failed; errCode: 0 not enough storage space, 1 network error |
| `onInstallError` | `int errCode, boolean isUserLocalUpgrade` | Install failed; isUserLocalUpgrade is true for a package copied manually by the user and false for a remotely downloaded one |
| `onInstallFinish` | none | The FOTA package has been installed |
| `onInstallFinish` | `boolean isUserLocalUpgrade` | The FOTA package has been installed; isUserLocalUpgrade is true for a package copied manually by the user and false for a remotely downloaded one |

### 2.13 UpgradeInfo (Package Info)

Package: `com.ssnwt.vr.androidmanager.fota`

Returned by `FotaUtils.getUpgradeInfo()`; implements `Parcelable` and is transferred across processes via AIDL.

FOTA upgrade package info data class. Holds the upgrade package metadata pushed by the service (version, download URL, MD5, size, force-upgrade flag, etc.); properties are read and written through getters/setters.

| Property | Type | Description |
| --- | --- | --- |
| `id` | `int` | Unique ID of the upgrade package record |
| `devicetype` | `String` | Device type |
| `customerid` | `String` | Customer ID |
| `hardversion` | `String` | Hardware version |
| `allsoft` | `int` | Whole-device (full) software upgrade flag |
| `isPublish` | `int` | Published flag |
| `currnumber` | `int` | Number of devices already upgraded |
| `maxnumber` | `int` | Maximum number of upgradable devices |
| `releasetime` | `long` | Release timestamp of the package |
| `storeFileName` | `String` | Local storage file name |
| `pkgurl` | `String` | Download URL of the package |
| `pkgmd5` | `String` | MD5 checksum of the package |
| `pkgversion` | `String` | Package version |
| `pkgcndesc` | `String` | Chinese description of the package |
| `pkgendesc` | `String` | English description of the package |
| `pkgdesc` | `String` | Package description |
| `forceupgrade` | `int` | Force-upgrade flag |
| `pkgtype` | `int` | Package type |
| `pkgsize` | `long` | Package size in bytes |
| `committime` | `long` | Commit timestamp |
| `modifytime` | `long` | Last modified timestamp |

> ⚠️ `pkgendesc` is NOT serialized into Parcel; it is lost across IPC and only valid for in-process objects.

##### Helper Methods

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `equals` | `(Object o)` | `boolean` | Business comparison: returns true when `pkgmd5`, `pkgurl` (both case-insensitive) and `forceupgrade` are all equal. | Not annotated with `@Override` and `hashCode` is not overridden, so behavior differs from a regular object in `HashSet`/`HashMap`; note this semantics is a business comparison, not a full-field comparison. |
| `isIDEqual` | `(Object o)` | `boolean` | Checks whether the given object has the same `id` as this instance. | Returns false for non-`UpgradeInfo` objects. |
| `isJustFroceNotEqual` | `(Object o)` | `boolean` | Checks whether md5 and url are equal but the force-upgrade flag differs (only the force-upgrade marker changed). | Method name is spelled `Froce` (should be Force); the historic spelling is kept. |
| `isEqual` | `(String str1, String str2)` | `boolean` | Static helper that compares two strings case-insensitively. | `static`; returns false when `str1` is null, and false when both are null. |

### 2.14 HandshankUtils (Controller)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getHandshankUtils()`

Controller (handshank) management APIs: bond and unbond the left/right controllers, query the connection state, read battery level, MAC address and version, enter DFU mode and upgrade firmware, and listen for bond, battery, button/thumbstick/touch/hall and connection events. Unless stated otherwise, `lr` is 0 for the left controller and 1 for the right controller.

> ⚠️ `getExtra` and `setExtra` are only supported on ES202 (wrist camera) devices; for `getControllerVersion` and `getControllerMac`, `lr` also accepts -1 for the head unit.

| Method | Signature | Return type | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(HandshankListener listener)` | `void` | Register a controller bond/unbond listener |  |
| `isBond` | `(int lr)` | `boolean` | Whether the controller is bonded | true bonded, false not bonded |
| `bond` | `(int lr)` | `void` | Bond the controller |  |
| `unbond` | `(int lr)` | `void` | Unbond the controller |  |
| `isConnect` | `(int lr)` | `boolean` | Whether the controller is connected | true connected, false not connected |
| `getControllerVersionAsync` | `(int lr, HandshankVersionListener listener)` | `void` | Get the controller version asynchronously | `lr` accepts -1 for the head unit |
| `getBatteryLevelAsync` | `(HandshankBatteryListener listener)` | `void` | Get the controller battery level asynchronously; polls up to 10 times until a non-zero value is received | A new listener object must be created for every call |
| `getControllerMac` | `(int lr)` | `String` | Get the controller MAC address | `lr` accepts -1 for the head unit; synchronous call |
| `getControllerMacAsync` | `(int lr, HandshankMacListener listener)` | `void` | Get the MAC address asynchronously; polls up to 3 times until a non-empty value is received | `lr`: 0 left controller, 1 right controller, -1 head unit |
| `getExtra` | `(int code, int lr)` | `String` | Query extended information of an ES202 wrist camera | ⚠️ Only supported on ES202 (wrist camera) devices; `code` is `C.EXTRA_GET_MAC` or `C.EXTRA_GET_STATUS`; returns a MAC string or a decimal status value, or "" on failure / when unsupported |
| `setExtra` | `(int code)` | `void` | Control an ES202 wrist camera | ⚠️ Only supported on ES202 (wrist camera) devices; `code` bitmask is defined in `C.EXTRA_SET_*`; no return value |
| `setExtra` | `(int code, String value)` | `void` | Control an extended node of an ES202 wrist camera (form with value; shares the command code with `setExtra(int)`, the server distinguishes them by parameter count) | ⚠️ Only supported on ES202 (wrist camera) devices; `code` is `C.EXTRA_SET_WIFI_NODE` (`value` "1" opens / "0" closes the pairing window) or `C.EXTRA_SET_TRANSFER_NODE` (`value` is 4 space-separated hex bytes, e.g. "DE 35 00 0B"); bitmasks (low byte of `C.EXTRA_SET_*`) are also accepted but `setExtra(int)` should be used; passing null for `value` degrades to the legacy single-parameter form (wire format is byte-for-byte identical to the old version) |
| `setBatteryListener` | `(HandshankBatteryListener listener)` | `void` | Listen for controller battery changes |  |
| `initBatteryListenerNative` | `(long handle)` | `void` | Helper for native code to register a HandshankBatteryListener | Intended for JNI usage; internally calls both `getBatteryLevelAsync` and `setBatteryListener` |
| `enterDFUMode` | `(int lr)` | `void` | Enter DFU mode |  |
| `checkNewVersion` | `(HandShankCheckListener listener)` | `void` | Check whether new controller firmware is available | Result values are described under `onCheckInfoResult` |
| `requestUpgrade` | `(HandshankUpgradeListener listener)` | `void` | Start upgrading the controller |  |
| `startUpgrade` | `(String deviceVersion, String deviceDfuPath, String hostVersion, String hostDfuPath, HandshankUpgradeListener listener)` | `void` | Start the firmware upgrade | `deviceVersion` controller version, `deviceDfuPath` controller firmware file path, `hostVersion` controller host version, `hostDfuPath` controller host firmware file path |
| `cancelFotaThenCheck` | `()` | `void` | Cancel the FOTA upgrade and re-check the controllers | The controller detection dialog has lower priority than the FOTA upgrade, so call this when the FOTA upgrade is cancelled |
| `setControllerListener` | `(HandshankControllerListener listener)` | `void` | Register a controller event listener | Pass null to clear the listener |
| `setConnectListener` | `(HandshankConnectListener listener)` | `void` | Register a controller connection state listener (connect/disconnect, callback ON_CONNECT_CHANGED=605) | ⚠️ Requires the device-side svrservice to support `HANDSHANK_SET_CONNECT_LISTENER(2318)`, otherwise the listener does not take effect; pass null to unregister |

##### HandshankListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onBond` | `int lr` | Bond succeeded |
| `onUnbond` | `int lr` | Unbond succeeded |
| `onBondError` | `int lr` | Bond failed |
| `onUnbondError` | `int lr` | Unbond failed |

##### HandshankBatteryListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onBatteryChanged` | `int l, int r` | l left controller battery level, r right controller battery level; -1 means not connected |

##### HandshankMacListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onReceiveMac` | `String mac` | MAC address received asynchronously |

##### HandshankVersionListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onReceiveVersion` | `String version` | Controller version received asynchronously |

##### HandshankUpgradeListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onSuccess` | none | Upgrade succeeded |
| `onError` | `int code` | Upgrade failed, returning an error code |

##### HandShankCheckListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onCheckInfoResult` | `int result, String updateInfo` | Version check result: 0 no update, 1 update available (optional), 2 update available (forced), -1 check failed; updateInfo is the update description |

##### HandshankConnectListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onConnectChanged` | `int lr, boolean connect` | Controller connection state changed; connect true means connected, false means disconnected |

##### HandshankControllerListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onControllerKeyEvent` | `int controllerId, int keyCode, KeyEvent event` | Button event; controllerId 0 left / 1 right; event is ACTION_DOWN or ACTION_UP |
| `onControllerTouchEvent` | `int controllerId, int keyCode, boolean touched` | Touch event; keyCode of the touched button; touched true while touching |
| `onControllerThumbstickEvent` | `int controllerId, float x, float y` | Thumbstick event; x/y are the thumbstick coordinates |
| `onControllerHallEvent` | `int controllerId, float trigger, float grip` | Hall sensor event; trigger value and grip value |

A public nested class `HandshankUtils.HandshankBatteryListenerNative` (constructor `HandshankBatteryListenerNative(long handle)`) forwards battery callbacks to native code; the public delegate classes `HandshankMacListenerDelegate` and `HandshankVersionListenerDelegate` are also available but normally do not need to be used directly.

### 2.15 IntentUtils (Intent)

Package: `com.ssnwt.vr.androidmanager.intent`

Obtain via: `AndroidInterface.getInstance().getIntentUtils()`

Intent-related APIs. Register a callback with `setListener` to receive the result notification of starting an Intent.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(IntentListener listener)` | `void` | Sets the Intent start callback listener |  |

##### IntentListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onStartIntent` | `(int result, String paramInfo)` | Intent start callback; `result` is the call result and `paramInfo` carries the Intent parameter information |

### 2.16 ProximitySensorUtils (Proximity)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getProximitySensorUtils()`

Proximity sensor utilities. Provides adding and removing proximity sensor listeners; the callback reports whether an object is far from (beyond 5 cm) or near to (within 5 cm) the sensor. Multiple listeners are supported and kept in a thread-safe list.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `addSensorListener` | `(SensorListener sensorListener)` | `void` | Adds a proximity sensor listener. | Adding the same listener twice has no effect; the sensor is registered with SvrService automatically when the first listener is added. |
| `removeSensorListener` | `(SensorListener sensorListener)` | `void` | Removes a proximity sensor listener. | The sensor is unregistered automatically once no listener remains, to save power. |

##### SensorListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onDistanceFar` | `boolean far` | true: farther than 5 cm; false: within 5 cm. |

### 2.17 StorageUtils (Storage)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getStorageUtils()`

Storage utilities. Used to query capacity and paths of the data directory, of a specific directory, and of external SD cards, and to listen for SD card insertion/removal. All size values are returned in Mb.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(StorageListener listener)` | `void` | Sets the listener for SD card insert/remove changes. |  |
| `getTotalStorageSize` | `()` | `int` | Gets the total storage size of the data directory. | In Mb. |
| `getUsedStorageSize` | `()` | `int` | Gets the used storage size of the data directory. | In Mb. |
| `getTotalStorageSize` | `(String path)` | `int` | Gets the total storage size of the specified directory. | In Mb; returns -1 if path is empty or does not exist. |
| `getUsedStorageSize` | `(String path)` | `int` | Gets the used storage size of the specified directory. | In Mb; returns -1 if path is empty or does not exist. |
| `getSDCardPath` | `()` | `String` | Gets the external SD card path. |  |
| `getAllSDCardPath` | `()` | `String[]` | Gets all external SD card paths. |  |

##### StorageListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onSDCardInsert` | `String path` | An SD card was inserted; the path of the card is returned. |
| `onSDCardRemove` | `()` | An SD card was removed. |

### 2.18 SpeedTestUtils (Speed Test)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getSpeedTestUtils()`

Network speed test APIs. Three variants are provided: a default test, a test with a custom duration, and a fully custom test. The real-time speed, final speed and error information are delivered through the `SpeedListener` callback.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `test` | `(String ip, SpeedListener listener)` | `void` | Default speed test | Defaults: echo interval 1 s, test duration 10 s, no rate limit |
| `test` | `(int t, String ip, SpeedListener listener)` | `void` | Speed test with a custom duration | `t` is the test duration in seconds |
| `test` | `(int i, int t, String b, boolean R, String ip, SpeedListener listener)` | `void` | Speed test with fully custom parameters | `i` echo interval (s); `t` test duration (s); `b` rate limit, e.g. 7M (mb/s); `R` reverse speed test |

##### SpeedListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onSpeed` | `String speed` | Real-time speed, unit M/B |
| `onResult` | `String speed` | Final speed, unit M/B |
| `onError` | `String error` | Speed test error callback; the parameter is the error message |

The interface also defines the callback ID constants `CALLBACK_ID_ON_SPEED` (1), `CALLBACK_ID_ON_ERROR` (2) and `CALLBACK_ID_ON_RESULT` (3), used internally by the SDK for callback dispatching; they normally need no attention.

### 2.19 TimeUtils (Time)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getTimeUtils()`

Time utilities. Used to read the current time and its format, and to set the 12/24-hour format, system time, time zone, and whether network time is acquired automatically.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `getCurrTime` | `()` | `String` | Gets the current time, formatted as yyyy-MM-dd HH:mm:ss. |  |
| `getCurrTimeWithoutYear` | `()` | `String` | Gets the current time, formatted as HH:mm, without the date. | Affected by the 24-hour/12-hour format. |
| `is24Hour` | `()` | `boolean` | Checks whether the 24-hour time format is currently used. | true: 24-hour; false: 12-hour. |
| `set24Hour` | `(boolean is24Hour)` | `void` | Sets the time format. |  |
| `setCurrentTime` | `(String time)` | `void` | Sets the current time. |  |
| `setCurrentTimezone` | `(String timezone)` | `void` | Sets the current time zone. |  |
| `setAutoTime` | `(boolean enable)` | `void` | Enables or disables automatic network time. |  |
| `isAutoTime` | `()` | `boolean` | Checks whether automatic network time is enabled. |  |

### 2.20 VolumeUtils (Volume)

Package: `com.ssnwt.vr.androidmanager`

Obtain via: `AndroidInterface.getInstance().getVolumeUtils()`

Volume utilities. Provides getting and setting the media volume, voice call volume and system volume, plus a volume change listener. The default maximum media volume is 15.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener` | `(VolumeListener listener)` | `void` | Sets the volume change listener. |  |
| `getMaxVolume` | `()` | `int` | Gets the maximum media volume. | Default is 15. |
| `getCurrentVolume` | `()` | `int` | Gets the current media volume. | Range: 0 to getMaxVolume(). |
| `setVolume` | `(int volume)` | `void` | Sets the media volume. | Range: 0 to getMaxVolume(). |
| `getCurrentVolumeS` | `()` | `String` | Gets the current media volume as a string. |  |
| `setVolumeF` | `(float volume)` | `void` | Sets the media volume as a float value. |  |
| `setVolumeWithUI` | `(int volume)` | `void` | Sets the media volume and shows the system volume UI. | Range: 0 to getMaxVolume(). |
| `getMaxVoiceCallVolume` | `()` | `int` | Gets the maximum voice call volume. |  |
| `getCurrentVoiceCallVolume` | `()` | `int` | Gets the current voice call volume. |  |
| `setVoiceCallVolume` | `(int volume)` | `void` | Sets the voice call volume. |  |
| `getMaxSystemVolume` | `()` | `int` | Gets the maximum system volume. |  |
| `getCurrentSystemVolume` | `()` | `int` | Gets the current system volume. |  |
| `setSystemVolume` | `(int volume)` | `void` | Sets the system volume. |  |

##### VolumeListener

| Callback | Parameters | Description |
| --- | --- | --- |
| `onVolumeChanged` | `()` | Called when the volume changes. |

### 2.21 WifiUtils (Wi-Fi)

Package: `com.ssnwt.vr.androidmanager.wifi`

Obtain via: `AndroidInterface.getInstance().getWifiUtils()`

Wi-Fi management interface. Provides Wi-Fi on/off and scanning, network add/connect/disconnect/forget, saved password queries, RSSI level queries, default country code setting, Wi-Fi hotspot control, and connection state / scan result callbacks.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setListener2` | `(WifiListener2 listener)` | `void` | Sets the listener for Wi-Fi connect/disconnect and related callbacks | The recommended callback interface |
| `isOpenWifi` | `()` | `boolean` | Whether Wi-Fi is currently open | true = open, false = closed |
| `openWifi` | `()` | `void` | Requests to turn Wi-Fi on |  |
| `closeWifi` | `()` | `void` | Requests to turn Wi-Fi off |  |
| `searchWifi` | `()` | `boolean` | Requests a Wi-Fi scan | Results are delivered via `WifiListener2.onSearchResult` |
| `addNetwork` | `(String ssid, int security, String password)` | `int` | Adds a Wi-Fi network; only OPEN or WPA PSK networks are supported | ssid is the network name; security is the encryption type: 0-NONE, 1-WEP, 2-PSK; password is the network password |
| `connectWifi` | `(String ssid, String bssid, String capabilities, String password)` | `int` | Connects to a Wi-Fi network with a password |  |
| `connectWifi` | `(int nid)` | `void` | Connects to a saved Wi-Fi network | nid is the networkId |
| `disconnectWifi` | `()` | `void` | Disconnects the currently connected Wi-Fi network |  |
| `disconnectWifi` | `(int nid)` | `void` | Disconnects the connected Wi-Fi network by networkId |  |
| `forget` | `(int nid)` | `void` | Forgets a saved Wi-Fi password | nid is the networkId |
| `saveWifi` | `(WifiConfiguration info, SaveWifiListener listener)` | `void` | Saves a Wi-Fi configuration | The result is delivered asynchronously via `SaveWifiListener` |
| `forget` | `()` | `void` | Forgets the password of the currently connected Wi-Fi network |  |
| `getCurrentNetworkID` | `()` | `int` | Gets the networkId of the currently connected network |  |
| `getConnectedWifi2` | `()` | `WifiInfo` | Gets the info object of the connected Wi-Fi network | The service returns a JSON string which is parsed into `WifiInfo` |
| `getWifiRssiLevel` | `()` | `int` | Gets the current Wi-Fi signal level | 0-3; 0 = no signal, 3 = strongest signal |
| `getWifiPassword` | `(String ssid)` | `String` | Gets the saved password of the given SSID |  |
| `setDefaultCountryCode` | `(String country)` | `void` | Sets the default Wi-Fi country code |  |
| `startHotspot` | `(String ssid, String password, int security)` | `boolean` | Starts a Wi-Fi hotspot | ssid is the hotspot name; password is required for encrypted mode and must be at least 8 characters, may be null for open mode; security values are listed in the public constants; true indicates the call succeeded |
| `stopHotspot` | `()` | `boolean` | Stops the Wi-Fi hotspot | true indicates the call succeeded |
| `isHotspotEnabled` | `()` | `boolean` | Queries whether the Wi-Fi hotspot is enabled | true indicates the hotspot is enabled |
| `connectWifi` | `(Context context, String ssid, String password)` | `void` | Requests a Wi-Fi connection directly via an Intent (`svr.intent.action.CONNECT_WIFI`) | Static method; does not go through SvrManager. Passes `ssid` / `password` extras to `com.ssnwt.vr.server/com.ssnwt.vr.svrservice.SvrService` |

##### Public constants

| Constant | Value | Description |
| --- | --- | --- |
| `SECURITY_OPEN` | `0` | Open hotspot (no password) |
| `SECURITY_WPA2` | `2` | WPA2-PSK encrypted hotspot |

##### Enum: WifiConnectionState

Wi-Fi connection state. Used as the status parameter of `WifiListener2.onConnecting`.

| Value | Ordinal | Description |
| --- | --- | --- |
| `IDLE` | `0` | Idle |
| `DISCONNECTED` | `1` | Disconnected |
| `CONNECTING` | `2` | Connecting |
| `CONNECTED` | `3` | Connected |
| `PASSWORD_ERROR` | `4` | Wrong password |
| `NOT_SUPPORT` | `5` | Not supported |
| `FORGET` | `6` | Network forgotten |
| `START_CONNECT` | `7` | Connection started |
| `FORGET_FAIL` | `8` | Failed to forget the network |
| `CONNECT_FAIL` | `9` | Connection failed |
| `ADD_NETWORK_FAIL` | `10` | Failed to add the network |

##### Interface: WifiListener2

Wi-Fi state callback interface, registered via `setListener2`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onOpened` | `(boolean open)` | Wi-Fi open state changed; true = open, false = closed |
| `onConnecting` | `(int status, String ssid)` | Connection status changed; status is the ordinal of `WifiConnectionState`, ssid is the Wi-Fi name |
| `onSearchResult` | `(ArrayList<WifiInfo> wifiList)` | Scan result callback; `searchWifi()` must be called first; wifiList is a list of `WifiInfo` |
| `onRssiLevelChanegd` | `(int level)` | Current Wi-Fi signal level changed (the method name spelling is kept as-is) |

##### Interface: SaveWifiListener

Callback interface for saving a Wi-Fi configuration, used by `saveWifi`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onSuccess` | `()` | Save succeeded |
| `onFailed` | `()` | Save failed |

### 2.22 WifiInfo (Wi-Fi Info)

Package: `com.ssnwt.vr.androidmanager.wifi`

Obtain via: returned by the `WifiUtils` interface, e.g. `WifiUtils.getConnectedWifi2()`, or the `WifiListener2.onSearchResult(ArrayList<WifiInfo>)` callback

Wi-Fi information data class. Describes a Wi-Fi network (name, MAC, band, signal strength, security type and status) and supports JSON serialization / deserialization. All fields are accessed through getters.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `parseFromJson` | `(String json)` | `WifiInfo` | Parses a JSON string into a WifiInfo object | Static method |
| `getSSID` | `()` | `String` | Gets the Wi-Fi name |  |
| `getBSSID` | `()` | `String` | Gets the Wi-Fi MAC address |  |
| `getNetworkID` | `()` | `int` | Gets the Wi-Fi id | Only available for the connected network |
| `get5G` | `()` | `boolean` | Whether the network is on the 5G band |  |
| `getFrequency` | `()` | `int` | Gets the frequency | 2.4G (2400) / 5G (5000+) / 6G (5925+) |
| `getRssi` | `()` | `int` | Gets the current Wi-Fi signal strength | Range (-128, 0) |
| `getRssiLevel` | `()` | `int` | Gets the current Wi-Fi signal level | 0-3 by default |
| `getWifiStatus` | `()` | `int` | Gets the Wi-Fi status | Value is the ordinal of `WifiStatus` |
| `getCapabilities` | `()` | `String` | Gets the Wi-Fi encryption mode (raw capabilities string) |  |
| `getSecurity` | `()` | `String` | Gets the security type | Values are the public constants `TYPE_SECURITY_*` |
| `setSecurity` | `(WifiConfiguration config)` | `void` | Sets the password type from a WifiConfiguration object | Checked in order: SAE / WPA_PSK / WPA_EAP or IEEE8021X / WEP; NONE when none matches |
| `isNeedPassword` | `()` | `boolean` | Whether a password is required |  |
| `toString` | `()` | `String` | Returns a concatenated text of frequency, rssi, level, networkId, status, security and SSID(BSSID) | Overrides `Object.toString` |
| `equals` | `(Object obj)` | `boolean` | Compares with another WifiInfo by SSID | Overrides `Object.equals` |
| `toJsonString` | `()` | `String` | Serializes this object into a JSON string |  |

##### Public constants

| Constant | Value | Description |
| --- | --- | --- |
| `TYPE_SECURITY_WEP` | `"WEP"` | WEP security type |
| `TYPE_SECURITY_PSK` | `"PSK"` | WPA/WPA2-PSK security type |
| `TYPE_SECURITY_EAP` | `"EAP"` | EAP / 802.1X security type |
| `TYPE_SECURITY_SAE` | `"SAE"` | WPA3-SAE security type |
| `TYPE_SECURITY_NONE` | `"NONE"` | No security; also the default value |
| `MIN_FREQ_5G` | `5000` | Start frequency of the 5G band (MHz) |
| `MIN_FREQ_6G` | `5925` | Start frequency of the 6G band (MHz) |

##### Enum: WifiStatus

Wi-Fi status. Used as the return value of `getWifiStatus()`.

| Value | Ordinal | Description |
| --- | --- | --- |
| `Enabled` | `0` | Enabled (default value) |
| `Saved` | `1` | Saved |
| `Using` | `2` | Currently in use |

### 2.23 GlobalWindowUtils (Global Window)

Package: `com.ssnwt.vr.globalwindow`

Obtain via: `AndroidInterface.getInstance().getGlobalWindowUtils()` (a public no-arg constructor exists, but always acquire the instance through this getter)

Global menu. Provides showing and hiding of built-in global windows, dialogs, Toast, KIOSK mode, and injection of touch and focus events.

> ⚠️ Global windows are only supported on VQ920 / VQ930 devices.

> ⚠️ You must first register a listener via `setGlobalWindowListener` / `setCustomWindowListener` before making global window calls.

**Show flow:** `requestShow` returns the unique window id; once the system has prepared the render target it invokes the listener — `GlobalWindowListener.onRequestShow(GlobalWindowInfo)` for built-in windows, `CustomGlobalWindowListener.onShow(int id, Surface surface)` for custom windows. A custom window must call `CustomGlobalWindow.show(surface)` inside that callback to render its content onto the `Surface`. Alternatively call `show(int id, Surface surface)` to bind a target Surface manually.

The internal dispatch classes `GlobalWindowListenerDelegate` / `CustomWindowListenerDelegate` / `KioskListenerDelegate` and their callback constants (`ON_REQUEST_SHOW=1`, `ON_HIDE=2`, `HOME_EVENT=3`, `ACTION_EVENT=4`, ...) are framework-internal message routing details. Callers do not need to know them — registering a listener is sufficient to receive the corresponding callbacks.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `setCustomWindowListener` | `(CustomGlobalWindowListener listener)` | `void` | A third-party app must set a CustomWindowListener before making global window calls |  |
| `setGlobalWindowListener` | `(GlobalWindowListener listener)` | `void` | The current app must set a GlobalWindowListener before making global window calls |  |
| `requestHomeEvent` | `(int type)` | `void` | Home event; type: 0 hide, 1 show |  |
| `requestKioskEvent` | `()` | `void` | Key combination: enable KIOSK mode |  |
| `requestAllMenu` | `()` | `void` | Key combination: show all functions of the global menu |  |
| `requestActionEvent` | `(int type, int action)` | `void` | type is the UI type, action is the action type | See `GlobalWindowType` and `GlobalActionType` |
| `initGlobalWindowNative` | `(long handle)` | `void` | Helper for native code to set a GlobalWindowListener; handle is the native context token | JNI only |
| `requestShow` | `(@GlobalWindowType int type)` | `int` | Request to show one of the GlobalWindowType windows; returns the generated unique window id | Some windows need dedicated APIs, e.g. `toast(String, int)` |
| `requestShow` | `(CustomGlobalWindow globalWindow)` | `int` | Request to show a custom window; returns the unique window id |  |
| `addOverrideCustomView` | `(CustomGlobalWindow globalWindow)` | `int` | Register a custom view overriding the global window of the same type |  |
| `show` | `(int id, Surface surface)` | `void` | Show the given window on the given Surface |  |
| `requestHide` | `(int id)` | `void` | Request to hide a window that is currently showing |  |
| `setVisibility` | `(int id)` | `void` | Request a currently visible window | Semantics: make the window visible |
| `setInVisibility` | `(int id)` | `void` | Request a currently invisible window | Semantics: make the window invisible |
| `injectTouchEvent` | `(int id, MotionEvent event)` | `void` | Inject a MotionEvent into the window |  |
| `updateFocusPosition` | `(int id, int x, int y)` | `void` | Set the hover effect position |  |
| `performClickDown` | `(int id, int x, int y)` | `void` | Inject a motion down event |  |
| `performActionMove` | `(int id, int x, int y)` | `void` | Inject a motion ACTION_MOVE event |  |
| `performClickUp` | `(int id, int x, int y)` | `void` | Inject a motion up event |  |
| `toast` | `(String content)` | `void` | Show a toast, 3 s by default |  |
| `toast` | `(String content, int millisecond)` | `void` | Show a toast with a custom duration in milliseconds |  |
| `showRelocateSuccessSafeArea` | `(OnConfirmListener listener)` | `int` | Show the safe-area relocation success global window |  |
| `showResetSuccessSafeArea` | `(OnConfirmListener listener)` | `int` | Show the safe-area reset success global window |  |
| `showConfirmDialog` | `(ActionListener listener)` | `int` | Show a confirmation dialog |  |
| `showConfirmDialog` | `(ActionListener listener, List<String> strings)` | `int` | strings are [title, tips, buttonText] in that exact order; pass an empty string to skip an item, e.g. ["title","","buttonText"]; a fourth parameter of "-" keeps the current app running | The parameter order is a hard contract |
| `showUninstallDialog` | `(ActionListener listener, String pkg)` | `int` | Show the uninstall dialog; pkg is the target package name |  |
| `showDeleteDialog` | `(ActionListener listener)` | `int` | Show the delete dialog |  |
| `showKioskDialog` | `(ActionListener listener)` | `int` | Show the KIOSK mode dialog |  |
| `showSpaceOrientationDialog` | `(ActionListener listener)` | `int` | Show the space orientation dialog |  |
| `showNoteNoSlamDialog` | `(ActionListener listener)` | `int` | Show the no-SLAM-tracking notice dialog |  |
| `showHandShakeDialog` | `(ActionListener listener, String type, String updateStr)` | `int` | Show the controller vibration and update dialog |  |
| `showSystemUpdateDialog` | `(String params)` | `void` | Show the system update dialog; params is passed through |  |
| `nativeShowRelocateSuccessSafeArea` | `(long handle)` | `int` | Native entry point showing the safe-area relocation success window | JNI only |
| `nativeShowResetSuccessSafeArea` | `(long handle)` | `int` | Native entry point showing the safe-area reset success window | JNI only |
| `setKioskListener` | `(KioskListener listener)` | `void` | Set the KIOSK request callback |  |
| `setKioskApp` | `(String pkg, boolean value)` | `void` | Allow or deny the given app in KIOSK mode |  |
| `setKioskSettings` | `(int settingsType, boolean value)` | `void` | Set a KIOSK setting |  |
| `getKioskSettings` | `(int settingsType)` | `boolean` | Read a KIOSK setting |  |
| `getKioskApp` | `(String pkg)` | `boolean` | Check whether the given app is in the KIOSK allow list |  |
| `inputKioskPwdSuccess` | `()` | `void` | Notify that the KIOSK password was accepted |  |
| `setKioskPwdFail` | `()` | `boolean` | Notify that the KIOSK password was rejected; returns the operation result |  |
| `canInputKioskPwd` | `()` | `boolean` | Check whether a KIOSK password may be entered now |  |

##### Public Fields

| Type | Field | Description |
| --- | --- | --- |
| `boolean` | `isUpdateDialogVisible` | Whether the system update dialog is currently visible; updated internally by the SDK |

##### Interface: KioskListener

KIOSK mode request callback interface. Implement it and register via `setKioskListener`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `requestKiosk` | `()` | Invoked when the system requests entering KIOSK mode |

### 2.24 CustomGlobalWindow (Custom Window)

Package: `com.ssnwt.vr.globalwindow`

Obtain via: `CustomGlobalUtils.getInstance()` (custom window management singleton); `CustomGlobalWindow` is an abstract base class to be extended by your app

Custom global window solution: extend `CustomGlobalWindow` and implement `initType()` / `initContentView()` / `initInfo()`. Internally a `VirtualDisplay` plus a `Presentation` renders your custom View into the global window system (the virtual display name prefix `CustomGlobalWindow` lets SurfaceFlinger skip distortion correction). `CustomGlobalUtils` keeps three maps — pending windows, currently shown windows, and same-type overrides — and switches to the main-thread Handler to drive each window when a system callback arrives.

##### CustomGlobalWindow (abstract base class)

> ⚠️ Global windows are only supported on VQ920 / VQ930 devices.

> ⚠️ Subclasses must implement the protected abstract methods `initType()`, `initContentView()` and `initInfo()`, and must call `init()` after construction to complete initialization.

**Show flow:** `CustomGlobalUtils.requestShow(window)` returns the window id → the system invokes `CustomGlobalWindowListener.onShow(int id, Surface surface)` → the framework automatically calls `CustomGlobalWindow.show(surface)` to create the virtual display on that Surface and render the content. `setVisibility()` / `setInVisibility()` are empty in the base class (the Unity side controls visibility); override them to refresh UI data.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `CustomGlobalWindow` | `(Context context)` | `CustomGlobalWindow` | Constructor; only stores the Context, call `init()` afterwards | Constructor |
| `init` | `()` | `CustomGlobalWindow` | Runs `initType()`, `initContentView()` with size measurement and `initInfo()`; returns this for chaining |  |
| `show` | `(Surface surface)` | `void` | Creates the VirtualDisplay on the given Surface and shows the content view | Skips showing and logs when the display is invalid |
| `setVisibility` | `()` | `void` | Empty implementation; visibility is driven by the Unity side, so a state field is needed to refresh UI data | Intended to be overridden |
| `setInVisibility` | `()` | `void` | Empty implementation | Intended to be overridden |
| `hide` | `()` | `void` | Dismisses the Presentation and releases the virtual display |  |
| `getGlobalWindowInfo` | `()` | `GlobalWindowInfo` | Returns the window descriptor submitted to the system |  |
| `injectTouchEvent` | `(MotionEvent event)` | `void` | Dispatches the touch event to the hosting view | Logs an error and returns when Presentation is null |
| `updateFocusPosition` | `(int x, int y)` | `void` | Builds and dispatches an ACTION_HOVER_MOVE event for the hover effect |  |
| `performClickDown` | `(int x, int y)` | `void` | Builds and dispatches an ACTION_DOWN event |  |
| `performActionMove` | `(int x, int y)` | `void` | Builds and dispatches an ACTION_MOVE event |  |
| `performClickUp` | `(int x, int y)` | `void` | Builds and dispatches an ACTION_UP event |  |

##### CustomGlobalUtils (singleton)

Private constructor — obtain it only through `getInstance()`. It relies on `AndroidInterface.getInstance().getGlobalWindowUtils()`, so AndroidInterface must be initialized first. The singleton registers a `CustomGlobalWindowListener` in its constructor; your app does not need to register one again.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `CustomGlobalUtils` | Returns the lazily created singleton | public static synchronized |
| `requestShow` | `(CustomGlobalWindow customGlobalWindow)` | `int` | Requests to show a custom window; returns the unique window id and stores the window in the pending map |  |
| `requestHide` | `(int infoId)` | `void` | Requests to hide the custom window with the given id |  |
| `addOverrideCustomView` | `(CustomGlobalWindow globalWindow)` | `void` | Overrides the global window of the same type with a custom window and records it in the override map | Returns void, unlike the int-returning method of the same name in GlobalWindowUtils |

##### Public fields

| Field | Type | Description |
| --- | --- | --- |
| `mHandler` | `android.os.Handler` | public final, bound to the main Looper. Window show / hide / event injection are all posted to the main thread through it; callers may also use it to post their own main-thread work |

##### CustomOffscreenPresentation

Extends `android.app.Presentation` and acts as the off-screen host container on the virtual display. It is created internally by `CustomGlobalWindow.show(surface)`, so apps normally never use it directly. Its `onCreate` sets a transparent background, immersive full screen and window type `TYPE_PRIVATE_PRESENTATION` (requires a system signature or platform permission; failures are logged as stack traces, never thrown). It also exposes `injectTouchEvent(MotionEvent)` and `injectGenericMotionEvent(MotionEvent)`, which dispatch events straight to the decorView on the touch and generic motion channels respectively. Constructor: `CustomOffscreenPresentation(Context context, Display display)`.

### 2.25 GlobalWindowInfo (Window Info)

Package: `com.ssnwt.vr.globalwindow`

Obtain via: create it with the full-argument constructor, or receive it from the `GlobalWindowListener.onRequestShow(GlobalWindowInfo)` callback

`GlobalWindowInfo` implements `android.os.Parcelable` and describes the complete rendering parameters of one global window (type, size, layer, position, orientation, curved-surface parameters and so on). It is the data class used to pass window configuration across processes. A unique auto-incrementing id is assigned on construction; `isDialog` has no matching constructor parameter, so assign it directly after construction if needed.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `GlobalWindowInfo` | `(@GlobalWindowType int type, int width, int height, int scale, String name, int layer, int layerIndex, int faceType, int resolution, float curvatureAngle, int focusType, int anchorType, float[] center, float[] position, float[] rotation)` | `GlobalWindowInfo` | Full-argument constructor; assigns every field and allocates the id via `sID++` | Many same-typed parameters — the order is a hard contract; constructor |
| `describeContents` | `()` | `int` | Always returns 0, no special file descriptor | Overrides Parcelable |
| `writeToParcel` | `(Parcel dest, int flags)` | `void` | Writes all fields in a fixed order | Overrides Parcelable |
| `toString` | `()` | `String` | Returns a readable string containing every field | Overrides Object |

##### Public fields

| Field | Type | Description |
| --- | --- | --- |
| `sID` | `static int` | Global auto-incrementing id counter starting at 0; each instance takes `sID++` as its id. public static — external code can overwrite it |
| `type` | `int` (`@GlobalWindowType`) | Window type, see `GlobalWindowType` |
| `id` | `int` | Unique window identifier, auto-allocated from `sID++` on construction |
| `width` | `int` | Window width |
| `height` | `int` | Window height |
| `scale` | `int` | Scale factor. Conventional values: 1400 for TabBar and Dialog, 1720 for ShortCut and ShutDown, 200 for toast, 600 for the safe area |
| `name` | `String` | Window name, used when composing the virtual display name |
| `layer` | `int` | UI layer; higher values sit on top |
| `focusType` | `int` | Whether the window needs focus: 1 needs focus, 0 does not |
| `anchorType` | `int` | Window anchor type: 0 fixed position (e.g. Toast); 1 follows the view direction (e.g. safe area hints); 2 perspective scaling (e.g. main menu) |
| `layerIndex` | `int` | Display order within the same layer; higher values sit on top; default 0 |
| `faceType` | `int` | Type of the UI surface: 0 flat, 1 curved; default 0 |
| `isDialog` | `int` | Whether this is a dialog; when a dialog pops up all windows behind it are hidden; 1 means yes; default 0 |
| `center` | `float[]` | Center position of the UI in normalized coordinates, origin at the center by default. Bottom-left is (0,0), top-right is (1,1); default `{0.5f, 0.5f, 0}` |
| `position` | `float[]` | Display position in xyz order relative to the system origin; X left/right, Y up/down, Z forward/back; default `{0, 0, -2}` |
| `rotation` | `float[]` | UI rotation in xyz order, Euler angles per axis; default `{0}` |
| `resolution` | `int` | Curved-surface segment count; the curved area size shares width / height with the flat case. 16 segments is common — more segments look smoother and cost more performance; default 0 |
| `curvatureAngle` | `float` | Angle between the arc apex and the flat plane; larger is more curved, 0 is flat; default 0 |
| `CREATOR` | `Parcelable.Creator<GlobalWindowInfo>` | public static final, the Parcelable deserialization entry point; internally implements `createFromParcel(Parcel)` and `newArray(int)` |

##### GlobalWindowCommon (utility class)

Common utilities for global windows, providing size measurement and MotionEvent construction. Used internally by the framework; apps normally do not call these directly.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `measureSize` | `(View view)` | `android.util.Pair<Integer, Integer>` | Measures the View with UNSPECIFIED specs and returns the measured width and height | public static |
| `generateEvent` | `(int action, float x, float y)` | `MotionEvent` | Builds a MotionEvent from uptimeMillis with source set to SOURCE_TOUCHSCREEN | public static |

| Field | Type | Description |
| --- | --- | --- |
| `TAG` | `static final String` | Log tag, equal to the simple class name GlobalWindowCommon |

### 2.26 GlobalWindowType (Window Types)

Package: `com.ssnwt.vr.globalwindow`

Usage: pass these values as the `@GlobalWindowType` argument of `GlobalWindowUtils.requestShow(int type)`, `requestActionEvent(int type, int action)` and similar APIs

`GlobalWindowType` is a constant annotation (`@interface` backed by `@IntDef`) defining the type ids of built-in global windows and safe-area notice windows. The value space has three ranges: ordinary windows 0~25, safe area 101~107, custom view 1000.

| Constant | Value | Description |
| --- | --- | --- |
| `ALL` | 0 | Mainly used to operate on all windows at once |
| `TOAST` | 1 | Toast window, scale 200 |
| `DIALOG` | 2 | Dialog window, scale 1400 |
| `TAB_BARS` | 3 | TabBar window, scale 1400 |
| `SHORT_CUT` | 4 | Shortcut menu window, scale 1720 |
| `SHUT_DOWN` | 5 | Shut-down menu window, scale 1720 |
| `MY_APP` | 6 | My App window |
| `UNINSTALL_DIALOG` | 7 | Uninstall dialog |
| `KEY_BOARD` | 8 | Keyboard window |
| `LOGIN` | 9 | Login page window |
| `POLICY` | 10 | Privacy policy window |
| `VOLUME` | 11 | Volume window |
| `PUPIL_DISTANCE` | 12 | Interpupillary distance window |
| `SETTINGS` | 13 | Settings window |
| `HAND_SHAKE_DIALOG` | 14 | Controller vibration and update dialog |
| `UPDATE_DIALOG` | 15 | Update dialog |
| `RESET_DIALOG` | 16 | Reset dialog |
| `RESOURCE_CENTER` | 17 | Resource center window |
| `EXIT_APP_DIALOG` | 18 | Exit app dialog |
| `KIOSK_DIALOG` | 19 | KIOSK dialog |
| `SAMBA_DIALOG` | 20 | Samba dialog |
| `SPACE_ORIENTATION_DIALOG` | 21 | Space orientation dialog |
| `NOTE_NO_SLAM_DIALOG` | 22 | No-SLAM-tracking notice dialog |
| `TRACKING_LOST` | 23 | Tracking lost notice window |
| `CREATE_MAP_DIALOG` | 24 | Create map dialog |
| `DISABLE_LARGE_SPACE_DIALOG` | 25 | Disable large-space dialog |
| `CUSTOM_VIEW` | 1000 | Custom view type, used by third-party custom windows |
| `SAFE_AREA_RELOCATE` | 103 | Safe area: relocating, recovering the safe area |
| `LEAVE_SAFE_AREA` | 101 | Safe area: recovered but currently outside the configured boundary |
| `SAFE_AREA_RELOCATE_FAIL` | 102 | Safe area: recognition failed |
| `SAFE_AREA_RELOCATE_SUCCESS` | 104 | Safe area: recognition succeeded |
| `SAFE_AREA_RESET_SUCCESS` | 105 | Safe area: configured successfully |
| `FIND_SAFE_AREA` | 106 | Safe area: searching for the safe area |
| `RESET_BOUNDARY_DIALOG` | 107 | Safe area: second confirmation on whether to set the safe area |

### 2.27 GlobalActionType (Action Types)

Package: `com.ssnwt.vr.globalwindow`

Usage: pass these values as the `@GlobalActionType` action argument of `GlobalWindowUtils.requestActionEvent(int type, int action)`, or read them from the action argument of `GlobalWindowListener.onActionEvent(int type, int action)`

`GlobalActionType` is a constant annotation (`@interface` backed by `@IntDef`) describing the interaction event ids of the global menu, shortcuts, TabBar, dialogs, keyboard, login page and safe area.

> ⚠️ The `@IntDef` whitelist contains only 13 values (ALL_HIDE, EXPAND_OR_HIDE, TAB_BAR_ITEM_CLICK, TAB_BAR_EXIT_APP_CLICK, SHORTCUT_CLOSE, SHORTCUT_ITEM_CLICK, SHORTCUT_ENABLE_VST, APP_EXIT, DIALOG_DISMISS, SHUT_DOWN, REBOOT, CANCEL, MY_APP_EXPAND_OR_HIDE). The other 16 declared constants are not in the whitelist: they still work at runtime, but passing them as this annotation's argument triggers a lint warning.

| Constant | Value | Description |
| --- | --- | --- |
| `ALL_HIDE` | 0 | Hide all windows |
| `EXPAND_OR_HIDE` | 10 | TabBar quick expand or collapse |
| `TAB_BAR_ITEM_CLICK` | 11 | TabBar item clicked, waking a dialog |
| `TAB_BAR_EXIT_APP_CLICK` | 12 | Clicked to close the current app |
| `MY_APP_EXPAND_OR_HIDE` | 13 | MyApp quick expand or collapse |
| `RESOURCE_EXPAND_OR_HIDE` | 14 | resource_center expand or collapse |
| `SETTINGS_EXPAND_OR_HIDE` | 15 | settings quick expand or collapse |
| `SHORTCUT_CLOSE` | 20 | Shortcut menu closed (hidden) |
| `SHORTCUT_ITEM_CLICK` | 21 | ShortCut item clicked, waking a dialog |
| `SHORTCUT_ENABLE_VST` | 22 | ShortCut enables VST |
| `APP_EXIT` | 30 | Confirm exiting the app |
| `DIALOG_DISMISS` | 31 | Dialog dismissed |
| `SHUT_DOWN` | 40 | Shut down |
| `REBOOT` | 41 | Reboot |
| `CANCEL` | 42 | Cancel |
| `UNINSTALL_DIALOG_SHOW` | 71 | Uninstall dialog shown |
| `UNINSTALL_DIALOG_DISMISS` | 72 | Uninstall dialog dismissed |
| `SHOW_KEY_BOARD` | 80 | Keyboard shown |
| `HIDE_KEY_BOARD` | 81 | Keyboard hidden |
| `HIDE_TAB_BAR` | 82 | Hide TabBar |
| `SHOW_TAB_BAR` | 83 | Show TabBar |
| `HIDE_SYSTEM_UPDATE` | 84 | Hide the system update popup |
| `HIDE_OR_SHOW_LOGIN` | 91 | Login page expand or collapse |
| `HIDE_LOGIN` | 92 | Login page closed |
| `FIND_SAFE_AREA_1` | 1 | Enter with the current boundary |
| `HAND_SHAKE` | 131 | Controller vibration |
| `Boundary_Enable` | 132 | Open safe-area settings |
| `Boundary_Unable` | 133 | Close safe-area settings |
| `START_LAUNCHER` | 141 | Force the controller-update launcher |

**Usage notes:** these values and `GlobalWindowType` often appear as adjacent arguments of `requestActionEvent(int type, int action)`, and the two value spaces overlap (both contain 1) — do not mix them up. `Boundary_Enable` / `Boundary_Unable` use an underscore-plus-capital naming style that differs from the other constants.

### 2.28 Global Window Listeners

Package: `com.ssnwt.vr.globalwindow` (including the sub-package `com.ssnwt.vr.globalwindow.listener`)

Obtain via: implement the interfaces below and register them through `GlobalWindowUtils.setGlobalWindowListener` / `setCustomWindowListener` / `setKioskListener` respectively; `ActionListener` and `OnConfirmListener` are passed directly as arguments of the corresponding show methods

Four callback interfaces are exposed: `GlobalWindowListener` for built-in window events, `CustomGlobalWindowListener` for custom window events, `ActionListener` for dialog confirm/dismiss, and `OnConfirmListener` for safe-area confirmation.

> ⚠️ You must register a listener first (`setGlobalWindowListener` / `setCustomWindowListener`), otherwise global window calls will not deliver any callback.

##### GlobalWindowListener (built-in window events)

Global window callback interface on the current app side, registered via `GlobalWindowUtils.setGlobalWindowListener`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onRequestShow` | `(GlobalWindowInfo info)` | Invoked when showing a global menu is requested; info is the window descriptor |
| `onHide` | `(int id)` | Invoked when hiding a global menu; id is the unique window identifier |
| `onHomeEvent` | `(int type)` | Invoked after the system Home key is detected so the Unity side can control visibility and avoid flicker; type: 0 hide, 1 show, 2 long-press Home (menu recenter) |
| `onActionEvent` | `(int type, int action)` | type is the UI type (see `GlobalWindowType`), action is the click event (see `GlobalActionType`) |

##### CustomGlobalWindowListener (custom window events)

Callback interface for third-party custom windows, registered via `GlobalWindowUtils.setCustomWindowListener`. `CustomGlobalUtils` already registers an implementation internally and dispatches events, so apps normally do not register another one.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onShow` | `(int id, Surface surface)` | Invoked when showing a global menu is requested; surface is the render target provided by the system — a custom window must call `CustomGlobalWindow.show(surface)` inside this callback |
| `onHide` | `(int id)` | Invoked when hiding a global menu |
| `setVisibility` | `(int id)` | Request the window to become visible |
| `setInVisibility` | `(int id)` | Request the window to become invisible |
| `injectTouchEvent` | `(int id, MotionEvent event)` | Inject a touch event into the window |
| `updateFocusPosition` | `(int id, int x, int y)` | Update the hover focus position |
| `performClickDown` | `(int id, int x, int y)` | Inject a down event |
| `performActionMove` | `(int id, int x, int y)` | Inject a move event |
| `performClickUp` | `(int id, int x, int y)` | Inject an up event |
| `requestTypeView` | `(int id)` | Request the custom View of the given type (the framework moves it into the pending map) |

##### ActionListener (dialogs)

Listens for the result of global dialogs; passed as an argument of `showConfirmDialog` / `showUninstallDialog` / `showDeleteDialog` / `showKioskDialog` / `showSpaceOrientationDialog` / `showNoteNoSlamDialog` / `showHandShakeDialog`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `dismiss` | `()` | Invoked when the dialog disappears. A default method with an empty body — implementers may skip it |
| `exit` | `()` | Invoked when the user confirms exit. Abstract method — implementers must implement it |

##### OnConfirmListener (safe-area confirmation)

Confirmation callback of the safe-area success windows; passed as an argument of `showRelocateSuccessSafeArea` / `showResetSuccessSafeArea`.

| Callback | Parameters | Description |
| --- | --- | --- |
| `onConfirm` | `()` | Invoked when the user taps confirm |

**Native bridge classes:** `com.ssnwt.vr.globalwindow.GlobalWindowNative` and `com.ssnwt.vr.globalwindow.listener.GlobalWindowConfirmListenerNative` are the JNI bridge implementations of `GlobalWindowListener` and `OnConfirmListener`; they forward callbacks to native methods (`nativeOnRequestShow` / `nativeOnHide` / `nativeOnHomeEvent` / `nativeOnActionEvent` / `nativeOnConfirm`). They are for JNI use only and are typically created internally by `GlobalWindowUtils.initGlobalWindowNative(long handle)` and `nativeShowRelocateSuccessSafeArea` / `nativeShowResetSuccessSafeArea` — the Java layer never instantiates them directly.

### 2.29 ControllerManager (Controller Data)

Package: `com.ssnwt.vr.svrcontroller`

Obtain via: `ControllerManager.getInstance()`

Controller data index layout. The `INDEX_xxx` constants define the offsets inside the controller data array. One controller data group has a length of `GROUP_DATA_SIZE = 30`; raw data obtained from `getData(float[])` is parsed item by item using the offsets below.

> ⚠️ Loading this class executes `System.loadLibrary("svr_controller_v2")`. The device must ship this native library and the controller service (`com.ssnwt.vr.server`). Controller state is polled through `getData(float[])`; there is no Listener callback.

> ⚠️ Only supported on 3DoF devices (V901 / S802 / BQ810 / S801); on 6DoF devices (VQ910 / VQ920 / VQ930) use `HandshankUtils` instead.

##### Data layout constants

| Constant | Value | Description |
| --- | --- | --- |
| `GROUP_DATA_SIZE` | `30` | Array length of a single controller data group |
| `INDEX_CONNECT_STATUS` | `0` | Connection state (`ConnectStatus`), length 1 |
| `INDEX_TYPE` | `1` | Controller type (`Type`), length 1 |
| `INDEX_HANDNESS` | `2` | Left/right hand state (`Handness`), length 1 |
| `INDEX_RECENTERED` | `3` | Recenter flag; 1 means the view needs to be recentered, length 1 |
| `INDEX_BATTERY` | `4` | Battery level (0-100), length 1 |
| `INDEX_ROTATION` | `5` | Orientation (x, y, z, w), length 4 |
| `INDEX_POSITION` | `9` | Position (x, y, z), length 3 |
| `INDEX_GATEWAY_POS` | `12` | Gateway position (x, y), length 3 |
| `INDEX_BUTTON_STATE` | `15` | Button state; the value is the key code of pressed buttons, multiple buttons may be held at the same time (`KeyCode` bit mask), length 2 |
| `INDEX_TOUCH_STATE` | `17` | Touchpad touch event; 1 means currently touching, length 1 |
| `INDEX_TOUCH_POS` | `18` | Touchpad touch coordinates (x, y), range (0-1), length 2 |
| `INDEX_TRIGGER_PROCESS` | `20` | Trigger press progress, range (0-1), length 1 |
| `INDEX_GRIP_PROCESS` | `21` | Grip press progress, range (0-1), length 1 |
| `INDEX_DEVICE_NAME` | `22` | Device name, at most 16 bytes, length 4 (use `getDeviceName(float[])` to decode) |
| `INDEX_RESERVED2` | `26` | Reserved data, length 4 |

##### Enum: ConnectStatus

| Value | Raw | Description |
| --- | --- | --- |
| `Disconnected` | `0` | Disconnected |
| `Scanning` | `1` | Scanning |
| `Connecting` | `2` | Connecting |
| `Connected` | `3` | Connected |
| `NoRecenter` | `4` | Not recentered |

##### Enum: Type (controller type)

| Value | Raw | Description |
| --- | --- | --- |
| `I3VR` | `0` | I3VR controller |
| `Nolo_6dof` | `1` | Nolo 6DoF controller |
| `Nolo_3dof` | `2` | Nolo 3DoF controller |

##### Enum: Handness

| Value | Raw | Description |
| --- | --- | --- |
| `Right` | `0` | Right hand |
| `Left` | `1` | Left hand |
| `Head` | `2` | Head mounted display |
| `Max` | `3` | Sentinel value marking the enum upper bound |

##### Enum: KeyCode (button bit mask, multiple buttons may be pressed together)

| Value | Raw | Description |
| --- | --- | --- |
| `Button_Up` | `0x00000001` | Touchpad up |
| `Button_Down` | `0x00000002` | Touchpad down |
| `Button_Left` | `0x00000004` | Touchpad left |
| `Button_Right` | `0x00000008` | Touchpad right |
| `Button_Enter` | `0x00000010` | System key Enter |
| `Button_Home` | `0x00000020` | System key Home |
| `Button_Menu` | `0x00000040` | System key Menu |
| `Button_Back` | `0x00000080` | System key Back |
| `Button_Volume_Up` | `0x00000100` | Volume up |
| `Button_Volume_Down` | `0x00000200` | Volume down |
| `Button_Grip` | `0x00000400` | Grip button |
| `Button_Trigger` | `0x00000800` | Trigger button |
| `Button_EnumSize` | `0x7fffffff` | Sentinel value marking the enum upper bound |

##### Public methods

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `ControllerManager` | Returns the singleton instance | Static method |
| `getDeviceName` | `(float[] data)` | `String` | Decodes the device name from the controller data array (equivalent to offset=0, size=data.length) | Static method |
| `getDeviceName` | `(float[] data, int offset, int size)` | `String` | Decodes the device name from the data starting at the given offset | Static method |
| `float2string` | `(float[] in, int offset, int size)` | `String` | Converts up to 4 floats (16 bytes) to little-endian bytes, strips trailing zeros and decodes the result as GBK | Static method; returns `"UNKNOWN"` when `size <= 0`, `size > 4` or the array is out of bounds |
| `string2float` | `(String in)` | `float[]` | Encodes the string as GBK and splits it into a float array (at most 4 floats), padding with zeros | Static method; returns `null` for an empty input |
| `getGBKBytes` | `(String in)` | `byte[]` | Returns the bytes encoded in GBK, falling back to UTF-8 when GBK is unsupported | Static method |
| `getGBKString` | `(byte[] in, int offset, int length)` | `String` | Decodes bytes as GBK, falling back to UTF-8 when GBK is unsupported | Static method |
| `startService` | `(Context context)` | `void` | Starts the controller service; may be called repeatedly. Action is `com.ssnwt.vr.svrapi.ISvrController`, package is `com.ssnwt.vr.server`; you may also start the service yourself | Requires a `Context` |
| `connect` | `()` | `int` | Connects to the controller service; returns 0 on success | native method |
| `disconnect` | `()` | `int` | Disconnects from the controller service; returns 0 on success | native method |
| `getData` | `(float[] data)` | `int` | Reads controller data from the service, parsed according to the `INDEX_*` layout; returns the length of the data read | native method |

### 2.30 PairManager (Controller Pairing)

Package: `com.ssnwt.vr.svrcontroller`

Obtain via: `PairManager.getInstance()`

Controller pairing management. It starts/stops controller scanning, cancels pairing, disconnects the controller and toggles the pairing switch. Every operation is performed by sending an Intent with an `action` extra to the controller service (`com.ssnwt.vr.server`). Pairing and connection results must be determined through `ControllerManager.getData(float[])`; this class provides no Listener callback.

> ⚠️ Only supported on 3DoF devices (V901 / S802 / BQ810 / S801); on 6DoF devices (VQ910 / VQ920 / VQ930) use `HandshankUtils` instead.

##### Pairing procedure

Standard controller:

1. Start controller scanning on the VR side (`search(Context)`).
2. Power on the controller (make sure it is not connected to another VR device).
3. Press and hold both the Home key and the App key (the two small round keys on the front of the controller, one marked with a small circle and the other with three small dots); the indicator LED stays solid.
4. Wait for the VR device to scan and bind. If it keeps failing, repeat steps 1, 2 and 3.

Nolo controller: power on the gateway device and the controller to be used, plug the VR head-mounted part of the controller into the USB port of the VR device; the controller connects automatically afterwards.

##### Public methods

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `getInstance` | `()` | `PairManager` | Returns the singleton instance | Static method |
| `search` | `(Context context)` | `void` | Scans for controllers. Once the first controller is found it is connected automatically and scanning stops. Whether it is paired and connected can be determined via `ControllerManager.getData(float[])`. If a controller is already paired, no new device will be paired until the paired device is cancelled | Passing `Activity#getApplicationContext()` is recommended |
| `stopSearch` | `(Context context)` | `void` | Stops scanning | Passing `Activity#getApplicationContext()` is recommended |
| `cancelPaired` | `(Context context)` | `void` | Cancels the pairing | Passing `Activity#getApplicationContext()` is recommended |
| `disconnect` | `(Context context)` | `void` | Disconnects the controller. To reconnect, simply long-press the controller Home key for about 1 second | Passing `Activity#getApplicationContext()` is recommended |
| `isPaired` | `()` | `boolean` | Whether a controller is currently paired; unrelated to whether a controller is currently connected. Returns true when paired, false when not paired | Reads the external storage file `/Controller/Runtime/CtrlMacAddr.txt` (external storage read permission required); treated as unpaired when the file is missing, empty, or the MAC is `ff:ff:ff:ff:ff:ff` |
| `enablePair` | `(Context context)` | `void` | Allows pairing (pairing is disallowed by default and must be enabled manually) | Passing `Activity#getApplicationContext()` is recommended |
| `disablePair` | `(Context context)` | `void` | Disallows pairing (it is recommended to disable pairing after a successful pairing to prevent cross-pairing between controllers). | Passing `Activity#getApplicationContext()` is recommended |

### 2.31 AIDL Helper Classes

Package: `com.ssnwt.vr.svrapi`

Obtain via: delivered directly as AIDL callback parameters (the `parcelParams` of `SvrCallback.callback`), or created through the constructor and the `wrap(...)` static factory of each wrapper class.

This set of classes provides AIDL helpers for cross-process transport: `SvrCallback` is the callback base class that applications extend, while the other four classes wrap a Parcelable value, a byte array, a Parcelable list and a string list respectively. All of them implement `Parcelable`.

##### `SvrCallback` — AIDL callback base class

`public abstract class SvrCallback extends ISvrCallback.Stub`. Applications extend it and pass the instance as the callback of various command APIs, avoiding a direct dependency on the AIDL-generated `Stub`. The class itself declares no methods; the callback method below comes from its superclass and must be implemented.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `callback` | `(int callbackId, List<String> params, List<SvrParcel> parcelParams)` | `void` | Command result callback. `callbackId` is the command ID; `params` is the string parameter list returned by that command (its length depends on the command, the result is usually carried by `params.get(0)`); `parcelParams` is the returned list of Parcelable wrappers | Declared `throws RemoteException`; must be implemented by subclasses |

##### `SvrParcel` — Parcelable value wrapper

`public class SvrParcel<T extends Parcelable> implements Parcelable` wraps a single Parcelable value for AIDL parameters and callbacks.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `SvrParcel` | `(T value)` | — | Wraps the given Parcelable value | Constructor |
| `wrap` | `(Parcelable value)` | `SvrParcel` | Static factory, equivalent to the constructor | Static method |
| `getValue` | `()` | `T` | Returns the wrapped value | — |
| `equals` | `(Object obj)` | `boolean` | True when the type matches and the wrapped values are equal | — |
| `hashCode` | `()` | `int` | Hash of the wrapped value | — |
| `toString` | `()` | `String` | String form of the wrapped value | — |

##### `SvrByteArray` — byte array wrapper

`public class SvrByteArray implements Parcelable` wraps a `byte[]` so that byte arrays can be transported over AIDL.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `SvrByteArray` | `(byte[] bytes)` | — | Wraps the given byte array | Constructor; the argument must not be null |
| `wrap` | `(byte[] bytes)` | `SvrByteArray` | Static factory, equivalent to the constructor | Static method |
| `getValue` | `()` | `byte[]` | Returns the original byte array | — |
| `equals` | `(Object thatObject)` | `boolean` | True when the byte contents are equal | Based on `Arrays.equals` |
| `hashCode` | `()` | `int` | Hash of the byte array contents | Based on `Arrays.hashCode` |

##### `SvrParcelArray` — Parcelable list wrapper

`public class SvrParcelArray<T extends Parcelable> implements Parcelable` wraps an `ArrayList<T>` for bulk Parcelable transport over AIDL.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `SvrParcelArray` | `(ArrayList<T> list)` | — | Wraps the given Parcelable list | Constructor; the argument must not be null |
| `wrap` | `(ArrayList<T> list)` | `SvrParcelArray` | Static factory, equivalent to the constructor | Static method |
| `getValue` | `()` | `ArrayList<T>` | Returns the list contents | — |
| `equals` | `(Object obj)` | `boolean` | True when the type matches and the list contents are equal | — |
| `hashCode` | `()` | `int` | Hash of the list contents | — |
| `toString` | `()` | `String` | String form of the list | — |

##### `SvrStringArray` — string list wrapper

`public class SvrStringArray implements Parcelable` wraps an `ArrayList<String>`.

| Method | Signature | Returns | Description | Notes |
| --- | --- | --- | --- | --- |
| `SvrStringArray` | `(ArrayList<String> list)` | — | Wraps the given string list | Constructor; the argument must not be null |
| `readStringList` | `(Parcel in)` | `ArrayList<String>` | Parses the string list from a Parcel: reads the int length first, then reads the strings one by one; returns null when the length is negative | Called internally during deserialization |
| `wrap` | `(ArrayList<String> list)` | `SvrStringArray` | Static factory, equivalent to the constructor | Static method |
| `getValue` | `()` | `ArrayList<String>` | Returns the list contents | — |
| `equals` | `(Object obj)` | `boolean` | True when the type matches and the list contents are equal | — |
| `hashCode` | `()` | `int` | Hash of the list contents | — |
| `toString` | `()` | `String` | String form of the list | — |

All classes above implement `Parcelable` and expose a `CREATOR` constant used by the framework for deserialization. `describeContents()` always returns 0 and `writeToParcel(Parcel dest, int flags)` writes the internal data; both are standard Parcelable implementations and do not need to be called directly.

## 3. Appendix

### 3.1 Version

| Item | Value |
| --- | --- |
| Artifact | `svr_plugin_android_api.aar` |
| versionCode | 20 |
| versionName | 1.0.20-<git commit> (commit hash appended at build time) |
| ABI | armeabi-v7a, arm64-v8a |
| minSdkVersion | 26 |

### 3.2 ProGuard

The AAR ships an empty `consumer-rules.pro` (no built-in keep rules). If your app enables code shrinking, add the following to your app's `proguard-rules.pro`:

```
-keep class com.ssnwt.vr.** { *; }
```
