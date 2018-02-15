/*******************************************************************************
 * Copyright (c) quickfixengine.org  All rights reserved.
 *
 * This file is part of the QuickFIX FIX Engine
 *
 * This file may be distributed under the terms of the quickfixengine.org
 * license as defined by quickfixengine.org and appearing in the file
 * LICENSE included in the packaging of this file.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 * See http://www.quickfixengine.org/LICENSE for licensing information.
 *
 * Contact ask@quickfixengine.org if any conditions of this licensing
 * are not clear to you.
 ******************************************************************************/

package quickfix.mina;

import org.apache.mina.core.session.IoSession;
import quickfix.Message;
import quickfix.Responder;
import quickfix.Session;
import quickfix.SessionID;

/**
 * An interface implemented by various FIX event handling strategies. Currently,
 * it only handles message reception events.
 */
public interface EventHandlingStrategy {
    final String LOWER_WATERMARK_FMT = "inbound queue size < lower watermark (%d), socket reads resumed";
    final String UPPER_WATERMARK_FMT = "inbound queue size > upper watermark (%d), socket reads suspended";

    /**
     * Constant indicating how long we wait for an incoming message. After
     * thread has been asked to stop, it can take up to this long to terminate.
     */
    long THREAD_WAIT_FOR_MESSAGE_MS = 250;

    // will be put to the eventQueue to signal a disconnection
    Message END_OF_STREAM = new Message();

    void onMessage(Session quickfixSession, Message message);

    /**
     * @return the SessionConnector associated with this strategy
     */
    SessionConnector getSessionConnector();

    int getQueueSize();

    int getQueueSize(SessionID sessionID);

    static IoSession lookupIoSession(Session qfSession) {
        final Responder responder = qfSession.getResponder();

        if (responder instanceof IoSessionResponder) {
            return ((IoSessionResponder)responder).getIoSession();
        } else {
            return null;
        }
    }

}
