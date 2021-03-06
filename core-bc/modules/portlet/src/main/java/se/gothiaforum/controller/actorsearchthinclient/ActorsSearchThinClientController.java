/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.gothiaforum.controller.actorsearchthinclient;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import se.gothiaforum.actorsarticle.util.ActorsConstants;
import se.gothiaforum.portlet.service.PortletService;
import se.gothiaforum.settings.service.SettingsService;
import se.gothiaforum.settings.util.ExpandoConstants;

import javax.portlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Controller class is used as a client to call the "main" search component (ActorSearchController).
 * 
 * @author simgo3
 */

@Controller
@RequestMapping(value = "VIEW")
public class ActorsSearchThinClientController {
    private static final Logger LOG = LoggerFactory.getLogger(ActorsSearchThinClientController.class);

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private JournalArticleLocalService articleService;
    
    @Autowired
    private PortletService portletService;

    /**
     * Render for showing the search for actor view.
     * 
     * @param request
     *            the request
     * @param model
     *            the model
     * @return the search actor page
     */
    @RenderMapping
    public String showSearchActorView(RenderRequest request, RenderResponse response, Model model) {

        // This is for pinking up the articles in the portlet.
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long groupId = themeDisplay.getScopeGroupId();
        
        try {
        	String webContentDisplayPortletId = "56_INSTANCE_bn02";
        	
        	HttpServletRequest servletRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(request));
        	
        	PortletPreferences webContentDisplayPortletPrefs = PortletPreferencesFactoryUtil.getPortletPreferences(servletRequest, webContentDisplayPortletId);
        	
        	webContentDisplayPortletPrefs.setValue("portlet-setup-show-borders", "false");
        	webContentDisplayPortletPrefs.store();        	
        	
			String bannerArticleHtml = portletService.renderPortlet(request, response, webContentDisplayPortletId, "");
			
			model.addAttribute("bannerArticleHtml", bannerArticleHtml);
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (ReadOnlyException e) {
			e.printStackTrace();
		}        

        /*
        try {
            String bannerArticleId =
                    settingsService.getSetting(ExpandoConstants.GOTHIA_BANNER_ARTICLE, companyId, groupId);

            String bannerArticleContent =
                    articleService.getArticleContent(groupId, bannerArticleId, null,
                            themeDisplay.getLanguageId(), themeDisplay);
            model.addAttribute("bannerArticleContent", bannerArticleContent);
        } catch (PortalException e) {
            LOG.info("no article for thin client search portlet found");
        } catch (SystemException e) {
            LOG.info("no article for thin client search portlet found");
        }
        */

        try {
            String searchclientArticleId =
                    settingsService.getSetting(ExpandoConstants.GOTHIA_THIN_SEARCH_ARTICLE, companyId,
                            groupId);
            String searchclientArticleContent =
                    articleService.getArticleContent(groupId, searchclientArticleId, null,
                            themeDisplay.getLanguageId(), themeDisplay);
            model.addAttribute("searchclientArticleContent", searchclientArticleContent);
        } catch (PortalException e) {
            LOG.info("no article for thin client search portlet found", e);
        } catch (SystemException e) {
            LOG.info("no article for thin client search portlet found", e);
        }

        try {
            model.addAttribute("hostName", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "searchActorView";
    }

    /**
     * The method that search for an actor.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     */
    @ActionMapping("search")
    public void search(ActionRequest request, ActionResponse response) {

        String searchTerm = request.getParameter("searchTerm");

        try {
            String redirect =
                    ActorsConstants.SEARCH_REDIRECT_URL
                            + URLEncoder.encode(searchTerm, ActorsConstants.UTF_8);
            response.sendRedirect(redirect);
        } catch (IOException e) {
            throw new RuntimeException("Could not execute the search.", e);
        }
    }

    /**
     * Serve resource.
     * 
     * @param searchFor
     *            the string searching for.
     * @param request
     *            the request
     * @param response
     *            the response
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws PortletException
     *             the portlet exception
     */
    @ResourceMapping
    public void serveResource(@RequestParam("searchTerm") String searchFor, ResourceRequest request,
            ResourceResponse response) throws IOException, PortletException {
        try {

            List<AssetTag> matchingTags = getMatchingTags(searchFor);

            JSONArray jsonResult = com.liferay.portal.kernel.json.JSONFactoryUtil.createJSONArray();

            for (AssetTag tag : matchingTags) {
                JSONObject jsonRow = JSONFactoryUtil.createJSONObject();
                jsonRow.put("key", tag.getTagId());
                jsonRow.put("name", tag.getName());
                jsonResult.put(jsonRow);
            }

            JSONObject jsonResponse = JSONFactoryUtil.createJSONObject();
            jsonResponse.put("results", jsonResult);

            // Must be here in order to make the java script for auto complete work.
            response.getWriter().append(jsonResponse.toString());

            response.setContentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AssetTag> getMatchingTags(String searchWord) {

        ArrayList<AssetTag> emptyList = new ArrayList<AssetTag>();
        List<AssetTag> tagsList = ListUtil.fromCollection(emptyList);

        DynamicQuery dq =
                DynamicQueryFactoryUtil.forClass(AssetTag.class, PortalClassLoaderUtil.getClassLoader());

        Criterion searchTermCriterion = RestrictionsFactoryUtil.ilike("name", searchWord + "%");

        dq.add(searchTermCriterion);

        try {
            tagsList = AssetTagLocalServiceUtil.dynamicQuery(dq);

            // for (AssetTag assetTag : tagsList) {
            // System.out.println("assetTag = " + assetTag.getName());
            // }

            LOG.info("Number of matching tags: " + tagsList.size());

        } catch (SystemException e) {
            LOG.error(e.getMessage(), e);
        }

        return tagsList;
    }

}
