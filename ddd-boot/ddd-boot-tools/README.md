# ddd-boot-tools
常用工具类
* [字符串](#字符串)
* [时间](#时间)
* [数字](#数字)
* [集合](#集合)
* [反射](#反射)
* [安全](#安全)

## 字符串

### 常用
`io.github.chensheng.dddboot.tools.text.TextUtil`
* isBlank：是否为空白字符串（NULL、空串、空格）
* isNotBlank：是否为非空白字符串
* isEmpty：是否为空字符串（NULL、空串）
* isNotEmpty：是否为百空字符串
* concatByComma：使用逗号连接字符串
* splitByComma：按逗号分割字符串
* substringAfter：提取指定字符串后的字符
* substringBefore：提取指定字符串前的字符
* getRandomStr：生成随机字符串
* underscoreToCamel：下划线格式转成鸵峰格式
* camelToUnderscore：鸵峰格式转成下划线格式
* maskMobile：手机号脱敏
* startsWith：字符串前缀校验
* endsWith：字符串后缀校验
* 其他方法可查阅`org.apache.commons.lang3.StringUtils`

### 格式校验
`io.github.chensheng.dddboot.tools.text.TextValidator`
* isMobileSimple：手机号，1字头＋10位数字即可
* isMobileExact：手机号，已知3位前缀＋8位数字
* isTel：固定电话号码，可带区号，然后6至少8位数字
* isIdCard：身份证号，身份证号码18位, 数字且关于生日的部分必须正确
* isEmail：电子邮箱地址
* isUrl：URL, 必须有"://",前面必须是英文，后面不能有空格
* isDate：日期，验证yyyy-MM-dd格式的日期校验，已考虑平闰年
* isIp： IP地址

### MD5
`io.github.chensheng.dddboot.tools.text.MD5Util`
* md5With16: 生成16位MD5
* md5With32：生成32位MD5

### 编解码
`io.github.chensheng.dddboot.tools.text.EncodeUtil`
* encodeHex: Base16编码
* decodeHex: Base16解码
* encodeBase64：Base64编码
* decodeBase64: Base64解码
* encodeBase64UrlSafe：Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)
* decodeBase64UrlSafe：Base64解码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)

### 转义
`io.github.chensheng.dddboot.tools.text.EscapeUtil`
* urlEncode：URL 编码, Encode默认为UTF-8
* urlDecode：URL 解码, Encode默认为UTF-8
* escapeXml：Xml编码，将字符串编码为符合XML1.1格式的字符串
* unescapeXml：Xml解码，XML格式的字符串解码为普通字符串
* escapeHtml：Html编码，将字符串编码为符合HTML4格式的字符串
* unescapeHtml：Html解码，将HTML4格式的字符串转码解码为普通字符串

### Hash散列
`io.github.chensheng.dddboot.tools.text.HashUtil`
* sha1：对输入字符串进行sha1散列
* sha1File：对文件进行sha1散列
* crc32AsInt：对输入字符串进行crc32散列返回int
* murmur32AsInt：对输入字符串进行murmur32散列
* generateSalt：用SecureRandom生成随机的byte[]作为salt


## 时间

### 常用
`io.github.chensheng.dddboot.tools.time.DateUtil`
* isSameDay：是否同一天
* isSameTime：是否同一时刻
* isLeapYear：是否闰年
* isBetween：判断日期是否在范围内，包含相等的日期
* getMonthLength：获取某个月有多少天, 考虑闰年等因数
* getDayOfWeek：获得日期是一周的第几天. 中国习惯，1 是Monday，而不是Sundays
* getDayOfYear：获得日期是一年的第几天，返回值从1开始
* getWeekOfMonth：获得日期是一月的第几周，返回值从1开始
* getWeekOfYear：获得日期是一年的第几周，返回值从1开始
* beginOfYear：一年的开始时间，2016-11-10 07:33:23, 则返回2016-1-1 00:00:00
* endOfYear：一年的结束时间，2016-11-10 07:33:23, 则返回2016-12-31 23:59:59.999
* nextYear：下一年，2016-11-10 07:33:23, 则返回2017-1-1 00:00:00
* beginOfMonth：月的开始时间，2016-11-10 07:33:23, 则返回2016-11-1 00:00:00
* endOfMonth：月的结束时间，2016-11-10 07:33:23, 则返回2016-11-30 23:59:59.999
* nextMonth：下个月，2016-11-10 07:33:23, 则返回2016-12-1 00:00:00
* beginOfWeek：周的开始时间，2017-1-20 07:33:23, 则返回2017-1-16 00:00:00
* endOfWeek：周的结束时间，2017-1-20 07:33:23, 则返回2017-1-22 23:59:59.999
* nextWeek：下一周，2017-1-20 07:33:23, 则返回2017-1-22 00:00:00
* beginOfDate：一天的开始时间，2016-11-10 07:33:23, 则返回2016-11-10 00:00:00
* endOfDate：一天的结束时间，2017-1-23 07:33:23, 则返回2017-1-23 23:59:59.999
* nextDate：下一天，2016-11-10 07:33:23, 则返回2016-11-11 00:00:00
* beginOfHour：某小时的开始时间，2016-12-10 07:33:23, 则返回2016-12-10 07:00:00
* endOfHour：某小时的结束时间，2017-1-23 07:33:23, 则返回2017-1-23 07:59:59.999
* nextHour：下个小时，2016-12-10 07:33:23, 则返回2016-12-10 08:00:00
* beginOfMinute：某分钟的开始时间，2016-12-10 07:33:23, 则返回2016-12-10 07:33:00
* endOfMinute：某分钟的结束时间，2017-1-23 07:33:23, 则返回2017-1-23 07:33:59.999
* nextMinute：下一分钟，2016-12-10 07:33:23, 则返回2016-12-10 07:34:00
* addMonths：加N月
* subMonths：减N月
* addWeeks：加N周
* subWeeks：减N周
* addDays：加N天
* subDays：减N天
* addHours：加N小时
* subHours：减N小时
* addMinutes：加N分钟
* subMinutes：减N分钟
* addSeconds：加N秒
* subSeconds：减N秒

### 格式化
`io.github.chensheng.dddboot.tools.time.DateFormatUtil`
* parseDate：字符串转日期
* formatDate：格式化日期
* formatDuration：按HH:mm:ss.SSS格式，格式化时间间隔
* formatFriendlyTimeSpanByNow：打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX


## 数字

### 常用
`io.github.chensheng.dddboot.tools.number.NumberUtil`
* isNumber：判断字符串是否合法数字
* isHexNumber：判断字符串是否16进制
* toBytes：数字转字节数组
* toInt：字节数组转整型
* toLong：字节数组转长整型
* toDouble：字节数组转浮点型
* toInt：字符串转整型
* toLong：字符串转长整型
* toDouble：字符串转浮点型
* toIntObject：将10进制的String安全的转化为Integer
* toLongObject：将10进制的String安全的转化为Long
* toDoubleObject：将10进制的String安全的转化为Double
* hexToIntObject：将16进制的String转化为Integer
* hexToLongObject：将16进制的String转化为Long

### 货币金额
`io.github.chensheng.dddboot.tools.number.MoneyUtil`
* fen2yuan：人民币金额单位转换，分转换成元，取两位小数 例如：150 => 1.5
* yuan2fen：人民币金额单位转换，元转换成分，例如：1 => 100
* format：格式化金额，例如：1=>1.00
* prettyFormat：格式化金额，默认格式：#,##0.00 ,例如：33999999932.3333d 输出：33,999,999,932.33
* parseString：解析格式为0.00格式的字符串
* parsePrettyString：分析格式为#,##0.00格式的字符串

### 随机数
`io.github.chensheng.dddboot.tools.number.RandomUtil`
* nextInt：整型随机数
* nextLong：长整型随机数
* nextDouble：浮点型随机数
* randomStringFixLength：随机字母或数字，固定长度
* randomStringRandomLength：随机字母或数字，随机长度
* randomLetterFixLength：随机字母，固定长度
* randomLetterRandomLength：随机字母，随机长度
* randomAsciiFixLength：随机ASCII字符(含字母，数字及其他符号)，固定长度
* randomAsciiRandomLength：随机ASCII字符(含字母，数字及其他符号)，随机长度

### 数据大小
`io.github.chensheng.dddboot.tools.number.SizeUnit`
* toBytes：byte
* toKiloBytes：KB
* toMegaBytes: MB
* toGigaBytes：GB

### 数学计算
`io.github.chensheng.dddboot.tools.number.MathUtil`
* nextPowerOfTwo：往上找出最接近的2的倍数，比如15返回16， 17返回32
* previousPowerOfTwo：往下找出最接近2的倍数，比如15返回8， 17返回16
* isPowerOfTwo：是否2的倍数
* mod：保证结果为正数的取模
* divide：能控制rounding方向的int相除.
* pow：平方
* sqrt：开方

## 集合

### 常用
`io.github.chensheng.dddboot.tools.collection.CollectionUtil`
* isEmpty：判断是否为空
* isNotEmpty：判断是否不为空
* getFirst：取得Collection的第一个元素，如果collection为空返回null
* getLast：获取Collection的最后一个元素，如果collection为空返回null
* elementsEqual：两个集合中的所有元素按顺序相等
* min：返回无序集合中的最小值
* max：返回无序集合中的最大值
* minAndMax：返回无序集合中的最小值和最大值
* topN：返回最大的N个对象
* bottomN：返回最小的N个对象

### 列表
`io.github.chensheng.dddboot.tools.collection.ListUtil`
* newArrayList：构造类型正确的ArrayList
* newArrayListWithCapacity：构造类型正确的ArrayList, 并初始化数组大小
* newLinkedList：构造类型正确的LinkedList
* newCopyOnWriteArrayList：构造类型正确的CopyOnWriteArrayList
* emptyList：返回一个空的结构特殊的List，节约空间
* singletonList：返回只含一个元素但结构特殊的List，节约空间
* unmodifiableList：返回包装后不可修改的List
* synchronizedList：返回包装后同步的List，所有方法都会被synchronized原语同步
* sort：采用JDK认为最优的排序算法
* binarySearch：二分法快速查找对象
* shuffle：随机乱序
* reverse：返回一个倒转顺序访问的List，仅仅是一个倒序的View，不会实际多生成一个List
* partition：分页拆分List
* notNullList：清理掉List中的Null对象
* uniqueNotNullList：清理掉List中的Null和重复的对象
* union：ist1,list2的并集（在list1或list2中的对象），产生新List
* intersection：list1, list2的交集（同时在list1和list2的对象），产生新List
* difference：list1, list2的差集（在list1，不在list2中的对象），产生新List
* disjoint：list1, list2的补集（在list1或list2中，但不在交集中的对象，又叫反交集）产生新List

### Map
`io.github.chensheng.dddboot.tools.collection.MapUtil`
* isEmpty：判断是否为空
* isNotEmpty：判断是否非空
* putIfAbsentReturnLast：ConcurrentMap的putIfAbsent()返回之前的Value，此函数封装返回最终存储在Map中的Value
* createIfAbsentReturnLast：如果Key不存在则创建，返回最后存储在Map中的Value
* newHashMap：构造类型正确的HashMap
* newHashMapWithCapacity：构造类型正确的HashMap，运算后初始化HashMap的大小
* newSortedMap：造类型正确的TreeMap
* newEnumMap：相比HashMap，当key是枚举类时, 性能与空间占用俱佳
* newConcurrentHashMap：构造类型正确的ConcurrentHashMap
* emptyMap：返回一个空的结构特殊的Map，节约空间
* singletonMap：返回一个只含一个元素但结构特殊的Map，节约空间
* unmodifiableMap：返回包装后不可修改的Map
* unmodifiableSortedMap：返回包装后不可修改的有序Map
* difference：对两个Map进行比较，返回MapDifference
* sortByValue：对一个Map按Value进行排序，返回排序LinkedHashMap，多用于Value是Counter的情况
* topNByValue：对一个Map按Value进行排序，返回排序LinkedHashMap，最多只返回n条，多用于Value是Counter的情况

### 数组
`io.github.chensheng.dddboot.tools.collection.ArrayUtil`
* newArray：传入类型与大小创建数组
* toArray：从collection转为Array, 以 list.toArray(new String[0]); 最快 不需要创建list.size()的数组
* swap：交换数组中的两个元素
* shuffle：将传入的数组乱序
* concat：添加元素到数组头
* asList：原版将数组转换为List
* intAsList：Arrays.asList()的加强版, 返回一个底层为原始类型int的List
* longAsList：Arrays.asList()的加强版, 返回一个底层为原始类型long的List
* doubleAsList：Arrays.asList()的加强版, 返回一个底层为原始类型double的List

### Set
`io.github.chensheng.dddboot.tools.collection.SetUtil`
* newHashSet：构造类型正确的HashSet
* newHashSetWithCapacity：创建HashSet并设置初始大小，因为HashSet内部是HashMap，会计算LoadFactor后的真实大小
* newSortedSet：构造类型正确的TreeSet
* newConcurrentHashSet：构造类型正确的ConcurrentHashSet
* emptySet：返回一个空的结构特殊的Set，节约空间
* singletonSet：返回只含一个元素但结构特殊的Set，节约空间
* unmodifiableSet：返回包装后不可修改的Set
* newSetFromMap：从Map构造Set
* unionView：set1, set2的并集（在set1或set2的对象）的只读view，不复制产生新的Set对象
* intersectionView：set1, set2的交集（同时在set1和set2的对象）的只读view，不复制产生新的Set对象
* differenceView：set1, set2的差集（在set1，不在set2中的对象）的只读view，不复制产生新的Set对象
* disjointView：set1, set2的补集（在set1或set2中，但不在交集中的对象，又叫反交集）的只读view，不复制产生新的Set对象

### Queue
`io.github.chensheng.dddboot.tools.collection.QueueUtil`
* newArrayDeque：创建ArrayDeque
* newLinkedDeque：创建LinkedDeque (LinkedList实现了Deque接口)
* newConcurrentNonBlockingQueue：创建无阻塞情况下，性能最优的并发队列
* newConcurrentNonBlockingDeque：创建无阻塞情况下，性能最优的并发双端队列
* newBlockingUnlimitQueue：创建并发阻塞情况下，长度不受限的队列
* newBlockingUnlimitDeque：创建并发阻塞情况下，长度不受限的双端队列
* newArrayBlockingQueue：创建并发阻塞情况下，长度受限，更节约内存，但共用一把锁的队列（无双端队列实现）
* newLinkedBlockingQueue：创建并发阻塞情况下，长度受限，头队尾两把锁, 但使用更多内存的队列
* newBlockingDeque：创建并发阻塞情况下，长度受限，头队尾两把锁, 但使用更多内存的双端队列


## 反射

### 常用
`io.github.chensheng.dddboot.tools.reflect.ReflectionUtil`
* getSetterMethod：循环遍历，按属性名获取前缀为set的函数，并设为可访问
* getGetterMethod：循环遍历，按属性名获取前缀为get或is的函数，并设为可访问
* getMethod：循环向上转型, 获取对象的DeclaredMethod, 并强制设置为可访问
* getAccessibleMethodByName：循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问
* getField：循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问
* invokeGetter：调用Getter方法, 无视private/protected修饰符
* invokeSetter：调用Setter方法, 无视private/protected修饰符, 按传入value的类型匹配函数
* getFieldValue：直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数
* setFieldValue：直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数
* setField：使用预先获取的Field, 直接读取对象属性值, 不经过setter函数
* getProperty：先尝试用Getter函数读取, 如果不存在则直接读取变量
* setProperty：尝试用Setter函数写入, 如果不存在则直接写入变量, 按传入value的类型匹配函数
* invokeMethod：反射调用对象方法, 无视private/protected修饰符
* invokeMethodByName：反射调用对象方法, 无视private/protected修饰符
* invokeConstructor：调用构造函数
* makeAccessible：改变private/protected的方法为可访问
* convertReflectionExceptionToUnchecked：将反射时的checked exception转换为unchecked exception
* findGenericType：通过泛型参数名称获取泛型的实际类型

### 注解
`io.github.chensheng.dddboot.tools.reflect.AnnotationUtil`
* getAllAnnotations：递归Class所有的Annotation
* getAnnotatedPublicFields：找出所有标注了该annotation的公共属性，循环遍历父类
* getAnnotatedFields：找出所有标注了该annotation的属性，循环遍历父类，包含private属性
* getAnnotatedPublicMethods：找出所有标注了该annotation的公共方法(含父类的公共函数)，循环其接口

### 类信息
`io.github.chensheng.dddboot.tools.reflect.ClassUtil`
* getShortClassName：返回短Class名, 不包含PackageName
* getPackageName：返回PackageName
* getAllSuperclasses：递归返回所有的SupperClasses，包含Object.class
* getAllInterfaces：递归返回本类及所有基类继承的接口，及接口继承的接口，比Spring中的相同实现完整
* unwrapCglib：获取CGLib处理过后的实体的原Class
* getClassGenericType：通过反射, 获得Class定义中声明的泛型参数的类型

## 安全
### 加解密
`io.github.chensheng.dddboot.tools.security.CyptoUtil`
* hmacSha1：使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节
* isMacValid：校验HMAC-SHA1签名是否正确
* generateHmacSha1Key：生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节)
* aesEncrypt：使用AES加密原始字符串
* aesDecrypt：使用AES解密字符串, 返回原始字符串
* generateAesKey：生成AES密钥,返回字节数组, 默认长度为128位(16字节)
* generateIV：生成随机向量,默认大小为cipher.getBlockSize(), 16字节

## 其他

### Bean复制
`io.github.chensheng.dddboot.tools.base.BeanUtil`
* copyProperties：复制所有属性
* copyNotNullProperties：复制非空属性
* copyNotBlankProperties：复制非空白属性
* copyProperty：复制单个属性

### 异常处理
`io.github.chensheng.dddboot.tools.base.ExceptionUtil`
* unchecked：将CheckedException转换为RuntimeException重新抛出, 可以减少函数签名中的CheckExcetpion定义
* unwrap：如果是著名的包裹类，从cause中获得真正异常. 其他异常则不变
* unwrapAndUnchecked：组合unwrap与unchecked，用于处理反射/Callable的异常
* stackTraceText：将StackTrace[]转换为String, 供Logger或e.printStackTrace()外的其他地方使用
* toStringWithShortName：拼装 短异常类名: 异常信息，与Throwable.toString()相比使用了短类名
* toStringWithRootCause：拼装 短异常类名: 异常信息 <-- RootCause的短异常类名: 异常信息
* getRootCause：获取异常的Root Cause
* findCause：获取某种类型的cause，如果没有则返回空
* isCausedBy：判断异常是否由某些底层的异常引起
* setStackTrace：为静态异常设置StackTrace
* clearStackTrace：清除StackTrace. 假设StackTrace已生成, 但把它打印出来也有不小的消耗

### 平台变量
`io.github.chensheng.dddboot.tools.base.Platforms`
* FILE_PATH_SEPARATOR：文件路径分隔符
* FILE_PATH_SEPARATOR_CHAR：文件路径分隔符(char类型)
* WINDOWS_FILE_PATH_SEPARATOR_CHAR：Windows文件路径分隔符
* LINUX_FILE_PATH_SEPARATOR_CHAR：Linux文件路径分隔符
* CLASS_PATH_SEPARATOR：ClassPath分隔符
* CLASS_PATH_SEPARATOR_CHAR：ClassPath分隔符(char类型)
* LINE_SEPARATOR：换行符
* TMP_DIR：临时目录
* WORKING_DIR：应用的工作目录
* USER_HOME：用户HOME目录
* JAVA_HOME：Java HOME目录
* JAVA_SPECIFICATION_VERSION：Java大版本，比如1.8
* JAVA_VERSION：Java小版本，1.8.0_102
* OS_NAME：操作系统名称
* OS_VERSION：操作系统版本
* OS_ARCH：操作系统架构，比如x86_64
* IS_LINUX：操作系统是否为linux
* IS_UNIX：操作系统是否为unix
* IS_WINDOWS：操作系统是否为windows

### 运行时信息
`io.github.chensheng.dddboot.tools.base.RuntimeUtil`
* getPid：获得当前进程的PID
* getUpTime：返回应用启动到现在的毫秒数
* getVmArguments：返回输入的JVM参数列表
* getCores：获取CPU核数
* addShutdownHook：注册JVM关闭时的钩子程序
* getCallerClass：通过StackTrace，获得调用者的类名
* getCallerMethod：通过StackTrace，获得调用者的"类名.方法名()“
* getCurrentClass：通过StackTrace，获得当前方法的类名
* getCurrentMethod：通过StackTrace，获得当前方法的"类名.方法名()"
