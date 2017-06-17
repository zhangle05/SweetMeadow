/**
 * 
 */
package com.sweetmeadow.api.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.octopusdio.api.common.controller.AbstractBaseController;

/**
 * @author zhangle
 *
 */
@RequestMapping("/api")
@Controller
public class HomeController extends AbstractBaseController {

    /**
     * Constructor
     */
    public HomeController() {
        LOG.debug("Creating HomeController.");
    }

    @RequestMapping("")
    public String goHome(Model uiModel, HttpServletRequest request) {
        LOG.debug("Go home.");
        return "index";
    }

    @RequestMapping("/")
    public String goHome2(Model uiModel, HttpServletRequest request) {
        LOG.debug("Go home.");
        return "index";
    }
}
