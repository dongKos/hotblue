package com.dh.hotblue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("[Buddystock] Rest Api")
                .description("[Buddystock] Spring Rest Api Description")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket buddyApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("버디")
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.pick.buddy"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }
    
    @Bean
    public Docket snsApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("문자 인증")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.aws"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    
    @Bean
    public Docket tradingApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("거래")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.trading"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    
    @Bean
    public Docket userApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("사용자")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.user"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    
    @Bean
    public Docket codeApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("공통코드")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.code"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    
    @Bean
    public Docket initApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("init")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.init"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    @Bean
    public Docket bookmarkApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("북마크설정")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.bookmark"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    @Bean
    public Docket alarmApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("알람설정")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.onAlarm"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    @Bean
    public Docket youtuberApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("유튜버")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.youtuber"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    @Bean
    public Docket blockApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("차단")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.block"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
    @Bean
    public Docket appPwdApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.groupName("보안")
    			.apiInfo(this.apiInfo())
    			.select()
    			.apis(RequestHandlerSelectors
    					.basePackage("com.pick.appPwd"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }

}
