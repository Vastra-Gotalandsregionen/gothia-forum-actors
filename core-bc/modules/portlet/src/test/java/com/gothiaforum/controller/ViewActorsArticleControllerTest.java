/**
 * 
 */
package com.gothiaforum.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.journal.service.JournalArticleLocalService;
import com.liferay.portlet.social.service.SocialRequestInterpreterLocalService;
import com.liferay.portlet.social.service.SocialRequestLocalService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import se.gothiaforum.actorsarticle.service.ActorsService;
import se.gothiaforum.controller.actorsform.ViewActorsArticleController;
import se.gothiaforum.settings.service.SettingsService;
import se.gothiaforum.validator.actorsform.ActorArticleValidator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.lang.reflect.Field;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author simongoransson
 * 
 */
public class ViewActorsArticleControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(ViewActorsArticleControllerTest.class);

    private ActionRequest actionRequest;
    private ActionResponse actionResponse;
    private RenderRequest renderRequest;
    private RenderResponse renderResponse;
    private Model model;
    private SessionStatus sessionStatus;

    @Autowired
    private JournalArticleLocalService articleService;

    @Autowired
    private ActorsService actorsService;

    @Autowired
    private ActorArticleValidator validator;

    @Autowired
    private SocialRequestLocalService socialRequestService;

    @Autowired
    private SocialRequestInterpreterLocalService socialRequestInterpreterService;

    @Autowired
    private SettingsService settingsService;

    @InjectMocks
    private final ViewActorsArticleController viewActorsArticleController = new ViewActorsArticleController();

    @Before
    public void before() throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {

        articleService = mock(JournalArticleLocalService.class);
        socialRequestService = mock(SocialRequestLocalService.class);
        settingsService = mock(SettingsService.class);
        actorsService = mock(ActorsService.class);
        socialRequestService = mock(SocialRequestLocalService.class);
        socialRequestInterpreterService = mock(SocialRequestInterpreterLocalService.class);

        // mock creation
        actionRequest = mock(ActionRequest.class);
        actionResponse = mock(ActionResponse.class);

        renderRequest = mock(RenderRequest.class);
        renderResponse = mock(RenderResponse.class);
        model = mock(Model.class);

        sessionStatus = mock(SessionStatus.class);

        Field field = viewActorsArticleController.getClass().getDeclaredField("articleService");
        field.setAccessible(true);
        field.set(viewActorsArticleController, articleService);

        Field field2 = viewActorsArticleController.getClass().getDeclaredField("socialRequestService");
        field2.setAccessible(true);
        field2.set(viewActorsArticleController, socialRequestService);

        Field field3 = viewActorsArticleController.getClass().getDeclaredField("settingsService");
        field3.setAccessible(true);
        field3.set(viewActorsArticleController, settingsService);

        Field field4 = viewActorsArticleController.getClass().getDeclaredField("actorsService");
        field4.setAccessible(true);
        field4.set(viewActorsArticleController, actorsService);

    }

    @Test
    public void testView() throws Exception {

        ThemeDisplay themeDisplay = mock(ThemeDisplay.class);

        when(renderRequest.getAttribute(eq(WebKeys.THEME_DISPLAY))).thenReturn(themeDisplay);
        when(themeDisplay.getCompanyId()).thenReturn((long) 888);
        when(themeDisplay.getScopeGroupId()).thenReturn((long) 999);
        when(themeDisplay.getLanguageId()).thenReturn("222");

        PortletURL portletURL = mock(PortletURL.class);

        when(renderResponse.createRenderURL()).thenReturn(portletURL);

        viewActorsArticleController.showActorView(model, renderRequest, renderResponse);

        // verify(model).addAttribute(eq("searchFirstTimeArticleContent"), eq("Hello World"));

    }

    @Test
    public void testShowConnectView() throws Exception {

        ThemeDisplay themeDisplay = mock(ThemeDisplay.class);

        when(renderRequest.getAttribute(eq(WebKeys.THEME_DISPLAY))).thenReturn(themeDisplay);

        PortletURL portletURL = mock(PortletURL.class);

        when(renderResponse.createRenderURL()).thenReturn(portletURL);

        viewActorsArticleController.showConnectView(model, renderRequest, renderResponse);

        // verify(model).addAttribute(eq("searchFirstTimeArticleContent"), eq("Hello World"));

    }

    @Test
    public void testshowUserView() throws Exception {

        ThemeDisplay themeDisplay = mock(ThemeDisplay.class);

        when(renderRequest.getAttribute(eq(WebKeys.THEME_DISPLAY))).thenReturn(themeDisplay);

        User user = mock(User.class);

        when(themeDisplay.getUser()).thenReturn(user);

        PortletURL portletURL = mock(PortletURL.class);

        when(renderResponse.createRenderURL()).thenReturn(portletURL);

        viewActorsArticleController.showUserView(model, renderRequest, renderResponse);

        // verify(model).addAttribute(eq("searchFirstTimeArticleContent"), eq("Hello World"));

    }

    @Test
    public void testShowFormView() throws Exception {

        PortletURL portletURL = mock(PortletURL.class);
        when(renderResponse.createRenderURL()).thenReturn(portletURL);

        viewActorsArticleController.showFormView(model, renderRequest, renderResponse);

    }
}
