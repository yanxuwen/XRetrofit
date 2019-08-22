package com.http.compiler;

import com.http.compiler.annotation.Body;
import com.http.compiler.annotation.DELETE;
import com.http.compiler.annotation.DOWNLOAD;
import com.http.compiler.annotation.Deal;
import com.http.compiler.annotation.DealAll;
import com.http.compiler.annotation.DealClass;
import com.http.compiler.annotation.Field;
import com.http.compiler.annotation.GET;
import com.http.compiler.annotation.Header;
import com.http.compiler.annotation.Headers;
import com.http.compiler.annotation.NetServiceClass;
import com.http.compiler.annotation.POST;
import com.http.compiler.annotation.PUT;
import com.http.compiler.annotation.Param;
import com.http.compiler.annotation.Path;
import com.http.compiler.annotation.Query;
import com.http.compiler.annotation.TimeOut;
import com.http.compiler.annotation.UPLOAD;
import com.http.compiler.bean.MethodMeta;
import com.http.compiler.bean.ParamMeta;
import com.http.compiler.bean.ServiceMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

@SuppressWarnings("unchecked")
public class ElementUtils {
    public static String packageName = "com.http";

    public static String getImplName(Class<?> clazz) {
        return packageName + "." + clazz.getSimpleName() + "$Impl";
    }

    public static List<ServiceMeta> parseServices(Set<? extends Element> routeElements, Elements elementUtils, Messager messager) {
        List<ServiceMeta> list = new ArrayList();
        if (routeElements != null && !routeElements.isEmpty()) {
            Iterator var3 = routeElements.iterator();

            while (var3.hasNext()) {
                Element element = (Element) var3.next();
                NetServiceClass service = (NetServiceClass) element.getAnnotation(NetServiceClass.class);
                DealAll dealAll = (DealAll) element.getAnnotation(DealAll.class);
                DealClass dealClass = (DealClass) element.getAnnotation(DealClass.class);
                if (service != null) {
                    TypeMirror tm = element.asType();
//                    messager.printMessage(Diagnostic.Kind.NOTE, ">>> Found class [" + tm.toString() + "] <<<");
                    TypeElement teService = elementUtils.getTypeElement(tm.toString());
                    ServiceMeta meta = new ServiceMeta();
                    meta.setBaseUrl(service.value());
                    meta.setPackageName(tm.toString());
                    meta.setSampleName(element.getSimpleName().toString());
                    if (dealClass != null) {
                        Class<HttpDealMethod> ds = HttpDealMethod.class;
                        try {
                            meta.setDealclassName(dealClass.value().getName());
                        } catch (MirroredTypeException mte) {
                            TypeMirror value = mte.getTypeMirror();
                            meta.setDealclassName(value.toString());
                        }
                    }
                    meta.setMethodMetas(parseMethod(meta, teService.getEnclosedElements(), elementUtils, messager, dealAll != null));
                    list.add(meta);
                }
            }
        }
        return list;
    }

    public static List<MethodMeta> parseMethod(ServiceMeta serviceMeta, List<? extends Element> routeElements, Elements elementUtils, Messager messager, boolean dealAll) {
        List<MethodMeta> list = new ArrayList();
        if (routeElements != null && !routeElements.isEmpty()) {
            Iterator var3 = routeElements.iterator();

            while (var3.hasNext()) {
                ExecutableElement element = (ExecutableElement) var3.next();
                MethodMeta meta = new MethodMeta();
                Headers apiHeaders = (Headers) element.getAnnotation(Headers.class);
                //设置头部
                if (apiHeaders != null) {
                    Map<String, String> mapHeader = new HashMap<>();
                    String[] headers = apiHeaders.value();
                    if (headers != null) {
                        for (String str : headers) {
                            String[] split = str.split(":");
                            if (split != null && split.length >= 2) {
                                mapHeader.put(split[0], split[1]);
                            }
                        }
                    }
                    meta.setHeaders(mapHeader);
                }
                GET apiGET = (GET) element.getAnnotation(GET.class);
                POST apiPOST = (POST) element.getAnnotation(POST.class);
                PUT apiPUT = (PUT) element.getAnnotation(PUT.class);
                DELETE apiDELETE = (DELETE) element.getAnnotation(DELETE.class);
                DOWNLOAD apiDOWNLOAD = (DOWNLOAD) element.getAnnotation(DOWNLOAD.class);
                UPLOAD apiUPLOAD= (UPLOAD) element.getAnnotation(UPLOAD.class);

                int requestType = 0;
                if (apiGET != null) {
                    requestType = MethodMeta.TYPE.TYPE_GET;
                    meta.setUrl(apiGET.value());
                } else if (apiPOST != null) {
                    requestType = MethodMeta.TYPE.TYPE_POST;
                    meta.setUrl(apiPOST.value());
                } else if (apiPUT != null) {
                    requestType = MethodMeta.TYPE.TYPE_PUT;
                    meta.setUrl(apiPUT.value());
                } else if (apiDELETE != null) {
                    requestType = MethodMeta.TYPE.TYPE_DELETE;
                    meta.setUrl(apiDELETE.value());
                } else if (apiDOWNLOAD != null) {
                    requestType = MethodMeta.TYPE.TYPE_DOWNLOAD;
                    meta.setUrl(apiDOWNLOAD.value());
                } else if (apiUPLOAD != null) {
                    requestType = MethodMeta.TYPE.TYPE_UPLOAD;
                    meta.setUrl(apiUPLOAD.value());
                }

                String methodName = element.getSimpleName().toString();
//                messager.printMessage(Diagnostic.Kind.NOTE, ">>> Found method [" + methodName + " " + element.asType().toString() + "] <<<");
                Deal apiDeal = (Deal) element.getAnnotation(Deal.class);
                if (dealAll || apiDeal != null) {
                    meta.setDeal(true);
                }

                TimeOut timeOut = (TimeOut) element.getAnnotation(TimeOut.class);
                if (timeOut != null) {
                    meta.setTimeout(timeOut.value());
                }

                meta.setRequestType(requestType);
                meta.setName(methodName);
                meta.setResult(element.asType());
                meta.setParams(parseParam(element, elementUtils, messager));
                //设置方法属性
                try {
                    meta.setReturnType(element.getReturnType());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.add(meta);
            }
        }

        return list;
    }

    public static List<ParamMeta> parseParam(Element element, Elements elementUtils, Messager messager) {
        List<ParamMeta> list = new ArrayList();
        if (element != null && element instanceof ExecutableElement) {
            Iterator var3 = ((ExecutableElement) element).getParameters().iterator();

            while (var3.hasNext()) {
                VariableElement ve = (VariableElement) var3.next();
                TypeMirror tm = ve.asType();
                String paramName = ve.getSimpleName().toString();
                String orginName = paramName;
                ParamMeta meta = new ParamMeta();
                if (ve.getAnnotation(Body.class) != null) {
                    paramName = "java.lang.String json";
                    meta.setType(ParamMeta.TYPE.Body);

                } else if (ve.getAnnotation(Field.class) != null) {
                    paramName = ve.getAnnotation(Field.class).value();
                    meta.setType(ParamMeta.TYPE.Field);

                } else if (ve.getAnnotation(Query.class) != null) {
                    paramName = ve.getAnnotation(Query.class).value();
                    meta.setType(ParamMeta.TYPE.Query);

                } else if (ve.getAnnotation(Path.class) != null) {
                    paramName = ve.getAnnotation(Path.class).value();
                    meta.setType(ParamMeta.TYPE.Path);

                } else if (ve.getAnnotation(Param.class) != null) {
                    paramName = ve.getAnnotation(Param.class).value();
                    meta.setType(ParamMeta.TYPE.Param);

                } else if (ve.getAnnotation(Header.class) != null) {
                    paramName = ve.getAnnotation(Header.class).value();
                    meta.setType(ParamMeta.TYPE.Header);
                }
                meta.setTypeMirror(tm);
                meta.setName(paramName);
                meta.setOrginName(orginName);
                list.add(meta);
            }
        }

        return list;
    }
}
