# SimplifyNet
基于Okhttp+Gson的一个简单封装的网络访问框架.在各个层次都有明确的异常抛出(更加方便添加头部.添加特殊请求参数.添加签名等逻辑).回调返回实体/错误信息.

## JitPack.io

我把项目放到了[jitpack.io](https://jitpack.io).如果要使用请按照如下对项目进行配置.

    repositories {
    	//...
    	maven { url "https://jitpack.io" }
	}

	dependencies {
		//...
    	compile 'com.github.chenzj-king:SimplifyNet:1.2.2'
	}

## 源码分析 ##

#### 请求开始 ####

    NetRequest.getWeatherMsg(map, mUUID, new GenericsCallback<Weather>() {
        @Override
        public void onError(int errorCode, String errorMes, Call call, Exception e) {
			//失败的处理
        }

        @Override
        public void onResponse(Weather response) {
			//成功的处理
        }
    });

#### 封装在请求类的处理 ####

    public static void getWeatherMsg(Map<String, String> specificParams, Object object, final GenericsCallback<Weather> callback) {
        callback.setErrMes("解释天气数据失败");
		//getAddParams是封装过的方法.里面会进行网络判断&添加参数&build RequestCall来进行请求
        getAddParams(HttpUrl.BASE_WEATHER_URL, specificParams, object, callback);
    }

#### 底册okhttpUtils封装的回调处理 ####

    public void execute(final RequestCall requestCall, HttpCallBack httpCallBack) {
        if (httpCallBack == null)
            httpCallBack = HttpCallBack.HttpCallBackDefault;
        final HttpCallBack finalHttpCallBack = httpCallBack;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
				//直接最底层的请求失败
                sendFailResultCallback(call, e, finalHttpCallBack);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
						////状态码不为200也判断为失败来执行
                        sendFailResultCallback(call, new AndroidRuntimeException(response.body().string()), finalHttpCallBack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
					//自己包装的一层BaseResponse.用来兼容gzip和方便jsonStr→bean
                    BaseResponse baseResponse = new BaseResponse(response);
                    Object object = finalHttpCallBack.parseNetworkResponse(baseResponse);

					//如果parseNetworkResponse没有抛出自定义的异常/其他异常.则走成功的逻辑
                    sendSuccessResultCallback(object, call, finalHttpCallBack);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalHttpCallBack);
                } finally {
					//及时回收.不然可能造成泄漏
                    response.close();
                }

            }
        });
    }

#### 对成功/失败进行状态判断后再执行回调 ####

    public void sendFailResultCallback(final Call call, final Exception exception, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {

                if (exception instanceof IOException) {
                    if (call.isCanceled()) {
                        Log.i("simplifyokhttp", "user finish Act/Fra/Dialog cancel the request");
                    } else {
                        if (exception instanceof SocketException) {
                            httpCallBack.onError(ErrorCode.SOCKET_EXCEPTION, "连接服务器失败，请重试！", call, exception);
                        } else if (exception instanceof InterruptedIOException) {
                            httpCallBack.onError(ErrorCode.INTERRUPTED_IOEXCEPTION, "请求失败，请重试！", call, exception);
                        } else {
                            httpCallBack.onError(ErrorCode.OTHER_IOEXCEPTION, "连接服务器失败，请重试！", call, exception);
                        }
                    }
                } else if (exception instanceof AndroidRuntimeException) {

                    //请求状态码非200的提示
                    httpCallBack.onError(ErrorCode.RESPONSE_ERROR_CODE_EXCEPTION, "请求成功,但返回的状态码不为200，请重试！", call, exception);

                } else if (exception instanceof DreamLinerException) {

                    //判断到是服务器返回的异常
                    DreamLinerException dreamLinerException = (DreamLinerException) exception;
                    httpCallBack.onError(dreamLinerException.getErrorCode(), dreamLinerException.getErrorMessage(), call, exception);

                } else {
                    //在parseNetworkResponse引发的异常/onResponse的时候操作不当引发的异常
                    httpCallBack.onError(ErrorCode.RUNTIME_EXCEPTION, exception.getMessage(), call, exception);
                }
                httpCallBack.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Call call, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    httpCallBack.onResponse(object);
                } catch (Exception e) {
                    e.printStackTrace();
                    //这里是为了防止在执行onResponse的时候操作不当导致crash
                    httpCallBack.onError(ErrorCode.EXCHANGE_DATA_ERROR, e.getMessage(), call, e);
                } finally {
                    httpCallBack.onAfter();
                }
            }
        });
    }

#### 泛型CallBack的自定义封装 ####

	public abstract class GenericsCallback<T> extends HttpCallBack<T> {
	
	    private String mErrMes;
	    private DataCallBack<T> mDataCallBack;
	
	    public GenericsCallback(@NonNull String errMes, DataCallBack<T> dataCallBack) {
	        mErrMes = errMes;
	        mDataCallBack = dataCallBack;
	    }
	
	    @Override
	    public void onError(int errorCode, String errorMes, Call call, Exception e) {
			//默认执行DataCallbck的失败回调
	        mDataCallBack.onError(errorCode, errorMes);	
	    }
	
	    @Override
	    public void onResponse(T response) {
			//默认执行DataCallbck的成功回调
	        mDataCallBack.onSuccess(response);
	    }
	
	    @Override
	    public T parseNetworkResponse(BaseResponse baseResponse) throws Exception {
	
	        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
			//兼容如果只是为了返回String[当然其实可以直接用StringCallback也行]
	        if (entityClass == String.class) {
	            return (T) baseResponse.getResponseBodyToString();
	        }
	
			//这里应该自定义你的Bean.可能不同的后台数据结构不同.
	        final ErrorDataMes dataErrorMes = parseResponseHandler(baseResponse);
	        // TODO: 2016/4/10 应该根据自己服务器的错误定义来进行回调到onEror/onSuccess.
	        if (null != dataErrorMes) {
	            if (dataErrorMes.getErr() != 0 && !dataErrorMes.getMsg().equals("success")) {
	                throw new DreamLinerException(dataErrorMes.getErr(), dataErrorMes.getMsg());
	            }
	        } else {
	            throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, "解释数据错误");
	        }
	
			//如果解释有问题的话.如服务器类型定义有问题.Object成了String之类的话.会抛出异常.这个时候刚好就可以用上了自定义异常的提示语的逻辑
	        T bean = null;
	        try {
	            bean = GsonUtil.getGson().fromJson(baseResponse.getResponseBodyToString(), entityClass);
	        } catch (Exception ex) {
	            throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, mErrMes);
	        }
	        return bean;
	    }
	
	
	    public ErrorDataMes parseResponseHandler(BaseResponse basicResponse) {
	        try {
	            return (ErrorDataMes) GsonUtil.fromJsonToObj(basicResponse.getResponseBodyToString(), ErrorDataMes.class);
	        } catch (Exception dataErrorMes) {
	            return null;
	        }
	    }
	}



#### 优点分析 ####
- 在BaseAct/BaseFra中生成一个UUID作为请求的tag.方便在关闭Act的时候随时终止所有请求.
- 很好的判断网络状态/登录状态.都可以在NetRequest类中实现的方法来进行管理.
- 每个层次都很好的分隔开了.可以定位到每一个层次.是底层访问.还是数据解释.然后每种不同的错误都可以有自定义好的一些错误码和错误信息来查看.方便调试.
- onSucces/onError回调都可以在主线程.直接操作UI.


#### TODO ####
- 考虑上类似showDialog等类似的可能随时用户中断请求的管理.则需要在NetRequest中返回RequestCall对象来进行随时cancel的逻辑
- 每种类型的请求都写一个sample.同时附上server代码.配对测试使用.


# 使用到的开源项目说明

>1.[butterknife](https://github.com/JakeWharton/butterknife)  
>2.[okhttp](https://github.com/square/okhttp)  
>3.[okhttp-utils](https://github.com/hongyangAndroid/okhttp-utils)  
>4.[gson](https://github.com/google/gson)  

# 关于我 #

- **QQ:** 364972027
- **Weibo:** [http://weibo.com/u/1829515680](http://weibo.com/u/1829515680)
- **Email:** admin@chenzhongjin.cn
- **Github:** [https://github.com/chenzj-king](https://github.com/chenzj-king)
- **Blog:** [http://www.chenzhongjin.cn](http://www.chenzhongjin.cn)
