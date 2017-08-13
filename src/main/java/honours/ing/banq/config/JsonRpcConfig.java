package honours.ing.banq.config;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration class for the JSON-RPC library.
 * @author Jeffrey Bakker
 * @since 17-4-17
 */
@Configuration
public class JsonRpcConfig {

    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        AutoJsonRpcServiceImplExporter exp = new AutoJsonRpcServiceImplExporter();
        // exp.setHttpStatusCodeProvider();
        // exp.setErrorResolver();
        return exp;
    }

}
