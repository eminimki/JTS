package eminimki.com.JTS.Business.requests.LogRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLogRequest {
    private int id;
    private String actioner;
    private String description;
}
