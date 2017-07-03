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
import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.methods.HttpPost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.entity.ContentType;
import org.apache.hc.core5.http.entity.StringEntity;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class RedirectService {

    private static final String DEFAULT_URL = "http://localhost:8080";

    private CloseableHttpClient httpclient = HttpClients.createDefault();

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
        String result = null;

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
                                Class<?> returnType = method.getReturnType();

                                JsonRpcService annotation = AnnotationUtils.findAnnotation(service.getClass(),
                                        JsonRpcService.class);
                                HttpPost httpPost = new HttpPost(DEFAULT_URL + annotation.value());
                                StringEntity msg = new StringEntity(json, ContentType.create("application/json",
                                        "UTF-8"));
                                httpPost.setEntity(msg);
                                HttpResponse response = httpclient.execute(httpPost);

                                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                        (response.getEntity().getContent())));
                                String out; StringBuilder output = new StringBuilder();
                                while ((out = reader.readLine()) != null) {
                                    output.append(out);
                                }

                                result = output.toString();
                            } catch (IllegalAccessException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

}
