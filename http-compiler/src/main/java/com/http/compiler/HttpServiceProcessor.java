package com.http.compiler;

import com.http.compiler.annotation.NetServiceClass;
import com.http.compiler.bean.MethodMeta;
import com.http.compiler.bean.ParamMeta;
import com.http.compiler.bean.ServiceMeta;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class HttpServiceProcessor extends AbstractProcessor {
    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        //初始化我们需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //支持的java版本
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(NetServiceClass.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {//这里开始处理我们的注解解析了，以及生成Java文件
        if (set == null || set.isEmpty()) {
            info(">>> set is null... <<<");
            return true;
        }

        info(">>> Found field, start... <<<");

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(NetServiceClass.class);

        if (elements == null || elements.isEmpty()) {
            info(">>> elements is null... <<<");
            return true;
        }

        if (elements != null && !elements.isEmpty()) {
            List<ServiceMeta> serviceList = ElementUtils.parseServices(elements, mElementUtils, mMessager);
            if (serviceList != null && !serviceList.isEmpty()) {
                Iterator var5 = serviceList.iterator();

                while (var5.hasNext()) {
                    ServiceMeta meta = (ServiceMeta) var5.next();
                    this.JavaClassFile(meta);
                }
            }

            return true;
        } else {
            return true;
        }
    }


    private void JavaClassFile(ServiceMeta serviceMeta) {
        try {
            String className = serviceMeta.getSampleName() + "$" + "Impl";
            JavaFileObject f = this.mFiler.createSourceFile(className);
//            mMessager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + f.toUri());
            Writer w = f.openWriter();
            try {
                PrintWriter pw = new PrintWriter(w);
                pw.println("package " + ElementUtils.packageName + ";\n");
                pw.println("import java.util.HashMap;");
                pw.println("import java.util.Map;");
                pw.println("import com.http.api.OkHttpUtils;");
                pw.println("import java.util.concurrent.CountDownLatch;");
                pw.println("import com.http.api.NetError;");
                pw.println("import com.http.api.DataCallBack;");
                pw.println("import com.alibaba.fastjson.JSONObject;");
                pw.println("import com.http.api.UrlUtils;");
                pw.println("import com.http.compiler.HttpDealMethod;");
                pw.format("import %s;\n\n", serviceMeta.getPackageName());
                pw.println("/**");
                pw.println(" * Created by yanxuwen");
                pw.println(" * Created at 2019/4/17 9:18");
                pw.println(" * 这个文件是自动生成的，请不要去编辑它");
                pw.println(" */");
                pw.format("public class %s implements %s {\n", className, serviceMeta.getSampleName());
                pw.println("     HttpDealMethod httpDealMethod;");
                pw.format("     String baseUrl = \"%s\";\n", serviceMeta.getBaseUrl());
                //设置方法
                for (Iterator var7 = serviceMeta.getMethodMetas().iterator(); var7.hasNext(); pw.println("    }")) {
                    MethodMeta methodMeta = (MethodMeta) var7.next();

                    switch (methodMeta.getRequestType()) {
                        case MethodMeta.TYPE.TYPE_GET:
                            get(methodMeta, pw);
                            break;
                        case MethodMeta.TYPE.TYPE_POST:
                        case MethodMeta.TYPE.TYPE_PUT:
                        case MethodMeta.TYPE.TYPE_DELETE:
                            post(methodMeta, pw);
                            break;
                        default:
                            pw.println("\n    @Override");
                            boolean firstItem = true;
                            StringBuilder params = new StringBuilder("");
                            for (Iterator var14 = methodMeta.getParams().iterator(); var14.hasNext(); firstItem = false) {
                                ParamMeta meta = (ParamMeta) var14.next();
                                setParams(meta, params, firstItem);
                            }
                            pw.format("    public %s %s(%s) {\n", methodMeta.getReturnType(), methodMeta.getName(), params.toString());
                            break;
                    }
                }
                pw.println("\n    private void request(String url,int requestType,Map<String, String> map, String json ,Map<String, String> params,Map<String, String> headers,HttpDealMethod dealMethod,final DataCallBack callback,boolean syn) {");
                pw.format("        new OkHttpUtils().request(baseUrl,url, requestType,map ,json ,params,headers,%s,callback,syn);\n", "dealMethod");
                pw.println("    }");

                pw.println("\n    private HttpDealMethod getHttpDealMethod(){");
                pw.println("        try {");
                pw.println("            if (httpDealMethod == null){");
                pw.format("                httpDealMethod = (HttpDealMethod) Class.forName(\"%s\").getConstructor().newInstance();\n" , serviceMeta.getDealclassName() );
                pw.println("            }");
                pw.println("        } catch (Exception e) {}");
                pw.format("         return httpDealMethod;\n");
                pw.println("     }");
                pw.println("}");
                pw.flush();
            } finally {
                w.close();
            }

        } catch (Exception e) {
//            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    /**
     * get请求
     */
    private void get(MethodMeta methodMeta, PrintWriter pw) {
        StringBuilder params = new StringBuilder("");
        StringBuilder str_query = null;
        StringBuilder str_path = null;
        StringBuilder str_headers = null;
        boolean firstItem = true;
        boolean hasCallback = false;        //参数
        for (Iterator var14 = methodMeta.getParams().iterator(); var14.hasNext(); firstItem = false) {
            ParamMeta meta = (ParamMeta) var14.next();
            if (meta.getTypeMirror().toString().startsWith("com.http.api.DataCallBack")) {
                //设置监听器
                if (!firstItem) {
                    params.append(", ");
                }
                hasCallback = true;
                params.append(meta.getTypeMirror().toString());
                params.append(" callback");
            } else if (meta.getType() == ParamMeta.TYPE.Path) {
                setParams(meta, params, firstItem);
                if (str_path == null) {
                    str_path = new StringBuilder();
                }
                str_path.append(setPath(methodMeta, meta));
            } else if (meta.getType() == ParamMeta.TYPE.Query) {
                setParams(meta, params, firstItem);
                //添加拼接url
                if (str_query == null) {
                    str_query = new StringBuilder();
                }
                str_query.append(String.format("           urlJoint.put(\"%s\", %s);\n", meta.getName(), meta.getOrginName()));

            } else if (meta.getType() == ParamMeta.TYPE.Header) {
                setParams(meta, params, firstItem);
                //头部
                if (str_headers == null) {
                    str_headers = new StringBuilder();
                }
                str_headers.append(String.format("            _headers.put(\"%s\", String.valueOf(%s));", meta.getName(), meta.getOrginName()));
            } else {
                setParams(meta, params, firstItem);
            }
        }
        pw.println("\n    @Override");
        pw.format("    public %s %s(%s) {\n", methodMeta.getReturnType(), methodMeta.getName(), params.toString());
        //如果有返回值方法，则设置里面
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            pw.println(setReturnMethod(methodMeta).toString());
        }
        setPwUrl(methodMeta, pw, str_query);
        boolean isHeaders = setPwHeaders(methodMeta, pw, str_headers);
        if (str_path != null) {
            pw.println(str_path.toString());
        }
        setPwRequest(methodMeta, pw, null, null, null, isHeaders, hasCallback);
        //如果有返回值方法，则设置里面
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            StringBuilder params_try = new StringBuilder();
            params_try.append("        try {\n");
            params_try.append("            countDownLatch.await();\n");
            params_try.append("        } catch (InterruptedException e) {\n");
            params_try.append("            e.printStackTrace();\n");
            params_try.append("        }\n");
            params_try.append("        return returnResult[0];\n");
            pw.print(params_try.toString());
        }
    }

    /**
     * post请求
     */
    private void post(MethodMeta methodMeta, PrintWriter pw) {
        boolean firstItem = true;
        String requestBody = null;
        String json_name = null;
        boolean hasCallback = false;
        //url拼接
        StringBuilder params = new StringBuilder("");
        StringBuilder str_field = null;
        StringBuilder str_params = null;
        StringBuilder str_query = null;
        StringBuilder str_path = null;
        StringBuilder str_headers = null;
        boolean isGson = false;
        for (Iterator var14 = methodMeta.getParams().iterator(); var14.hasNext(); firstItem = false) {
            ParamMeta meta = (ParamMeta) var14.next();
            if (meta.getType() == ParamMeta.TYPE.Body) {
                json_name = meta.getOrginName();
                setParams(meta, params, firstItem);
                if (!meta.getTypeMirror().toString().equals("java.lang.String") && meta.getTypeMirror().toString().equals("okhttp3.RequestBody")) {
                    requestBody = meta.getOrginName();
                } else if (!meta.getTypeMirror().toString().equals("java.lang.String")) {
                    isGson = true;
                    json_name = " JSONObject.toJSONString(" + json_name + ")";
                }
            } else if (meta.getTypeMirror().toString().startsWith("com.http.api.DataCallBack")) {
                //设置监听器
                if (!firstItem) {
                    params.append(", ");
                }
                hasCallback = true;
                params.append(meta.getTypeMirror().toString());
                params.append(" callback");
            } else if (meta.getType() == ParamMeta.TYPE.Path) {
                setParams(meta, params, firstItem);
                if (str_path == null) {
                    str_path = new StringBuilder();
                }
                str_path.append(setPath(methodMeta, meta));
            } else if (meta.getType() == ParamMeta.TYPE.Query) {
                setParams(meta, params, firstItem);
                //添加拼接url
                if (str_query == null) {
                    str_query = new StringBuilder();
                }
                str_query.append(String.format("           urlJoint.put(\"%s\", %s);\n", meta.getName(), meta.getOrginName()));
            } else if (meta.getType() == ParamMeta.TYPE.Param) {
                setParams(meta, params, firstItem);
                //参数
                if (str_params == null) {
                    str_params = new StringBuilder();
                }
                str_params.append(String.format("            _params.put(\"%s\",String.valueOf(%s));", meta.getName(), meta.getOrginName()));
            } else if (meta.getType() == ParamMeta.TYPE.Header) {
                setParams(meta, params, firstItem);
                //头部
                if (str_headers == null) {
                    str_headers = new StringBuilder();
                }
                str_headers.append(String.format("            _headers.put(\"%s\", String.valueOf(%s));", meta.getName(), meta.getOrginName()));
            } else if (json_name == null) {
                setParams(meta, params, firstItem);
                if (meta.getType() == ParamMeta.TYPE.Field) {
                    //表单
                    if (str_field == null) {
                        str_field = new StringBuilder();
                    }
                    str_field.append(String.format("            _map.put(\"%s\", String.valueOf(%s));", meta.getName(), meta.getOrginName()));
                }
            }
        }
        pw.println("\n    @Override");
        pw.format("    public %s %s(%s) {\n", methodMeta.getReturnType(), methodMeta.getName(), params.toString());
        //如果有返回值方法，则设置里面
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            pw.println(setReturnMethod(methodMeta).toString());
        }
        setPwUrl(methodMeta, pw, str_query);
        if (str_path != null) {
            pw.println(str_path.toString());
        }
        setPwParams(methodMeta, pw, str_params);
        boolean isHeaders = setPwHeaders(methodMeta, pw, str_headers);
        //如果是body则不处理表单
        if (json_name == null) {
            setPwField(methodMeta, pw, str_field);
        } else {
            str_field = null;
        }
        if (requestBody != null) {
            pw.format("          new OkHttpUtils().post(url,%s,%s,%s,callback);\n"
                    , requestBody
                    , isHeaders ? "_headers" : null
                    , methodMeta.isDeal() ? "getHttpDealMethod()" : "null");
        } else {
            setPwRequest(methodMeta, pw, str_field, json_name, str_params, isHeaders, hasCallback);
        }
        //如果有返回值方法，则设置里面
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            StringBuilder params_try = new StringBuilder();
            params_try.append("        try {\n");
            params_try.append("            countDownLatch.await();\n");
            params_try.append("        } catch (InterruptedException e) {\n");
            params_try.append("            e.printStackTrace();\n");
            params_try.append("        }\n");
            params_try.append("        return returnResult[0];\n");
            pw.println(params_try.toString());
        }
    }

    /**
     * 设置参数
     */
    private void setParams(ParamMeta meta, StringBuilder params, boolean firstItem) {
        if (!firstItem) {
            params.append(", ");
        }
        params.append(meta.getTypeMirror().toString());
        params.append(" ");
        params.append(meta.getOrginName());
    }

    /**
     * 设置有返回值的方法
     */
    public StringBuilder setReturnMethod(MethodMeta methodMeta) {
        StringBuilder params = new StringBuilder();
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            params.append(String.format("        final %s[] returnResult = new %s[1];\n", methodMeta.getReturnType(), methodMeta.getReturnType()));
            params.append("        final CountDownLatch countDownLatch = new CountDownLatch(1);\n");
            params.append(String.format("        DataCallBack  callback_ = new DataCallBack<%s>(%s.class) {\n", methodMeta.getReturnType(), methodMeta.getReturnType()));
            params.append("            @Override\n");
            params.append(String.format("            public void onHttpSuccess(%s result) {\n", methodMeta.getReturnType()));
            params.append("                returnResult[0] = result;\n");
            params.append("                countDownLatch.countDown();\n");
            params.append("            }\n");
            params.append("            @Override\n");
            params.append("            public void onHttpFail(NetError netError) {\n");
            params.append("                returnResult[0] = netError.toString();\n");
            params.append("                countDownLatch.countDown();\n");
            params.append("            }\n");
            params.append("        };\n");
        }
        return params;
    }

    /**
     * @Path 重置Url
     */
    private StringBuilder setPath(MethodMeta methodMeta, ParamMeta meta) {
        StringBuilder str_path = new StringBuilder();
        str_path.append(String.format("        String key_%s = \"{%s}\";\n ", meta.getName(), meta.getName()));
        str_path.append(String.format("       if (url.contains(key_%s)){\n", meta.getName()));
        str_path.append(String.format("           url = url.replace(key_%s,%s);\n", meta.getName(), meta.getOrginName()));
        str_path.append("        }\n");
        return str_path;
    }

    /**
     * 写入Url
     */
    private void setPwUrl(MethodMeta methodMeta, PrintWriter pw, StringBuilder str_query) {
        if (str_query != null) {
            //拼接url
            pw.println("        Map<String, String> urlJoint = new HashMap<>();");
            pw.println("        try {");
            pw.println(str_query.toString());
            pw.println("        } catch (Exception e) {");
            pw.println("            e.printStackTrace();");
            pw.println("        }");
            pw.format("        String url =  UrlUtils.urlJoint(\"%s\",urlJoint);\n", methodMeta.getUrl());
        } else {
            pw.format("        String url = \"%s\";\n", methodMeta.getUrl());
        }
    }

    private void setPwField(MethodMeta methodMeta, PrintWriter pw, StringBuilder str_field) {
        if (str_field == null) return;
        pw.println("         Map<String, String> _map = new HashMap<>();");
        pw.println("        try {");
        pw.println(str_field.toString());
        pw.println("        } catch (Exception e) {");
        pw.println("            e.printStackTrace();");
        pw.println("        }");
    }

    private void setPwParams(MethodMeta methodMeta, PrintWriter pw, StringBuilder str_params) {
        if (str_params == null) return;
        pw.println("         Map<String, String> _params = new HashMap<>();");
        pw.println("        try {");
        pw.println(str_params.toString());
        pw.println("        } catch (Exception e) {");
        pw.println("            e.printStackTrace();");
        pw.println("        }");
    }

    private boolean setPwHeaders(MethodMeta methodMeta, PrintWriter pw, StringBuilder str_headers) {
        if (methodMeta.getHeaders() == null && str_headers == null) return false;
        pw.println("         Map<String, String> _headers = new HashMap<>();\n");
        pw.println("        try {");
        //先添加方法注解里面的Header
        if (methodMeta.getHeaders() != null) {
            for (Map.Entry<String, String> entry : methodMeta.getHeaders().entrySet()) {
                pw.format("            _headers.put(\"%s\",\"%s\");\n", entry.getKey(), entry.getValue());
            }
        }
        if (str_headers != null) {
            //然后添加参数里面的注解，动态
            pw.println(str_headers.toString());
        }
        pw.println("        } catch (Exception e) {");
        pw.println("            e.printStackTrace();");
        pw.println("        }");
        return true;
    }

    private void setPwRequest(MethodMeta methodMeta, PrintWriter pw, StringBuilder str_field, String json_name, StringBuilder str_params, boolean isHeaders, boolean hasCallback) {
        String callback = hasCallback ? "callback" : "null";
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            callback = "callback_";
        }

        boolean syn = false;
        if (!(methodMeta.getReturnType() + "").equals("void")) {
            syn = true;
        }
        pw.format("        request( url, %d, %s, %s ,%s, %s,%s,%s,%s);\n"
                , methodMeta.getRequestType()
                , str_field == null ? null : "_map"
                , json_name
                , str_params == null ? null : "_params"
                , isHeaders ? "_headers" : null
                , methodMeta.isDeal() ? "getHttpDealMethod()" : "null"
                , callback
                ,syn);
    }


    private void error(Element e, String msg, Object... args) {
//        mMessager.printMessage(
//                Diagnostic.Kind.ERROR,
//                String.format(msg, args),
//                e);
    }

    private void info(String msg, Object... args) {
//        mMessager.printMessage(
//                Diagnostic.Kind.NOTE,
//                String.format(msg, args));
    }
}
