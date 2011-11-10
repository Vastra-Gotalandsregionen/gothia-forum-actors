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

package se.gothiaforum.util.actorsform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.portlet.social.model.SocialRequest;
import com.liferay.portlet.social.model.SocialRequestFeedEntry;

/**
 * The Class Social Request Value Object.
 */
public class SocialRequestVO {
    private static final Log LOG = LogFactory.getLog(SocialRequestVO.class);

    private SocialRequest socialRequest;

    private SocialRequestFeedEntry requestFeedEntry;

    public SocialRequest getSocialRequest() {
        return socialRequest;
    }

    public void setSocialRequest(SocialRequest socialRequest) {
        this.socialRequest = socialRequest;
    }

    public SocialRequestFeedEntry getRequestFeedEntry() {
        return requestFeedEntry;
    }

    public void setRequestFeedEntry(SocialRequestFeedEntry requestFeedEntry) {
        this.requestFeedEntry = requestFeedEntry;
    }

}
