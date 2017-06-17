/**
 * 
 */
package com.sweetmeadow.api.web.controller.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.octopusdio.api.common.annotation.Login;
import com.octopusdio.api.common.controller.AbstractBaseController;

/**
 * @author zhangle
 *
 */
@Login
@Controller
@RequestMapping("/page/admin")
public class AdminPageController extends AbstractBaseController {

    @RequestMapping("/index")
    public String adminIndex(Model uiModel, HttpServletRequest request) {
        LOG.info("Go admin index page.");
        return "admin/index";
    }
}
