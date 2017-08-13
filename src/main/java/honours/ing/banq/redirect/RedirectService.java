package honours.ing.banq.redirect;

import com.google.gson.Gson;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.methods.HttpPost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.entity.ContentType;
import org.apache.hc.core5.http.entity.StringEntity;
import org.springframework.beans.BeansException;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Kevin Witlox
 * @since 22-6-2017.
 */
@Controller
public class RedirectService implements ApplicationContextAware {

    private static final String DEFAULT_URL = "http://localhost:8080";

    private ApplicationContext context;
    private CloseableHttpClient httpclient = HttpClients.createDefault();

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    @ResponseBody
    public String redirect(@RequestBody String json) {
        Gson gson = new Gson();
        JsonRequest jsonRequest = gson.fromJson(json, JsonRequest.class);
        String result = null;

        // Scan for Services
        ClassPathScanningCandidateComponentProvider provider = new
                ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(AutoJsonRpcServiceImpl.class));
        Set<BeanDefinition> beans = provider.findCandidateComponents("honours.ing");

        // Loop through all services to find matching method
        for (BeanDefinition bd : beans) {
            Class<?> serviceClass = null;
            try {
                serviceClass = Class.forName(bd.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Loop through methods of service
            for (Method method : serviceClass.getMethods()) {
                if (method.getName().equals(jsonRequest.getMethod())) {
                    // method.invoke() hopefully works and gives less overhead (compared to HTTP)
                    // TODO: method.invoke()

                    // Method found in Service
                    try {
                        Object service = context.getBean(serviceClass);

                        JsonRpcService annotation = AnnotationUtils.findAnnotation(
                                service.getClass(), JsonRpcService.class);

                        HttpPost httpPost = new HttpPost(DEFAULT_URL + annotation.value());
                        StringEntity msg = new StringEntity(
                                json, ContentType.create("application/json", "UTF-8"));

                        httpPost.setEntity(msg);
                        HttpResponse response = httpclient.execute(httpPost);

                        // Read response
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                (response.getEntity().getContent())));
                        String out; StringBuilder output = new StringBuilder();
                        while ((out = reader.readLine()) != null) {
                            output.append(out);
                        }

                        result = output.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
