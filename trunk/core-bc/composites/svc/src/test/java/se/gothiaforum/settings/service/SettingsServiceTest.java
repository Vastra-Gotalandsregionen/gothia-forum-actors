/**
 * 
 */
package se.gothiaforum.settings.service;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.gothiaforum.settings.service.impl.SettingsServiceImpl;

import com.liferay.portlet.expando.service.ExpandoColumnLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;

/**
 * @author simongoransson
 * 
 */
public class SettingsServiceTest {
    private static final Log LOG = LogFactory.getLog(SettingsServiceTest.class);

    SettingsService settingsService;

    @Mock
    private ExpandoColumnLocalService expandoColumnService;

    @Mock
    private ExpandoTableLocalService expandoTableService;

    @Mock
    private ExpandoValueLocalService expandoValueService;

    @Before
    public void before() throws IllegalArgumentException, IllegalAccessException, SecurityException,
            NoSuchFieldException {

        settingsService = new SettingsServiceImpl();

        expandoColumnService = mock(ExpandoColumnLocalService.class);
        expandoTableService = mock(ExpandoTableLocalService.class);
        expandoValueService = mock(ExpandoValueLocalService.class);

        Field field = settingsService.getClass().getDeclaredField("expandoColumnService");
        field.setAccessible(true);
        field.set(settingsService, expandoColumnService);

        Field field2 = settingsService.getClass().getDeclaredField("expandoTableService");
        field2.setAccessible(true);
        field2.set(settingsService, expandoTableService);

        Field field3 = settingsService.getClass().getDeclaredField("expandoValueService");
        field3.setAccessible(true);
        field3.set(settingsService, expandoValueService);

    }

    @Test
    public void testGetSetting() {

        String columnName = "";
        long companyId = 0;
        long groupId = 0;

        settingsService.getSetting(columnName, companyId, groupId);

    }

    @Test
    public void testSetSetting() {

        String data = "test data";
        String columnName = "";
        long companyId = 0;
        long groupId = 0;

        settingsService.setSetting(data, columnName, companyId, groupId);

    }

}
