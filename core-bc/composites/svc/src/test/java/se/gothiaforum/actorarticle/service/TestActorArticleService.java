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

/**
 * 
 */
package se.gothiaforum.actorarticle.service;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.workflow.WorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistry;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.kernel.xml.SAXReader;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.*;
import com.liferay.portal.model.impl.ListTypeImpl;
import com.liferay.portal.model.impl.OrganizationImpl;
import com.liferay.portal.model.impl.RoleImpl;
import com.liferay.portal.service.*;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.xml.SAXReaderImpl;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.model.impl.AssetEntryImpl;
import com.liferay.portlet.asset.model.impl.AssetTagImpl;
import com.liferay.portlet.asset.service.AssetEntryLocalService;
import com.liferay.portlet.asset.service.AssetTagLocalService;
import com.liferay.portlet.asset.service.AssetTagPropertyLocalService;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalService;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.model.impl.JournalArticleImpl;
import com.liferay.portlet.journal.model.impl.JournalArticleResourceImpl;
import com.liferay.portlet.journal.service.JournalArticleLocalService;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalService;
import com.liferay.portlet.journal.service.JournalStructureLocalService;
import com.liferay.portlet.journal.service.JournalTemplateLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.social.service.SocialRequestLocalService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.gothiaforum.actorsarticle.domain.model.ActorArticle;
import se.gothiaforum.actorsarticle.service.ActorsArticleConverterService;
import se.gothiaforum.actorsarticle.service.ActorsService;
import se.gothiaforum.actorsarticle.service.impl.ActorsArticleConverterServiceImpl;
import se.gothiaforum.actorsarticle.service.impl.ActorsServiceImpl;
import se.gothiaforum.actorsarticle.util.ActorAssetEntryUtil;
import se.gothiaforum.actorsarticle.util.ActorsConstants;
import se.gothiaforum.actorsarticle.util.ActorsServiceUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author simongoransson
 * 
 */

public class TestActorArticleService {
    private static final Logger LOG = LoggerFactory.getLogger(TestActorArticleService.class);

    private ActorsArticleConverterService actorsArticleConverterService;
    private ActorsServiceUtil actorsServiceUtil;
    private ActorsService actorsService;
    private AssetEntryLocalService assetEntryService;
    private AssetTagLocalService assetTagService;
    private AssetTagPropertyLocalService assetTagPropertyService;
    private ClassNameLocalService classNameService;
    private CounterLocalService counterService;
    private JournalStructureLocalService structureService;
    private JournalTemplateLocalService templateService;
    private ListTypeService listTypeService;
    private OrganizationLocalService organizationService;
    private RoleLocalService roleService;
    private UserGroupRoleLocalService userGroupRoleService;
    private UserLocalService userService;
    private DLFolderLocalService iGFolderService;
    private JournalArticleLocalService articleService;
    private JournalArticleResourceLocalService articleResourceService;
    private SocialRequestLocalService socialRequestService;
    private MBMessageLocalService mBMessageLocalService;
    private ActorAssetEntryUtil actorAssetEntryUtil;

    private JournalArticle journalArticle;

    @Before
    public void before() throws SystemException, PortalException {

        SAXReaderUtil sax = new SAXReaderUtil();

        SAXReader saxRead = new SAXReaderImpl();

        sax.setSAXReader(saxRead);

        actorsArticleConverterService = new ActorsArticleConverterServiceImpl();

        assetEntryService = Mockito.mock(AssetEntryLocalService.class);
        assetTagService = Mockito.mock(AssetTagLocalService.class);
        assetTagPropertyService = Mockito.mock(AssetTagPropertyLocalService.class);
        classNameService = Mockito.mock(ClassNameLocalService.class);
        counterService = Mockito.mock(CounterLocalService.class);
        structureService = Mockito.mock(JournalStructureLocalService.class);
        templateService = Mockito.mock(JournalTemplateLocalService.class);
        listTypeService = Mockito.mock(ListTypeService.class);
        organizationService = Mockito.mock(OrganizationLocalService.class);
        roleService = Mockito.mock(RoleLocalService.class);
        userGroupRoleService = Mockito.mock(UserGroupRoleLocalService.class);
        userService = Mockito.mock(UserLocalService.class);
        iGFolderService = Mockito.mock(DLFolderLocalService.class);
        articleService = Mockito.mock(JournalArticleLocalService.class);
        articleResourceService = Mockito.mock(JournalArticleResourceLocalService.class);
        socialRequestService = Mockito.mock(SocialRequestLocalService.class);
        mBMessageLocalService = Mockito.mock(MBMessageLocalService.class);
        actorAssetEntryUtil = new ActorAssetEntryUtilMock();

        actorsServiceUtil = new ActorsServiceUtil(assetEntryService, classNameService, counterService,
                structureService, templateService, listTypeService, organizationService, roleService,
                userGroupRoleService, userService);
        actorsService = new ActorsServiceImpl(actorsArticleConverterService, actorsServiceUtil, assetEntryService,
                assetTagService, assetTagPropertyService, counterService, /*iGImageService,*/ iGFolderService,
                articleService, articleResourceService, organizationService, socialRequestService, roleService,
                userService, mBMessageLocalService, actorAssetEntryUtil);

        List<ListType> listTypes = new ArrayList<ListType>();
        ListType listType = new ListTypeImpl();
        listType.setName("Full Member");
        listTypes.add(listType);

        Mockito.when(listTypeService.getListTypes(ActorsConstants.LISTTYPE_TYPE)).thenReturn(listTypes);

        Mockito.when(organizationService.getOrganizationsCount()).thenReturn(1);

        List<Organization> allOrgs = new ArrayList<Organization>();
        Organization organization = new OrganizationImpl();
        organization.setType(ActorsConstants.ACTOR_PARENT);
        allOrgs.add(organization);
        Mockito.when(organizationService.getOrganizations(Mockito.anyInt(), Mockito.anyInt())).thenReturn(allOrgs);

        Organization organizationMock = Mockito.mock(Organization.class);

        Mockito.when(organizationMock.getOrganizationId()).thenReturn((long) 1);

        Group group = Mockito.mock(Group.class);

        Mockito.when(group.getGroupId()).thenReturn((long) 1);

        Mockito.when(organizationMock.getGroup()).thenReturn(group);

        Mockito.when(
                organizationService.addOrganization(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyLong(), Mockito.anyLong(),
                        Mockito.anyInt(), Mockito.anyString(), Mockito.anyBoolean(), (ServiceContext) Mockito.isNull())).thenReturn(
                organizationMock);

        Role role = new RoleImpl();

        role.setRoleId(11);

        Mockito.when(counterService.increment()).thenReturn((long) 1111);

        // JournalArticle....
        journalArticle = new JournalArticleImpl();

        Mockito.when(articleService.createJournalArticle(Mockito.anyLong())).thenReturn(journalArticle);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getUserId()).thenReturn((long) 1111);
        Mockito.when(user.getFullName()).thenReturn("Test Simon");

        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(user);

        JournalArticleResource journalArticleResource = new JournalArticleResourceImpl();
        journalArticleResource.setResourcePrimKey(2000);

        Mockito.when(articleResourceService.createJournalArticleResource(Mockito.anyLong())).thenReturn(
                journalArticleResource);

        AssetEntry assetEntry = new AssetEntryImpl();

        Mockito.when(assetEntryService.createAssetEntry(Mockito.anyLong())).thenReturn(assetEntry);

        Mockito.when(roleService.getRole(Mockito.anyLong(), Mockito.anyString())).thenReturn(role);
        Mockito.when(roleService.getRole(Mockito.anyLong(), Mockito.anyString())).thenReturn(role);
        Mockito.when(roleService.getRole(Mockito.anyLong(), Mockito.anyString())).thenReturn(role);

    }

    @Test
    public void test1() throws Exception {

        ActorArticle actorArticle = new ActorArticle();
        actorsService.addActor(actorArticle, 10, 1, 100, null, "", 1000);
        assertEquals(contentResult, journalArticle.getContent());

    }

    @Test
    public void test2() throws Exception {

        ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

        User user = Mockito.mock(User.class);

        Mockito.when(themeDisplay.getUser()).thenReturn(user);

        List<Organization> allOrgs = new ArrayList<Organization>();
        Organization organizationMock = Mockito.mock(Organization.class);
        allOrgs.add(organizationMock);
        Mockito.when(user.getOrganizations()).thenReturn(allOrgs);

        Mockito.when(organizationMock.getType()).thenReturn(ActorsConstants.ORGNIZATION_ACTOR_TYPE);
        Mockito.when(organizationMock.getOrganizationId()).thenReturn((long) 1);
        Group group = Mockito.mock(Group.class);
        Mockito.when(group.getGroupId()).thenReturn((long) 1);
        Mockito.when(organizationMock.getGroup()).thenReturn(group);

        List<JournalArticle> articles = new ArrayList<JournalArticle>();

        JournalArticle journalArticle2 = new JournalArticleImpl();

        journalArticle2.setArticleId("11111");
        journalArticle2.setType(ActorsConstants.TYPE_ACTOR);

        journalArticle2.setContent(contentResult);

        articles.add(journalArticle2);

        Mockito.when(articleService.getArticles(Mockito.anyLong())).thenReturn(articles);

        Mockito.when(articleService.getArticles()).thenReturn(articles);
        Mockito.when(articleService.getLatestArticle(Mockito.anyLong())).thenReturn(journalArticle2);

        // An exception will occur of we don't set this value.
        System.setProperty("plugin.servlet.context.name", "asdf");
        ActorArticle actorArticle = actorsService.getActorsArticle(themeDisplay);

        assertEquals("11111", actorArticle.getArticleId());

    }

    @Test
    public void test3() throws Exception {

        JournalArticle journalArticle3 = new JournalArticleImpl();

        journalArticle3.setUserId(33333);
        journalArticle3.setCompanyId(44444);
        journalArticle3.setType("hehehj");
        journalArticle3.setGroupId(4331);

        journalArticle3.setPrimaryKey(1234);

        WorkflowHandlerRegistryUtil workflowHandlerRegistryUtil = new WorkflowHandlerRegistryUtil();

        WorkflowHandlerRegistry workflowHandlerRegistry = PowerMockito.mock(WorkflowHandlerRegistry.class);

        workflowHandlerRegistryUtil.setWorkflowHandlerRegistry(workflowHandlerRegistry);

        WorkflowHandler workflowHandler = PowerMockito.mock(WorkflowHandler.class);

        PowerMockito.when(WorkflowHandlerRegistryUtil.getWorkflowHandler(Mockito.anyString())).thenReturn(
                workflowHandler);

        ServiceContext serviceContext = Mockito.mock(ServiceContext.class);

        actorsService.sendActors(10, journalArticle3, 100, serviceContext);

        Mockito.verify(workflowHandler).startWorkflowInstance(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyLong(), Mockito.anyLong(), Mockito.isNotNull(), Mockito.anyMap());

    }

    @Test
    public void test4() throws Exception {

        ActorArticle actorArticle = new ActorArticle();
        ServiceContext serviceContext = Mockito.mock(ServiceContext.class);

        actorArticle.setAddress("Cheesecake sweet street 24 Marshmallow City");
        actorArticle.setArticleId("10003");
        actorArticle.setArticleStatus(1);
        actorArticle.setCompanyName("Tiramisu caramels");
        actorArticle.setContactWeb("Candy canes bonbon");
        actorArticle.setDetailedDescription("Cake pie sugar plum liquorice gingerbread marshmallow."
                + " Chocolate cake cake cheesecake halvah fruitcake gingerbread topping. Biscuit "
                + "jelly beans applicake. Jelly beans soufflé sweet candy croissant ice cream "
                + "candy. Sweet biscuit chupa chups jujubes cotton candy cake chocolate cake");
        actorArticle.setExternalHomepage("www.fruitcake.com");
        actorArticle.setFax("+4631 345 64 24");
        actorArticle.setMail("sweet@fruitcake.com");
        actorArticle.setMobilePhone("+4631 345 64 25");
        actorArticle.setName("Mr Cake Jelly");
        actorArticle.setOrganizationName("Applicake pie");
        actorArticle.setPhone("+4631 345 64 25");
        actorArticle.setTagsStr("chups,carrot,cake,soufflé");
        actorArticle.setTitle("Powder chocolate cake pie");
        actorArticle.setIntro("Cake pie sugar plum liquorice gingerbread marshmallow."
                + " Chocolate cake cake cheesecake halvah fruitcake gingerbread topping. Biscuit ");

        JournalArticle journalArticle4 = new JournalArticleImpl();
        journalArticle4.setResourcePrimKey(2222);

        Mockito.when(articleService.getArticle(Mockito.anyLong())).thenReturn(journalArticle4);

        AssetEntry assetEntry = new AssetEntryImpl();
        assetEntry.setPrimaryKey(1111);

        Mockito.when(assetEntryService.getEntry(Mockito.anyString(), Mockito.anyLong())).thenReturn(assetEntry);

        Mockito.when(assetTagService.hasTag(Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        actorsService.updateActors(actorArticle, 11, serviceContext, "apa,bepa,cepa", 10000);

        Mockito.when(assetTagService.hasTag(Mockito.anyLong(), Mockito.anyString())).thenReturn(false);

        AssetTag assetTag = new AssetTagImpl();

        Mockito.when(
                assetTagService.addTag(Mockito.anyLong(), Mockito.anyString(), Mockito.any(String[].class),
                        Mockito.eq(serviceContext))).thenReturn(assetTag);

        actorsService.updateActors(actorArticle, 11, serviceContext, "apa,bepa,cepa", 10000);

    }

    private static final String contentResult = "<?xml version=\"1.0\"?><root><dynamic-element instance-id=\"1111\" name=\"actor-name\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"org-name\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"intro\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"description\" type=\"text_area\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-webpage\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-name\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-title\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-address\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-phone\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-mobile\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-fax\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"contact-email\" type=\"text\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"logotype\" type=\"image_gallery\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id=\"1111\" name=\"actor-images\" type=\"image_gallery\" index-type=\"\"><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element></root>";

}
