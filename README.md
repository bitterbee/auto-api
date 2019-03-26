# AutoApi


```
    通过注解实现模块组件化的一个框架
```

##### [![Join the chat at https://gitter.im/alibaba/ARouter](https://badges.gitter.im/alibaba/ARouter.svg)](https://gitter.im/alibaba/ARouter?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

#### 最新版本

模块|autoapi|autoapi-plugin|autoapi_process|
---|---|---|---
最新版本|1.1.0|1.1.0|1.1.0

## 一、功能介绍
1. **支持通过注解自动生成接口工程代码**
2. **支持静态 public 方法和普通 public 方法**
3. **通过注解构造函数和静态方法，支持多构造函数接口实例创建**
4. **支持服务方法泛型**
5. **支持方法自定义参数生成 api 类替换，避免参数类型下沉**
6. **支持 callback 参数类型**
7. **支持提供父类 public 方法**
8. **支持 EventBus 通信**
9. 提供开关，关闭 api 工程代码自动生成

## 二、应用场景
1. 业务模块分离，实现组件化的通信方式，解耦、接口（非协议）方式通信
2. 跨模块工程之间 EventBus 通信

## 三、功能使用

### 1. SDK 引入

1. 提供接口服务的原 module 工程配置

	* apiProjectPath：当前工程对应的接口工程路径
	* packageName：当前工程对应的接口工程包名
	* apiBuildEnable：编译期是否重新生成接口工程代码
	* logOpen：编译期代码生成逻辑，是否打开本地日志

	```gradle
	apply plugin: 'android-aspectjx'
	
	repositories {
	    maven { url 'https://raw.githubusercontent.com/bitterbee/mvn-repo/master/' }
	}
	
	android {
	    defaultConfig {
	        javaCompileOptions {
	            annotationProcessorOptions {
	                def path = "${rootProject.projectDir}/${接口工程文件夹名}".toString()
	                arguments = [apiProjectPath: path,
	                             packageName: "${接口工程包名}",
	                             apiBuildEnable: "true",
	                             logOpen: "true"]
	            }
	        }
	    }
	}
	
	
	dependencies {
	    compile 'com.netease.libs:autoapi:1.1.0'
	    compile 'com.netease.libs:autoapi-api:1.1.0'
	    annotationProcessor 'com.netease.libs:autoapi_process:1.1.0'
	    compileOnly 'org.aspectj:aspectjrt:1.8.9'
	}
	```


2. 接口工程配置

	>配置生成 `api_records/${packagename}.records.txt` 文件，支持接口工程 clean 操作。
	>每次编译生成接口工程代码时，在 `${packagename}.records.txt` 文件记录接口类，clean 操作的时候，对照接口工程源码和 records.txt，删除真正需要删除的接口类

	```gradle
	clean.doLast {
	    def recordDir = file("${project.projectDir}/api_records/")
	    if (!recordDir.exists() || !recordDir.directory) {
	        return
	    }
	
	    def contents = new ArrayList<String>()
	    for (File recordFile : recordDir.listFiles()) {
	        if (recordFile.exists() && !recordFile.directory) {
	            def text = recordFile.text
	            if (text != null && !text.empty) {
	                contents.add(recordFile.text)
	            }
	        }
	    }
	
	    if (contents.empty) {
	        return
	    }
	
	    def sourcePath = "${project.projectDir}/src/main/java/"
	    def sourceDir = file(sourcePath)
	    def records = new ArrayList<String>()
	    for (String content : contents) {
	        for (String r : content.split(";\\n")) {
	            def record = r.trim()
	            if (record != null && !record.isEmpty()) {
	                // println "record file " + sourcePath + record
	                records.add(sourcePath + record)
	            }
	        }
	    }
	
	    def removePaths = subtract(recursivlyGetFiles(sourceDir), records)
	    for (String path : removePaths) {
	        if (path.endsWith(".java")) {
	            file(path).delete()
	            println "remove file " + path
	        }
	    }
	}
	
	List<String> subtract(List<String> a, List<String> b) {
	    if (a == null || a.empty || b == null || b.empty) {
	        return a
	    }
	    def res = new ArrayList<String>(a)
	    for (String item : b) {
	        if (res.contains(item)) {
	            res.remove(item)
	        }
	    }
	    return res
	}
	
	List<String> recursivlyGetFiles(File dir) {
	    def result = new ArrayList<String>()
	    if (dir == null || !dir.exists() || !dir.directory) {
	        return result
	    }
	    for (File sub : dir.listFiles()) {
	        if (sub.directory) {
	            result.addAll(recursivlyGetFiles(sub))
	        } else {
	            result.add(sub.absolutePath)
	        }
	    }
	    return result
	}
	```

### 2. 框架基础使用

1. 接口实现工程（app工程）：

	* AutoApiClassAnno 注解类指定提供接口服务类
	* AutoApiMethodAnno 注解 public 方法，指定服务接口类提供的方法
	
	```java
	@AutoApiClassAnno
	public class DeviceUtil {
	
	    @AutoApiMethodAnno
	    public int getDeviceId() {
	        return 0;
	    }
	}
	```

2. 接口使用工程（demomodule1）：

	接口工程引用：
	
	```gradle
	compile project(':app-api') // 接口工程名自行制定
	```
	
	接口服务使用：
	
	```java
	DeviceUtilApiFactory deviceUtilFactory = AutoApi.getApiFactory("DeviceUtilApi");
	DeviceUtilApi deviceUtil = deviceUtilFactory.newInstance();
	int id = deviceUtil.getDeviceId();  // id == 0
	```
	
### 3. 框架高级使用

#### 1. 多构造方式

1. 接口实现工程（app工程）：

	* `AutoApiConstructAnno`：注解构造函数
	* `AutoApiClassBuildMethodAnno`：注解静态方法返回实例对象

	```java
	@AutoApiClassAnno
	public class AddUtil {
	
	    private int mData1 = 0;
	    private int mData2 = 0;
	
	    @AutoApiConstructAnno
	    public AddUtil(int data1, int data2) {
	        mData1 = data1;
	        mData2 = data2;
	    }
	
	    @AutoApiConstructAnno
	    public AddUtil(int data) {
	        mData1 = data;
	        mData2 = data;
	    }
	
	    @AutoApiClassBuildMethodAnno
	    public static AddUtil getInstance(int data1, int data2) {
	        return new AddUtil(data1, data2);
	    }
	
	    @AutoApiMethodAnno
	    public int calu() {
	        return mData1 + mData2;
	    }
	}
	```

2. 接口使用工程（demomodule1）：

	接口服务使用：
	
	```java
	AddUtilApiFactory factory = AutoApi.getApiFactory("AddUtilApi");
    AddUtilApi api0 = factory.newInstance(11, 12);
    AddUtilApi api1 = factory.newInstance(3);
    AddUtilApi api2 = factory.getInstance(1, 2);
    int result0 = api0.calu(); // 23
    int result1 = api1.calu(); // 6
    int result2 = api1.calu(); // 3
	```
	
#### 2. 支持普通方法和静态方法

1. 接口实现工程（app工程）：

	```java
	@AutoApiClassAnno
	public class Minus {
	
	    private int mData1 = 0;
	    private int mData2 = 0;
	
	    @AutoApiConstructAnno
	    public Minus(int data1, int data2) {
	        mData1 = data1;
	        mData2 = data2;
	    }
	
	    @AutoApiMethodAnno
	    public int calu() {
	        return mData1 - mData2;
	    }
	
	    @AutoApiMethodAnno
	    public static int minus(int a, int b) {
	        return a - b;
	    }
	}
	```
	
2. 接口使用工程（demomodule1）：

	接口服务使用：

	```java
	MinusApiFactory factory = AutoApi.getApiFactory("MinusApi");
    MinusApi api = factory.newInstance(10, 5);
    int result0 = api.calu(); // 5
    int result1 = api.minus(10, 5); // 5
    ```
    >接口类不区分是否静态方法

#### 3. 支持单例

1. 接口实现工程（app工程）：

	```java
	@AutoApiClassAnno
	public class Singleton {
	
	    private static Singleton sInstance = null;
	
	    @AutoApiClassBuildMethodAnno()
	    public static Singleton getInstance() {
	        if (sInstance == null) {
	            synchronized (Singleton.class) {
	                if (sInstance == null) {
	                    sInstance = new Singleton();
	                }
	            }
	        }
	
	        return sInstance;
	    }
	
	    private Singleton() {
	    }
	
	    @AutoApiMethodAnno()
	    public String foo1(String str1, String str2) {
	        Log.i("Singleton", "foo1 called");
	        return str1 + "_" + str2;
	    }
	}
	```

2. 接口使用工程（demomodule1）：

	接口服务使用：

	```java
	SingletonApiFactory apiFactory = AutoApi.getApiFactory("SingletonApi");
    SingletonApi singleton = apiFactory.getInstance();
    String result = singleton.foo1("var1", "var2");
    ```
    
#### 4. 支持自定义参数和返回值类型转换

1. 接口实现工程（app工程）：

	```java
	@AutoApiClassAnno
	public class DataModelOperator {
	
	    @AutoApiMethodAnno
	    public static DataModel add(DataModel a, DataModel b) {
	        return a == null || b == null ?
	                null :
	                new DataModel(a.getA() + b.getA(), a.getB() + b.getB());
	    }
	}
	```
    
    ```java
    @AutoApiClassAnno
	public class DataModel {
	
	    private int a;
	    private int b;
	
	    @AutoApiConstructAnno()
	    public DataModel(int a, int b) {
	        this.a = a;
	        this.b = b;
	    }
	
	    public DataModel() {
	        this.a = 0;
	        this.b = 0;
	    }
	
	    public int getA() {
	        return a;
	    }
	
	    public int getB() {
	        return b;
	    }
	}
    ```
    
    生成接口类：
    
    ```java
	public interface DataModelOperatorApi extends ApiBase {
		DataModelApi add(DataModelApi a, DataModelApi b);
	}
    ```
    
    ```java
    public interface DataModelApi extends ApiBase {
	}
	```
    
2. 接口使用工程（demomodule1）：

	接口服务使用：
	
	```java
	DataModelOperatorApiFactory factory = AutoApi.getApiFactory("DataModelOperatorApi");
    DataModelOperatorApi api = factory.newInstance();

    DataModelApiFactory dataFactory = AutoApi.getApiFactory("DataModelApi");
    DataModelApi model1 = dataFactory.newInstance(4, 5);
    DataModelApi model2 = dataFactory.newInstance(1, 2);
    DataModelApi result = api.add(model1, model2);
    ```
    
#### 5. callback 类型参数转换

1. 接口实现工程（app工程）：

	* listener 参数属于回调，添加注解 `AutoApiCallbackAnno`

	```java
	@AutoApiClassAnno(allPublicNormalApi = true)
	public class HttpUtil {
	
	    public Request query(@AutoApiCallbackAnno HttpListener listener) {
	        return new Request();
	    }
	}
	```

	```java
	@AutoApiClassAnno(allPublicNormalApi = true)
	public class Request {
	
	    public void cancel() {
	        Log.i("AutoApi", "cancel request");
	    }
	}
	```

	```java	
	@AutoApiClassAnno(allPublicNormalApi = true, includeSuperApi = true)
	public interface HttpListener extends BaseHttpListener {
	    void onCancel();
	}
	```

2. 接口使用工程（demomodule1）：

	接口服务使用：

	```java
	HttpUtilApiFactory factory = AutoApi.getApiFactory("HttpUtilApi");
    HttpUtilApi api = factory.newInstance();
    RequestApi request = api.query(new HttpListenerApi() {
        @Override
        public void onCancel() { }

        @Override
        public void onHttpSuccess(String httpName, Object result) { }

        @Override
        public void onHttpError(String httpName, int errorCode, String errorMsg) { }

        @Override
        public Object getApiServiceTarget() {
            return null;
        }
    });

    // 根据业务场景执行请求取消操作
    request.cancel();
    ```
	
#### 6. 支持泛型

1. 接口实现工程（app工程）：

	```java
	@AutoApiClassAnno
	public class JsonUtil {
	
	    @AutoApiMethodAnno
	    public static <T> T toJsonObj(String jsonStr, Class<T> clazz) {
	        if (TextUtils.isEmpty(jsonStr)) {
	            return null;
	        }
	        try {
	            return JSONObject.parseObject(jsonStr, clazz, Feature.IgnoreNotMatch);
	        } catch (Throwable e) {
				e.printStackTrace();
	        }
	        return null;
	    }
	}
	```
	
2. 接口工程代码 (app-api工程)
	
	```java
	public interface JsonUtilApi extends ApiBase {
	  <T> T toJsonObj(String jsonStr, Class<T> clazz);
	}
	```
	
#### 7. 支持 EventBus

1. 接口实现工程（app工程）：

	```java
	@AutoApiClassAnno()
	public class EventA {
		
	}
	```

2. 接口使用工程（demomodule1）：

	接口服务使用：
	
	```java
	EventAApiFactory factory = AutoApi.getApiFactory("EventAApi");
	EventAApi api = factory.newInstance();
	EventBus.getDefault().post(api.getApiServiceTarget());
	```
	
#### 8. 支持别名，支持导出父类接口

1. 接口实现工程（app工程）：

	```java
	@AutoApiClassAnno(name = "CalculatorAlias", allPublicStaticApi = true, includeSuperApi = true)
	public class Calculator extends Adder {
	
	    public static int minuse(int a, int b) {
	        return a - b;
	    }
	
	    public static int multiply(int a, int b) {
	        return a * b;
	    }
	}
	```
	
	```java
	public class Adder {
    	public static int add(int a, int b) {
	        return a + b;
	    }
	}
	```

2. 接口使用工程（demomodule1）：

	接口服务使用：
				
	```java
	CalculatorAliasApiFactory factory = AutoApi.getApiFactory("CalculatorAlias");
    CalculatorAlias calculator = factory.newInstance();
    int add = calculator.add(1,2); // 3
    int minuse = calculator.minuse(2, 1); // 1
    int multiply = calculator.multiply(2,3); //6
    ```