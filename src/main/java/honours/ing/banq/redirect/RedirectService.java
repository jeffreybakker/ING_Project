package honours.ing.banq.redirect;

import com.google.gson.Gson;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import honours.ing.banq.access.AccessService;
import honours.ing.banq.account.BankAccountService;
import honours.ing.banq.auth.AuthService;
import honours.ing.banq.info.InfoService;
import honours.ing.banq.transaction.TransactionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Kevin Witlox
 * @since 22-6-2017.
 */
@Controller
public class RedirectService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private AccessService accessService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private AuthService authService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    @ResponseBody
    public String redirect(@RequestBody String json) {
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(json, JsonRequest.class);
        Object result = null;
        Type returnType = null;

        ClassPathScanningCandidateComponentProvider provider = new
                ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(AutoJsonRpcServiceImpl.class));
        Set<BeanDefinition> beans = provider.findCandidateComponents("honours.ing");
        for (BeanDefinition bd : beans) {
            Class<?> bean = null;
            try {
                bean = Class.forName(bd.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            for (Method method : bean.getMethods()) {
                if (method.getName().equals(jsonRequest.getMethod())) {
                    // Method found in bd
                    for (Field field : getClass().getDeclaredFields()) {
                        if (field.getType().isAssignableFrom(bean)) {
                            // Found the service belonging to bd
                            field.setAccessible(true);
                            try {
                                Object service = field.get(this);
                                List<Object> list = new ArrayList<>();

                                Class<?> interfaze = service.getClass().getSuperclass().getInterfaces()[0];
                                Method interfaceMethod = interfaze.getMethod(method.getName(), method
                                        .getParameterTypes());
                                Parameter[] parameters = interfaceMethod.getParameters();
                                for (Parameter parameter : parameters) {
                                    for (Map.Entry<String, String> entry : jsonRequest.getParams().entrySet()) {
                                        JsonRpcParam annotation = AnnotationUtils.findAnnotation(parameter,
                                                JsonRpcParam.class);
                                        if (entry.getKey().equals(annotation.value())) {
                                            list.add(entry.getValue());
                                        }
                                    }
                                }

                                result = method.invoke(service, list.toArray());
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return gson.toJson(result);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
