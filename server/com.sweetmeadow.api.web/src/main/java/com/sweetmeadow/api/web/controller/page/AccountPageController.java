/**
 * 
 */
package com.sweetmeadow.api.web.controller.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.octopusdio.api.common.controller.AbstractBaseController;

/**
 * @author zhangle
 *
 */
@Controller
@RequestMapping("/page/account")
public class AccountPageController extends AbstractBaseController {

    @RequestMapping("/login")
    public String login(Model uiModel, HttpServletRequest request) {
        LOG.info("Go lecture login.");
        return "account/login";
    }
}
