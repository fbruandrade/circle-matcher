package dev.fandrade.circlematcher.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class Identify {

    @NotBlank
    public String workspaceId;

    @NotNull
    public Map<String, Object> requestData;

    public Identify() {
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Map<String, Object> getRequestData() {
        return requestData;
    }

    public void setRequestData(Map<String, Object> requestData) {
        this.requestData = requestData;
    }
}
