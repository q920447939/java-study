	serer
		配置类  
			1.设置parent,children EventLoop
			2.ServerBootstrap 绑定parent,children
			3.设置端口，设置参数，设置channel类型
			4.设置children 初始化内容
				4.1 protobuf 解密类  继承channelinbound
					标记一下读的位置（当未完整读取时，可以重复读）
					读取魔数
					读取版本
					读取字节中的长度（后续要读取多少个字节的数据），
						没有长度或者长度为0时 -> 主动关闭连接
						长度不够等待下次读取 -> resetMark标记
						判断是否是堆内存还是直接内存
						使用byte数组读取 buff.readBytes()...
					读取成功 把protobuf 解析成对象。
					super.channel... 继续调用下一个channelInBound
				4.2  loginRequestHandle  登录请求处理类 ,继承channelinbound
					channelActive方法，
						写入日志获取到连接，但是没有登录
					channelRead方法
						如果不是protobuf Message对象。那么继续调用super.channelRead方法
						此时，字节流已经被转换成了Message对象
						判断处理类型，如果不是登录类型，那么继续调用super.channelRead方法
						获取Mesage里面的用户信息。
						进行登录（使用future 线程），创建一个Session对象 与channel绑定
							1.获取protobuf 对象里面的seqId(方便客户端在多线程处理时，可以通过seqId做某些事情)
							2.把请求对象信息转换成User对象
							3.检查用户
								判断用户是否已经登录（判断标准是用户id是否与"数据库"的用户id一致等）,不一致就给客户端返回失败结果	
							登录成功
								session与用户信息做绑定
								构建回复报文
							登录失败
						future->结果 。
							成功
								绑定一个聊天channelHandle
								绑定一个心跳channelHandle
								移除这个登录类解码处理器（后续不在需要级）
							失败或者异常
								关闭channel
								如果SessionMap集合中包含这个用户信息。将他下线！
								
				4.2  protobufEncode 编码类
					1.获取到要返回的butff
					2.构建返回字段 魔术，版本号，bytebuff 长度
					3.填充返回的butff
					4.写入channel
				
				4.3 一个异常处理类，专门处理channelInBound异常  重写 exceptionCaught方法
					仅仅记录一下日志
			
			5.开始同步绑定,并获取到channelFuture对象
			6.获取到channelFuture的channel. closeFuture事件， 同步
			
	final
		parent,children优雅关闭