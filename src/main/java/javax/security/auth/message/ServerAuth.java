package javax.security.auth.message;

import javax.security.auth.Subject;

//$Id$

/**
 *  <p>An implementation of this interface is used to validate received service request messages, and to secure service response messages.</p>
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @author Charlie Lai, Ron Monzillo (Javadoc for JSR-196)</a> 
 *  @since  May 12, 2006 
 *  @version $Revision$
 *  @see MessageInfo
 *  @see javax.security.auth.Subject
 */
public interface ServerAuth
{

   /**
    * <p>Remove method specific principals and credentials from the subject.</p>
    *
    * @param messageInfo - A contextual object that encapsulates the client request 
    *                      and server response objects, and that may be used to save 
    *                      state across a sequence of calls made to the methods of 
    *                      this interface for the purpose of completing a secure 
    *                      message exchange.
    * @param subject - The Subject instance from which the Principals and credentials 
    *                      are to be removed. 
    * @throws AuthException if an error occurs during the Subject processing.
    */
   public void cleanSubject( MessageInfo messageInfo, Subject subject)
   throws AuthException;
   
   /**
    * <p>Secure a service response before sending it to the client. This method is called to transform the response
    * message acquired by calling getResponseMessage (on messageInfo) into the mechanism-specific form to
    * be sent by the runtime.</p>
    * 
    * <p>This method conveys the outcome of its message processing either by returning an AuthStatus value or by
    * throwing an AuthException.</p>
    * 
    * @param messageInfo - A contextual object that encapsulates the client request 
    *                      and server response objects, and that may be used to save 
    *                      state across a sequence of calls made to the methods of 
    *                      this interface for the purpose of completing a secure 
    *                      message exchange.
    * @param serviceSubject - A Subject that represents the source of the service request,
    *                      or null. It may be used by the method implementation as the 
    *                      source of Principals or credentials to be used to secure 
    *                      the request. If the Subject is not null, the method 
    *                      implementation may add additional Principals or credentials 
    *                      (pertaining to the source of the service request) to the Subject.
    * @return An AuthStatus object representing the completion status of the processing performed by the
    *         method. The AuthStatus values that may be returned by this method are defined as follows:
    *
    *         <ul>
    *            <li>AuthStatus.SEND_SUCCESS when the application response message was successfully secured. The
    *                secured response message may be obtained by calling getResponseMessage on messageInfo.</li>
    *            <li>AuthStatus.SEND_CONTINUE to indicate that the application response message (within
    *                messageInfo) was replaced with a security message that should elicit a security-specific response (in
    *                the form of a request) from the peer. This status value serves to inform the calling runtime that (to
    *                successfully complete the message exchange) it will need to be capable of continuing the message
    *                dialog by processing at least one additional request/response exchange (after having sent the
    *                response message returned in messageInfo). When this status value is returned, the application
    *                response must be saved by the authentication module such that it can be recovered when the
    *                module’s validateRequest message is called to process the elicited response.</li>
    *           <li>AuthStatus.SEND_FAILURE to indicate that a failure occurred while securing the response message
    *               and that an appropriate failure response message is available by calling getResponseMessage on
    *               messageInfo.</li>
    *         </ul>
    * @throws AuthException When the message processing failed without establishing a failure response message (in messageInfo).
    */
   public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject)
   throws AuthException;
   
   /**
    * <p>Authenticate a received service request. This method is called to transform the mechanism-specific request
    * message acquired by calling getRequestMessage (on messageInfo) into the validated application message
    * to be returned to the message processing runtime. If the received message is a (mechanism-specific) meta-message,
    * the method implementation must attempt to transform the meta-message into a corresponding mechanism-specific response message,
    * or to the validated application request message. The runtime will bind a validated application message into the the
    * corresponding service invocation.</p>
    *
    * <p>This method conveys the outcome of its message processing either by returning an AuthStatus value or by
    * throwing an AuthException</p>
    *
    * @param messageInfo - A contextual object that encapsulates the client 
    *                      request and server response objects, and that may be 
    *                      used to save state across a sequence of calls made to 
    *                      the methods of this interface for the purpose of 
    *                      completing a secure message exchange.
    * 
    * @param clientSubject - A Subject that represents the source of the service request. It is used by the
    *                        method implementation to store Principals and credentials validated in the request.
    *                  
    * @param serviceSubject - A Subject that represents the recipient of the service request, or null. It may be
    *                        used by the method implementation as the source of Principals or credentials to be used to validate the
    *                        request. If the Subject is not null, the method implementation may add additional Principals or
    *                        credentials (pertaining to the recipient of the service request) to the Subject.
    *
    * @return An AuthStatus object representing the completion status of the processing performed by the
    *         method. The AuthStatus values that may be returned by this method are defined as follows:
    *
    *         <ul>
    *            <li>AuthStatus.SUCCESS when the application request message was successfully validated. The
    *                validated request message is available by calling getRequestMessage on messageInfo.</li>
    *            <li>AuthStatus.SEND_SUCCESS to indicate that validation/processing of the request message
    *                successfully produced the secured application response message (in messageInfo). The secured
    *                response message is available by calling getResponseMessage on messageInfo.</li>
    *            <li>AuthStatus.SEND_CONTINUE to indicate that message validation is incomplete, and that a
    *                preliminary response was returned as the response message in messageInfo. When this status value is
    *                returned to challenge an application request message, the challenged request must be saved by the
    *                authentication module such that it can be recovered when the module’s validateRequest message is
    *                called to process the request returned for the challenge.</li>
    *            <li>AuthStatus.SEND_FAILURE to indicate that message validation failed and that an appropriate
    *                failure response message is available by calling getResponseMessage on messageInfo.</li>
    *         </ul>
    *
    * @throws AuthException When the message processing failed without establishing a failure response
    *                       message (in messageInfo).
    */
   public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, 
         Subject serviceSubject)
   throws AuthException;
}
