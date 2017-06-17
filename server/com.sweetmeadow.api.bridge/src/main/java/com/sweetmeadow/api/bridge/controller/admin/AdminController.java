/**
 * 
 */
package com.sweetmeadow.api.bridge.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octopusdio.api.common.annotation.Login;
import com.octopusdio.api.common.controller.AbstractBaseController;

/**
 * @author zhangle
 *
 */
@Login
@RestController
@RequestMapping("/bridge/admin")
public class AdminController extends AbstractBaseController {

}
