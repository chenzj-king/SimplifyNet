# SimplifyNet
基于Okhttp+Gson的一个简单封装的网络访问框架.在各个层次都有明确的异常抛出(更加方便添加头部.添加特殊请求参数.添加签名等逻辑).回调返回实体/错误信息.


## 源码分析 ##

#### 请求开始 ####
    Map<String, String> map = new HashMap<String, String>();
	map.put("cityname", cityName);
	NetRequest.getWeatherMsg(map, mUUID, new DataCallBack<Weather>() {
    	@Override
    	public void onSuccess(Weather weather) {
			//成功的处理
    	}

    	@Override
    	public void onError(int code, String errorMsg) {
    	    //失败的处理
    	}
	});

#### 封装在请求类的处理 ####
    public static void getWeatherMsg(Map<String, String> specificParams, Object object, final DataCallBack<Weather> callback) {

        //这里进行网络状态判断.無网络直接回调onError.return该请求
        if (!checkNetStatus(callback)) {
            return;
        }
        //这里可以进行是否登录的校验.如果没有Token就回调onEror.并且return该请求(类似登录接口/查询无需登录权限的业务就无需调用该方法)
        Map<String, String> finalparams = addCommonParams(specificParams, callback);
        if (null == finalparams) {
            return;
        }

        try {
            OkHttpUtils.get().url(HttpUrl.BASE_WEATHER_URL).addHeader("apikey", "自己申请一下apikey")
                    .params(finalparams).tag(object).build().execute(new HttpCallBack() {

                @Override
                public void onError(int errorCode, String errorMes, Call call, Exception e) {
                    callback.onError(errorCode, errorMes);
                }

                @Override
                public void onResponse(Object response) {
                    callback.onSuccess((Weather) response);
                }

                @Override
                public Object parseNetworkResponse(Response response) throws Exception {

                    BaseResponse baseResponse = new BaseResponse(response);
                    //可以保存报文到本地出问题的时候方便调试
                    Type type = (new TypeToken<Weather>() {
                    }).getType();
                    Weather weather = (Weather) GsonUtil.fromJsonToObj(baseResponse.getResponseBodyToString(), type);

                    if (null == weather) {
                        throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, "解释天气数据失败");
                    }
                    return weather;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            OkHttpUtils.getInstance().postErro(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

#### 底册okhttpUtils封装的回调处理 ####
    public void execute(final RequestCall requestCall, HttpCallBack httpCallBack) {
        if (httpCallBack == null)
            httpCallBack = HttpCallBack.HttpCallBackDefault;
        final HttpCallBack finalHttpCallBack = httpCallBack;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
				//底层回调onError进行处理
                sendFailResultCallback(call, e, finalHttpCallBack);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
						/非200状态码都分发到onError
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalHttpCallBack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
					//执行callback的解释数据方法.在上面'封装在请求类的处理'里面有相关的sample.如果jsonString
				    //不规范.则抛出自定义的异常
                    Object object = finalHttpCallBack.parseNetworkResponse(response);
					//解释成功之后执行成功的处理
                    sendSuccessResultCallback(object, response, finalHttpCallBack);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalHttpCallBack);
                }

            }
        });
    }

#### 对成功/失败进行状态判断后再执行回调 ####
    public void sendFailResultCallback(final Call call, final Exception exception, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;


        mDelivery.post(new Runnable() {
            @Override
            public void run() {

                if (exception instanceof IOException) {
                    if (exception instanceof SocketException) {
                        if (call.isCanceled()) {
							//很多时候会不小心打开错Act.然后用户立马就关闭页面.为了不需要做无用功的请求.主动根据tag来
							//kill掉之后会触发到这里
                            Log.i("EasyNet", "user finish Act/Fra cancel the request");
                        } else {
                            httpCallBack.onError(ErrorCode.SOCKET_EXCEPTION, "连接服务器失败，请重试！", call, exception);
                        }
                    } else if (exception instanceof InterruptedIOException) {
                        httpCallBack.onError(ErrorCode.INTERRUPTED_IOEXCEPTION, "请求失败，请重试！", call, exception);
                    } else {
                        httpCallBack.onError(ErrorCode.OTHER_IOEXCEPTION, "连接服务器失败，请重试！", call, exception);
                    }
                } else if (exception instanceof DreamLinerException) {
                    //这是上层parseNetworkResponse的时候.如果gson解释有问题.就会抛出这个异常.就可以走onError回调
                    httpCallBack.onError(((DreamLinerException) exception).getErrorCode(),
                            ((DreamLinerException) exception).getErrorMessage(), call, exception);
                } else {
                    //请求状态码非200的提示
                    httpCallBack.onError(ErrorCode.RUNTIME_EXCEPTION, "连接服务器失败，请重试！", call, exception);
                }
                httpCallBack.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Response response, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;

        final BaseResponse basicResponse = new BaseResponse(response);
        final ErrorDataMes dataErrorMes = parseResponseHandler(basicResponse);

        mDelivery.post(new Runnable() {
            @Override
            public void run() {

                // TODO: 2016/4/10 应该根据自己服务器的错误定义来进行回调到onEror/onSuccess.
                // 现在只用来测试.所以不进行判断拦截.
                httpCallBack.onResponse(object);
                /*
                if (null != dataErrorMes) {
                    if (dataErrorMes.getErr() == 0 && dataErrorMes.getMsg().equals("成功")) {
                        //服务器返回成功的状态码和信息
                        httpCallBack.onResponse(object);
                    } else {
                        //正常的服务器错误状态码
                        httpCallBack.onError(ErrorCode.SERVER_CUSTOM_ERROR, dataErrorMes.getMsg(), null, null);
                    }
                } else {
                    //回来的报文不是规范Json导致无法用Gson解释catch
                    httpCallBack.onError(ErrorCode.EXCHANGE_DATA_ERROR, "解释数据错误", null, null);
                }*/

                httpCallBack.onAfter();
            }
        });
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

1.  ButterKnife  
Link: [https://github.com/JakeWharton/butterknife](https://github.com/JakeWharton/butterknife)

1. okhttp  
Link:[https://github.com/square/okhttp](https://github.com/square/okhttp)

2. okhttp-utils  
Link:[https://github.com/hongyangAndroid/okhttp-utils](https://github.com/hongyangAndroid/okhttp-utils) 


1. Gson  
Link:[https://github.com/google/gson](https://github.com/google/gson)

# 关于我 #

- **QQ:** 364972027
- **Weibo:** [http://weibo.com/u/1829515680](http://weibo.com/u/1829515680)
- **Email:** admin@chenzhongjin.cn
- **Github:** [https://github.com/chenzj-king](https://github.com/chenzj-king)
- **Blog:** [http://www.chenzhongjin.cn](http://www.chenzhongjin.cn)
