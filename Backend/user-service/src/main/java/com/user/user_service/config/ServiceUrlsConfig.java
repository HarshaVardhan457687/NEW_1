package com.user.user_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceUrlsConfig {

    @Value("${PROJECT_SERVICE_URL}")
    private String PROJECT_SERVICE_URL;

    @Value("${OBJECTIVE_SERVICE_URL}")
    private String OBJECTIVE_SERVICE_URL;

    @Value("${KEYRESULT_SERVICE_URL}")
    private String KEYRESULT_SERVICE_URL;

    @Value("${TASK_SERVICE_URL}")
    private String TASK_SERVICE_URL;

    @Value("${TEAM_SERVICE_URL}")
    private String TEAM_SERVICE_URL;

    // Getters for each service URL

//    public String getPROJECT_SERVICE_URL() {
//        return PROJECT_SERVICE_URL;
//    }
//
//    public String getOBJECTIVE_SERVICE_URL() {
//        return OBJECTIVE_SERVICE_URL;
//    }
//
//    public String getKEYRESULT_SERVICE_URL() {
//        return KEYRESULT_SERVICE_URL;
//    }
//
//    public String getTASK_SERVICE_URL() {
//        return TASK_SERVICE_URL;
//    }
//
//    public String getTEAM_SERVICE_URL() {
//        return TEAM_SERVICE_URL;
//    }
}
