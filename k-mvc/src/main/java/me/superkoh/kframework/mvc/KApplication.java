package me.superkoh.kframework.mvc;

import me.superkoh.kframework.mvc.config.profiles.ProfileConstant;
import me.superkoh.kframework.mvc.script.ScriptInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

public class KApplication {
    private static final Logger scriptLogger = LoggerFactory.getLogger("scriptLogger");

    public void run(Class clazz, String[] args) {
        SpringApplication springApplication = new SpringApplication(clazz);
//        springApplication.addListeners(new ApplicationPidFileWriter("application.pid"));
        ConfigurableApplicationContext ctx = springApplication.run(args);
        Environment env = ctx.getEnvironment();
        if (env.acceptsProfiles(ProfileConstant.RT_SCRIPT)) {
            if (args.length > 0) {
                String shellClass = args[0];
                ScriptInterface script = (ScriptInterface) ctx.getBean(shellClass);
                if (null != script) {
                    try {
                        if (args.length > 1) {
                            script.run(Arrays.copyOfRange(args, 1, args.length));
                        } else {
                            script.run(new String[]{});
                        }
                    } catch (Exception e) {
                        scriptLogger.error("error occur", e);
                    }
                }
            }
        }
    }
}
