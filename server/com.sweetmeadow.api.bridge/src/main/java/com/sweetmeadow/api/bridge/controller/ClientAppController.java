/**
 * 
 */
package com.sweetmeadow.api.bridge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.octopusdio.api.common.annotation.Secured;
import com.octopusdio.api.common.controller.AbstractBaseController;
import com.octopusdio.api.common.domain.ListResult;
import com.octopusdio.api.common.domain.RESTResult;
import com.sweetmeadow.api.bridge.domain.pojo.ClientApp;
import com.sweetmeadow.api.bridge.service.ClientAppService;
import com.sweetmeadow.api.bridge.utils.SharedConstants;

import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
@Secured
@RestController
@RequestMapping("/bridge/client")
public class ClientAppController extends AbstractBaseController {

    private static final String SECRET_MASK = "******";

    @Autowired
    private ClientAppService clientSvc;

    @RequestMapping("/list")
    public ResponseEntity<RESTResult> purchaseCheck() {
        try {
            List<ClientApp> clients = clientSvc.listClients();
            for (ClientApp c : clients) {
                c.setSecret(SECRET_MASK);
            }
            ListResult<ClientApp> list = new ListResult<ClientApp>(
                    clients.size(), clients);
            RESTResult result = new RESTResult(list);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<RESTResult> updateClientSecret(
            @RequestBody String jsonBody) {
        try {
            JSONObject inputJson = JSONObject.fromObject(jsonBody);
            String clientName = inputJson
                    .optString(SharedConstants.CLIENT_NAME_PARAM_NAME);
            ClientApp client = clientSvc.getClientByName(clientName);
            if (client != null) {
                client.setSecret(UUID.randomUUID().toString());
                clientSvc.updateClientApp(client);
                client.setSecret(SECRET_MASK);
                RESTResult result = new RESTResult(client);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                RESTResult result = new RESTResult(
                        "找不到客户端'" + clientName + "'");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }
}
